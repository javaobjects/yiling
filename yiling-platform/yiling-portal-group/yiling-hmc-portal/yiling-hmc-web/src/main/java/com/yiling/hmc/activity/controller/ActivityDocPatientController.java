package com.yiling.hmc.activity.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.HttpMethod;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.form.*;
import com.yiling.hmc.activity.utils.AliHttpUtils;
import com.yiling.hmc.activity.vo.DoctorInfoVO;
import com.yiling.hmc.activity.vo.SaveActivityDocPatientRelVO;
import com.yiling.hmc.config.AliRealNameAuthenticationWebProperties;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.CheckUserDoctorRelRequest;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.HmcUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 医带患活动控制器
 *
 * @author: fan.shen
 * @date: 2023-01-31
 */
@Api(tags = "医带患活动控制器")
@RestController
@RequestMapping("/activityDocPatient")
@Slf4j
public class ActivityDocPatientController extends BaseController {

    @DubboReference(timeout = 1000 * 90)
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    SmsApi smsApi;

    @DubboReference
    DoctorApi doctorApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    HMCActivityApi hmcActivityApi;

    @Autowired
    AliRealNameAuthenticationWebProperties realNameProperties;

    @ApiOperation(value = "查看用户是否和医生绑定-医带患")
    @PostMapping("/checkUserDoctorRel")
    @ApiResponses({
            @ApiResponse(code = 200, message = "true-未绑定,false-已绑定")
    })
    @Log(title = "查看用户是否和医生绑定-医带患", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> checkUserDoctorRel(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid CheckDocPatientRelForm form) {
        CheckUserDoctorRelRequest request = new CheckUserDoctorRelRequest();
        request.setDoctorId(form.getDoctorId());
        request.setUserIdList(Collections.singletonList(currentUser.getCurrentUserId().intValue()));
        return Result.success(doctorApi.checkUserDoctorRel(request));
    }

    @ApiResponses({
            @ApiResponse(code = 100000, message = "uk或sessionKey过期，请重新发起登录流程")
    })
    @ApiOperation(value = "提交医带患绑定")
    @PostMapping("/saveActivityDocPatient")
    @Log(title = "提交医带患绑定", businessType = BusinessTypeEnum.OTHER)
    public Result<SaveActivityDocPatientRelVO> saveActivityDocPatient(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid SaveDocPatientRelForm form) {
        HmcUser hmcUser = hmcUserApi.getById(currentUser.getCurrentUserId());
        if (Objects.isNull(hmcUser)) {
            return Result.failed(100000, "uk已过期，请重新发起登录流程");
        }
        ActivityDocToPatientDTO activity = null;
        if (Objects.nonNull(form.getActivityId())) {
            activity = hmcActivityApi.queryActivityById(form.getActivityId());
        }
        AddActDocPatientRelRequest request = PojoUtils.map(form, AddActDocPatientRelRequest.class);
        if (Objects.nonNull(activity)) {
            int userState = 2;
            int activityState = 2;
            if (DateUtil.compare(activity.getBeginTime(), hmcUser.getCreateTime()) <= 0 && DateUtil.compare(activity.getEndTime(), hmcUser.getCreateTime()) >= 0) {
                // 填写报道表单的微信用户UnionId创建的健康管理中心用户OpenId是否为活动期间内创建的，如果是，则为新用户，否则为老用户
                userState = 1;
            }
            if (DateUtil.compare(activity.getBeginTime(), DateUtil.date()) <= 0 && DateUtil.compare(activity.getEndTime(), DateUtil.date()) >= 0) {
                // 医带患活动状态 1-进行中，2-已结束
                activityState = 1;
            }
            // 活动停用之后 -> 活动状态变为已结束
            if (activity.getActivityStatus().equals(2)) {
                activityState = 2;
            }
            request.setUserState(userState);
            request.setActivityState(activityState);
        }

        if(StrUtil.isNotBlank(form.getPatientName()) && StrUtil.isNotBlank(form.getIdCard())) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "APPCODE " + realNameProperties.getAppcode());
            try {
                Map<String, String> querys = new HashMap<>();
                Map<String, String> bodys = new HashMap<>();
                bodys.put("idcard", form.getIdCard());
                bodys.put("name", form.getPatientName());
                HttpResponse httpResponse = AliHttpUtils.doPost(realNameProperties.getUrl(), null, headers, querys, bodys);
                log.info("[realNameAuthentication] response:{}", httpResponse.toString());
                String entity = EntityUtils.toString(httpResponse.getEntity());
                log.info("[realNameAuthentication] entity:{}", entity);
                // https://market.aliyun.com/products/57000002/cmapi026109.html?spm=5176.730005.result.5.20153524LYIxCV&innerSource=search_实名认证#sku=yuncode20109000025
                if (!(JSONUtil.parseObj(entity).getStr("code").equals("0") &&
                        JSONUtil.parseObj(entity).getJSONObject("result").getStr("res").equals("1"))) {
                    return Result.failed("实名认证失败，请重新输入");
                }
                form.setRealNameFlag(1);
                request.setRealNameFlag(1);
            } catch (Exception e) {
                log.error("实名认证校验失败,错误信息:{}", e.getMessage(), e);
                return Result.failed("调用实名认证校验失败");
            }
        }

        request.setFromUserId(currentUser.getCurrentUserId().intValue());

        SaveActivityDocPatientRelDTO result = ihActivityPatientEducateApi.saveActivityDocPatient(request);
        SaveActivityDocPatientRelVO relVO = PojoUtils.map(result, SaveActivityDocPatientRelVO.class);
        log.info("[saveRel]提交医带患绑定返回结果：{}", JSONUtil.toJsonStr(relVO));
        return Result.success(relVO);
    }

    @ApiOperation(value = "根据医生id获取医生信息")
    @PostMapping("/getDoctorInfoByDoctorId")
    public Result<DoctorInfoVO> getDoctorInfoByDoctorId(@RequestBody @Valid GetDoctorInfoForm form) {
        DoctorAppInfoDTO doctorInfoDTO = ihActivityPatientEducateApi.getDoctorInfoByDoctorId(form.getDoctorId());
        log.info("[getDoctorInfoByDoctorId]根据医生id获取医生名称返回结果：{}", JSONUtil.toJsonStr(doctorInfoDTO));
        return Result.success(PojoUtils.map(doctorInfoDTO, DoctorInfoVO.class));
    }

    @ApiOperation(value = "实名认证校验")
    @PostMapping("/realNameAuthentication")
    public Result<Boolean> realNameAuthentication(@RequestBody @Valid RealNameAuthenticationForm form) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + realNameProperties.getAppcode());
        try {
            Map<String, String> querys = new HashMap<>();
            Map<String, String> bodys = new HashMap<>();
            bodys.put("idcard", form.getIdcard());
            bodys.put("name", form.getName());
            HttpResponse httpResponse = AliHttpUtils.doPost(realNameProperties.getUrl(), null, headers, querys, bodys);
            log.info("[realNameAuthentication] response:{}", httpResponse.toString());
            String entity = EntityUtils.toString(httpResponse.getEntity());
            log.info("[realNameAuthentication] entity:{}", entity);
            if (JSONUtil.parseObj(entity).getStr("code").equals("0") &&
                    JSONUtil.parseObj(entity).getJSONObject("result").getStr("res").equals("1")) {
                return Result.success(Boolean.TRUE);
            } else {
                return Result.failed("实名认证失败，请重新输入");
            }

        } catch (Exception e) {
            log.error("实名认证校验失败,错误信息:{}", e.getMessage(), e);
            return Result.success(Boolean.FALSE);
        }
    }

}