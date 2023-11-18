package com.yiling.sjms.gb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.gb.dao.GbMainInfoMapper;
import com.yiling.sjms.gb.dto.request.UpdateMainInfoReviewTypeRequest;
import com.yiling.sjms.gb.entity.GbMainInfoDO;
import com.yiling.sjms.gb.service.GbMainInfoService;

/**
 * <p>
 * 团购信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Service
public class GbMainInfoServiceImpl extends BaseServiceImpl<GbMainInfoMapper, GbMainInfoDO> implements GbMainInfoService {


    @Override
    public GbMainInfoDO getOneByGbId(Long gbId) {
        QueryWrapper<GbMainInfoDO> wrapper = new QueryWrapper();
        wrapper.lambda().eq(GbMainInfoDO:: getGbId,gbId)
                .last(" limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<GbMainInfoDO> listByGbIds(List<Long> gbIds) {
        QueryWrapper<GbMainInfoDO> wrapper = new QueryWrapper();
        wrapper.lambda().in(GbMainInfoDO:: getGbId,gbIds);
        return list(wrapper);
    }

    @Override
    public Boolean updateByGbId(UpdateMainInfoReviewTypeRequest request) {
        QueryWrapper<GbMainInfoDO> wrapper = new QueryWrapper();
        wrapper.lambda().eq(GbMainInfoDO:: getGbId,request.getGbId());
        GbMainInfoDO one = new GbMainInfoDO();

        one.setGbReviewType(request.getGbReviewType());
        one.setGbCityBelow(request.getGbCityBelow());
        one.setOpUserId(request.getOpUserId());
        one.setOpTime(request.getOpTime());

        return  update(one,wrapper);
    }
}
