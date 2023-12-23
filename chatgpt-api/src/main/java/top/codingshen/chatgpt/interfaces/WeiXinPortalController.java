package top.codingshen.chatgpt.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import top.codingshen.chatgpt.application.IWeiXinValidateService;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.domain.receive.model.BehaviorMatter;
import top.codingshen.chatgpt.domain.receive.model.MessageTextEntity;
import top.codingshen.chatgpt.infrastructure.util.XmlUtil;
import top.codingshen.chatgpt.session.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;
import top.codingshen.chatgpt.session.OpenAiSessionFactory;
import top.codingshen.chatgpt.session.defaults.DefaultOpenAiSessionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName WeiXinPortalController
 * @Description 微信公众号，请求处理服务
 * @Author alex_shen
 * @Date 2023/11/26 - 21:38
 */
@RestController
@RequestMapping("/wx/portal/{appid}")
public class WeiXinPortalController {
    @Value("${wx.config.originalid}")
    private String originalId;


    private Logger logger = LoggerFactory.getLogger(WeiXinPortalController.class);

    @Resource
    private IWeiXinValidateService weiXinValidateService;

    private OpenAiSession openAiSession;

    private Map<String, String> chatGPTMap = new ConcurrentHashMap<>();

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    public WeiXinPortalController() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://api.openai.com/");
        configuration.setApiKey("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");
        configuration.setAuthToken("xxx");
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
        logger.info("开始 openAiSession");
    }

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            logger.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAllBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法,请核实!");
            }
            boolean check = weiXinValidateService.checkSign(signature, timestamp, nonce);
            logger.info("微信公众号验签信息{}完成 check:{}", appid, check);

            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            logger.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            return null;
        }
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam(value = "signature") String signature,
                       @RequestParam(value = "timestamp") String timestamp,
                       @RequestParam(value = "nonce") String nonce,
                       @RequestParam(value = "openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        // todo: 如果 5 秒内获得 chatgpt-api 的答复,直接回复,不需要异步调用
        // todo: 如果有常见的问题和标准答案,直接存在 map 中,直接调取
        // todo: 发送发送相同问题太复杂了,可以尝试使用任意消息.
        try {
            logger.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);

            // 解析接收到的文本信息
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);

            // 异步任务
            // 第一次发送消息 / 消息未处理完成
            if (chatGPTMap.get(message.getContent().trim()) == null ||
                    "NULL".equals(chatGPTMap.get(message.getContent().trim()))) {

                // 反馈文本信息
                MessageTextEntity res = new MessageTextEntity();
                res.setToUserName(openid);
                res.setFromUserName(originalId);
                res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
                res.setMsgType("text");
                res.setContent("消息处理中,请再回复我一句[" + message.getContent().trim() + "]");

                // 如果未处理该消息则异步调用
                if (chatGPTMap.get(message.getContent().trim()) == null) {
                    doChatGPTTask(message.getContent().trim());
                }

                return XmlUtil.beanToXml(res);
            }

            // 反馈文本信息
            MessageTextEntity res = new MessageTextEntity();
            res.setToUserName(openid);
            res.setFromUserName(originalId);
            res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
            res.setMsgType("text");
            res.setContent(chatGPTMap.get(message.getContent().trim()));

            String result = XmlUtil.beanToXml(res);
            chatGPTMap.remove(message.getContent().trim());
            logger.info("接收微信公众号信息请求{}完成 {}", openid, result);

            return result;
        } catch (Exception e) {
            logger.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }

    public void doChatGPTTask(String content) {
        chatGPTMap.put(content, "NULL");
        taskExecutor.execute(() -> {
            // openai 请求
            // 1. 创建参数
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content(content).build()))
                    .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                    .build();

            // 2. 发起请求
            ChatCompletionResponse chatCompletionResponse = openAiSession.completions(chatCompletionRequest);

            // 3. 解析结果
            StringBuilder messages = new StringBuilder();
            chatCompletionResponse.getChoices().forEach(e -> {
                messages.append(e.getMessage().getContent());
            });
            chatGPTMap.put(content, messages.toString());
            logger.info("消息处理完成,已存入 chatGPTMap 中.");
        });
    }

}
