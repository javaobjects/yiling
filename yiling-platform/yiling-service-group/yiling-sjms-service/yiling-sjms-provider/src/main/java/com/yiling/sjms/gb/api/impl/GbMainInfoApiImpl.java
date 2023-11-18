package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbMainInfoApi;
import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.dto.request.UpdateMainInfoReviewTypeRequest;
import com.yiling.sjms.gb.service.GbMainInfoService;

/**
 * 团购主要信息
 *
 * @author: wei.wang
 * @date: 2022/11/29
 */
@DubboService
public class GbMainInfoApiImpl implements GbMainInfoApi {
    @Autowired
    private GbMainInfoService gbMainInfoService;

    @Override
    public MainInfoDTO getOneByGbId(Long gbId) {
        return PojoUtils.map(gbMainInfoService.getOneByGbId(gbId),MainInfoDTO.class);
    }

    @Override
    public List<MainInfoDTO> listByGbIds(List<Long> gbIds) {
        return PojoUtils.map(gbMainInfoService.listByGbIds(gbIds),MainInfoDTO.class);
    }

    @Override
    public Boolean updateByGbId(UpdateMainInfoReviewTypeRequest request) {
        return gbMainInfoService.updateByGbId(request);
    }
}
