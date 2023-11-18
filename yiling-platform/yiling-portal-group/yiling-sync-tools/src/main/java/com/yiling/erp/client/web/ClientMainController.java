package com.yiling.erp.client.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shuan
 */
@Controller
@RequestMapping({"/erp"})
public class ClientMainController {

    @RequestMapping(value = {"/clientMain.htm"}, produces = {"text/html;charset=UTF-8"})
    public String client(HttpServletRequest request, HttpServletResponse response) {
        return "clientMain";
    }
}
