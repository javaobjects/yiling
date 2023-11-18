package com.yiling.hmc.diagnosis.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.utils.AliHttpUtils;
import com.yiling.hmc.config.AliRealNameAuthenticationWebProperties;
import com.yiling.hmc.diagnosis.form.CompleteMyPatientDiseaseForm;
import com.yiling.hmc.diagnosis.form.GetPatientDetailForm;
import com.yiling.hmc.diagnosis.form.SaveMyPatientForm;
import com.yiling.hmc.diagnosis.form.UpdateMyPatientForm;
import com.yiling.hmc.diagnosis.vo.MyPatientDetailVO;
import com.yiling.hmc.diagnosis.vo.MyPatientVO;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.patient.dto.HmcSavePatientResultDTO;
import com.yiling.ih.patient.dto.MyPatientDTO;
import com.yiling.ih.patient.dto.MyPatientDetailDTO;
import com.yiling.ih.patient.dto.request.*;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 我的就诊人
 *
 * @author: fan.shen
 * @date: 2022-12-12
 */
@Api(tags = "我的就诊人")
@RestController
@RequestMapping("/myPatient")
@Slf4j
public class MyPatientController extends BaseController {

    @DubboReference
    HmcPatientApi hmcPatientApi;

    @Autowired
    AliRealNameAuthenticationWebProperties realNameProperties;

    @ApiOperation(value = "我的就诊人列表")
    @PostMapping("/myPatientList")
    @Log(title = "我的就诊人列表", businessType = BusinessTypeEnum.OTHER)
    public Result<List<MyPatientVO>> myPatientList(@CurrentUser CurrentUserInfo currentUser) {
        List<MyPatientDTO> patientDTOList = hmcPatientApi.myPatientList(currentUser.getCurrentUserId().intValue());
        return Result.success(PojoUtils.map(patientDTOList, MyPatientVO.class));
    }

    /**
     * 温馨提示：
     * 1.解析结果时，先判断code
     * 2.出现'无记录'时，有以下几种原因
     *     (1)现役军人、武警官兵、特殊部门人员及特殊级别官员；
     *     (2)退役不到2年的军人和士兵（根据军衔、兵种不同，时间会有所不同，一般为2年）；
     *     (3)户口迁出，且没有在新的迁入地迁入；
     *     (4)户口迁入新迁入地，当地公安系统未将迁移信息上报到公安部（上报时间地域不同而有所差异）；
     *     (5)更改姓名，当地公安系统未将更改信息上报到公安部（上报时间因地域不同而有所差异）；
     *     (6)移民；
     *     (7)未更换二代身份证；
     *     (8)死亡。
     *     (9)身份证号确实不存在
     * {
     *     "code": "0", //返回码，0：成功，非0：失败（详见错误码定义）
     *           //当code=0时，再判断下面result中的res；当code!=0时，表示调用已失败，无需再继续
     *     "message": "成功", //返回码说明
     *     "result": {
     *         "name": "冯天", //姓名
     *         "idcard": "350301198011129422", //身份证号
     *         "res": "1", //核验结果状态码，1 一致；2 不一致；3 无记录
     *         "description": "一致",  //核验结果状态描述
     *        "sex": "男",
     *         "birthday": "19940320",
     *         "address": "江西省南昌市东湖区"
     *     }
     * }
     * @param currentUser
     * @param form
     * @return
     */
    @ApiOperation(value = "保存就诊人")
    @PostMapping("/saveMyPatient")
    @Log(title = "保存就诊人", businessType = BusinessTypeEnum.OTHER)
    public Result<Integer> saveMyPatient(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid SaveMyPatientForm form) {
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
            } catch (Exception e) {
                log.error("实名认证校验失败,错误信息:{}", e.getMessage(), e);
                return Result.failed("调用实名认证校验失败");
            }
        }

        SaveMyPatientRequest request = PojoUtils.map(form, SaveMyPatientRequest.class);
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        HmcSavePatientResultDTO hmcSavePatientResultDTO = hmcPatientApi.saveMyPatient(request);
        if (Objects.nonNull(hmcSavePatientResultDTO)) {
            return Result.success(hmcSavePatientResultDTO.getPatientId());
        }
        return Result.failed("添加就诊人出错！");
    }

    @ApiOperation(value = "完善既往史")
    @PostMapping("/completeMyPatientDisease")
    @Log(title = "完善既往史", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> completeMyPatientDisease(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid CompleteMyPatientDiseaseForm form) {
        CompletePatientDiseaseRequest request = PojoUtils.map(form, CompletePatientDiseaseRequest.class);
        hmcPatientApi.completeMyPatientDisease(request);
        return Result.success();
    }

    @ApiOperation(value = "就诊人详情")
    @PostMapping("/myPatientDetail")
    @Log(title = "就诊人详情", businessType = BusinessTypeEnum.OTHER)
    public Result<MyPatientDetailVO> myPatientDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid GetPatientDetailForm form) {
        GetMyPatientDetailRequest request = new GetMyPatientDetailRequest();
        request.setPatientId(form.getPatientId());
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        MyPatientDetailDTO patientDetailDTO = hmcPatientApi.myPatientDetail(request);
        return Result.success(PojoUtils.map(patientDetailDTO, MyPatientDetailVO.class));
    }

    @ApiOperation(value = "删除就诊人")
    @PostMapping("/delMyPatient")
    @Log(title = "删除就诊人", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> delMyPatient(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid GetPatientDetailForm form) {
        DelMyPatientRequest request = new DelMyPatientRequest();
        request.setPatientId(form.getPatientId());
        request.setFromUserId(currentUser.getCurrentUserId().intValue());
        Boolean res = hmcPatientApi.delMyPatient(request);
        return Result.success(res);
    }

    @ApiOperation(value = "修改就诊人")
    @PostMapping("/updateMyPatient")
    @Log(title = "修改就诊人", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> updateMyPatient(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid UpdateMyPatientForm form) {
        UpdateMyPatientRequest request = PojoUtils.map(form, UpdateMyPatientRequest.class);

        if(StrUtil.isNotBlank(form.getPatientName()) && StrUtil.isNotBlank(form.getIdCard()) && !form.getRealNameFlag().equals(1)) {
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
        Boolean res = hmcPatientApi.updateMyPatient(request);
        return Result.success(res);
    }

    @ApiOperation(value = "获取上一个就诊人")
    @PostMapping("/getLastPatient")
    @Log(title = "获取上一个就诊人", businessType = BusinessTypeEnum.OTHER)
    public Result<MyPatientDetailVO> getLastPatient(@CurrentUser CurrentUserInfo currentUser) {
        MyPatientDetailDTO patientDetailDTO = hmcPatientApi.getLastPatient(currentUser.getCurrentUserId());
        return Result.success(PojoUtils.map(patientDetailDTO, MyPatientDetailVO.class));
    }

}