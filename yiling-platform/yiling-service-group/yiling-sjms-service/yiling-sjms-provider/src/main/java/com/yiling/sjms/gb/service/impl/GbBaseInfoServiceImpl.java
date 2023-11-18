package com.yiling.sjms.gb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.gb.dao.GbBaseInfoMapper;
import com.yiling.sjms.gb.entity.GbBaseInfoDO;
import com.yiling.sjms.gb.service.GbBaseInfoService;

/**
 * <p>
 * 团购基本信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbBaseInfoServiceImpl extends BaseServiceImpl<GbBaseInfoMapper, GbBaseInfoDO> implements GbBaseInfoService {

    @Override
    public GbBaseInfoDO getOneByGbId(Long gbId) {
        QueryWrapper<GbBaseInfoDO> wrapper = new QueryWrapper();
        wrapper.lambda().eq(GbBaseInfoDO:: getGbId,gbId)
                .last(" limit 1");

        return getOne(wrapper);
    }

    @Override
    public List<GbBaseInfoDO> listByGbIds(List<Long> gbIds) {
        QueryWrapper<GbBaseInfoDO> wrapper = new QueryWrapper();
        wrapper.lambda().in(GbBaseInfoDO:: getGbId,gbIds);
        return list(wrapper);
    }
}
