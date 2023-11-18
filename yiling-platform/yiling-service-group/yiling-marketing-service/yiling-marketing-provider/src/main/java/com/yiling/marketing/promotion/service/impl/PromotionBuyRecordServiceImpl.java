package com.yiling.marketing.promotion.service.impl;

import java.util.List;
import java.util.Objects;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.promotion.dao.PromotionBuyRecordMapper;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateDetailRequest;
import com.yiling.marketing.promotion.entity.PromotionBuyRecordDO;
import com.yiling.marketing.promotion.service.PromotionBuyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 促销活动购买记录服务
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Slf4j
@Service
public class PromotionBuyRecordServiceImpl extends BaseServiceImpl<PromotionBuyRecordMapper, PromotionBuyRecordDO>
                                           implements PromotionBuyRecordService {

    @Override
    public List<PromotionBuyRecordDO> query(PromotionBuyRecord param) {
        QueryWrapper<PromotionBuyRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionBuyRecordDO::getPromotionActivityId, param.getPromotionActivityId());
        wrapper.lambda().eq(PromotionBuyRecordDO::getGoodsId, param.getGoodsId());
        wrapper.lambda().eq(PromotionBuyRecordDO::getEid, param.getEid());
        return this.list(wrapper);
    }

    @Override
    public Boolean saveBuyRecord(PromotionSaveBuyRecordRequest request) {
        log.info("[saveBuyRecord]保存购买记录参数:{}", request);
        List<PromotionBuyRecord> buyRecordList = request.getBuyRecordList();
        if (CollUtil.isEmpty(buyRecordList)) {
            log.info("[saveBuyRecord]保存购买记录参数为空，参数：{}", buyRecordList);
            return Boolean.FALSE;
        }
        List<PromotionBuyRecordDO> list = PojoUtils.map(buyRecordList, PromotionBuyRecordDO.class);
        return this.saveBatch(list);
    }

    @Override
    public Boolean updateBuyRecordQuantity(PromotionUpdateBuyRecordRequest request) {
        log.info("[updateBuyRecordQuantity]更新活动购买数量参数：{}", request);
        Long orderId = request.getOrderId();
        List<PromotionUpdateDetailRequest> detailList = request.getDetailList();
        if (Objects.isNull(orderId) || CollUtil.isEmpty(detailList)) {
            log.info("[updateBuyRecordQuantity]参数为空，参数:{}", request);
            return Boolean.FALSE;
        }
        int total = 0;
        for (PromotionUpdateDetailRequest item : detailList) {
            Long goodsId = item.getGoodsId();
            Integer quantity = item.getQuantity();
            total += this.baseMapper.updateBuyRecordQuantity(orderId, goodsId, quantity);
        }
        return total > 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
