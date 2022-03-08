package com.bin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class Message implements CommunityConstant{
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;

    public Message() {
        status = MESSAGE_STATE_UNREAD;
    }
}
