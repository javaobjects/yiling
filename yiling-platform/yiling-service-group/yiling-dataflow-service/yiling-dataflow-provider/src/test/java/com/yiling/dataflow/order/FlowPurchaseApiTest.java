package com.yiling.dataflow.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseGoodsDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseMonthDTO;
import com.yiling.dataflow.order.dto.StorageInfoDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.service.FlowPurchaseService;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/7/8
 */
@Slf4j
public class FlowPurchaseApiTest extends BaseTest {

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowPurchaseApi flowPurchaseApi;

    @Test
    public void getFlowPurchaseEnterpriseListTest() {
        List<FlowPurchaseDTO> flowPurchaseDTOList = flowPurchaseApi.getFlowPurchaseEnterpriseList(null);
        System.out.println("flowPurchaseDTOList.size = " + flowPurchaseDTOList.size());
    }

    @Test
    public void getFlowPurchaseSupplierListTest() {
        List<FlowPurchaseDTO> flowPurchaseDTOList = flowPurchaseApi.getFlowPurchaseSupplierList(3);
        System.out.println("flowPurchaseDTOList.size = " + flowPurchaseDTOList.size());
    }

    @Test
    public void getFlowPurchaseMonthList() {
        QueryFlowPurchaseListRequest request = new QueryFlowPurchaseListRequest();
//        request.setTime("2022-07");
        List<String> poMonthList = new ArrayList<>();
        poMonthList.add("2022-07");
        poMonthList.add("2022-06");
        poMonthList.add("2022-05");
        poMonthList.add("2022-04");
        poMonthList.add("2022-03");
        poMonthList.add("2022-02");
        request.setPoMonthList(poMonthList);
//        request.setPurchaseChannelId(2);
        List<FlowPurchaseMonthDTO> flowPurchaseMonthDTOList = flowPurchaseApi.getFlowPurchaseMonthList(request);

        for (FlowPurchaseMonthDTO f : flowPurchaseMonthDTOList) {
            System.out.print(f.getPurchaseEnterpriseId() + "\t" + f.getSupplierEnterpriseId() + "\t" + f.getStorageQuantitySum()  + "\t");
            for (String poMonth : poMonthList) {
                StorageInfoDTO storageInfoDTO = f.getStorageInfoMap().get(poMonth);
                System.out.print(storageInfoDTO.getStorageQuantity() + "\t");
            }
            System.out.println();
        }

        System.out.println("flowPurchaseMonthDTOList.size = " + flowPurchaseMonthDTOList.size());

    }

    @Test
    public void getFlowPurchaseDetail() {
        List<FlowPurchaseDetailDTO> flowPurchaseDetailDTOList = flowPurchaseApi.getFlowPurchaseDetail(1503L, 4L, "2022-06");

        System.out.println("flowPurchaseDetailDTOList.size = " + flowPurchaseDetailDTOList.size());
    }

    @Test
    public void getPurchaseGoodsNameList() {
        List<String> list = flowPurchaseApi.getPurchaseGoodsNameList();

        System.out.println("list = " + list);
        System.out.println("list.size = " + list.size());
    }

    @Test
    public void getFlowPurchaseGoodsListTest() {
        QueryPurchaseGoodsListRequest request = new QueryPurchaseGoodsListRequest();
//        request.setTime("2022-07");
        List<String> poMonthList = new ArrayList<>();
        poMonthList.add("2022-07");
        poMonthList.add("2022-06");
        poMonthList.add("2022-05");
        poMonthList.add("2022-04");
        poMonthList.add("2022-03");
        poMonthList.add("2022-02");
        request.setPoMonthList(poMonthList);
        request.setMinQuantity(0);
        request.setMaxQuantity(10000);

//        List<Long> purchaseEnterpriseIds = new ArrayList<>();
//        purchaseEnterpriseIds.add(101821L);
//        request.setPurchaseEnterpriseIds(purchaseEnterpriseIds);
        request.setGoodsNameList(new ArrayList<>());
//        request.setProvinceCode("410000");
//        request.setCityCode("410300");
//        request.setRegionCode("410305");

        List<FlowPurchaseGoodsDTO> flowPurchaseGoodsDTOList = flowPurchaseApi.getFlowPurchaseGoodsList(request);

        System.out.println("flowPurchaseGoodsDTOList.size = " + flowPurchaseGoodsDTOList.size());
        System.out.println(flowPurchaseGoodsDTOList);
        for (FlowPurchaseGoodsDTO f : flowPurchaseGoodsDTOList) {
            System.out.print(f.getSpecificationId() + "\t" + f.getSpec() + "\t" + f.getGoodsName() + "\t" + f.getStorageQuantitySum() + "\t" + f.getRank() + "\t");
            for (String poMonth : poMonthList) {
                StorageInfoDTO storageInfoDTO = f.getStorageInfoMap().get(poMonth);
                System.out.print(storageInfoDTO.getStorageQuantity() + "\t" + storageInfoDTO.getRank() + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void getPurchaseGoodsNameListTest() {
        List<String> list = flowPurchaseApi.getPurchaseGoodsNameList();
        System.out.println("list.size = " + list.size());
    }

    @Test
    public void statisticsFlowPurchaseTotalQuantityTest() {
        flowPurchaseApi.statisticsFlowPurchaseTotalQuantity();
    }

    @Test
    public void test11(){
        UpdateFlowIndexRequest request=new UpdateFlowIndexRequest();
        request.setEid(1270L);
        request.setStartUpdateTime(DateUtil.parse("2022-01-01","yyyy-MM-dd"));
        request.setEndUpdateTime(new Date());
        flowPurchaseService.refreshFlowPurchase(request);
    }

    @Test
    public void deleteByIdListTest() {
        List<Long> idList = Arrays.asList(93142L, 93143L);
        flowPurchaseService.deleteByIdList(idList);
    }

}
