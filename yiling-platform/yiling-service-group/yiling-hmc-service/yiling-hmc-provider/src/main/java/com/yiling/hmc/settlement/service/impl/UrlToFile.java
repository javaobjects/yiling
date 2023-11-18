package com.yiling.hmc.settlement.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: yong.zhang
 * @date: 2022/7/6
 */
@Slf4j
public class UrlToFile {
    /**
     * url资源转化为file流
     *
     * @param url
     * @return
     */
    public static File urlToFile(URL url) {
        InputStream is = null;
        File file;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile("tmp", "." + FileTypeUtil.getType(url.openStream()));
            URLConnection urlConn = url.openConnection();
            is = urlConn.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            return file;
        } catch (IOException e) {
            log.error("下载远程文件出错，错误信息:{}", ExceptionUtils.getFullStackTrace(e), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("下载远程文件出错，错误信息:{}", ExceptionUtils.getFullStackTrace(e), e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("下载远程文件出错，错误信息:{}", ExceptionUtils.getFullStackTrace(e), e);
                }
            }
        }
        return null;
    }

//
//    public static void main(String[] args) throws IOException {
//
//        String  str = "https://yl-private-file.oss-cn-zhangjiakou.aliyuncs.com/test/idCardFrontPhoto/2022/08/03/0247f857cb7b4e06ae28ebfd2edf97f1.png?Expires=1660103087&OSSAccessKeyId=LTAI5tQBbZ3LEAHjVzYAkw4C&Signature=gser0SjTb76P7te8F52UJY%2BJ%2BCU%3D&x-oss-process=image%2Fresize%2Cp_40";
//        URL url = new URL(str);
//
//        String fileType = FileTypeUtil.getType(new URL(str).openStream());
//        System.out.println("fileType:" + fileType);
//        if(null == fileType){
//            log.info("远程文件类型无法识别");
//
//        }
//
//        InputStream is = null;
//        File file = null;
//        FileOutputStream fos = null;
//        try {
//            file = File.createTempFile("tmp", "."+FileTypeUtil.getType(new URL(str).openStream()));
//            URLConnection urlConn = null;
//            urlConn = url.openConnection();
//            is = urlConn.getInputStream();
//            fos = new FileOutputStream(file);
//            byte[] buffer = new byte[4096];
//            int length;
//            while ((length = is.read(buffer)) > 0) {
//                fos.write(buffer, 0, length);
//            }
//            System.out.println(file.getAbsolutePath());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                }
//            }
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }
}
