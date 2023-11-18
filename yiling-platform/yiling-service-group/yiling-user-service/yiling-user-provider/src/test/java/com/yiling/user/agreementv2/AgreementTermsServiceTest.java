package com.yiling.user.agreementv2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.user.BaseTest;
import com.yiling.user.agreementv2.dto.request.AddAgreementAttachmentRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementControlAreaRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementControlRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementOtherRebateRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementPaymentMethodRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateControlAreaRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateControlRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateGoodsGroupRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebatePayEnterpriseRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateScopeRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateStageRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTaskStageRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTimeSegmentRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSettlementMethodRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSettlementTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesEnterpriseRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsGroupRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesTermsRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementRebateTermsService;
import com.yiling.user.agreementv2.service.AgreementSettlementTermsService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesTermsService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议V2.0测试类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-02
 */
@Slf4j
public class AgreementTermsServiceTest extends BaseTest {


    @Autowired
    AgreementMainTermsService agreementMainTermsService;
    @Autowired
    AgreementSupplySalesTermsService agreementSupplySalesTermsService;
    @Autowired
    AgreementSettlementTermsService agreementSettlementTermsService;
    @Autowired
    AgreementRebateTermsService agreementRebateTermsService;

    @Test
    public void addAgreementMainTerms(){
        AddAgreementMainTermsRequest request = new AddAgreementMainTermsRequest();
        request.setEid(35L);
        request.setEname("武汉光谷企业天地有限公司");
        request.setFirstType(3);
        request.setAgreementType(3);
        request.setSecondEid(39L);
        request.setSecondName("郑田的诊所科技股份有限公司");
        request.setSignTime(DateUtil.parseDate("2022-03-02 14:00:00"));
        request.setStartTime(DateUtil.parseDate("2022-03-02 14:08:21"));
        request.setEndTime(DateUtil.parseDate("2022-06-02 14:08:21"));
        request.setFirstSignUserId(66L);
        request.setFirstSignUserName("郑田");
        request.setFirstSignUserPhone("13554092888");
        request.setSecondSignUserId(67L);
        request.setSecondSignUserName("戴力002");
        request.setSecondSignUserPhone("13800000002");
        request.setMainUser(1);
        request.setFlowFlag(1);
        request.setDraftFlag(0);
        request.setMarginFlag(1);
        request.setMarginAmount(BigDecimal.valueOf(10000));
        request.setMarginPayer(3);
        request.setBusinessEid(37L);
        request.setMarginBackType(2);
        request.setMarginBackDate(DateUtil.parseDate("2022-06-09 14:08:21"));
        request.setAttachmentType(1);

        List<AddAgreementAttachmentRequest> agreementAttachmentList = new ArrayList<>();
        AddAgreementAttachmentRequest attachmentRequest = new AddAgreementAttachmentRequest();
        attachmentRequest.setAttachmentType(1);
        attachmentRequest.setFileKey("dev/enterpriseCertificate/2022/01/27/3ca050ef19d64c1d91aeda79c24a1fa2.jpg");
        agreementAttachmentList.add(attachmentRequest);

        request.setAgreementAttachmentList(agreementAttachmentList);

        log.info("addAgreementMainTerms请求参数：{}", JSONObject.toJSONString(request));
        AgreementMainTermsDO agreementMainTermsDO = agreementMainTermsService.addAgreementMainTerms(request);
        log.info(JSONObject.toJSONString(agreementMainTermsDO));
    }

