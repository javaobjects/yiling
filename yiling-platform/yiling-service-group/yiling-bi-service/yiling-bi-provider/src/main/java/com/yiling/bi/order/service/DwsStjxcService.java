package com.yiling.bi.order.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yiling.bi.order.bo.DwsStjxInventoryBO;
import com.yiling.bi.order.entity.DwsStjxcDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/9/27
 */
public interface DwsStjxcService extends BaseService<DwsStjxcDO> {

    List<DwsStjxInventoryBO> getDwsStjxInventoryList(String dyear, String dmonth);
}
