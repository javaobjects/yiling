package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业客户使用产品线表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCustomerLineDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业客户ID（企业客户表id字段）
     */
    private Long customerId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 使用产品线：1-POP 2-B2B
     */
    private Integer useLine;

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
