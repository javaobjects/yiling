package com.yiling.sjms.flow.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补传月流向
 * @author: gxl
 * @date: 2023/6/25 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMonthFlowFormRequest extends BaseRequest {

    private static final long serialVersionUID = 2101457798916109977L;
    /**
     * 子表单
     */
    private List<SaveSubFormRequest> subForms;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 发起人姓名
     */
    private String empId;

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 申诉类型 1补传月流向
     */
    private Integer appealType;


    /**
     * 申诉金额
     */
    private BigDecimal appealAmount;



    /**
     * 申诉描述
     */
    private String appealDescribe;

    /**
     * 附件
     */
    private String appendix;

}
