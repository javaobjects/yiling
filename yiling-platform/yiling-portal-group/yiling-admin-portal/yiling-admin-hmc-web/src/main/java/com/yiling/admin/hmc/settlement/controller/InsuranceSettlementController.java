package com.yiling.admin.hmc.settlement.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionDetailVO;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionGoodsVO;
import com.yiling.admin.hmc.order.vo.OrderPrescriptionVO;
import com.yiling.admin.hmc.settlement.form.ImportInsuranceSettlementForm;
import com.yiling.admin.hmc.settlement.form.InsuranceSettlementImportForm;
import com.yiling.admin.hmc.settlement.form.InsuranceSettlementPageForm;
import com.yiling.admin.hmc.settlement.form.PageSyncInsuranceOrderForm;
import com.yiling.admin.hmc.settlement.form.SyncOrderForm;
import com.yiling.admin.hmc.settlement.handler.ImportInsuranceSettlementDataHandler;
import com.yiling.admin.hmc.settlement.vo.InsuranceSettlementAndDetailVO;
import com.yiling.admin.hmc.settlement.vo.InsuranceSettlementDetailVO;
import com.yiling.admin.hmc.settlement.vo.InsuranceSettlementPageResultVO;
import com.yiling.admin.hmc.settlement.vo.SyncInsuranceOrderDetailVO;
import com.yiling.admin.hmc.settlement.vo.SyncInsuranceOrderVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.bo.TkOrderResult;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.api.OrderDetailControlApi;
import com.yiling.hmc.order.api.OrderPrescriptionApi;
import com.yiling.hmc.order.api.OrderPrescriptionGoodsApi;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.order.dto.request.SyncOrderPageRequest;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.settlement.api.InsuranceSettlementApi;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDTO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDetailDTO;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 保司结账表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Api(tags = "保司结账接口")
@Slf4j
@RestController
@RequestMapping("/settlement/insurance")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceSettlementController extends BaseController {

    @DubboReference
    InsuranceSettlementApi insuranceSettlementApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderPrescriptionApi orderPrescriptionApi;

    @DubboReference
    OrderPrescriptionGoodsApi orderPrescriptionGoodsApi;

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    private final ImportInsuranceSettlementDataHandler importInsuranceSettlementDataHandler;

    private final FileService fileService;

    @ApiOperation(value = "保司结账分页查询--保司的范围查询")
    @PostMapping("/pageList")
    public Result<InsuranceSettlementPageResultVO> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid InsuranceSettlementPageForm form) {
        InsuranceSettlementPageRequest request = PojoUtils.map(form, InsuranceSettlementPageRequest.class);
        InsuranceSettlementPageResultBO resultBO = insuranceSettlementApi.pageList(request);
        InsuranceSettlementPageResultVO resultVO = PojoUtils.map(resultBO, InsuranceSettlementPageResultVO.class);
        return Result.success(resultVO);
    }

    @ApiOperation(value = "保司结账明细分页查询--某一个保司里面的详情")
    @GetMapping("/queryDetail")
    public Result<InsuranceSettlementAndDetailVO> queryDetail(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("id") @ApiParam("保司结账主表id") Long id) {
        InsuranceSettlementDTO insuranceSettlementDTO = insuranceSettlementApi.queryById(id);
        InsuranceSettlementAndDetailVO settlementAndDetailVO = PojoUtils.map(insuranceSettlementDTO, InsuranceSettlementAndDetailVO.class);
        if (null == insuranceSettlementDTO) {
            return Result.success(null);
        }

        List<InsuranceSettlementDetailDTO> detailDTOList = insuranceSettlementApi.listByInsuranceSettlementId(id);
        List<InsuranceSettlementDetailVO> detailVOList = PojoUtils.map(detailDTOList, InsuranceSettlementDetailVO.class);
        settlementAndDetailVO.setInsuranceSettlementDetailList(detailVOList);
        return Result.success(settlementAndDetailVO);
    }

    @ApiOperation(value = "保司导入结算数据", httpMethod = "POST")
    @PostMapping(value = "importInsuranceSettlement", headers = "content-type=multipart/form-data")
    public Result<ImportResultVO> importInsuranceSettlement(InsuranceSettlementImportForm form, @CurrentUser CurrentAdminInfo staffInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importInsuranceSettlementDataHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = form.getFile().getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel<ImportInsuranceSettlementForm> importResultModel;
        try {
            //包含了插入数据库失败的信息
            long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, staffInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportInsuranceSettlementForm.class, params, importInsuranceSettlementDataHandler, paramMap);
            log.info("保司导入结算数据,耗时：{},导入数据为:[{}]", System.currentTimeMillis() - start, importResultModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    // ============================================手动同步到保司的理赔对账=============================================

    @ApiOperation(value = "同步订单数据到保司")
    @PostMapping("/syncOrder")
    public Result<TkOrderResult> syncOrder(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SyncOrderForm form) {
        SyncOrderRequest request = PojoUtils.map(form, SyncOrderRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = insuranceSettlementApi.syncOrder(request);
        if (!isSuccess) {
            return Result.failed("同步订单数据到保司数据出现错误");
        }
        return Result.success();
    }

    @ApiOperation(value = "获取同步订单数据到保司的结果")
    @PostMapping("/getSyncOrderResult")
    public Result<Object> getSyncOrderResult(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SyncOrderForm form) {
        SyncOrderRequest request = PojoUtils.map(form, SyncOrderRequest.class);
        return insuranceSettlementApi.getSyncOrderResult(request);
    }

    @ApiOperation(value = "同步订单到保司的订单分页查询--为同步订单到保司的查询")
    @PostMapping("/querySyncInsuranceOrder")
    public Result<Page<SyncInsuranceOrderVO>> querySyncInsuranceOrder(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PageSyncInsuranceOrderForm form) {
        SyncOrderPageRequest request = PojoUtils.map(form, SyncOrderPageRequest.class);
        request.setOrderStatus(HmcOrderStatusEnum.FINISHED.getCode());
        request.setPrescriptionStatus(form.getPrescriptionStatus());
        List<Integer> synchronousTypeList = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(3);
        }};
        request.setSynchronousTypeList(synchronousTypeList);
        Page<OrderDTO> orderDTOPage = orderApi.syncPageList(request);
        Page<SyncInsuranceOrderVO> voPage = PojoUtils.map(orderDTOPage, SyncInsuranceOrderVO.class);
        for (SyncInsuranceOrderVO record : voPage.getRecords()) {

            // 票据图片处理展示
            addOrderReceiptsList(record);

            // 保险公司名称
            addInsuranceCompanyName(record, record.getInsuranceCompanyId());

            // 处方信息
            addOrderPrescriptionDetail(record, record.getId());

            // 订单明细信息
            addSyncInsuranceOrderDetail(record, record.getId());
        }
        return Result.success(voPage);
    }

    /**
     * 票据图片处理展示
     *
     * @param syncInsuranceOrderVO 返回的数据
     */
    private void addOrderReceiptsList(SyncInsuranceOrderVO syncInsuranceOrderVO) {
        String orderReceipts = syncInsuranceOrderVO.getOrderReceipts();
        if (StringUtils.isBlank(orderReceipts)) {
            return;
        }
        String[] orderReceiptStr = orderReceipts.split(",");
        List<String> orderReceiptsUrlList = new ArrayList<>();
        for (String str : orderReceiptStr) {
            orderReceiptsUrlList.add(fileService.getUrl(str, FileTypeEnum.ORDER_RECEIPTS));
        }
        syncInsuranceOrderVO.setOrderReceiptsList(orderReceiptsUrlList);
    }

    /**
     * 新增保险公司名称
     *
     * @param syncInsuranceOrderVO 返回的数据
     * @param insuranceCompanyId 保险公司名称
     */
    private void addInsuranceCompanyName(SyncInsuranceOrderVO syncInsuranceOrderVO, Long insuranceCompanyId) {
        InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(insuranceCompanyId);
        if (null != insuranceCompanyDTO) {
            syncInsuranceOrderVO.setInsuranceCompanyName(insuranceCompanyDTO.getCompanyName());
        }
    }

    /**
     * 新增订单明细信息
     *
     * @param syncInsuranceOrderVO 返回的数据
     * @param orderId 订单id
     */
    private void addSyncInsuranceOrderDetail(SyncInsuranceOrderVO syncInsuranceOrderVO, Long orderId) {
        List<OrderDetailDTO> detailDTOList = orderDetailApi.listByOrderId(orderId);
        List<SyncInsuranceOrderDetailVO> orderDetailVOList = PojoUtils.map(detailDTOList, SyncInsuranceOrderDetailVO.class);

        // 管控信息
        List<OrderDetailControlBO> orderDetailControlList = orderDetailControlApi.listByOrderIdAndSellSpecificationsIdList(orderId, null);
        Map<Long, OrderDetailControlBO> detailControlMap = orderDetailControlList.stream().collect(Collectors.toMap(OrderDetailControlBO::getSellSpecificationsId, e -> e, (k1, k2) -> k1));

        orderDetailVOList.forEach(e -> {
            OrderDetailControlBO detailControlBO = detailControlMap.get(e.getSellSpecificationsId());
            e.setControlStatus(0);
            if (null != detailControlBO) {
                List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(detailControlBO.getEidList());
                List<String> channelNameList = enterpriseList.stream().map(EnterpriseDTO::getName).collect(Collectors.toList());
                e.setControlStatus(1);
                e.setChannelNameList(channelNameList);
            }
        });
        syncInsuranceOrderVO.setSyncInsuranceOrderDetailList(orderDetailVOList);
    }

    /**
     * 新增处方信息
     *
     * @param syncInsuranceOrderVO 返回的数据
     * @param orderId 订单id
     */
    private void addOrderPrescriptionDetail(SyncInsuranceOrderVO syncInsuranceOrderVO, Long orderId) {
        OrderPrescriptionDTO prescriptionDTO = orderPrescriptionApi.getByOrderId(orderId);
        List<OrderPrescriptionGoodsDTO> goodsDTOList = orderPrescriptionGoodsApi.getByOrderId(orderId);
        OrderPrescriptionVO prescriptionVO = PojoUtils.map(prescriptionDTO, OrderPrescriptionVO.class);
        if (null != prescriptionVO && StringUtils.isNotBlank(prescriptionVO.getPrescriptionSnapshotUrl())) {
            List<String> prescriptionSnapshotUrlList = new ArrayList<>();
            String[] picStr = prescriptionVO.getPrescriptionSnapshotUrl().split(",");
            for (String str : picStr) {
                prescriptionSnapshotUrlList.add(fileService.getUrl(str, FileTypeEnum.PRESCRIPTION_PIC));
            }
            prescriptionVO.setPrescriptionSnapshotUrlList(prescriptionSnapshotUrlList);
        }
        List<OrderPrescriptionGoodsVO> goodsVO = PojoUtils.map(goodsDTOList, OrderPrescriptionGoodsVO.class);
        OrderPrescriptionDetailVO detailVO = new OrderPrescriptionDetailVO();
        detailVO.setPrescription(prescriptionVO);
        detailVO.setGoodsList(goodsVO);
        syncInsuranceOrderVO.setOrderPrescriptionDetail(detailVO);
    }
}
