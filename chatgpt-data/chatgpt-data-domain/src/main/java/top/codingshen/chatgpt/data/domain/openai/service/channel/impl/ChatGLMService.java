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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.MessageEntity;
import top.codingshen.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import top.codingshen.chatgpt.data.types.enums.ChatGLMModel;
import top.codingshen.chatgpt.data.types.exception.ChatGPTException;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @Autowired(required = false)
    protected OpenAiSession chatGLMOpenAiSession;

    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {
        if (null == chatGLMOpenAiSession) {
            emitter.send("ChatGLM 通道，模型调用未开启！");
            return;
        }

        // 1. 请求消息
        List<ChatCompletionRequest.Prompt> prompts = new ArrayList<>();

        List<MessageEntity> messages = chatProcess.getMessages();
        MessageEntity messageEntity = messages.remove(messages.size() - 1);

        for (MessageEntity message : messages) {
            String role = message.getRole();
            if (Objects.equals(role, Role.system.getCode())) {
                prompts.add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.system.getCode())
                        .content(message.getContent())
                        .build());

                prompts.add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("Okay")
                        .build());
            } else {
                prompts.add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content(message.getContent())
                        .build());

                prompts.add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("Okay")
                        .build());
            }
        }

        prompts.add(ChatCompletionRequest.Prompt.builder()
                .role(messageEntity.getRole())
                .content(messageEntity.getContent())
                .build());

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
                    } catch (Exception e) {
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
