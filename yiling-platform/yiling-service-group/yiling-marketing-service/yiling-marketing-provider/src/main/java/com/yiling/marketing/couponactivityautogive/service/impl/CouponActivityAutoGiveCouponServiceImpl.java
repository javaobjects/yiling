package com.yiling.marketing.couponactivityautogive.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivityautogive.dao.CouponActivityAutoGiveCouponMapper;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveCouponRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveCouponDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveCouponService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * <p>
 * 自动发券活动关联优惠券活动表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class CouponActivityAutoGiveCouponServiceImpl extends BaseServiceImpl<CouponActivityAutoGiveCouponMapper, CouponActivityAutoGiveCouponDO>
                                                     implements CouponActivityAutoGiveCouponService {

    @Override
    public CouponActivityAutoGiveCouponDTO getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return null;
        }
        CouponActivityAutoGiveCouponDO one = this.baseMapper.getEffectiveAutoGiveCouponByByCouponActivityId(couponActivityId);
        return PojoUtils.map(one, CouponActivityAutoGiveCouponDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveCouponDTO> getByCouponByCouponActivityIdList(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveCouponDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGiveCouponDO::getCouponActivityId, couponActivityIdList);
        List<CouponActivityAutoGiveCouponDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGiveCouponDTO.class);
    }

    @Override
    public List<CouponActivityAutoGiveCouponDO> getByCouponActivityAutoGiveId(Long couponActivityAutoGiveId) {
        if (ObjectUtil.isNull(couponActivityAutoGiveId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveCouponDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityAutoGiveCouponDO::getCouponActivityAutoGiveId, couponActivityAutoGiveId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<CouponActivityAutoGiveCouponDO> getAutoGiveCouponByAutoGiveIdList(List<Long> couponActivityAutoGiveIds) {
        if (ObjectUtil.isNull(couponActivityAutoGiveIds)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGiveCouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponActivityAutoGiveCouponDO::getCouponActivityAutoGiveId, couponActivityAutoGiveIds);
        return this.list(queryWrapper);
    }

    @Override
    @GlobalTransactional
    public Boolean saveGiveCoupon(Long opUserId, Long autoGiveId, List<SaveCouponActivityAutoGiveCouponRequest> requestList) {
        if (ObjectUtil.isNull(autoGiveId) || CollUtil.isEmpty(requestList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Date date = new Date();
        // 待新增数据
        List<CouponActivityAutoGiveCouponDO> addList = new ArrayList<>();
        // 待更新数据
        List<CouponActivityAutoGiveCouponDO> updateList = new ArrayList<>();
        // 待删除数据
        List<Long> deleteIdList = new ArrayList<>();

        List<CouponActivityAutoGiveCouponDO> dbList = this.getByCouponActivityAutoGiveId(autoGiveId);
        if (CollUtil.isEmpty(dbList)) {
            for (SaveCouponActivityAutoGiveCouponRequest request : requestList) {
                buildAddAutoGiveCoupon(opUserId, autoGiveId, date, addList, request);
            }
        } else {
            Map<String, SaveCouponActivityAutoGiveCouponRequest> requestMap = requestList.stream()
                .collect(Collectors.toMap(r -> r.getCouponActivityAutoGiveId() + "_" + r.getCouponActivityId(), r -> r, (v1, v2) -> v1));
            Map<String, CouponActivityAutoGiveCouponDO> dbMap = dbList.stream()
                .collect(Collectors.toMap(r -> r.getCouponActivityAutoGiveId() + "_" + r.getCouponActivityId(), r -> r, (v1, v2) -> v1));
            // 待删除
            for (CouponActivityAutoGiveCouponDO db : dbList) {
                SaveCouponActivityAutoGiveCouponRequest request = requestMap.get(db.getCouponActivityAutoGiveId() + "_" + db.getCouponActivityId());
                if (ObjectUtil.isNull(request)) {
                    deleteIdList.add(db.getId());
                }
            }
            // 待更新、新增
            for (SaveCouponActivityAutoGiveCouponRequest request : requestList) {
                CouponActivityAutoGiveCouponDO db = dbMap.get(request.getCouponActivityAutoGiveId() + "_" + request.getCouponActivityId());
                if (ObjectUtil.isNull(db)) {
                    buildAddAutoGiveCoupon(opUserId, autoGiveId, date, addList, request);
                } else {
                    CouponActivityAutoGiveCouponDO newData = new CouponActivityAutoGiveCouponDO();
                    newData.setId(db.getId());
                    newData.setGiveNum(request.getGiveNum());
                    newData.setUpdateUser(opUserId);
                    newData.setUpdateTime(date);
                    updateList.add(newData);
                }
            }
        }

        if (CollUtil.isNotEmpty(addList)) {
            this.saveBatch(addList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        if (CollUtil.isNotEmpty(deleteIdList)) {
            LambdaQueryWrapper<CouponActivityAutoGiveCouponDO> queryWrapperDelete = new LambdaQueryWrapper<>();
            queryWrapperDelete.in(CouponActivityAutoGiveCouponDO::getId, deleteIdList);
            CouponActivityAutoGiveCouponDO entity = new CouponActivityAutoGiveCouponDO();
            entity.setUpdateUser(opUserId);
            entity.setUpdateTime(date);
            this.batchDeleteWithFill(entity, queryWrapperDelete);
        }
        return true;
    }

    private void buildAddAutoGiveCoupon(Long opUserId, Long autoGiveId, Date date, List<CouponActivityAutoGiveCouponDO> addList,
                                        SaveCouponActivityAutoGiveCouponRequest request) {
        CouponActivityAutoGiveCouponDO newData = PojoUtils.map(request, CouponActivityAutoGiveCouponDO.class);
        newData.setCouponActivityAutoGiveId(autoGiveId);
        newData.setCreateUser(opUserId);
        newData.setCreateTime(date);
        addList.add(newData);
    }
}
