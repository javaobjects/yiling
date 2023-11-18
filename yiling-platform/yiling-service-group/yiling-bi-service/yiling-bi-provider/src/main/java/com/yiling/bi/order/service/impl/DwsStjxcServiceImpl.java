package com.yiling.bi.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yiling.bi.order.bo.DwsStjxInventoryBO;
import com.yiling.bi.order.dao.DwsStjxcMapper;
import com.yiling.bi.order.entity.DwsStjxcDO;
import com.yiling.bi.order.service.DwsStjxcService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * @author fucheng.bai
 * @date 2022/9/27
 */
@Service
public class DwsStjxcServiceImpl extends BaseServiceImpl<DwsStjxcMapper, DwsStjxcDO> implements DwsStjxcService {


    @Override
    public List<DwsStjxInventoryBO> getDwsStjxInventoryList(String dyear, String dmonth) {
        return baseMapper.getDwsStjxInventoryList(dyear, dmonth);
    }
}
