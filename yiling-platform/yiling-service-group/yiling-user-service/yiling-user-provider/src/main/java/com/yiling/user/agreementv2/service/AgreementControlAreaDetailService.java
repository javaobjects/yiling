package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.request.QueryAgreementControlAreaDetailRequest;
import com.yiling.user.agreementv2.entity.AgreementControlAreaDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议控销区域详情表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementControlAreaDetailService extends BaseService<AgreementControlAreaDetailDO> {

    /**
     * 查询供销区域详情
     * @param request
     * @return
     */
    List<AgreementControlAreaDetailDO> getControlAreaDetailList(QueryAgreementControlAreaDetailRequest request);

}
