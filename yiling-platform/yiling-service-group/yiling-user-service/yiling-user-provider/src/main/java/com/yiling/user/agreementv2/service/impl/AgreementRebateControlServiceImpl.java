package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateControlDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlDO;
import com.yiling.user.agreementv2.dao.AgreementRebateControlMapper;
import com.yiling.user.agreementv2.service.AgreementRebateControlService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 协议返利范围控销条件表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementRebateControlServiceImpl extends BaseServiceImpl<AgreementRebateControlMapper, AgreementRebateControlDO> implements AgreementRebateControlService {

    @Override
    public Map<Long, List<AgreementRebateControlDTO>> getRebateControlMap(List<Long> rebateScopeIdList) {
        if (CollUtil.isEmpty(rebateScopeIdList)) {
            return MapUtil.newHashMap();
        }
        LambdaQueryWrapper<AgreementRebateControlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AgreementRebateControlDO::getRebateScopeId, rebateScopeIdList);
        List<AgreementRebateControlDTO> controlDTOList = PojoUtils.map(this.list(wrapper), AgreementRebateControlDTO.class);
        return controlDTOList.stream().collect(Collectors.groupingBy(AgreementRebateControlDTO::getRebateScopeId));
    }
}
