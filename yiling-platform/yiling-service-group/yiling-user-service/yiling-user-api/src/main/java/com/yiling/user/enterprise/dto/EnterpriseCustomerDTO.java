package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业客户 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCustomerDTO extends BaseDTO {

    private static final long serialVersionUID = -9044217305740097365L;

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
     * 数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成
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
