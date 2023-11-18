package com.yiling.admin.data.center.enterprise.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.data.center.enterprise.form.OpenPlatformForm;
import com.yiling.admin.data.center.enterprise.form.QueryEnterprisePageListForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseChannelForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseHmcTypeForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseStatusForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseTypeForm;
import com.yiling.admin.data.center.enterprise.form.UpdateManagerMobileForm;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseCertificateVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseDetailPageVO;
import com.yiling.admin.data.center.enterprise.vo.EnterprisePageListItemVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseStatisticsVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.ValidateUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.dto.request.UpdateGoodsStatusByEidRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.bo.EnterpriseStatisticsBO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.OpenPlatformRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseChannelRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseHmcTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseStatusRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseTypeRequest;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业模块 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@RestController
@RequestMapping("/enterprise")
@Api(tags = "企业模块接口")
@Slf4j
public class EnterpriseController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    LocationApi locationApi;
    @DubboReference
    ShopApi shopApi;
    @DubboReference(async = true)
    B2bGoodsApi b2bGoodsApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "企业数量统计")
    @GetMapping("/quantityStatistics")
    public Result<EnterpriseStatisticsVO> quantityStatistics() {
        EnterpriseStatisticsBO statisticsBO = enterpriseApi.quantityStatistics();
        return Result.success(PojoUtils.map(statisticsBO, EnterpriseStatisticsVO.class));
    }

    @ApiOperation(value = "获取企业分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterprisePageListItemVO>> pageList(@RequestBody QueryEnterprisePageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        if (Objects.nonNull(form.getErpSyncLevel())) {
            request.setErpSyncLevelList(ListUtil.toList(form.getErpSyncLevel()));
        }

        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        List<EnterpriseDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        List<Long> authUserIds = records.stream().map(EnterpriseDTO::getAuthUser).distinct().collect(Collectors.toList());
        List<Long> createUserIds = records.stream().map(EnterpriseDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIds = records.stream().map(EnterpriseDTO::getUpdateUser).distinct().collect(Collectors.toList());
        List<Long> userIds = CollUtil.union(authUserIds, createUserIds, updateUserIds).stream().distinct().collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIds);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));

        List<Long> eids = records.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        // 企业账号数统计
        Map<Long, Long> enterpriseAccountNumMap = employeeApi.countByEids(eids);
        // 企业开通平台数据
        List<EnterprisePlatformDTO> enterprisePlatformDTOList = enterpriseApi.getEnterprisePlatforms(eids);
        Map<Long, EnterprisePlatformDTO> enterprisePlatformDTOMap = enterprisePlatformDTOList.stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity()));
        // 企业标签信息
        Map<Long, List<EnterpriseTagDTO>> enterpriseTagDTOMap = enterpriseTagApi.listByEidList(eids);

        List<EnterprisePageListItemVO> list = Lists.newArrayList();
        records.forEach(e -> {
            EnterprisePageListItemVO item = new EnterprisePageListItemVO();
            EnterpriseVO enterpriseVO = PojoUtils.map(e, EnterpriseVO.class);
            enterpriseVO.setAuthUserName(userDTOMap.getOrDefault(e.getAuthUser(), new UserDTO()).getName());
            enterpriseVO.setCreateUserName(userDTOMap.getOrDefault(e.getCreateUser(), new UserDTO()).getName());
            enterpriseVO.setUpdateUserName(userDTOMap.getOrDefault(e.getUpdateUser(), new UserDTO()).getName());
            item.setEnterpriseInfo(enterpriseVO);

            // 获取账号数量
            item.setAccountNum(Convert.toLong(enterpriseAccountNumMap.get(e.getId()), 0L));
            // 是否开通商城、是否开通POP、是否开通销售助手
            EnterprisePlatformDTO enterprisePlatformDTO = enterprisePlatformDTOMap.get(e.getId());
            if (enterprisePlatformDTO == null) {
                enterprisePlatformDTO = EnterprisePlatformDTO.newEmptyInstance();
            }
            item.setPlatformInfo(PojoUtils.map(enterprisePlatformDTO, EnterprisePageListItemVO.PlatformInfo.class));
            // 企业标签信息
            List<EnterpriseTagDTO> enterpriseTagDTOList = enterpriseTagDTOMap.get(e.getId());
            if (CollUtil.isNotEmpty(enterpriseTagDTOList)) {
                item.setTagNames(enterpriseTagDTOList.stream().map(EnterpriseTagDTO::getName).distinct().collect(Collectors.toList()));
            }

            list.add(item);
        });

        Page<EnterprisePageListItemVO> pageVO = PojoUtils.map(page, EnterprisePageListItemVO.class);
        pageVO.setRecords(list);

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取企业详情")
    @GetMapping("/get")
    public Result<EnterpriseDetailPageVO> get(@ApiParam(value = "企业ID", required = true) @RequestParam("id") Long id) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(id);
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(enterpriseDTO.getType());

        EnterpriseDetailPageVO pageVO = new EnterpriseDetailPageVO();
        pageVO.setEnterpriseInfo(PojoUtils.map(enterpriseDTO, EnterpriseVO.class));

        // 企业销售区域（商业类型才有）
        if (enterpriseTypeEnum == EnterpriseTypeEnum.BUSINESS) {
            EnterpriseSalesAreaDTO enterpriseSalesAreaDTO = enterpriseApi.getEnterpriseSalesArea(id);
            if (enterpriseSalesAreaDTO != null) {
                pageVO.setSalesAreaDescription(enterpriseSalesAreaDTO.getDescription());
            }
        }

        // 归属企业列表
        List<EnterpriseDTO> subEnterpriseDTOList = enterpriseApi.listByParentId(id);
        pageVO.setSubEnterpriseList(PojoUtils.map(subEnterpriseDTOList, EnterpriseDetailPageVO.SubEnterpriseVO.class));

        // 获取企业账号数、是否开通商城、是否开通POP、是否开通销售助手
        EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(id);
        if (enterprisePlatformDTO == null) {
            enterprisePlatformDTO = EnterprisePlatformDTO.newEmptyInstance();
        }

        // 开通平台
        {
            EnterpriseDetailPageVO.PlatformInfo platformInfo = PojoUtils.map(enterprisePlatformDTO, EnterpriseDetailPageVO.PlatformInfo.class);
            // POP
            platformInfo.setPopVisiableFlag(1);
            platformInfo.setPopEnableFlag(platformInfo.getPopFlag() == 0 ? 1 : 0);
            // B2B
            platformInfo.setMallVisiableFlag(1);
            platformInfo.setMallEnableFlag(platformInfo.getMallFlag() == 0 ? 1 : 0);
            // 销售助手
            platformInfo.setSalesAssistVisiableFlag(1);
            platformInfo.setSalesAssistEnableFlag(platformInfo.getSalesAssistFlag() == 0 ? 1 : 0);
            // C端药+险
            platformInfo.setHmcVisiableFlag(1);
            platformInfo.setHmcEnableFlag(platformInfo.getHmcFlag() == 0 ? 1 : 0);

            pageVO.setPlatformInfo(platformInfo);
        }

        // 获取管理员账号
        List<Staff> staffList = employeeApi.listAdminsByEid(id);
        if (CollUtil.isNotEmpty(staffList)) {
            pageVO.setAdminAccount(staffList.get(0).getMobile());

            List<EnterpriseDetailPageVO.ManagerInfo> managerList = CollUtil.newArrayList();
            for (Staff staff : staffList) {
                EnterpriseDetailPageVO.ManagerInfo managerInfo = new EnterpriseDetailPageVO.ManagerInfo();
                managerInfo.setUserId(staff.getId());
                managerInfo.setName(staff.getName());
                managerInfo.setMobile(staff.getMobile());
                managerInfo.setStatus(staff.getStatus());
                managerList.add(managerInfo);
            }
            pageVO.setManagerList(managerList);
        }

        // 企业资质
        {
            // 企业类型对应的资质列表
            List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()).getCertificateTypeEnumList();
            // 已上传的企业资质列表
            List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(id);
            Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDTOMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));

            List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
            enterpriseCertificateTypeEnumList.forEach(e -> {
                EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
                enterpriseCertificateVO.setType(e.getCode());
                enterpriseCertificateVO.setName(e.getName());
                enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
                enterpriseCertificateVO.setRequired(e.getMustExist());

                EnterpriseCertificateDTO enterpriseCertificateDTO = enterpriseCertificateDTOMap.get(e.getCode());
                if (enterpriseCertificateDTO != null) {
                    enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
                    enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
                    enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
                    enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
                    enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
                }
                enterpriseCertificateVOList.add(enterpriseCertificateVO);
            });
            pageVO.setEnterpriseCertificateList(enterpriseCertificateVOList);
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "修改企业信息")
    @PostMapping("/update")
    @Log(title = "修改企业信息",businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> update(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseForm form) {
        boolean validateCodeResult = locationApi.validateCode(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
        if (!validateCodeResult) {
            return Result.failed("省市区编码错误");
        }

        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getId());
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        UpdateEnterpriseRequest request = PojoUtils.map(form, UpdateEnterpriseRequest.class);
        String[] locations = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
        request.setProvinceName(locations[0]);
        request.setCityName(locations[1]);
        request.setRegionName(locations[2]);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = enterpriseApi.update(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "更新企业状态")
    @PostMapping("/updateStatus")
    @Log(title = "更新企业状态",businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseStatusForm form) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getId());
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        EnterpriseStatusEnum statusEnum = EnterpriseStatusEnum.getByCode(form.getStatus());
        if (statusEnum == null) {
            return Result.validateFailed("status", "参数值错误");
        }

        UpdateEnterpriseStatusRequest request = PojoUtils.map(form, UpdateEnterpriseStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = enterpriseApi.updateStatus(request);

        //企业（店铺）停用需要下架商品
        if (EnterpriseStatusEnum.DISABLED == EnterpriseStatusEnum.getByCode(form.getStatus())) {
            ShopDTO shop = shopApi.getShop(form.getId());
            log.info("企业店铺停用，需要下架商品，企业ID：{}，店铺ID：{}",form.getId(),shop.getId());

            if (Objects.nonNull(shop.getId()) && shop.getId() != 0) {
                UpdateGoodsStatusByEidRequest eidRequest = new UpdateGoodsStatusByEidRequest();
                eidRequest.setEid(form.getId());
                eidRequest.setOpUserId(adminInfo.getCurrentUserId());
                b2bGoodsApi.updateB2bStatusByEid(eidRequest);
                log.info("企业店铺停用下架商品，店铺ID：{}，请求商品下架入参：{}",shop.getId(), JSONObject.toJSONString(eidRequest));
            }

        }

        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "更新企业类型")
    @PostMapping("/updateType")
    @Log(title = "更新企业类型", businessType = BusinessTypeEnum.UPDATE)
    public Result updateType(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseTypeForm form) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(form.getType());
        if (enterpriseTypeEnum == null) {
            return Result.failed("企业类型参数值错误");
        }

        UpdateEnterpriseTypeRequest request = new UpdateEnterpriseTypeRequest();
        request.setEid(form.getEid());
        request.setEnterpriseTypeEnum(enterpriseTypeEnum);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = enterpriseApi.updateType(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "更新企业渠道")
    @PostMapping("/updateChannel")
    @Log(title = "更新企业渠道", businessType = BusinessTypeEnum.UPDATE)
    public Result updateChannel(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseChannelForm form) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        EnterpriseChannelEnum enterpriseChannelEnum = EnterpriseChannelEnum.getByCode(form.getChannelId());
        if (enterpriseChannelEnum == null) {
            return Result.failed("企业渠道参数值错误");
        }

        UpdateEnterpriseChannelRequest request = new UpdateEnterpriseChannelRequest();
        request.setEid(form.getEid());
        request.setEnterpriseChannelEnum(enterpriseChannelEnum);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = enterpriseApi.updateChannel(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "更新企业HMC业务类型")
    @PostMapping("/updateHmcType")
    @Log(title = "更新企业HMC业务类型", businessType = BusinessTypeEnum.UPDATE)
    public Result updateHmcType(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseHmcTypeForm form) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
        if (enterpriseDTO == null) {
            return Result.failed(UserErrorCode.ENTERPRISE_NOT_EXISTS);
        }

        EnterpriseHmcTypeEnum enterpriseHmcTypeEnum = EnterpriseHmcTypeEnum.getByCode(form.getHmcType());
        if (enterpriseHmcTypeEnum == null) {
            return Result.failed("HMC业务类型参数值错误");
        }

        UpdateEnterpriseHmcTypeRequest request = new UpdateEnterpriseHmcTypeRequest();
        request.setEid(form.getEid());
        request.setEnterpriseHmcTypeEnum(enterpriseHmcTypeEnum);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = enterpriseApi.updateHmcType(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "企业开通平台")
    @PostMapping("/openPlatform")
    @Log(title = "企业开通平台", businessType = BusinessTypeEnum.INSERT)
    public Result openPlatform(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid OpenPlatformForm form) {
        // 开通平台
        List<PlatformEnum> platformEnumList = CollUtil.newArrayList();
        form.getPlatformCodeList().forEach(e -> {
            platformEnumList.add(PlatformEnum.getByCode(e));
        });

        Class[] groups;
        if (platformEnumList.contains(PlatformEnum.POP)) {
            groups = new Class[]{ OpenPlatformForm.OpenPopValidateGroup.class };
        } else if (platformEnumList.contains(PlatformEnum.HMC)) {
            groups = new Class[]{ OpenPlatformForm.OpenHmcValidateGroup.class };
        } else {
            groups = new Class[]{ OpenPlatformForm.OpenOtherValidateGroup.class };
        }
        String errorMessage = ValidateUtils.failFastValidate(form, groups);
        if (StrUtil.isNotEmpty(errorMessage)) {
            return Result.validateFailed(errorMessage);
        }

        OpenPlatformRequest request = new OpenPlatformRequest();
        request.setEid(form.getEid());
        request.setPlatformEnumList(platformEnumList);
        request.setEnterpriseChannelEnum(EnterpriseChannelEnum.getByCode(form.getChannelCode()));
        request.setEnterpriseHmcTypeEnum(EnterpriseHmcTypeEnum.getByCode(form.getHmcType()));
        request.setOpUserId(adminInfo.getCurrentUserId());

        boolean result = enterpriseApi.openPlatform(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "修改管理员账号")
    @PostMapping("/updateManagerAccount")
    @Log(title = "修改管理员账号", businessType = BusinessTypeEnum.UPDATE)
    public Result updateManagerMobile(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateManagerMobileForm form) {
        UpdateManagerMobileRequest request = PojoUtils.map(form, UpdateManagerMobileRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        boolean result = enterpriseApi.updateManagerMobile(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

}
