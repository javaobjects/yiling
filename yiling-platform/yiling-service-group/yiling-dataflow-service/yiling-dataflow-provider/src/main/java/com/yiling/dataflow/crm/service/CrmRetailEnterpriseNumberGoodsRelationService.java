package com.yiling.dataflow.crm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmRetailEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmRetailEnterpriseNumberGoodsRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-02
 */
public interface CrmRetailEnterpriseNumberGoodsRelationService extends BaseService<CrmRetailEnterpriseNumberGoodsRelationDO> {

    Page<CrmRetailEnterpriseNumberGoodsRelationDO> getCrmRetailEnterpriseNumberGoodsRelationDOPage(QueryCrmRetailEnterpriseNumberGoodsRelationRequest retailEnterpriseNumberGoodsRelationRequest);

}
