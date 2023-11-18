package com.yiling.marketing.promotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.entity.PromotionBuyRecordDO;

/**
 * 促销活动购买记录服务
 *
 * @author fan.shen
 * @date 2022-2-8
 */
public interface PromotionBuyRecordService extends BaseService<PromotionBuyRecordDO> {

    /**
     * 保存购买记录
     * @param buyRecordRequest
     * @return
     */
    Boolean saveBuyRecord(PromotionSaveBuyRecordRequest buyRecordRequest);

    /**
     * 查询购买记录
     * @param param
     * @return
     */
    List<PromotionBuyRecordDO> query(PromotionBuyRecord param);

    /**
     * 退货更新购买数量
     * @param request
     * @return
     */
    Boolean updateBuyRecordQuantity(PromotionUpdateBuyRecordRequest request);

}
