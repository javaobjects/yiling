package com.yiling.marketing.promotion.dao;


import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionSpecialActivityAppointmentPageDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityItemInfoDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityEnterpriseDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 专场活动主表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
@Repository
public interface PromotionSpecialActivityMapper extends BaseMapper<PromotionSpecialActivityDO> {

    /**
     * 分页查询营销活动信息
     *
     * @param page
     * @param request
     * @return
     */
    Page<SpecialActivityEnterpriseDTO> pageActivityInfo(Page<PromotionActivityDO> page, @Param("request") SpecialActivityPageRequest request);

    /**
     * 分页查询预约活动
     *
     * @param page
     * @param request
     * @return
     */
    Page<PromotionSpecialActivityAppointmentPageDTO> pageSpecialActivityAppointmentInfo(Page<PromotionSpecialAppointmentDO> page, @Param("request") SpecialActivityPageRequest request);

    /**
     * 活动中心数据
     *
     * @param
     * @param
     * @return
     */
    List<SpecialActivityPageDTO> activityCenter();

    /**
     * 专场活动app数据
     *
     * @param request
     * @return
     */
    List<SpecialAvtivityItemInfoDTO> pageSpecialActivityInfo(@Param("request") SpecialActivityInfoRequest request);

    /**
     * 专场活动app企业信息
     *
     * @param
     * @param
     * @return
     */
    List<Long> pageSpecialActivityEidInfo(@Param("request") SpecialActivityInfoRequest request);

    /**
     * 我的预约
     *
     * @param
     * @param
     * @return
     */
    Page<SpecialAvtivityAppointmentItemDTO> queryMyAppointment(Page<Object> objectPage, @Param("userId") Long userId, @Param("request") SpecialActivityPageRequest request);

    /**
     * 我的预约
     *
     * @param
     * @param
     * @return
     */
    List<PromotionSpecialActivityEnterpriseDO> selectEnterpriseBySpecialActivityId(@Param("request")SpecialActivityPageRequest request);

    /**
     * 已经占用的活动
     *
     * @param
     * @param
     * @return
     */
    List<PromotionSpecialActivityDO> getSpecialActivityInfo(@Param("eid")Long eid, @Param("promotionActivityId")Long promotionActivityId);
}
