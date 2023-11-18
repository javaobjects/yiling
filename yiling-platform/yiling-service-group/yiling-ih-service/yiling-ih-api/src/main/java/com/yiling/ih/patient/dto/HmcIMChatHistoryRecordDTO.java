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
public class HmcIMChatHistoryRecordDTO implements java.io.Serializable{

    // // ApiModelProperty("id")
    private Integer id;

    // // ApiModelProperty("data")
    private String data;

    // // ApiModelProperty("desc")
    private String desc;

    // // ApiModelProperty("ext")
    private String ext;

    // ApiModelProperty("flow")
    private String flow;

    // ApiModelProperty("发送时间")
    private Date time;

    // ApiModelProperty("消息类型text文本;patient患者信息类消息;prescription处方药消息 ;image图片消息 ;audio音频类型 ;time时间类型 ;callTime音视频拨打时长 ;endDiagnosisRecord问诊结束横线 ;imageTextprompt:下图文订单发送的提醒消息;doctorCommunicateTips:医生创建赠送患者问诊单医生侧的提示语;patientCommunicateTips:医生创建赠送患者问诊单患者侧的提示语")
    private String type;

    // ApiModelProperty("to")
    private String to;

    // ApiModelProperty("avatar")
    private String avatar;

    // ApiModelProperty("消息为文本时的内容")
    private String content;

    // ApiModelProperty("图片或语音或视频的url,type为image/audio时有值")
    private String url;

    // ApiModelProperty("患者姓名,type为patient时有值")
    private String patientName;

    // ApiModelProperty("患者主诉描述,type为patient时有值")
    private String patientDescription;

    // ApiModelProperty("患者主诉图片集合，使用|隔开,type为patient时有值")
    private String patientImgs;

    // ApiModelProperty("处方id,type为prescription时有值")
    private Integer prescriptionId;

    // ApiModelProperty("处方药文本{title:string,usage:string}[] ,type为prescription时有值")
    private String prescriptionContent;

    // ApiModelProperty("患者手机号,type为patient时有值")
    private String patientMobile;

    // ApiModelProperty("患者出生日期,type为patient时有值")
    private String patientBirthday;

    // ApiModelProperty("患者性别1 : 男 ， 0 : 女,type为patient时有值")
    private Integer patientGender;

    // ApiModelProperty("问诊记录id")
    private Integer diagnosisRecordId;

    // ApiModelProperty("patient消息类型 1图文 2音视频")
    private Integer patientType;

    // ApiModelProperty("type为patient时有值,就诊时间")
    private String diagnosisTime;

    // ApiModelProperty("type为audio时有值,秒数")
    private Integer second;

    private Integer imageHeight;

    private Integer imageWidth;


}
