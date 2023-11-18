package com.yiling.admin.hmc.welfare.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.admin.hmc.welfare.form.DrugWelfareStatisticsPageForm;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareCouponStatisticsVO;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareStatisticsPageVO;
import com.yiling.admin.hmc.welfare.vo.SellerUserVO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupCouponApi;
import com.yiling.hmc.welfare.api.DrugWelfareVerificationApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/28
 */
@Api(tags = "用药福利计划统计接口")
@RestController
@RequestMapping("/drug/welfare/statistics")
public class DrugWelfareStatisticsController {

    @DubboReference
    DrugWelfareApi drugWelfareApi;

    @DubboReference
    DrugWelfareGroupApi drugWelfareGroupApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    DrugWelfareGroupCouponApi drugWelfareGroupCouponApi;

    @DubboReference
    DrugWelfareVerificationApi drugWelfareVerificationApi;

    @ApiOperation(value = "用药福利计划统计列表")
    @PostMapping("/queryPage")
    public Result<Page<DrugWelfareStatisticsPageVO>> queryPage(@RequestBody DrugWelfareStatisticsPageForm form) {
        DrugWelfareStatisticsPageRequest request = PojoUtils.map(form, DrugWelfareStatisticsPageRequest.class);
        Page<DrugWelfareStatisticsPageDTO> pageDTO = drugWelfareGroupApi.statisticsPage(request);
        Page<DrugWelfareStatisticsPageVO> voPage = PojoUtils.map(pageDTO, DrugWelfareStatisticsPageVO.class);
        if (voPage.getTotal() == 0) {
            return Result.success(voPage);
        }
        List<DrugWelfareStatisticsPageVO> records = voPage.getRecords();
        //用户信息
        List<Long> userIdList = records.stream().map(e -> e.getUserId()).distinct().collect(Collectors.toList());
        List<HmcUser> hmcUserList = hmcUserApi.listByIds(userIdList);
        Map<Long, HmcUser> hmcUserMap = hmcUserList.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));
        //福利计划
        List<Long> drugWelfareIdList = records.stream().map(e -> e.getDrugWelfareId()).distinct().collect(Collectors.toList());
        List<DrugWelfareDTO> drugWelfareDTOS = drugWelfareApi.getByIdList(drugWelfareIdList);
        Map<Long, DrugWelfareDTO> drugWelfareMap = drugWelfareDTOS.stream().collect(Collectors.toMap(DrugWelfareDTO::getId, Function.identity()));
        //商家信息
        List<Long> eidList = records.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseDTOS.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        //商家销售人员
        List<Long> sellerUserIdList = records.stream().map(e -> e.getSellerUserId()).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> sellerUserMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        records.forEach(e -> {
            if (Objects.nonNull(hmcUserMap.get(e.getUserId())) && StringUtils.isNotBlank(hmcUserMap.get(e.getUserId()).getNickName())){
                e.setNickName(hmcUserMap.get(e.getUserId()).getNickName());
            }
            if (Objects.nonNull(hmcUserMap.get(e.getUserId())) && StringUtils.isNotBlank(hmcUserMap.get(e.getUserId()).getAvatarUrl())){
                e.setAvatarUrl(hmcUserMap.get(e.getUserId()).getAvatarUrl());
            }

            if (Objects.nonNull(hmcUserMap.get(e.getUserId())) && Objects.nonNull(hmcUserMap.get(e.getUserId()).getGender())){
                e.setGender(hmcUserMap.get(e.getUserId()).getGender());
            }

            if (Objects.nonNull(hmcUserMap.get(e.getUserId())) && StringUtils.isNotBlank(hmcUserMap.get(e.getUserId()).getMobile())){
                e.setMobile(hmcUserMap.get(e.getUserId()).getMobile());
            }
            e.setDrugWelfareName(drugWelfareMap.get(e.getDrugWelfareId()).getName());
            e.setEname(enterpriseMap.get(e.getEid()).getName());
            if (Objects.nonNull(sellerUserMap.get(e.getSellerUserId()))) {
                e.setSellerUserName(sellerUserMap.get(e.getSellerUserId()).getName());
            } else {
                e.setSellerUserName("无");
            }
            //使用进度
            List<DrugWelfareGroupCouponDTO> groupCouponList = drugWelfareGroupCouponApi.getWelfareGroupCouponByGroupId(e.getId());
            Long usedCount = groupCouponList.stream().filter(record -> record.getCouponStatus().equals(DrugWelfareCouponStatusEnum.USED.getCode())).count();
            StringBuffer useSchedule = new StringBuffer();
            useSchedule.append(usedCount);
            useSchedule.append("/");
            useSchedule.append(groupCouponList.size());
            e.setUseSchedule(useSchedule.toString());
            e.setBeginTime(drugWelfareMap.get(e.getDrugWelfareId()).getBeginTime());
            e.setEndTime(drugWelfareMap.get(e.getDrugWelfareId()).getEndTime());
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "商家销售人员下拉选单")
    @PostMapping("/querySellerUser")
    public Result<CollectionObject<SellerUserVO>> querySellerUser() {
        List<Long> ids = drugWelfareGroupApi.getSellerUserIds();
        List<UserDTO> userDTOS = Collections.EMPTY_LIST;
        if (CollectionUtils.isNotEmpty(ids)) {
            userDTOS = userApi.listByIds(ids);
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(userDTOS, SellerUserVO.class)));
    }


    @ApiOperation(value = "用药福利计划统计列表")
    @GetMapping("/queryCouponList")
    public Result<CollectionObject<DrugWelfareCouponStatisticsVO>> queryCouponList(@NotNull @ApiParam(value = "用户入组id（取列表中id字段的值）") @RequestParam(value = "groupId") Long groupId) {

        List<DrugWelfareGroupCouponDTO> dtoList = drugWelfareGroupCouponApi.getWelfareGroupCouponByGroupId(groupId);
        if (CollectionUtils.isEmpty(dtoList)) {
            return Result.success(new CollectionObject<>(Collections.EMPTY_LIST));
        }
        List<Long> groupCouponIds = dtoList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        //查询是谁核销的福利券
        Map<Long, DrugWelfareVerificationDTO> dtoMap = Maps.newHashMap();
        List<DrugWelfareVerificationDTO> list = drugWelfareVerificationApi.getDrugWelfareVerificationByGroupCouponIds(groupCouponIds);
        if (CollectionUtils.isNotEmpty(list)) {
            dtoMap = list.stream().collect(Collectors.toMap(DrugWelfareVerificationDTO::getDrugWelfareGroupCouponId, Function.identity()));
        }
        List<DrugWelfareCouponStatisticsVO> vos = PojoUtils.map(dtoList, DrugWelfareCouponStatisticsVO.class);
        for (DrugWelfareCouponStatisticsVO vo : vos) {
            if (Objects.nonNull(dtoMap.get(vo.getId()))) {
                vo.setVerificationName(userApi.getById(dtoMap.get(vo.getId()).getCreateUser()).getName());
            }
        }
        return Result.success(new CollectionObject<>(vos));
    }

}
