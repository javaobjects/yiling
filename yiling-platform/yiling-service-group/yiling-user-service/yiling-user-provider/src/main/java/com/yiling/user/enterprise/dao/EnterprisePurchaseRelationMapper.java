package com.yiling.user.enterprise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.bo.CountSellerChannelBO;
import com.yiling.user.enterprise.bo.PurchaseRelationBO;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.entity.EnterprisePurchaseRelationDO;

/**
 * <p>
 * 企业采购关系 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Repository
public interface EnterprisePurchaseRelationMapper extends BaseMapper<EnterprisePurchaseRelationDO> {
    Page<PurchaseRelationBO> pageList(Page page, @Param("request") QueryPurchaseRelationPageListRequest request);

    /**
     *  销售助手选择配送商-按照下单时间(倒序排序)
     * @param page
     * @param request
     * @return
     */
    Page<PurchaseRelationBO> pageListOrderByOrderTime(Page page, @Param("request") QueryPurchaseRelationPageListRequest request);

    Page<PurchaseRelationBO> canPurchaseEnterprisePageList(Page page, @Param("request")QueryPurchaseRelationPageListRequest request);

    List<CountSellerChannelBO> countSellerChannel(@Param("request")QueryPurchaseRelationPageListRequest request);
}
