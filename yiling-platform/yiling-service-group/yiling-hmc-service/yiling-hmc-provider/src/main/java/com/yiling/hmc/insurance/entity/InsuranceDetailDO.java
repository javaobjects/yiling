package com.yiling.hmc.insurance.entity;

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
 * 保险商品明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_detail")
public class InsuranceDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 保险id
     */
    private Long insuranceId;

    /**
     * 药品id
     */
    private Long controlId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 保司跟以岭的结算单价
     */
    private BigDecimal settlePrice;

    /**
     * 每月1次，每次拿多少盒
     */
    private Long monthCount;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;

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
