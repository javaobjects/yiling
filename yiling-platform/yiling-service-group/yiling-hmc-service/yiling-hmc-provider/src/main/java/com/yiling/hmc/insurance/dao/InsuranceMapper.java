package com.yiling.hmc.insurance.dao;

import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.insurance.entity.InsuranceDO;

import java.util.List;

/**
 * <p>
 * C端保险保险表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Repository
public interface InsuranceMapper extends BaseMapper<InsuranceDO> {

    /**
     * 查询保险关联商品
     * @return
     */
    List<InsuranceGoodsDTO> queryGoods();
}
