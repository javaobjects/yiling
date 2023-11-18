package com.yiling.sjms.gb.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.sjms.gb.dao.GbCompanyRelationMapper;
import com.yiling.sjms.gb.entity.GbCompanyRelationDO;
import com.yiling.sjms.gb.service.GbCompanyRelationService;


/**
 * <p>
 * 团购出库终端和商业关系
 * </p>
 *
 * @author wei.wang
 * @date 2023-03-07
 */

@Service
public class GbCompanyRelationServiceImpl extends BaseServiceImpl<GbCompanyRelationMapper, GbCompanyRelationDO> implements GbCompanyRelationService {

    @Override
    public List<GbCompanyRelationDO> listByFormId(Long formId) {
        QueryWrapper<GbCompanyRelationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbCompanyRelationDO :: getFormId,formId);
        return list(wrapper);
    }

    @Override
    public Boolean deleteByFormId(Long formId) {
        QueryWrapper<GbCompanyRelationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GbCompanyRelationDO :: getFormId,formId);

        return batchDeleteWithFill(new GbCompanyRelationDO(),wrapper) >0;
    }

    @Override
    public List<GbCompanyRelationDO> listByFormIds(List<Long> formIds) {
        QueryWrapper<GbCompanyRelationDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GbCompanyRelationDO :: getFormId,formIds);
        return list(wrapper);
    }
}
