package com.yiling.dataflow.report.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashHospitalStockReportDTO;
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.FlowWashPharmacyPurchaseReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.FlowWashSupplyStockReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashPharmacyPurchaseReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSupplyStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateSaleReportRelationShipRequest;

/** 流向清洗报表
 * @author zhigang.guo
 * @date: 2023/3/2
 */
public interface FlowWashReportApi {

    /**
     * 查询流向数据
     * @param flowSaleWashIds
     * @return
     */
    List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds);

    /**
     * 更新三者关系数据
     * @param relationShipRequests
     */
    Boolean batchUpdateSaleReportRelationShip(List<UpdateSaleReportRelationShipRequest> relationShipRequests);

    /**
     * 根据月份删除商业进销存报表
     * @param soMonth 年月(yyyy-MM) eg:2023-02
     * @return
     */
    Boolean removeSupplyStockReportBySoMonth(String soMonth);

    /**
     * 根据月份删除医疗进销存报表
     * @param soMonth 年月(yyyy-MM) eg:2023-02
     * @return
     */
    Boolean removeHospitalStockReportBySoMonth(String soMonth);

    /**
     * 根据月份删除零售购进报表
     * @param soMonth 年月(yyyy-MM) eg:2023-02
     * @return
     */
    Boolean removePharmacyPurchaseReportBySoMonth(String soMonth);

    /**
     * 流向销售合并报表
     * @param request
     * @return
     */
    Page<FlowWashSaleReportDTO> saleReportPageList(FlowWashSaleReportPageRequest request);

    /**
     * 库存合并报表
     * @param request
     * @return
     */
    Page<FlowWashInventoryReportDTO> inventoryReportPageList(FlowWashInventoryReportPageRequest request);

    /**
     * 查询商业经销存报表
     * @param request
     * @return
     */
    Page<FlowWashSupplyStockReportDTO> supplyStockReportPageList(FlowWashSupplyStockReportPageRequest request);

    /**
     * 查询医疗经销存报表
     * @param request
     * @return
     */
    Page<FlowWashHospitalStockReportDTO> hospitalStockReportPageList(FlowWashHospitalStockReportPageRequest request);

    /**
     * 查询医疗经销存报表
     * @param request
     * @return
     */
    Page<FlowWashPharmacyPurchaseReportDTO> pharmacyPurchaseReportPageList(FlowWashPharmacyPurchaseReportPageRequest request);


    /**
     * 根据年月查询销售合并报表经销商crmId
     * @param request 查询条件
     * @return
     */
    List<Long> listCrmIdBySaleReportCondition(QueryFlowWashSaleReportRequest request);

    /**
     * 根据年月查询销售合并报表机构crmId
     * @param request 查询条件
     * @return
     */
    List<Long> listCustomerCrmIdBySaleReportCondition(QueryFlowWashSaleReportRequest request);


}
