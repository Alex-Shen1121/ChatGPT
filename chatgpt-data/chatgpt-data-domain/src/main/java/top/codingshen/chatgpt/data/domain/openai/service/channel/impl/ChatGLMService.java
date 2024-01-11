package top.codingshen.chatgpt.data.domain.openai.service.channel.impl;

import cn.bugstack.chatglm.model.*;
import cn.bugstack.chatglm.session.OpenAiSession;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import top.codingshen.chatgpt.data.types.enums.ChatGLMModel;
import top.codingshen.chatgpt.data.types.exception.ChatGPTException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName ChatGLMService
 * @Description ChatGLM 服务
 * @Author alex_shen
 * @Date 2024/1/10 - 15:48
 */
@Slf4j
@Service
public class ChatGLMService implements OpenAiGroupService {
    @Resource
    protected OpenAiSession chatGLMOpenAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws JsonProcessingException {
       // 1. 请求消息
        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages().stream()
                .map(entity -> ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content(entity.getContent())
                        .build())
                .collect(Collectors.toList());

        // 2. 封装参数
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.valueOf(ChatGLMModel.get(chatProcess.getModel()).name()));
        request.setPrompt(prompts);

        // 3. 请求应答
        chatGLMOpenAiSession.completions(request, new EventSourceListener() {
            StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);

                // 发送信息
                if (EventType.add.getCode().equals(type)) {
                    try {
                        stringBuilder.append(response.getData());
                        emitter.send(response.getData());
                    } catch (Exception e){
                        throw new ChatGPTException(e.getMessage());
                    }
                }

                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getCode().equals(type)) {
                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                }
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                log.info("问答结果:" + stringBuilder.toString());
                emitter.complete();
            }
        });
    }
}
