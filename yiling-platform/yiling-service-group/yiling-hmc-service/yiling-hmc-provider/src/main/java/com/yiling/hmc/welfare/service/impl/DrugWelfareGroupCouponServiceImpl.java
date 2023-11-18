package com.yiling.hmc.welfare.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupApi;
import com.yiling.hmc.welfare.api.DrugWelfareVerificationApi;
import com.yiling.hmc.welfare.dao.DrugWelfareGroupCouponMapper;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponVerificationDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponSaveRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponVerificationRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareGroupCouponDO;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.hmc.welfare.enums.DrugWelfareGroupCouponVerificationStatusEnum;
import com.yiling.hmc.welfare.enums.DrugWelfareStatusEnum;
import com.yiling.hmc.welfare.service.DrugWelfareGroupCouponService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 入组福利券表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareGroupCouponServiceImpl extends BaseServiceImpl<DrugWelfareGroupCouponMapper, DrugWelfareGroupCouponDO> implements DrugWelfareGroupCouponService {


    @DubboReference
    DrugWelfareApi drugWelfareApi;

    @DubboReference
    DrugWelfareVerificationApi drugWelfareVerificationApi;

    @DubboReference
    DrugWelfareGroupApi drugWelfareGroupApi;



    @Override
    public List<DrugWelfareGroupCouponDO> getWelfareGroupCouponByGroupId(Long groupId) {
        QueryWrapper<DrugWelfareGroupCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareGroupCouponDO::getGroupId, groupId);
        return this.list(queryWrapper);
    }

    @Override
    public List<DrugWelfareGroupCouponDO> getWelfareGroupCouponByGroupIdList(List<Long> groupIdList) {
        QueryWrapper<DrugWelfareGroupCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(DrugWelfareGroupCouponDO::getGroupId, groupIdList);
        return this.list(queryWrapper);
    }

    @Override
    public Page<DrugWelfareGroupCouponDO> listDrugWelfareGroupCoupon(DrugWelfareGroupCouponListRequest request) {
        QueryWrapper<DrugWelfareGroupCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Objects.nonNull(request.getIdList()), DrugWelfareGroupCouponDO::getGroupId, request.getIdList())
                .eq(Objects.nonNull(request.getCouponStatus()), DrugWelfareGroupCouponDO::getCouponStatus, request.getCouponStatus())
                .orderByDesc(DrugWelfareGroupCouponDO::getVerifyTime);
        if (Objects.nonNull(request.getStartTime())) {
            queryWrapper.lambda().ge(DrugWelfareGroupCouponDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
        }
        if (Objects.nonNull(request.getEndTime())) {
            queryWrapper.lambda().le(DrugWelfareGroupCouponDO::getCreateTime, DateUtil.endOfDay(request.getEndTime()));
        }
        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
    }

    @Override
    public DrugWelfareGroupCouponDO getGroupCouponByCode(String code) {
        QueryWrapper<DrugWelfareGroupCouponDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DrugWelfareGroupCouponDO::getCouponCode, code);
        queryWrapper.last(" limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<DrugWelfareGroupCouponStatisticsPageDTO> exportStatistics(DrugWelfareStatisticsPageRequest request) {
        Page<DrugWelfareGroupCouponStatisticsPageDTO> page = new Page<>(request.getCurrent(), request.getSize());
        if (Objects.nonNull(request.getStartTime())){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (Objects.nonNull(request.getEndTime())){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        return this.baseMapper.exportStatistics(page, request);
    }

    @Override
    public DrugWelfareGroupCouponVerificationDTO verificationDrugWelfareGroupCoupon(DrugWelfareGroupCouponVerificationRequest request) {
        DrugWelfareGroupCouponVerificationDTO dto = new DrugWelfareGroupCouponVerificationDTO();
        DrugWelfareGroupCouponDO groupCouponByCode = this.getGroupCouponByCode(request.getCouponCode());
        //校验优惠券状态
        if(Objects.isNull(groupCouponByCode)){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.ERROR.getCode());
            return dto;
        }
        //校验是否同一商家
        Long groupId = groupCouponByCode.getGroupId();
        DrugWelfareGroupDTO drugWelfareGroupDTO = drugWelfareGroupApi.getById(groupId);

        if(Objects.isNull(drugWelfareGroupDTO)){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.ERROR.getCode());
            return dto;
        }
        if(!request.getEid().equals(drugWelfareGroupDTO.getEid())){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.ERROR.getCode());
            return dto;
        }

        if(DrugWelfareCouponStatusEnum.USED.getCode().equals(groupCouponByCode.getCouponStatus())){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.VERIFICATION.getCode());
            return dto;
        }
        if(!DrugWelfareCouponStatusEnum.ACTIVATED.getCode().equals(groupCouponByCode.getCouponStatus())){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.ERROR.getCode());
            return dto;
        }

        //查询查询福利计划是否过期
        DrugWelfareDTO drugWelfareDTO = drugWelfareApi.queryById(groupCouponByCode.getDrugWelfareId());
        if(Objects.isNull(drugWelfareDTO)){
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.NON_EXIST.getCode());
            return dto;
        }
        if (DateUtil.compare(drugWelfareDTO.getBeginTime(), DateUtil.date().toJdkDate()) > 0) {
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.NOT_START.getCode());
            return dto;
        }

        if (DateUtil.compare(drugWelfareDTO.getEndTime(), DateUtil.date().toJdkDate()) < 0) {
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.EXPIRED.getCode());
            return dto;
        }
        if (drugWelfareDTO.getStatus().equals(DrugWelfareStatusEnum.INVALID.getCode())) {
            dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.ENDED.getCode());
            return dto;
        }
        //核销
        groupCouponByCode.setCouponStatus(DrugWelfareCouponStatusEnum.USED.getCode());
        Date date = new Date();
        groupCouponByCode.setVerifyTime(date);
        this.updateById(groupCouponByCode);

        //激活下一张券
        List<DrugWelfareGroupCouponDO> welfareGroupCouponByGroupId = this.getWelfareGroupCouponByGroupId(groupId);
        //找到未激活的下一张券
        Optional<DrugWelfareGroupCouponDO> first = welfareGroupCouponByGroupId.stream().filter(item -> DrugWelfareCouponStatusEnum.TO_ACTIVE.getCode().equals(item.getCouponStatus())).findFirst();
        if(first.isPresent()){
            DrugWelfareGroupCouponDO drugWelfareGroupCouponDO =first.get();
            drugWelfareGroupCouponDO.setCouponStatus(DrugWelfareCouponStatusEnum.ACTIVATED.getCode());
            drugWelfareGroupCouponDO.setActiveTime(DateUtil.date());
            this.updateById(drugWelfareGroupCouponDO);
        }

        //核销记录
        DrugWelfareGroupCouponSaveRequest saveRequest = new DrugWelfareGroupCouponSaveRequest();
        saveRequest.setDrugWelfareId(groupCouponByCode.getDrugWelfareId());
        saveRequest.setDrugWelfareGroupCouponId(groupCouponByCode.getId());
        saveRequest.setCouponId(groupCouponByCode.getCouponId());
        saveRequest.setVerificationTime(date);
        saveRequest.setOpUserId(request.getUserId());
        drugWelfareVerificationApi.saveVerification(saveRequest);

        dto.setStatus(DrugWelfareGroupCouponVerificationStatusEnum.SUCCESS.getCode());
        dto.setDrugWelfareName(drugWelfareDTO.getName());
        dto.setDrugWelfareId(drugWelfareDTO.getId());
        dto.setCouponId(groupCouponByCode.getCouponId());
        dto.setMedicineUserName(drugWelfareGroupDTO.getMedicineUserName());
        dto.setJoinGroupId(drugWelfareGroupDTO.getJoinGroupId());
        return dto;
    }
}
