package com.yiling.mall.agreement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.mall.BaseTest;
import com.yiling.mall.agreement.dto.request.AgreementCalculateRequest;
import com.yiling.mall.agreement.dto.request.CashMallAgreementRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/7/7
 */
public class AgreementBusinessServiceTest extends BaseTest {

    @Autowired
    AgreementBusinessService agreementBusinessService;
    @Autowired
    AgreementImportTaskService agreementImportTaskService;

    @Test
    public void calculateCashAgreement() {
        AgreementCalculateRequest.AgreementCalculateDetailRequest detailRequest2=new AgreementCalculateRequest.AgreementCalculateDetailRequest();
        detailRequest2.setGoodsId(2L);
        detailRequest2.setGoodsAmount(new BigDecimal(15));
        detailRequest2.setGoodsQuantity(5L);
        AgreementCalculateRequest.AgreementCalculateDetailRequest detailRequest1=new AgreementCalculateRequest.AgreementCalculateDetailRequest();
        detailRequest1.setGoodsId(1L);
        detailRequest1.setGoodsAmount(new BigDecimal(25));
        detailRequest1.setGoodsQuantity(5L);
        AgreementCalculateRequest.AgreementCalculateDetailRequest detailRequest=new AgreementCalculateRequest.AgreementCalculateDetailRequest();
        detailRequest.setGoodsId(1792L);
        detailRequest.setGoodsAmount(new BigDecimal(4.4));
        detailRequest.setGoodsQuantity(5L);
        List<AgreementCalculateRequest.AgreementCalculateDetailRequest> list=new ArrayList<>();
        list.add(detailRequest);
        list.add(detailRequest1);
        list.add(detailRequest2);
        AgreementCalculateRequest request=new AgreementCalculateRequest();
        request.setBuyerEid(22L);
        request.setPaymentMethod(3);
        request.setAgreementCalculateDetailList(list);
        System.out.println(JSON.toJSONString(agreementBusinessService.calculateCashAgreement(request)));
    }

    /**
     * 协议计算测试
     */
    @Test
    public void calculateRebateAgreementByAgreementIdTest() {
        CashMallAgreementRequest request=new CashMallAgreementRequest();
        request.setAgreementId(447L);
        request.setTimeRange(Arrays.asList("2021-08"));
        request.setEasAccount("027.GKHB");
        request.setOpUserId(0L);
        System.out.println(JSON.toJSONString(agreementBusinessService.calculateRebateAgreementByAgreementId(request)));
    }

    /**
     * 协议兑付
     */
    @Test
    public void cashAgreementByAgreementIdTest() {
        CashMallAgreementRequest request=new CashMallAgreementRequest();
        request.setAgreementId(340L);
        request.setTimeRange(Arrays.asList("2021-08"));
        request.setEasAccount("027.XL");
        request.setOpUserId(0L);
        System.out.println(JSON.toJSONString(agreementBusinessService.cashAgreementByAgreementId(request)));
    }

    /**
     * 协议兑付
     */
    @Test
    public void cashAgreementByAgreementIdTest1() {
        System.out.println(JSON.toJSONString(agreementBusinessService.calculateRebateAgreementByDay()));
    }

    @Test
    public void generateAgree() {
        agreementImportTaskService.generateAgree("aa");
        System.err.println("");

    }
}
