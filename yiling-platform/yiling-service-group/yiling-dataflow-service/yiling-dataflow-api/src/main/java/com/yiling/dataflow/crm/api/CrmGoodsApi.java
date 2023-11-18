package com.yiling.dataflow.crm.api;

import java.util.List;

import com.yiling.dataflow.crm.dto.CrmGoodsDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
public interface CrmGoodsApi {
    List<CrmGoodsDTO> getCrmGoodsBySpecificationId(Long specificationId);
}
