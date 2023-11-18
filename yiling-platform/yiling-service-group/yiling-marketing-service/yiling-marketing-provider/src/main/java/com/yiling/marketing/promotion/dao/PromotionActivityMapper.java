package com.yiling.marketing.promotion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;

/**
 * <p>
 * 促销活动主表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface PromotionActivityMapper extends BaseMapper<PromotionActivityDO> {

    /**
     * 分页查询营销活动信息
     *
     * @param page
     * @param request
     * @return
     */
    Page<PromotionActivityPageDTO> pageList(Page<PromotionActivityDO> page, @Param("request") PromotionActivityPageRequest request);

    /**
     * 校验名称是否重复
     * @param name
     * @param eid
     * @param sponsorType
     * @return
     */
    List<PromotionActivityDO> selectByParam(@Param("name") String name, @Param("eid") Long eid, @Param("sponsorType") Integer sponsorType, @Param("id") Long id);

    /**
     * 查询企业参与的活动信息
     * @param request
     * @return
     */
    List<PromotionAppListDTO> queryEnterprisePromotion(@Param("request") PromotionEnterpriseRequest request);

    /**
     * 查询商品参与的活动信息
     * @param request
     * @return
     */
    List<PromotionGoodsLimitDTO> queryPromotionGoodsInfo(@Param("request") PromotionAppRequest request);

    /**
     * 某个商户的所有组合包产品
     * @param request
     * @return
     */
    List<PromotionGoodsLimitDTO> pagePromotionGoodsInfo(@Param("request") PromotionAppRequest request);

    /**
     * 查询我的预约个数
     * @param currentUserId
     * @return
     */
    SpecialAvtivityAppointmentItemDTO myAppointmentCount(@Param("currentUserId")Long currentUserId);
}
