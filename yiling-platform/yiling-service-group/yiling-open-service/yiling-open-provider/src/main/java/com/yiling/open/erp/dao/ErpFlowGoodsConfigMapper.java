package com.yiling.open.erp.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpFlowGoodsConfigDO;

/**
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Repository
public interface ErpFlowGoodsConfigMapper extends BaseMapper<ErpFlowGoodsConfigDO> {

    Integer deleteFlowGoodsConfigById(@Param("id") Long id);

}
