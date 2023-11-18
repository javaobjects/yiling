package com.yiling.dataflow.report.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.lang.Nullable;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateSaleReportRelationShipRequest;
import com.yiling.dataflow.report.entity.FlowWashSaleReportDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向销售合并报表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-01
 */
public interface FlowWashSaleReportService extends BaseService<FlowWashSaleReportDO> {


    /**
     * 查询销售流向合并报表
     *
     * @param request
     * @return
     */
    Page<FlowWashSaleReportDTO> pageList(FlowWashSaleReportPageRequest request);


    /**
     * 根据清洗任务Id删除报表数据
     * @param fmwtId
     * @return
     */
    Boolean removeByFmwtId(Long fmwtId);

    /**
     * 查询流向数据
     * @param flowSaleWashIds
     * @return
     */
    @Deprecated
    List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds);

    /**
     * 根据流向Id查询流向数据
     * @param flowSaleWashIds 流向Id
     * @param flowClassifyEnumList 流向类型
     * @return
     */
    List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds, @Nullable List<FlowClassifyEnum> flowClassifyEnumList);

    /**
     * 通过流向key查询流向
     * @param flowKeys
     * @return
     */
    List<FlowWashSaleReportDO> listByFlowKey(List<String> flowKeys);


    /**
     * 更新销售报表三者关系数据
     * @param relationShipRequests
     */
    Boolean batchUpdateSaleReportRelationShip(List<UpdateSaleReportRelationShipRequest> relationShipRequests);

    /**
     * 根据年月查询经销商crmId
     * @param reportRequest 查询条件
     * @return
     */
    List<Long> listCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest);

    /**
     * 根据年月查询机构crmId
     * @param reportRequest 查询条件
     * @return
     */
    List<Long> listCustomerCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest);

    /**
     * 根据流向Id删除流向数据
     * @param flowWashSaleId 流向Id
     * @param flowClassifyEnum 流向类型 {@link FlowClassifyEnum}
     * @return
     */
    boolean removeByFlowSaleWashId(Long flowWashSaleId, FlowClassifyEnum flowClassifyEnum);

    /**
     * 根据流向Id删除流向数据
     * @param flowWashSaleIds 流向Id
     * @param flowClassifyEnum 流向类型 {@link FlowClassifyEnum}
     * @return
     */
    boolean removeByFlowSaleWashId(List<Long> flowWashSaleIds, FlowClassifyEnum flowClassifyEnum);

    /**
     * 根据流向Id查询流向数据
     * @param flowWashSaleId 流向Id
     * @param flowClassifyEnum 流向类型 {@link FlowClassifyEnum}
     * @return
     */
    FlowWashSaleReportDTO getFlowWashSale(Long flowWashSaleId, FlowClassifyEnum flowClassifyEnum);


    /**
     * 统计销售合并报表金额
     * @param reportRequest
     * @return
     */
    BigDecimal sumTotalMoney(SumFlowWashReportRequest reportRequest);

    /**
     * 根据源流向新增新的流向
     * @param createFlowWashSaleReportRequests
     * @return
     */
    boolean createByFlowWashRecord(List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests) ;

}
