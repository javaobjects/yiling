package com.yiling.open.erp.dao;

import org.apache.ibatis.annotations.Param;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ClientPushConfigDO;

/**
 * @author shuan
 */
public interface ClientPushConfigMapper extends BaseMapper<ClientPushConfigDO> {

    int save(ClientPushConfigDO record);

    int deleteBySuId(@Param("suId") Long suId);

    ClientPushConfigDO getBySuid(@Param("suId") Long suId);

}