    @Test
    public void addAgreementSupplySalesTerms(){
        AddAgreementSupplySalesTermsRequest request = new AddAgreementSupplySalesTermsRequest();
        request.setAgreementId(6L);
        request.setBuyChannel(3);
        request.setPullOrderFlag(1);
        request.setDistributionAgreementFlag(1);
        request.setAllLevelKindsFlag(0);

        List<AddAgreementSupplySalesEnterpriseRequest> supplySalesEnterpriseList = new ArrayList<>();
        AddAgreementSupplySalesEnterpriseRequest enterpriseRequest = new AddAgreementSupplySalesEnterpriseRequest();
        enterpriseRequest.setEid(37L);
        enterpriseRequest.setEname("山东鸿林医药有限公司");
        supplySalesEnterpriseList.add(enterpriseRequest);
        request.setSupplySalesEnterpriseList(supplySalesEnterpriseList);

        List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList = new ArrayList<>();
        AddAgreementSupplySalesGoodsGroupRequest goodsGroupRequest = new AddAgreementSupplySalesGoodsGroupRequest();
        goodsGroupRequest.setControlSaleType(2);
        goodsGroupRequest.setExitWarehouseTaxPriceFlag(true);
        goodsGroupRequest.setRetailTaxPriceFlag(true);

        List<Integer> agreementControlList = new ArrayList<>();
        agreementControlList.add(1);
        goodsGroupRequest.setAgreementControlList(agreementControlList);

        AddAgreementControlAreaRequest controlAreaRequest = new AddAgreementControlAreaRequest();
        controlAreaRequest.setJsonContent("[{\"code\":\"110000\",\"name\":\"北京市\",\"children\":[{\"code\":\"110100\",\"name\":\"北京市\"}]}]");
        goodsGroupRequest.setControlArea(controlAreaRequest);

        List<AddAgreementSupplySalesGoodsRequest> supplySalesGoodsList = new ArrayList<>();
        AddAgreementSupplySalesGoodsRequest goodsRequest = new AddAgreementSupplySalesGoodsRequest();
        goodsRequest.setGoodsId(1473L);
        goodsRequest.setGoodsName("银翘解毒颗粒");
        goodsRequest.setProducerManufacturer("广东嘉应制药股份有限公司");
        goodsRequest.setSpecifications("15g*10袋");
        goodsRequest.setExclusiveFlag(true);
        goodsRequest.setSupplyTaxPrice(BigDecimal.valueOf(12.2));
        goodsRequest.setExitWarehouseTaxPrice(BigDecimal.valueOf(15.2));
        goodsRequest.setRetailTaxPrice(BigDecimal.valueOf(19.2));
        supplySalesGoodsList.add(goodsRequest);
        goodsGroupRequest.setSupplySalesGoodsList(supplySalesGoodsList);

        supplySalesGoodsGroupList.add(goodsGroupRequest);
        request.setSupplySalesGoodsGroupList(supplySalesGoodsGroupList);

        log.info("addAgreementSupplySalesTerms请求参数：{}", JSONObject.toJSONString(request));
        Boolean result = agreementSupplySalesTermsService.addAgreementSupplySalesTerms(request);
        log.info(JSONObject.toJSONString(result));
    }

