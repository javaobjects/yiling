package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送群组消息
 */
@NoArgsConstructor
@Data
public class SendGroupMsgRequest implements java.io.Serializable{

    /**
     * groupId
     */
    private String GroupId;

    /**
     * fromAccount
     */
    private String From_Account;

    /**
     * random
     */
    private Integer Random;

    /**
     * msgBody
     */
    private List<MsgBodyDTO> MsgBody;
}
