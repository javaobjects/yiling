package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加企业客户产品线 Request
 *
 * @author: lun.yu
 * @date: 2021/11/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerLineRequest extends BaseRequest {

    /**
     * 企业客户ID（企业客户表id字段）
     */
    @NotNull
    private Long customerId;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 企业名称
     */
    @NotNull
    private String ename;

    /**
     * 客户ID
     */
    @NotNull
    private Long customerEid;

    /**
     * 客户名称
     */
    @NotNull
    private String customerName;

    /**
     * 使用产品线：1-POP 2-B2B
     */
    @NotNull
    private Integer useLine;
}
