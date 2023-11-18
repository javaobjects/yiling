package com.yiling.hmc.tencent.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MsgContentDTO
 */
@NoArgsConstructor
@Data
public class MsgContentDTO implements java.io.Serializable{
    /**
     * 消息内容。当接收方为 iOS 或 Android 后台在线时，作为离线推送的文本展示。
     */
    private String Text;

    /**
     * 自定义消息数据
     */
    private String Data;

    /**
     * 自定义消息描述信息
     */
    private String Desc;

}
