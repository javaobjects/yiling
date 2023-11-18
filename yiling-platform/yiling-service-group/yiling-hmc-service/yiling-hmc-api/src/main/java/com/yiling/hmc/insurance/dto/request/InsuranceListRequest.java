package com.yiling.hmc.insurance.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险条件查询
 *
 * @author: yong.zhang
 * @date: 2022/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceListRequest extends BaseRequest {

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 保险名称
     */
    private String insuranceName;

    /**
     * 保险状态 1-启用 2-停用
     */
    private Integer status;

    /**
     * 定额类型季度标识
     */
    private String quarterIdentification;

    /**
     * 定额类型年标识
     */
    private String yearIdentification;
}
