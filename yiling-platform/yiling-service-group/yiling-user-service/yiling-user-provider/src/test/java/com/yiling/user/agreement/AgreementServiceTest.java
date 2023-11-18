package com.yiling.user.agreement;/**
 * @author: ray
 * @date: 2021/5/21
 */

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.BaseTest;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.service.AgreementApplyService;
import com.yiling.user.agreement.service.AgreementRebateOrderDetailService;
import com.yiling.user.agreement.service.AgreementService;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gxl
 * @description:
 * @date: Created in 8:44 2021/5/21
 * @modified By:
 */
@Slf4j
public class AgreementServiceTest extends BaseTest {


    @Autowired
	AgreementService agreementService;

    @Autowired
	AgreementRebateOrderDetailService agreementRebateOrderDetailService;
    @Autowired
	AgreementApplyService agreementApplyService;

    @Test
    public void test(){
		QueryThirdAgreementEntListRequest request=new QueryThirdAgreementEntListRequest();
		request.setEid(9L);
		request.setCurrent(2);
		request.setSize(1);
		Page<ThirdAgreementEntPageListItemDTO> thirdAgreementEntListItemBOPage = agreementService.queryThirdAgreementsEntPageList(request);
		System.err.println(thirdAgreementEntListItemBOPage);
    }

    @Test
    public void test1(){
		QueryRebateOrderDetailPageListRequest request=new QueryRebateOrderDetailPageListRequest();

		request.setCurrent(1);
		request.setSize(10);
		request.setOrderIdList(ListUtil.toList(8l));
		request.setReturnIdList(ListUtil.toList(1L));
		Page<AgreementRebateOrderDetailDTO> page = agreementRebateOrderDetailService.queryAgreementRebateOrderDetailPageList(request);
		System.err.println(page);
    }

    @Test
    public void test2(){
		Boolean aBoolean = agreementApplyService.applyCompletePush(ListUtil.toList(1L));
		System.err.println(aBoolean);
    }
    @Test
    public void test3(){
		List<AgreementApplyOpenDTO> agreementApplyOpenDTOS = agreementApplyService.queryAgreementApplyOpenList(null, null);
		System.err.println(agreementApplyOpenDTOS);
    }

    @Test
    public void test4(){
		List<AgreementApplyOpenDTO> agreementApplyOpenDTOS = agreementApplyService.queryAgreementApplyOpenList(null, null);
		System.err.println(agreementApplyOpenDTOS);
    }
    @Test
    public void test5(){
		Boolean aBoolean = agreementApplyService.applyCompletePush(ListUtil.toList(1L));
		System.err.println(aBoolean);
    }

//    @Test
//    public void test6(){
//        CloseAgreementRequest request=new CloseAgreementRequest();
//        request.setAgreementId(259L);
//        request.setOpType(2);
//        Boolean aBoolean = agreementService.agreementClose(request);
//        System.err.println(aBoolean);
//    }
//
//    @Test
//    public void test7(){
//        CloseAgreementRequest request=new CloseAgreementRequest();
//        request.setAgreementId(262L);
//        request.setOpType(2);
//        Boolean aBoolean = agreementService.agreementClose(request);
//        System.err.println(aBoolean);
//    }

}
