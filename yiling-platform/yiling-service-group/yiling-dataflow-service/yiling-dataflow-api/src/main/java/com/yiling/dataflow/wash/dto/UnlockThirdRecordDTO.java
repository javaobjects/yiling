package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockThirdRecordDTO extends BaseDTO {

    private static final long serialVersionUID = 2357807873112097862L;

    /**
     * 客户机构编码
     */
    private Long orgCrmId;

    /**
     * 客户机构名称
     */
    private String customerName;

    /**
     * 月购进额度（万元）
     */
    private BigDecimal purchaseQuota;

    /**
     * 生效业务部门
     */
    private String effectiveDepartment;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 操作人
     */
    private Long lastOpUser;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
