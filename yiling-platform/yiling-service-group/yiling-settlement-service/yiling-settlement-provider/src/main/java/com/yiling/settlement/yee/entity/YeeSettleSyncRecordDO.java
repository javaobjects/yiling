package com.yiling.settlement.yee.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 易宝结算记录同步表
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("yee_settle_sync_record")
public class YeeSettleSyncRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 发起方商编
     */
    private String parentMerchantNo;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 结算订单号
     */
    private String summaryNo;

    /**
     * 应结金额
     */
    private BigDecimal settleAmount;

    /**
     * 结算手续费
     */
    private BigDecimal realFee;

    /**
     * 结算到账金额
     */
    private BigDecimal realAmount;

    /**
     * 结算订单状态： 1-结算成功 2-失败 3-待处理 4-待出款 5-结算异常 6-银行处理中
     */
    private Integer status;

    /**
     * 		结算订单状态描述
     */
    private String statusDesc;

    /**
     * 结算产品
     */
    private String settleType;

    /**
     * 同步时间
     */
    private Date syncTime;

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


}
