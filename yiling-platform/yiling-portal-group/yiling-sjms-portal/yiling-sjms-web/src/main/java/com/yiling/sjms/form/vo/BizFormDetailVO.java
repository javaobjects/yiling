package com.yiling.sjms.form.vo;

import java.util.List;

import com.yiling.sjms.workflow.vo.AuditHistoryVO;
import com.yiling.sjms.workflow.vo.WfActHistoryVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务表单详情 VO
 *
 * @author: xuan.zhou
 * @date: 2023/3/1
 */
@Data
public class BizFormDetailVO<T> {

    /**
     * 业务表单基础信息
     */
    @ApiModelProperty("业务表单基础信息")
    private FormVO basicInfo;

    private T bizInfo;

    /**
     * 流程操作历史记录列表
     */
    @ApiModelProperty("流程操作历史记录列表")
    private List<WfActHistoryVO> wfActHistoryList;

    @ApiModelProperty("流程审批记录列表")
    private List<AuditHistoryVO> auditHistoryVOList;

    @ApiModelProperty("按钮控制")
    private OperateControlVO operateControlVO;
}
