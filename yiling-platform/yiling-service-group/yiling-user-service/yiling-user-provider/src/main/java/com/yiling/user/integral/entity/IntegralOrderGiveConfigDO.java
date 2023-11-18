package com.yiling.user.integral.entity;

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
 * 订单送积分配置表
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("integral_order_give_config")
public class IntegralOrderGiveConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 商家范围：1-全部商家 2-指定商家
     */
    private Integer merchantScope;

    /**
     * 商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
     */
    private Integer goodsScope;

    /**
     * 客户范围：1-全部客户 2-指定客户 3-指定客户范围
     */
    private Integer customerScope;

    /**
     * 指定客户范围的企业类型：1-全部类型 2-指定类型
     */
    private Integer enterpriseType;

    /**
     * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
     */
    private Integer userType;

    /**
     * 是否区分支付方式：1-全部支付方式 2-指定支付方式
     */
    private Integer paymentMethodFlag;

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
