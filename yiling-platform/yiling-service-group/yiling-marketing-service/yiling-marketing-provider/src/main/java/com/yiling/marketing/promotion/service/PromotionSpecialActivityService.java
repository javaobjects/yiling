package com.yiling.marketing.promotion.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.AvtivityCenterDTO;
import com.yiling.marketing.promotion.dto.PromotionSpecialActivityAppointmentPageDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityInfoDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityItemInfoDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialSaveRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityDO;

/**
 * <p>
 * 专场活动主表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
public interface PromotionSpecialActivityService extends BaseService<PromotionSpecialActivityDO> {

    /**
     * 根据营销活动id营销活动信息(包含主表和商品表信息，企业表，秒杀表信息)
     *
     * @param request
     * @return
     */
    Page<SpecialActivityPageDTO> querySpecialActivity(SpecialActivityPageRequest request);

    /**
     * 保存专场活动信息
     *
     * @param request
     * @return
     */
    long savePromotionSpecialActivity(PromotionSpecialSaveRequest request);

    /**
     * 通过id查询专场活动信息
     *
     * @param id
     * @return
     */
    SpecialActivityPageDTO querySpecialActivityInfo(Long id);

    /**
     * 查询符合条件的企业和营销活动信息
     *
     * @param request
     * @return
     */
    Page<SpecialActivityEnterpriseDTO> pageActivityInfo(SpecialActivityPageRequest request);

    /**
     * 停用专场活动
     *
     * @param request
     * @return
     */
    boolean editStatusById(PromotionActivityStatusRequest request);

    /**
     * 查询专场活动预约信息
     *
     * @param request
     * @return
     */
    Page<PromotionSpecialActivityAppointmentPageDTO> querySpecialActivityAppointment(SpecialActivityPageRequest request);

    /**
     * 活动中心专场活动信息
     *
     * @param
     * @return
     */
    AvtivityCenterDTO activityCenter(Long currentEid);

    /**
     *
     *专场活动信息
     * @param
     * @return
     */
    List<SpecialAvtivityInfoDTO> specialActivityInfo(SpecialActivityInfoRequest request);
}
