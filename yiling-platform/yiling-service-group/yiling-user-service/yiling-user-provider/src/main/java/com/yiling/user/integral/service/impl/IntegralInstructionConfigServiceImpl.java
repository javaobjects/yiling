package com.yiling.user.integral.service.impl;

import java.util.Objects;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralInstructionConfigRequest;
import com.yiling.user.integral.entity.IntegralInstructionConfigDO;
import com.yiling.user.integral.dao.IntegralInstructionConfigMapper;
import com.yiling.user.integral.entity.IntegralInstructionLogDO;
import com.yiling.user.integral.service.IntegralInstructionConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralInstructionLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分说明配置 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralInstructionConfigServiceImpl extends BaseServiceImpl<IntegralInstructionConfigMapper, IntegralInstructionConfigDO> implements IntegralInstructionConfigService {

    @Autowired
    IntegralInstructionLogService integralInstructionLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveConfig(SaveIntegralInstructionConfigRequest request) {
        IntegralInstructionConfigDO instructionConfigDO = PojoUtils.map(request, IntegralInstructionConfigDO.class);

        IntegralInstructionConfigDTO instructionConfigDTO = Optional.ofNullable(this.get(request.getId(), request.getPlatform())).orElse(new IntegralInstructionConfigDTO());

        if (Objects.isNull(request.getId()) || request.getId() == 0) {
            this.save(instructionConfigDO);
        } else {
            this.updateById(instructionConfigDO);
        }

        // 插入变更日志记录
        IntegralInstructionLogDO instructionLogDO = new IntegralInstructionLogDO();
        instructionLogDO.setConfigId(instructionConfigDO.getId());
        instructionLogDO.setBeforeContent(Objects.nonNull(instructionConfigDTO.getContent()) ? instructionConfigDTO.getContent() : "");
        instructionLogDO.setAfterContent(instructionConfigDO.getContent());
        instructionLogDO.setOpUserId(request.getOpUserId());
        return integralInstructionLogService.save(instructionLogDO);
    }

    @Override
    public IntegralInstructionConfigDTO get(Long id, Integer platform) {
        if (Objects.nonNull(id) && id != 0) {
            return PojoUtils.map(this.getById(id), IntegralInstructionConfigDTO.class);
        } else {
            LambdaQueryWrapper<IntegralInstructionConfigDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(IntegralInstructionConfigDO::getPlatform, platform);
            wrapper.last("limit 1");
            return PojoUtils.map(this.getOne(wrapper), IntegralInstructionConfigDTO.class);
        }

    }
}
