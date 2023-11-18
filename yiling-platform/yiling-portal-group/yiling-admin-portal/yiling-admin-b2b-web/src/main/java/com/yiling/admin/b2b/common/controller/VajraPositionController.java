package com.yiling.admin.b2b.common.controller;


import java.util.Date;
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
import com.yiling.admin.b2b.common.form.B2bAppVajraPositionDeleteForm;
import com.yiling.admin.b2b.common.form.B2bAppVajraPositionPageForm;
import com.yiling.admin.b2b.common.form.B2bAppVajraPositionStatusForm;
import com.yiling.admin.b2b.common.form.B2bAppVajraPositionWeightForm;
import com.yiling.admin.b2b.common.form.SaveB2bAppVajraPositionForm;
import com.yiling.admin.b2b.common.vo.B2bAppVajraPositionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.VajraPositionApi;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionWeightRequest;
import com.yiling.mall.banner.dto.request.SaveB2bAppVajraPositionRequest;
import com.yiling.mall.banner.enums.B2BVajraPositionLinkTypeEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 金刚位表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Slf4j
@Api(tags = "金刚位管理接口")
@RestController
@RequestMapping("/vajra")
public class VajraPositionController extends BaseController {

    @DubboReference
    VajraPositionApi vajraPositionApi;

    @DubboReference
    AdminApi adminApi;

    @ApiOperation(value = "新增和编辑金刚位信息")
    @PostMapping("/save")
    @Log(title = "新增和编辑金刚位信息", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> saveB2bAppVajraPosition(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveB2bAppVajraPositionForm form) {
        SaveB2bAppVajraPositionRequest request = PojoUtils.map(form, SaveB2bAppVajraPositionRequest.class);

        if ((1 == request.getLinkType() && StringUtils.isBlank(form.getActivityLinks())) || (3 == request.getLinkType() && StringUtils.isBlank(form.getSearchKeywords())) || (4 == request.getLinkType() && Objects.isNull(form.getGoodsId())) || (5 == request.getLinkType() && Objects.isNull(form.getSellerEid())) || (8 == request.getLinkType() && StringUtils.isBlank(form.getActivityLinks()))) {
            return Result.failed("配置数据不能为空");
        }

        B2BVajraPositionLinkTypeEnum vajraPositionLinkTypeEnum = B2BVajraPositionLinkTypeEnum.getByCode(request.getLinkType());
        if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SEARCH) {
            request.setActivityLinks(form.getSearchKeywords());
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.GOODS) {
            request.setActivityLinks(form.getGoodsId().toString());
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SHOP) {
            request.setActivityLinks(form.getSellerEid().toString());
        }

        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setSource(SourceEnum.B2B.getCode());
        boolean isSuccess = vajraPositionApi.saveB2bAppVajraPosition(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "金刚位排序，权重数字修改")
    @PostMapping("/editWeight")
    @Log(title = "编辑金刚位权重", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppVajraPositionWeight(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid B2bAppVajraPositionWeightForm form) {
        B2bAppVajraPositionWeightRequest request = PojoUtils.map(form, B2bAppVajraPositionWeightRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = vajraPositionApi.editB2bAppVajraPositionWeight(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "金刚位停用")
    @PostMapping("/editStatus")
    @Log(title = "编辑金刚位状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppVajraPositionStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid B2bAppVajraPositionStatusForm form) {
        B2bAppVajraPositionStatusRequest request = PojoUtils.map(form, B2bAppVajraPositionStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = vajraPositionApi.editB2bAppVajraPositionStatus(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "金刚位删除")
    @PostMapping("/delete")
    @Log(title = "删除金刚位", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> deleteB2bAppVajraPosition(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppVajraPositionDeleteForm form) {
        B2bAppVajraPositionDeleteRequest request = PojoUtils.map(form, B2bAppVajraPositionDeleteRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = vajraPositionApi.deleteB2bAppVajraPosition(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "分页列表查询金刚位-运营后台")
    @PostMapping("/pageList")
    public Result<Page<B2bAppVajraPositionVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppVajraPositionPageForm form) {
        B2bAppVajraPositionPageRequest request = PojoUtils.map(form, B2bAppVajraPositionPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setSource(SourceEnum.B2B.getCode());
        Page<B2bAppVajraPositionDTO> pageDTO = vajraPositionApi.pageList(request);
        Page<B2bAppVajraPositionVO> pageVO = PojoUtils.map(pageDTO, B2bAppVajraPositionVO.class);
        pageVO.getRecords().forEach(b2bAppVajraPositionVO -> {
            Admin createUser = adminApi.getById(b2bAppVajraPositionVO.getCreateUser());
            b2bAppVajraPositionVO.setCreateUserName(null != createUser ? createUser.getName() : "");

            Admin updateUser = adminApi.getById(b2bAppVajraPositionVO.getUpdateUser());
            b2bAppVajraPositionVO.setUpdateUserName(null != updateUser ? updateUser.getName() : "");

            operate(b2bAppVajraPositionVO);
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "通过id查询金刚位")
    @GetMapping("/getById")
    public Result<B2bAppVajraPositionVO> getById(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "id") Long id) {
        B2bAppVajraPositionDTO b2bAppVajraPositionDTO = vajraPositionApi.queryById(id);
        B2bAppVajraPositionVO b2bAppVajraPositionVO = PojoUtils.map(b2bAppVajraPositionDTO, B2bAppVajraPositionVO.class);

        operate(b2bAppVajraPositionVO);

        return Result.success(b2bAppVajraPositionVO);
    }

    private void operate(B2bAppVajraPositionVO b2bAppVajraPositionVO) {
        B2BVajraPositionLinkTypeEnum vajraPositionLinkTypeEnum = B2BVajraPositionLinkTypeEnum.getByCode(b2bAppVajraPositionVO.getLinkType());
        if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SEARCH) {
            b2bAppVajraPositionVO.setSearchKeywords(b2bAppVajraPositionVO.getActivityLinks());
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.GOODS) {
            b2bAppVajraPositionVO.setGoodsId(StringUtils.isNotEmpty(b2bAppVajraPositionVO.getActivityLinks()) ? Long.parseLong(b2bAppVajraPositionVO.getActivityLinks()) : 0L);
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SHOP) {
            b2bAppVajraPositionVO.setSellerEid(StringUtils.isNotEmpty(b2bAppVajraPositionVO.getActivityLinks()) ? Long.parseLong(b2bAppVajraPositionVO.getActivityLinks()) : 0L);
        }
    }
}
