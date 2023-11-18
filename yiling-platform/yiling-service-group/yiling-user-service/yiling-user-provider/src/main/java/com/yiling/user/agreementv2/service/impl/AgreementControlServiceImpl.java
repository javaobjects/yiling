package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementControlDTO;
import com.yiling.user.agreementv2.entity.AgreementControlDO;
import com.yiling.user.agreementv2.dao.AgreementControlMapper;
import com.yiling.user.agreementv2.service.AgreementControlService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议控销条件表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementControlServiceImpl extends BaseServiceImpl<AgreementControlMapper, AgreementControlDO> implements AgreementControlService {

    @Override
    public List<AgreementControlDTO> getControlList(Long controlGoodsGroupId) {
        LambdaQueryWrapper<AgreementControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementControlDO::getControlGoodsGroupId, controlGoodsGroupId);
        return PojoUtils.map(this.list(wrapper), AgreementControlDTO.class);
    }
}
