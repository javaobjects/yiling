package com.yiling.user.web.user.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.UpdateUserRequest;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.web.auth.vo.LoginInfoVO;
import com.yiling.user.web.auth.vo.StaffVO;
import com.yiling.user.web.user.form.CheckNewMobileNumberVerifyCodeForm;
import com.yiling.user.web.user.form.GetNewMobileNumberVerifyCodeForm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Value("${jwt.expiration}")
    private Long    expiration;

    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    RedisService  redisService;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    StaffApi      staffApi;
    @DubboReference
    UserApi       userApi;
    @DubboReference
    SmsApi        smsApi;

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/getMyDetails")
    public Result<StaffVO> getMyDetails(@CurrentUser CurrentStaffInfo staffInfo) {
        Staff staff = staffApi.getById(staffInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(staff, StaffVO.class));
    }

    @ApiOperation(value = "获取当前用户隶属的企业列表")
    @GetMapping("/getMyEnterpriseList")
    public Result<CollectionObject<SimpleEnterpriseVO>> getMyEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staffInfo.getCurrentUserId(), EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 筛选出审核通过的企业
        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 筛选出开通了POP、B2B、销售助手、健康管理中心的企业
        {
            List<Long> eids = userEnterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            List<EnterprisePlatformDTO> enterprisePlatformDTOList = enterpriseApi.getEnterprisePlatforms(eids);
            if (CollUtil.isEmpty(enterprisePlatformDTOList)) {
                return Result.success(new CollectionObject<>(ListUtil.empty()));
            }

            Map<Long, EnterprisePlatformDTO> enterprisePlatformDTOMap = enterprisePlatformDTOList.stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity()));
            userEnterpriseList = userEnterpriseList.stream().filter(e -> {
                EnterprisePlatformDTO enterprisePlatformDTO = enterprisePlatformDTOMap.get(e.getId());
                if (enterprisePlatformDTO == null) {
                    return false;
                }
                return enterprisePlatformDTO.getPopFlag() == 1 || enterprisePlatformDTO.getMallFlag() == 1 || enterprisePlatformDTO.getSalesAssistFlag() == 1 || enterprisePlatformDTO.getHmcFlag() == 1;
            }).collect(Collectors.toList());
        }

        return Result.success(new CollectionObject<>(PojoUtils.map(userEnterpriseList, SimpleEnterpriseVO.class)));
    }

    @ApiOperation(value = "切换当前用户的企业")
    @GetMapping("/toggleEnterprise")
    public Result<LoginInfoVO> toggleEnterprise(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("eid") Long eid) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
        if (enterpriseDTO == null) {
            return Result.failed("企业信息不存在");
        } else if (EnterpriseStatusEnum.getByCode(enterpriseDTO.getStatus()) == EnterpriseStatusEnum.DISABLED) {
            return Result.failed("该企业已被停用");
        }

        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staffInfo.getCurrentUserId(), EnableStatusEnum.ENABLED);
        EnterpriseDTO userEnterprise = userEnterpriseList.stream().filter(e -> e.getId().equals(eid)).findFirst().orElse(null);
        if (userEnterprise == null) {
            return Result.failed("当前用户不属于该企业");
        }

        LoginInfoVO loginInfo = new LoginInfoVO();
        // 获取用户信息
        Staff staff = staffApi.getById(staffInfo.getCurrentUserId());
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));
        loginInfo.setUserEnterpriseList(PojoUtils.map(userEnterpriseList, SimpleEnterpriseVO.class));

        LoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(enterpriseDTO, LoginInfoVO.CurrentEnterpriseVO.class);
        // 当前企业对应的员工信息
        EnterpriseEmployeeDTO currentEnterpriseEmployeeDTO = employeeApi.getByEidUserId(eid, staff.getId());
        Long employeeId = currentEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = currentEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        loginInfo.setCurrentEnterpriseInfo(currentEnterpriseVO);

        // 生成token
        String token = jwtTokenUtils.generateToken(new JwtDataModel(staffInfo.getAppEnum().getAppId(), staff.getId(), enterpriseDTO.getId(),
                enterpriseDTO.getType(), enterpriseDTO.getChannelId(), employeeId, isAdmin));
        loginInfo.setToken(token);

        // 服务端保存的用户Token
        String userAppTokenKey = RedisKey.generate("token", staffInfo.getAppEnum().getAppCode(), "user", staff.getId().toString());
        redisService.set(userAppTokenKey, loginInfo.getToken(), expiration);

        return Result.success(loginInfo);
    }

    @ApiOperation(value = "获取原手机号短信验证码")
    @GetMapping("/getOriginalMobileNumberVerifyCode")
    public Result<BoolObject> getOriginalMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.sendVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.VERIFY_ORIGINAL_MOBILE_NUMBER);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "校验原手机号短信验证码")
    @GetMapping("/checkOriginalMobileNumberVerifyCode")
    public Result<BoolObject> checkOriginalMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam String verifyCode) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), verifyCode, SmsVerifyCodeTypeEnum.VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取更换绑定的新手机号短信验证码")
    @PostMapping("/getNewMobileNumberVerifyCode")
    public Result<BoolObject> getNewMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid GetNewMobileNumberVerifyCodeForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("原手机号短信验证码已失效，请返回上一步重新提交");
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff != null) {
            return Result.failed("该手机号已被绑定，请更换");
        }

        result = smsApi.sendVerifyCode(form.getMobile(), SmsVerifyCodeTypeEnum.CHANGE_MOBILE_NUMBER);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "校验更换绑定的新手机号短信验证码")
    @PostMapping("/checkNewMobileNumberVerifyCode")
    public Result<BoolObject> checkNewMobileNumberVerifyCode(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CheckNewMobileNumberVerifyCodeForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getOriginalVerifyCode(), SmsVerifyCodeTypeEnum.VERIFY_ORIGINAL_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("原手机号短信验证码已失效，请返回上一步重新提交");
        }

        result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.CHANGE_MOBILE_NUMBER);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }

        UpdateUserRequest request = new UpdateUserRequest();
        request.setAppId(UserAuthsAppIdEnum.MALL.getCode());
        request.setId(staffInfo.getCurrentUserId());
        request.setMobile(form.getMobile());
        request.setOpUserId(staffInfo.getCurrentUserId());
        result = userApi.updateUserInfo(request);

        return Result.success(new BoolObject(result));
    }
}
