package com.yiling.job.executor.service.jobhandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsSaleVolumeApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.BatchSaveGoodsSaleVolumeRequest;
import com.yiling.job.executor.log.JobLog;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderTypeGoodsQuantityDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsSaleVolumeJobHandler
 * @描述
 * @创建时间 2023/5/10
 * @修改人 shichen
 * @修改时间 2023/5/10
 **/
@Component
@Slf4j
public class GoodsSaleVolumeJobHandler {

    @DubboReference(timeout = 1000*60)
    private OrderApi orderApi;

    @DubboReference(timeout = 1000*60)
    private GoodsApi goodsApi;

    @DubboReference(timeout = 1000*60)
    private GoodsSaleVolumeApi goodsSaleVolumeApi;

    @JobLog
    @XxlJob("statisticGoodsSaleVolume")
    public ReturnT<String> statisticGoodsSaleVolume(String param) {
        log.info("job任务开始：统计前一日商品销量");
        long start = System.currentTimeMillis();
        DateTime today = DateUtil.parseDateTime(DateUtil.today()+" 12:00:00");
        DateTime yesterday = DateUtil.offsetDay(today,-1);
        QueryOrderPageRequest request = new QueryOrderPageRequest();
        request.setStartCreateTime( DateUtil.beginOfDay(yesterday));
        request.setEndCreateTime( DateUtil.endOfDay(yesterday));
        List<OrderTypeGoodsQuantityDTO> goodsCountList = orderApi.getCountOrderTypeQuantity(request);
        if(CollectionUtil.isNotEmpty(goodsCountList)){
            List<Long> goodsIds = goodsCountList.stream().map(OrderTypeGoodsQuantityDTO::getDistributorGoodsId).distinct().collect(Collectors.toList());
            List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIds);
            Map<Long, GoodsDTO> goodsMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));

            BatchSaveGoodsSaleVolumeRequest saveRequest = new BatchSaveGoodsSaleVolumeRequest();
            saveRequest.setSaleDate(yesterday);
            List<BatchSaveGoodsSaleVolumeRequest.SaveGoodsSaleVolumeRequest> saveList = ListUtil.toList();
            goodsCountList.forEach(goodsCount->{
                GoodsDTO goodsDTO = goodsMap.get(goodsCount.getDistributorGoodsId());
                if(null!=goodsDTO){
                    BatchSaveGoodsSaleVolumeRequest.SaveGoodsSaleVolumeRequest save = new BatchSaveGoodsSaleVolumeRequest.SaveGoodsSaleVolumeRequest();
                    save.setGoodsId(goodsCount.getDistributorGoodsId());
                    save.setGoodsLine(goodsCount.getOrderType());
                    save.setVolume(goodsCount.getGoodsQuantity());
                    save.setStandardId(goodsDTO.getStandardId());
                    save.setSellSpecificationsId(goodsDTO.getSellSpecificationsId());
                    saveList.add(save);
                }
            });
            saveRequest.setGoodsSaleVolumeList(saveList);
            Boolean saveFlag = goodsSaleVolumeApi.batchSaveSaleVolume(saveRequest);
            if(!saveFlag){
                log.error("统计前一日商品销量失败，统计数据：{}", JSONUtil.toJsonStr(goodsCountList));
            }
        }
        log.info("job任务结束：统计前一日商品销量。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}
