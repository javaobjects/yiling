package com.yiling.open.cms.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送群组消息
 */
@NoArgsConstructor
@Data
public class SendGroupMsgForm {

    /**
     * GroupId
     */
    @ApiModelProperty("GroupId")
    private String GroupId;

    /**
     * From_Account
     */
    @ApiModelProperty("GroupId")
    private String From_Account;

    /**
     * Random
     */
    @ApiModelProperty("Random")
    private Integer Random;

    /**
     * msgBody
     */
    @ApiModelProperty("MsgBody")
    private List<MsgBodyForm> MsgBody;
}
