package com.yiling.user.system.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.util.LocationTreeUtils;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.system.dao.UserSalesAreaDetailMapper;
import com.yiling.user.system.dao.UserSalesAreaMapper;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.entity.UserSalesAreaDO;
import com.yiling.user.system.entity.UserSalesAreaDetailDO;
import com.yiling.user.system.service.UserSalesAreaService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 用户销售区域 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-09-28
 */
@Service
public class UserSalesAreaServiceImpl extends BaseServiceImpl<UserSalesAreaMapper, UserSalesAreaDO> implements UserSalesAreaService {

    @Autowired
    private UserSalesAreaDetailMapper userSalesAreaDetailMapper;

    @DubboReference
    LocationApi locationApi;

    @Override
    public List<String> listSalesAreaCodeByUserId(Long userId) {
        QueryWrapper<UserSalesAreaDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserSalesAreaDetailDO::getUserId, userId);
        List<UserSalesAreaDetailDO> list = userSalesAreaDetailMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(UserSalesAreaDetailDO::getAreaCode).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserSalesArea(SaveUserSalesAreaRequest request) {
        Long userId = request.getUserId();
        Long opUserId = request.getOpUserId();

        // 销售区域是否全国：0-否 1-是
        int salesAreaAllFlag = 0;
        if (CollUtil.isEmpty(request.getSalesAreaTree())) {
            salesAreaAllFlag = 1;
        }

        String description = salesAreaAllFlag == 0 ? LocationTreeUtils.getLocationTreeDesc(request.getSalesAreaTree(), 2) : "全国";
        String jsonContent = salesAreaAllFlag == 0 ? JSONUtil.toJsonStr(request.getSalesAreaTree()) : "";

        //获取销售区域Json
        UserSalesAreaDO userSalesAreaDO = Optional.ofNullable(this.getSaleAreaByUserId(userId)).orElse(new UserSalesAreaDO());
        userSalesAreaDO.setUserId(userId);
        userSalesAreaDO.setDescription(description);
        userSalesAreaDO.setJsonContent(jsonContent);
        userSalesAreaDO.setSalesAreaAllFlag(salesAreaAllFlag);
        userSalesAreaDO.setOpUserId(request.getOpUserId());
        this.saveOrUpdate(userSalesAreaDO);

        List<String> regionCodeList = ListUtil.toList();
        if (salesAreaAllFlag == 0) {
            List<LocationTreeDTO> cityList = request.getSalesAreaTree().stream().map(LocationTreeDTO::getChildren).flatMap(Collection::stream).collect(Collectors.toList());
            List<LocationTreeDTO> regionList = cityList.stream().map(LocationTreeDTO::getChildren).flatMap(Collection::stream).collect(Collectors.toList());
            regionCodeList = regionList.stream().map(LocationTreeDTO::getCode).distinct().collect(Collectors.toList());
        }

        // 原始用户销售区域（区）编码列表
        List<String> userSalesAreaCodes = this.listSalesAreaCodeByUserId(userId);
        if (CollUtil.isEmpty(regionCodeList)) {
            if (CollUtil.isEmpty(userSalesAreaCodes)) {
                return true;
            }

            return this.removeUserSalesAreaCodes(userId, userSalesAreaCodes, opUserId);
        }

        if (CollUtil.isEmpty(userSalesAreaCodes)) {
            return this.addUserSalesAreaCodes(userId, regionCodeList, opUserId);
        }

        // 移除
        List<String> finalCityCodeList = regionCodeList;
        List<String> removeAreaCodes = userSalesAreaCodes.stream().filter(e -> !finalCityCodeList.contains(e)).distinct().collect(Collectors.toList());
        this.removeUserSalesAreaCodes(userId, removeAreaCodes, opUserId);

        // 新增
        List<String> addAreaCodes = regionCodeList.stream().filter(e -> !userSalesAreaCodes.contains(e)).distinct().collect(Collectors.toList());
        this.addUserSalesAreaCodes(userId, addAreaCodes, opUserId);

        return true;
    }

    @Override
    public boolean hasUserSalesAreaSetting(Long userId) {
        QueryWrapper<UserSalesAreaDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserSalesAreaDO::getUserId, userId).last("limit 1");
        int count = this.count(queryWrapper);
        return count > 0;
    }

    private boolean removeUserSalesAreaCodes(Long userId, List<String> areaCodes, Long opUserId) {
        if (CollUtil.isEmpty(areaCodes)) {
            return true;
        }

        QueryWrapper<UserSalesAreaDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserSalesAreaDetailDO::getUserId, userId)
                .in(UserSalesAreaDetailDO::getAreaCode, areaCodes);

        UserSalesAreaDetailDO entity = new UserSalesAreaDetailDO();
        entity.setOpUserId(opUserId);

        return userSalesAreaDetailMapper.batchDeleteWithFill(entity, queryWrapper) > 0;
    }

    private boolean addUserSalesAreaCodes(Long userId, List<String> areaCodes, Long opUserId) {
        if (CollUtil.isEmpty(areaCodes)) {
            return true;
        }

        List<UserSalesAreaDetailDO> list = CollUtil.newArrayList();
        for (String areaCode : areaCodes) {
            UserSalesAreaDetailDO entity = new UserSalesAreaDetailDO();
            entity.setUserId(userId);
            entity.setAreaCode(areaCode);
            entity.setOpUserId(opUserId);
            list.add(entity);
        }

        return this.userSalesAreaDetailMapper.addUserSalesAreaDetail(list) > 0;
    }

    @Override
    public UserSalesAreaDO getSaleAreaByUserId(Long userId) {
        QueryWrapper<UserSalesAreaDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserSalesAreaDO::getUserId, userId).last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 根据用户ID获取用户销售区域编码，可获取城市和省份级别
     * @param userId 用户ID
     * @param level 根据Level指定，若要获取市级编码则level传入2，若要获取省级编码则level传入1，不传默认获取市级编码
     * @return 返回市级编码
     */
    @Override
    public List<String> getSaleAreaDetailByUserId(Long userId , Integer level) {
        LambdaQueryWrapper<UserSalesAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSalesAreaDetailDO::getUserId,userId);
        List<UserSalesAreaDetailDO> detailDOList = this.userSalesAreaDetailMapper.selectList(wrapper);
        if (CollUtil.isEmpty(detailDOList)) {
            return ListUtil.toList();
        }
        if (Objects.nonNull(level) && level == 3) {
            return detailDOList.stream().map(UserSalesAreaDetailDO::getAreaCode).collect(Collectors.toList());
        }

        List<String> codeList = detailDOList.stream().map(UserSalesAreaDetailDO::getAreaCode).collect(Collectors.toList());
        level = Objects.nonNull(level) ? level : 2;
        List<LocationDTO> cityLocationList = locationApi.getParentByCodeList(codeList);
        List<String> cityCodeList = cityLocationList.stream().map(LocationDTO::getCode).collect(Collectors.toList());
        if (level == 1) {
            List<LocationDTO> provinceLocationList = locationApi.getParentByCodeList(cityCodeList);
            return provinceLocationList.stream().map(LocationDTO::getCode).collect(Collectors.toList());
        } else {
            return cityCodeList;
        }

    }

}
