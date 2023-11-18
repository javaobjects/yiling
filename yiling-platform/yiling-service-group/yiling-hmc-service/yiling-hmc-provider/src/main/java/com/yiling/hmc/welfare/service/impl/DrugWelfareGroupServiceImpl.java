package com.yiling.hmc.welfare.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.welfare.dao.DrugWelfareGroupMapper;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.dto.request.QueryGroupCouponRequest;
import com.yiling.hmc.welfare.dto.request.SaveGroupRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareCouponDO;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupCouponDO;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupDO;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.hmc.welfare.service.DrugWelfareCouponService;
import com.yiling.hmc.welfare.service.DrugWelfareGroupCouponService;
import com.yiling.hmc.welfare.service.DrugWelfareGroupService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * <p>
 * 药品福利入组表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareGroupServiceImpl extends BaseServiceImpl<DrugWelfareGroupMapper, DrugWelfareGroupDO> implements DrugWelfareGroupService {

    @Autowired
    private DrugWelfareCouponService couponService;

    @Autowired
    private DrugWelfareGroupCouponService groupCouponService;

    @Override
    public DrugWelfareGroupDO getWelfareGroupByWelfareIdAndUserId(Long welfareId, Long userId) {
        QueryWrapper<DrugWelfareGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareGroupDO::getDrugWelfareId, welfareId);
        queryWrapper.lambda().eq(DrugWelfareGroupDO::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<DrugWelfareGroupDO> getValidWelfareByUserId(Long userId) {
        QueryWrapper<DrugWelfareGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareGroupDO::getUserId, userId);
        return this.list(queryWrapper);
    }

    @Override
    public DrugWelfareGroupDO getValidWelfareByEidAndDrugWelfareId(Long eid, Long drugWelfareId) {
        QueryWrapper<DrugWelfareGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareGroupDO::getEid, eid).eq(DrugWelfareGroupDO::getDrugWelfareId, drugWelfareId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional
    public Long joinGroup(SaveGroupRequest request) {

        // 1、构建入组对象，保存入组
        DrugWelfareGroupDO groupDO = new DrugWelfareGroupDO();
        groupDO.setUserId(request.getOpUserId());
        groupDO.setDrugWelfareId(request.getWelfareId());
        groupDO.setEid(request.getEid());
        groupDO.setSellerUserId(request.getSellerUserId());
        groupDO.setMedicineUserName(request.getMedicineUserName());
        groupDO.setMedicineUserPhone(request.getMedicineUserPhone());
        groupDO.setJoinGroupId(getCurrentJoinGroupId());

        this.save(groupDO);

        // 生成券码
        List<String> couponCodeList = generateCouponCode();

        // 2、构建入组券包对象，保存
        List<DrugWelfareCouponDO> couponDOList = couponService.getByWelfareId(request.getWelfareId());
        List<DrugWelfareGroupCouponDO> groupCouponDOList = Lists.newArrayList();
        for (int i = 0; i < couponDOList.size(); i++) {
            DrugWelfareGroupCouponDO groupCouponDO = new DrugWelfareGroupCouponDO();
            groupCouponDO.setGroupId(groupDO.getId());
            groupCouponDO.setDrugWelfareId(request.getWelfareId());
            groupCouponDO.setDrugWelfareCouponId(couponDOList.get(i).getId());
            groupCouponDO.setCouponCode(couponCodeList.get(i));
            groupCouponDO.setCouponStatus(DrugWelfareCouponStatusEnum.TO_ACTIVE.getCode());
            groupCouponDO.setGiveNumber(couponDOList.get(i).getGiveNumber());
            groupCouponDO.setRequirementNumber(couponDOList.get(i).getRequirementNumber());
            groupCouponDO.setCouponId(couponDOList.get(i).getCouponId());
            groupCouponDOList.add(groupCouponDO);

        }
        // 设置第一个为已激活
        groupCouponDOList.get(0).setCouponStatus(DrugWelfareCouponStatusEnum.ACTIVATED.getCode());
        groupCouponDOList.get(0).setActiveTime(DateUtil.date());

        groupCouponService.saveBatch(groupCouponDOList);

        return groupDO.getId();
    }

    /**
     * 获取当前入组记录id最大值
     *
     * @return
     */
    private Long getCurrentJoinGroupId() {
        LambdaQueryWrapper<DrugWelfareGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(DrugWelfareGroupDO::getJoinGroupId);
        queryWrapper.last(" limit 1");
        DrugWelfareGroupDO welfareGroupDO = this.getOne(queryWrapper);
        if (Objects.isNull(welfareGroupDO)) {
            return 456001L;
        }
        return welfareGroupDO.getJoinGroupId() + 1;
    }

    /**
     * 生成6个不重复的券码
     *
     * @return
     */
    private List<String> generateCouponCode() {
        List<String> result = Lists.newArrayList();
        String code = RandomUtil.randomNumbers(12);
        DrugWelfareGroupCouponDO groupCouponDO = groupCouponService.getGroupCouponByCode(code);
        while (Objects.isNull(groupCouponDO)) {
            result.add(code);
            code = RandomUtil.randomNumbers(12);
            groupCouponDO = groupCouponService.getGroupCouponByCode(code);
            if (result.size() > 5) {
                break;
            }
        }
        return result;
    }

    @Override
    public Page<DrugWelfareGroupDO> statisticsPage(DrugWelfareStatisticsPageRequest request) {
        LambdaQueryWrapper<DrugWelfareGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(request.getUserId()), DrugWelfareGroupDO::getUserId, request.getUserId());
        queryWrapper.eq(Objects.nonNull(request.getDrugWelfareId()), DrugWelfareGroupDO::getDrugWelfareId, request.getDrugWelfareId());
        queryWrapper.eq(Objects.nonNull(request.getEid()), DrugWelfareGroupDO::getEid, request.getEid());
        queryWrapper.eq(Objects.nonNull(request.getSellerUserId()), DrugWelfareGroupDO::getSellerUserId, request.getSellerUserId());
        queryWrapper.like(StringUtils.isNotBlank(request.getMedicineUserName()), DrugWelfareGroupDO::getMedicineUserName, request.getMedicineUserName());
        queryWrapper.eq(StringUtils.isNotBlank(request.getMedicineUserPhone()), DrugWelfareGroupDO::getMedicineUserPhone, request.getMedicineUserPhone());
        queryWrapper.eq(Objects.nonNull(request.getJoinGroupId()), DrugWelfareGroupDO::getJoinGroupId, request.getJoinGroupId());
        if (Objects.nonNull(request.getStartTime())){
            queryWrapper.ge(DrugWelfareGroupDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
        }
        if (Objects.nonNull(request.getEndTime())){
            queryWrapper.le(DrugWelfareGroupDO::getCreateTime, DateUtil.endOfDay(request.getEndTime()));
        }
        queryWrapper.orderByDesc(DrugWelfareGroupDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
    }

    @Override
    public List<Long> getSellerUserIds() {
        QueryWrapper<DrugWelfareGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT seller_user_id");
        List<DrugWelfareGroupDO> list = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(e -> e.getSellerUserId()).distinct().collect(Collectors.toList());
            return ids;
        } else {
            return null;
        }
    }

    @Override
    public List<DrugWelfareGroupDO> listDrugWelfareGroup(DrugWelfareGroupListRequest request) {
        LambdaQueryWrapper<DrugWelfareGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(request.getDrugWelfareId()), DrugWelfareGroupDO::getDrugWelfareId, request.getDrugWelfareId())
                .eq(Objects.nonNull(request.getEid()), DrugWelfareGroupDO::getEid, request.getEid())
                .orderByDesc(DrugWelfareGroupDO::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Integer queryVerifyStatus(QueryGroupCouponRequest request) {
        DrugWelfareGroupCouponDO groupCoupon = groupCouponService.getGroupCouponByCode(request.getCouponCode());
        if (Objects.isNull(groupCoupon)) {
            return 1;
        }
        List<DrugWelfareGroupCouponDO> welfareGroupCoupon = groupCouponService.getWelfareGroupCouponByGroupId(groupCoupon.getGroupId());
        if (CollUtil.isEmpty(welfareGroupCoupon)) {
            return 1;
        }
        boolean checkResult = welfareGroupCoupon.stream().allMatch(item -> item.getCouponStatus().equals(DrugWelfareCouponStatusEnum.USED.getCode()));
        if (checkResult) {
            return 3;
        }
        if (groupCoupon.getCouponStatus().equals(DrugWelfareCouponStatusEnum.USED.getCode())) {
            return 2;
        }
        return 1;
    }
}
