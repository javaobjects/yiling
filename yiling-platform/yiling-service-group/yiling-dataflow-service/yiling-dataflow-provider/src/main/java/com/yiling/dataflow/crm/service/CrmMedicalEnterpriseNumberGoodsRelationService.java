package com.yiling.dataflow.crm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmMedicalEnterpriseNumberGoodsRelationRequest;
import com.yiling.dataflow.crm.entity.CrmMedicalEnterpriseNumberGoodsRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-02
 */
public interface CrmMedicalEnterpriseNumberGoodsRelationService extends BaseService<CrmMedicalEnterpriseNumberGoodsRelationDO> {
    Page<CrmMedicalEnterpriseNumberGoodsRelationDO> getCrmMedicalEnterpriseNumberGoodsRelationPage(QueryCrmMedicalEnterpriseNumberGoodsRelationRequest request);
}
