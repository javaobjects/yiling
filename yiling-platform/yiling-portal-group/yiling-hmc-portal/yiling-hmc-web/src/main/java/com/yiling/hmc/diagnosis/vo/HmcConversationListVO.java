package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * IM 会话列表
 *
 * @author: fan.shen
 * @data: 2023/06/21
 */
@Data
@Accessors(chain = true)
public class HmcConversationListVO {


    @ApiModelProperty("群组id")
    private String groupId;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("医生头像")
    private String avatar;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("医生职称")
    private String professionName;

    @ApiModelProperty(value = "科室名称")
    private String departmentName;

    @ApiModelProperty("最后一条消息类型")
    private String messageType;

    @ApiModelProperty("最后一条消息文本")
    private String messageContent;

    @ApiModelProperty("最后一条消息时间")
    private Date messageTime;

    @ApiModelProperty("未读数")
    private Integer unreadCount;

    @ApiModelProperty("最后一条消息类型data")
    private String messageData;
}
