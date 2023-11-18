package com.yiling.sjms.gb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.gb.dao.GbGoodsInfoMapper;
import com.yiling.sjms.gb.entity.GbGoodsInfoDO;
import com.yiling.sjms.gb.service.GbGoodsInfoService;

/**
 * <p>
 * 团购商品信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbGoodsInfoServiceImpl extends BaseServiceImpl<GbGoodsInfoMapper, GbGoodsInfoDO> implements GbGoodsInfoService {


    @Override
    public Integer deleteGoodsByGbId(Long gbId) {
        GbGoodsInfoDO one = new GbGoodsInfoDO();
        QueryWrapper<GbGoodsInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbGoodsInfoDO:: getGbId,gbId);
        return  batchDeleteWithFill(one,wrapper);
    }

    @Override
    public List<GbGoodsInfoDO> listByGbId(Long gbId) {
        QueryWrapper<GbGoodsInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbGoodsInfoDO:: getGbId,gbId);
        return list(wrapper);
    }

    @Override
    public List<GbGoodsInfoDO> listByGbIds(List<Long> gbIds) {
        QueryWrapper<GbGoodsInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbGoodsInfoDO:: getGbId,gbIds);
        return list(wrapper);
    }

    @Override
    public List<GbGoodsInfoDO> listByCompanyId(Long companyId) {
        QueryWrapper<GbGoodsInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbGoodsInfoDO :: getCompanyId,companyId);
        return list(wrapper);
    }

    @Override
    public List<GbGoodsInfoDO> listByCompanyIds(List<Long> companyIds) {
        QueryWrapper<GbGoodsInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbGoodsInfoDO :: getCompanyId,companyIds);
        return list(wrapper);
    }
}
