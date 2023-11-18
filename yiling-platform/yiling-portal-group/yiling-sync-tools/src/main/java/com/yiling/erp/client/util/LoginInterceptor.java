package com.yiling.erp.client.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author shuan
 */
public class LoginInterceptor
        implements HandlerInterceptor {
    private List<String> excludeUrls;

    public List<String> getExcludeUrls() {
        return this.excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestUri = request.getRequestURI();
        if (null != this.excludeUrls) {
            for (String uri : this.excludeUrls) {
                if (requestUri.contains(uri)) {
                    return true;
                }
            }
        }
        HttpSession httpSession = request.getSession();
//        LOG.debug("request url:" + request.getRequestURL());

        if (null != httpSession.getAttribute("userInfo")) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/login/userLogin.htm");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}