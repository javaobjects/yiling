package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 患者给医生发送消息
 */
@NoArgsConstructor
@Data
public class PatientSendMsgToDoctorRequest {

    /**
     * 1：把消息同步到 From_Account 在线终端和漫游上 2：消息不同步至 From_Account 若不填写默认情况下会将消息存 From_Account 漫游
     */
    private Integer SyncOtherMachine;

    /**
     * 消息发送方 UserID（用于指定发送消息方帐号）
     */
    private String From_Account;

    /**
     * 消息接收方 UserID
     */
    private String To_Account;

    /**
     * (选填)消息序列号（32位无符号整数），后台会根据该字段去重及进行同秒内消息的排序，详细规则请看本接口的功能说明。若不填该字段，则由后台填入随机数
     */
    private Integer MsgSeq;

    /**
     * 消息随机数（32位无符号整数），后台用于同一秒内的消息去重。请确保该字段填的是随机
     */
    private Integer MsgRandom;

    /**
     * 消息内容，具体格式请参考 消息格式描述（注意，一条消息可包括多种消息元素，MsgBody 为 Array 类型）
     */
    private List<MsgBodyDTO> MsgBody;

    /**
     * 消息自定义数据（云端保存，会发送到对端，程序卸载重装后还能拉取到）
     */
    private String CloudCustomData;

}
