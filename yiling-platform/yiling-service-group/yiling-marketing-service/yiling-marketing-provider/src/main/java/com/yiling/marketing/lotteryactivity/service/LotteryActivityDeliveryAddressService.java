package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDeliveryAddressDTO;
import com.yiling.marketing.lotteryactivity.dto.request.SaveDeliveryAddressRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDeliveryAddressDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动-奖品收货地址表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
public interface LotteryActivityDeliveryAddressService extends BaseService<LotteryActivityDeliveryAddressDO> {

    /**
     * 查询抽奖活动收货地址列表
     *
     * @param uid
     * @return
     */
    List<LotteryActivityDeliveryAddressDTO> queryList(Long uid);

    /**
     * 保存抽奖活动收货地址
     *
     * @param request
     * @return
     */
    boolean saveDeliveryAddress(SaveDeliveryAddressRequest request);
}
