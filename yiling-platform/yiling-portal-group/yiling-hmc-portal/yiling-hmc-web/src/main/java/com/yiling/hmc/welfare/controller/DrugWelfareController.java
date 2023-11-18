package com.yiling.hmc.welfare.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.hmc.welfare.api.*;
import com.yiling.hmc.welfare.dto.*;
import com.yiling.hmc.welfare.dto.request.QueryGroupCouponRequest;
import com.yiling.hmc.welfare.dto.request.SaveGroupRequest;
import com.yiling.hmc.welfare.enums.CheckDrugWelfareStatusEnum;
import com.yiling.hmc.welfare.enums.DrugWelfareCouponStatusEnum;
import com.yiling.hmc.welfare.enums.DrugWelfareStatusEnum;
import com.yiling.hmc.welfare.form.QueryDrugWelfareForm;
import com.yiling.hmc.welfare.form.QueryVerifyStatusForm;
import com.yiling.hmc.welfare.form.QueryWelfareGroupDetailForm;
import com.yiling.hmc.welfare.form.SaveGroupForm;
import com.yiling.hmc.welfare.vo.*;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static cn.binarywang.wx.miniapp.constant.WxMaConstants.DEFAULT_ENV_VERSION;

/**
 * 药品福利控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/30
 */
@Slf4j
@RestController
@RequestMapping("/drugWelfare")
@Api(tags = "药品福利控制器")
public class DrugWelfareController {

    @DubboReference
    DrugWelfareApi welfareApi;

    @DubboReference
    DrugWelfareCouponApi welfareCouponApi;

    @DubboReference
    DrugWelfareEnterpriseApi welfareEnterpriseApi;

    @DubboReference
    DrugWelfareGroupApi groupApi;

    @DubboReference
    DrugWelfareGroupCouponApi groupCouponApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StandardGoodsSpecificationApi specificationApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    FileService fileService;


