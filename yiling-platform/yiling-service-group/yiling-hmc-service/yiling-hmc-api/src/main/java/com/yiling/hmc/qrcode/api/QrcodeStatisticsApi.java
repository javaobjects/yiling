package com.yiling.hmc.qrcode.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.qrcode.dto.QrcodeStatisticsDTO;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsPageRequest;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;

/**
 * @author: gxl
 * @date: 2022/4/2
 */
public interface QrcodeStatisticsApi {

    /**
     * 保存
     * @param request
     */
    void save(SaveQrcodeStatisticsRequest request);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<QrcodeStatisticsDTO> queryPage(QueryQrcodeStatisticsPageRequest request);

    /**
     * 总计
     * @param request
     * @return
     */
    QrcodeStatisticsDTO getTotal(QueryQrcodeStatisticsRequest request);
}