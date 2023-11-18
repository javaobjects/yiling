package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFleeingFormDraftRequest extends BaseRequest {

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 申诉描述
     */
    private String describe;

    /**
     * 附件
     */
    private String appendix;

    /**
     * 附件
     */
    private List<SaveAppendixRequest> appendixList;

    /**
     * 发起人姓名
     */
    private String empName;
}
