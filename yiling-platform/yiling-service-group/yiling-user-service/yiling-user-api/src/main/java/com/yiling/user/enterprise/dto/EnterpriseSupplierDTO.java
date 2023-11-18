package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 供应商管理表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSupplierDTO extends BaseDTO {

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
