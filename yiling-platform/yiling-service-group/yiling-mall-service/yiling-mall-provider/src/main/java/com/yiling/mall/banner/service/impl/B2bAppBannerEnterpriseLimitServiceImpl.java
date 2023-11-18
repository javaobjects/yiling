package com.yiling.mall.banner.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.dao.B2bAppBannerEnterpriseLimitMapper;
import com.yiling.mall.banner.dto.request.B2bAppBannerEnterpriseSaveRequest;
import com.yiling.mall.banner.entity.B2bAppBannerEnterpriseLimitDO;
import com.yiling.mall.banner.service.B2bAppBannerEnterpriseLimitService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * B2B的店铺banner企业表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-11-10
 */
@Service
public class B2bAppBannerEnterpriseLimitServiceImpl extends BaseServiceImpl<B2bAppBannerEnterpriseLimitMapper, B2bAppBannerEnterpriseLimitDO> implements B2bAppBannerEnterpriseLimitService {

    @Override
    public boolean saveBannerEnterprise(Long bannerId, List<B2bAppBannerEnterpriseSaveRequest> bannerEnterpriseList, Long opUserId, Date opTime) {
        List<B2bAppBannerEnterpriseLimitDO> oldEnterpriseList = this.listByBannerIdAndEid(bannerId, null);
        List<Long> oldEidList = oldEnterpriseList.stream().map(B2bAppBannerEnterpriseLimitDO::getEid).collect(Collectors.toList());
        List<Long> newEidList = bannerEnterpriseList.stream().map(B2bAppBannerEnterpriseSaveRequest::getEid).collect(Collectors.toList());

        List<B2bAppBannerEnterpriseSaveRequest> addEnterpriseList = bannerEnterpriseList.stream().filter(e -> !oldEidList.contains(e.getEid())).collect(Collectors.toList());
        List<B2bAppBannerEnterpriseLimitDO> deleteEnterpriseList = oldEnterpriseList.stream().filter(e -> !newEidList.contains(e.getEid())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(addEnterpriseList)) {
            List<B2bAppBannerEnterpriseLimitDO> bannerEnterpriseLimitDOList = PojoUtils.map(addEnterpriseList, B2bAppBannerEnterpriseLimitDO.class);
            bannerEnterpriseLimitDOList.forEach(e -> {
                e.setBannerId(bannerId);
                e.setOpUserId(opUserId);
                e.setOpTime(opTime);
            });
            this.saveBatch(bannerEnterpriseLimitDOList);
        }

        if (CollUtil.isNotEmpty(deleteEnterpriseList)) {
            List<Long> deleteIdList = deleteEnterpriseList.stream().map(B2bAppBannerEnterpriseLimitDO::getId).collect(Collectors.toList());
            B2bAppBannerEnterpriseLimitDO b2bAppBannerEnterpriseLimitDO = new B2bAppBannerEnterpriseLimitDO();
            b2bAppBannerEnterpriseLimitDO.setDelFlag(1);
            b2bAppBannerEnterpriseLimitDO.setOpUserId(opUserId);
            b2bAppBannerEnterpriseLimitDO.setOpTime(opTime);
            QueryWrapper<B2bAppBannerEnterpriseLimitDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(B2bAppBannerEnterpriseLimitDO::getId, deleteIdList);
            this.batchDeleteWithFill(b2bAppBannerEnterpriseLimitDO, wrapper);
        }

        return true;
    }

    @Override
    public List<B2bAppBannerEnterpriseLimitDO> listByBannerIdAndEid(Long bannerId, Long eid) {
        QueryWrapper<B2bAppBannerEnterpriseLimitDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(bannerId)) {
            wrapper.lambda().eq(B2bAppBannerEnterpriseLimitDO::getBannerId, bannerId);
        }
        if (Objects.nonNull(eid)) {
            wrapper.lambda().eq(B2bAppBannerEnterpriseLimitDO::getEid, eid);
        }
        return this.list(wrapper);
    }

    @Override
    public boolean deleteByBannerId(Long bannerId, Long opUserId, Date opTime) {
        B2bAppBannerEnterpriseLimitDO b2bAppBannerEnterpriseLimitDO = new B2bAppBannerEnterpriseLimitDO();
        b2bAppBannerEnterpriseLimitDO.setDelFlag(1);
        b2bAppBannerEnterpriseLimitDO.setOpUserId(opUserId);
        b2bAppBannerEnterpriseLimitDO.setOpTime(opTime);
        QueryWrapper<B2bAppBannerEnterpriseLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(B2bAppBannerEnterpriseLimitDO::getBannerId, bannerId);
        return this.batchDeleteWithFill(b2bAppBannerEnterpriseLimitDO, wrapper) > 0;
    }
}
