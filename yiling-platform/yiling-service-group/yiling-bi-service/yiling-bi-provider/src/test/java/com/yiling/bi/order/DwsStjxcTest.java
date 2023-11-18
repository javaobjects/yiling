package com.yiling.bi.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.bi.BaseTest;
import com.yiling.bi.order.bo.DwsStjxInventoryBO;
import com.yiling.bi.order.service.DwsStjxcService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/11/28
 */
@Slf4j
public class DwsStjxcTest extends BaseTest {

    @Autowired
    public DwsStjxcService dwsStjxcService;

    @Test
    public void getDwsStjxInventoryList() {
        List<DwsStjxInventoryBO> dwsStjxInventoryBOList = dwsStjxcService.getDwsStjxInventoryList("2022", "7");

        //  遍历updateCrmOrderList，匹配库存
        Map<String, BigDecimal> b2bInventoryMap = dwsStjxInventoryBOList.stream().collect(Collectors.toMap(d -> d.getCustomer() + "_" + d.getCrmGoodsid(), DwsStjxInventoryBO::getB2bInventory, (a, b) -> a));
        Map<String, BigDecimal> jdInventoryMap = dwsStjxInventoryBOList.stream().collect(Collectors.toMap(d -> d.getCustomer() + "_" + d.getCrmGoodsid(), DwsStjxInventoryBO::getJdInventory, (a, b) -> a));

    }
}
