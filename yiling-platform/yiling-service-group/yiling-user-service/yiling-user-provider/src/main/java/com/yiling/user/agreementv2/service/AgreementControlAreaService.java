package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementControlAreaDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementControlAreaRequest;
import com.yiling.user.agreementv2.entity.AgreementControlAreaDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议控销区域表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementControlAreaService extends BaseService<AgreementControlAreaDO> {

    /**
     * 保存协议控销区域
     *
     * @param allControlAreaList
     */
    Boolean saveControlAreaList(List<AddAgreementControlAreaRequest> allControlAreaList);

    /**
     * 获取控销区域
     *
     * @param controlGoodsGroupId
     * @return
     */
    AgreementControlAreaDTO getControlArea(Long controlGoodsGroupId);

}
