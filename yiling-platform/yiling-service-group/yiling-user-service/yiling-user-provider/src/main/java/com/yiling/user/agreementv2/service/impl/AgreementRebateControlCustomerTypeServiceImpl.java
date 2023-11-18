package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateControlCustomerTypeDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlCustomerTypeDO;
import com.yiling.user.agreementv2.dao.AgreementRebateControlCustomerTypeMapper;
import com.yiling.user.agreementv2.service.AgreementRebateControlCustomerTypeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利范围控销客户类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementRebateControlCustomerTypeServiceImpl extends BaseServiceImpl<AgreementRebateControlCustomerTypeMapper, AgreementRebateControlCustomerTypeDO> implements AgreementRebateControlCustomerTypeService {

    @Override
    public Map<Long, List<AgreementRebateControlCustomerTypeDTO>> getControlCustomerTypeMap(List<Long> rebateScopeIdList) {
        LambdaQueryWrapper<AgreementRebateControlCustomerTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AgreementRebateControlCustomerTypeDO::getRebateScopeId, rebateScopeIdList);
        List<AgreementRebateControlCustomerTypeDTO> customerTypeDTOList = PojoUtils.map(this.list(wrapper), AgreementRebateControlCustomerTypeDTO.class);
        return customerTypeDTOList.stream().collect(Collectors.groupingBy(AgreementRebateControlCustomerTypeDTO::getRebateScopeId));
    }
}
