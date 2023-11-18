package com.yiling.hmc.insurance.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险新增和修改
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSaveRequest extends BaseRequest {

    /**
     * 保险id
     */
    private Long id;

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

    /**
     * 售卖金额
     */
    private BigDecimal payAmount;

    /**
     * 服务商扣服务费比例
     */
    private BigDecimal serviceRatio;

    /**
     * 售卖地址--h5页面链接
     */
    private String url;

    /**
     * 保险商品明细新增信息
     */
    private List<InsuranceDetailSaveRequest> insuranceDetailSaveList;
}
