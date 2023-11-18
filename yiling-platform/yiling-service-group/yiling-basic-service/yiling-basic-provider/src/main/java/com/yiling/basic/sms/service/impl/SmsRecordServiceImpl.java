package com.yiling.basic.sms.service.impl;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.sms.dao.SmsRecordMapper;
import com.yiling.basic.sms.dto.request.QuerySmsRecordPageListRequest;
import com.yiling.basic.sms.entity.SmsRecordDO;
import com.yiling.basic.sms.enums.SmsStatusEnum;
import com.yiling.basic.sms.service.SmsRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 短信记录表服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-07
 */
@Slf4j
@Service
public class SmsRecordServiceImpl extends BaseServiceImpl<SmsRecordMapper, SmsRecordDO> implements SmsRecordService {

    @Override
    public boolean updateStatus(Long id, SmsStatusEnum statusEnum, String remark) {
        SmsRecordDO entity = new SmsRecordDO();
        entity.setId(id);
        entity.setStatus(statusEnum.getCode());
        entity.setRemark(remark);
        return this.updateById(entity);
    }

    @Override
    public Page<SmsRecordDO> pageList(QuerySmsRecordPageListRequest request) {
        Page<SmsRecordDO> page = new Page<>(request.getCurrent(), request.getSize());
        QueryWrapper<SmsRecordDO> queryWrapper = new QueryWrapper<>();
        String mobile = request.getMobile();
        if (StringUtils.isNotEmpty(mobile)) {
            queryWrapper.lambda().like(SmsRecordDO::getMobile, mobile);
        }
        Integer status = request.getStatus();
        if (status != null) {
            queryWrapper.lambda().eq(SmsRecordDO::getStatus, status);
        }
        if (Objects.nonNull(request.getStartCreateTime())) {
            queryWrapper.lambda().ge(SmsRecordDO::getCreateTime, request.getStartCreateTime());
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            queryWrapper.lambda().le(SmsRecordDO::getCreateTime, request.getEndCreateTime());
        }

        queryWrapper.lambda().orderByDesc(SmsRecordDO::getCreateTime);

        return this.page(page, queryWrapper);
    }
}
