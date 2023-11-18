package com.yiling.settlement.report.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表的会员订单表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_member_sync")
public class MemberSyncDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    private Integer source;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 实际金额
     */
    private BigDecimal amount;

    /**
     * 订单金额
     */
    private BigDecimal originalPrice;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    private Integer status;

    /**
     * 退款状态：1-未退款 2-已退款
     */
    private Integer refundStatus;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 报表计算状态：1-未计算 2-已计算
     */
    private Integer reportSettStatus;

    /**
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    private Integer reportStatus;

    /**
     * 报表ID
     */
    private Long reportId;

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
