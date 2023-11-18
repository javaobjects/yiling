package com.yiling.framework.oss.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.bo.OssCallbackParam;
import com.yiling.framework.oss.bo.OssCallbackResult;
import com.yiling.framework.oss.bo.OssPolicyResult;
import com.yiling.framework.oss.enums.BucketEnum;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.oss.util.FileUtil;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    OSSClient ossClient;

    @Value("${env.name}")
    private String envName;
    @Value("${aliyun.oss.endpoint:xxxxx}")
    private String endpoint;
    @Value("${aliyun.oss.protocol:http}")
    private String protocol;
    @Value("${aliyun.oss.domain:xxxxx}")
    private String domain;
    @Value("${aliyun.oss.url-expires:3600}")
    private Long expires;
    @Value("${aliyun.oss.policy-expires:300}")
    private Long policyExpires;
    @Value("${aliyun.oss.maxSize:100}")
    private Long maxSize;
    @Value("${aliyun.oss.callback:xxxxx}")
    private String callback;

    @Override
    public OssPolicyResult policy(FileTypeEnum fileType) {
        return this.policy(fileType, this.callback);
    }

    @Override
    public OssPolicyResult policy(FileTypeEnum fileType, String callbackUrl) {
        OssPolicyResult result = new OssPolicyResult();
        // 存储目录
        String dir = FileUtil.genFilePath(fileType, envName);
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + this.expires * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = this.maxSize * 1024 * 1024;
        // 回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(callbackUrl);
        callback.setCallbackBody("bucket=${bucket}&object=${object}&etag=${etag}&size=${size}&mimeType=${mimeType}&format=${imageInfo.format}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = new StringBuilder(this.protocol).append("://").append(fileType.getBucketEnum().getCode()).append(".").append(this.domain).toString();
        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes("utf-8"));
            // 返回结果
            result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(action);
        } catch (Exception e) {
            log.error("OSS签名生成失败", e);
        }
        return result;
    }

    @Override
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult result= new OssCallbackResult();

        String bucket = request.getParameter("bucket");
        result.setBucket(bucket);
        String object = request.getParameter("object");
        result.setObject(object);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));

        OSSObject ossObject = ossClient.getObject(bucket, object);
        // 文件名
        result.setName(ossObject.getObjectMetadata().getUserMetadata().getOrDefault("fileName", FileNameUtil.getName(object)));
        // 文件URL
        result.setUrl(this.getUrl(bucket, object));
        // 文件MD5
        result.setMd5(ossObject.getObjectMetadata().getContentMD5());
        // 是否重复
        result.setRepeatFlag(false);
        // 图片信息
        OssCallbackResult.ImageInfo imageInfo = new OssCallbackResult.ImageInfo();
        imageInfo.setFormat(request.getParameter("format"));
        imageInfo.setWidth(request.getParameter("width"));
        imageInfo.setHeight(request.getParameter("height"));
        result.setImageInfo(imageInfo);

        return result;
    }

    @Override
    public FileInfo upload(MultipartFile file, FileTypeEnum fileType) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(file, fileType, envName);
        ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), file.getInputStream());
        fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
        return fileInfo;
    }

    @Override
    public FileInfo upload(File file, FileTypeEnum fileType) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(file, fileType, envName);
        uploadFileToOss(file, fileType, fileInfo);
        return fileInfo;
    }

    @Override
    public FileInfo upload(InputStream inputStream, String originalFileName, FileTypeEnum fileType) throws Exception {
        FastByteArrayOutputStream fastByteArrayOutputStream = null;
        try {
            fastByteArrayOutputStream = IoUtil.read(inputStream);
            byte[] bytes = fastByteArrayOutputStream.toByteArray();

            FileInfo fileInfo = FileUtil.getFileInfo(new ByteArrayInputStream(bytes), originalFileName, fileType, envName);
            ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), new ByteArrayInputStream(bytes));
            fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
            return fileInfo;
        } finally {
            IoUtil.closeIfPosible(fastByteArrayOutputStream);
        }
    }

    @Override
    public FileInfo upload(InputStream inputStream, String originalFileName, FileTypeEnum fileType, ObjectMetadata objectMetadata) throws Exception {
        FastByteArrayOutputStream fastByteArrayOutputStream = null;
        try {
            fastByteArrayOutputStream = IoUtil.read(inputStream);
            byte[] bytes = fastByteArrayOutputStream.toByteArray();

            FileInfo fileInfo = FileUtil.getFileInfo(new ByteArrayInputStream(bytes), originalFileName, fileType, envName);
            ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), new ByteArrayInputStream(bytes),objectMetadata);
            fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
            return fileInfo;
        } finally {
            IoUtil.closeIfPosible(fastByteArrayOutputStream);
        }
    }

    @Override
    public FileInfo upload(byte[] bytes, String originalFileName, FileTypeEnum fileType,ObjectMetadata objectMetadata) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(new ByteArrayInputStream(bytes), originalFileName, fileType, envName);
        ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), new ByteArrayInputStream(bytes), objectMetadata);
        fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
        return fileInfo;
    }

    @Override
    public FileInfo get(FileTypeEnum fileTypeEnum, String key) {
        OSSObject ossObject = ossClient.getObject(fileTypeEnum.getBucketEnum().getCode(), key);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(ossObject.getObjectMetadata().getUserMetadata().getOrDefault("fileName", FileNameUtil.getName(key)));
        fileInfo.setKey(key);
        fileInfo.setIsImg(ossObject.getObjectMetadata().getContentType().startsWith("image/"));
        fileInfo.setContentType(ossObject.getObjectMetadata().getContentType());
        fileInfo.setSize(ossObject.getObjectMetadata().getContentLength());
        fileInfo.setMd5(ossObject.getObjectMetadata().getContentMD5());
        fileInfo.setUrl(this.getUrl(key, fileTypeEnum));
        fileInfo.setCreateTime(ossObject.getObjectMetadata().getLastModified());
        return fileInfo;
    }

    @Override
    public String getUrl(String key, FileTypeEnum fileType) {
        return this.getUrl(fileType.getBucketEnum().getCode(), key);
    }

    private String getUrl(String bucket, String key) {
        if (BucketEnum.getByCode(bucket).getPublicFlag()) {
            return new StringBuilder(protocol).append("://").append(bucket).append(".").append(domain).append("/").append(key).toString();
        } else {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key);
            request.setExpiration(new Date(System.currentTimeMillis() + expires * 1000));
            URL url = ossClient.generatePresignedUrl(request);
            return url != null ? StrUtil.replace(url.toString(), endpoint, domain) : null;
        }
    }

    @Override
    public String getReSizeUrl(String key, FileTypeEnum fileType) {
        if (fileType.getBucketEnum().getPublicFlag()) {
            return new StringBuilder(protocol).append("://").append(fileType.getBucketEnum().getCode()).append(".").append(domain).append("/").append(key).append("?x-oss-process=image/resize,p_40").toString();
        } else {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(fileType.getBucketEnum().getCode(), key);
            request.setExpiration(new Date(System.currentTimeMillis() + expires * 1000));
            request.setProcess("image/resize,p_40");
            URL url = ossClient.generatePresignedUrl(request);
            return url != null ? StrUtil.replace(url.toString(), endpoint, domain) : null;
        }
    }

    @Override
    public FileInfo flowUpload(File file, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) throws Exception {
        FileInfo fileInfo = FileUtil.getFlowFileInfo(file, fileType, envName, suId, suDeptNo, dateDir);
        uploadFileToOss(file, fileType, fileInfo);
        return fileInfo;
    }

    @Override
    public FileInfo fixedPathUpload(InputStream inputStream, String originalFileName, FileTypeEnum fileTypeEnum, String filePath,ObjectMetadata objectMetadata) throws Exception {
        FileInfo fileInfo = FileUtil.getFixedPathFileInfo(inputStream,originalFileName,fileTypeEnum,this.envName,filePath);
        ossClient.putObject(fileTypeEnum.getBucketEnum().getCode(), fileInfo.getKey(), inputStream, objectMetadata);
        fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileTypeEnum));
        return fileInfo;
    }

    @Override
    public String getFixedUrl(String originalFileName, String filePath, FileTypeEnum fileType) {
        String key = FileUtil.genFixedPathKey(originalFileName, fileType, this.envName, filePath);
        return getUrl(key,fileType);
    }

    private void uploadFileToOss(File file, FileTypeEnum fileType, FileInfo fileInfo) {
        ossClient.putObject(fileType.getBucketEnum().getCode(), fileInfo.getKey(), cn.hutool.core.io.FileUtil.getInputStream(file));
        fileInfo.setUrl(this.getUrl(fileInfo.getKey(), fileType));
    }
}
