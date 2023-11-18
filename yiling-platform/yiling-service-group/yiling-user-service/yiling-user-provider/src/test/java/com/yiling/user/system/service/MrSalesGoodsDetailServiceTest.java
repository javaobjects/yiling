package com.yiling.user.system.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.BaseTest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/6/10
 */
@Slf4j
public class MrSalesGoodsDetailServiceTest extends BaseTest {

    @Autowired
    MrSalesGoodsDetailService mrSalesGoodsDetailService;

    @Test
    public void listByEmployeeIds() {
        Map<Long, List<Long>> map = mrSalesGoodsDetailService.listByEmployeeIds(ListUtil.toList(1L));
        log.info("map = {}", JSONUtil.toJsonStr(map));
    }
}
