package com.yiling.dataflow.relation.service;

import com.yiling.dataflow.relation.dto.FlowGoodsPriceRelationDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowGoodsPriceRelationRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsPriceRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-06-13
 */
public interface FlowGoodsPriceRelationService extends BaseService<FlowGoodsPriceRelationDO> {

    /**
     * 通过商品名称和规格获取价格信息
     * @param goodsName
     * @param spec
     * @return
     */
    FlowGoodsPriceRelationDO getByGoodsNameAndSpec(String goodsName,String spec);

    /**
     * 通过商品名称，规格，商业公司获取crm产品Id信息
     * @param goodsName
     * @param spec
     * @param enterpriseCode
     * @return
     */
    FlowGoodsPriceRelationDTO getByGoodsNameAndSpecAndEnterpriseCode(String goodsName, String spec, String enterpriseCode);

    /**
     * 新增
     * @param request
     * @return
     */
    boolean save(SaveFlowGoodsPriceRelationRequest request);

}
