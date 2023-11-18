package com.yiling.bi.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yiling.bi.order.bo.OdsStjxcJddyhoutBO;
import com.yiling.bi.order.dao.OdsStjxcJddyhoutMapper;
import com.yiling.bi.order.entity.OdsStjxcJddyhoutDO;
import com.yiling.bi.order.service.OdsStjxcJddyhoutService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * @author fucheng.bai
 * @date 2022/12/26
 */
@Service
public class OdsStjxcJddyhoutServiceImpl extends BaseServiceImpl<OdsStjxcJddyhoutMapper, OdsStjxcJddyhoutDO> implements OdsStjxcJddyhoutService {


    @Override
    public List<OdsStjxcJddyhoutBO> getByYearAndMonth(String dyear, String dmonth) {
        return baseMapper.getByYearAndMonth(dyear, dmonth);
    }
}
