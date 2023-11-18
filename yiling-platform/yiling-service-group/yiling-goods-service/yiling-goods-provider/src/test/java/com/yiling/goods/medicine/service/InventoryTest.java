package com.yiling.goods.medicine.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.goods.BaseTest;

import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.service.InventoryService;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
public class InventoryTest extends BaseTest {

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void addInventoryTest(){
        List<AddOrSubtractQtyRequest> requestList = new ArrayList<>();
        AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
        request.setInventoryId(1546L);
        request.setFrozenQty(300L);
        request.setSkuId(281L);
        request.setOrderNo("DDCESHIAA123456");
        request.setOpTime(new Date());
        request.setOpUserId(0L);
        requestList.add(request);
        int i = inventoryService.batchAddFrozenQty(requestList);
        System.out.println(i);
    }

    @Test
    public void getInventoryBySkuTest(){
        Map<Long, InventoryDTO> map = inventoryService.getMapBySkuIds(Lists.newArrayList(280L, 281L));
        InventoryDTO dto = inventoryService.getBySkuId(280L);
        System.out.println(map);
        System.out.println(dto);
    }
}
