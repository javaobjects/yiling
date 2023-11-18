package com.yiling.sjms.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbGoodsInfoApi;
import com.yiling.sjms.gb.dto.GoodsInfoDTO;
import com.yiling.sjms.gb.service.GbGoodsInfoService;

/**
 * 团购商品信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
@DubboService
public class GbGoodsInfoApiImpl implements GbGoodsInfoApi {
    @Autowired
    private GbGoodsInfoService gbGoodsInfoService;

    @Override
    public List<GoodsInfoDTO> listByGbId(Long gbId) {
        return PojoUtils.map(gbGoodsInfoService.listByGbId(gbId),GoodsInfoDTO.class);
    }

    @Override
    public List<GoodsInfoDTO> listByGbIds(List<Long> gbIds) {
        return PojoUtils.map(gbGoodsInfoService.listByGbIds(gbIds),GoodsInfoDTO.class);
    }

    @Override
    public List<GoodsInfoDTO> listByCompanyIds(List<Long> companyIds) {
        return PojoUtils.map(gbGoodsInfoService.listByCompanyIds(companyIds),GoodsInfoDTO.class);
    }
}
