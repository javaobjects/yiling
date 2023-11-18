package com.yiling.sjms.agency.dto.request;

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
public class SubmitAgencyFormRequest extends BaseRequest {

    /**
     * 表单id
     */
    private Long formId;

    /**
     * 1-团购单据 2-机构新增 3-机构修改 4-机构锁定 5-机构解锁 6-机构扩展信息修改 7-机构三者关系变更
     */
    private FormTypeEnum formTypeEnum;


    /**
     * 工号
     */
    private String empId;
}
