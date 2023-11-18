package com.yiling.sales.assistant.app.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationSelectTreeDTO;
import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.basic.location.util.LocationTreeUtils;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsVerifyCodeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.JwtTokenUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.mall.userderegister.api.UserDeregisterAccountMallApi;
import com.yiling.sales.assistant.app.homepage.form.CompleteZiRanRenInfoForm;
import com.yiling.sales.assistant.app.homepage.vo.PersonalInfoCompleteStatusVO;
import com.yiling.sales.assistant.app.homepage.vo.UncompletedPersonalInfoVO;
import com.yiling.sales.assistant.app.system.enums.LoginErrorCode;
import com.yiling.sales.assistant.app.system.form.AddDeregisterAccountForm;
import com.yiling.sales.assistant.app.system.form.CheckLoginPasswordForm;
import com.yiling.sales.assistant.app.system.form.CheckResetPasswordVerifyCodeForm;
import com.yiling.sales.assistant.app.system.form.GetSmsVerifyCodeForm;
import com.yiling.sales.assistant.app.system.form.RegistForm;
import com.yiling.sales.assistant.app.system.form.ResetMobileForm;
import com.yiling.sales.assistant.app.system.form.ResetPasswordForm;
import com.yiling.sales.assistant.app.system.form.UpdateUserSalesAreaForm;
import com.yiling.sales.assistant.app.system.vo.LocationSelectTreeVO;
import com.yiling.sales.assistant.app.system.vo.LoginInfoVO;
import com.yiling.sales.assistant.app.system.vo.StaffVO;
import com.yiling.sales.assistant.app.system.vo.UserDeregisterValidVO;
import com.yiling.sales.assistant.app.system.vo.UserSalesAreaVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.StaffExternaAuditApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.api.UserDeregisterAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.StaffExternaAuditDTO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.UserSalesAreaDTO;
import com.yiling.user.system.dto.request.AddDeregisterAccountRequest;
import com.yiling.user.system.dto.request.CreateStaffExternaAuditRequest;
import com.yiling.user.system.dto.request.CreateStaffRequest;
import com.yiling.user.system.dto.request.SaveUserSalesAreaRequest;
import com.yiling.user.system.enums.UserAuthsAppIdEnum;
import com.yiling.user.system.enums.UserDeregisterAccountSourceEnum;
import com.yiling.user.system.enums.UserStatusEnum;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/9/26
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController extends BaseController {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RedisService  redisService;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    StaffExternaAuditApi staffExternaAuditApi;
    @DubboReference
    LocationApi locationApi;
    @DubboReference
    SmsApi smsApi;
    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;
    @DubboReference
    UserDeregisterAccountMallApi userDeregisterAccountMallApi;

    @ApiOperation(value = "注册账号")
    @PostMapping("/regist")
    @Log(title = "注册账号", businessType = BusinessTypeEnum.INSERT)
    public Result regist(@RequestBody @Valid RegistForm form) {
        // 验证码token校验
        boolean checkVerifyCodeTokenResult = smsApi.checkVerifyCodeToken(form.getMobile(), form.getVerifyCodeToken(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);
        if (!checkVerifyCodeTokenResult) {
            return Result.failed(LoginErrorCode.VERIFY_CODE_TOKEN_ERROR);
        }

        CreateStaffRequest request = new CreateStaffRequest();
        request.setMobile(form.getMobile());
        request.setPassword(form.getPassword());
        request.setOpUserId(0L);
        staffApi.create(request);

        smsApi.cleanVerifyCodeToken(form.getMobile(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_LOGIN);
        return Result.success();
    }

    @ApiOperation(value = "判断个人信息是否完善")
    @GetMapping("/isPersonalInfoCompleted")
    public Result<PersonalInfoCompleteStatusVO> isPersonalInfoCompleted(@CurrentUser CurrentStaffInfo staffInfo) {
        // 以岭人员和小三元不做是否完善信息校验
        if (UserTypeEnum.YILING == staffInfo.getUserType() || UserTypeEnum.XIAOSANYUAN == staffInfo.getUserType()) {
            return Result.success(new PersonalInfoCompleteStatusVO(2, null));
        }

        // 是否设置销售区域
        boolean hasUserSalesAreaSettingFlag = userApi.hasUserSalesAreaSetting(staffInfo.getCurrentUserId());
        if (!hasUserSalesAreaSettingFlag) {
            StaffExternaAuditDTO staffExternaAuditDTO = staffExternaAuditApi.getUserLatestAuditInfo(staffInfo.getCurrentUserId());
            if (staffExternaAuditDTO == null) {
                return Result.success(new PersonalInfoCompleteStatusVO(0, "请先完善个人信息"));
            } else if (staffExternaAuditDTO.getAuditStatus() == 1) {
                return Result.success(new PersonalInfoCompleteStatusVO(1, "您的证件24小时之内审核通过，请耐心等待~"));
            } else if (staffExternaAuditDTO.getAuditStatus() == 3) {
                return Result.success(new PersonalInfoCompleteStatusVO(3, staffExternaAuditDTO.getAuditRejectReason()));
            }
        }

        return Result.success(new PersonalInfoCompleteStatusVO(2, null));
    }

    @ApiOperation(value = "获取待完善的个人信息")
    @GetMapping("/getUncompletedPersonalInfo")
    public Result<UncompletedPersonalInfoVO> getUncompletedPersonalInfo(@CurrentUser CurrentStaffInfo staffInfo) {
        if (UserTypeEnum.ZIRANREN != staffInfo.getUserType()) {
            return Result.failed("用户类型非法，不允许此操作");
        }

        UncompletedPersonalInfoVO uncompletedPersonalInfoVO = new UncompletedPersonalInfoVO();

        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        uncompletedPersonalInfoVO.setName(userDTO.getName());
        uncompletedPersonalInfoVO.setIdNumber(userDTO.getIdNumber());
        uncompletedPersonalInfoVO.setSalesAreaDesc("全国");

        // 小三元不用完善个人信息
        // if (UserTypeEnum.XIAOSANYUAN == staffInfo.getUserType()) {
        //     EnterpriseSalesAreaDTO enterpriseSalesAreaDTO = enterpriseApi.getEnterpriseSalesArea(staffInfo.getCurrentEid());
        //     if (enterpriseSalesAreaDTO != null) {
        //         uncompletedPersonalInfoVO.setSalesAreaDesc(enterpriseSalesAreaDTO.getDescription());
        //         List<LocationTreeVO> salesAreaTree = JSONUtil.toList(JSONUtil.parseArray(enterpriseSalesAreaDTO.getJsonContent()), LocationTreeVO.class);
        //         uncompletedPersonalInfoVO.setSalesAreaTree(salesAreaTree);
        //     } else {
        //         uncompletedPersonalInfoVO.setSalesAreaDesc("0省0市");
        //     }
        // }

        return Result.success(uncompletedPersonalInfoVO);
    }

    // @ApiOperation(value = "完善小三元个人信息")
    // @PostMapping("/completeXiaoSanYuanInfo")
    // public Result completeXiaoSanYuanInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CompleteXiaoSanYuanInfoForm form) {
    //     if (UserTypeEnum.XIAOSANYUAN != staffInfo.getUserType()) {
    //         return Result.failed("用户类型非法，不允许此操作");
    //     }
    //
    //     EnterpriseSalesAreaDTO enterpriseSalesAreaDTO = enterpriseApi.getEnterpriseSalesArea(staffInfo.getCurrentEid());
    //     if (enterpriseSalesAreaDTO != null) {
    //         return Result.failed("您已完善过个人信息，请勿重复操作");
    //     }
    //
    //     CompletePersonalInformationRequest request = new CompletePersonalInformationRequest();
    //     request.setUserId(staffInfo.getCurrentUserId());
    //     request.setName(form.getName());
    //     request.setIdNumber(form.getIdNumber());
    //     request.setSalesAreaTree(PojoUtils.map(form.getSalesAreaTree(), LocationTreeDTO.class));
    //     request.setOpUserId(staffInfo.getCurrentUserId());
    //
    //     boolean result = userApi.completePersonalInformation(request);
    //     return result ? Result.success() : Result.failed(ResultCode.FAILED);
    // }

    @ApiOperation(value = "获取用户销售区域信息")
    @GetMapping("/getUserSalesArea")
    public Result<UserSalesAreaVO> getUserSalesArea(@CurrentUser CurrentStaffInfo staffInfo) {
        if (staffInfo.getUserType() == UserTypeEnum.ZIRANREN) {
            UserSalesAreaDTO userSalesAreaDTO = userApi.getSaleAreaByUserId(staffInfo.getCurrentUserId());
            if (userSalesAreaDTO == null) {
                return Result.failed("未设置销售区域信息");
            }

            UserSalesAreaVO userSalesAreaVO = new UserSalesAreaVO();
            userSalesAreaVO.setSalesAreaAllFlag(userSalesAreaDTO.getSalesAreaAllFlag());
            userSalesAreaVO.setDescription(userSalesAreaDTO.getDescription());
            if (userSalesAreaDTO.getSalesAreaAllFlag() == 1) {
                userSalesAreaVO.setSalesAreaSelectTree(this.getLocationSelectedAllTreeVO());
            } else {
                userSalesAreaVO.setSalesAreaSelectTree(this.getLocationSelectTreeVO(userSalesAreaDTO.getJsonContent()));
            }
            return Result.success(userSalesAreaVO);
        } else if (staffInfo.getUserType() == UserTypeEnum.XIAOSANYUAN) {
            EnterpriseSalesAreaDTO enterpriseSalesAreaDTO = enterpriseApi.getEnterpriseSalesArea(staffInfo.getCurrentEid());
            if (enterpriseSalesAreaDTO == null) {
                return Result.failed("未设置销售区域信息");
            }

            UserSalesAreaVO userSalesAreaVO = new UserSalesAreaVO();
            userSalesAreaVO.setSalesAreaAllFlag(0);
            userSalesAreaVO.setDescription(enterpriseSalesAreaDTO.getDescription());
            userSalesAreaVO.setSalesAreaSelectTree(this.getLocationSelectTreeVO(enterpriseSalesAreaDTO.getJsonContent()));
            return Result.success(userSalesAreaVO);
        } else {
            return Result.failed("非法用户类型");
        }
    }

    private List<LocationSelectTreeVO> getLocationSelectedAllTreeVO() {
        List<LocationTreeDTO> allLocationTree = locationApi.listTreeByParentCode("", 3);
        List<LocationSelectTreeDTO> locationSelectTree = LocationTreeUtils.getLocationSelectedAllTree(allLocationTree);
        return PojoUtils.map(locationSelectTree, LocationSelectTreeVO.class);
    }

    private List<LocationSelectTreeVO> getLocationSelectTreeVO(String areaJsonContent) {
        List<LocationTreeDTO> allLocationTree = locationApi.listTreeByParentCode("", 3);
        List<String> selectedLocationCode = ListUtil.empty();

        if (StrUtil.isNotEmpty(areaJsonContent)) {
            List<LocationTreeDTO> selectedLocationTree = JSONUtil.toList(JSONUtil.parseArray(areaJsonContent), LocationTreeDTO.class);
            selectedLocationCode = LocationTreeUtils.listTreeCode(selectedLocationTree);
        }

        List<LocationSelectTreeDTO> locationSelectTree = LocationTreeUtils.getLocationSelectTree(allLocationTree, selectedLocationCode);
        return PojoUtils.map(locationSelectTree, LocationSelectTreeVO.class);
    }

    @ApiOperation(value = "修改用户销售区域")
    @PostMapping("/updateUserSalesArea")
    @Log(title = "修改用户销售区域", businessType = BusinessTypeEnum.UPDATE)
    public Result updateUserSalesArea(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateUserSalesAreaForm form) {
        if (staffInfo.getUserType() != UserTypeEnum.ZIRANREN) {
            return Result.failed("非法用户类型");
        }

        SaveUserSalesAreaRequest request = PojoUtils.map(form, SaveUserSalesAreaRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean result = userApi.saveUserSalesArea(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "完善自然人个人信息")
    @PostMapping("/completeZiRanRenInfo")
    @Log(title = "完善自然人个人信息", businessType = BusinessTypeEnum.INSERT)
    public Result completeZiRanRenInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CompleteZiRanRenInfoForm form) {
        if (UserTypeEnum.ZIRANREN != staffInfo.getUserType()) {
            return Result.failed("用户类型非法，不允许此操作");
        }

        StaffExternaAuditDTO staffExternaAuditDTO = staffExternaAuditApi.getUserLatestAuditInfo(staffInfo.getCurrentUserId());
        if (staffExternaAuditDTO != null) {
            if (staffExternaAuditDTO.getAuditStatus() == 1) {
                return Result.failed("您的证件24小时之内审核通过，请耐心等待~");
            } else if (staffExternaAuditDTO.getAuditStatus() == 2) {
                return Result.failed("您已完善过个人信息，请勿重复操作");
            }
        }

        CreateStaffExternaAuditRequest request = PojoUtils.map(form, CreateStaffExternaAuditRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setSalesAreaAllFlag(CollUtil.isEmpty(form.getSalesAreaTree()) ? 1 : 0);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = staffExternaAuditApi.create(request);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    private Result sendVerifyCode(String mobile) {
        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_RESET_PASSWORD);
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "获取重置登录密码验证码")
    @PostMapping("/getResetPasswordVerifyCode")
    public Result getResetPasswordVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        Staff staff = staffApi.getByMobile(mobile);
        if (staff != null) {
            if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
                return Result.failed(LoginErrorCode.ACCOUNT_DISABLED);
            }

            return this.sendVerifyCode(mobile);
        }

        return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST);
    }

    @ApiOperation(value = "校验重置登录密码验证码")
    @PostMapping("/checkResetPasswordVerifyCode")
    public Result checkResetPasswordVerifyCode(@RequestBody @Valid CheckResetPasswordVerifyCodeForm form) {
        boolean result = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_RESET_PASSWORD);
        return result ? Result.success() : Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
    }

    @ApiOperation(value = "重置登录密码")
    @PostMapping("/resetPassword")
    @Log(title = "重置登录密码", businessType = BusinessTypeEnum.UPDATE)
    public Result resetPassword(@RequestBody @Valid ResetPasswordForm form) {
        boolean checkVerifyCodeResult = smsApi.checkVerifyCode(form.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_RESET_PASSWORD);
        if (!checkVerifyCodeResult) {
            return Result.failed(LoginErrorCode.VERIFY_CODE_ERROR);
        }

        Staff staff = staffApi.getByMobile(form.getMobile());
        if (staff == null) {
            return Result.failed(LoginErrorCode.ACCOUNT_NOT_EXIST);
        }

        boolean result = staffApi.updatePassword(staff.getId(), form.getPassword(), staff.getId());
        return result ? Result.success() : Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "获取换绑手机号验证码")
    @PostMapping("/getChangeMobileVerifyCode")
    public Result getChangeMobileVerifyCode(@RequestBody @Valid GetSmsVerifyCodeForm form) {
        String mobile = form.getMobile();

        boolean result = smsApi.sendVerifyCode(mobile, SmsVerifyCodeTypeEnum.SALES_ASSISTANT_CHANGE_MOBILE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("获取换绑手机号验证码失败");
        }
    }

    @ApiOperation(value = "校验换绑原手机号验证码",notes = "点击下一步时调用此接口做校验")
    @PostMapping("/checkChangeMobileVerifyCode")
    public Result checkChangeMobileVerifyCode(@RequestBody @Valid CheckResetPasswordVerifyCodeForm form) {
        String mobile = form.getMobile();

        boolean result = smsApi.checkVerifyCode(mobile, form.getVerifyCode(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_CHANGE_MOBILE);
        if (result) {
            return Result.success();
        } else {
            return Result.failed("验证码错误或失效，请重新填写");
        }
    }

    @ApiOperation(value = "校验登录密码（原手机号不可接受验证码的方式）",notes = "点击下一步时调用此接口做校验")
    @PostMapping("/checkLoginPassword")
    public Result checkLoginPassword(@RequestBody @Valid CheckLoginPasswordForm form) {
        Staff staff = Optional.ofNullable(staffApi.getByMobile(form.getOldMobile())).orElseThrow(() -> new BusinessException(LoginErrorCode.ACCOUNT_NOT_EXIST));

        boolean result = staffApi.checkPassword(staff.getId(), form.getPassword());
        if (!result) {
            return Result.failed("您填写的登录密码有误，请重新填写");
        } else {
            return Result.success();
        }

    }

    @ApiOperation(value = "确认换绑手机号")
    @PostMapping("/changeMobile")
    @Log(title = "确认换绑手机号", businessType = BusinessTypeEnum.UPDATE)
    public Result changeMobile(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ResetMobileForm form) {
        if (StrUtil.equals(form.getOldMobile(),form.getNewMobile())) {
            return Result.failed("换绑后的手机号不能与原手机号相同");
        }

        boolean checkNewVerifyCode = smsApi.checkVerifyCode(form.getNewMobile(), form.getNewVerifyCode(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_CHANGE_MOBILE);
        if (!checkNewVerifyCode) {
            return Result.failed("新手机号验证码错误或失效，请重新填写");
        }

        boolean result = userApi.changeMobile(staffInfo.getCurrentUserId(), UserAuthsAppIdEnum.MALL.getCode(), form.getNewMobile(), staffInfo.getCurrentUserId());
        if (result) {
            smsApi.cleanVerifyCode(form.getNewMobile(), SmsVerifyCodeTypeEnum.SALES_ASSISTANT_CHANGE_MOBILE);
            return Result.success();
        } else {
            return Result.failed("换绑手机号失败");
        }
    }

    @ApiOperation(value = "获取当前用户隶属的企业列表")
    @GetMapping("/getMyEnterpriseList")
    public Result<CollectionObject<SimpleEnterpriseVO>> getMyEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<EnterpriseDTO> userEnterpriseList = enterpriseApi.listByUserId(staffInfo.getCurrentUserId(), EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
        }

        // 企业开通平台信息
        List<Long> eids = userEnterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        List<EnterprisePlatformDTO> enterprisePlatformDTOList = enterpriseApi.getEnterprisePlatforms(eids);
        Map<Long, EnterprisePlatformDTO> enterprisePlatformDTOMap = enterprisePlatformDTOList.stream().collect(Collectors.toMap(EnterprisePlatformDTO::getEid, Function.identity()));

        // 企业销售区域设置
        List<EnterpriseSalesAreaDTO> enterpriseSalesAreaDTOList = enterpriseApi.listEnterpriseSalesArea(eids);
        Map<Long, EnterpriseSalesAreaDTO> enterpriseSalesAreaDTOMap = enterpriseSalesAreaDTOList.stream().collect(Collectors.toMap(EnterpriseSalesAreaDTO::getEid, Function.identity()));

        // 筛选出审核通过、非终端类型、开通销售助手、设置销售区域的企业
        userEnterpriseList = userEnterpriseList.parallelStream().filter(e -> {
            // 是否审核通过
            boolean isAuthSuccess = EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS;
            if (!isAuthSuccess) {
                return false;
            }

            // 是否非终端类型
            boolean isNotTerminal = !EnterpriseTypeEnum.getByCode(e.getType()).isTerminal();
            if (!isNotTerminal) {
                return false;
            }

            // 是否开通销售助手
            boolean hasOpenSalesAssist = enterprisePlatformDTOMap.getOrDefault(e.getId(), EnterprisePlatformDTO.newEmptyInstance()).getSalesAssistFlag() == 1;
            if (!hasOpenSalesAssist) {
                return false;
            }

            // 是否设置销售区域
            boolean hasEnterpriseSalesArea = Constants.YILING_EID.equals(e.getId()) || enterpriseSalesAreaDTOMap.containsKey(e.getId());
            if (!hasEnterpriseSalesArea) {
                return false;
            }

            return true;
        }).collect(Collectors.toList());

        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.success(new CollectionObject<>(ListUtil.empty()));
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
        // 筛选审核通过的企业
        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed("您的账号尚未加入企业");
        }

        EnterpriseDTO userEnterpriseDTO = userEnterpriseList.stream().filter(e -> e.getId().equals(eid)).findFirst().orElse(null);
        if (userEnterpriseDTO == null) {
            return Result.failed("当前用户不属于该企业");
        }

        LoginInfoVO loginInfo = new LoginInfoVO();
        // 获取用户信息
        Staff staff = staffApi.getById(staffInfo.getCurrentUserId());
        loginInfo.setUserInfo(PojoUtils.map(staff, StaffVO.class));

        LoginInfoVO.CurrentEnterpriseVO currentEnterpriseVO = PojoUtils.map(enterpriseDTO, LoginInfoVO.CurrentEnterpriseVO.class);
        // 当前企业对应的员工信息
        EnterpriseEmployeeDTO currentEnterpriseEmployeeDTO = employeeApi.getByEidUserId(eid, staff.getId());
        Long employeeId = currentEnterpriseEmployeeDTO.getId();
        currentEnterpriseVO.setEmployeeId(employeeId);
        // 用户是否为企业管理员
        boolean isAdmin = currentEnterpriseEmployeeDTO.getAdminFlag() == 1;
        currentEnterpriseVO.setAdminFlag(isAdmin);
        // 用户类型
        loginInfo.setUserType(com.yiling.framework.common.util.Constants.YILING_EID.equals(enterpriseDTO.getId()) ? 1 : 2);
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

    @ApiOperation(value = "校验注销账号", notes = "如果data为空表示校验通过，不为空表示校验不通过并返回错误信息")
    @GetMapping("/checkLogoutAccount")
    public Result<CollectionObject<UserDeregisterValidVO>> checkLogoutAccount(@CurrentUser CurrentStaffInfo staffInfo) {
        List<UserDeregisterAssistantValidDTO> deregisterValidDTOList = userDeregisterAccountMallApi.checkAssistantLogoutAccount(staffInfo.getCurrentUserId(), staffInfo.getCurrentEid(), staffInfo.getUserType());
        if (CollUtil.isNotEmpty(deregisterValidDTOList)) {
            List<UserDeregisterValidVO> deregisterValidVOList = PojoUtils.map(deregisterValidDTOList, UserDeregisterValidVO.class);
            return Result.success(new CollectionObject<>(deregisterValidVOList));
        }

        return Result.success();
    }

    @ApiOperation(value = "获取注销账号短信验证码")
    @GetMapping("/getDeregisterVerifyCode")
    public Result<Void> getDeregisterVerifyCode(@CurrentUser CurrentStaffInfo staffInfo) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        Staff staff = staffApi.getByMobile(userDTO.getMobile());
        if (Objects.nonNull(staff) && UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
            return Result.failed("手机号已被停用，请联系平台客服");
        }

        smsApi.sendVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);
        return Result.success();
    }

    @ApiOperation(value = "申请注销账号")
    @PostMapping("/applyDeregisterAccount")
    @Log(title = "申请注销账号", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> applyDeregisterAccount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddDeregisterAccountForm form) {
        UserDTO userDTO = userApi.getById(staffInfo.getCurrentUserId());
        boolean result = smsApi.checkVerifyCode(userDTO.getMobile(), form.getVerifyCode(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);
        if (!result) {
            return Result.failed("验证码错误或失效，请重新填写");
        }
        // 清理短信验证码
        smsApi.cleanVerifyCode(userDTO.getMobile(), SmsVerifyCodeTypeEnum.B2B_DEREGISTER_ACCOUNT);

        AddDeregisterAccountRequest request = new AddDeregisterAccountRequest();
        request.setUserId(staffInfo.getCurrentUserId());
        request.setSource(UserDeregisterAccountSourceEnum.SALES_ASSISTANT_APP.getCode());
        request.setTerminalType(form.getTerminalType());
        request.setApplyReason(form.getApplyReason());
        request.setOpUserId(staffInfo.getCurrentUserId());
        userDeregisterAccountApi.applyDeregisterAccount(request);

        return Result.success();
    }

}
