package com.yiling.sales.assistant.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(CurrentStaffInfo.class)
            && parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Long currentUserId = Convert.toLong(webRequest.getParameter(CurrentStaffInfo.CURRENT_USER_ID), 0L);
        if (currentUserId == 0L) {
            currentUserId = Convert.toLong(webRequest.getHeader(Constants.CURRENT_USER_ID), 0L);
        }

        // 未登录情况
        if (currentUserId == 0L) {
            return null;
        }

        Long currentEid = Convert.toLong(webRequest.getParameter(CurrentStaffInfo.CURRENT_EID), 0L);
        if (currentEid == 0L) {
            currentEid = Convert.toLong(webRequest.getHeader(Constants.CURRENT_EID), 0L);
        }

        // 用户类型
        String userType = webRequest.getParameter(CurrentStaffInfo.USER_TYPE);
        if (StrUtil.isEmpty(userType)) {
            userType = webRequest.getHeader(Constants.CURRENT_USER_TYPE);
        }

        CurrentStaffInfo currentStaffInfo = new CurrentStaffInfo();
        // 应用类型
        Integer appId = Convert.toInt(webRequest.getHeader(Constants.CURRENT_APP_ID));
        currentStaffInfo.setAppEnum(AppEnum.getByAppId(appId));
        // 用户ID
        currentStaffInfo.setCurrentUserId(currentUserId);
        // 企业ID
        currentStaffInfo.setCurrentEid(currentEid);
        currentStaffInfo.setUserType(UserTypeEnum.getByCode(Convert.toInt(userType)));
        // 企业类型
        Integer etype = Convert.toInt(webRequest.getHeader(Constants.CURRENT_ETYPE));
        currentStaffInfo.setEnterpriseType(EnterpriseTypeEnum.getByCode(etype));
        // 渠道类型
        Long channelId = Convert.toLong(webRequest.getHeader(Constants.CURRENT_CHANNEL_ID));
        currentStaffInfo.setEnterpriseChannel(EnterpriseChannelEnum.getByCode(channelId));
        // 员工ID
        Long employeeId = Convert.toLong(webRequest.getHeader(Constants.CURRENT_EMPLOYEE_ID));
        currentStaffInfo.setCurrentEmployeeId(employeeId);
        // 是否是企业管理员
        Boolean isAdmin = Convert.toBool(webRequest.getHeader(Constants.CURRENT_IS_ADMIN), false);
        currentStaffInfo.setAdminFlag(isAdmin);

        return currentStaffInfo;
    }
}