package com.yiling.user.agreementv2.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreementv2.dto.AgreementRebateGoodsDTO;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利商品表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateGoodsService extends BaseService<AgreementRebateGoodsDO> {

    /**
     * 根据协议ID获取返利商品分页
     *
     * @param request
     * @return
     */
    Page<AgreementRebateGoodsDO> getRebateGoodsByAgreementId(QueryAgreementGoodsPageRequest request);

    /**
     * 根据协议ID获取商品
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateGoodsDTO> getRebateGoodsByAgreementId(Long agreementId);

}
