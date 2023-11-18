package com.yiling.bi.order.service;

import java.util.List;

import com.yiling.bi.order.bo.OdsStjxcJddyhoutBO;
import com.yiling.bi.order.entity.OdsStjxcJddyhoutDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/12/26
 */
public interface OdsStjxcJddyhoutService extends BaseService<OdsStjxcJddyhoutDO> {

    List<OdsStjxcJddyhoutBO> getByYearAndMonth(String dyear, String dmonth);
}
