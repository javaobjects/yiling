package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/11 0011
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySalesAppealPageRequest extends QueryPageListRequest {

    /**
     * 申报编号
     */
    private String code;

    /**
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    private Integer appealType;

    /**
     * 状态 ：字典gb_form_status
     */
    private Integer status;

    /**
     * formId
     */
    private Long formId;

    /**
     * formId
     */
    private String empId;
}
