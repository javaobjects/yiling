package com.yiling.marketing.promotion.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.marketing.promotion.dto.AvtivityCenterDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;

/**
 * <p>
 * 专场活动预约表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-20
 */
public interface PromotionSpecialAppointmentService extends BaseService<PromotionSpecialAppointmentDO> {

    Page<SpecialAvtivityAppointmentItemDTO> queryMyAppointment(Long userId, SpecialActivityPageRequest type);
}
