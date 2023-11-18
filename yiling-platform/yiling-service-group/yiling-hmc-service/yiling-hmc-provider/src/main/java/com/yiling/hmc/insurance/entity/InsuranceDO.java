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
 * C端保险保险表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance")
public class InsuranceDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 保险名称
     */
    private String insuranceName;

    /**
     * 保险状态 1-启用 2-停用
     */
    private Integer status;

    /**
     * 定额类型季度标识
     */
    private String quarterIdentification;

    /**
     * 定额类型年标识
     */
    private String yearIdentification;

    /**
     * 售卖金额
     */
    private BigDecimal payAmount;

    /**
     * 服务商扣服务费比例
     */
    private BigDecimal serviceRatio;

    /**
     * 售卖地址--h5页面链接
     */
    private String url;

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
