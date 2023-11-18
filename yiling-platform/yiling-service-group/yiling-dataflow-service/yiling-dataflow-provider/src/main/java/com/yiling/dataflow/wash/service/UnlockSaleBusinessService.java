package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleBusinessDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleBusinessService extends BaseService<UnlockSaleBusinessDO> {

    List<Long> getCrmEnterpriseIdByRuleId(Long ruleId);

    boolean save(SaveUnlockSaleBusinessRequest request);

    boolean delete(DeleteUnlockSaleBusinessRequest request);

    Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request);
}
