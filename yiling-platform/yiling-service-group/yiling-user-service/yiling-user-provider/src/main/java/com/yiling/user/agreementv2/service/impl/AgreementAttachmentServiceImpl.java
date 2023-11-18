package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementAttachmentDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementAttachmentRequest;
import com.yiling.user.agreementv2.entity.AgreementAttachmentDO;
import com.yiling.user.agreementv2.dao.AgreementAttachmentMapper;
import com.yiling.user.agreementv2.service.AgreementAttachmentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议附件表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementAttachmentServiceImpl extends BaseServiceImpl<AgreementAttachmentMapper, AgreementAttachmentDO> implements AgreementAttachmentService {

    @Override
    public List<AgreementAttachmentDTO> getByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementAttachmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementAttachmentDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementAttachmentDTO.class);
    }
}
