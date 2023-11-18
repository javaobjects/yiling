package com.yiling.sjms.gb.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.gb.api.GbOrgMangerApi;
import com.yiling.sjms.gb.entity.GbOrgManagerDO;
import com.yiling.sjms.gb.service.GbOrgManagerService;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 团购主要信息
 *
 * @author: shixing.sun
 * @date: 2022/11/29
 */
@DubboService
public class GbOrgMangerApiImpl implements GbOrgMangerApi {
    @Autowired
    private GbOrgManagerService gbOrgManagerService;

    @Override
    public Map<Long,String> getGBFormList() {
        List<GbOrgManagerDO> list = gbOrgManagerService.list();
        if (CollectionUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(GbOrgManagerDO::getOrgId, GbOrgManagerDO::getOrgName, (k1, k2) -> k2));
    }

    @Override
    public List<Long> listOrgIds() {
        return gbOrgManagerService.listOrgIds();
    }
}
