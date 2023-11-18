package com.yiling.goods.restriction.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;
import com.yiling.goods.restriction.entity.GoodsPurchaseRestrictionCustomerDO;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionCustomerMapper
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Repository
public interface GoodsPurchaseRestrictionCustomerMapper extends BaseMapper<GoodsPurchaseRestrictionCustomerDO> {
    /**
     * 限购id查询限购客户
     * @param goodsId
     * @return
     */
    List<Long> getCustomerEidByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 限购id查询限购客户数量
     * @param goodsId
     * @return
     */
    Long getCustomerCountByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 批量添加客户
     * @param request
     * @return
     */
    Long batchSaveRestrictionCustomer(@Param("request")SaveRestrictionCustomerRequest request);
}
