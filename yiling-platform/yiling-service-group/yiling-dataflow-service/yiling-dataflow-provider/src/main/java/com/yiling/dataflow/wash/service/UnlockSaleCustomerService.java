package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleCustomerService extends BaseService<UnlockSaleCustomerDO> {

    List<Long> getCrmEnterpriseIdByRuleId(Long ruleId);

    boolean save(SaveUnlockSaleCustomerRequest request);

    boolean delete(DeleteUnlockSaleCustomerRequest request);

    Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request);
}
