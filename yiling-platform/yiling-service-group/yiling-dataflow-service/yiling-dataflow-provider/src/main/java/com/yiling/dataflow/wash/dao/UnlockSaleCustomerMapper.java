package com.yiling.dataflow.wash.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleBusinessDO;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Repository
public interface UnlockSaleCustomerMapper extends BaseMapper<UnlockSaleCustomerDO> {
    Page<CrmEnterpriseBusinessRuleBO> listPage(@Param("request") QueryCrmEnterpriseUnlockSalePageListRequest request, Page<UnlockSaleBusinessDO> page);
}