    @ApiOperation(value = "01、校验是否入组过")
    @PostMapping("/hasJoinGroup")
    @Log(title = "校验是否入组过", businessType = BusinessTypeEnum.OTHER)
    public Result<CheckDrugWelfareVO> hasJoinGroup(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryDrugWelfareForm form) {
        log.info("[hasJoinGroup]校验是否入组过参数：{}", JSONUtil.toJsonStr(form));

        form.convertQrCodeValue();

        List<DrugWelfareDTO> welfareList = welfareApi.getByIdList(Collections.singletonList(form.getWelfareId()));
        if (CollUtil.isEmpty(welfareList)) {
            return Result.failed("未获取到用药福利计划");
        }
        DrugWelfareDTO drugWelfareDTO = welfareList.get(0);
        CheckDrugWelfareVO checkDrugWelfareVO = PojoUtils.map(form, CheckDrugWelfareVO.class);
        checkDrugWelfareVO.setBeginTime(drugWelfareDTO.getBeginTime());
        checkDrugWelfareVO.setEndTime(drugWelfareDTO.getEndTime());

        // 1、判断用药福利计划状态 -> 状态校验不通过，直接返回
        if (DateUtil.compare(drugWelfareDTO.getBeginTime(), DateUtil.date().toJdkDate()) > 0) {
            checkDrugWelfareVO.setFlag(CheckDrugWelfareStatusEnum.NOT_START.getCode());
            return Result.success(checkDrugWelfareVO);
        }

        if (DateUtil.compare(drugWelfareDTO.getEndTime(), DateUtil.date().toJdkDate()) < 0) {
            checkDrugWelfareVO.setFlag(CheckDrugWelfareStatusEnum.EXPIRED.getCode());
            return Result.success(checkDrugWelfareVO);
        }

        if (drugWelfareDTO.getStatus().equals(DrugWelfareStatusEnum.INVALID.getCode())) {
            checkDrugWelfareVO.setFlag(CheckDrugWelfareStatusEnum.ENDED.getCode());
            return Result.success(checkDrugWelfareVO);
        }
        // 2、根据用户id获取入组记录 -> 没有入组，则跳转福利计划页
        List<DrugWelfareGroupDTO> welfareGroupDTOList = groupApi.getWelfareGroupByUserId(currentUser.getCurrentUserId());

        if (CollUtil.isEmpty(welfareGroupDTOList)) {
            // 未入组 -> 跳转福利计划页
            checkDrugWelfareVO.setRedirectFlag(1);
            return Result.success(checkDrugWelfareVO);
        }

        // 根据入组记录id -> 获取入组券包记录
        List<DrugWelfareGroupCouponDTO> welfareGroupCouponList = groupCouponApi.getWelfareGroupCouponByGroupIdList(welfareGroupDTOList.stream().map(DrugWelfareGroupDTO::getId).collect(Collectors.toList()));
        if (CollUtil.isEmpty(welfareGroupCouponList)) {
            // 无券包 -> 跳转福利计划页
            checkDrugWelfareVO.setRedirectFlag(1);
            return Result.success(checkDrugWelfareVO);
        }

//        Map<Long, DrugWelfareGroupDTO> welfareGroupDTOMap = welfareGroupDTOList.stream().collect(Collectors.toMap(DrugWelfareGroupDTO::getId, o -> o, (k1, k2) -> k1));

        Map<Long, List<DrugWelfareGroupCouponDTO>> groupCouponList = welfareGroupCouponList.stream().collect(Collectors.groupingBy(DrugWelfareGroupCouponDTO::getGroupId));

        // 3、有药店id -> 扫码过来的
        if (Objects.nonNull(form.getSellerEId())) {

            for (DrugWelfareGroupDTO group : welfareGroupDTOList) {
                List<DrugWelfareGroupCouponDTO> couponList = groupCouponList.get(group.getId());
                boolean hasUnChecked = couponList.stream().anyMatch(item -> !DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus()));

                // 3.1、存在已入组的药店!=当前药店 && 券未核销完 ->  构造详情对象返回
                if (!group.getEid().equals(form.getSellerEId()) && hasUnChecked) {

                    // 获取药品福利计划
                    List<DrugWelfareDTO> drugWelfareDTOList = welfareApi.getByIdList(Collections.singletonList(group.getDrugWelfareId()));
                    if (CollUtil.isEmpty(drugWelfareDTOList)) {
                        return Result.success(checkDrugWelfareVO);
                    }
                    EnterpriseDTO enterpriseDTO = enterpriseApi.getById(group.getEid());
                    StandardGoodsSpecificationDTO specification = specificationApi.getStandardGoodsSpecification(drugWelfareDTOList.get(0).getSellSpecificationsId());
                    if (Objects.isNull(enterpriseDTO) || Objects.isNull(specification)) {
                        log.info("未获取到企业信息或者标准库信息，参数：eId-{},sellSpecificationsId-{}", group.getEid(), drugWelfareDTOList.get(0).getSellSpecificationsId());
                        return Result.success(checkDrugWelfareVO);
                    }
                    // 构造详情对象返回
                    GroupWelfareVO groupWelfareVO = new GroupWelfareVO();
                    groupWelfareVO.setMedicineUserName(group.getMedicineUserName());
                    groupWelfareVO.setMedicineUserPhone(group.getMedicineUserPhone());
                    groupWelfareVO.setEName(enterpriseDTO.getName());
                    groupWelfareVO.setDrugName(specification.getName() + "（" + (specification.getSellSpecifications()) + "）");
                    checkDrugWelfareVO.setOtherGroupWelfareVO(groupWelfareVO);
                    checkDrugWelfareVO.setRedirectFlag(0);
                    return Result.success(checkDrugWelfareVO);
                } else if (group.getEid().equals(form.getSellerEId()) && hasUnChecked) {
                    // 3.2、当前企业 && 未核销完 -> 入组详情页
                    checkDrugWelfareVO.setGroupId(group.getId());
                    checkDrugWelfareVO.setRedirectFlag(2);
                    return Result.success(checkDrugWelfareVO);
                } else {
                    // 3.3、跳转福利计划页
                    checkDrugWelfareVO.setRedirectFlag(1);
                }
            }

//            if (eIdList.stream().anyMatch(item -> !item.equals(form.getSellerEId())) && hasUnChecked) {
//
//                List<DrugWelfareGroupCouponDTO> unchecked = welfareGroupCouponList.stream().filter(item -> !DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus())).collect(Collectors.toList());
//
//                // 获取药品福利计划
//                List<DrugWelfareDTO> drugWelfareDTOList = welfareApi.getByIdList(Collections.singletonList(unchecked.get(0).getDrugWelfareId()));
//                if (CollUtil.isEmpty(drugWelfareDTOList)) {
//                    return Result.success(checkDrugWelfareVO);
//                }
//                DrugWelfareGroupDTO welfareGroup = welfareGroupDTOList.stream().filter(item -> item.getId().equals(unchecked.get(0).getGroupId())).collect(Collectors.toList()).get(0);
//                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(welfareGroup.getEid());
//                StandardGoodsSpecificationDTO specification = specificationApi.getStandardGoodsSpecification(drugWelfareDTOList.get(0).getSellSpecificationsId());
//                if (Objects.isNull(enterpriseDTO) || Objects.isNull(specification)) {
//                    log.info("未获取到企业信息或者标准库信息，参数：eId-{},sellSpecificationsId-{}", welfareGroup.getEid(), drugWelfareDTOList.get(0).getSellSpecificationsId());
//                    return Result.success(checkDrugWelfareVO);
//                }
//                // 构造详情对象返回
//                GroupWelfareVO groupWelfareVO = new GroupWelfareVO();
//                groupWelfareVO.setMedicineUserName(welfareGroup.getMedicineUserName());
//                groupWelfareVO.setMedicineUserPhone(welfareGroup.getMedicineUserPhone());
//                groupWelfareVO.setEName(enterpriseDTO.getName());
//                groupWelfareVO.setDrugName(specification.getName() + "（" + (specification.getSellSpecifications()) + "）");
//                checkDrugWelfareVO.setOtherGroupWelfareVO(groupWelfareVO);
//                return Result.success(checkDrugWelfareVO);
//            } else if (eIdList.stream().allMatch(item -> item.equals(form.getSellerEId())) && hasUnChecked) {
//                // 3.2、当前企业 && 未核销完 -> 入组详情页
//                List<DrugWelfareGroupCouponDTO> unchecked = welfareGroupCouponList.stream().filter(item -> !DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus())).collect(Collectors.toList());
//                DrugWelfareGroupDTO welfareGroup = welfareGroupDTOList.stream().filter(item -> item.getId().equals(unchecked.get(0).getGroupId())).collect(Collectors.toList()).get(0);
//                checkDrugWelfareVO.setGroupId(welfareGroup.getId());
//                checkDrugWelfareVO.setRedirectFlag(2);
//            } else {
//                // 3.3、跳转福利计划页
//                checkDrugWelfareVO.setRedirectFlag(1);
//            }
        } else {
            // 4、无药店id -> 从公众号菜单过来
            boolean hasUnChecked = welfareGroupCouponList.stream().anyMatch(item -> !DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus()));
            if (hasUnChecked) {
                // 4.1、券未核销完 ->  入组详情页
                List<DrugWelfareGroupCouponDTO> unchecked = welfareGroupCouponList.stream().filter(item -> !DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus())).collect(Collectors.toList());
                DrugWelfareGroupDTO welfareGroup = welfareGroupDTOList.stream().filter(item -> item.getId().equals(unchecked.get(0).getGroupId())).collect(Collectors.toList()).get(0);
                checkDrugWelfareVO.setGroupId(welfareGroup.getId());
                checkDrugWelfareVO.setRedirectFlag(2);
            } else {
                // 4.2、券已核销完 -> 跳转福利计划页
                checkDrugWelfareVO.setRedirectFlag(1);
            }
        }

        return Result.success(checkDrugWelfareVO);
    }

    @ApiOperation(value = "02、获取福利计划详情")
    @PostMapping("/getWelfareDetailById")
    @Log(title = "获取福利计划详情", businessType = BusinessTypeEnum.OTHER)
    public Result<DrugWelfareVO> getWelfareDetailById(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryDrugWelfareForm form) {

        log.info("[getWelfareDetailById]获取福利计划详情参数：{}", JSONUtil.toJsonStr(form));

        form.convertQrCodeValue();

        List<DrugWelfareDTO> welfareList = welfareApi.getByIdList(Collections.singletonList(form.getWelfareId()));
        if (CollUtil.isEmpty(welfareList)) {
            return Result.failed("未获取到用药福利计划");
        }

        DrugWelfareVO welfareVO = PojoUtils.map(welfareList.get(0), DrugWelfareVO.class);

        // 获取企业名称
        if (Objects.nonNull(form.getSellerEId()) && form.getSellerEId() > 0) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getSellerEId());
            welfareVO.setEName(enterpriseDTO.getName());
        }

        // 获取福利券包
        List<DrugWelfareCouponDTO> couponList = welfareCouponApi.getByWelfareId(welfareList.get(0).getId());
        if (CollUtil.isEmpty(couponList)) {
            return Result.failed("未获取到用药福利券包");
        }
        Long saveCount = couponList.stream().map(DrugWelfareCouponDTO::getGiveNumber).reduce(Long::sum).get();
        welfareVO.setCouponVOList(PojoUtils.map(couponList, DrugWelfareCouponVO.class));
        welfareVO.setSaveCount(saveCount);
        welfareVO.setSellerEId(form.getSellerEId());
        welfareVO.setSellerUserId(form.getSellerUserId());

        StandardGoodsSpecificationDTO specification = specificationApi.getStandardGoodsSpecification(welfareList.get(0).getSellSpecificationsId());
        welfareVO.setDrugName(specification.getName() + "（" + (specification.getSellSpecifications()) + "）");

        log.info("[getWelfareDetailById]获取福利计划详情返参：{}", JSONUtil.toJsonStr(welfareVO));
        return Result.success(welfareVO);
    }

    @ApiOperation(value = "03、获取福利计划承接药店")
    @PostMapping("/getWelfareShopList")
    @Log(title = "获取福利计划承接药店", businessType = BusinessTypeEnum.OTHER)
    public Result<List<DrugWelfareEnterpriseVO>> getWelfareShopList(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryDrugWelfareForm form) {
        log.info("[getWelfareShopList]获取福利计划承接药店：{}", JSONUtil.toJsonStr(form));
        List<DrugWelfareEnterpriseDTO> welfareEnterpriseList = welfareEnterpriseApi.getByWelfareId(form.getWelfareId());
        log.info("[getWelfareShopList]获取福利计划承接药店返参：{}", JSONUtil.toJsonStr(welfareEnterpriseList));
        return Result.success(PojoUtils.map(welfareEnterpriseList, DrugWelfareEnterpriseVO.class));
    }

    @ApiOperation(value = "04、入组")
    @PostMapping("/joinGroup")
    @Log(title = "入组", businessType = BusinessTypeEnum.OTHER)
    public Result<Long> joinGroup(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid SaveGroupForm form) {
        log.info("[joinGroup]入组参数：{}", JSONUtil.toJsonStr(form));
        SaveGroupRequest request = PojoUtils.map(form, SaveGroupRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        Long groupId = groupApi.joinGroup(request);
        return Result.success(groupId);
    }


    @ApiOperation(value = "05、查询入组详情")
    @PostMapping("/getWelfareGroupDetail")
    @Log(title = "查询福利券包详情", businessType = BusinessTypeEnum.OTHER)
    public Result<GroupWelfareVO> getWelfareGroupDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryWelfareGroupDetailForm form) {

        log.info("[getWelfareGroupDetail]获取入组详情参数：{}", JSONUtil.toJsonStr(form));

        form.convertQrCodeValue();

        DrugWelfareGroupDTO welfareGroup;
        List<DrugWelfareGroupCouponDTO> welfareGroupCoupon;
        if (Objects.nonNull(form.getGroupId()) && form.getGroupId() > 0) {

            // 1、如果参数入组id非空 && 大于0 -> 获取当前入组详情
            welfareGroup = groupApi.getById(form.getGroupId());
            if (Objects.isNull(welfareGroup)) {
                return Result.failed("根据入组id未获取到入组记录");
            }
            // 根据入组记录id -> 获取入组券包记录
            welfareGroupCoupon = groupCouponApi.getWelfareGroupCouponByGroupId(welfareGroup.getId());

        } else {
            // 2、从菜单点击进来发现有未核销的福利券包,根据福利id + 用户id -> 获取入组记录
            welfareGroup = groupApi.getWelfareGroupByWelfareIdAndUserId(form.getWelfareId(), currentUser.getCurrentUserId());
            if (Objects.isNull(welfareGroup)) {
                return Result.failed("未获取到入组记录");
            }
            // 根据入组记录id -> 获取入组券包记录
            welfareGroupCoupon = groupCouponApi.getWelfareGroupCouponByGroupId(welfareGroup.getId());
            if (CollUtil.isEmpty(welfareGroupCoupon)) {
                return Result.failed("未获取到福利券包记录");
            }
        }
        List<DrugWelfareDTO> drugWelfareDTOList = welfareApi.getByIdList(Collections.singletonList(welfareGroup.getDrugWelfareId()));
        if (CollUtil.isEmpty(drugWelfareDTOList)) {
            return Result.failed("根据药品福利计划id未获取到原计划");
        }

        // 3、获取企业信息、规格信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(welfareGroup.getEid());
        StandardGoodsSpecificationDTO specification = specificationApi.getStandardGoodsSpecification(drugWelfareDTOList.get(0).getSellSpecificationsId());
        if (Objects.isNull(enterpriseDTO) || Objects.isNull(specification)) {
            log.info("未获取到企业信息或者标准库信息，参数：eId-{},sellSpecificationsId-{}", welfareGroup.getEid(), drugWelfareDTOList.get(0).getSellSpecificationsId());
            return Result.failed("未获取到企业信息或者标准库信息");
        }
        // 4、构造详情对象返回
        GroupWelfareVO groupWelfareVO = new GroupWelfareVO();
        groupWelfareVO.setGroupId(welfareGroup.getId());
        groupWelfareVO.setJoinGroupId(welfareGroup.getJoinGroupId());
        groupWelfareVO.setEndTime(drugWelfareDTOList.get(0).getEndTime());
        groupWelfareVO.setName(drugWelfareDTOList.get(0).getName());
        groupWelfareVO.setMedicineUserName(welfareGroup.getMedicineUserName());
        groupWelfareVO.setMedicineUserPhone(welfareGroup.getMedicineUserPhone());
        groupWelfareVO.setEName(enterpriseDTO.getName());
        groupWelfareVO.setDrugName(specification.getName() + "（" + (specification.getSellSpecifications()) + "）");

        // 5、处理券包顺序
        Optional<DrugWelfareGroupCouponDTO> ACTIVATED = welfareGroupCoupon.stream().filter(item -> DrugWelfareCouponStatusEnum.ACTIVATED.getCode().equals(item.getCouponStatus())).findFirst();
        List<DrugWelfareGroupCouponDTO> TO_ACTIVE = welfareGroupCoupon.stream().filter(item -> DrugWelfareCouponStatusEnum.TO_ACTIVE.getCode().equals(item.getCouponStatus())).collect(Collectors.toList());
        List<DrugWelfareGroupCouponDTO> USED = welfareGroupCoupon.stream().filter(item -> DrugWelfareCouponStatusEnum.USED.getCode().equals(item.getCouponStatus())).collect(Collectors.toList());
        List<DrugWelfareGroupCouponDTO> ranked = Lists.newArrayList();
        ACTIVATED.ifPresent(ranked::add);
        ranked.addAll(TO_ACTIVE);
        ranked.addAll(USED);

        groupWelfareVO.setGroupCouponVOList(PojoUtils.map(ranked, GroupCouponVO.class));
        return Result.success(groupWelfareVO);
    }


    @ApiOperation(value = "06、查询核销状态")
    @PostMapping("/queryVerifyStatus")
    @Log(title = "查询核销状态", businessType = BusinessTypeEnum.OTHER)
    public Result<QueryCouponStatusVO> queryVerifyStatus(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryVerifyStatusForm form) {
        log.info("[queryVerifyStatus]入组参数：{}", JSONUtil.toJsonStr(form));
        QueryGroupCouponRequest request = PojoUtils.map(form, QueryGroupCouponRequest.class);
        Integer flag = groupApi.queryVerifyStatus(request);
        QueryCouponStatusVO couponStatusVO = new QueryCouponStatusVO();
        couponStatusVO.setFlag(flag);
        return Result.success(couponStatusVO);
    }

    @ApiOperation(value = "07、生成福利小程序码")
    @PostMapping("/genWelfareQr")
    @Log(title = "生成福利小程序码", businessType = BusinessTypeEnum.OTHER)
    public Result<FileInfo> genWelfareQr(Long id) throws Exception {
        log.info("[genWelfareQr]生成福利小程序码参数：{}", id);
        String page = "pagesSub/main/welfarePlan/index";
        File wxaCode = wxMaService.getQrcodeService().createWxaCodeUnlimit("qr=wId:1", page, false, WxMaConstants.MiniProgramState.TRIAL, 430, true, null, false);
        FileInfo file = fileService.upload(wxaCode, FileTypeEnum.CONTENT_COVER);
        return Result.success(file);
    }

}
