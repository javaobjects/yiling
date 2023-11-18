package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IM 群组信息
 *
 * @author: fan.shen
 * @data: 2023/05/12
 */
@Data
public class IMGroupInfoVO {

    @ApiModelProperty("会话id")
    private String conversationID;

    @ApiModelProperty("医生头像")
    private String groupDocPic;

    @ApiModelProperty("用户头像")
    private String groupUserPic;

    @ApiModelProperty("医生名称")
    private String groupDocName;

    @ApiModelProperty("用户名称")
    private String groupUserName;
}
