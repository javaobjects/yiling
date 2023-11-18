package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementControlCustomerTypeDTO;
import com.yiling.user.agreementv2.entity.AgreementControlCustomerTypeDO;
import com.yiling.user.agreementv2.dao.AgreementControlCustomerTypeMapper;
import com.yiling.user.agreementv2.service.AgreementControlCustomerTypeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议控销客户类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementControlCustomerTypeServiceImpl extends BaseServiceImpl<AgreementControlCustomerTypeMapper, AgreementControlCustomerTypeDO> implements AgreementControlCustomerTypeService {

    @Override
    public List<AgreementControlCustomerTypeDTO> getControlCustomerTypeList(Long controlGoodsGroupId) {
        LambdaQueryWrapper<AgreementControlCustomerTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementControlCustomerTypeDO::getControlGoodsGroupId, controlGoodsGroupId);
        return PojoUtils.map(this.list(wrapper), AgreementControlCustomerTypeDTO.class);
    }
}
