package com.yiling.common.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户访问认证拦截器
 *
 * @author xuan.zhou
 * @date 2021/6/18
 */
@Slf4j
public class UserAccessAuthenticationInterceptor implements HandlerInterceptor {

    private static final String OPTIONS   = "OPTIONS";
    private static final String AUTH_PATH = "/error";

    @DubboReference
    EmployeeApi   employeeApi;
    @DubboReference
    StaffApi      staffApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.contains(AUTH_PATH)) {
            return true;
        }

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        UserAccessAuthentication userAccessAuthentication = this.getUserAccessAuthenticationAnnotation(handler);
        if (userAccessAuthentication == null) {
            return true;
        }

        Long userId = Convert.toLong(request.getParameter(CurrentStaffInfo.CURRENT_USER_ID));
        if (userId == null || userId == 0L) {
            userId = Convert.toLong(request.getHeader(Constants.CURRENT_USER_ID));
        }

        // 未登录
        if (userId == null || userId == 0L) {
            throw new BusinessException(UserErrorCode.NOT_LOGIN);
        }

        Long eid = Convert.toLong(request.getParameter(CurrentStaffInfo.CURRENT_EID));
        if (eid == null || eid == 0L) {
            eid = Convert.toLong(request.getHeader(Constants.CURRENT_EID));
        }

        // 未登录
        if (eid == null || eid == 0L) {
            throw new BusinessException(UserErrorCode.NOT_LOGIN);
        }

        if (userAccessAuthentication.checkUserStatus()) {
            EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(eid, userId);
            if (employeeDTO == null) {
                throw new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS);
            } else if (EnableStatusEnum.getByCode(employeeDTO.getStatus()) == EnableStatusEnum.DISABLED) {
                throw new BusinessException(UserErrorCode.EMPLOYEE_DISABLED);
            }

            Staff staff = staffApi.getById(userId);
            if (staff == null) {
                throw new BusinessException(UserErrorCode.STAFF_NOT_EXISTS);
            } else if (UserStatusEnum.getByCode(staff.getStatus()) == UserStatusEnum.DISABLED) {
                throw new BusinessException(UserErrorCode.STAFF_DISABLED);
            }
        }

        if (userAccessAuthentication.checkEnterpriseStatus() || userAccessAuthentication.checkEnterpriseAuthStatus()) {
            EnterpriseDTO enterpriseInfo = enterpriseApi.getById(eid);
            if (enterpriseInfo == null) {
                throw new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS);
            } else {
                if (userAccessAuthentication.checkEnterpriseStatus() && EnterpriseStatusEnum.getByCode(enterpriseInfo.getStatus()) == EnterpriseStatusEnum.DISABLED) {
                    throw new BusinessException(UserErrorCode.ENTERPRISE_DISABLED);
                }

                if (userAccessAuthentication.checkEnterpriseAuthStatus() && EnterpriseAuthStatusEnum.getByCode(enterpriseInfo.getAuthStatus()) != EnterpriseAuthStatusEnum.AUTH_SUCCESS) {
                    throw new BusinessException(UserErrorCode.ENTERPRISE_NOT_AUTH_SUCCESS);
                }
            }
        }

        if (userAccessAuthentication.checkUserEnterpriseRelation()) {
            boolean result = employeeApi.exists(eid, userId);
            if (!result) {
                throw new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private UserAccessAuthentication getUserAccessAuthenticationAnnotation(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        return AnnotationUtils.findAnnotation(method, UserAccessAuthentication.class);
    }

}
