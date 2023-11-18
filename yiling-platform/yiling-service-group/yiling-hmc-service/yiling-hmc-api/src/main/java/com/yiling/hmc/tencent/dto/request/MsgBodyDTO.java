package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MsgBodyDTO
 */
@NoArgsConstructor
@Data
public class MsgBodyDTO implements java.io.Serializable{
    /**
     * TIM 消息对象类型，目前支持的消息对象包括： TIMTextElem（文本消息） TIMLocationElem（位置消息） TIMFaceElem（表情消息） TIMCustomElem（自定义消息） TIMSoundElem（语音消息） TIMImageElem（图像消息） TIMFileElem（文件消息） TIMVideoFileElem（视频消息）
     */
    private String MsgType;
    /**
     * 对于每种 MsgType 用不同的 MsgContent 格式，具体可参考 消息格式描述
     */
    private MsgContentDTO MsgContent;
}
