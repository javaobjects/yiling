package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.entity.ErpSaleFlowDO;

/**
 * <p>
 * 流向销售明细信息表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
public interface ErpSaleFlowService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void synSaleFlow();

    List<ErpSaleFlowDO> synSaleFlowPage();

    void unLockSynSaleFlow(ErpFlowSealedUnLockRequest request);

    Long mathGoodsCrmCode(String goodsName, String spec, String manufacturer, String unit, String goodsInnerCode, CrmEnterpriseDTO crmEnterpriseDTO);
}
