package com.yiling.hmc.qrcode.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.qrcode.dao.QrcodeStatisticsMapper;
import com.yiling.hmc.qrcode.dto.QrcodeStatisticsDTO;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsPageRequest;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.entity.QrcodeStatisticsDO;
import com.yiling.hmc.qrcode.service.QrcodeStatisticsService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 二维码统计 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-04-02
 */
@Service
public class QrcodeStatisticsServiceImpl extends BaseServiceImpl<QrcodeStatisticsMapper, QrcodeStatisticsDO> implements QrcodeStatisticsService {

    @Override
    public void save(SaveQrcodeStatisticsRequest request) {
        LambdaQueryWrapper<QrcodeStatisticsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.between(QrcodeStatisticsDO::getCreateTime, DateUtil.beginOfDay(new Date()), DateUtil.endOfDay(new Date())).last("limit 1");
        QrcodeStatisticsDO statisticsDO = this.getOne(wrapper);
        if (Objects.nonNull(statisticsDO)) {
            if (Objects.nonNull(request.getFollow())) {
                statisticsDO.setFollow(statisticsDO.getFollow() + request.getFollow());
            }
            if (Objects.nonNull(request.getPageView())) {
                statisticsDO.setPageView(statisticsDO.getPageView() + request.getPageView());
            }
            if (Objects.nonNull(request.getRegister())) {
                statisticsDO.setRegister(statisticsDO.getRegister() + request.getRegister());
            }
            if (Objects.nonNull(request.getAdClick())) {
                statisticsDO.setAdClick(statisticsDO.getAdClick() + request.getAdClick());
            }
        } else {
            statisticsDO = new QrcodeStatisticsDO();
            PojoUtils.map(request, statisticsDO);
        }
        this.saveOrUpdate(statisticsDO);
    }

    @Override
    public Page<QrcodeStatisticsDTO> queryPage(QueryQrcodeStatisticsPageRequest request) {
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        LambdaQueryWrapper<QrcodeStatisticsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.between(QrcodeStatisticsDO::getCreateTime, request.getStartTime(), request.getEndTime()).orderByDesc(QrcodeStatisticsDO::getId);
        Page<QrcodeStatisticsDO> page = this.page(request.getPage(), wrapper);
        Page<QrcodeStatisticsDTO> statisticsDTOPage = PojoUtils.map(page, QrcodeStatisticsDTO.class);
        return statisticsDTOPage;
    }

    @Override
    public QrcodeStatisticsDTO getTotal(QueryQrcodeStatisticsRequest request) {
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        LambdaQueryWrapper<QrcodeStatisticsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.between(QrcodeStatisticsDO::getCreateTime, request.getStartTime(), request.getEndTime());
        List<QrcodeStatisticsDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return new QrcodeStatisticsDTO();
        }
        QrcodeStatisticsDTO qrcodeStatisticsDTO = new QrcodeStatisticsDTO();
        qrcodeStatisticsDTO.setFollow(list.stream().mapToInt(QrcodeStatisticsDO::getFollow).sum());
        qrcodeStatisticsDTO.setPageView(list.stream().mapToInt(QrcodeStatisticsDO::getPageView).sum());
        qrcodeStatisticsDTO.setRegister(list.stream().mapToInt(QrcodeStatisticsDO::getRegister).sum());
        return qrcodeStatisticsDTO;
    }
}
