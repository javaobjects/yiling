package com.yiling.mall.banner.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.B2bAppBannerEnterpriseLimitDTO;
import com.yiling.mall.banner.dto.BannerDTO;
import com.yiling.mall.banner.dto.request.B2bAppBannerDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerWeightRequest;
import com.yiling.mall.banner.dto.request.CheckBannerRequest;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.dto.request.SaveBannerRequest;
import com.yiling.mall.banner.dto.request.UpdateBannerRequest;
import com.yiling.mall.banner.entity.B2bAppBannerDO;
import com.yiling.mall.banner.entity.B2bAppBannerEnterpriseLimitDO;
import com.yiling.mall.banner.entity.BannerDO;
import com.yiling.mall.banner.enums.BannerStatusEnum;
import com.yiling.mall.banner.service.B2bAppBannerEnterpriseLimitService;
import com.yiling.mall.banner.service.B2bAppBannerService;
import com.yiling.mall.banner.service.BannerService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/15 0015
 */
@DubboService
public class BannerApiImpl implements BannerApi {

    @Autowired
    BannerService bannerService;

    @Autowired
    B2bAppBannerService b2bAppBannerService;

    @Autowired
    B2bAppBannerEnterpriseLimitService b2bAppBannerEnterpriseLimitService;

    @Override
    public Page<BannerDTO> pageList(QueryBannerPageListRequest request) {
        request.setCreateTimeEnd(request.getCreateTimeEnd() != null ? DateUtil.endOfDay(request.getCreateTimeEnd()) : null);
        Page<BannerDO> page = bannerService.pageList(request);

        List<BannerDTO> records = new ArrayList<>();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(e -> {
                BannerDTO dto = PojoUtils.map(e, BannerDTO.class);
                // 处理banner状态
                dto.setBannerStatus(this.judgeBannerStatus(dto.getStartTime(), dto.getEndTime()));
                records.add(dto);
            });
        }

        Page<BannerDTO> pageResult = PojoUtils.map(page, BannerDTO.class);
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public BannerDTO get(Long bannerId) {
        BannerDO bannerDO = bannerService.get(bannerId);
        BannerDTO dto = PojoUtils.map(bannerDO, BannerDTO.class);
        dto.setBannerStatus(this.judgeBannerStatus(dto.getStartTime(), dto.getEndTime()));
        return dto;
    }

    @Override
    public Boolean createBanner(SaveBannerRequest request) {
        return bannerService.addBanner(request);
    }

    @Override
    public Boolean updateBanner(UpdateBannerRequest request) {
        return bannerService.updateBanner(request);
    }

    @Override
    public List<BannerDTO> getAvailableBannerList(Integer num) {
        return PojoUtils.map(bannerService.queryAvailableBannerList(num), BannerDTO.class);
    }

    @Override
    public Boolean checkRightful(CheckBannerRequest request) {
        return bannerService.checkRightful(request);
    }

    /**
     * 修改状态
     *
     * @param id bannerId
     * @param status 启用停用状态
     * @param opUserId 操作人
     * @return
     */
    @Override
    public Boolean updateStatusById(Long id, Integer status, Long opUserId) {
        return bannerService.updateStatusById(id, status, opUserId);
    }

    @Override
    public B2bAppBannerDTO queryByB2BId(Long id) {
        B2bAppBannerDO b2bAppBannerDO = b2bAppBannerService.getById(id);
        return PojoUtils.map(b2bAppBannerDO, B2bAppBannerDTO.class);
    }

    @Override
    public boolean saveB2bAppBanner(B2bAppBannerSaveRequest request) {
        return b2bAppBannerService.saveB2bAppBanner(request);
    }

    @Override
    public boolean editB2bAppBannerWeight(B2bAppBannerWeightRequest request) {
        return b2bAppBannerService.editWeight(request.getId(), request.getSort(), request.getOpUserId());
    }

    @Override
    public boolean editB2bAppBannerStatus(B2bAppBannerStatusRequest request) {
        return b2bAppBannerService.editStatus(request.getId(), request.getBannerStatus(), request.getOpUserId());
    }

    @Override
    public boolean deleteB2bAppBanner(B2bAppBannerDeleteRequest request) {
        return b2bAppBannerService.deleteById(request.getId(), request.getOpUserId());
    }

    @Override
    public Page<B2bAppBannerDTO> pageB2bList(B2bAppBannerPageRequest request) {
        return b2bAppBannerService.pageList(request);
    }

    @Override
    public List<B2bAppBannerDTO> listByScenarioAndSource(Integer usageScenario, Integer bannerSource, int count) {
        List<B2bAppBannerDO> b2bAppBannerDOList = b2bAppBannerService.listByScenarioAndSource(usageScenario, bannerSource, count);
        return PojoUtils.map(b2bAppBannerDOList, B2bAppBannerDTO.class);
    }

    @Override
    public List<B2bAppBannerEnterpriseLimitDTO> listBannerEnterpriseByBannerIdAndEid(Long bannerId, Long eid) {
        List<B2bAppBannerEnterpriseLimitDO> doList = b2bAppBannerEnterpriseLimitService.listByBannerIdAndEid(bannerId, eid);
        return PojoUtils.map(doList, B2bAppBannerEnterpriseLimitDTO.class);
    }

    @Override
    public List<B2bAppBannerDTO> listByScenarioAndSourceAndEid(Integer usageScenario, Integer bannerSource, Long eid, int count) {
        return b2bAppBannerService.listByScenarioAndSourceAndEid(usageScenario, bannerSource, eid, count);
    }

    /**
     * 根据当前时间判断banner是否开始
     *
     * @param start banner开始时间
     * @param end banner结束时间
     * @return
     */
    private Integer judgeBannerStatus(Date start, Date end) {
        Date now = new Date();
        if (now.before(start)) {
            return BannerStatusEnum.NO_START.getCode();
        } else if (now.before(end)) {
            return BannerStatusEnum.START.getCode();
        } else if (now.after(end)) {
            return BannerStatusEnum.END.getCode();
        }
        return null;
    }
}
