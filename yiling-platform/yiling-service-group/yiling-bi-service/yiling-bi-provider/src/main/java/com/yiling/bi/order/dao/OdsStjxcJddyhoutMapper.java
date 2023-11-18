package com.yiling.bi.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.bi.order.bo.OdsStjxcJddyhoutBO;
import com.yiling.bi.order.entity.OdsStjxcJddyhoutDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/12/26
 */
@Repository
public interface OdsStjxcJddyhoutMapper extends BaseMapper<OdsStjxcJddyhoutDO> {

    List<OdsStjxcJddyhoutBO> getByYearAndMonth(@Param("dyear") String dyear, @Param("dmonth") String dmonth);
}
