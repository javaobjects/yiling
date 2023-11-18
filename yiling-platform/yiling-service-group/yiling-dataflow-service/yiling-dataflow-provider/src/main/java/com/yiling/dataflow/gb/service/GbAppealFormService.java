package com.yiling.dataflow.gb.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.bo.GbAppealFormEsbInfoBO;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormUpdateExecuteStatusRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractCancleRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.dataflow.gb.entity.GbAppealFormDO;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

/**
 * <p>
 * 流向团购申诉申请表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
public interface GbAppealFormService extends BaseService<GbAppealFormDO> {

    List<GbAppealFormDO> getListByGbOrderId(Long gbOrderId);

    List<GbAppealFormDO> getListByGbOrderIdList(List<Long> gbOrderIds);

    /**
     * 团购处理源流向扣减
     *
     * @param gbAppealFormDO
     * @param gbAppealFlowRelatedDOList
     * @param gbAppealFlowStatisticDOList
     * @param flowWashSaleReportMap
     * @param gbAppealFormEsbInfoBO
     * @param execType
     * @param opUserId
     * @param opTime
     * @return
     */
    boolean substractMateFlow(GbAppealFormDO gbAppealFormDO, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedDOList, List<GbAppealFlowStatisticDO> gbAppealFlowStatisticDOList, Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO, Integer execType, Long opUserId, Date opTime);

    Page<GbAppealFormDO> listPage(QueryGbAppealFormListPageRequest request);

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
    boolean editGbAppealAllocation(GbAppealFormExecuteEditDetailRequest request, GbAppealFormDO gbAppealFormDO, GbAppealAllocationDO appealAllocation, GbAppealFlowStatisticDO appealFlowStatistic, GbAppealFlowRelatedDO appealFlowRelated);

    /**
     * 添加团购数据，批量保存团购处理
     *
     * @param request
     * @return
     */
    boolean saveList(SaveGbAppealFormRequest request, FlowMonthWashControlDTO flowMonthWashControlDTO, List<GbOrderDO> gbOrderList, Map<Long, String> crmEnterpriseMap, Map<String, EsbEmployeeDTO> esbEmployeeMap);

    /**
     * 根据团购处理ID、源流向id取消源流向扣减
     *
     * @param request
     * @return
     */
    boolean cancleSubstract(GbAppealSubstractCancleRequest request, List<GbAppealAllocationDO> appealAllocationList, GbAppealFlowRelatedDO appealFlowRelated, GbAppealFlowStatisticDO appealFlowStatistic);

    /**
     * 根据id删除团购处理
     *
     * @param request
     * @return
     */
    boolean deleteAppealForm(DeleteGbAppealFormRequest request, GbAppealFormDO gbAppealFormDO, GbOrderDO gbOrderDO, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedList,
                             List<GbAppealFlowStatisticDO> gbAppealFlowStatisticList, List<GbAppealAllocationDO> gbAppealAllocationList);

    /**
     * 匹配源流向
     *
     * @param request
     * @return
     */
    boolean selectFlowForMatch(SaveGbAppealFormSaleReportMatchRequest request, GbAppealFormDO appealFormOld, Map<Long, FlowWashSaleReportDTO> flowSaleReportMap);

    /**
     * 源流向匹配删除（未扣减的）
     * 根据团购处理id、关联表id，删除关联源流向、更新团购处理匹配源流向数量
     *
     * @param appealFormId
     * @param appealFlowRelatedId
     * @param opUserId
     * @param opTime
     * @return
     */
    boolean deleteAppealFlowRelatedForNotSubstract(Long appealFormId, Long flowWashId, Long appealFlowRelatedId, Long opUserId, Date opTime);

    /**
     * 源流向匹配删除（已扣减的）
     * 根据团购处理id、关联表id、源流向扣减统计id、扣减增加结果id列表，
     * 归还扣减数量、删除扣减增加结果、删除关联源流向、更新团购处理匹配源流向数量
     *
     * @param appealFormId
     * @param appealFlowRelatedId
     * @param appealFlowStatisticId
     * @param appealAllocationIds
     * @param returnQuantity
     * @param opUserId
     * @param opTime
     * @return
     */
    boolean deleteAppealFlowRelatedForSubstract(Long appealFormId, Long flowWashId, Long appealFlowRelatedId, Long appealFlowStatisticId, List<Long> appealAllocationIds, BigDecimal returnQuantity, Long opUserId, Date opTime);

    /**
     * 根据formId查询列表
     *
     * @param formIds
     * @return
     */
    List<GbAppealFormDO> listByFormIds(List<Long> formIds);

    /**
     * 获取可用日程、并校验锁定、非锁团购状态
     *
     * @return
     */
    FlowMonthWashControlDTO washControlGbStatusCheck(String month);
}
