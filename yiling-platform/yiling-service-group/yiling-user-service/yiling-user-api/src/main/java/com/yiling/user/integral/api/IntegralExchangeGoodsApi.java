package com.yiling.user.integral.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralExchangeGoodsItemBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.IntegralExchangeGoodsDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralExchangeGoodsRequest;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.dto.request.UpdateShelfStatusRequest;

/**
 * 积分兑换商品 API
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
public interface IntegralExchangeGoodsApi {

    /**
     * 积分兑换商品分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralExchangeGoodsItemBO> queryListPage(QueryIntegralExchangeGoodsPageRequest request);

    /**
     * 更新上下架状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateShelfStatusRequest request);

    /**
     * 保存积分兑换商品
     *
     * @param request
     * @return
     */
    boolean saveExchangeGoods(SaveIntegralExchangeGoodsRequest request);

    /**
     * 根据ID获取积分兑换商品信息
     *
     * @param id
     * @return
     */
    IntegralExchangeGoodsDTO getById(Long id);

    /**
     * 根据积分兑换商品ID查询区分用户会员ID
     *
     * @param exchangeGoodsId
     * @return
     */
    List<Long> getMemberByExchangeGoodsId(Long exchangeGoodsId);

    /**
     * 根据积分兑换商品ID集合查询对应的区分用户会员ID
     *
     * @param exchangeGoodsIdList
     * @return
     */
    Map<Long, List<Long>> getMemberByExchangeGoodsIdList(List<Long> exchangeGoodsIdList);

}
