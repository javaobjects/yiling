package com.yiling.marketing.presale.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.request.AddPresaleActivityRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;
import com.yiling.marketing.presale.dto.request.SavePresaleActivityRequest;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
public interface PresaleActivityApi {

    /**
     * 新增预售活动
     *
     * @param request 预售活动主信息
     * @return 预售活动主信息
     */
    PresaleActivityDTO save(AddPresaleActivityRequest request);

    /**
     * 新增预售活动-具体内容
     *
     * @param request 预售活动具体内容
     * @return 成功/失败
     */
    boolean saveAll(SavePresaleActivityRequest request);

    /**
     * 策略满赠的复制
     *
     * @param request 活动id
     * @return 预售活动主信息
     */
    StrategyActivityDTO copy(CopyStrategyRequest request);

    /**
     * 策略满赠的停用
     *
     * @param request 活动id
     * @return 成功/失败
     */
    boolean stop(StopStrategyRequest request);

    /**
     * 营销活动分页查询
     *
     * @param request 查询条件
     * @return 营销活动主表信息
     */
    Page<StrategyActivityDTO> pageList(QueryStrategyActivityPageRequest request);

    /**
     * 根据id查询活动主表信息
     *
     * @param id 主键id
     * @return 营销活动主表信息
     */
    StrategyActivityDTO queryById(Long id);

    /**
     * 根据活动id查询营销活动订单累计金额满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @return 营销活动订单累计金额满赠内容阶梯
     */
    List<StrategyAmountLadderDTO> listAmountLadderByActivityId(Long strategyActivityId);

    /**
     * 复制预售活动
     *
     * @param currentUserId 删除预售商品限制
     * @return 成功/失败
     */
    Long presaleActivityApi(Long currentUserId, Long id);

    /**
     * 预售活动-订单生成回调
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    Boolean presaleOrderCallback(PromotionSaveBuyRecordRequest request);

    /**
     * 预售活动信息(包含用户已经购买的商品数量，包含所有用户已经购买商品的数量)
     *
     * @param request 参数
     * @return 返回创建时间最新预售活动信息
     */
    List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEid(QueryPresaleInfoRequest request);

    /**
     * 预售活动信息 ===不包含用户购买数量
     *
     * @param request 参数
     * @return 返回创建时间最新预售活动信息
     */
    List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEidNoNum(QueryPresaleInfoRequest request);

    /**
     * 返回关联的订单信息
     *
     * @param request 预售活动id
     * @return 返回关联的订单信息
     */
    Page<PresaleActivityOrderDTO> queryOrderInfoByPresaleId(QueryPresaleOrderRequest request);


    /**
     * 通过预售活动id，返回预售活动简单信息
     *
     * @param idList 预售活动id数组
     * @return 返回预售活动简单信息
     */
    Map<Long, PresaleActivityDTO> batchQueryByIdList(List<Long> idList);
}
