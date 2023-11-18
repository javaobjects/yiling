package com.yiling.basic.location.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.basic.location.entity.LocationDO;
import com.yiling.basic.location.service.LocationReaderService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 行政区划 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/20
 */
@DubboService
public class LocationApiImpl implements LocationApi {

    @Autowired
    LocationReaderService locationReaderService;

    @Override
    public List<LocationDTO> listByParentCode(String parentCode) {
        List<LocationDO> list = locationReaderService.listByParentCode(parentCode);
        return PojoUtils.map(list, LocationDTO.class);
    }

    @Override
    public List<LocationTreeDTO> listTreeByParentCode(String parentCode, Integer deep) {
        return locationReaderService.listTreeByParentCode(parentCode, deep);
    }

    @Override
    public boolean validateCode(String provinceCode, String cityCode, String regionCode) {
        return locationReaderService.validateCode(provinceCode, cityCode, regionCode);
    }

    @Override
    public boolean validateName(String provinceName, String cityName, String regionName) {
        return locationReaderService.validateName(provinceName, cityName, regionName);
    }

    @Override
    public String[] getNamesByCodes(String provinceCode, String cityCode, String regionCode) {
        return locationReaderService.getNamesByCodes(provinceCode, cityCode, regionCode);
    }

    @Override
    public String[] getCodesByNames(String provinceName, String cityName, String regionName) {
        return locationReaderService.getCodesByNames(provinceName, cityName, regionName);
    }

    @Override
    public List<RegionFullViewDTO> getAllProvinceCityRegionList() {
        return locationReaderService.getAllProvinceCityRegionList();
    }

    @Override
    public List<String> getProvinceNameByRegionCode(List<String> regionCode) {
        return locationReaderService.getProvinceNameByRegionCode(regionCode);
    }

    @Override
    public List<LocationDTO> getParentByCodeList(List<String> codeList) {
        return locationReaderService.getParentByCodeList(codeList);
    }


}
