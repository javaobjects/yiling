package com.yiling.hmc.wechat.entity;

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
 * C端拿药计划明细表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_fetch_plan_detail")
public class InsuranceFetchPlanDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单支付记录表id
     */
    private Long recordPayId;

    /**
     * eId
     */
    private Long eid;


    /**
     * hmcGoodsId
     */
    private Long hmcGoodsId;

    /**
     * goodsId
     */
    private Long goodsId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 保司给以岭结算价格
     */
    private BigDecimal settlePrice;

    /**
     * 以岭给终端结算价格
     */
    private BigDecimal terminalSettlePrice;

    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;

    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;

    /**
     * 每月拿药量
     */
    private Long perMonthCount;

    /**
     * 规格信息
     */
    private String specificInfo;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;


}
