package com.yiling.basic.location.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.location.entity.LocationDO;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@Slf4j
public class LocationServiceTest extends BaseTest {

    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationReaderService locationReaderService;

    @Test
    public void listByParentCode() {
        String parentCode = "";
        List<LocationDO> list = locationService.listByParentCode(parentCode);
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }

    @Test
    public void validate() {
        String provinceCode = "130104";
        String cityCode = "130105";
        String regionCode = "130107";
        boolean result = locationService.validateCode(provinceCode, cityCode, regionCode);
        log.info("result = {}", result);
    }

    @Test
    public void getByCodes() {
        String[] array = locationService.getNamesByCodes("110000", "1", "3");
        log.info("array = {}", JSONUtil.toJsonStr(array));
    }

    @Test
    public void getById() {
        LocationDO entity = locationService.getById(1L);
        log.info("entity = {}", entity);
        entity = locationReaderService.getById(1L);
        log.info("entity = {}", entity);
    }

    @Test
    public void save() {
        LocationDO entity = new LocationDO();
        entity.setCode("test");
        entity.setName("测试");
        entity.setParentCode("");
        entity.setPriority(0);
        locationService.save(entity);
    }
}
