package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateUnlockFlowWashSaleRequest extends BaseRequest {

    private static final long serialVersionUID = 2063831600409169286L;

    private Integer year;

    private Integer month;

    /**
     * 经销商crmid
     */
    private Long crmId;

    /**
     * 客户原始名称
     */
    private String customerName;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 分类来源 1-规则 2-人工
     */
    private Integer distributionSource;

    /**
     * 非锁客户分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;


}
