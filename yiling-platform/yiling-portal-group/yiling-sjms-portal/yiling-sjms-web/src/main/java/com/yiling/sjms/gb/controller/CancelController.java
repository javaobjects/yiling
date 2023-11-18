package com.yiling.sjms.gb.controller;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbFormSubmitProcessApi;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.enums.GBErrorCode;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.form.QueryGBFormListPageForm;
import com.yiling.sjms.gb.form.SaveGBCancelInfoForm;
import com.yiling.sjms.gb.vo.GbCancelFormInfoListVO;
import com.yiling.sjms.gb.vo.GbFormCancelListVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gb/cancel")
@Api(tags = "团购取消")
public class CancelController extends BaseController {


    @DubboReference
    GbFormApi gbFormApi;
    @DubboReference
    GbFormSubmitProcessApi gbFormSubmitProcessApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "团购取消选择列表")
    @PostMapping("/choose/list")
    public Result<Page<GbCancelFormInfoListVO>> getGBCancelFormChooseListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFormListPageForm form){

        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        if(StringUtils.isNotBlank(form.getStartMonthTime())){
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if(StringUtils.isNotBlank(form.getEndMonthTime())){
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }

        request.setStatus(FormStatusEnum.APPROVE.getCode());
        request.setCancelFlag(1);
        request.setBizType(GbFormBizTypeEnum.SUBMIT.getCode());
        request.setCreateUser(userInfo.getCurrentUserId());
        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFormListPage(request);

        return Result.success(PojoUtils.map(gbFormListPage, GbCancelFormInfoListVO.class));
    }

    @ApiOperation(value = "团购取消列表")
    @PostMapping("/list")
    public Result<Page<GbFormCancelListVO>> getGbSubmitFormListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFormListPageForm form){
        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        request.setBizType(GbFormBizTypeEnum.CANCEL.getCode());

        if(StringUtils.isNotBlank(form.getStartMonthTime())){
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if(StringUtils.isNotBlank(form.getEndMonthTime())){
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }
        if(request.getStartApproveTime()!= null){
            request.setStartApproveTime(DateUtil.beginOfDay(request.getStartApproveTime()));
        }
        if(request.getEndApproveTime()!= null){
            request.setEndApproveTime(DateUtil.endOfDay(request.getEndApproveTime()));
        }

        if(request.getStartSubmitTime()!= null){
            request.setStartSubmitTime(DateUtil.beginOfDay(request.getStartSubmitTime()));
        }
        if(request.getEndSubmitTime()!= null){
            request.setEndSubmitTime(DateUtil.endOfDay(request.getEndSubmitTime()));
        }

        request.setCreateUser(userInfo.getCurrentUserId());
        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFormListPage(request);
        return Result.success(PojoUtils.map(gbFormListPage, GbFormCancelListVO.class));
    }

    @ApiOperation(value = "取消保存提交")
    @PostMapping("/save")
    public Result saveCancelForm(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveGBCancelInfoForm form){

        Boolean flag = (Boolean)redisService.get(RedisKey.generate("sjms", "gb","submit", "button"));
        if(flag == null){
            flag = false;
        }
        if(!flag){
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_SUBMIT_START);
        }

        SaveGBCancelInfoRequest request = PojoUtils.map(form, SaveGBCancelInfoRequest.class);

        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setBizType(GbFormBizTypeEnum.CANCEL.getCode());
        request.setEmpId(employee.getEmpId());
        request.setEmpName(employee.getEmpName());
        request.setDeptId(employee.getDeptId());
        request.setDeptName(employee.getYxDept());
        request.setBizArea(employee.getYxArea());
        request.setBizProvince(employee.getYxProvince());
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setFormType(FormTypeEnum.GB_CANCEL.getCode());
        gbFormSubmitProcessApi.cancelFormProcess(request);

        return Result.success();
    }



}
