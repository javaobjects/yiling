package com.yiling.hmc.qrcode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.qrcode.dto.QrcodeStatisticsDTO;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsPageRequest;
import com.yiling.hmc.qrcode.dto.request.QueryQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.dto.request.SaveQrcodeStatisticsRequest;
import com.yiling.hmc.qrcode.entity.QrcodeStatisticsDO;

/**
 * <p>
 * 二维码统计 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-04-02
 */
public interface QrcodeStatisticsService extends BaseService<QrcodeStatisticsDO> {

    /**
     * 保存
     * @param request
     */
    void save(SaveQrcodeStatisticsRequest request);

    /**
     *
     * 分页查询
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
