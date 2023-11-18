package com.yiling.ih.patient.dto;

import lombok.Data;

import java.util.Date;

/**
 * 获取IM聊天历史记录
 *
 * author: fan.shen
 * data: 2023/01/31
 */
@Data
public class HmcIMConversationListDTO implements java.io.Serializable{

    // @ApiModelProperty("群组id")
    private String groupId;

    // @ApiModelProperty("医生id")
    private Integer doctorId;

    // @ApiModelProperty("医生头像")
    private String avatar;

    // @ApiModelProperty("医生姓名")
    private String doctorName;

    // @ApiModelProperty("医生职称")
    private String professionName;

    // @ApiModelProperty(value = "科室名称")
    private String departmentName;

    // @ApiModelProperty("最后一条消息类型")
    private String messageType;

    // @ApiModelProperty("最后一条消息文本")
    private String messageContent;

    // @ApiModelProperty("最后一条消息时间")
    private Date messageTime;

    // @ApiModelProperty("未读数")
    private Integer unreadCount;

    // 最后一条消息类型data
    private String messageData;
}
