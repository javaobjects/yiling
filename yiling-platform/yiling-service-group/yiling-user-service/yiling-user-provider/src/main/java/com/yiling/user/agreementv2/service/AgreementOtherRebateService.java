package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementOtherRebateDTO;
import com.yiling.user.agreementv2.entity.AgreementOtherRebateDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 非商品返利表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementOtherRebateService extends BaseService<AgreementOtherRebateDO> {

    /**
     * 获取非商品返利
     *
     * @param agreementId
     * @return
     */
    List<AgreementOtherRebateDTO> getOtherRebateList(Long agreementId);

}
