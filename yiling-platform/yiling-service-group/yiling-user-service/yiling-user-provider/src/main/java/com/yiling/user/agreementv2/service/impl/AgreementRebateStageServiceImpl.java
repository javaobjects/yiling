package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateStageDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateStageDO;
import com.yiling.user.agreementv2.dao.AgreementRebateStageMapper;
import com.yiling.user.agreementv2.service.AgreementRebateStageService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 协议返利阶梯表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementRebateStageServiceImpl extends BaseServiceImpl<AgreementRebateStageMapper, AgreementRebateStageDO> implements AgreementRebateStageService {

    @Override
    public List<AgreementRebateStageDTO> getRebateStageList(Long taskStageId) {
        LambdaQueryWrapper<AgreementRebateStageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateStageDO::getTaskStageId, taskStageId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateStageDTO.class);
    }

    @Override
    public Map<Long, List<AgreementRebateStageDTO>> getStageMapByStageIdList(List<Long> taskStageIdList) {
        if (CollUtil.isEmpty(taskStageIdList)) {
            return MapUtil.newHashMap();
        }
        LambdaQueryWrapper<AgreementRebateStageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AgreementRebateStageDO::getTaskStageId, taskStageIdList);
        List<AgreementRebateStageDTO> list = PojoUtils.map(this.list(wrapper), AgreementRebateStageDTO.class);
        return list.stream().collect(Collectors.groupingBy(AgreementRebateStageDTO::getTaskStageId));
    }
}
