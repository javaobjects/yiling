package com.yiling.settlement.b2b;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.BaseTest;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;
import com.yiling.settlement.b2b.bo.B2BSettlementMetadataBO;
import com.yiling.settlement.b2b.dto.SettlementOrderDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementOrderPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;
import com.yiling.settlement.b2b.service.SettlementOrderService;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;
import com.yiling.settlement.b2b.service.SettlementService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class B2BSettlementServiceTest extends BaseTest {


    @Autowired
	SettlementService settlementService;
    @Autowired
    SettlementOrderService settlementOrderService;
    @Autowired
    SettlementOrderSyncService settlementOrderSyncService;

    @DubboReference
    SettlementOrderSyncApi  settlementOrderSyncApi;


//    @Test
//    public void test(){
//		Map<Long, List<GenerateSettByOrderRequest>> request= MapUtil.newHashMap();
//		List<GenerateSettByOrderRequest> s1;
//		GenerateSettByOrderRequest request11=new GenerateSettByOrderRequest();
//		request11.setOrderId(115L);
//		request11.setOrderNo("D20210804153038548551");
//		request11.setSellerEid(2L);
//		request11.setBuyerEid(36L);
//		request11.setPaymentMethod(1L);
//		request11.setPaymentAmount(new BigDecimal("100"));
//		request11.setPaymentRefundAmount(BigDecimal.ZERO);
//		request11.setPlatformAmount(BigDecimal.ZERO);
//		request11.setReturnPlatformAmount(BigDecimal.ZERO);
//		GenerateSettByOrderRequest request12=new GenerateSettByOrderRequest();
//		request12.setOrderId(117L);
//		request12.setOrderNo("D20210804163836539196");
//		request12.setSellerEid(2L);
//		request12.setBuyerEid(35L);
//		request12.setPaymentMethod(2L);
//		request12.setPaymentAmount(new BigDecimal("200"));
//		request12.setPaymentRefundAmount(new BigDecimal("50"));
//		request12.setPlatformAmount(new BigDecimal("100"));
//		request12.setReturnPlatformAmount(new BigDecimal("20"));
//		request12.setShopCouponId(1L);
//		request12.setPlatformCouponId(2L);
//		request12.setShopCouponPercent(new BigDecimal("0.5"));
//		request12.setPlatformCouponPercent(new BigDecimal("0.5"));
//		request12.setPlatformCouponDiscountAmount(new BigDecimal("20"));
//		request12.setCouponDiscountAmount(new BigDecimal("20"));
//		request12.setReturnPlatformCouponDiscountAmount(new BigDecimal("10"));
//		request12.setReturnCouponDiscountAmount(new BigDecimal("10"));
//		GenerateSettByOrderRequest request13=new GenerateSettByOrderRequest();
//		request13.setOrderId(119L);
//		request13.setOrderNo("D20210805090925512167");
//		request13.setSellerEid(2L);
//		request13.setBuyerEid(36L);
//		request13.setPaymentMethod(3L);
//		request13.setPaymentAmount(new BigDecimal("200"));
//		request13.setPaymentRefundAmount(new BigDecimal("50"));
//		request13.setPlatformAmount(new BigDecimal("300"));
//		request13.setReturnPlatformAmount(new BigDecimal("30"));
//		request13.setShopCouponId(3L);
//		request13.setPlatformCouponId(4L);
//		request13.setShopCouponPercent(new BigDecimal("0.5"));
//		request13.setPlatformCouponPercent(new BigDecimal("0.3"));
//		request13.setPlatformCouponDiscountAmount(new BigDecimal("50"));
//		request13.setCouponDiscountAmount(new BigDecimal("30"));
//		request13.setReturnPlatformCouponDiscountAmount(new BigDecimal("15"));
//		request13.setReturnCouponDiscountAmount(new BigDecimal("15"));
//		GenerateSettByOrderRequest request14=new GenerateSettByOrderRequest();
//		request14.setOrderId(120L);
//		request14.setOrderNo("D20210805093346227993");
//		request14.setSellerEid(2L);
//		request14.setBuyerEid(36L);
//		request14.setPaymentMethod(1L);
//		request14.setPaymentAmount(new BigDecimal("150"));
//		request14.setPaymentRefundAmount(new BigDecimal("0"));
//		request14.setPlatformAmount(new BigDecimal("300"));
//		request14.setReturnPlatformAmount(new BigDecimal("290"));
//		request14.setShopCouponId(6L);
//		request14.setPlatformCouponId(7L);
//		request14.setShopCouponPercent(new BigDecimal("0.5"));
//		request14.setPlatformCouponPercent(new BigDecimal("0.3"));
//		request14.setPlatformCouponDiscountAmount(new BigDecimal("500"));
//		request14.setCouponDiscountAmount(new BigDecimal("300"));
//		request14.setReturnPlatformCouponDiscountAmount(new BigDecimal("150"));
//		request14.setReturnCouponDiscountAmount(new BigDecimal("140"));
//		s1=ListUtil.toList(request11,request12,request13,request14);
////		s1=ListUtil.toList(request13);
//		request.put(1L,s1);
//		Boolean aBoolean = settlementService.generateSettlementByOrder(request);
//		System.err.println(aBoolean);
//    }

    @Test
    public void test(){
		Map<Long, List<SettlementOrderSyncDO>> request= MapUtil.newHashMap();
		List<SettlementOrderSyncDO> s1;
        SettlementOrderSyncDO request11=new SettlementOrderSyncDO();
		request11.setOrderId(215L);
		request11.setOrderNo("D11111111111111111");
		request11.setSellerEid(2L);
		request11.setBuyerEid(36L);
		request11.setPaymentMethod(1);
		request11.setPaymentAmount(new BigDecimal("100"));
		request11.setRefundPaymentAmount(BigDecimal.ZERO);
		request11.setCouponAmount(BigDecimal.ZERO);
		request11.setRefundCouponAmount(BigDecimal.ZERO);
        SettlementOrderSyncDO request12=new SettlementOrderSyncDO();
		request12.setOrderId(217L);
		request12.setOrderNo("D22222222222222222");
		request12.setSellerEid(2L);
		request12.setBuyerEid(35L);
		request12.setPaymentMethod(2);
		request12.setPaymentAmount(new BigDecimal("200"));
		request12.setRefundPaymentAmount(new BigDecimal("50"));
		request12.setCouponAmount(new BigDecimal("100"));
		request12.setRefundCouponAmount(new BigDecimal("20"));
		request12.setShopCouponId(1L);
		request12.setPlatformCouponId(2L);
		request12.setShopCouponPercent(new BigDecimal("0.5"));
		request12.setPlatformCouponPercent(new BigDecimal("0.5"));
		request12.setPlatformCouponDiscountAmount(new BigDecimal("20"));
		request12.setShopCouponDiscountAmount(new BigDecimal("20"));
		request12.setReturnPlatformCouponDiscountAmount(new BigDecimal("10"));
		request12.setReturnCouponDiscountAmount(new BigDecimal("10"));
        SettlementOrderSyncDO request13=new SettlementOrderSyncDO();
		request13.setOrderId(219L);
		request13.setOrderNo("D333333333333333333333");
		request13.setSellerEid(2L);
		request13.setBuyerEid(36L);
		request13.setPaymentMethod(3);
		request13.setPaymentAmount(new BigDecimal("200"));
		request13.setRefundPaymentAmount(new BigDecimal("50"));
		request13.setCouponAmount(new BigDecimal("300"));
		request13.setRefundCouponAmount(new BigDecimal("30"));
		request13.setShopCouponId(3L);
		request13.setPlatformCouponId(4L);
		request13.setShopCouponPercent(new BigDecimal("0.5"));
		request13.setPlatformCouponPercent(new BigDecimal("0.3"));
		request13.setPlatformCouponDiscountAmount(new BigDecimal("50"));
		request13.setShopCouponDiscountAmount(new BigDecimal("30"));
		request13.setReturnPlatformCouponDiscountAmount(new BigDecimal("15"));
		request13.setReturnCouponDiscountAmount(new BigDecimal("15"));
        SettlementOrderSyncDO request14=new SettlementOrderSyncDO();
		request14.setOrderId(220L);
		request14.setOrderNo("D44444444444444444444");
		request14.setSellerEid(2L);
		request14.setBuyerEid(36L);
		request14.setPaymentMethod(1);
		request14.setPaymentAmount(new BigDecimal("150"));
		request14.setRefundPaymentAmount(new BigDecimal("0"));
		request14.setCouponAmount(new BigDecimal("300"));
		request14.setRefundCouponAmount(new BigDecimal("290"));
		request14.setShopCouponId(6L);
		request14.setPlatformCouponId(7L);
		request14.setShopCouponPercent(new BigDecimal("0.5"));
		request14.setPlatformCouponPercent(new BigDecimal("0.3"));
		request14.setPlatformCouponDiscountAmount(new BigDecimal("500"));
		request14.setShopCouponDiscountAmount(new BigDecimal("300"));
		request14.setReturnPlatformCouponDiscountAmount(new BigDecimal("150"));
		request14.setReturnCouponDiscountAmount(new BigDecimal("140"));
		s1=ListUtil.toList(request11,request12,request13,request14);
//		s1=ListUtil.toList(request13);
		request.put(1L,s1);
		B2BSettlementMetadataBO settlementDOList = settlementService.generateSettlementByOrder(request);
		System.err.println(settlementDOList);
    }

    @Test
    public void test3(){
    	settlementService.generateSettlement(26L);
	}

    @Test
    public void test4(){
		UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
		request.setPaymentStatus(4);
		request.setPayNo("TEST_PT20221207101402229876");
        request.setThirdPayNo("20221207101500d1333d29");
        request.setErrMsg("银行充退");
		Boolean result = settlementService.updatePaymentStatus(Collections.singletonList(request));
		System.err.println(result);
	}

    @Test
    public void test6(){
        QuerySettlementOrderPageListRequest request=new QuerySettlementOrderPageListRequest();
        request.setGoodsStatus(3);
        request.setSaleStatus(0);
        Page<SettlementOrderDTO> dtoPage = settlementOrderService.querySettlementOrderPageList(request);
        System.err.println(dtoPage);
    }

    @Test
    public void test8(){
        settlementOrderSyncApi.settOrderSyncFailData();
        System.err.println("null");
    }

    @Test
    public void test9(){
        settlementOrderSyncService.createSettOrderSync("D20230511144015574785");
        System.err.println("null");
    }





}
