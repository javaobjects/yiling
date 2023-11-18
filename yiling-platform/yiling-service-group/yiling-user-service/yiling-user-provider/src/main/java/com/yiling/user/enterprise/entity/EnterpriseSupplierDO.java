package com.yiling.user.enterprise.entity;

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
 * 供应商管理表
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_supplier")
public class EnterpriseSupplierDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 采购商ID
     */
    private Long customerEid;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * ERP供应商名称
     */
    private String erpName;

    /**
     * ERP编码
     */
    private String erpCode;

    /**
     * ERP内码
     */
    private String erpInnerCode;

    /**
     * 采购员编码
     */
    private String buyerCode;

    /**
     * 采购员名称
     */
    private String buyerName;

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
