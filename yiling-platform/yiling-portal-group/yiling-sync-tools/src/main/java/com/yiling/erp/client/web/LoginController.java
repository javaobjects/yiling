package com.yiling.erp.client.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiling.erp.client.common.UserInfo;
import com.yiling.erp.client.util.ReadProperties;

/**
 * @author shuan
 */
@Controller
@RequestMapping({"/login"})
public class LoginController {

    @RequestMapping(value = {"/userLogin.htm"}, produces = {"text/html;charset=UTF-8"})
    public String userLogin(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = {"/verify.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String verify(HttpServletRequest request, HttpServletResponse response) {

        String loginAccount = request.getParameter("account");
        String loginPassWord = request.getParameter("password");
//        loger.info("用户: " + loginAccount + "登录成功，欢迎使用对接工具！！！");
        String account = ReadProperties.getProperties("account");
        String passWord = ReadProperties.getProperties("password");
        if ((loginAccount.trim().equals(account)) && (loginPassWord.trim().equals(passWord))) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(account);
            userInfo.setPassWord(passWord);
            request.getSession().setAttribute("userInfo", userInfo);
            return "success";
        }
        return "false";
    }

    @RequestMapping({"/logout.htm"})
    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        session.removeAttribute("userInfo");
        response.sendRedirect(request.getContextPath() + "/login/userLogin.htm");
    }
}