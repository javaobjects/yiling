package com.yiling.marketing.promotion.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;

import com.yiling.marketing.promotion.dao.PromotionSpecialActivityMapper;
import com.yiling.marketing.promotion.dao.PromotionSpecialAppointmentMapper;

import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;
import com.yiling.marketing.promotion.service.PromotionSpecialAppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专场活动预约表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-20
 */
@Service
public class PromotionSpecialAppointmentServiceImpl extends BaseServiceImpl<PromotionSpecialAppointmentMapper, PromotionSpecialAppointmentDO> implements PromotionSpecialAppointmentService {

    @Autowired
    PromotionSpecialActivityMapper promotionSpecialActivityMapper;

    @Override
    public Page<SpecialAvtivityAppointmentItemDTO> queryMyAppointment(Long userId, SpecialActivityPageRequest request) {
        return promotionSpecialActivityMapper.queryMyAppointment(new Page<>(request.getCurrent(), request.getSize()), userId, request);
    }
}
