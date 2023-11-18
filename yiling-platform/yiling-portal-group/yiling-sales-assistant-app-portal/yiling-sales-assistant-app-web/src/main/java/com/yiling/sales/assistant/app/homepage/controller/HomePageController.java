package com.yiling.sales.assistant.app.homepage.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.app.homepage.form.InformationForm;
import com.yiling.sales.assistant.app.homepage.vo.MyInfoVO;
import com.yiling.sales.assistant.information.api.InformationApi;
import com.yiling.sales.assistant.information.dto.InformationRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手-我的首页Controller
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Slf4j
@RestController
@RequestMapping("/homePage")
@Api(tags = "我的首页")
public class HomePageController extends BaseController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    InformationApi informationApi;

    @ApiOperation(value = "个人信息")
    @GetMapping("/getMyInfo")
    public Result<MyInfoVO> getMyInfo(@CurrentUser CurrentStaffInfo staffInfo) {
        // 获取用户信息
        UserDTO userDTO = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElseThrow(() -> new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS));
        MyInfoVO myInfoVO = new MyInfoVO();
        myInfoVO.setUserId(staffInfo.getCurrentUserId());
        myInfoVO.setName(userDTO.getName());
        myInfoVO.setMobile(userDTO.getMobile());

        return Result.success(myInfoVO);
    }

    @ApiOperation(value = "信息反馈")
    @PostMapping("/saveInformation")
    public Result<Boolean> saveInformation(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid InformationForm form) {
        InformationRequest request = PojoUtils.map(form,InformationRequest.class);

        return Result.success(informationApi.saveInformation(request));
    }

}
