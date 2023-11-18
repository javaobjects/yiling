package com.yiling.goods.standard.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.dao.StandardInstructionsGoodsMapper;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.entity.StandardInstructionsGoodsDO;
import com.yiling.goods.standard.service.StandardInstructionsGoodsService;

/**
 * <p>
 * 标准库商品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsGoodsServiceImpl extends BaseServiceImpl<StandardInstructionsGoodsMapper, StandardInstructionsGoodsDO> implements StandardInstructionsGoodsService {

    /**
     * 根据StandardId找到药品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsGoodsDO getInstructionsGoodsByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsGoodsDO::getStandardId, standardId);
        return getOne(wrapper);
    }

    /**
     * 根据StandardIds找到药品说明书
     *
     * @param standardIds
     * @return
     */
    @Override
    public List<StandardInstructionsGoodsDTO> getInstructionsGoodsByStandardIdList(List<Long> standardIds) {
        QueryWrapper<StandardInstructionsGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardInstructionsGoodsDO::getStandardId, standardIds);
        return PojoUtils.map(list(wrapper),StandardInstructionsGoodsDTO.class);
    }

    /**
     * 保存普通药品说明书
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsGoodsOne(StandardInstructionsGoodsDO one) {
        return saveOrUpdate(one);
    }
}
