package com.yiling.user.enterprise.entity;

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
 * 企业客户信息
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_customer")
public class EnterpriseCustomerDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 数据来源：1-后台导入 2-ERP对接 3-开通POP 4-协议生成 5-线上采购
     */
    private Integer source;

    /**
     * 采购次数
     */
    private Integer purchaseNumber;

    /**
     * 最后购买时间
     */
    private Date lastPurchaseTime;

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
