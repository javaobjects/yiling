package com.yiling.erp.client.util;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.yiling.erp.client.enums.FileTypeEnum;
import com.yiling.open.erp.dto.HeartParamDTO;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/18
 */
@Component
@Slf4j
public class OssManager {

    @Autowired
    private InitErpConfig initErpConfig;

    public OssFileInfo flowUpload(File file, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) {
        HeartParamDTO heartParamDTO = initErpConfig.getHeartParamDTO();
        if(heartParamDTO==null){
            log.error("oss配置信息为空");
            return null;
        }
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setProtocol(Protocol.HTTPS);
        conf.setConnectionTimeout(300*1000);
        conf.setMaxErrorRetry(3);
        conf.setRequestTimeoutEnabled(true);
        OSS ossClient = new OSSClientBuilder().build(heartParamDTO.getDomain(), heartParamDTO.getAccessKey(), heartParamDTO.getAccessKeySecret(), conf);
        try {
            OssFileInfo fileInfo = getFlowFileInfo(file, fileType, envName, suId, suDeptNo, dateDir);
            ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), cn.hutool.core.io.FileUtil.getInputStream(file));
            fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
            return fileInfo;
        } catch (OSSException oe) {
            log.error("ossClient连接信息, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("ossClient连接信息, Error Message:{}", oe.getErrorMessage());
            log.error("ossClient连接信息, Error Code:{}", oe.getErrorCode());
            log.error("ossClient连接信息, Request ID:{}", oe.getRequestId());
            log.error("ossClient连接信息, Host ID:{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("Error Message:{}", ce.getMessage());
        } catch (Exception e) {
            log.error("oss连接错误", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    public static OssFileInfo getFlowFileInfo(File file, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) throws Exception {
        OssFileInfo fileInfo = new OssFileInfo();
        fileInfo.setKey(genFlowFileKey(file.getName(), fileType, envName, suId, suDeptNo, dateDir));
        buildFileInfo(file, fileInfo);
        return fileInfo;
    }

    private static void buildFileInfo(File file, OssFileInfo fileInfo) {
        fileInfo.setName(file.getName());
        fileInfo.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
        fileInfo.setIsImg(false);
        fileInfo.setSize(file.length());
        fileInfo.setMd5(fileMd5(cn.hutool.core.io.FileUtil.readBytes(file)));
        fileInfo.setCreateTime(new Date());
    }

    /**
     * 计算文件的MD5值
     *
     * @param bytes
     * @return
     */
    public static String fileMd5(byte[] bytes) {
        return BinaryUtil.toBase64String(BinaryUtil.calculateMd5(bytes));
    }

    public static String genFlowFileKey(String fileName, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) {
        String fileKey = IdUtil.fastSimpleUUID();
        String fileSuffix = FileNameUtil.getSuffix(fileName).toLowerCase();
        StringBuilder builder = new StringBuilder();
        builder.append(envName).append("/").append(fileType.getType()).append("/").append(suId);
        if (StrUtil.isNotBlank(suDeptNo)) {
            builder.append("/").append(suDeptNo);
        }
        builder.append("/").append(dateDir).append("/").append(fileKey).append(".").append(fileSuffix);
        return builder.toString();
    }

    public String getUrl(String key, FileTypeEnum fileType) {
        HeartParamDTO heartParamDTO = initErpConfig.getHeartParamDTO();
        if(heartParamDTO==null){
            log.error("oss配置信息为空");
            return null;
        }
        if (fileType.getBucketEnum().getPublicFlag()) {
            return new StringBuilder(heartParamDTO.getProtocol()).append("://").append(fileType.getBucketEnum().getCode()).append(".").append(heartParamDTO.getDomain()).append("/").append(key).toString();
        }
        return null;
    }

}
