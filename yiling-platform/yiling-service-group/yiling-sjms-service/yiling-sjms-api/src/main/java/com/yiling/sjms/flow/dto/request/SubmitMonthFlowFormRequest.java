package com.yiling.sjms.flow.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.flee.dto.request.SaveAppendixRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitMonthFlowFormRequest extends BaseRequest {


    private static final long serialVersionUID = 5783910509391040767L;
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
     * 姓名
     */
    private String empName;

    /**
     * 工号
     */
    private String empId;

    /**
     * 附件
     */
    private List<SaveAppendixRequest> appendixList;

}
