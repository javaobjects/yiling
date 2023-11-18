package com.yiling.hmc.admin.welfare.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.admin.welfare.form.DrugWelfareStatisticsPageForm;
import com.yiling.hmc.admin.welfare.form.DrugWelfareVerificationForm;
import com.yiling.hmc.admin.welfare.form.DrugWelfareVerificationPageForm;
import com.yiling.hmc.admin.welfare.vo.DrugWelfareCouponStatisticsVO;
import com.yiling.hmc.admin.welfare.vo.DrugWelfareCouponVerificationVO;
import com.yiling.hmc.admin.welfare.vo.DrugWelfareListVO;
import com.yiling.hmc.admin.welfare.vo.DrugWelfareStatisticsPageVO;
import com.yiling.hmc.admin.welfare.vo.SellerUserVO;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareEnterpriseApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupApi;
import com.yiling.hmc.welfare.api.DrugWelfareGroupCouponApi;
import com.yiling.hmc.welfare.api.DrugWelfareVerificationApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareEnterpriseDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupCouponVerificationDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareGroupDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareStatisticsPageDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponVerificationRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupListRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareStatisticsPageRequest;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.hmc.welfare.enums.DrugWelfareGroupCouponVerificationStatusEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 用药福利计划入组统计
 *
 * @author: benben.jia
 * @data: 2022/09/28
 */
@Api(tags = "用药福利计划入组统计接口")
@RestController
@Slf4j
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

