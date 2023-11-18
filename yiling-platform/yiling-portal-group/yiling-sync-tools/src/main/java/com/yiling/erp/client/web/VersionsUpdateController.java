package com.yiling.erp.client.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.websocket.ErpWebSocket;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.SysConfig;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.erp.client.util.PopRequest;

/**
 * @author shuan
 */
@Controller
@RequestMapping({"/erp"})
public class VersionsUpdateController {

    @Autowired
    private InitErpConfig initErpConfig;

    @RequestMapping(value = {"/versionsUpdate.htm"}, produces = {"text/html;charset=UTF-8"})
    public String versionsUpdate(HttpServletRequest request, HttpServletResponse response) {
        return "versionsUpdate";
    }

    @ResponseBody
    @RequestMapping(value = {"/checkVersion.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String checkVersion(HttpServletRequest request, HttpServletResponse response) {
        String res = "";
        try {
            SysConfig sysConfig = initErpConfig.getSysConfig();
            PopRequest pr = new PopRequest();
            Map<String, String> headParams = pr.generateHeadMap(ErpTopicName.ErpToolVersion.getMethod(), sysConfig.getKey());

            String returnValue = pr.getPost(sysConfig.getUrlPath(), sysConfig.getVersion(), headParams, sysConfig.getSecret());
            if ((returnValue != null) && (!returnValue.equals(""))) {
                Result apiResponse = JSONObject.parseObject(returnValue, Result.class);
                Integer code = apiResponse.getCode();
                String data = String.valueOf(apiResponse.getData());
                if (200 == code && StringUtils.isNotEmpty(data)) {
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    if(jsonObject==null){
                        return "";
                    }
                    String ver = jsonObject.get("version").toString();
                    if (!ver.equals(sysConfig.getVersion())) {
                        res = data;
                    }
                }
            }
        }catch (Exception e){
//            loger.error("获取版本信息错误",e);
            e.printStackTrace();
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = {"/upgrade.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public void upgrade(HttpServletRequest request, HttpServletResponse response) {
        try {
            String downUrl = request.getParameter("downUrl");

            //先判断文件是否存在，如果存在就删除，不存在就创建
            File file = new File(InitErpConfig.PATH + "/download/");
            //创建文件夹
            if (!file.exists()) {
                file.mkdir();
            }
            //删除原文件信息
            File toolsFile = new File(file.getPath() + File.separator + "rk-client-web.war");
            if (toolsFile.exists()) {
                toolsFile.delete();
            }

            //创建文件信息
            File createFile = new File(file.getPath() + File.separator + "rk-client-web.war");
            if (!createFile.exists()) {
                createFile.createNewFile();
                //设置可读权限
                createFile.setReadable(true,false);
                //设置可写权限
                createFile.setWritable(true,false);
            }


            SysConfig sysConfig = initErpConfig.getSysConfig();
            InputStream inputStream = PopRequest.downloadFile(downUrl);
            OutputStream out = new FileOutputStream(createFile);
            IOUtils.copy(inputStream, out);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(out);
            ErpWebSocket.sendInfo("1");
        } catch (Exception e) {
//            loger.error("版本升级错误", e);
            e.printStackTrace();
        }
    }


    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
        String downUrl = request.getParameter("downUrl");
        InputStream inputStream = PopRequest.downloadFile(downUrl);

        //先判断文件是否存在，如果存在就删除，不存在就创建
        File file = new File(InitErpConfig.PATH + "/download/");
        //创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        //删除原文件信息
        File toolsFile = new File(file.getPath() + File.separator + "rk-client-web.war");
        if (toolsFile.exists()) {
            toolsFile.delete();
        }

        OutputStream out = new FileOutputStream(toolsFile);
        IOUtils.copy(inputStream, out);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(out);

        byte[] body = null;
        InputStream is = new FileInputStream(toolsFile);
        body = new byte[is.available()];
        is.read(body);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + toolsFile.getName());
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }
}
