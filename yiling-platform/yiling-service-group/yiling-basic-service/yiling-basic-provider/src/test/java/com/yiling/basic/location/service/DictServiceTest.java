package com.yiling.basic.location.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.dict.entity.DictDataDO;
import com.yiling.basic.dict.service.DictDataService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Slf4j
public class DictServiceTest extends BaseTest {

    @Autowired
    DictDataService dictDataService;

    @Test
    public void getEnabledList() {
        List<DictDataDO> list = dictDataService.getEnabledList();
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }

    @Test
    public void test() {
        List<DictDataDO> list = dictDataService.getEnabledByTypeIdList(1L);
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }

    @Test
    public void test1() {
        Boolean bool = dictDataService.enabledDictData(1L, 1L);
        log.info("list = {}", JSONUtil.toJsonStr(bool));
    }
}
