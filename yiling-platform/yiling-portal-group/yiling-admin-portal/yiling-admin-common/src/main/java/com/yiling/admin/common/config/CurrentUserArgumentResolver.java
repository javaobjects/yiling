package com.yiling.admin.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(CurrentAdminInfo.class)
            && parameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String currentUserId = webRequest.getParameter(CurrentAdminInfo.CURRENT_USER_ID);
        if (StrUtil.isEmpty(currentUserId)) {
            currentUserId = webRequest.getHeader(Constants.CURRENT_USER_ID);
        }

        // 未登录情况
        if (StrUtil.isEmpty(currentUserId)) {
            return null;
        }

        CurrentAdminInfo currentAdminInfo = new CurrentAdminInfo();
        currentAdminInfo.setCurrentUserId(Convert.toLong(currentUserId));

        return currentAdminInfo;
    }
}