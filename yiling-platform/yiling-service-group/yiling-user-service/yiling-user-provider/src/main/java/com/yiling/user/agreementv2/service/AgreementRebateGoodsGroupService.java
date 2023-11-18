package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateGoodsGroupDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsGroupDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利商品组表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateGoodsGroupService extends BaseService<AgreementRebateGoodsGroupDO> {

    /**
     * 根据协议Id获取商品组
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateGoodsGroupDTO> getRebateGoodsGroupByAgreementId(Long agreementId);

    /**
     * 根据时段Id获取商品组
     *
     * @param segmentId
     * @return
     */
    List<AgreementRebateGoodsGroupDTO> getRebateGoodsGroupBySegmentId(Long segmentId);

}
