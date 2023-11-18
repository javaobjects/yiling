package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询客户EAS信息分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCustomerEasInfoPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 客户名称（全模糊匹配）
     */
    private String customerName;

    /**
     * EAS编码
     */
    private String easCode;

    /**
     * 执业许可证号/社会信用统一代码（精准匹配）
     */
    private String licenseNumber;
}
