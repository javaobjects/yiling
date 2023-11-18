package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加客户 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerRequest extends BaseRequest {

    private static final long serialVersionUID = -2339229218309563990L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户企业ID
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
     * 数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成 5-线上采购
     */
    private Integer source;

    /**
     * 是否建立采购关系
     */
    private boolean addPurchaseRelationFlag = false;
}
