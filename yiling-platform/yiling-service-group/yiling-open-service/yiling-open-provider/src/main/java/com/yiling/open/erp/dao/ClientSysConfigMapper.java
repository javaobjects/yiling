package com.yiling.open.erp.dao;


import org.apache.ibatis.annotations.Param;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ClientSysConfigDO;

/**
 * @author shuan
 */
public interface ClientSysConfigMapper extends BaseMapper<ClientSysConfigDO> {

    int save(ClientSysConfigDO record);

    int deleteBySuId(@Param("suId") Long suId);

    ClientSysConfigDO getBySuid(@Param("suId") Long suId);
}