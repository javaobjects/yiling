package com.yiling.search.goods.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.search.BaseTest;
import com.yiling.search.goods.dao.EsGoodsRepository;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.search.goods.dto.request.EsGoodsInventoryIndexRequest;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;
import com.yiling.search.goods.entity.EsGoodsEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: fei.wu <br>
 * @date: 2021/6/11 <br>
 */
@Slf4j
public class EsGoodsSearchServiceTest extends BaseTest {


    @Autowired
    private EsGoodsSearchService esGoodsSearchService;

    @Autowired
    private EsGoodsRepository esGoodsRepository;

    @Test
    public void testSearchGoods() {
        List<EsGoodsEntity> entities = esGoodsRepository.findByNameAndEid("连花清瘟胶囊",22L);
        System.out.println(entities);

        EsGoodsSearchRequest request = new EsGoodsSearchRequest();
        request.setKey("连花清瘟");
        request.setMallStatus(1);
        request.setSize(10);
        request.setCurrent(1);
        EsAggregationDTO<GoodsInfoDTO> goodsFullDTOEsAggregationDTO = esGoodsSearchService.searchGoods(request);
        log.info(JSON.toJSONString(goodsFullDTOEsAggregationDTO));


        EsActivityGoodsSearchRequest request1 = new EsActivityGoodsSearchRequest();
        request1.setKey("连花清瘟胶囊");
        request1.setAllEidFlag(true);
        request1.setExcludeEids(Lists.list(22L));
        request1.setMallStatus(1);
        EsAggregationDTO<GoodsInfoDTO> aggregationDTO = esGoodsSearchService.searchActivityGoods(request1);
        System.out.println(JSON.toJSONString(aggregationDTO));

        EsGoodsEntity entity = esGoodsRepository.findById(String.valueOf(2760L)).get();
        System.out.println(entity);

        EsGoodsSearchRequest request2 = new EsGoodsSearchRequest();
        request2.setKey("胶囊");
        request2.setMallStatus(1);
        List<String> stringList = esGoodsSearchService.searchGoodsSuggest(request2);
        System.out.println(stringList);

    }

    @Test
    public void test1() {
        EsGoodsInventoryIndexRequest request=new EsGoodsInventoryIndexRequest();
        request.setGoodsId(2760L);
        request.setAvailableQty(2233L);
        System.out.println(esGoodsSearchService.updateQty(request));
    }

    @Test
    public void test2() {
        EsGoodsInventoryIndexRequest request=new EsGoodsInventoryIndexRequest();
        request.setGoodsId(3207L);
        request.setHasB2bStock(1);
        System.out.println(esGoodsSearchService.updateQtyFlag(request));
    }

    @Test
    public void creatIndexTest(){
        esGoodsSearchService.creatIndex();
    }
    @Test
    public void searchTest1(){
        EsActivityGoodsSearchRequest request1 = new EsActivityGoodsSearchRequest();
        //request1.setKey("连花清瘟胶囊");
        request1.setAllEidFlag(true);
        request1.setMallFlag(1);
        request1.setMallStatus(1);
        request1.setSortEid(Lists.list(324L));
        EsAggregationDTO<GoodsInfoDTO> aggregationDTO = esGoodsSearchService.searchActivityGoods(request1);
        System.out.println(JSON.toJSONString(aggregationDTO));
    }

}
