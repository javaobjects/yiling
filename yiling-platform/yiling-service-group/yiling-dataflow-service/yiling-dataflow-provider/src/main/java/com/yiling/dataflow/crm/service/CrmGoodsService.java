package com.yiling.dataflow.crm.service;

import java.util.List;
import java.util.Map;

import com.yiling.dataflow.crm.dto.CrmGoodsDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * crm商品信息 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-11
 */
public interface CrmGoodsService extends BaseService<CrmGoodsDO> {

    /**
     * 规格Id
     * @param specificationId
     * @return
     */
    List<CrmGoodsDTO> getCrmGoodsBySpecificationId(Long specificationId);

    Map<Long, CrmGoodsInfoDTO> getCrmGoodsMap();
}
