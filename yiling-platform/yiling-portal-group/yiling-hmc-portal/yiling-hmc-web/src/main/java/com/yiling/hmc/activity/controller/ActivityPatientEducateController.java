package com.yiling.hmc.activity.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.JsonUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.form.*;
import com.yiling.hmc.activity.utils.AliHttpUtils;
import com.yiling.hmc.activity.vo.DoctorInfoVO;
import com.yiling.hmc.activity.vo.IdCardFrontPhotoOcrVO;
import com.yiling.hmc.activity.vo.PatientDoctorRelVO;
import com.yiling.hmc.activity.vo.SaveHmcPatientRelVO;
import com.yiling.hmc.config.AliRealNameAuthenticationWebProperties;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import com.yiling.ih.user.dto.request.VerifyActivityDoctorRequest;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 患教活动控制器
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Api(tags = "患教活动控制器")
@RestController
@RequestMapping("/activityPatientEducate")
@Slf4j
public class ActivityPatientEducateController extends BaseController {

    @DubboReference(timeout = 1000 * 90)
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    SmsApi smsApi;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    AliRealNameAuthenticationWebProperties realNameProperties;

    @ApiOperation(value = "身份证照片OCR识别")
    @PostMapping("/idCardFrontPhotoOcr")
    @Log(title = "身份证照片OCR识别", businessType = BusinessTypeEnum.OTHER)
    public Result<IdCardFrontPhotoOcrVO> idCardFrontPhotoOcr(@RequestBody @Valid IdCardFrontPhotoOcrForm form) {
        IdCardFrontPhotoOcrDTO ocrDTO = ihActivityPatientEducateApi.idCardFrontPhotoOcr(form.getIdCardFrontPhotoBase64());
        log.info("[idCardFrontPhotoOcr]返回结果：{}", JSONUtil.toJsonStr(ocrDTO));
        return Result.success(PojoUtils.map(ocrDTO, IdCardFrontPhotoOcrVO.class));
    }

    @ApiOperation(value = "校验身份证是否存在")
    @PostMapping("/checkIdCard")
    @Log(title = "校验身份证是否存在", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> checkIdCard(@RequestBody @Valid CheckIdCardForm form) {
        return Result.success(ihActivityPatientEducateApi.checkIdCard(form.getIdCard()));
    }

    @ApiOperation(value = "查询是否已入组")
    @PostMapping("/queryPatientDoctorRel")
    @Log(title = "查询是否已入组", businessType = BusinessTypeEnum.OTHER)
    public Result<PatientDoctorRelVO> queryPatientDoctorRel(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryPatientDoctorRelForm form) {
        log.info("[queryPatientDoctorRel]入参:{}", JSONUtil.toJsonStr(form));
        VerifyActivityDoctorRequest queryDoctorRequest = new VerifyActivityDoctorRequest();
        queryDoctorRequest.setActivityId(form.getActivityId().intValue());
        queryDoctorRequest.setDoctorId(form.getDoctorId());
        Boolean res = doctorApi.verifyActivityDoctor(queryDoctorRequest);
        if (Objects.isNull(res) || Boolean.FALSE.equals(res)) {
            return Result.failed("当前活动下未查询到医生信息");
        }

        QueryPatientDoctorRelRequest request = PojoUtils.map(form, QueryPatientDoctorRelRequest.class);
        request.setUserId(currentUser.getCurrentUserId().intValue());
        PatientDoctorRelDTO relDTO = ihActivityPatientEducateApi.queryPatientDoctorRel(request);
        PatientDoctorRelVO result = PojoUtils.map(relDTO, PatientDoctorRelVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "保存入组")
    @PostMapping("/saveRel")
    @Log(title = "保存入组", businessType = BusinessTypeEnum.OTHER)
    public Result<SaveHmcPatientRelVO> saveRel(@RequestBody @Valid SaveHmcPatientRelForm form) {
//        if(StrUtil.isNotBlank(form.getPatientName()) && StrUtil.isNotBlank(form.getIdCard())) {
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Authorization", "APPCODE " + realNameProperties.getAppcode());
//            try {
//                Map<String, String> querys = new HashMap<>();
//                Map<String, String> bodys = new HashMap<>();
//                bodys.put("idcard", form.getIdCard());
//                bodys.put("name", form.getPatientName());
//                HttpResponse httpResponse = AliHttpUtils.doPost(realNameProperties.getUrl(), null, headers, querys, bodys);
//                log.info("[realNameAuthentication] response:{}", httpResponse.toString());
//                String entity = EntityUtils.toString(httpResponse.getEntity());
//                log.info("[realNameAuthentication] entity:{}", entity);
//                // https://market.aliyun.com/products/57000002/cmapi026109.html?spm=5176.730005.result.5.20153524LYIxCV&innerSource=search_实名认证#sku=yuncode20109000025
//                if (!(JSONUtil.parseObj(entity).getStr("code").equals("0") &&
//                        JSONUtil.parseObj(entity).getJSONObject("result").getStr("res").equals("1"))) {
//                    return Result.failed("实名认证失败，请重新输入");
//                }
//                form.setRealNameFlag(1);
//            } catch (Exception e) {
//                log.error("实名认证校验失败,错误信息:{}", e.getMessage(), e);
//                return Result.failed("调用实名认证校验失败");
//            }
//        }

        AddPatientRelRequest request = PojoUtils.map(form, AddPatientRelRequest.class);
        SavePatientRelDTO result = ihActivityPatientEducateApi.savePatient(request);
        SaveHmcPatientRelVO relVO = PojoUtils.map(result, SaveHmcPatientRelVO.class);
        relVO.setDesc(form.getDesc());
        log.info("[saveRel]保存入组返回结果：{}", JSONUtil.toJsonStr(relVO));
        return Result.success(relVO);
    }

    @ApiOperation(value = "获取验证码")
    @PostMapping("/getVerifyCode")
    @Log(title = "获取验证码", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> getVerifyCode(@RequestBody @Valid GetVerifyCodeForm form) {
        boolean result = smsApi.sendVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.HMC_ACTIVITY_PATIENT_EDUCATE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("获取验证码失败");
        }
    }

    @ApiOperation(value = "校验验证码")
    @PostMapping("/checkVerifyCode")
    public Result checkVerifyCode(@RequestBody @Valid CheckSmsVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.HMC_ACTIVITY_PATIENT_EDUCATE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("验证码错误或失效，请重新填写");
        }
    }

    @ApiOperation(value = "根据医生id获取医生名称")
    @PostMapping("/getDoctorInfoByDoctorId")
    public Result<DoctorInfoVO> getDoctorInfoByDoctorId(@RequestBody @Valid GetDoctorInfoForm form) {
        DoctorAppInfoDTO doctorInfoDTO = ihActivityPatientEducateApi.getDoctorInfoByDoctorId(form.getDoctorId());
        log.info("[getDoctorInfoByDoctorId]根据医生id获取医生名称返回结果：{}", JSONUtil.toJsonStr(doctorInfoDTO));
        return Result.success(PojoUtils.map(doctorInfoDTO, DoctorInfoVO.class));
    }

}