package com.yiling.dataflow.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmMedicalEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmBusinessEnterpriseNumberGoodsRelationDO;
import com.yiling.dataflow.crm.entity.CrmMedicalEnterpriseNumberGoodsRelationDO;
import com.yiling.dataflow.crm.dao.CrmMedicalEnterpriseNumberGoodsRelationMapper;
import com.yiling.dataflow.crm.service.CrmMedicalEnterpriseNumberGoodsRelationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-02
 */
@Service
public class CrmMedicalEnterpriseNumberGoodsRelationServiceImpl extends BaseServiceImpl<CrmMedicalEnterpriseNumberGoodsRelationMapper, CrmMedicalEnterpriseNumberGoodsRelationDO> implements CrmMedicalEnterpriseNumberGoodsRelationService {

    @Override
    public Page<CrmMedicalEnterpriseNumberGoodsRelationDO> getCrmMedicalEnterpriseNumberGoodsRelationPage(QueryCrmMedicalEnterpriseNumberGoodsRelationRequest request) {
        QueryWrapper<CrmMedicalEnterpriseNumberGoodsRelationDO> queryWrapper = new QueryWrapper<>();
        Page<CrmMedicalEnterpriseNumberGoodsRelationDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, queryWrapper);
    }
}
