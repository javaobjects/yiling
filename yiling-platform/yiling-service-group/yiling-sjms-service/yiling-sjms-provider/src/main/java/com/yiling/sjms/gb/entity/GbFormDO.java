package com.yiling.sjms.gb.entity;

import java.math.BigDecimal;
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
 * 团购表单
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_form")
public class GbFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 来源团购编号（操作类型为取消时）
     */
    private String srcGbNo;

    /**
     * 业务类型：1-提报 2-取消 3-费用申请
     */
    private Integer bizType;

    /**
     * 是否关联取消: 1-否 2-是
     */
    private Integer cancelFlag;

    /**
     * 是否复核: 1-否 2-是
     */
    private Integer reviewStatus;

    /**
     * 复核意见
     */
    private String reviewReply;


    /**
     * 复核时间
     */
    private Date reviewTime;

    /**
     * 团购费用申请原因
     */
    private String feeApplicationReply;
}
