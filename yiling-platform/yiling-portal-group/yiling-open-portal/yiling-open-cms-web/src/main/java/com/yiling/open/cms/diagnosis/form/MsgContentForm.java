package com.yiling.open.cms.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MsgContentDTO
 */
@NoArgsConstructor
@Data
public class MsgContentForm implements java.io.Serializable {
    /**
     * 消息内容。当接收方为 iOS 或 Android 后台在线时，作为离线推送的文本展示。
     */
    @ApiModelProperty("Text")
    private String Text;

    /**
     * 自定义消息数据
     */
    @ApiModelProperty("Data")
    private String Data;

    /**
     * 自定义消息描述信息
     */
    @ApiModelProperty("Desc")
    private String Desc;

}
