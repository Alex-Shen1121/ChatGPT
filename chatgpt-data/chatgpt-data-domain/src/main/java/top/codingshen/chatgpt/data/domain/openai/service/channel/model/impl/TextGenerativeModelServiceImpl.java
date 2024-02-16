package top.codingshen.chatgpt.data.domain.openai.service.channel.model.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.service.channel.model.IGenerativeModelService;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.session.OpenAiSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TextGenerativeModelServiceImpl
 * @Description 文本生成
 * @Author alex_shen
 * @Date 2024/2/17 - 01:06
 */
@Slf4j
@Service
public class TextGenerativeModelServiceImpl implements IGenerativeModelService {
    @Autowired(required = false)
    protected OpenAiSession chatGPTOpenAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {
        if (null == chatGPTOpenAiSession) {
            emitter.send("ChatGPT 通道，模型调用未开启！");
            return;
        }

        // 1. 请求消息
        List<Message> messages = chatProcess.getMessages().stream()
                .map(entity ->
                        Message.builder()
                                .role(Constants.Role.valueOf(entity.getRole().toUpperCase()))
                                .content(entity.getContent())
                                .name(entity.getName())
                                .build())
                .collect(Collectors.toList());

        // 2. 封装参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest.builder()
                .stream(true)
                .messages(messages)
                .model(chatProcess.getModel())
                .build();

        // 3. 请求应答
        chatGPTOpenAiSession.completions(chatCompletion, new EventSourceListener() {
            StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<ChatChoice> choices = chatCompletionResponse.getChoices();
                for (ChatChoice chatChoice : choices) {
                    Message delta = chatChoice.getDelta();
                    if (Constants.Role.ASSISTANT.getCode().equals(delta.getRole())) continue;

                    // 应答完成
                    String finishReason = chatChoice.getFinishReason();
                    if (StringUtils.isNoneBlank(finishReason) && "stop".equals(finishReason)) {
                        log.info("问答结果:" + stringBuilder.toString());
                        emitter.complete();
                        break;
                    }

                    // 发送信息
                    try {
                        emitter.send(delta.getContent());
                        stringBuilder.append(delta.getContent());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
