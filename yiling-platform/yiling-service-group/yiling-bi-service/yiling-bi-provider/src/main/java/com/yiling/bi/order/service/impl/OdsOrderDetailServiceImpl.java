package com.yiling.bi.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.order.dao.OdsOrderDetailMapper;
import com.yiling.bi.order.entity.OdsOrderDetailDO;
import com.yiling.bi.order.service.OdsOrderDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Service
public class OdsOrderDetailServiceImpl extends BaseServiceImpl<OdsOrderDetailMapper, OdsOrderDetailDO> implements OdsOrderDetailService {

    @Override
    public Integer getCountByOrderMonth(String monthStr) {
        return baseMapper.getCountByOrderMonth(monthStr);
    }

    @Override
    public List<OdsOrderDetailDO> getByOrderMonth(String monthStr, Page<OdsOrderDetailDO> page) {
        return baseMapper.getByOrderMonth(monthStr, page);
    }

    @Override
    public Integer getCountByGeExtractTime(String extractTime) {
        return baseMapper.getCountByGeExtractTime(extractTime);
    }

    @Override
    public List<OdsOrderDetailDO> getGeExtractTime(String extractTime, Page<OdsOrderDetailDO> page) {
        return baseMapper.getGeExtractTime(extractTime, page);
    }

    @Override
    public String getMinExtractTime() {
        return baseMapper.getMinExtractTime();
    }
}
