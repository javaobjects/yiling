package com.yiling.basic.sms.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sms_record")
public class SmsRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 短信签名（参考：SmsSignatureEnum）
     */
    private String signature;

    /**
     * 发送状态：1-待发送 2-发送成功 3-发送失败
     */
    private Integer status;

    /**
     * 发送通道标识
     */
    private String channelCode;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
