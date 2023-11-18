package com.yiling.admin.b2b.member.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.member.form.MemberEquityForm;
import com.yiling.admin.b2b.member.form.QueryMemberEquityForm;
import com.yiling.admin.b2b.member.form.UpdateMemberEquityForm;
import com.yiling.admin.b2b.member.vo.MemberEquityVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.enums.MemberEquityTypeEnum;
import com.yiling.user.member.api.MemberEquityApi;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员权益 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/26
 */
@Slf4j
@RestController
@RequestMapping("/memberEquity")
@Api(tags = "权益接口")
public class MemberEquityController extends BaseController {

    @DubboReference
    MemberEquityApi memberEquityApi;

    @ApiOperation(value = "权益分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<MemberEquityVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryMemberEquityForm form) {
        QueryMemberEquityRequest request = PojoUtils.map(form,QueryMemberEquityRequest.class);
        Page<MemberEquityDTO> equityDtoPage = memberEquityApi.queryListPage(request);

        return Result.success(PojoUtils.map(equityDtoPage,MemberEquityVO.class));
    }

    @ApiOperation(value = "权益列表")
    @PostMapping("/queryList")
    public Result<List<MemberEquityVO>> queryList(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryMemberEquityForm form) {
        QueryMemberEquityRequest request = PojoUtils.map(form,QueryMemberEquityRequest.class);
        request.setStatus(1);
        List<MemberEquityDTO> equityDtoList = memberEquityApi.queryList(request);

        return Result.success(PojoUtils.map(equityDtoList,MemberEquityVO.class));
    }

    @ApiOperation(value = "获取权益详情")
    @GetMapping("/getEquity")
    public Result<MemberEquityVO> getEquity(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("id") Long id) {
        MemberEquityDTO equityDto = memberEquityApi.getEquity(id);

        return Result.success(PojoUtils.map(equityDto,MemberEquityVO.class));
    }

    @ApiOperation(value = "修改权益状态")
    @GetMapping("/updateStatus")
    @Log(title = "修改权益状态",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateStatus(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("id") Long id) {
        Boolean result = memberEquityApi.updateStatus(id,adminInfo.getCurrentUserId());

        return Result.success(result);
    }

    @ApiOperation(value = "新增权益")
    @PostMapping("/createEquity")
    @Log(title = "新增权益",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> createEquity(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid MemberEquityForm form) {
        CreateMemberEquityRequest request = PojoUtils.map(form,CreateMemberEquityRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setType(MemberEquityTypeEnum.USER_DEFINE.getCode());

        Boolean result = memberEquityApi.createEquity(request);
        return Result.success(result);
    }

    @ApiOperation(value = "修改权益")
    @PostMapping("/updateEquity")
    @Log(title = "修改权益",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateEquity(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid UpdateMemberEquityForm form) {
        UpdateMemberEquityRequest request = PojoUtils.map(form,UpdateMemberEquityRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = memberEquityApi.updateEquity(request);
        return Result.success(result);
    }

    @ApiOperation(value = "删除权益")
    @GetMapping("/deleteEquity")
    @Log(title = "删除权益",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> deleteEquity(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("id") Long id) {
        Boolean result = memberEquityApi.deleteEquity(id,adminInfo.getCurrentUserId());

        return Result.success(result);
    }

}
