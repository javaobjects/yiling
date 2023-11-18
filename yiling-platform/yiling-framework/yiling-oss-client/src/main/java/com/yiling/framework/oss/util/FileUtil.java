package com.yiling.framework.oss.util;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.common.utils.BinaryUtil;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件工具类
 */
@Slf4j
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static FileInfo getFileInfo(MultipartFile file, FileTypeEnum fileType, String envName) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(genFileKey(file.getOriginalFilename(), fileType, envName));
        fileInfo.setName(file.getOriginalFilename());
        fileInfo.setContentType(file.getContentType());
        fileInfo.setIsImg(fileInfo.getContentType().startsWith("image/"));
        fileInfo.setSize(file.getSize());
        fileInfo.setMd5(fileMd5(file.getBytes()));
        fileInfo.setCreateTime(new Date());
        return fileInfo;
    }

    public static FileInfo getFileInfo(File file, FileTypeEnum fileType, String envName) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(genFileKey(file.getName(), fileType, envName));
        buildFileInfo(file, fileInfo);
        return fileInfo;
    }

    public static FileInfo getFlowFileInfo(File file, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(genFlowFileKey(file.getName(), fileType, envName, suId, suDeptNo, dateDir));
        buildFileInfo(file, fileInfo);
        return fileInfo;
    }
    public static FileInfo getFixedPathFileInfo(InputStream inputStream, String originalFileName, FileTypeEnum fileType, String envName,String filePath) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(genFixedPathKey(originalFileName, fileType, envName,filePath));
        fileInfo.setName(originalFileName);
        fileInfo.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
        fileInfo.setIsImg(false);
        fileInfo.setSize(inputStream.available());
        fileInfo.setCreateTime(new Date());
        return fileInfo;
    }

    private static void buildFileInfo(File file, FileInfo fileInfo) {
        fileInfo.setName(file.getName());
        fileInfo.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
        fileInfo.setIsImg(false);
        fileInfo.setSize(file.length());
        fileInfo.setMd5(fileMd5(cn.hutool.core.io.FileUtil.readBytes(file)));
        fileInfo.setCreateTime(new Date());
    }

    public static FileInfo getFileInfo(InputStream inputStream, String originalFileName, FileTypeEnum fileType, String envName) throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setKey(genFileKey(originalFileName, fileType, envName));
        fileInfo.setName(originalFileName);
        fileInfo.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
        fileInfo.setIsImg(false);
        fileInfo.setSize(inputStream.available());
//        fileInfo.setMd5(fileMd5(inputStream));
        fileInfo.setCreateTime(new Date());
        return fileInfo;
    }

    public static String genFilePath(FileTypeEnum fileType, String envName) {
        String dateDir = DateUtil.format(new Date(), "yyyy/MM/dd");
        return new StringBuilder().append(envName).append("/").append(fileType.getType()).append("/").append(dateDir).toString();
    }

    public static String genFileKey(String fileName, FileTypeEnum fileType, String envName) {
        String dateDir = DateUtil.format(new Date(), "yyyy/MM/dd");
        String fileKey = IdUtil.fastSimpleUUID();
        String fileSuffix = FileNameUtil.getSuffix(fileName).toLowerCase();
        return new StringBuilder().append(envName).append("/").append(fileType.getType()).append("/").append(dateDir)
            .append("/").append(fileKey).append(".").append(fileSuffix).toString();
    }

    public static String genFlowFileKey(String fileName, FileTypeEnum fileType, String envName, String suId, String suDeptNo, String dateDir) {
        String fileKey = IdUtil.fastSimpleUUID();
        String fileSuffix = FileNameUtil.getSuffix(fileName).toLowerCase();
        StringBuilder builder = new StringBuilder();
        builder.append(envName).append("/").append(fileType.getType()).append("/").append(suId);
        if(StrUtil.isNotBlank(suDeptNo)){
            builder.append("/").append(suDeptNo);
        }
        builder.append("/").append(dateDir).append("/").append(fileKey).append(".").append(fileSuffix);
        return builder.toString();
    }

    public static String genFixedPathKey(String fileName, FileTypeEnum fileType, String envName,String filePath){
        StringBuilder builder = new StringBuilder();
        builder.append(envName).append("/").append(fileType.getType())
                .append("/").append(filePath).append("/").append(fileName);
        return builder.toString();
    }

    /**
     * 计算文件的MD5值
     *
     * @param inputStream
     * @return
     */
    public static String fileMd5(InputStream inputStream) {
        return fileMd5(IoUtil.readBytes(inputStream));
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
}
