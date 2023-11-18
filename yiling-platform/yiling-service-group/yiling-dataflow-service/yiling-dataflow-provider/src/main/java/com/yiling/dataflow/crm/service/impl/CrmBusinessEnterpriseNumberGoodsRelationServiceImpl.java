package com.yiling.dataflow.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmBusinessEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmBusinessEnterpriseNumberGoodsRelationDO;
import com.yiling.dataflow.crm.dao.CrmBusinessEnterpriseNumberGoodsRelationMapper;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmBusinessEnterpriseNumberGoodsRelationService;
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
public class CrmBusinessEnterpriseNumberGoodsRelationServiceImpl extends BaseServiceImpl<CrmBusinessEnterpriseNumberGoodsRelationMapper, CrmBusinessEnterpriseNumberGoodsRelationDO> implements CrmBusinessEnterpriseNumberGoodsRelationService {

    @Override
    public Page<CrmBusinessEnterpriseNumberGoodsRelationDO> getCrmBusinessEnterpriseNumberGoodsRelationPage(QueryCrmBusinessEnterpriseNumberGoodsRelationRequest request) {
        QueryWrapper<CrmBusinessEnterpriseNumberGoodsRelationDO> queryWrapper = new QueryWrapper<>();
        Page<CrmBusinessEnterpriseNumberGoodsRelationDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, queryWrapper);
    }
}
