package com.yiling.dataflow.crm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmBusinessEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmBusinessEnterpriseNumberGoodsRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-02
 */
public interface CrmBusinessEnterpriseNumberGoodsRelationService extends BaseService<CrmBusinessEnterpriseNumberGoodsRelationDO> {
        Page<CrmBusinessEnterpriseNumberGoodsRelationDO> getCrmBusinessEnterpriseNumberGoodsRelationPage(QueryCrmBusinessEnterpriseNumberGoodsRelationRequest request);
}
