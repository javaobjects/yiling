package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.strategy.dto.StrategyStageMemberEffectDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyStageMemberEffectPageRequest;

/**
 * 策略满赠购买会员方案表Api
 *
 * @author: yong.zhang
 * @date: 2022/9/5
 */
public interface StrategyStageMemberEffectApi {

    /**
     * 添加购买会员方案
     *
     * @param request 添加购买会员方案
     * @return 成功/失败
     */
    boolean add(AddStrategyStageMemberEffectRequest request);

    /**
     * 删除购买会员方案
     *
     * @param request 删除购买会员方案
     * @return 成功/失败
     */
    boolean delete(DeleteStrategyStageMemberEffectRequest request);

    /**
     * 策略满赠客户-购买会员方案列表查询
     *
     * @param request 查询条件
     * @return 购买会员方案
     */
    Page<StrategyStageMemberEffectDTO> pageList(QueryStrategyStageMemberEffectPageRequest request);

    /**
     * 根据活动id查询购买会员方案数量
     *
     * @param strategyActivityId 活动id
     * @return 购买会员方案数量
     */
    Integer countStageMemberEffectByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询购买会员方案
     *
     * @param strategyActivityId 活动id
     * @return 购买会员方案
     */
    List<StrategyStageMemberEffectDTO> listByActivityId(Long strategyActivityId);
}
