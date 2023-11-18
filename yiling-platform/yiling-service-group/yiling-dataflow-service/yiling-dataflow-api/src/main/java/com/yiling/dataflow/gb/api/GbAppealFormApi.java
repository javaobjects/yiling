package com.yiling.dataflow.gb.api;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dto.GbAppealFormDTO;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormUpdateExecuteStatusRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractCancleRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractMateFlowRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;

/**
 * 月流向销售数据 API
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
public interface GbAppealFormApi {

    /**
     * 团购数据扣减流向数据
     * @param request
     * @return
     */
    boolean substractMateFlow(GbAppealSubstractMateFlowRequest request);

    Page<GbAppealFormDTO> listPage(QueryGbAppealFormListPageRequest request);

    GbAppealFormDTO getById(Long id);

    /**
     * 根据id、处理状态，更新团购处理状态
     *
     * @param request
     * @return
     */
    boolean updateByIdAndExecuteStatus(GbAppealFormUpdateExecuteStatusRequest request);

    /**
     * 根据团购申诉申请ID，查询关联流向列表分页
     *
     * @param request
     * @return
     */
    Page<GbAppealFormFlowStatisticBO> flowStatisticListPage(QueryGbAppealFormFlowStatisticPageRequest request);

    /**
     * 根据团购处理申请ID查询匹配流向总数量
     *
     * @param appealFormId
     * @return
     */
    BigDecimal getTotalFlowMatchQuantityByAppealFormId(Long appealFormId);

    /**
     * 修改扣减/增加处理结果明细
     *
     * @param request
     * @return
     */
    boolean editGbAppealAllocation(GbAppealFormExecuteEditDetailRequest request);

    /**
     * 添加团购数据，批量保存团购处理
     *
     * @param request
     * @return
     */
    boolean saveList(SaveGbAppealFormRequest request);

    /**
     * 根据团购处理ID、源流向id取消源流向扣减
     *
     * @param request
     * @return
     */
    boolean cancleSubstract(GbAppealSubstractCancleRequest request);

    /**
     * 根据id删除团购处理
     *
     * @param request
     * @return
     */
    boolean deleteAppealForm(DeleteGbAppealFormRequest request);

    /**
     * 匹配源流向
     *
     * @param request
     * @return
     */
    boolean selectFlowForMatch(SaveGbAppealFormSaleReportMatchRequest request);

    /**
     * 删除已匹配源流向
     *
     * @param request
     * @return
     */
    boolean deleteFlowForMatch(GbAppealSubstractCancleRequest request);

    /**
     * 根据formId查询列表
     *
     * @param formIds
     * @return
     */
    List<GbAppealFormDTO> listByFormIds(List<Long> formIds);

    /**
     * 获取可用日程、并校验锁定、非锁团购状态
     *
     * @return
     */
    FlowMonthWashControlDTO washControlGbStatusCheck(String month);

}
