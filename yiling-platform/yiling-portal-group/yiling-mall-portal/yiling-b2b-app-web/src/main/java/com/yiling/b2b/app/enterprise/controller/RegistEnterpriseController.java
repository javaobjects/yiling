package com.yiling.b2b.app.enterprise.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.enterprise.form.RegistEnterpriseForm;
import com.yiling.b2b.app.enterprise.vo.RegistEnterpriseCertificateVO;
import com.yiling.b2b.app.system.form.CheckSmsVerifyCodeForm;
import com.yiling.b2b.app.system.form.GetSmsVerifyCodeForm;
import com.yiling.b2b.app.system.vo.StaffVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.common.web.rest.IdObject;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.RegistEnterpriseRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 注册企业信息 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/10/26
 */
@Slf4j
@RestController
@RequestMapping("/enterprise/register")
@Api(tags = "注册企业信息")
public class RegistEnterpriseController extends BaseController {

    @DubboReference
    StaffApi      staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    SmsApi        smsApi;
    @DubboReference
    LocationApi   locationApi;

    @ApiOperation(value = "获取企业入驻注册手机号短信验证码")
    @PostMapping("/getRegistVerifyCode")
    public Result getRegistVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        if (staff != null && UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed("您的账号已被停用，请联系客服或企业管理员");
        }

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.B2B_REGIST);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("获取注册手机号短信验证码失败");
        }
    }

    @ApiOperation(value = "校验企业入驻注册手机号短信验证码")
    @PostMapping("/checkRegistVerifyCode")
    public Result checkRegistVerifyCode(@RequestBody @Valid CheckSmsVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_REGIST);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("验证码错误或失效，请重新填写");
        }
    }

    @ApiOperation(value = "获取注册手机号对应的账号信息")
    @GetMapping("/getRegistAccountInfo")
    public Result<StaffVO> getRegistAccountInfo(@RequestParam @ApiParam(value = "注册手机号", required = true) String mobile) {
        Staff staff = staffApi.getByMobile(mobile);
        StaffVO staffVO = PojoUtils.map(staff, StaffVO.class);
        return Result.success(staffVO);
    }

    @ApiOperation(value = "获取注册企业类型所需的资质列表")
    @GetMapping("/getRegistCertificateList")
    public Result<CollectionObject<RegistEnterpriseCertificateVO>> getRegistCertificateList(@RequestParam @ApiParam(value = "企业类型（见字典：b2b_regist_enterprise_type）", required = true) Integer type) {
        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(type);
        if (enterpriseTypeEnum == null || !enterpriseTypeEnum.isTerminal()) {
            return Result.failed("企业类型参数不正确");
        }

        // 获取注册企业类型所需的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = enterpriseTypeEnum.getCertificateTypeEnumList();

        List<RegistEnterpriseCertificateVO> registEnterpriseCertificateVOList = CollUtil.newArrayList();
        enterpriseCertificateTypeEnumList.forEach(e -> {
            RegistEnterpriseCertificateVO registEnterpriseCertificateVO = new RegistEnterpriseCertificateVO();
            registEnterpriseCertificateVO.setType(e.getCode());
            registEnterpriseCertificateVO.setName(e.getName());
            registEnterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
            registEnterpriseCertificateVO.setRequired(e.getMustExist());
            registEnterpriseCertificateVOList.add(registEnterpriseCertificateVO);
        });

        return Result.success(new CollectionObject<>(registEnterpriseCertificateVOList));
    }

    @ApiOperation(value = "检查执业许可证号是否有效")
    @GetMapping("/validLicenseNumber")
    public Result<Void> validLicenseNumber(@RequestParam(required = true) @ApiParam(value = "执业许可证号", required = true) String licenseNumber) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getByLicenseNumber(licenseNumber);
        return enterpriseDTO == null ? Result.success() : Result.failed("执业许可证号已存在");
    }

    @ApiOperation(value = "注册企业信息")
    @PostMapping("/regist")
    public Result<IdObject> regist(@RequestBody @Valid RegistEnterpriseForm form) {
        EnterpriseTypeEnum enterpriseTypeEnum = EnterpriseTypeEnum.getByCode(form.getType());
        if (enterpriseTypeEnum == null) {
            return Result.failed("企业类型参数不正确");
        }

        String errorMsg = this.validateEnterpriseCertificateList(enterpriseTypeEnum, form.getCertificateList());
        if (StrUtil.isNotEmpty(errorMsg)) {
            return Result.failed(errorMsg);
        }

        // 获取省市区名称
        String[] locations = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());

        RegistEnterpriseRequest request = PojoUtils.map(form, RegistEnterpriseRequest.class);
        request.setProvinceName(locations[0]);
        request.setCityName(locations[1]);
        request.setRegionName(locations[2]);
        request.setEnterpriseSourceEnum(EnterpriseSourceEnum.B2B_APP_REGIST);
        Long id = enterpriseApi.regist(request);
        return Result.success(new IdObject(id));
    }

    /**
     * 验证企业资质列表
     *
     * @param enterpriseTypeEnum 企业类型
     * @param certificateList 企业资质列表
     * @return 错误信息
     */
    private String validateEnterpriseCertificateList(EnterpriseTypeEnum enterpriseTypeEnum, List<RegistEnterpriseForm.EnterpriseCertificateForm> certificateList) {
        if (CollUtil.isEmpty(certificateList)) {
            return "请上传相关资质";
        }

        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = enterpriseTypeEnum.getCertificateTypeEnumList();
        if (CollUtil.isEmpty(enterpriseCertificateTypeEnumList)) {
            return null;
        }

        List<RegistEnterpriseForm.EnterpriseCertificateForm> validList = CollUtil.newArrayList();
        for (EnterpriseCertificateTypeEnum c : enterpriseCertificateTypeEnumList) {
            RegistEnterpriseForm.EnterpriseCertificateForm enterpriseCertificateForm = certificateList.stream().filter(e -> e.getType().equals(c.getCode())).findFirst().orElse(null);

            if (c.getMustExist() && (enterpriseCertificateForm == null || StrUtil.isEmpty(enterpriseCertificateForm.getFileKey()))) {
                return "请上传《" + c.getName() + "》";
            }
            if (c.getCollectDate() && (enterpriseCertificateForm.getPeriodBegin() == null || (enterpriseCertificateForm.getPeriodEnd() == null && enterpriseCertificateForm.getLongEffective() == 0))) {
                return "《" + c.getName() + "》证照日期不能为空";
            }

            validList.add(enterpriseCertificateForm);
        }

        // 重新赋值
        certificateList = validList;

        return null;
    }
}
