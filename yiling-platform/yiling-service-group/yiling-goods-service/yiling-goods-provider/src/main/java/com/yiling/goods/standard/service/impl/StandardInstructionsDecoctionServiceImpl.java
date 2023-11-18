package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsDecoctionMapper;
import com.yiling.goods.standard.entity.StandardInstructionsDecoctionDO;
import com.yiling.goods.standard.service.StandardInstructionsDecoctionService;

/**
 * <p>
 * 中药饮片商品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsDecoctionServiceImpl extends BaseServiceImpl<StandardInstructionsDecoctionMapper, StandardInstructionsDecoctionDO> implements StandardInstructionsDecoctionService {

    /**
     * 根据StandardId找到中药饮片商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsDecoctionDO getInstructionsDecoctionByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsDecoctionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsDecoctionDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    /**
     * 保存中药饮片商品说明书
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsDecoctionOne(StandardInstructionsDecoctionDO one) {

        return this.saveOrUpdate(one);
    }
}
