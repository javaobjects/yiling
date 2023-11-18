package com.yiling.marketing.lotteryactivity.service;

import java.util.List;

import com.yiling.marketing.lotteryactivity.dto.LotteryActivityReceiptInfoDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityReceiptInfoDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动收货信息表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
public interface LotteryActivityReceiptInfoService extends BaseService<LotteryActivityReceiptInfoDO> {

    /**
     * 根据详情ID批量查询收货信息
     *
     * @param detailIdList
     * @return
     */
    List<LotteryActivityReceiptInfoDTO> getByDetailIdList(List<Long> detailIdList);

    /**
     * 根据详情ID查询收货信息
     *
     * @param detailId
     * @return
     */
    LotteryActivityReceiptInfoDTO getByDetailId(Long detailId);


}