    @Test
    public void addAgreementSettlementTerms(){
        AddAgreementSettlementTermsRequest request = new AddAgreementSettlementTermsRequest();
        request.setAgreementId(6L);

        List<AddAgreementPaymentMethodRequest> agreementPaymentMethodList = new ArrayList<>();
        AddAgreementPaymentMethodRequest methodRequest = new AddAgreementPaymentMethodRequest();
        methodRequest.setPayMethod(1);
        methodRequest.setRatio(15);
        AddAgreementPaymentMethodRequest methodRequest2 = new AddAgreementPaymentMethodRequest();
        methodRequest2.setPayMethod(2);
        methodRequest2.setRatio(35);
        AddAgreementPaymentMethodRequest methodRequest3 = new AddAgreementPaymentMethodRequest();
        methodRequest3.setPayMethod(3);
        methodRequest3.setRatio(50);
        agreementPaymentMethodList.add(methodRequest);
        agreementPaymentMethodList.add(methodRequest2);
        agreementPaymentMethodList.add(methodRequest3);
        request.setAgreementPaymentMethodList(agreementPaymentMethodList);

        AddAgreementSettlementMethodRequest agreementSettlementMethod = new AddAgreementSettlementMethodRequest();
        agreementSettlementMethod.setAdvancePaymentFlag(false);
        agreementSettlementMethod.setPaymentDaysFlag(true);
        agreementSettlementMethod.setPaymentDaysSettlementPeriod(30);
        agreementSettlementMethod.setPaymentDaysSettlementDay(10);
        agreementSettlementMethod.setPaymentDaysPayDays(20);
        agreementSettlementMethod.setActualSalesSettlementFlag(true);
        agreementSettlementMethod.setActualSalesSettlementPeriod(30);
        agreementSettlementMethod.setActualSalesSettlementDay(12);
        agreementSettlementMethod.setActualSalesSettlementPayDays(22);
        agreementSettlementMethod.setPayDeliveryFlag(false);
        agreementSettlementMethod.setPressGroupFlag(true);
        agreementSettlementMethod.setPressGroupNumber(20);
        agreementSettlementMethod.setCreditFlag(true);
        agreementSettlementMethod.setCreditAmount(100000);
        agreementSettlementMethod.setArrivePeriod(14);
        agreementSettlementMethod.setMinShipmentNumber(100);
        agreementSettlementMethod.setDeliveryType(1);
        agreementSettlementMethod.setBuyerId(request.getOpUserId());

        request.setAgreementSettlementMethod(agreementSettlementMethod);
        log.info("addAgreementSettlementTerms请求参数：{}", JSONObject.toJSONString(request));
        agreementSettlementTermsService.addAgreementSettlementTerms(request);
    }

