package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityAutoGiveGoodsRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveGoodsLimitDO;

/**
 * <p>
 * 自动发券活动商品限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGiveGoodsLimitService extends BaseService<CouponActivityAutoGiveGoodsLimitDO> {

    /**
     * 查询已添加商品信息列表
     * @param request
     * @return
     */
    Page<CouponActivityGoodsDTO> pageList(QueryCouponActivityAutoGiveGoodsRequest request);

    /**
     * 添加商品信息
     * @param request
     * @return
     */
    Boolean saveGoodsLimit(SaveCouponActivityAutoGiveGoodsLimitRequest request);

    /**
     * 删除已添加商品信息
     * @param request
     * @return
     */
    Boolean deleteGoodsLimit(DeleteCouponActivityAutoGiveGoodsLimitRequest request);

    /**
     * 根据自动发放活动ids查询指定商品
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveGoodsLimitDO> getGoodsLimitByAutoGiveIdList(List<Long> autoGiveIdList);

    /**
     * 根据自动发放活动id查询指定商品
     * @param autoGiveId
     * @return
     */
    List<CouponActivityAutoGiveGoodsLimitDO> getGoodsLimitByAutoGiveId(Long autoGiveId);

}
