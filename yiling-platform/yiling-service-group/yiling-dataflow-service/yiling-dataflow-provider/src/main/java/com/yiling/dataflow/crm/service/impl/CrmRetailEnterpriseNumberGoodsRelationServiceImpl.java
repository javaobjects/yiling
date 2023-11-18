package com.yiling.dataflow.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmRetailEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmMedicalEnterpriseNumberGoodsRelationDO;
import com.yiling.dataflow.crm.entity.CrmRetailEnterpriseNumberGoodsRelationDO;
import com.yiling.dataflow.crm.dao.CrmRetailEnterpriseNumberGoodsRelationMapper;
import com.yiling.dataflow.crm.service.CrmRetailEnterpriseNumberGoodsRelationService;
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
public class CrmRetailEnterpriseNumberGoodsRelationServiceImpl extends BaseServiceImpl<CrmRetailEnterpriseNumberGoodsRelationMapper, CrmRetailEnterpriseNumberGoodsRelationDO> implements CrmRetailEnterpriseNumberGoodsRelationService {

    @Override
    public Page<CrmRetailEnterpriseNumberGoodsRelationDO> getCrmRetailEnterpriseNumberGoodsRelationDOPage(QueryCrmRetailEnterpriseNumberGoodsRelationRequest retailEnterpriseNumberGoodsRelationRequest) {
        QueryWrapper<CrmRetailEnterpriseNumberGoodsRelationDO> queryWrapper = new QueryWrapper<>();
        Page<CrmRetailEnterpriseNumberGoodsRelationDO> page = new Page<>(retailEnterpriseNumberGoodsRelationRequest.getCurrent(), retailEnterpriseNumberGoodsRelationRequest.getSize());
        return this.page(page, queryWrapper);
    }
}
