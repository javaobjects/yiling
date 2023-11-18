package com.yiling.erp.client.web;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiling.erp.client.util.ErpRunException;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.Utils;

@Controller
@RequestMapping({"/log"})
public class LogController {
    private static long         lastTimeFileSize = 0L;
    private final  StringBuffer builder          = new StringBuffer();
    private static Boolean      isShow           = Boolean.valueOf(true);

    @Autowired
    private InitErpConfig initErpConfig;

    @ResponseBody
    @RequestMapping(value = {"/getLogInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getLogInfo(HttpServletRequest request, HttpServletResponse response) {
        String path = InitErpConfig.LOG_PATH;
        if (isShow.booleanValue()) {
            StringBuffer buff = showLogData(path);
            return buff.toString();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = {"/cleanLogInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String cleanLogInfo(HttpServletRequest request, HttpServletResponse response) {
        Utils u = new Utils();
        u.writeNullFile(InitErpConfig.LOG_PATH);
        this.builder.delete(0, this.builder.length());

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = {"/stopLogInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String stopLogInfo(HttpServletRequest request, HttpServletResponse response) {
        isShow = Boolean.valueOf(false);
        return "success";
    }

    @ResponseBody
    @RequestMapping(value = {"/startLogInfo.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String startLogInfo(HttpServletRequest request, HttpServletResponse response) {
        isShow = Boolean.valueOf(true);
        return "success";
    }

    public StringBuffer showLogData(String str) {
        StringBuffer builder = new StringBuffer();
        try {
            RandomAccessFile randomFile = new RandomAccessFile(new File(str), "rw");
            try {
                randomFile.seek(lastTimeFileSize);
                String tmp = "";
                while ((tmp = randomFile.readLine()) != null) {
                    builder.append(new String(tmp.getBytes("ISO8859-1")) + "\r\n");
                    if (builder.length() > 9999) {
                        builder.delete(0, builder.length());
                    }
                }
                lastTimeFileSize = randomFile.length();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            new ErpRunException("日志获取失败", e);
        }
        return builder;
    }

    public void showData(String str) {
        try {
            if (this.builder.length() > 9999) {
                this.builder.delete(0, this.builder.length());
            }

            RandomAccessFile randomFile = new RandomAccessFile(new File(str), "rw");
            try {
                randomFile.seek(lastTimeFileSize);
                String tmp = "";
                while ((tmp = randomFile.readLine()) != null) {
                    this.builder.append(new String(tmp.getBytes("ISO8859-1")) + "\r\n");
                }

                lastTimeFileSize = randomFile.length();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            new ErpRunException("日志获取失败", e);
        } catch (Exception e) {
            new ErpRunException("日志获取失败", e);
        }
    }

}