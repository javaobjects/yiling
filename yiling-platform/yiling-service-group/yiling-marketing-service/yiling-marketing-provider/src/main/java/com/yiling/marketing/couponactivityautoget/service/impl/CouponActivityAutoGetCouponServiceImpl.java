package com.yiling.marketing.couponactivityautoget.service.impl;

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
import com.yiling.marketing.couponactivityautoget.dao.CouponActivityAutoGetCouponMapper;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetCouponRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 自主领券活动关联优惠券活动表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class CouponActivityAutoGetCouponServiceImpl extends BaseServiceImpl<CouponActivityAutoGetCouponMapper, CouponActivityAutoGetCouponDO>
                                                    implements CouponActivityAutoGetCouponService {

    @Override
    public CouponActivityAutoGetCouponDTO getByCouponActivityId(Long couponActivityId) {
        if (ObjectUtil.isNull(couponActivityId)) {
            return null;
        }
        CouponActivityAutoGetCouponDO one = this.baseMapper.getOneByCouponActivityId(couponActivityId);
        return PojoUtils.map(one, CouponActivityAutoGetCouponDTO.class);
    }

    @Override
    public List<CouponActivityAutoGetCouponDTO> getByCouponActivityIdList(List<Long> couponActivityIdList) {
        if (CollUtil.isEmpty(couponActivityIdList)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetCouponDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGetCouponDO::getCouponActivityId, couponActivityIdList);
        List<CouponActivityAutoGetCouponDO> list = this.list(lambdaQueryWrapper);
        return PojoUtils.map(list, CouponActivityAutoGetCouponDTO.class);
    }

    @Override
    public List<CouponActivityAutoGetCouponDO> getByCouponActivityGetId(Long couponActivityGetId) {
        if (ObjectUtil.isNull(couponActivityGetId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetCouponDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CouponActivityAutoGetCouponDO::getCouponActivityAutoGetId, couponActivityGetId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean saveGiveCoupon(Long opUserId, Long autoGiveId, List<SaveCouponActivityAutoGetCouponRequest> requestList) {
        if (ObjectUtil.isNull(autoGiveId) || CollUtil.isEmpty(requestList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Date date = new Date();
        // 待新增数据
        List<CouponActivityAutoGetCouponDO> addList = new ArrayList<>();
        // 待更新数据
        List<CouponActivityAutoGetCouponDO> updateList = new ArrayList<>();
        // 待删除数据
        List<Long> deleteIdList = new ArrayList<>();

        List<CouponActivityAutoGetCouponDO> dbList = this.getByCouponActivityGetId(autoGiveId);
        if (CollUtil.isEmpty(dbList)) {
            for (SaveCouponActivityAutoGetCouponRequest request : requestList) {
                buildAddAutoGiveCoupon(opUserId, autoGiveId, date, addList, request);
            }
        } else {
            Map<String, SaveCouponActivityAutoGetCouponRequest> requestMap = requestList.stream()
                .collect(Collectors.toMap(r -> r.getCouponActivityAutoGetId() + "_" + r.getCouponActivityId(), r -> r, (v1, v2) -> v1));
            Map<String, CouponActivityAutoGetCouponDO> dbMap = dbList.stream()
                .collect(Collectors.toMap(r -> r.getCouponActivityAutoGetId() + "_" + r.getCouponActivityId(), r -> r, (v1, v2) -> v1));
            // 待删除
            for (CouponActivityAutoGetCouponDO db : dbList) {
                SaveCouponActivityAutoGetCouponRequest request = requestMap.get(db.getCouponActivityAutoGetId() + "_" + db.getCouponActivityId());
                if (ObjectUtil.isNull(request)) {
                    deleteIdList.add(db.getId());
                }
            }
            // 待更新、新增
            for (SaveCouponActivityAutoGetCouponRequest request : requestList) {
                CouponActivityAutoGetCouponDO db = dbMap.get(request.getCouponActivityAutoGetId() + "_" + request.getCouponActivityId());
                if (ObjectUtil.isNull(db)) {
                    buildAddAutoGiveCoupon(opUserId, autoGiveId, date, addList, request);
                } else {
                    CouponActivityAutoGetCouponDO newData = new CouponActivityAutoGetCouponDO();
                    newData.setId(db.getId());
                    newData.setGiveNum(request.getGiveNum());
                    newData.setGiveNumDaily(request.getGiveNumDaily());
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
            LambdaQueryWrapper<CouponActivityAutoGetCouponDO> queryWrapperDelete = new LambdaQueryWrapper<>();
            queryWrapperDelete.in(CouponActivityAutoGetCouponDO::getId, deleteIdList);
            CouponActivityAutoGetCouponDO entity = new CouponActivityAutoGetCouponDO();
            entity.setUpdateUser(opUserId);
            entity.setUpdateTime(date);
            this.batchDeleteWithFill(entity, queryWrapperDelete);
        }
        return true;
    }

    @Override
    public List<CouponActivityAutoGetCouponDO> getByCouponActivityGetIdList(List<Long> couponActivityGetIds) {
        if (CollUtil.isEmpty(couponActivityGetIds)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CouponActivityAutoGetCouponDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(CouponActivityAutoGetCouponDO::getCouponActivityAutoGetId, couponActivityGetIds);
        return this.list(lambdaQueryWrapper);
    }

    private void buildAddAutoGiveCoupon(Long opUserId, Long autoGiveId, Date date, List<CouponActivityAutoGetCouponDO> addList,
                                        SaveCouponActivityAutoGetCouponRequest request) {
        CouponActivityAutoGetCouponDO newData = PojoUtils.map(request, CouponActivityAutoGetCouponDO.class);
        newData.setCouponActivityAutoGetId(autoGiveId);
        newData.setCreateUser(opUserId);
        newData.setCreateTime(date);
        addList.add(newData);
    }
}
