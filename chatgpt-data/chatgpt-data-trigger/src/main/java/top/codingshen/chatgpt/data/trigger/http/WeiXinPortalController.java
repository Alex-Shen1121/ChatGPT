package top.codingshen.chatgpt.data.trigger.http;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.codingshen.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import top.codingshen.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import top.codingshen.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import top.codingshen.chatgpt.data.domain.weixin.service.IWeiXinValidateService;
import top.codingshen.chatgpt.data.types.sdk.weixin.XmlUtil;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @ClassName IWeiXinValidateService
 * @Description description 微信公众号，验签和请求应答
 * @Author alex_shen
 * @Date 2023/12/26 - 21:27
 */

@Slf4j
@RestController
@RequestMapping("/api/${app.config.api-version}/wx/portal/{appid}")
public class WeiXinPortalController {
    @Value("${wx.config.originalid}")
    private String originalId;

    @Resource
    private IWeiXinValidateService weiXinValidateService;

    @Resource
    private IWeiXinBehaviorService weiXinBehaviorService;

    /**
     * 处理微信服务器发来的get请求
     * @param appid 微信端AppID
     * @param signature 微信端发来的签名
     * @param timestamp 微信端发来的时间戳
     * @param nonce 微信端发来的随机字符串
     * @param echostr 微信端发来的验证字符串
     * @return
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String validate(@PathVariable String appid,
                           @RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            log.info("微信公众号验签信息{}开始 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr);
            if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
                throw new IllegalArgumentException("请求参数非法，请核实!");
            }
            boolean check = weiXinValidateService.checkSign(signature, timestamp, nonce);
            log.info("微信公众号验签信息{}完成 check：{}", appid, check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            log.error("微信公众号验签信息{}失败 [{}, {}, {}, {}]", appid, signature, timestamp, nonce, echostr, e);
            return null;
        }
    }


    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@PathVariable String appid,
                       @RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        try {
            log.info("接收微信公众号信息请求{}开始 {}", openid, requestBody);
            // 消息转换
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);

            // 构建实体
            UserBehaviorMessageEntity entity = UserBehaviorMessageEntity.builder()
                    .openId(openid)
                    .fromUserName(message.getFromUserName())
                    .msgType(message.getMsgType())
                    .content(StringUtils.isBlank(message.getContent()) ? null : message.getContent().trim())
                    .event(message.getEvent())
                    .createTime(new Date(Long.parseLong(message.getCreateTime()) * 1000L))
                    .build();

            // 受理消息
            String result = weiXinBehaviorService.acceptUserBehavior(entity);
            log.info("接收微信公众号信息请求{}完成 {}", openid, result);
            return result;
        } catch (Exception e) {
            log.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }
}

