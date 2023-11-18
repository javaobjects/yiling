package com.yiling.hmc.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.common.dao.AdvertisementMapper;
import com.yiling.hmc.common.dto.request.AdvertisementDeleteRequest;
import com.yiling.hmc.common.dto.request.AdvertisementListRequest;
import com.yiling.hmc.common.dto.request.AdvertisementPageRequest;
import com.yiling.hmc.common.dto.request.AdvertisementSaveRequest;
import com.yiling.hmc.common.entity.AdvertisementDO;
import com.yiling.hmc.common.service.AdvertisementService;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 广告表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Service
public class AdvertisementServiceImpl extends BaseServiceImpl<AdvertisementMapper, AdvertisementDO> implements AdvertisementService {

    @Override
    public boolean saveAdvertisement(AdvertisementSaveRequest request) {
        AdvertisementDO advertisementDO = PojoUtils.map(request, AdvertisementDO.class);
        if (null != request.getId() && null == request.getStartTime() && null == request.getStopTime()) {
            DateTime timeZero = DateUtil.parse("1970-01-01 00:00:00");
            advertisementDO.setStartTime(timeZero);
            advertisementDO.setStopTime(timeZero);
        }
        return this.saveOrUpdate(advertisementDO);
    }

    @Override
    public boolean deleteAdvertisement(AdvertisementDeleteRequest request) {
        AdvertisementDO advertisementDO = PojoUtils.map(request, AdvertisementDO.class);
        return this.deleteByIdWithFill(advertisementDO) > 0;
    }

    @Override
    public Page<AdvertisementDO> pageList(AdvertisementPageRequest request) {
        QueryWrapper<AdvertisementDO> wrapper = new QueryWrapper<>();
        if (null != request.getPosition() && 0 != request.getPosition()) {
            wrapper.lambda().like(AdvertisementDO::getPosition, request.getPosition() + "");
        }
        wrapper.lambda().orderByDesc(AdvertisementDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public List<AdvertisementDO> listByCondition(AdvertisementListRequest request) {
        QueryWrapper<AdvertisementDO> wrapper = new QueryWrapper<>();
        if (null != request.getPosition() && 0 != request.getPosition()) {
            wrapper.lambda().like(AdvertisementDO::getPosition, request.getPosition() + "");
        }
        Date now = new Date();
        DateTime timeZero = DateUtil.parse("1970-01-01 00:00:00");
        wrapper.lambda().and(m -> m.le(AdvertisementDO::getStartTime, now).ge(AdvertisementDO::getStopTime, now).or(w -> w.eq(AdvertisementDO::getStartTime, timeZero).eq(AdvertisementDO::getStopTime, timeZero)));
        wrapper.lambda().orderByDesc(AdvertisementDO::getSort).orderByDesc(AdvertisementDO::getCreateTime);
        wrapper.lambda().last("limit 6");
        return this.list(wrapper);
    }
}
