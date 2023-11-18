package com.yiling.sjms.crm.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.crm.dto.request.DeleteHosDruRelFormAppendixRequest;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationExtFormDO;
import com.yiling.sjms.crm.dao.HospitalDrugstoreRelationExtFormMapper;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationFormDO;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationExtFormService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 院外药店关系流程表单扩展信息表 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-06-07
 */
@Service
public class HospitalDrugstoreRelationExtFormServiceImpl extends BaseServiceImpl<HospitalDrugstoreRelationExtFormMapper, HospitalDrugstoreRelationExtFormDO> implements HospitalDrugstoreRelationExtFormService {

    @Override
    public void removeById(DeleteHosDruRelFormAppendixRequest request) {
        HospitalDrugstoreRelationExtFormDO hospitalDrugstoreRelationExtFormDO = PojoUtils.map(request, HospitalDrugstoreRelationExtFormDO.class);
        this.deleteByIdWithFill(hospitalDrugstoreRelationExtFormDO);
    }

    @Override
    public HospitalDrugstoreRelationExtFormDO detailByFormId(Long formId) {
        LambdaQueryWrapper<HospitalDrugstoreRelationExtFormDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HospitalDrugstoreRelationExtFormDO::getFormId, formId);
        wrapper.orderByDesc(HospitalDrugstoreRelationExtFormDO::getId);
        List<HospitalDrugstoreRelationExtFormDO> list = this.list(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

}
