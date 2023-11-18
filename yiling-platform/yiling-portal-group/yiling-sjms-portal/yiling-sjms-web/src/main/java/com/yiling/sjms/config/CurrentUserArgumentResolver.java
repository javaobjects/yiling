package com.yiling.sjms.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(CurrentSjmsUserInfo.class)
            && parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Long currentUserId = Convert.toLong(webRequest.getParameter(CurrentSjmsUserInfo.CURRENT_USER_ID), 0L);
        if (currentUserId == 0L) {
            currentUserId = Convert.toLong(webRequest.getHeader(Constants.CURRENT_USER_ID), 0L);
        }

        String currentUserCode = webRequest.getParameter(CurrentSjmsUserInfo.CURRENT_USER_CODE);
        if (StrUtil.isEmpty(currentUserCode)) {
            currentUserCode = webRequest.getHeader(Constants.CURRENT_USER_CODE);
        }

        // 未登录情况
        if (currentUserId == 0L) {
            return null;
        }

        CurrentSjmsUserInfo currentSjmsUserInfo = new CurrentSjmsUserInfo();
        currentSjmsUserInfo.setCurrentUserId(currentUserId);
        currentSjmsUserInfo.setCurrentUserCode(currentUserCode);
        return currentSjmsUserInfo;
    }
}