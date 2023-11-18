package com.yiling.mall.banner.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.dao.B2bAppBannerMapper;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.request.B2bAppBannerPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerSaveRequest;
import com.yiling.mall.banner.entity.B2bAppBannerDO;
import com.yiling.mall.banner.entity.B2bAppBannerEnterpriseLimitDO;
import com.yiling.mall.banner.enums.BannerUsageScenarioEnum;
import com.yiling.mall.banner.service.B2bAppBannerEnterpriseLimitService;
import com.yiling.mall.banner.service.B2bAppBannerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * B2B的banner表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class B2bAppBannerServiceImpl extends BaseServiceImpl<B2bAppBannerMapper, B2bAppBannerDO> implements B2bAppBannerService {

    private final B2bAppBannerEnterpriseLimitService bannerEnterpriseLimitService;

    @Override
    public boolean saveB2bAppBanner(B2bAppBannerSaveRequest request) {
        B2bAppBannerDO b2bAppBannerDO = PojoUtils.map(request, B2bAppBannerDO.class);
        if (null == request.getId()) {
            b2bAppBannerDO.setCreateUser(request.getOpUserId());
            b2bAppBannerDO.setCreateTime(new Date());
            this.save(b2bAppBannerDO);
        } else {
            b2bAppBannerDO.setUpdateUser(request.getOpUserId());
            b2bAppBannerDO.setUpdateTime(new Date());
            this.updateById(b2bAppBannerDO);
        }

        if (BannerUsageScenarioEnum.getByCode(request.getUsageScenario()) == BannerUsageScenarioEnum.ENTERPRISE) {
            return bannerEnterpriseLimitService.saveBannerEnterprise(b2bAppBannerDO.getId(), request.getBannerEnterpriseList(), request.getOpUserId(), request.getOpTime());
        } else {
            List<B2bAppBannerEnterpriseLimitDO> enterpriseList = bannerEnterpriseLimitService.listByBannerIdAndEid(b2bAppBannerDO.getId(), null);
            if (CollUtil.isNotEmpty(enterpriseList)) {
                return bannerEnterpriseLimitService.deleteByBannerId(b2bAppBannerDO.getId(), request.getOpUserId(), request.getOpTime());
            }
            return true;
        }
    }

    @Override
    public boolean editWeight(Long id, Integer sort, Long currentUserId) {
        B2bAppBannerDO b2bAppBannerDO = new B2bAppBannerDO();
        b2bAppBannerDO.setId(id);
        b2bAppBannerDO.setSort(sort);
        b2bAppBannerDO.setUpdateUser(currentUserId);
        b2bAppBannerDO.setUpdateTime(new Date());
        return this.updateById(b2bAppBannerDO);
    }

    @Override
    public boolean editStatus(Long id, Integer bannerStatus, Long currentUserId) {
        B2bAppBannerDO b2bAppBannerDO = new B2bAppBannerDO();
        b2bAppBannerDO.setId(id);
        b2bAppBannerDO.setBannerStatus(bannerStatus);
        b2bAppBannerDO.setUpdateUser(currentUserId);
        b2bAppBannerDO.setUpdateTime(new Date());
        return this.updateById(b2bAppBannerDO);
    }

    @Override
    public boolean deleteById(Long id, Long currentUserId) {
        Date opTime = new Date();
        B2bAppBannerDO b2bAppBannerDO = new B2bAppBannerDO();
        b2bAppBannerDO.setId(id);
        b2bAppBannerDO.setUpdateUser(currentUserId);
        b2bAppBannerDO.setUpdateTime(opTime);
        boolean isSuccess = this.deleteByIdWithFill(b2bAppBannerDO) > 0;
        if (isSuccess) {
            bannerEnterpriseLimitService.deleteByBannerId(b2bAppBannerDO.getId(), currentUserId, opTime);
        }
        return true;
    }

    @Override
    public Page<B2bAppBannerDTO> pageList(B2bAppBannerPageRequest request) {
        QueryWrapper<B2bAppBannerDO> wrapper = new QueryWrapper<>();
        if (null != request.getBannerSource() && 0 != request.getBannerSource()) {
            wrapper.lambda().eq(B2bAppBannerDO::getBannerSource, request.getBannerSource());
        }
        if (null != request.getUsageScenario() && 0 != request.getUsageScenario()) {
            wrapper.lambda().eq(B2bAppBannerDO::getUsageScenario, request.getUsageScenario());
        }
        if (StringUtils.isNotEmpty(request.getTitle())) {
            wrapper.lambda().like(B2bAppBannerDO::getTitle, request.getTitle());
        }
        if (null != request.getBannerStatus() && 0 != request.getBannerStatus()) {
            wrapper.lambda().eq(B2bAppBannerDO::getBannerStatus, request.getBannerStatus());
        }
        if (null != request.getCreateStartTime() && null != request.getCreateEndTime()) {
            wrapper.lambda().ge(B2bAppBannerDO::getCreateTime, DateUtil.beginOfDay(request.getCreateStartTime()));
            wrapper.lambda().lt(B2bAppBannerDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        // 投放开始时间
        if (null != request.getUseStartTime() && null != request.getUseEndTime()) {
            wrapper.lambda().ge(B2bAppBannerDO::getStartTime, DateUtil.beginOfDay(request.getUseStartTime()));
            wrapper.lambda().lt(B2bAppBannerDO::getStopTime, DateUtil.endOfDay(request.getUseEndTime()));
        }
        wrapper.lambda().orderByDesc(B2bAppBannerDO::getSort).orderByAsc(B2bAppBannerDO::getStopTime).orderByAsc(B2bAppBannerDO::getStartTime).orderByDesc(B2bAppBannerDO::getCreateTime);
        Page<B2bAppBannerDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, B2bAppBannerDTO.class);
    }

    @Override
    public List<B2bAppBannerDO> listByScenarioAndSource(Integer usageScenario, Integer bannerSource, int count) {
        QueryWrapper<B2bAppBannerDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(B2bAppBannerDO::getUsageScenario, usageScenario).eq(B2bAppBannerDO::getBannerSource, bannerSource).eq(B2bAppBannerDO::getBannerStatus, 1).le(B2bAppBannerDO::getStartTime, new Date()).ge(B2bAppBannerDO::getStopTime, new Date()).eq(B2bAppBannerDO::getDelFlag, 0).orderByDesc(B2bAppBannerDO::getSort).orderByAsc(B2bAppBannerDO::getStopTime).orderByAsc(B2bAppBannerDO::getStartTime).orderByDesc(B2bAppBannerDO::getCreateTime).last("limit " + count);
        return this.list(wrapper);
    }

    @Override
    public List<B2bAppBannerDTO> listByScenarioAndSourceAndEid(Integer usageScenario, Integer bannerSource, Long eid, int count) {
        return this.getBaseMapper().listByScenarioAndSourceAndEid(usageScenario, bannerSource, eid, count);
    }
}
