package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSalesAppealFormRequest extends BaseRequest {

    /**
     * 主流程表单id
     */
    private List<SaveSalesAppealFormUploadRequest> saveSalesAppealDetailForms;

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
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    private Integer appealType;
}
