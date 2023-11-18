package com.yiling.user.web.auth.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.IErrorCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.web.auth.enums.LoginErrorCode;
import com.yiling.user.web.config.B2BLoginConfig;

import cn.hutool.core.collection.CollUtil;

/**
 * B2B 认证基类 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/4/7
 */
public class BaseB2BAuthController extends BaseAuthController {

    @Autowired
    private B2BLoginConfig loginConfig;

    protected Result<List<EnterpriseDTO>> validateUserEnterpriseList(List<EnterpriseDTO> userEnterpriseList) {
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_EMPTY);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseStatusEnum.getByCode(e.getStatus()) == EnterpriseStatusEnum.ENABLED).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_DISABLED);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseAuthStatusEnum.getByCode(e.getAuthStatus()) == EnterpriseAuthStatusEnum.AUTH_SUCCESS).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.ENTERPRISE_UNAUDITED);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> this.isValidEnterpriseType(e)).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.B2B_HOSPITAL_NOT_ALLOWED);
        }

        userEnterpriseList = userEnterpriseList.stream().filter(e -> EnterpriseTypeEnum.getByCode(e.getType()).isTerminal()).collect(Collectors.toList());
        if (CollUtil.isEmpty(userEnterpriseList)) {
            return Result.failed(LoginErrorCode.PLEASE_LOGIN_AT_POP);
        }

        return Result.success(userEnterpriseList);
    }

    protected boolean isValidEnterpriseType(EnterpriseDTO enterpriseDTO) {
        if (EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()) == EnterpriseTypeEnum.HOSPITAL) {
            if (!loginConfig.getEnabledHospitalEids().contains(enterpriseDTO.getId())) {
                return false;
            }
        }

        return true;
    }

}
