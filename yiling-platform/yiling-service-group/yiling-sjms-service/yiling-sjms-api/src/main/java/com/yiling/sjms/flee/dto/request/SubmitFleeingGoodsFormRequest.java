package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.form.enums.FormTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/25 0025
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitFleeingGoodsFormRequest extends BaseRequest {

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 9-窜货申报
     */
    private FormTypeEnum formTypeEnum;


    /**
     * 工号
     */
    private String empId;

    /**
     * 发起人姓名
     */
    private String empName;
}
