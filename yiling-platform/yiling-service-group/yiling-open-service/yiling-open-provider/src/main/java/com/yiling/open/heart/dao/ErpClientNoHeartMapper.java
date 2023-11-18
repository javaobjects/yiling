package com.yiling.open.heart.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.heart.dto.request.QueryErpClientNoHeartRequest;
import com.yiling.open.heart.entity.ErpClientNoHeartDO;

/**
 * @author: houjie.sun
 * @date: 2022/10/27
 */
@Repository
public interface ErpClientNoHeartMapper extends BaseMapper<ErpClientNoHeartDO> {

    List<ErpClientNoHeartDO> getByRkSuIdListAndTaskTime(@Param("request") QueryErpClientNoHeartRequest request);

    Integer deleteByTaskTime(@Param("taskTime") Date taskTime);

}
