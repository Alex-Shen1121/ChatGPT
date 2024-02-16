package top.codingshen.chatgpt.data.domain.openai.service.channel.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.GenerativeModelVO;
import top.codingshen.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import top.codingshen.chatgpt.data.domain.openai.service.channel.model.IGenerativeModelService;
import top.codingshen.chatgpt.data.domain.openai.service.channel.model.impl.ImageGenerativeModelServiceImpl;
import top.codingshen.chatgpt.data.domain.openai.service.channel.model.impl.TextGenerativeModelServiceImpl;
import top.codingshen.chatgpt.session.OpenAiSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ChatGPTService
 * @Description ChatGPT 服务
 * @Author alex_shen
 * @Date 2024/1/10 - 15:48
 */
@Slf4j
@Service
public class ChatGPTService implements OpenAiGroupService {
    @Autowired(required = false)
    protected OpenAiSession chatGPTOpenAiSession;

    private final Map<GenerativeModelVO, IGenerativeModelService> generativeModelGroup = new HashMap<>();

    public ChatGPTService(ImageGenerativeModelServiceImpl imageGenerativeModelService, TextGenerativeModelServiceImpl textGenerativeModelService) {
        generativeModelGroup.put(GenerativeModelVO.IMAGES, imageGenerativeModelService);
        generativeModelGroup.put(GenerativeModelVO.TEXT, textGenerativeModelService);
    }


    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {
        GenerativeModelVO generativeModelVO = chatProcess.getGenerativeModelVO();
        generativeModelGroup.get(generativeModelVO).doMessageResponse(chatProcess, emitter);
    }
}
