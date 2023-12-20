package top.codingshen.chatgpt.domain.receive.model;

import lombok.Data;

import java.util.Date;
@Data
public class BehaviorMatter {

    private String openId;
    private String fromUserName;
    private String msgType;
    private String content;
    private String event;
    private Date createTime;

}
