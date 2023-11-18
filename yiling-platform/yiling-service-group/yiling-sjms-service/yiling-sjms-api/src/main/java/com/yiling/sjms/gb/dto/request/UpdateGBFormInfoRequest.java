package com.yiling.sjms.gb.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.sjms.form.enums.FormStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改表单状态和数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGBFormInfoRequest extends BaseRequest {
    /**
     * 表单Id
     */
    private Long id;

    /**
     * 旧状态枚举
     */
    private FormStatusEnum originalStatus;
    /**
     * 新状态枚举
     */
    private FormStatusEnum newStatus;

    /**
     * 所属流程ID
     */
    private String flowId;

    /**
     * 所属流程名称
     */
    private String flowName;

    /**
     * 流程模板ID
     */
    private String flowTplId;

    /**
     * 流程版本
     */
    private String flowVersion;

    /**
     * 提交审批时间
     */
    private Date submitTime;

    /**
     * 审批通过时间
     */
    private Date approveTime;


    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 表单类型
     */
    private Integer formType;

}



