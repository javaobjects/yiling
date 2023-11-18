package com.yiling.goods.standard.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.dao.StandardInstructionsHealthMapper;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.entity.StandardInstructionsHealthDO;
import com.yiling.goods.standard.service.StandardInstructionsHealthService;

/**
 * <p>
 * 保健食品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsHealthServiceImpl extends BaseServiceImpl<StandardInstructionsHealthMapper, StandardInstructionsHealthDO> implements StandardInstructionsHealthService {

    /**
     * 根据StandardId找到保健食品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsHealthDO getInstructionsHealthByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsHealthDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsHealthDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    /**
     * 根据StandardIds找到保健食品说明书
     *
     * @param standardIds
     * @return
     */
    @Override
    public List<StandardInstructionsHealthDTO> getInstructionsHealthByStandardIdList(List<Long> standardIds) {
        QueryWrapper<StandardInstructionsHealthDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardInstructionsHealthDO :: getStandardId,standardIds);
        return PojoUtils.map(list(wrapper),StandardInstructionsHealthDTO.class);
    }

    /**
     * 保存保健食品说明书信息
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsHealthOne(StandardInstructionsHealthDO one) {
        return this.saveOrUpdate(one);
    }
}
