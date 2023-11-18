package com.yiling.open.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.enums.OpenErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shuang.zhang
 * @Email: shuang.zhang@rograndec.com
 * @CreateTime: 2019-3-6
 * @Version: 1.0
 */
@RestController
@Slf4j
public class LogController  {

    @RequestMapping(path = "/test/log")
    public Result<String> billInvoice(HttpServletRequest request,
                                      HttpServletResponse response
    ) {
        log.error("日志输出");
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }

}
