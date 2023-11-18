package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsMaterialsMapper;
import com.yiling.goods.standard.entity.StandardInstructionsMaterialsDO;
import com.yiling.goods.standard.service.StandardInstructionsMaterialsService;

/**
 * <p>
 * 中药材商品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsMaterialsServiceImpl extends BaseServiceImpl<StandardInstructionsMaterialsMapper, StandardInstructionsMaterialsDO> implements StandardInstructionsMaterialsService {


    /**
     * 根据StandardId找到中药材商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsMaterialsDO getInstructionsMaterialsByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsMaterialsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsMaterialsDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    /**
     * 中药材商品说明书
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsMaterialsOne(StandardInstructionsMaterialsDO one) {
        return saveOrUpdate(one);
    }
}
