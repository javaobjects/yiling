package com.yiling.basic.location.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.location.dao.LocationMapper;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.dto.RegionFullViewDTO;
import com.yiling.basic.location.entity.LocationDO;
import com.yiling.basic.location.service.LocationReaderService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.mybatis.config.DataSourceConfiguration;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 行政区划字典表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-17
 */
@Service("locationReaderService")
@DS(DataSourceConfiguration.SLAVE_DATA_SOURCE_NAME)
public class LocationReaderServiceImpl extends BaseServiceImpl<LocationMapper, LocationDO> implements LocationReaderService {

    private static final Integer THREE = 3;

    @Override
    public List<LocationDO> listByParentCode(String parentCode) {
        LambdaQueryWrapper<LocationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LocationDO::getParentCode, parentCode).orderByAsc(LocationDO::getPriority, LocationDO::getId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<LocationTreeDTO> listTreeByParentCode(String parentCode, Integer deep) {
        List<LocationTreeDTO> locationTreeDTOS = CollUtil.newArrayList();

        LambdaQueryWrapper<LocationDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(LocationDO::getStatus, EnableStatusEnum.ENABLED.getCode())
                .orderByAsc(LocationDO::getPriority, LocationDO::getId);
        List<LocationDO> locationDOList = this.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(locationDOList)){
            return locationTreeDTOS;
        }

        locationTreeDTOS = PojoUtils.map(locationDOList, LocationTreeDTO.class);

        // 2.配置
        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("code");
        //默认为parentId可以不设置
        config.setParentIdKey("parentCode");
        //最大递归深度
        config.setDeep(deep);
        //排序字段
        config.setWeightKey("priority");

        List<Tree<String>> treeList = TreeUtil.build(locationTreeDTOS, parentCode, config, (treeNode, tree) -> {
            tree.setId(treeNode.getCode());
            tree.setParentId(treeNode.getParentCode());
            tree.setWeight(treeNode.getPriority());
            tree.setName(treeNode.getName());
            // 扩展属性 ...
            // tree.putExtra("other", new Object());
        });

        locationTreeDTOS = PojoUtils.map(treeList, LocationTreeDTO.class);
        return locationTreeDTOS;
    }

    @Override
    public boolean validateCode(String provinceCode, String cityCode, String regionCode) {
        QueryWrapper<LocationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(LocationDO::getCode, ListUtil.list(true, provinceCode, cityCode, regionCode));

        List<LocationDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list) || list.size() < THREE) {
            return false;
        }

        LocationDO province = list.stream().filter(e -> StrUtil.isEmpty(e.getParentCode())).findFirst().orElse(null);
        if (province == null || !province.getCode().equals(provinceCode)) {
            return false;
        }

        LocationDO city = list.stream().filter(e -> e.getParentCode().equals(provinceCode)).findFirst().orElse(null);
        if (city == null || !city.getCode().equals(cityCode)) {
            return false;
        }

        LocationDO region = list.stream().filter(e -> e.getParentCode().equals(cityCode)).findFirst().orElse(null);
        if (region == null || !region.getCode().equals(regionCode)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateName(String provinceName, String cityName, String regionName) {
        QueryWrapper<LocationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(LocationDO::getName, ListUtil.list(true, provinceName, cityName, regionName));

        List<LocationDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list) || list.size() < THREE) {
            return false;
        }

        LocationDO province = list.stream().filter(e -> StrUtil.isEmpty(e.getParentCode())).findFirst().orElse(null);
        if (province == null || !province.getName().equals(provinceName)) {
            return false;
        }

        LocationDO city = list.stream().filter(e -> e.getParentCode().equals(province.getCode())).findFirst().orElse(null);
        if (city == null || !city.getName().equals(cityName)) {
            return false;
        }

        LocationDO region = list.stream().filter(e -> e.getParentCode().equals(city.getCode())).findFirst().orElse(null);
        if (region == null || !region.getName().equals(regionName)) {
            return false;
        }

        return true;
    }

    @Override
    public String[] getNamesByCodes(String provinceCode, String cityCode, String regionCode) {
        QueryWrapper<LocationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(LocationDO::getCode, ListUtil.list(true, provinceCode, cityCode, regionCode));
        List<LocationDO> list = this.list(queryWrapper);

        LocationDO province = list.stream().filter(e -> e.getCode().equals(provinceCode)).findFirst().orElse(null);
        LocationDO city = list.stream().filter(e -> e.getCode().equals(cityCode)).findFirst().orElse(null);
        LocationDO region = list.stream().filter(e -> e.getCode().equals(regionCode)).findFirst().orElse(null);

        String[] array = new String[3];
        array[0] = province != null ? province.getName() : "";
        array[1] = city != null ? city.getName() : "";
        array[2] = region != null ? region.getName() : "";
        return array;
    }

    @Override
    public String[] getCodesByNames(String provinceName, String cityName, String regionName) {
        QueryWrapper<LocationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(LocationDO::getName, ListUtil.list(true, provinceName, cityName, regionName));
        List<LocationDO> list = this.list(queryWrapper);

        LocationDO province = list.stream().filter(e -> StrUtil.isEmpty(e.getParentCode())).findFirst().orElse(null);
        LocationDO city = list.stream().filter(e -> province != null && e.getParentCode().equals(province.getCode())).findFirst().orElse(null);
        LocationDO region = list.stream().filter(e -> city != null && e.getParentCode().equals(city.getCode())).findFirst().orElse(null);

        String[] array = new String[3];
        array[0] = province != null ? province.getCode() : "";
        array[1] = city != null ? city.getCode() : "";
        array[2] = region != null ? region.getCode() : "";
        return array;
    }

    @Override
    public List<RegionFullViewDTO> getAllProvinceCityRegionList() {
        return this.baseMapper.getAllProvinceCityRegionList();
    }

    @Override
    public List<String> getProvinceNameByRegionCode(List<String> regionCodeList) {
        if (CollUtil.isEmpty(regionCodeList)) {
            return ListUtil.toList();
        }

        // 获取到城市编码list
        List<String> cityCodeList = getByCodeList(regionCodeList).stream().map(LocationDO::getParentCode).distinct().collect(Collectors.toList());
        // 获取到省份list
        List<String> provinceCodeList = getByCodeList(cityCodeList).stream().map(LocationDO::getParentCode).distinct().collect(Collectors.toList());

        List<LocationDO> provinceLocation = getByCodeList(provinceCodeList);
        return provinceLocation.stream().map(LocationDO::getName).collect(Collectors.toList());
    }

    @Override
    public List<LocationDTO> getParentByCodeList(List<String> codeList) {
        if (CollUtil.isEmpty(codeList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<LocationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LocationDO::getCode,codeList);
        List<String> parentCodeList = this.list(wrapper).stream().map(LocationDO::getParentCode).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(parentCodeList)) {
            return ListUtil.toList();
        }

        wrapper.clear();
        wrapper.in(LocationDO::getCode,parentCodeList);
        return PojoUtils.map(this.list(wrapper),LocationDTO.class);
    }

    private List<LocationDO> getByCodeList(List<String> regionCodeList){
        LambdaQueryWrapper<LocationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(LocationDO::getCode,regionCodeList);
        List<LocationDO> locationDOList = list(queryWrapper);
        if (CollUtil.isEmpty(locationDOList)) {
            return ListUtil.toList();
        }
        return locationDOList;
    }
}
