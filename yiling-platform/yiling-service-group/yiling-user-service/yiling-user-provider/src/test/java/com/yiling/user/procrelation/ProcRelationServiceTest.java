package com.yiling.user.procrelation;/**
 * @author: ray
 * @date: 2021/5/21
 */

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.BaseTest;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.service.AgreementApplyService;
import com.yiling.user.agreement.service.AgreementRebateOrderDetailService;
import com.yiling.user.agreement.service.AgreementService;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationByTimePageRequest;
import com.yiling.user.procrelation.dto.request.UpdateRelationStatusRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.user.procrelation.enums.ProcurementRelationStatusEnum;
import com.yiling.user.procrelation.service.ProcurementRelationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-03-02
 */
@Slf4j
public class ProcRelationServiceTest extends BaseTest {


    @Autowired
    ProcurementRelationService procurementRelationService;

    @DubboReference
    ProcurementRelationApi procurementRelationApi;


    @Test
    public void test(){
//        QueryProcRelationByTimePageRequest request=new QueryProcRelationByTimePageRequest();
//        request.setStartTime(DateUtil.yesterday());
//        request.setEndTime(new Date());
//        Page<ProcurementRelationDO> page = procurementRelationService.queryProcRelationPageByTime(request);
//        System.err.println(page);
    }

    @Test
    public void test2(){
         procurementRelationService.initData();
        System.err.println("null");
    }

    @Test
    public void test3(){
        procurementRelationService.updateInProgress();
        System.err.println("null");
    }

    @Test
    public void test4(){
        procurementRelationService.updateInProgress();
        System.err.println("null");
    }

}
