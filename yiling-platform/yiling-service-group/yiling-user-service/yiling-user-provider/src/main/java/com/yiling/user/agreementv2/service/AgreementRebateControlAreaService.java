package com.yiling.user.agreementv2.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.agreementv2.dto.AgreementRebateControlAreaDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateControlAreaRequest;
import com.yiling.user.agreementv2.entity.AgreementRebateControlAreaDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利范围控销区域表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementRebateControlAreaService extends BaseService<AgreementRebateControlAreaDO> {

    /**
     * 保存区域信息
     * @param rebateControlArea
     * @return
     */
    Boolean saveArea(AddAgreementRebateControlAreaRequest rebateControlArea);

    /**
     * 根据返利范围ID批量获取返利控销区域
     *
     * @param rebateScopeIdList
     * @return
     */
    Map<Long, AgreementRebateControlAreaDTO> getRebateControlAreaMap(List<Long> rebateScopeIdList);
}
