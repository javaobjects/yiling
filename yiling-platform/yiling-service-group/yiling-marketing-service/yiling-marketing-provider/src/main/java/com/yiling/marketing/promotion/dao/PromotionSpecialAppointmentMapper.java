package com.yiling.marketing.promotion.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentDTO;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 专场活动预约表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-20
 */
@Repository
public interface PromotionSpecialAppointmentMapper extends BaseMapper<PromotionSpecialAppointmentDO> {


    Page<SpecialAvtivityAppointmentDTO> queryMyAppointment(Page<Object> page, @Param("userId") Long userId, @Param("request") SpecialActivityPageRequest request);
}
