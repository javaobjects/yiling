package com.yiling.admin.b2b.common.controller;


import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.common.form.B2bAppBannerDeleteForm;
import com.yiling.admin.b2b.common.form.B2bAppBannerPageForm;
import com.yiling.admin.b2b.common.form.B2bAppBannerSaveForm;
import com.yiling.admin.b2b.common.form.B2bAppBannerStatusForm;
import com.yiling.admin.b2b.common.form.B2bAppBannerWeightForm;
import com.yiling.admin.b2b.common.vo.B2bAppBannerEnterpriseVO;
import com.yiling.admin.b2b.common.vo.B2bAppBannerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.B2bAppBannerEnterpriseLimitDTO;
import com.yiling.mall.banner.dto.request.B2bAppBannerDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerWeightRequest;
import com.yiling.mall.banner.enums.B2BBannerLinkTypeEnum;
import com.yiling.mall.banner.enums.BannerUsageScenarioEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Banner 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Slf4j
@Api(tags = "Banner管理接口")
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {

    @DubboReference
    BannerApi bannerApi;

    @DubboReference
    AdminApi adminApi;

    @ApiOperation(value = "B2B新增和编辑banner信息")
    @PostMapping("/save")
    @Log(title = "新增和编辑Banner信息", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> saveB2bAppBanner(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid B2bAppBannerSaveForm form) {
        B2bAppBannerSaveRequest request = PojoUtils.map(form, B2bAppBannerSaveRequest.class);
        if (null == request.getId()) {
            Date startTime = request.getStartTime();
            if (startTime.getTime() < DateUtil.beginOfDay(new Date()).getTime()) {
                return Result.failed("投放开始时间不应可选当天之前");
            }
            Date stopTime = request.getStopTime();
            if (startTime.getTime() > stopTime.getTime()) {
                return Result.failed("投放开始时间不应大于投放结束时间");
            }
        }

        if ((1 == request.getLinkType() && StringUtils.isBlank(form.getActivityLinks())) || (3 == request.getLinkType() && StringUtils.isBlank(form.getSearchKeywords())) || (4 == request.getLinkType() && Objects.isNull(form.getGoodsId())) || (5 == request.getLinkType() && Objects.isNull(form.getSellerEid())) || (6 == request.getLinkType() && StringUtils.isBlank(form.getActivityLinks()))) {
            return Result.failed("配置数据不能为空");
        }

        B2BBannerLinkTypeEnum bannerLinkTypeEnum = B2BBannerLinkTypeEnum.getByCode(request.getLinkType());
        if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SEARCH) {
            request.setActivityLinks(form.getSearchKeywords());
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.GOODS) {
            request.setActivityLinks(form.getGoodsId().toString());
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SHOP) {
            request.setActivityLinks(form.getSellerEid().toString());
        }

        if (StringUtils.isBlank(request.getActivityLinks())) {
            return Result.failed("配置数据不能为空");
        }

        if (4 == request.getUsageScenario() && CollUtil.isEmpty(request.getBannerEnterpriseList())) {
            return Result.failed("请求参数不完整");
        }
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setBannerSource(SourceEnum.B2B.getCode());
        boolean isSuccess = bannerApi.saveB2bAppBanner(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "B2B中banner排序，权重数字修改")
    @PostMapping("/editWeight")
    @Log(title = "编辑Banner权重", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppBannerWeight(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid B2bAppBannerWeightForm form) {
        B2bAppBannerWeightRequest request = PojoUtils.map(form, B2bAppBannerWeightRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = bannerApi.editB2bAppBannerWeight(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "B2B中banner的停用")
    @PostMapping("/editStatus")
    @Log(title = "编辑Banner状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppBannerStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppBannerStatusForm form) {
        B2bAppBannerStatusRequest request = PojoUtils.map(form, B2bAppBannerStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = bannerApi.editB2bAppBannerStatus(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "删除B2B中banner")
    @PostMapping("/delete")
    @Log(title = "删除Banner", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> deleteB2bAppBanner(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppBannerDeleteForm form) {
        B2bAppBannerDeleteRequest request = PojoUtils.map(form, B2bAppBannerDeleteRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = bannerApi.deleteB2bAppBanner(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "分页列表查询B2B中banner-运营后台")
    @PostMapping("/pageList")
    public Result<Page<B2bAppBannerVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppBannerPageForm form) {
        B2bAppBannerPageRequest request = PojoUtils.map(form, B2bAppBannerPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setBannerSource(SourceEnum.B2B.getCode());
        Page<B2bAppBannerDTO> pageDTO = bannerApi.pageB2bList(request);
        Page<B2bAppBannerVO> pageVO = PojoUtils.map(pageDTO, B2bAppBannerVO.class);
        pageVO.getRecords().forEach(b2bAppBannerVO -> {
            Admin createUser = adminApi.getById(b2bAppBannerVO.getCreateUser());
            b2bAppBannerVO.setCreateUserName(null != createUser ? createUser.getName() : "");

            Admin updateUser = adminApi.getById(b2bAppBannerVO.getUpdateUser());
            b2bAppBannerVO.setUpdateUserName(null != updateUser ? updateUser.getName() : "");

            operate(b2bAppBannerVO);
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "通过id查询banner")
    @GetMapping("/getById")
    public Result<B2bAppBannerVO> queryById(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "id") Long id) {
        B2bAppBannerDTO b2bAppBannerDTO = bannerApi.queryByB2BId(id);
        B2bAppBannerVO b2bAppBannerVO = PojoUtils.map(b2bAppBannerDTO, B2bAppBannerVO.class);

        if (BannerUsageScenarioEnum.getByCode(b2bAppBannerDTO.getUsageScenario()) == BannerUsageScenarioEnum.ENTERPRISE) {
            List<B2bAppBannerEnterpriseLimitDTO> b2bAppBannerEnterpriseLimitDTOList = bannerApi.listBannerEnterpriseByBannerIdAndEid(b2bAppBannerDTO.getId(), null);
            List<B2bAppBannerEnterpriseVO> bannerEnterpriseVOList = PojoUtils.map(b2bAppBannerEnterpriseLimitDTOList, B2bAppBannerEnterpriseVO.class);
            b2bAppBannerVO.setBannerEnterpriseList(bannerEnterpriseVOList);
        }

        operate(b2bAppBannerVO);

        return Result.success(b2bAppBannerVO);
    }

    private void operate(B2bAppBannerVO b2bAppBannerVO) {
        B2BBannerLinkTypeEnum bannerLinkTypeEnum = B2BBannerLinkTypeEnum.getByCode(b2bAppBannerVO.getLinkType());
        if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SEARCH) {
            b2bAppBannerVO.setSearchKeywords(b2bAppBannerVO.getActivityLinks());
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.GOODS) {
            b2bAppBannerVO.setGoodsId(StringUtils.isNotEmpty(b2bAppBannerVO.getActivityLinks()) ? Long.parseLong(b2bAppBannerVO.getActivityLinks()) : 0L);
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SHOP) {
            b2bAppBannerVO.setSellerEid(StringUtils.isNotEmpty(b2bAppBannerVO.getActivityLinks()) ? Long.parseLong(b2bAppBannerVO.getActivityLinks()) : 0L);
        }
    }
}
