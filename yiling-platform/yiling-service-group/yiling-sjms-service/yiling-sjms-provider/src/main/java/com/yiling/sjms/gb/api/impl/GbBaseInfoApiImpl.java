package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbBaseInfoApi;
import com.yiling.sjms.gb.dto.BaseInfoDTO;
import com.yiling.sjms.gb.service.GbBaseInfoService;


/**
 * 团购基础信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbBaseInfoApiImpl implements GbBaseInfoApi {
    @Autowired
    private GbBaseInfoService gbBaseInfoService;

    @Override
    public BaseInfoDTO getOneByGbId(Long gbId) {
        return PojoUtils.map(gbBaseInfoService.getOneByGbId(gbId),BaseInfoDTO.class);
    }

    @Override
    public List<BaseInfoDTO> listByGbIds(List<Long> gbIds) {
        return PojoUtils.map(gbBaseInfoService.listByGbIds(gbIds),BaseInfoDTO.class);
    }
}