    @Test
    public void addAgreementRebateTerms(){
        AddAgreementRebateTermsRequest request = new AddAgreementRebateTermsRequest();
        request.setAgreementId(6L);
        request.setReserveSupplyFlag(0);
        request.setGoodsRebateRuleType(1);
        request.setRebatePay(2);
        request.setMaxRebate(BigDecimal.valueOf(1000000));
        request.setRebateCashType(5);
        request.setRebateCashTime(2);
        request.setRebateCashSegment(14);
        request.setRebateCashUnit(2);
        request.setOtherRemark("我现在测试一下全品设置的协议返利");
        request.setTaskFlag(1);
        request.setTaskStandard(2);
        request.setRebateStandard(1);
        request.setRebateCalculatePrice(3);
        request.setRebateStageMethod(2);
        request.setRebateCalculateRule(2);
        request.setRebateRuleType(1);
        request.setTimeSegmentTypeSet(3);
        request.setCashChildSegmentFlag(true);
        request.setCashAllSegmentFlag(true);
        request.setRemark("测试一个协议返利可真不容易啊");

        // 协议返利支付方指定商业公司集合
        List<AddAgreementRebatePayEnterpriseRequest> agreementRebatePayEnterpriseList = new ArrayList<>();
        AddAgreementRebatePayEnterpriseRequest enterpriseRequest = new AddAgreementRebatePayEnterpriseRequest();
        enterpriseRequest.setEid(36L);
        enterpriseRequest.setEname("湖北人福医药集团有限公司");
        agreementRebatePayEnterpriseList.add(enterpriseRequest);
        request.setAgreementRebatePayEnterpriseList(agreementRebatePayEnterpriseList);

        // 非商品返利集合（最多6个阶梯）
        List<AddAgreementOtherRebateRequest> agreementOtherRebateList = new ArrayList<>();
        AddAgreementOtherRebateRequest rebateRequest1 = new AddAgreementOtherRebateRequest();
        rebateRequest1.setRebateType(1);
        rebateRequest1.setAmountType(1);
        rebateRequest1.setAmount(BigDecimal.valueOf(10000));
        rebateRequest1.setUnit(1);
        rebateRequest1.setTaxFlag(true);
        AddAgreementOtherRebateRequest rebateRequest2 = new AddAgreementOtherRebateRequest();
        rebateRequest2.setRebateType(2);
        rebateRequest2.setAmountType(2);
        rebateRequest2.setAmount(BigDecimal.valueOf(20000));
        rebateRequest2.setUnit(1);
        rebateRequest2.setTaxFlag(true);
        AddAgreementOtherRebateRequest rebateRequest3 = new AddAgreementOtherRebateRequest();
        rebateRequest3.setRebateType(3);
        rebateRequest3.setAmountType(3);
        rebateRequest3.setAmount(BigDecimal.valueOf(30000));
        rebateRequest3.setUnit(1);
        rebateRequest3.setTaxFlag(true);
        agreementOtherRebateList.add(rebateRequest1);
        agreementOtherRebateList.add(rebateRequest2);
        agreementOtherRebateList.add(rebateRequest3);
        request.setAgreementOtherRebateList(agreementOtherRebateList);

        // 协议返利时段集合
        List<AddAgreementRebateTimeSegmentRequest> agreementRebateTimeSegmentList = new ArrayList<>();
        AddAgreementRebateTimeSegmentRequest segmentRequest = new AddAgreementRebateTimeSegmentRequest();
        segmentRequest.setType(1);
        segmentRequest.setSort(1);
        List<AddAgreementRebateGoodsGroupRequest> agreementRebateGoodsGroupList = new ArrayList<>();
        AddAgreementRebateGoodsGroupRequest goodsGroupRequest = new AddAgreementRebateGoodsGroupRequest();
        goodsGroupRequest.setSort(1);
        List<AddAgreementRebateTaskStageRequest> agreementRebateTaskStageList = new ArrayList<>();
        AddAgreementRebateTaskStageRequest taskStageRequest = new AddAgreementRebateTaskStageRequest();
        taskStageRequest.setTaskNum(BigDecimal.valueOf(10000));
        taskStageRequest.setTaskUnit(1);
        taskStageRequest.setOverSumBack(BigDecimal.valueOf(50000));
        taskStageRequest.setOverSumBackUnit(1);
        List<AddAgreementRebateStageRequest> agreementRebateStageList = new ArrayList<>();
        AddAgreementRebateStageRequest stageRequest1 = new AddAgreementRebateStageRequest();
        stageRequest1.setFull(BigDecimal.valueOf(100));
        stageRequest1.setFullUnit(1);
        stageRequest1.setBack(BigDecimal.valueOf(1));
        stageRequest1.setBackUnit(1);
        stageRequest1.setSort(1);
        AddAgreementRebateStageRequest stageRequest2 = new AddAgreementRebateStageRequest();
        stageRequest2.setFull(BigDecimal.valueOf(500));
        stageRequest2.setFullUnit(1);
        stageRequest2.setBack(BigDecimal.valueOf(10));
        stageRequest2.setBackUnit(1);
        stageRequest2.setSort(2);
        agreementRebateStageList.add(stageRequest1);
        agreementRebateStageList.add(stageRequest2);
        taskStageRequest.setAgreementRebateStageList(agreementRebateStageList);

        agreementRebateTaskStageList.add(taskStageRequest);

        // 控销条件
        List<Integer> agreementControlList = new ArrayList<>();
        agreementControlList.add(1);

        AddAgreementRebateControlAreaRequest controlAreaRequest = new AddAgreementRebateControlAreaRequest();
        controlAreaRequest.setJsonContent("[{\"code\":\"110000\",\"name\":\"北京市\",\"children\":[{\"code\":\"110100\",\"name\":\"北京市\"}]}]");

        List<AddAgreementRebateScopeRequest> agreementRebateScopeList = new ArrayList<>();
        AddAgreementRebateScopeRequest scopeRequest = new AddAgreementRebateScopeRequest();
        scopeRequest.setControlSaleType(2);
        scopeRequest.setAddAgreementRebateControlArea(controlAreaRequest);
        scopeRequest.setAgreementRebateControlList(agreementControlList);
        scopeRequest.setAgreementRebateTaskStageList(agreementRebateTaskStageList);
        agreementRebateScopeList.add(scopeRequest);

        goodsGroupRequest.setAgreementRebateScopeList(agreementRebateScopeList);
        agreementRebateGoodsGroupList.add(goodsGroupRequest);

        // 协议返利范围
        segmentRequest.setAgreementRebateGoodsGroupList(agreementRebateGoodsGroupList);

        // 子时段
        AddAgreementRebateTimeSegmentRequest segmentRequest2 = new AddAgreementRebateTimeSegmentRequest();
        segmentRequest2.setType(2);
        segmentRequest2.setSort(2);
        segmentRequest2.setStartTime(DateUtil.parseDate("2022-03-02 14:08:21"));
        segmentRequest2.setEndTime(DateUtil.parseDate("2022-06-02 14:08:21"));
        List<AddAgreementRebateGoodsGroupRequest> agreementRebateGoodsGroupList2 = new ArrayList<>();
        AddAgreementRebateGoodsGroupRequest goodsGroupRequest2 = new AddAgreementRebateGoodsGroupRequest();
        goodsGroupRequest2.setSort(1);
        List<AddAgreementRebateTaskStageRequest> agreementRebateTaskStageList2 = new ArrayList<>();
        AddAgreementRebateTaskStageRequest taskStageRequest3 = new AddAgreementRebateTaskStageRequest();
        taskStageRequest3.setTaskNum(BigDecimal.valueOf(10000));
        taskStageRequest3.setTaskUnit(1);
        taskStageRequest3.setOverSumBack(BigDecimal.valueOf(50000));
        taskStageRequest3.setOverSumBackUnit(1);
        List<AddAgreementRebateStageRequest> agreementRebateStageList2 = new ArrayList<>();
        AddAgreementRebateStageRequest stageRequest11 = new AddAgreementRebateStageRequest();
        stageRequest11.setFull(BigDecimal.valueOf(100));
        stageRequest11.setFullUnit(1);
        stageRequest11.setBack(BigDecimal.valueOf(1));
        stageRequest11.setBackUnit(1);
        stageRequest11.setSort(1);
        AddAgreementRebateStageRequest stageRequest22 = new AddAgreementRebateStageRequest();
        stageRequest22.setFull(BigDecimal.valueOf(500));
        stageRequest22.setFullUnit(1);
        stageRequest22.setBack(BigDecimal.valueOf(10));
        stageRequest22.setBackUnit(1);
        stageRequest22.setSort(2);
        agreementRebateStageList2.add(stageRequest11);
        agreementRebateStageList2.add(stageRequest22);
        taskStageRequest3.setAgreementRebateStageList(agreementRebateStageList2);
        agreementRebateTaskStageList2.add(taskStageRequest3);

        List<AddAgreementRebateScopeRequest> agreementRebateScopeList2 = new ArrayList<>();
        AddAgreementRebateScopeRequest scopeRequest2 = new AddAgreementRebateScopeRequest();
        scopeRequest2.setControlSaleType(2);
        scopeRequest2.setAddAgreementRebateControlArea(controlAreaRequest);
        scopeRequest2.setAgreementRebateControlList(agreementControlList);
        scopeRequest2.setAgreementRebateTaskStageList(agreementRebateTaskStageList2);
        agreementRebateScopeList2.add(scopeRequest2);
        // 协议返利范围
        goodsGroupRequest2.setAgreementRebateScopeList(agreementRebateScopeList2);

        agreementRebateGoodsGroupList2.add(goodsGroupRequest2);
        // 返利商品组
        segmentRequest2.setAgreementRebateGoodsGroupList(agreementRebateGoodsGroupList2);


        agreementRebateTimeSegmentList.add(segmentRequest);
        agreementRebateTimeSegmentList.add(segmentRequest2);
        request.setAgreementRebateTimeSegmentList(agreementRebateTimeSegmentList);

        log.info("addAgreementRebateTerms请求参数：{}", JSONObject.toJSONString(request));
        agreementRebateTermsService.addAgreementRebateTerms(request);
    }

}
