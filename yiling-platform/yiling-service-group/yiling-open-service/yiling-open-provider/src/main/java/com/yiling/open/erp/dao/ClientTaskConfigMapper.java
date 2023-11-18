package com.yiling.open.erp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ClientTaskConfigDO;

/**
 * @author shuan
 */
public interface ClientTaskConfigMapper extends BaseMapper<ClientTaskConfigDO> {

    int save(ClientTaskConfigDO record);

    int deteleBySuIdAndTaskNo(@Param("suId") Long suId, @Param("taskNo") String taskNo);

    List<ClientTaskConfigDO> getListBySuid(@Param("suId") Long suId);

    ClientTaskConfigDO getByTaskNoAndSuId(@Param("suId") Long suId,@Param("taskNo") String taskNo);
}