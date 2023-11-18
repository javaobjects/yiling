package com.yiling.dataflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryListPageDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryLogListPageDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.enums.FlowPurchaseInventorySourceEnum;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/5/24
 */
@Slf4j
public class FlowPurchaseInventoryServiceTest extends BaseTest  {

    @Autowired
    private FlowPurchaseInventoryService flowPurchaseInventoryService;
    @DubboReference
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;

    @Test
    public void flowSaleYunCangPageTest() {
        System.out.println(">>>>> pageTest start");
        Long eid = 27L;
        Long ylGoodsId = 1L;
        String goodsInSn = "001";

        QueryFlowPurchaseInventorySettlementRequest request = new QueryFlowPurchaseInventorySettlementRequest();
        List<QueryFlowPurchaseInventorySettlementDetailRequest> list = new ArrayList<>();
        QueryFlowPurchaseInventorySettlementDetailRequest detailRequest = new QueryFlowPurchaseInventorySettlementDetailRequest();
        detailRequest.setEid(eid);
        detailRequest.setYlGoodsId(ylGoodsId);
        detailRequest.setGoodsInSn(goodsInSn);
        list.add(detailRequest);

        QueryFlowPurchaseInventorySettlementDetailRequest detailRequest2 = new QueryFlowPurchaseInventorySettlementDetailRequest();
        detailRequest2.setEid(28L);
        detailRequest2.setYlGoodsId(ylGoodsId);
        detailRequest2.setGoodsInSn(goodsInSn);
        list.add(detailRequest2);

        request.setList(list);

        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getList(), "参数 list 不能为空");
        request.getList().forEach(detail -> {
            Assert.notNull(detail.getEid(), "参数 eid 不能为空");
            Assert.notNull(detail.getYlGoodsId(), "参数 ylGoodsId 不能为空");
            Assert.notBlank(detail.getGoodsInSn(), "参数 goodsInSn 不能为空");
        });

        List<FlowPurchaseInventoryBO> boList = getBoList(request);
        log.info(">>>>> boList:{}", boList.toString());

        System.out.println(">>>>> pageTest end");
    }

    public List<FlowPurchaseInventoryBO> getBoList(QueryFlowPurchaseInventorySettlementRequest request){
        List<FlowPurchaseInventoryDTO> list = PojoUtils.map(flowPurchaseInventoryService.getListByEidAndYlGoodsIdAndGoodsInSn(request), FlowPurchaseInventoryDTO.class);
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        Map<String, List<FlowPurchaseInventoryDTO>> inventoryMap = list.stream().collect(Collectors.groupingBy(o -> o.getEid() + "_" + o.getYlGoodsId() + "_" + o.getGoodsInSn()));
        if(MapUtil.isEmpty(inventoryMap)){
            return ListUtil.empty();
        }

        List<FlowPurchaseInventoryBO> resultList = new ArrayList<>();
        for (Map.Entry<String, List<FlowPurchaseInventoryDTO>> entry: inventoryMap.entrySet()){
            List<FlowPurchaseInventoryDTO> inventoryList = entry.getValue();
            if(CollUtil.isEmpty(inventoryList)){
                continue;
            }

            if(inventoryList.size() > 2){
                log.error("流向库存数据异常, 参数：request{}; 异常数据：flowPurchaseInventoryList{}", request, inventoryList);
                throw new ServiceException("流向库存数据异常");
            }
            Map<Integer, List<FlowPurchaseInventoryDTO>> poSourceMap = inventoryList.stream().collect(Collectors.groupingBy(FlowPurchaseInventoryDTO::getPoSource));
            for(Map.Entry<Integer, List<FlowPurchaseInventoryDTO>> poSourceEntry: poSourceMap.entrySet()){
                if(CollUtil.isNotEmpty(poSourceEntry.getValue()) && poSourceEntry.getValue().size() > 1){
                    log.error("流向库存数据异常, 参数：request{}; 异常数据：flowPurchaseInventoryList{}", request, poSourceEntry.getValue());
                    throw new ServiceException("流向库存数据异常");
                }
            }

            FlowPurchaseInventoryBO bo = new FlowPurchaseInventoryBO();
            bo.setEid(inventoryList.get(0).getEid());
            bo.setYlGoodsId(inventoryList.get(0).getYlGoodsId());
            bo.setGoodsInSn(inventoryList.get(0).getGoodsInSn());
            for (FlowPurchaseInventoryDTO dto : inventoryList) {
                if(ObjectUtil.equal(FlowPurchaseInventorySourceEnum.DA_YUN_HE.getCode(), dto.getPoSource())){
                    bo.setDyhId(dto.getId());
                    bo.setDyhQuantity(dto.getPoQuantity());
                } else if(ObjectUtil.equal(FlowPurchaseInventorySourceEnum.JING_DONG.getCode(), dto.getPoSource())){
                    bo.setJdId(dto.getId());
                    bo.setJdQuantity(dto.getPoQuantity());
                }
            }
            resultList.add(bo);
        }

        return resultList;
    }

    @Test
    public void pageByEidAndYlGoodsIdTest() {
        QueryFlowPurchaseInventoryListPageRequest request = new QueryFlowPurchaseInventoryListPageRequest();
        request.setCurrent(1);
        request.setSize(10);
//        request.setYlGoodsId(2805L);
        request.setEid(4333L);
        Page<FlowPurchaseInventoryListPageDTO> page = flowPurchaseInventoryApi.pageByEidAndYlGoodsId(request);
        System.out.println(">>>>> page:" + JSON.toJSONString(page));
    }

    @Test
    public void adjustmentLogPageLogByInventoryIdTest() {
        QueryFlowPurchaseInventoryLogListPageRequest request = new QueryFlowPurchaseInventoryLogListPageRequest();
        request.setInventoryId(3L);
        request.setBusinessType(4);
        Page<FlowPurchaseInventoryLogListPageDTO> page = flowPurchaseInventoryApi.adjustmentLogPageLogByInventoryId(request);
        System.out.println(">>>>> page:" + JSON.toJSONString(page));
    }

    @Test
    public void getSumAdjustmentQuantityByInventoryIdListTest() {
        List<Long> list = new ArrayList<>();
        list.add(3L);
        list.add(4L);
        Map<Long, BigDecimal> map = flowPurchaseInventoryApi.getSumAdjustmentQuantityByInventoryIdList(list);
        System.out.println(">>>>> map:" + JSON.toJSONString(map));
    }

}
