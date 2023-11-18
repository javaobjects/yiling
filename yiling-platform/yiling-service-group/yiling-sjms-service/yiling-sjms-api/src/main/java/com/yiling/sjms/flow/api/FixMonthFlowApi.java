package com.yiling.sjms.flow.api;

import java.util.List;

import com.yiling.sjms.flow.dto.MonthFlowExtFormDTO;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.DeleteFormRequest;
import com.yiling.sjms.flow.dto.request.QueryMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;
import com.yiling.sjms.flow.dto.request.SubmitMonthFlowFormRequest;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
public interface FixMonthFlowApi {
    /**
     * 补传月流向表单保存
     * @param request
     * @return
     */
     Long save(SaveMonthFlowFormRequest request);

    /**
     * 提交审批
     * @param request
     */
    void submit(SubmitMonthFlowFormRequest request);

    /**
     * 流程申请-补传月流向列表
     * @param formId
     * @return
     */
    List<MonthFlowFormDTO> list(QueryMonthFlowFormRequest request);

    /**
     * 根据id删除
     * @param request
     */
    void deleteById(DeleteFormRequest request);

    /**
     * 根据导入任务id 查询
     * @param recordId
     * @return
     */
    MonthFlowFormDTO getByRecordId(Long recordId);

    void updateFlowMonthRecord(SaveSubFormRequest request);

    /**
     * 查看附件
     *
     * @param formId 文件名称
     * @return 附件信息
     */
    MonthFlowExtFormDTO queryAppendix(Long formId);

    /**
     * 根据文件名称查询
     * @param fileName
     * @return
     */
    Boolean getByFileName(String fileName);
}