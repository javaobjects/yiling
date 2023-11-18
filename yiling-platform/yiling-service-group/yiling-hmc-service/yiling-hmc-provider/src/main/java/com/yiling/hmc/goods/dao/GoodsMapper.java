package com.yiling.hmc.goods.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.entity.HmcGoodsDO;

/**
 * <p>
 * C端保险药品商家提成设置表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Repository
public interface GoodsMapper extends BaseMapper<HmcGoodsDO> {

    /**
     * 统计企业商品数量
     * @param eidList
     * @return
     */
    List<EnterpriseGoodsCountBO> countGoodsByEids(@Param("eidList") List<Long> eidList);


}
