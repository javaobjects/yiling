package com.yiling.admin.b2b.common.controller;


import java.util.Date;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.common.form.B2bAppHotWordsPageForm;
import com.yiling.admin.b2b.common.form.B2bAppHotWordsSaveForm;
import com.yiling.admin.b2b.common.form.B2bAppHotWordsStatusForm;
import com.yiling.admin.b2b.common.form.B2bAppHotWordsWeightForm;
import com.yiling.admin.b2b.common.vo.B2bAppHotWordsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.HotWordsApi;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsWeightRequest;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 热词 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-26
 */
@Slf4j
@Api(tags = "热词管理接口")
@RestController
@RequestMapping("/hotWords")
public class HotWordsController extends BaseController {

    @DubboReference
    HotWordsApi hotWordsApi;
    @DubboReference
    AdminApi    adminApi;

    @ApiOperation(value = "B2B新增和编辑热词")
    @PostMapping("/save")
    @Log(title = "新增和编辑热词信息", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> saveB2bAppHotWords(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid B2bAppHotWordsSaveForm form) {
        B2bAppHotWordsSaveRequest request = PojoUtils.map(form, B2bAppHotWordsSaveRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setHotWordsSource(SourceEnum.B2B.getCode());
        boolean isSuccess = hotWordsApi.saveB2bAppHotWords(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "B2B中热词排序，权重数字修改")
    @PostMapping("/editWeight")
    @Log(title = "编辑热词权重", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppHotWordsWeight(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppHotWordsWeightForm form) {
        B2bAppHotWordsWeightRequest request = PojoUtils.map(form, B2bAppHotWordsWeightRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = hotWordsApi.editB2bAppHotWordsWeight(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "B2B中热词的停用")
    @PostMapping("/editStatus")
    @Log(title = "编辑热词状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editB2bAppHotWordsStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppHotWordsStatusForm form) {
        B2bAppHotWordsStatusRequest request = PojoUtils.map(form, B2bAppHotWordsStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = hotWordsApi.editB2bAppHotWordsStatus(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "编辑时查询B2B中热词-运营后台")
    @GetMapping("/query")
    public Result<B2bAppHotWordsVO> query(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam("id") Long id) {
        B2bAppHotWordsDTO b2bAppHotWordsDTO = hotWordsApi.queryById(id);
        B2bAppHotWordsVO b2bAppHotWordsVO = PojoUtils.map(b2bAppHotWordsDTO, B2bAppHotWordsVO.class);
        return Result.success(b2bAppHotWordsVO);
    }

    @ApiOperation(value = "分页列表查询B2B中热词-运营后台")
    @PostMapping("/pageList")
    public Result<Page<B2bAppHotWordsVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody B2bAppHotWordsPageForm form) {
        B2bAppHotWordsPageRequest request = PojoUtils.map(form, B2bAppHotWordsPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setHotWordsSource(SourceEnum.B2B.getCode());
        Page<B2bAppHotWordsDTO> pageDTO = hotWordsApi.pageList(request);
        Page<B2bAppHotWordsVO> pageVO = PojoUtils.map(pageDTO, B2bAppHotWordsVO.class);
        pageVO.getRecords().forEach(b2bAppHotWordsVO -> {
            Admin admin = adminApi.getById(b2bAppHotWordsVO.getCreateUser());
            b2bAppHotWordsVO.setCreateUserName(admin.getName());
        });
        return Result.success(pageVO);
    }

}