//    @DubboReference
//    EnterpriseApi enterpriseApi;

    @DubboReference
    DrugWelfareGroupCouponApi drugWelfareGroupCouponApi;

    @DubboReference
    DrugWelfareEnterpriseApi drugWelfareEnterpriseApi;

    @DubboReference
    DrugWelfareVerificationApi drugWelfareVerificationApi;

    @DubboReference
    CouponActivityApi couponActivityApi;


    //    @ApiOperation(value = "列表查询商家下拉单选")
    //    @PostMapping("/queryEnterpriseList")
    //    public Result<CollectionObject<EnterpriseListVO>> queryEnterpriseList() {
    //        List<EnterpriseListDTO> list = drugWelfareEnterpriseApi.getEnterpriseList();
    //        return Result.success(new CollectionObject<>(PojoUtils.map(list, EnterpriseListVO.class)));
    //    }

    @ApiOperation(value = "列表查询福利计划下拉单选")
    @PostMapping("/queryDrugWelfareList")
    public Result<CollectionObject<DrugWelfareListVO>> queryDrugWelfareList(@CurrentUser CurrentStaffInfo staffInfo) {
        CollectionObject<DrugWelfareListVO> listCollectionObject = new CollectionObject<DrugWelfareListVO>(Collections.EMPTY_LIST);

        // 获取当前企业参与的药品福利计划
        List<DrugWelfareEnterpriseDTO> welfareEnterprise = drugWelfareEnterpriseApi.getByEid(staffInfo.getCurrentEid());
        if (CollUtil.isEmpty(welfareEnterprise)) {
            return Result.success(listCollectionObject);
        }
        List<Long> welfareIdList = welfareEnterprise.stream().map(DrugWelfareEnterpriseDTO::getDrugWelfareId).collect(Collectors.toList());
        if (CollUtil.isEmpty(welfareIdList)) {
            return Result.success(listCollectionObject);
        }
        List<DrugWelfareDTO> welfareDTOList = drugWelfareApi.getByIdList(welfareIdList);
        if (CollUtil.isEmpty(welfareDTOList)) {
            return Result.success(listCollectionObject);
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(welfareDTOList, DrugWelfareListVO.class)));
    }


    @ApiOperation(value = "商家销售人员下拉单选")
    @PostMapping("/querySellerUser")
    public Result<CollectionObject<SellerUserVO>> querySellerUser(@CurrentUser CurrentStaffInfo staffInfo) {
        DrugWelfareGroupListRequest request = new DrugWelfareGroupListRequest();
        request.setEid(staffInfo.getCurrentEid());
        List<DrugWelfareGroupDTO> drugWelfareGroupDTOList = drugWelfareGroupApi.listDrugWelfareGroup(request);
        List<UserDTO> userDTOS = Collections.EMPTY_LIST;
        if (CollectionUtils.isNotEmpty(drugWelfareGroupDTOList)) {
            List<Long> ids = drugWelfareGroupDTOList.stream().map(DrugWelfareGroupDTO::getSellerUserId).distinct().collect(Collectors.toList());
            userDTOS = userApi.listByIds(ids);
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(userDTOS, SellerUserVO.class)));
    }


    @ApiOperation(value = "入组统计列表接口")
    @PostMapping("/queryPage")
    public Result<Page<DrugWelfareStatisticsPageVO>> queryPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody DrugWelfareStatisticsPageForm form) {
        Long currentEid = staffInfo.getCurrentEid();
        DrugWelfareStatisticsPageRequest request = PojoUtils.map(form, DrugWelfareStatisticsPageRequest.class);
        request.setEid(currentEid);
        request.setJoinGroupId(form.getGroupId());
        Page<DrugWelfareStatisticsPageDTO> pageDTO = drugWelfareGroupApi.statisticsPage(request);
        Page<DrugWelfareStatisticsPageVO> voPage = PojoUtils.map(pageDTO, DrugWelfareStatisticsPageVO.class);
        if (voPage.getTotal() == 0) {
            return Result.success(voPage);
        }
        List<DrugWelfareStatisticsPageVO> records = voPage.getRecords();
//        //用户信息
//        List<Long> userIdList = records.stream().map(e -> e.getUserId()).distinct().collect(Collectors.toList());
//        List<HmcUser> hmcUserList = hmcUserApi.listByIds(userIdList);
//        Map<Long, HmcUser> hmcUserMap = hmcUserList.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));
        //福利计划
        List<Long> drugWelfareIdList = records.stream().map(e -> e.getDrugWelfareId()).distinct().collect(Collectors.toList());
        List<DrugWelfareDTO> drugWelfareDTOS = drugWelfareApi.getByIdList(drugWelfareIdList);
        Map<Long, DrugWelfareDTO> drugWelfareMap = drugWelfareDTOS.stream().collect(Collectors.toMap(DrugWelfareDTO::getId, Function.identity()));
        //商家信息
//        List<Long> eidList = records.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
//        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(eidList);
//        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseDTOS.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        //商家销售人员
        List<Long> sellerUserIdList = records.stream().map(e -> e.getSellerUserId()).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> sellerUserMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        records.forEach(e -> {
//            e.setNickName(hmcUserMap.get(e.getUserId()).getNickName());
//            e.setAvatarUrl(hmcUserMap.get(e.getUserId()).getAvatarUrl());
//            e.setGender(hmcUserMap.get(e.getUserId()).getGender());
//            e.setMobile(hmcUserMap.get(e.getUserId()).getMobile());
            e.setDrugWelfareName(drugWelfareMap.get(e.getDrugWelfareId()).getName());
//            e.setEname(enterpriseMap.get(e.getEid()).getName());
            UserDTO userDTO = sellerUserMap.get(e.getSellerUserId());
            if(Objects.nonNull(userDTO)){
                e.setSellerUserName(userDTO.getName());
            }
            //使用进度
            List<DrugWelfareGroupCouponDTO> groupCouponList = drugWelfareGroupCouponApi.getWelfareGroupCouponByGroupId(e.getId());
            Long usedCount = groupCouponList.stream().filter(record -> record.getCouponStatus().equals(DrugWelfareCouponStatusEnum.USED.getCode())).count();
            StringBuffer useSchedule = new StringBuffer();
            useSchedule.append(usedCount);
            useSchedule.append("/");
            useSchedule.append(groupCouponList.size());
            e.setUseSchedule(useSchedule.toString());
//            StringBuffer validTime = new StringBuffer();
//            validTime.append(DateUtil.format(drugWelfareMap.get(e.getDrugWelfareId()).getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
//            validTime.append(" 至 ");
//            validTime.append(DateUtil.format(drugWelfareMap.get(e.getDrugWelfareId()).getEndTime(), "yyyy-MM-dd HH:mm:ss"));
//            e.setValidTime(validTime.toString());
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "福利券使用统计列表")
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
            String couponCode = vo.getCouponCode();
            vo.setCouponCode(StrUtil.hide(couponCode, 3, couponCode.length()-3));
        }
        return Result.success(new CollectionObject<>(vos));
    }


    @ApiOperation(value = "核销记录")
    @PostMapping("/queryVerificationList")
    public Result<Page<DrugWelfareCouponStatisticsVO>> queryCouponList(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody DrugWelfareVerificationPageForm form) {
        DrugWelfareGroupListRequest request = new DrugWelfareGroupListRequest();
        request.setDrugWelfareId(form.getDrugWelfareId());
        request.setEid(staffInfo.getCurrentEid());
        List<DrugWelfareGroupDTO> drugWelfareGroupDTOList = drugWelfareGroupApi.listDrugWelfareGroup(request);
        if (CollectionUtils.isEmpty(drugWelfareGroupDTOList)) {
            return Result.success(form.getPage());
        }
        List<Long> groupIdList = drugWelfareGroupDTOList.stream().map(DrugWelfareGroupDTO::getId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIdList)) {
            return Result.success(form.getPage());
        }

        DrugWelfareGroupCouponListRequest groupCouponListRequest = PojoUtils.map(form, DrugWelfareGroupCouponListRequest.class);
        groupCouponListRequest.setCouponStatus(DrugWelfareCouponStatusEnum.USED.getCode());
        groupCouponListRequest.setIdList(groupIdList);
        groupCouponListRequest.setStartTime(form.getStartTime());
        groupCouponListRequest.setEndTime(form.getEndTime());
        Page<DrugWelfareGroupCouponDTO> dtoPage = drugWelfareGroupCouponApi.listDrugWelfareGroupCoupon(groupCouponListRequest);
        List<DrugWelfareGroupCouponDTO> dtoList = dtoPage.getRecords();
        if (CollectionUtils.isEmpty(dtoList)) {
            return Result.success(form.getPage());
        }

        List<Long> groupCouponIds = dtoList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        //查询是谁核销的福利券
        Map<Long, DrugWelfareVerificationDTO> dtoMap = Maps.newHashMap();
        List<DrugWelfareVerificationDTO> list = drugWelfareVerificationApi.getDrugWelfareVerificationByGroupCouponIds(groupCouponIds);
        if (CollectionUtils.isNotEmpty(list)) {
            dtoMap = list.stream().collect(Collectors.toMap(DrugWelfareVerificationDTO::getDrugWelfareGroupCouponId, Function.identity()));
        }

        //查询福利计划
        List<Long> drugWelfareIdIds = dtoList.stream().map(DrugWelfareGroupCouponDTO::getDrugWelfareId).distinct().collect(Collectors.toList());
        Map<Long, DrugWelfareDTO> drugWelfareMap = Maps.newHashMap();
        List<DrugWelfareDTO> drugWelfareList = drugWelfareApi.getByIdList(drugWelfareIdIds);
        if (CollectionUtils.isNotEmpty(drugWelfareList)) {
            drugWelfareMap = drugWelfareList.stream().collect(Collectors.toMap(DrugWelfareDTO::getId, Function.identity()));
        }

        Page<DrugWelfareCouponStatisticsVO> vos = PojoUtils.map(dtoPage, DrugWelfareCouponStatisticsVO.class);
        for (DrugWelfareCouponStatisticsVO vo : vos.getRecords()) {
            if (Objects.nonNull(dtoMap.get(vo.getId()))) {
                vo.setVerificationName(userApi.getById(dtoMap.get(vo.getId()).getCreateUser()).getName());
            }
            DrugWelfareDTO drugWelfareDTO = drugWelfareMap.get(vo.getDrugWelfareId());
            if(drugWelfareDTO!=null){

                vo.setDrugWelfareId(drugWelfareDTO.getId());
                vo.setDrugWelfareName(drugWelfareDTO.getName());
            }
        }
        return Result.success(vos);
    }


    @ApiOperation(value = "确定核销")
    @PostMapping("/verificationDrugWelfareGroupCoupon")
    public Result<DrugWelfareCouponVerificationVO> verificationDrugWelfareGroupCoupon(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody DrugWelfareVerificationForm form) {
        DrugWelfareGroupCouponVerificationRequest request = PojoUtils.map(form, DrugWelfareGroupCouponVerificationRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        DrugWelfareGroupCouponVerificationDTO dto = drugWelfareGroupCouponApi.verificationDrugWelfareGroupCoupon(request);

        //发b2b优惠券
        if (DrugWelfareGroupCouponVerificationStatusEnum.SUCCESS.getCode().equals(dto.getStatus())) {
            List<SaveCouponRequest> saveCouponRequestList = Lists.newArrayList();
            SaveCouponRequest saveCouponRequest = new SaveCouponRequest();
            saveCouponRequest.setCouponActivityId(dto.getCouponId());
            saveCouponRequest.setEid(staffInfo.getCurrentEid());
            saveCouponRequest.setGetType(CouponGetTypeEnum.C_SEND.getCode());
            saveCouponRequestList.add(saveCouponRequest);
            Boolean aBoolean = couponActivityApi.giveCoupon(saveCouponRequestList);
            log.debug("aBoolean------{}",aBoolean);
        }

        DrugWelfareCouponVerificationVO vo = PojoUtils.map(dto, DrugWelfareCouponVerificationVO.class);
        return Result.success(vo);
    }






}
