package com.yiling.sjms.flee.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.sjms.flee.dao.SalesAppealExtFormMapper;
import com.yiling.sjms.flee.service.SalesAppealExtFormService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 销量申诉拓展表单 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Service
public class SalesAppealExtFormServiceImpl extends BaseServiceImpl<SalesAppealExtFormMapper, SalesAppealExtFormDO> implements SalesAppealExtFormService {

    @Override
    public SalesAppealExtFormDTO getByFormId(Long formId) {
        QueryWrapper<SalesAppealExtFormDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(SalesAppealExtFormDO::getFormId,formId);
        return PojoUtils.map(this.getOne(objectQueryWrapper),SalesAppealExtFormDTO.class);
    }

    @Override
    public List<Long> getSubmitUnCleaned() {
        return baseMapper.getSubmitUnCleaned();
    }
}
