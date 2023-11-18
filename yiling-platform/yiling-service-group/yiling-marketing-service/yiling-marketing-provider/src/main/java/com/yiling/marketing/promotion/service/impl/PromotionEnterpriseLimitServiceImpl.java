package com.yiling.marketing.promotion.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.promotion.dao.PromotionEnterpriseLimitMapper;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionEnterpriseLimitDO;
import com.yiling.marketing.promotion.service.PromotionEnterpriseLimitService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 促销活动企业限制表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class PromotionEnterpriseLimitServiceImpl extends BaseServiceImpl<PromotionEnterpriseLimitMapper, PromotionEnterpriseLimitDO> implements PromotionEnterpriseLimitService {

    @Override
    public List<PromotionEnterpriseLimitDO> queryByActivityId(Long promotionActivityId) {
        QueryWrapper<PromotionEnterpriseLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PromotionEnterpriseLimitDO::getPromotionActivityId, promotionActivityId);
        List<PromotionEnterpriseLimitDO> doList = this.list(wrapper);
        return doList;
    }

    @Override
    public boolean editEnterprise(List<PromotionEnterpriseLimitDO> enterpriseLimitDOS, Long promotionActivityId, Long opUserId, Date opTime) {
        List<PromotionEnterpriseLimitDO> promotionEnterpriseLimitDOS = this.queryByActivityId(promotionActivityId);
        if (CollUtil.isEmpty(promotionEnterpriseLimitDOS)) {
            // 新增
            enterpriseLimitDOS.forEach(e -> e.setPromotionActivityId(promotionActivityId).setUpdateTime(opTime).setUpdateUser(opUserId).setCreateTime(opTime).setCreateUser(opUserId));
            return this.saveBatch(enterpriseLimitDOS);
        }
        // 修改
        List<Long> newLongList = enterpriseLimitDOS.stream().map(PromotionEnterpriseLimitDO::getEid).collect(Collectors.toList());
        List<Long> oldLongList = promotionEnterpriseLimitDOS.stream().map(PromotionEnterpriseLimitDO::getEid).collect(Collectors.toList());

        List<Long> addLongList = newLongList.stream().filter(item -> !oldLongList.contains(item)).collect(Collectors.toList());
        List<Long> deleteLongList = oldLongList.stream().filter(item -> !newLongList.contains(item)).collect(Collectors.toList());


        Map<Long, PromotionEnterpriseLimitDO> newLimitDOMap = enterpriseLimitDOS.stream().collect(Collectors.toMap(PromotionEnterpriseLimitDO::getEid, o -> o, (k1, k2) -> k1));
        Map<Long, PromotionEnterpriseLimitDO> oldLimitDOMap = promotionEnterpriseLimitDOS.stream().collect(Collectors.toMap(PromotionEnterpriseLimitDO::getEid, o -> o, (k1, k2) -> k1));

        for (Long eid : addLongList) {
            // 新增
            PromotionEnterpriseLimitDO enterpriseLimitDO = newLimitDOMap.get(eid);
            PromotionEnterpriseLimitDO limitDO = new PromotionEnterpriseLimitDO();
            limitDO.setPromotionActivityId(promotionActivityId)
                    .setEid(enterpriseLimitDO.getEid())
                    .setEname(enterpriseLimitDO.getEname())
                    .setCreateUser(opUserId)
                    .setCreateTime(opTime)
                    .setUpdateUser(opUserId)
                    .setUpdateTime(opTime);
            this.save(limitDO);
        }
        for (Long eid : deleteLongList) {
            // 删除
            PromotionEnterpriseLimitDO enterpriseLimitDO = oldLimitDOMap.get(eid);
            enterpriseLimitDO.setUpdateUser(opUserId).setUpdateTime(opTime);
            this.deleteByIdWithFill(enterpriseLimitDO);
        }
        return true;
    }

    @Override
    public boolean copy(PromotionActivityStatusRequest request, Long promotionActivityId) {
        List<PromotionEnterpriseLimitDO> enterpriseLimitDOS = this.queryByActivityId(request.getId());
        if (enterpriseLimitDOS.size() < 1) {
            return true;
        }
        List<PromotionEnterpriseLimitDO> enterpriseLimitDOList = new ArrayList<>();
        for (PromotionEnterpriseLimitDO enterpriseLimitDO : enterpriseLimitDOS) {
            PromotionEnterpriseLimitDO limitDO = new PromotionEnterpriseLimitDO();
            limitDO.setPromotionActivityId(promotionActivityId)
                    .setEid(enterpriseLimitDO.getEid())
                    .setEname(enterpriseLimitDO.getEname())
                    .setCreateUser(request.getOpUserId())
                    .setCreateTime(request.getOpTime())
                    .setUpdateUser(request.getOpUserId())
                    .setUpdateTime(request.getOpTime());
            enterpriseLimitDOList.add(limitDO);
        }
        if (enterpriseLimitDOList.size() > 0) {
            return this.saveBatch(enterpriseLimitDOList);
        }
        return true;
    }
}
