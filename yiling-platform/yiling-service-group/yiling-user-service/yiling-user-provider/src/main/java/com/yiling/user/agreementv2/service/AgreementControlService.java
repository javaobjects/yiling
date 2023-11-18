package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementControlDTO;
import com.yiling.user.agreementv2.entity.AgreementControlDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议控销条件表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementControlService extends BaseService<AgreementControlDO> {

    /**
     * 获取控销条件集合
     *
     * @param controlGoodsGroupId
     * @return
     */
    List<AgreementControlDTO> getControlList(Long controlGoodsGroupId);

}
