package com.yiling.bi.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.order.entity.OdsOrderDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
public interface OdsOrderDetailService extends BaseService<OdsOrderDetailDO> {

    Integer getCountByOrderMonth(String monthStr);

    List<OdsOrderDetailDO> getByOrderMonth(String monthStr, Page<OdsOrderDetailDO> page);

    Integer getCountByGeExtractTime(String extractTime);

    List<OdsOrderDetailDO> getGeExtractTime(String extractTime, Page<OdsOrderDetailDO> page);

    String getMinExtractTime();
}