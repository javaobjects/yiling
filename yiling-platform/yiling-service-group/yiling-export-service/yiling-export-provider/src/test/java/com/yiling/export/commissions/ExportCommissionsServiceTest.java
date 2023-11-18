package com.yiling.export.commissions;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.export.BaseTest;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.impl.AgreementApplyBatchExportServiceImpl;
import com.yiling.export.export.service.impl.B2bSettlementExportServiceImpl;
import com.yiling.export.export.service.impl.CommissionsExportServiceImpl;
import com.yiling.export.export.service.impl.ReportFlowOrderSyncInfoExportServiceImpl;
import com.yiling.export.export.service.impl.ReportPurchaseStockExportServiceImpl;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.ExportCommissionsRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.settlement.b2b.dto.request.ExportSettlementPageListRequest;
import com.yiling.settlement.report.dto.request.ExportFlowOrderSyncRequest;
import com.yiling.settlement.report.dto.request.ExportReportPurchaseStockRequest;
import com.yiling.user.agreement.dto.request.ExportAgreementBatchApplyRequest;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: dexi.yao
 * @date: 2021/09/26
 */
public class ExportCommissionsServiceTest extends BaseTest {

	@Autowired
	CommissionsExportServiceImpl commissionsExportService;
	@Autowired
	AgreementApplyBatchExportServiceImpl agreementApplyBatchExportService;
	@Autowired
	B2bSettlementExportServiceImpl b2bSettlementExportService;
	@Autowired
    ReportPurchaseStockExportServiceImpl reportPurchaseStockExportService;
	@DubboReference
    OrderDetailApi orderDetailApi;

	@Test
	public void test() {
//		QueryCommissionsUserPageListRequest request=new QueryCommissionsUserPageListRequest();
//		QueryExportDataDTO queryExportDataDTO = commissionsExportService.queryData(request);
//		System.err.println(queryExportDataDTO);
	}

	@Test
	public void test1() {
		ExportAgreementBatchApplyRequest request=new ExportAgreementBatchApplyRequest();
		request.setEid(1L);
		request.setOpUserId(1L);
		request.setStatus(0);
		QueryExportDataDTO queryExportDataDTO = agreementApplyBatchExportService.queryData(request);
		System.err.println(queryExportDataDTO);
	}

	@Test
	public void test2() {
		ExportSettlementPageListRequest request=new ExportSettlementPageListRequest();
		request.setEnterpriseId(1L);
		QueryExportDataDTO queryExportDataDTO = b2bSettlementExportService.queryData(request);
		System.err.println(queryExportDataDTO);
	}

	@Test
	public void test3() {
        List<OrderDetailDTO> orderDetailDTOS = orderDetailApi.listByIdList(ListUtil.toList(1985L));
        List<OrderDetailDTO> orderDetailDTOS1 = orderDetailApi.listByIdList(ListUtil.toList(1986L));
        List<OrderDetailDTO> orderDetailById = orderDetailApi.getOrderDetailInfo(1586L);
        System.err.println(orderDetailDTOS);
	}

	@Test
	public void test4() {
        ExportReportPurchaseStockRequest request=new ExportReportPurchaseStockRequest();
        request.setEid(27L);
        QueryExportDataDTO dto = reportPurchaseStockExportService.queryData(request);
        System.err.println(dto);
	}


}
