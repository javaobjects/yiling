package com.yiling.activity.web.wx.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 生成微信小程序码参数
 *
 * @author fan.shen
 * @date: 2022/4/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class WxQrCodeForm extends BaseForm {
//
//    @NotNull(message = "类型不能为空")
//    @ApiModelProperty(value = "类型 1-药品福利，2-用药指导，3-用药指导详情，4-保单详情", required = true)
//    private WxQrCodeTypeEnum typeEnum;
//
//    @NotNull(message = "url不能为空")
//    @ApiModelProperty(value = "url", required = true)
//    private String url;
//
//    @ApiModelProperty(value = "注册来源 2-员工二维码，3-药盒二维码")
//    private String source;
//
//    @ApiModelProperty(value = "员工所属企业id")
//    private String eId;
//
//    @ApiModelProperty(value = "员工id")
//    private String userId;
//
//    @ApiModelProperty(value = "订单id")
//    private String orderId;
//
//    @ApiModelProperty(value = "保单id")
//    private String insuranceRecordId;
//
    @ApiModelProperty(value = "要打开的小程序版本,正式版为 release，体验版为 trial，开发版为 develop")
    private String envVersion;

    @ApiModelProperty(value = "链接携带key")
    private String key;
}
