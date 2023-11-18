package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.mall.order.service.OrderFlowService;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
@Service
public class OrderFlowServiceImpl implements OrderFlowService {

    @DubboReference
    OrderDeliveryApi orderDeliveryApi;

    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    DictDataApi dictDataApi;

    @Override
    public Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request) {
        //查询发货单信息
        Page<OrderFlowBO> orderFlowBOList = orderDeliveryApi.getPageList(request);
        //获取渠道id集合
        List<DictDataBO> dictDataBOList=dictDataApi.getEnabledByTypeIdList(1L);
        Map<String,String> dictDataMap=dictDataBOList.stream().collect(Collectors.toMap(DictDataBO::getValue,DictDataBO::getLabel));

        //取货商品id集合
        List<Long> goodsIds = orderFlowBOList.getRecords().stream().distinct().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<StandardGoodsBasicDTO> standardGoodsBasicDTOList=goodsApi.batchQueryStandardGoodsBasic(goodsIds);
        Map<Long,StandardGoodsBasicDTO> standardGoodsBasicDTOMap=standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //按照批次信息分组
        List<Long> detailIds=orderFlowBOList.getRecords().stream().map(e->e.getDetailId()).collect(Collectors.toList());

        List<ReturnDetailBathDTO> returnDetailBathDTOList=orderReturnDetailBatchApi.queryByDetailId(detailIds);
        returnDetailBathDTOList=returnDetailBathDTOList.stream().filter(e->!e.getReturnType().equals(1)).collect(Collectors.toList());
        Map<String,List<ReturnDetailBathDTO>> returnDetailBathDTOMap=new HashMap<>();
        for(ReturnDetailBathDTO returnDetailBathDTO:returnDetailBathDTOList){
            String key=getReturnKey(returnDetailBathDTO);
            List<ReturnDetailBathDTO> list= null;
            if(returnDetailBathDTOMap.containsKey(key)){
                list= returnDetailBathDTOMap.get(key);
                list.add(returnDetailBathDTO);
            }else{
                list=new ArrayList<>();
                list.add(returnDetailBathDTO);
                returnDetailBathDTOMap.put(key,list);
            }
        }

        orderFlowBOList.getRecords().forEach(e->{
            String buyerReturnKey=e.getDetailId()+"3"+e.getBatchNo();
            String returnKey=e.getDetailId()+"2"+e.getBatchNo();
            List<ReturnDetailBathDTO> buyerReturnDTOList=returnDetailBathDTOMap.getOrDefault(buyerReturnKey,ListUtil.empty());
            Integer buyerReturnQuantity=0;
            for(ReturnDetailBathDTO returnDetailBathDTO:buyerReturnDTOList){
                buyerReturnQuantity=buyerReturnQuantity+returnDetailBathDTO.getReturnQuantity();
            }
            List<ReturnDetailBathDTO> returnDTOList=returnDetailBathDTOMap.getOrDefault(returnKey,ListUtil.empty());
            Integer returnQuantity=0;
            for(ReturnDetailBathDTO returnDetailBathDTO:returnDTOList){
                returnQuantity=returnQuantity+returnDetailBathDTO.getReturnQuantity();
            }
            e.setBuyerReturnQuantity(buyerReturnQuantity);
            e.setReturnQuantity(returnQuantity);
            e.setBuyerChannelName(dictDataMap.get(String.valueOf(e.getBuyerChannelId())));
            e.setSellUnit(standardGoodsBasicDTOMap.get(e.getGoodsId()).getSellUnit());
//            e.setGoodsInSn(standardGoodsBasicDTOMap.get(e.getGoodsId()).getInSn());
            e.setGoodsAmount(new BigDecimal(e.getDeliveryQuantity()-buyerReturnQuantity-returnQuantity).multiply(e.getGoodsPrice()));
        });

        return orderFlowBOList;
    }

    public String getReturnKey(ReturnDetailBathDTO returnDetailBathDTO){
        return returnDetailBathDTO.getDetailId()+""+returnDetailBathDTO.getReturnType()+""+returnDetailBathDTO.getBatchNo();
    }
}
