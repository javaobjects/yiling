package com.yiling.sjms.flow.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.dto.request.DeleteFormRequest;
import com.yiling.sjms.flow.dto.request.QueryMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveMonthFlowFormRequest;
import com.yiling.sjms.flow.dto.request.SaveSubFormRequest;
import com.yiling.sjms.flow.dto.request.SubmitMonthFlowFormRequest;
import com.yiling.sjms.flow.entity.MonthFlowFormDO;

/**
 * <p>
 * 补传月流向表单 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-06-25
 */
public interface MonthFlowFormService extends BaseService<MonthFlowFormDO> {

    /**
     * 补传月流向表单保存
     * @param request
     */
    Long save(SaveMonthFlowFormRequest request);

    /**
     * 提交
     * @param request
     */
    void submit(SubmitMonthFlowFormRequest request);

    /**
     * 流程申请-补传月流向列表
     * @param request
     * @return
     */
    List<MonthFlowFormDTO> list(QueryMonthFlowFormRequest request);

    /**
     * 根据id删除
     * @param request
     */
    void deleteById(DeleteFormRequest request);

    /**
     * 根据导入日志表id查询
     * @param recordId
     * @return
     */
    MonthFlowFormDTO getByRecordId(Long recordId);

    /**
     * 更新导入状态
     * @param request
     */
    void updateFlowMonthRecord(SaveSubFormRequest request);

    /**
     * 审批通过写入流向数据生成清洗任务
     * @param id
     */
    void approveTo(Long id);

    /**
     * 根据文件名称查询
     *
     * @param fileName
     * @return
     */
    Boolean getByFileName(String fileName);
}
