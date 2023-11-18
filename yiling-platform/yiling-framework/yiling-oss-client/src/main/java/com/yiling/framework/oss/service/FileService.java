package com.yiling.framework.oss.service;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.ObjectMetadata;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.bo.OssCallbackResult;
import com.yiling.framework.oss.bo.OssPolicyResult;
import com.yiling.framework.oss.enums.FileTypeEnum;

/**
 * 文件 Service
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
public interface FileService {

    /**
     * 获取oss上传策略
     *
     * @param fileType
     * @return com.yiling.framework.oss.bo.OssPolicyResult
     * @author xuan.zhou
     * @date 2022/6/8
     **/
    OssPolicyResult policy(FileTypeEnum fileType);

    /**
     * 生成oss上传策略（支持自定义回调）
     *
     * @param fileType
     * @param callbackUrl
     * @return com.yiling.framework.oss.bo.OssPolicyResult
     * @author xuan.zhou
     * @date 2023/3/18
     **/
    OssPolicyResult policy(FileTypeEnum fileType, String callbackUrl);

    /**
     * oss上传成功回调
     *
     * @param request HttpServletRequest
     * @return com.yiling.framework.oss.bo.OssCallbackResult
     * @author xuan.zhou
     * @date 2022/6/8
     **/
    OssCallbackResult callback(HttpServletRequest request);

    FileInfo upload(MultipartFile file, FileTypeEnum fileType) throws Exception;

    FileInfo upload(File file, FileTypeEnum fileTypeEnum) throws Exception;

    FileInfo upload(InputStream inputStream, String originalFileName, FileTypeEnum fileType) throws Exception;

    FileInfo upload(InputStream inputStream, String originalFileName, FileTypeEnum fileType,ObjectMetadata objectMetadata) throws Exception;

    FileInfo upload(byte[] bytes, String originalFileName, FileTypeEnum fileType, ObjectMetadata objectMetadata) throws Exception;

    FileInfo get(FileTypeEnum fileTypeEnum, String key);

    String getUrl(String key, FileTypeEnum fileType);

    /**
     * 从oss获取缩小50%的图片地址
     *
     * @param key   图片存储的key值
     * @param fileType  图片类型
     * @return  图片地址
     */
    String getReSizeUrl(String key, FileTypeEnum fileType);

    /**
     * 流向文件上传方法
     *
     * @param file 文件
     * @param fileTypeEnum 类型
     * @param envName 类型
     * @param suId 供应商id
     * @param suDeptNo 供应商部门编号
     * @param dateDir 日期，yyyy/MM/dd
     * @return
     * @throws Exception
     */
    FileInfo flowUpload(File file, FileTypeEnum fileTypeEnum, String envName, String suId, String suDeptNo, String dateDir) throws Exception;

    /**
     * 固定路径上传oss
     *
     * @param inputStream
     * @param originalFileName
     * @param fileTypeEnum
     * @param filePath
     * @return
     * @throws Exception
     */
    FileInfo fixedPathUpload(InputStream inputStream, String originalFileName, FileTypeEnum fileTypeEnum, String filePath, ObjectMetadata objectMetadata) throws Exception;

    /**
     * 获取固定地址
     *
     * @param originalFileName
     * @param filePath
     * @param fileType
     * @return
     */
    String getFixedUrl(String originalFileName, String filePath, FileTypeEnum fileType);
}
