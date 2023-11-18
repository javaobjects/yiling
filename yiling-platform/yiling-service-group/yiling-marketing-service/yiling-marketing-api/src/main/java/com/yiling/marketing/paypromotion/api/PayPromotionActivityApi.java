package com.yiling.marketing.paypromotion.api;


import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityEidOrGoodsIdDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionCalculateRuleDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionParticipateDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionSellerLimitActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.AddPayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPaypromotionSellerLimitPageRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;

/**
 * @author: yong.zhang
 * @date: 2022/9/21
 */
public interface PayPromotionActivityApi {

    /**
     * id查询支付促销活动
     * @param id
     * @return
     */
    PayPromotionActivityDTO getById(Long id);

    /**
     * id查询支付促销活动
     * @param ids
     * @return
     */
    List<PayPromotionActivityDTO> getByIds(List<Long> ids);

    /**
     * 查询支付促销列表
     * @param request
     * @return
     */
    Page<PayPromotionActivityDTO> pageList(QueryPayPromotionActivityPageRequest request);

    /**
     * 保存支付促销基本信息
     * @param request
     * @return
     */
    Long saveBasic(AddPayPromotionActivityRequest request);

    /**
     * 保存支付促销规则信息
     * @param request
     * @return
     */
    boolean saveAll(SavePayPromotionActivityRequest request);

    /**
     * 保存支付促销规则信息
     * @param request
     * @return
     */
    boolean stop(StopStrategyRequest request);

    /**
     * 支付促销添加关联的商家
     *
     * @param request
     * @return
     */
    boolean addSellerLimiti(AddStrategySellerLimitRequest request);

    /**
     * 支付促销删除关联的商家
     *
     * @param request
     * @return
     */
    boolean deletePayPromotionSeller(DeleteStrategySellerLimitRequest request);

    /**
     * 支付促销查询关联的商家
     *
     * @param request
     * @return
     */
    Page<PayPromotionSellerLimitActivityDTO> PayPromotionSellerPageList(QueryPaypromotionSellerLimitPageRequest request);

    /**
     * 支付促销id查询商家id集合
     *
     * @param marketingStrategyId
     * @return
     */
    List<PayPromotionSellerLimitActivityDTO> listSellerByActivityId(Long marketingStrategyId);

    /**
     * 支付促销id查询计算规则
     *
     * @param id
     * @return
     */
    List<PayPromotionCalculateRuleDTO> getPayCalculateRuleList(Long id);

    /**
     * 支付促销id获取参与记录列表
     *
     * @param request
     * @return
     */
    Page<PayPromotionParticipateDTO>  getPayPromotionParticipateById(QueryPayPromotionActivityPageRequest request);

    /**
     * 新增支付促销活动参与记录
     *
     * @param requestList 新增数据
     * @return 成功/失败
     */
    boolean savePayPromotionRecord(List<SavePayPromotionRecordRequest> requestList);

    /**
     * 根据支付促销活动id查询可用商品列表分页
     *
     * @param activityId 活动id
     * @param buyerEid 商家企业id
     * @param sellerEid 卖家企业id
     * @return 可用商品列表分页
     */
    PayPromotionActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long activityId, Long buyerEid, Long sellerEid);

    /**
     * 支付促销复制
     *
     * @param request 活动id
     * @return 可用商品列表分页
     */
    PayPromotionActivityDTO copy(CopyStrategyRequest request);
}
