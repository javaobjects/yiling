package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityReceiptInfoDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityReceiptInfoDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityReceiptInfoMapper;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityReceiptInfoService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 抽奖活动收货信息表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityReceiptInfoServiceImpl extends BaseServiceImpl<LotteryActivityReceiptInfoMapper, LotteryActivityReceiptInfoDO> implements LotteryActivityReceiptInfoService {

    @Override
    public List<LotteryActivityReceiptInfoDTO> getByDetailIdList(List<Long> detailIdList) {
        if (CollUtil.isEmpty(detailIdList)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<LotteryActivityReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LotteryActivityReceiptInfoDO::getJoinDetailId, detailIdList);
        return PojoUtils.map(this.list(wrapper), LotteryActivityReceiptInfoDTO.class);
    }

    @Override
    public LotteryActivityReceiptInfoDTO getByDetailId(Long detailId) {
        LambdaQueryWrapper<LotteryActivityReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityReceiptInfoDO::getJoinDetailId, detailId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), LotteryActivityReceiptInfoDTO.class);
    }
}
