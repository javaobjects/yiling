package com.yiling.sjms.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flow.dao.MonthFlowExtFormMapper;
import com.yiling.sjms.flow.dto.MonthFlowExtFormDTO;
import com.yiling.sjms.flow.entity.MonthFlowExtFormDO;
import com.yiling.sjms.flow.service.MonthFlowExtFormService;

/**
 * <p>
 * 补传月流向拓展表单 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-06-25
 */
@Service
public class MonthFlowExtFormServiceImpl extends BaseServiceImpl<MonthFlowExtFormMapper, MonthFlowExtFormDO> implements MonthFlowExtFormService {
    @Override
    public MonthFlowExtFormDO getByFormId(Long formId) {
        QueryWrapper<MonthFlowExtFormDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(MonthFlowExtFormDO::getFormId,formId).last("limit 1");
        return this.getOne(objectQueryWrapper);
    }

    @Override
    public MonthFlowExtFormDTO queryAppendix(Long formId) {
        LambdaQueryWrapper<MonthFlowExtFormDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonthFlowExtFormDO::getFormId, formId).last("limit 1");
        MonthFlowExtFormDO monthFlowExtFormDO = this.getOne(queryWrapper);
        return PojoUtils.map(monthFlowExtFormDO, MonthFlowExtFormDTO.class);
    }
}
