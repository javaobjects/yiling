package com.yiling.sales.assistant.banner.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.banner.dao.BannerMapper;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerAppListRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerDeleteRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerPageRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerSaveRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerStatusRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerWeightRequest;
import com.yiling.sales.assistant.banner.entity.BannerDO;
import com.yiling.sales.assistant.banner.enums.BannerStatusEnum;
import com.yiling.sales.assistant.banner.service.BannerService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 销售助手banner表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-14
 */
@Service
public class BannerServiceImpl extends BaseServiceImpl<BannerMapper, BannerDO> implements BannerService {

    @Override
    public boolean saveSaAppBanner(SaAppBannerSaveRequest request) {
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        if (null == request.getId()) {
            return this.save(bannerDO);
        } else {
            return this.updateById(bannerDO);
        }
    }

    @Override
    public boolean editWeight(SaAppBannerWeightRequest request) {
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        bannerDO.setId(request.getId());
        bannerDO.setSort(request.getSort());
        return this.updateById(bannerDO);
    }

    @Override
    public boolean editStatus(SaAppBannerStatusRequest request) {
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        bannerDO.setId(request.getId());
        bannerDO.setBannerStatus(request.getBannerStatus());
        return this.updateById(bannerDO);
    }

    @Override
    public boolean deleteById(SaAppBannerDeleteRequest request) {
        BannerDO bannerDO = PojoUtils.map(request, BannerDO.class);
        bannerDO.setId(request.getId());
        return this.deleteByIdWithFill(bannerDO) > 0;
    }

    @Override
    public Page<BannerDO> pageList(SaAppBannerPageRequest request) {
        QueryWrapper<BannerDO> wrapper = new QueryWrapper<>();
        if (null != request.getUsageScenario() && 0 != request.getUsageScenario()) {
            wrapper.lambda().eq(BannerDO::getUsageScenario, request.getUsageScenario());
        }
        if (null != request.getBannerCondition() && 0 != request.getBannerCondition()) {
            wrapper.lambda().eq(BannerDO::getBannerCondition, request.getBannerCondition());
        }
        if (StringUtils.isNotEmpty(request.getTitle())) {
            wrapper.lambda().like(BannerDO::getTitle, request.getTitle());
        }
        if (null != request.getBannerStatus() && 0 != request.getBannerStatus()) {
            wrapper.lambda().eq(BannerDO::getBannerStatus, request.getBannerStatus());
        }
        if (null != request.getCreateStartTime() && null != request.getCreateEndTime()) {
            wrapper.lambda().ge(BannerDO::getCreateTime, DateUtil.beginOfDay(request.getCreateStartTime()));
            wrapper.lambda().lt(BannerDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        // 投放开始时间
        if (null != request.getUseStartTime() && null != request.getUseEndTime()) {
            wrapper.lambda().ge(BannerDO::getStartTime, DateUtil.beginOfDay(request.getUseStartTime()));
            wrapper.lambda().lt(BannerDO::getStopTime, DateUtil.endOfDay(request.getUseEndTime()));
        }
        wrapper.lambda().orderByDesc(BannerDO::getSort).orderByAsc(BannerDO::getStopTime).orderByAsc(BannerDO::getStartTime).orderByDesc(BannerDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public List<BannerDO> listByScenarioAndSource(SaAppBannerAppListRequest request) {
        QueryWrapper<BannerDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BannerDO::getUsageScenario, request.getUsageScenario()).eq(BannerDO::getBannerStatus, BannerStatusEnum.ENABLE.getCode()).eq(BannerDO::getBannerCondition, null != request.getInternalEmployeeFlag() && request.getInternalEmployeeFlag() ? 1 : 2).le(BannerDO::getStartTime, new Date()).ge(BannerDO::getStopTime, new Date()).eq(BannerDO::getDelFlag, 0).orderByDesc(BannerDO::getSort).orderByAsc(BannerDO::getStopTime).orderByAsc(BannerDO::getStartTime).orderByDesc(BannerDO::getCreateTime).last("limit " + request.getCount());
        return this.list(wrapper);
    }

}
