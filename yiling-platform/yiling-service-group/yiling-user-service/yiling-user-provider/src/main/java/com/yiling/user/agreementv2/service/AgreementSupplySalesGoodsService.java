package com.yiling.user.agreementv2.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议供销商品表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementSupplySalesGoodsService extends BaseService<AgreementSupplySalesGoodsDO> {

    /**
     * 获取供销商品数量
     *
     * @param agreementId
     * @return
     */
    Integer getSupplyGoodsNumber(Long agreementId);

    /**
     * 查询供销商品分页列表
     *
     * @param request
     * @return
     */
    Page<AgreementSupplySalesGoodsDTO> getSupplyGoodsByAgreementId(QueryAgreementGoodsPageRequest request);

    /**
     * 根据控销商品组ID查询商品组对应商品
     *
     * @param controlGoodsGroupId
     * @return
     */
    List<AgreementSupplySalesGoodsDTO> getSupplyGoodsList(Long controlGoodsGroupId);

    /**
     * 根据协议ID查询供销商品
     *
     * @param agreementId
     * @return
     */
    List<AgreementSupplySalesGoodsDTO> getSupplyGoodsByAgreementId(Long agreementId);
}
