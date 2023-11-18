package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementAttachmentDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementAttachmentRequest;
import com.yiling.user.agreementv2.entity.AgreementAttachmentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议附件表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementAttachmentService extends BaseService<AgreementAttachmentDO> {

    /**
     * 查询协议附件
     *
     * @param agreementId
     * @return
     */
    List<AgreementAttachmentDTO> getByAgreementId(Long agreementId);
}
