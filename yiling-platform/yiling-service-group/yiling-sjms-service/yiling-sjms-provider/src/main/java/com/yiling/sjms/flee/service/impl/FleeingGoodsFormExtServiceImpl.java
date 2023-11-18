package com.yiling.sjms.flee.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.flee.dao.FleeingGoodsFormExtMapper;
import com.yiling.sjms.flee.entity.FleeingGoodsFormExtDO;
import com.yiling.sjms.flee.service.FleeingGoodsFormExtService;

/**
 * <p>
 * 窜货申诉拓展表单 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-14
 */
@Service
public class FleeingGoodsFormExtServiceImpl extends BaseServiceImpl<FleeingGoodsFormExtMapper, FleeingGoodsFormExtDO> implements FleeingGoodsFormExtService {

    @Override
    public FleeingGoodsFormExtDO queryExtByFormId(Long formId) {
        LambdaQueryWrapper<FleeingGoodsFormExtDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FleeingGoodsFormExtDO::getFormId, formId);
        wrapper.last(" limit 1");
        return this.getOne(wrapper);
    }
}
