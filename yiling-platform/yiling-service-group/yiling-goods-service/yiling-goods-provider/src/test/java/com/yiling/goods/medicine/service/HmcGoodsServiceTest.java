package com.yiling.goods.medicine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.goods.BaseTest;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.HmcSaveOrUpdateGoodsRequest;

/**
 * @author shichen
 * @类名 HmcGoodsServiceTest
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
public class HmcGoodsServiceTest extends BaseTest {
    @Autowired
    private HmcGoodsService hmcGoodsService;

    @Test
    public void generateTest(){
        HmcSaveOrUpdateGoodsRequest request = new HmcSaveOrUpdateGoodsRequest();
        request.setEid(51L);
        request.setEname("测试注册企业2");
        request.setEnterpriseType(1);
        request.setSellSpecificationsId(40483L);
        hmcGoodsService.generateGoods(request);
    }

    @Test
    public void batchQueryGoodsBasicTest(){
        List<Long> goodIds = Lists.newArrayList(9994L, 9995L, 9996L, 9997L, 9999L);
        List<StandardGoodsBasicDTO> list1 = hmcGoodsService.batchQueryStandardGoodsBasic(goodIds);
        System.out.println(list1.toString());
        List<Long> specificationsIds = Lists.newArrayList(41188L, 41136L, 41187L, 41190L, 40483L);
        List<StandardGoodsBasicDTO> list2 = hmcGoodsService.batchQueryStandardGoodsBasicBySpecificationsIds(specificationsIds);
        System.out.println(list2.toString());
    }

    @Test
    public void updateGoodsInventoryTest(){
        hmcGoodsService.updateGoodsInventoryBySku(250L,100L,0L);

        AddOrSubtractQtyRequest request2 = new AddOrSubtractQtyRequest();
        request2.setSkuId(250L);
        request2.setInventoryId(1518L);
        request2.setFrozenQty(200L);
        request2.setOpUserId(0L);
        request2.setOpTime(new Date());
        hmcGoodsService.addHmcFrozenQty(request2);

        AddOrSubtractQtyRequest request1 = new AddOrSubtractQtyRequest();
        request1.setSkuId(250L);
        request1.setInventoryId(1518L);
        request1.setFrozenQty(200L);
        request1.setQty(200L);
        request1.setOpUserId(0L);
        request1.setOpTime(new Date());
        hmcGoodsService.subtractFrozenQtyAndQty(request1);

    }
}
