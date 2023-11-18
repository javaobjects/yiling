package com.yiling.bi.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.bi.order.bo.DwsStjxInventoryBO;
import com.yiling.bi.order.entity.DwsStjxcDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/9/27
 */
@Repository
public interface DwsStjxcMapper extends BaseMapper<DwsStjxcDO> {

    List<DwsStjxInventoryBO> getDwsStjxInventoryList(@Param("dyear") String dyear, @Param("dmonth")String dmonth);
}
