package com.yiling.marketing.lotteryactivity.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDeliveryAddressDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveDeliveryAddressRequest;

/**
 * <p>
 * 抽奖活动收货地址 API
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
public interface LotteryActivityDeliveryAddressApi {

    /**
     * 查询抽奖活动收货地址列表
     *
     * @param uid
     * @return
     */
    List<LotteryActivityDeliveryAddressDTO> queryList(Long uid);

    /**
     * 获取单个抽奖活动收货地址信息
     *
     * @param id
     * @return
     */
    LotteryActivityDeliveryAddressDTO getById(Long id);

    /**
     * 保存抽奖活动收货地址
     *
     * @param request
     * @return
     */
    boolean saveDeliveryAddress(SaveDeliveryAddressRequest request);

    /**
     * 获取单个抽奖活动收货地址信息
     *
     * @param id
     * @param opUserId
     * @return
     */
    boolean deleteById(Long id, Long opUserId);


}
