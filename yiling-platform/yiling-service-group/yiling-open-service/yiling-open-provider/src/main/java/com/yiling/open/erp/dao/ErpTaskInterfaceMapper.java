package com.yiling.open.erp.dao;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpTaskInterfaceDO;

public interface ErpTaskInterfaceMapper extends BaseMapper<ErpTaskInterfaceDO> {

    int deleteByPrimaryKey(Integer id);

    int insert(ErpTaskInterfaceDO record);

    int insertSelective(ErpTaskInterfaceDO record);

    ErpTaskInterfaceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ErpTaskInterfaceDO record);

    int updateByPrimaryKey(ErpTaskInterfaceDO record);

    ErpTaskInterfaceDO findErpTaskInterfaceByTaskNo(String taskNo);


}