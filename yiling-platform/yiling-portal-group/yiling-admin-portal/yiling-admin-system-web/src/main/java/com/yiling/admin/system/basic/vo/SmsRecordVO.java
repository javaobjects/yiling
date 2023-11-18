package com.yiling.admin.system.basic.vo;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.yiling.basic.sms.enums.SmsTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@Data
@ApiModel
public class SmsRecordVO {

    /**
     * 接收人手机号
     */
    @ApiModelProperty("接收人手机号")
    private String mobile;

    /**
     * 短信内容
     */
    @ApiModelProperty("短信内容")
    private String content;

    /**
     * 短信类型（参考：SmsTypeEnum）
     */
    @ApiModelProperty("短信类型（参考：SmsTypeEnum）")
    private String type;

    /**
     * 发送状态：1-待发送 2-发送成功 3-发送失败
     */
    @ApiModelProperty("发送状态：1-待发送 2-发送成功 3-发送失败")
    private Integer status;

    /**
     * 发送通道标识
     */
    @ApiModelProperty("发送通道标识")
    private String channelCode;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public String getType() {
        if (StringUtils.isNotEmpty(type)) {
            return SmsTypeEnum.getFromCode(type).getName();
        }
        return type;
    }
}
