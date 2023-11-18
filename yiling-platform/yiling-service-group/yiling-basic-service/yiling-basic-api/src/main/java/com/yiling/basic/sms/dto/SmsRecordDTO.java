package com.yiling.basic.sms.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SmsRecordDTO extends BaseDTO {

    /**
     * 接收人手机号
     */
    private String mobile;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 短信类型（参考：SmsTypeEnum）
     */
    private String type;

    /**
     * 发送状态：1-待发送 2-发送成功 3-发送失败
     */
    private Integer status;

    /**
     * 发送通道标识
     */
    private String channelCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updateTime;

}
