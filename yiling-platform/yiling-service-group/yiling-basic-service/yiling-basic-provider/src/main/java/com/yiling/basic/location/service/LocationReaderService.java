package com.yiling.basic.location.service;

import java.util.List;

import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.basic.location.entity.LocationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 行政区划字典表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-17
 */
public interface LocationReaderService extends BaseService<LocationDO> {

    /**
     * 根据上级区域编码获取下级区域列表
     *
     * @param parentCode 上级区域编码
     * @return
     */
    List<LocationDO> listByParentCode(String parentCode);

    /**
     * 根据上级区域编码获取下级区域树结构列表
     *
     * @param parentCode 上级区域编码
     * @param deep 递归深度
     * @return
     */
    List<LocationTreeDTO> listTreeByParentCode(String parentCode, Integer deep);

    /**
     * 校验省市区编码
     *
     * @param provinceCode 省份编码
     * @param cityCode 城市编码
     * @param regionCode 区县编码
     * @return
     */
    boolean validateCode(String provinceCode, String cityCode, String regionCode);

    /**
     * 校验省市区名称
     *
     * @param provinceName 省份名称
     * @param cityName 城市名称
     * @param regionName 区县名称
     * @return
     */
    boolean validateName(String provinceName, String cityName, String regionName);

    /**
     * 根据省市区编码获取省市区名称
     *
     * @param provinceCode 省份编码
     * @param cityCode 城市编码
     * @param regionCode 区县编码
     * @return
     */
    String[] getNamesByCodes(String provinceCode, String cityCode, String regionCode);

    /**
     * 根据省市区名称获取省市区编码
     *
     * @param provinceName 省份名称
     * @param cityName 城市名称
     * @param regionName 区县名称
     * @return
     */
    String[] getCodesByNames(String provinceName, String cityName, String regionName);

    /**
     * 查询所有的省市区数据列表
     * @return
     */
    List<RegionFullViewDTO> getAllProvinceCityRegionList();

    /**
     * 根据区域编码获取省份名称
     * @param regionCode
     * @return
     */
    List<String> getProvinceNameByRegionCode(List<String> regionCode);

    /**
     * 根据编码获取上级区域信息
     * @param codeList
     * @return
     */
    List<LocationDTO> getParentByCodeList(List<String> codeList);
}
