package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGetMapper;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetPageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGetDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGetService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 获取抽奖机会明细表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Service
public class LotteryActivityGetServiceImpl extends BaseServiceImpl<LotteryActivityGetMapper, LotteryActivityGetDO> implements LotteryActivityGetService {

    @Override
    public Page<LotteryActivityGetDTO> queryPageList(QueryLotteryActivityGetPageRequest request) {
        LambdaQueryWrapper<LotteryActivityGetDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getLotteryActivityId()) && request.getLotteryActivityId() != 0) {
            wrapper.eq(LotteryActivityGetDO::getLotteryActivityId, request.getLotteryActivityId());
        }
        return PojoUtils.map(this.page(request.getPage(), wrapper), LotteryActivityGetDTO.class);
    }

    @Override
    public Map<Long, Integer> getNumberByLotteryActivityId(List<Long> lotteryActivityIdList) {
        if (CollUtil.isEmpty(lotteryActivityIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<LotteryActivityGetDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LotteryActivityGetDO::getLotteryActivityId, lotteryActivityIdList);

        return this.list(wrapper).stream().collect(Collectors.groupingBy(LotteryActivityGetDO::getLotteryActivityId, Collectors.summingInt(LotteryActivityGetDO::getGetTimes)));
    }

    @Override
    public Integer countByUidAndGetType(QueryLotteryActivityGetCountRequest request) {
        LambdaQueryWrapper<LotteryActivityGetDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getUid())) {
            wrapper.eq(LotteryActivityGetDO::getUid, request.getUid());
        }
        if (Objects.nonNull(request.getLotteryActivityId())) {
            wrapper.eq(LotteryActivityGetDO::getLotteryActivityId, request.getLotteryActivityId());
        }
        if (Objects.nonNull(request.getGetType())) {
            wrapper.eq(LotteryActivityGetDO::getGetType, request.getGetType());
        }
        wrapper.eq(LotteryActivityGetDO::getPlatformType, request.getPlatformType());
        if (request.getIsToday()) {
            Date now = new Date();
            wrapper.ge(LotteryActivityGetDO::getCreateTime, DateUtil.beginOfDay(now));
            wrapper.le(LotteryActivityGetDO::getCreateTime, now);
        }
        return this.list(wrapper).stream().mapToInt(LotteryActivityGetDO::getGetTimes).sum();
    }
}
