package com.yiling.bi.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.order.entity.OdsOrderDetailDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
@Repository
public interface OdsOrderDetailMapper extends BaseMapper<OdsOrderDetailDO> {

    List<OdsOrderDetailDO> getByOrderMonth(@Param("monthStr") String monthStr, Page<OdsOrderDetailDO> page);

    Integer getCountByOrderMonth(@Param("monthStr") String monthStr);

    Integer getCountByGeExtractTime(@Param("extractTime") String extractTime);

    List<OdsOrderDetailDO> getGeExtractTime(@Param("extractTime") String extractTime, Page<OdsOrderDetailDO> page);

    String getMinExtractTime();
}
