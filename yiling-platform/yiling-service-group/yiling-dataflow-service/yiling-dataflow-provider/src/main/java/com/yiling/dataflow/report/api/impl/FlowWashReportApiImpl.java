package com.yiling.dataflow.report.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.api.FlowWashReportApi;
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
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateSaleReportRelationShipRequest;
import com.yiling.dataflow.report.service.FlowWashHospitalStockReportService;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.dataflow.report.service.FlowWashPharmacyPurchaseReportService;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.report.service.FlowWashSupplyStockReportService;

/** 流向清洗业务报表
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@DubboService
public class FlowWashReportApiImpl implements FlowWashReportApi {

    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;
    @Autowired
    private FlowWashInventoryReportService flowWashInventoryReportService;
    @Autowired
    private FlowWashSupplyStockReportService flowWashSupplyStockReportService;
    @Autowired
    private FlowWashHospitalStockReportService hospitalStockReportService;
    @Autowired
    private FlowWashPharmacyPurchaseReportService pharmacyPurchaseReportService;


    @Override
    public Page<FlowWashSaleReportDTO> saleReportPageList(FlowWashSaleReportPageRequest request) {

        return flowWashSaleReportService.pageList(request);
    }

    @Override
    public Page<FlowWashInventoryReportDTO> inventoryReportPageList(FlowWashInventoryReportPageRequest request) {

        return flowWashInventoryReportService.pageList(request);
    }

    @Override
    public Page<FlowWashSupplyStockReportDTO> supplyStockReportPageList(FlowWashSupplyStockReportPageRequest request) {

        return flowWashSupplyStockReportService.pageList(request);
    }


    @Override
    public Page<FlowWashHospitalStockReportDTO> hospitalStockReportPageList(FlowWashHospitalStockReportPageRequest request) {

        return hospitalStockReportService.pageList(request);
    }

    @Override
    public Page<FlowWashPharmacyPurchaseReportDTO> pharmacyPurchaseReportPageList(FlowWashPharmacyPurchaseReportPageRequest request) {

        return pharmacyPurchaseReportService.pageList(request);
    }

    @Override
    public List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds) {

        return flowWashSaleReportService.listByFlowSaleWashIds(flowSaleWashIds);
    }

    @Override
    public Boolean batchUpdateSaleReportRelationShip(List<UpdateSaleReportRelationShipRequest> relationShipRequests) {

        return flowWashSaleReportService.batchUpdateSaleReportRelationShip(relationShipRequests);

    }

    @Override
    public Boolean removeSupplyStockReportBySoMonth(String month) {

        return flowWashSupplyStockReportService.removeByCrmId(new RemoveFlowWashStockReportRequest().setSoMonth(month));
    }

    @Override
    public Boolean removeHospitalStockReportBySoMonth(String month) {


        return hospitalStockReportService.removeByCrmId(new RemoveFlowWashStockReportRequest().setSoMonth(month));
    }

    @Override
    public Boolean removePharmacyPurchaseReportBySoMonth(String month) {

        return pharmacyPurchaseReportService.removeByCrmId(new RemoveFlowWashStockReportRequest().setSoMonth(month));

    }

    @Override
    public List<Long> listCrmIdBySaleReportCondition(QueryFlowWashSaleReportRequest request) {


        return flowWashSaleReportService.listCrmIdByCondition(request);
    }


    @Override
    public List<Long> listCustomerCrmIdBySaleReportCondition(QueryFlowWashSaleReportRequest request) {


        return flowWashSaleReportService.listCustomerCrmIdByCondition(request);
    }
}
