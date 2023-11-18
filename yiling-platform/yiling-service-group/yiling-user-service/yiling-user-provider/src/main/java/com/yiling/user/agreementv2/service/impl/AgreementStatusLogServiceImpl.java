package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementStatusLogDTO;
import com.yiling.user.agreementv2.entity.AgreementStatusLogDO;
import com.yiling.user.agreementv2.dao.AgreementStatusLogMapper;
import com.yiling.user.agreementv2.service.AgreementStatusLogService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议状态日志表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
@Service
public class AgreementStatusLogServiceImpl extends BaseServiceImpl<AgreementStatusLogMapper, AgreementStatusLogDO> implements AgreementStatusLogService {

    @Override
    public List<AgreementStatusLogDTO> getByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementStatusLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementStatusLogDO::getAgreementId, agreementId);
        wrapper.orderByDesc(AgreementStatusLogDO::getCreateTime);
        return PojoUtils.map(this.list(wrapper), AgreementStatusLogDTO.class);
    }
}
