package com.yiling.hmc.qrcode.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.qrcode.api.QrcodeStatisticsApi;
import com.yiling.hmc.qrcode.dto.QrcodeStatisticsDTO;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsPageRequest;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.service.QrcodeStatisticsService;

/**
 * 二维码统计
 * @author: gxl
 * @date: 2022/4/2
 */
@DubboService
public class QrcodeStatisticsApiImpl implements QrcodeStatisticsApi {

    @Autowired
    private QrcodeStatisticsService qrcodeStatisticsService;

    @Override
    public void save(SaveQrcodeStatisticsRequest request) {
        qrcodeStatisticsService.save(request);
    }

    @Override
    public Page<QrcodeStatisticsDTO> queryPage(QueryQrcodeStatisticsPageRequest request) {
        return qrcodeStatisticsService.queryPage(request);
    }

    @Override
    public QrcodeStatisticsDTO getTotal(QueryQrcodeStatisticsRequest request) {
        return qrcodeStatisticsService.getTotal(request);
    }
}