package com.yiling.sjms.manor.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/5/10
 */
@Data
public class ManorChangeFormRequest extends BaseRequest {
    private static final long serialVersionUID = 5217193345176495196L;
    /**
     * 医院id
     */
    private Long crmEnterpriseId;

    /**
     * form表主键
     */
    private Long formId;


    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 调整原因
     */
    private String adjustReason;

    private String remark;

    private  List<HospitalManorChangeRequest> list;

    /**
     * 是否是修改请求
     */
    private Boolean isUpdate;
}