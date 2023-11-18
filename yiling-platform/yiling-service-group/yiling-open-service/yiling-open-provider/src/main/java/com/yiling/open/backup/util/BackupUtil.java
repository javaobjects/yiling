package com.yiling.open.backup.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.google.common.collect.Lists;
import com.yiling.framework.oss.enums.BucketEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Slf4j
public class BackupUtil {

    public static final String FORMATE_YEAR_ONLY = "yyyy";
    public static final String FORMATE_MONTH_ONLY = "MM";
    public static final String FORMATE_MONTH = "yyyy-MM";
    public static final String FORMATE_MONTH_1 = "yyyyMM";
    public static final String FORMATE_SECOND = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.CHINA);

    public static String monthBackup(Integer flowDateCount) {
        Integer count = flowDateCount - 1;
        List<String> monthStrList = new ArrayList<>();
        DateTime lastMonth = DateUtil.lastMonth();
        Date startMonth = DateUtil.offset(lastMonth, DateField.MONTH, count * -1);
        return DateUtil.format(startMonth, FORMATE_MONTH);
    }

    /**
     * 列举文件列表
     * 根据目录地址获取此目录下的所有子目录和子文件，例如：src/
     *
     * @param endpoint 区域站点
     * @param accessKey 账号
     * @param accessKeySecret 密匙
     * @param prefix 目录key
     */
    public static List<String> getOssOneLevelDirectoryAndFileByKey(String endpoint, String accessKey, String accessKeySecret, String prefix) {
        // Bucket名称。
        String bucketName = BucketEnum.PUBLIC.getCode();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        // 指定每页列举200个文件。
        int maxKeys = 200;
        String nextMarker = null;
        ObjectListing objectListing = null;
        List<String> list = new ArrayList<>();
        try {
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker);
                // 设置正斜线（/）为文件夹的分隔符。仅获取下一级子目录、子文件，子目录的子文件不获取
                listObjectsRequest.setDelimiter("/");
                // 列出fun目录下的所有文件和文件夹。
                listObjectsRequest.setPrefix(prefix);
                // 每页列举的文件最大数
                listObjectsRequest.setMaxKeys(maxKeys);
                objectListing = ossClient.listObjects(listObjectsRequest);
                if (ObjectUtil.isNull(objectListing) || (objectListing.getObjectSummaries().size() == 0 && objectListing.getCommonPrefixes().size() == 0)) {
                    break;
                }

                // 遍历所有文件。
                //            System.out.println("file:");
                // objectSummaries的列表中给出的是 prefix 目录下的文件。
                for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    if (!objectSummary.getKey().endsWith("/")) {
                        list.add(objectSummary.getKey());
                    }
                }

                // 遍历所有目录。
                //            System.out.println("\ndirectory:");
                // commonPrefixs列表中显示的是 prefix 目录下的所有子目录。因此属于 prefix 子目录下的文件和子目录，不会在列表中。
                for (String commonPrefix : objectListing.getCommonPrefixes()) {
                    // 去掉当前目录，只列举子目录、子文件
                    if (!ObjectUtil.equal(prefix, commonPrefix)) { // 不需判断
                        list.add(commonPrefix);
                    }
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } catch (OSSException oe) {
            log.error("getOssDirectoryAndFileByUrl, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:：{}", oe.getErrorMessage());
            log.error("getOssDirectoryAndFileByUrl, Error Code:：{}", oe.getErrorCode());
            log.error("getOssDirectoryAndFileByUrl, Request ID:：{}", oe.getRequestId());
            log.error("getOssDirectoryAndFileByUrl, Host ID:：{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("getOssDirectoryAndFileByUrl, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return list;
    }

    /**
     * 列举指定目录下所有文件，不包括子目录的文件
     * 根据目录地址获取此目录下的所有子文件，例如：src/
     *
     * @param endpoint 区域站点
     * @param accessKey 账号
     * @param accessKeySecret 密匙
     * @param prefix 目录key
     */
    public static List<String> getOssOneLevelFileByKey(String endpoint, String accessKey, String accessKeySecret, String prefix) {
        // Bucket名称。
        String bucketName = BucketEnum.PUBLIC.getCode();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        // 指定每页列举200个文件。
        int maxKeys = 200;
        String nextMarker = null;
        ObjectListing objectListing = null;
        List<String> list = new ArrayList<>();
        try {
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker);
                // 设置正斜线（/）为文件夹的分隔符。仅获取下一级子目录、子文件，子目录的子文件不获取
                listObjectsRequest.setDelimiter("/");
                // 列出fun目录下的所有文件和文件夹。
                listObjectsRequest.setPrefix(prefix);
                // 每页列举的文件最大数
                listObjectsRequest.setMaxKeys(maxKeys);
                objectListing = ossClient.listObjects(listObjectsRequest);
                if (ObjectUtil.isNull(objectListing) || (objectListing.getObjectSummaries().size() == 0 && objectListing.getCommonPrefixes().size() == 0)) {
                    break;
                }

                // 遍历所有文件。
                //            System.out.println("file:");
                // objectSummaries的列表中给出的是 prefix 目录下的文件。
                for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    if (!objectSummary.getKey().endsWith("/")) {
                        list.add(objectSummary.getKey());
                    }
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } catch (OSSException oe) {
            log.error("getOssDirectoryAndFileByUrl, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:：{}", oe.getErrorMessage());
            log.error("getOssDirectoryAndFileByUrl, Error Code:：{}", oe.getErrorCode());
            log.error("getOssDirectoryAndFileByUrl, Request ID:：{}", oe.getRequestId());
            log.error("getOssDirectoryAndFileByUrl, Host ID:：{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("getOssDirectoryAndFileByUrl, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return list;
    }

    /**
     * 指定目录是否为空目录（是否至少有一个子文件，包含子目录的子文件）
     *
     * @param parentDirectory
     * @return
     */
    public static Boolean existOneFileByParentDirectory(String endpoint, String accessKey, String accessKeySecret, String prefix) {
        Boolean fileKeyFlag = false;
        // Bucket名称。
        String bucketName = BucketEnum.PUBLIC.getCode();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        // 指定每页列举200个文件。
        int maxKeys = 200;
        String nextMarker = null;

        try {
            ObjectListing objectListing = null;
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker);
                // 列出fun目录下的所有子文件。
                listObjectsRequest.setPrefix(prefix);
                // 每页列举的文件最大数
                listObjectsRequest.setMaxKeys(maxKeys);
                objectListing = ossClient.listObjects(listObjectsRequest);
                // 文件是否存在
                if (ObjectUtil.isNotNull(objectListing) && CollUtil.isNotEmpty(objectListing.getObjectSummaries())) {
                    // 仅获取子文件，不以“/”结尾
                    List<OSSObjectSummary> fileKeyList = objectListing.getObjectSummaries().stream().filter(o -> !o.getKey().endsWith("/")).distinct().collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(fileKeyList)) {
                        fileKeyFlag = true;
                        break;
                    }
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } catch (OSSException oe) {
            log.error("getOssDirectoryAndFileByUrl, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:：{}", oe.getErrorMessage());
            log.error("getOssDirectoryAndFileByUrl, Error Code:：{}", oe.getErrorCode());
            log.error("getOssDirectoryAndFileByUrl, Request ID:：{}", oe.getRequestId());
            log.error("getOssDirectoryAndFileByUrl, Host ID:：{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("getOssDirectoryAndFileByUrl, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileKeyFlag;
    }

    /**
     * 获取指定目录下的所有子文件（包含所有子目录的子文件）
     *
     * @param parentDirectory
     * @return
     */
    public static List<String> getAllFileByParentDirectory(String endpoint, String accessKey, String accessKeySecret, String prefix) {
        // Bucket名称。
        String bucketName = BucketEnum.PUBLIC.getCode();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
        // 指定每页列举200个文件。
        int maxKeys = 200;
        String nextMarker = null;
        List<String> list = new ArrayList<>();

        try {
            ObjectListing objectListing = null;
            do {
                ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withMarker(nextMarker);
                // 列出fun目录下的所有文件和文件夹。
                listObjectsRequest.setPrefix(prefix);
                // 每页列举的文件最大数
                listObjectsRequest.setMaxKeys(maxKeys);
                objectListing = ossClient.listObjects(listObjectsRequest);
                if (ObjectUtil.isNull(objectListing) || CollUtil.isEmpty(objectListing.getObjectSummaries())) {
                    break;
                }

                // 遍历所有文件。
                // System.out.println("file:");
                // objectSummaries的列表中给出的是 prefix 目录下的文件。
                for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    // 仅取文件，不以“/”结尾的
                    if (!objectSummary.getKey().endsWith("/")) {
                        list.add(objectSummary.getKey());
                    }
                }

                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
        } catch (OSSException oe) {
            log.error("getOssDirectoryAndFileByUrl, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:：{}", oe.getErrorMessage());
            log.error("getOssDirectoryAndFileByUrl, Error Code:：{}", oe.getErrorCode());
            log.error("getOssDirectoryAndFileByUrl, Request ID:：{}", oe.getRequestId());
            log.error("getOssDirectoryAndFileByUrl, Host ID:：{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("getOssDirectoryAndFileByUrl, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("getOssDirectoryAndFileByUrl, Error Message:{}", ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return list;
    }


    /**
     * 批量删除文件
     *
     * @param endpoint 区域站点
     * @param accessKey 账号
     * @param accessKeySecret 密匙
     * @param fileKeyList 需要删除的文件列表，每次最多删除1000个文件
     */
    public static void deleteOssFileByFileKeyList(String endpoint, String accessKey, String accessKeySecret, List<String> fileKeyList) {
        if (CollUtil.isEmpty(fileKeyList)) {
            return;
        }
        List<List<String>> deleteList = new ArrayList<>();
        if (fileKeyList.size() > 200) {
            deleteList = Lists.partition(fileKeyList, 200);
        } else {
            deleteList.add(fileKeyList);
        }

        for (List<String> fileKeys : deleteList) {
            // Bucket名称。
            String bucketName = BucketEnum.PUBLIC.getCode();
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
            try {
                // 删除文件。
                // 填写需要删除的多个文件完整路径。文件完整路径中不能包含Bucket名称。
                DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileKeys).withEncodingType("url"));
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                if (CollUtil.isEmpty(deletedObjects)) {
                    log.error("OSS删除erp流向文件失败，deletedObjects is empty, fileKey:{}", JSON.toJSONString(fileKeys));
                }
                try {
                    for (String obj : deletedObjects) {
                        String deleteObj = URLDecoder.decode(obj, "UTF-8");
                        fileKeys.remove(deleteObj);
                    }
                    if (CollUtil.isNotEmpty(fileKeys)) {
                        log.error("OSS删除erp流向文件失败，fileKey:{}", JSON.toJSONString(fileKeys));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (OSSException oe) {
                log.error("deleteOssFileByDirectoryPrefixs, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
                log.error("deleteOssFileByDirectoryPrefixs, Error Message:：{}", oe.getErrorMessage());
                log.error("deleteOssFileByDirectoryPrefixs, Error Code:：{}", oe.getErrorCode());
                log.error("deleteOssFileByDirectoryPrefixs, Request ID:：{}", oe.getRequestId());
                log.error("deleteOssFileByDirectoryPrefixs, Host ID:：{}", oe.getHostId());
            } catch (ClientException ce) {
                log.error("deleteOssFileByDirectoryPrefixs, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
                log.error("deleteOssFileByDirectoryPrefixs, Error Message:{}", ce.getMessage());
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
    }

    /**
     * 删除目录
     * 根据目录地址删除此目录及目录下的所有子目录、子文件
     * 删除 src 目录及目录下的所有文件，则 prefix 设置为 src/
     *
     * @param endpoint 区域站点
     * @param accessKey 账号
     * @param accessKeySecret 密匙
     * @param filePrefixList 目录集合
     */
    public static void deleteOssFileByDirectoryPrefixs(String endpoint, String accessKey, String accessKeySecret, List<String> filePrefixList) {
        if (CollUtil.isEmpty(filePrefixList)) {
            return;
        }
        // Bucket名称。
        String bucketName = BucketEnum.PUBLIC.getCode();
        for (String prefix : filePrefixList) {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
            // 指定每页列举200个文件。
            int maxKeys = 200;
            try {
                // 列举所有包含指定前缀的文件并删除，先删除子文件、再删除子目录。
                String nextMarker = null;
                ObjectListing objectListing = null;
                do {
                    ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withPrefix(prefix).withMaxKeys(maxKeys).withMarker(nextMarker);
                    objectListing = ossClient.listObjects(listObjectsRequest);
                    if (CollUtil.isEmpty(objectListing.getObjectSummaries())) {
                        break;
                    }
                    // 子文件
                    List<String> keys = new ArrayList<>();
                    for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                        if (!objectSummary.getKey().endsWith("/")) {
                            keys.add(objectSummary.getKey());
                        }
                    }
                    if (CollUtil.isNotEmpty(keys)) {
                        List<String> deletedObjects = new ArrayList<>();
                        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url");
                        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                        deletedObjects.addAll(deleteObjectsResult.getDeletedObjects());
                        if (CollUtil.isEmpty(deletedObjects)) {
                            log.warn("oss删除文件失败，prefix：{}", prefix);
                        }
                    }
                    nextMarker = objectListing.getNextMarker();
                } while (objectListing.isTruncated());

                // 子文件已经全部删除，子目录进行删除
                String directoryNextMarker = null;
                ObjectListing directoryObjectListing = null;
                do {
                    ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withPrefix(prefix).withMaxKeys(maxKeys).withMarker(directoryNextMarker);
                    directoryObjectListing = ossClient.listObjects(listObjectsRequest);
                    if (CollUtil.isEmpty(objectListing.getObjectSummaries()) && CollUtil.isEmpty(directoryObjectListing.getCommonPrefixes())) {
                        break;
                    }

                    List<String> keys = new ArrayList<>();
                    // 当前目录、及子目录
                    if (CollUtil.isNotEmpty(objectListing.getObjectSummaries())) {
                        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                            if (objectSummary.getKey().endsWith("/")) {
                                keys.add(objectSummary.getKey());
                            }
                        }
                    }
                    if (CollUtil.isNotEmpty(objectListing.getCommonPrefixes())) {
                        for (String commonPrefix : directoryObjectListing.getCommonPrefixes()) {
                            keys.add(commonPrefix);
                        }
                    }
                    if (CollUtil.isNotEmpty(keys)) {
                        keys = keys.stream().distinct().collect(Collectors.toList());
                        List<String> deletedObjects = new ArrayList<>();
                        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url");
                        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
                        deletedObjects.addAll(deleteObjectsResult.getDeletedObjects());
                        if (CollUtil.isEmpty(deletedObjects)) {
                            log.warn("oss删除目录失败，prefix：{}", prefix);
                        }
                    }
                    directoryNextMarker = directoryObjectListing.getNextMarker();
                } while (directoryObjectListing.isTruncated());

            } catch (OSSException oe) {
                log.error("deleteOssFileByDirectoryPrefixs, Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
                log.error("deleteOssFileByDirectoryPrefixs, Error Message:：{}", oe.getErrorMessage());
                log.error("deleteOssFileByDirectoryPrefixs, Error Code:：{}", oe.getErrorCode());
                log.error("deleteOssFileByDirectoryPrefixs, Request ID:：{}", oe.getRequestId());
                log.error("deleteOssFileByDirectoryPrefixs, Host ID:：{}", oe.getHostId());
            } catch (ClientException ce) {
                log.error("deleteOssFileByDirectoryPrefixs, Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
                log.error("deleteOssFileByDirectoryPrefixs, Error Message:{}", ce.getMessage());
            } finally {
                if (ossClient != null) {
                    ossClient.shutdown();
                }
            }
        }
    }

    /**
     * 获取字符串中指定字符的数量
     *
     * @param input 字符
     * @param symbol 统计数量的指定字符
     * @return
     */
    private static int getCharCountBySymbol(String input, String symbol) {
        int count = 0;
        int length = input.length();
        while (input.indexOf(symbol) != -1) {
            input = input.substring(0, input.lastIndexOf(symbol));
            count++;
        }
        return count;
    }


    /**
     * 根据分割符号获取倒数第1个符号后面面的字符
     *
     * @param str 字符
     * @param symbol 分割符号
     * @return
     */
    public static String getSuffixBySymbol(String str, String symbol) {
        if (StrUtil.isBlank(str) || StrUtil.isBlank(symbol)) {
            return str;
        }
        return str.substring(str.lastIndexOf(symbol, str.lastIndexOf(symbol)) + 1);
    }

    /**
     * 根据分割符号获取倒数第2个符号前面的字符
     *
     * @param str 字符
     * @param symbol 分割符号
     * @return
     */
    public static String getPrefixBySymbol(String str, String symbol) {
        if (StrUtil.isBlank(str) || StrUtil.isBlank(symbol)) {
            return str;
        }
        return str.substring(0, str.lastIndexOf(symbol, str.lastIndexOf(symbol) - 1));
    }

    /**
     * 根据分割符号获取倒数第一个符号前面的字符
     *
     * @param str 字符
     * @param symbol 分割符号
     * @return
     */
    public static String getPrefixBySymbol2(String str, String symbol) {
        if (StrUtil.isBlank(str) || StrUtil.isBlank(symbol)) {
            return str;
        }
        return str.substring(0, str.lastIndexOf(symbol));
    }

    public static String getOssDirectoryName(String directoryUrl) {
        if (StrUtil.isBlank(directoryUrl)) {
            return "";
        }
        String directory = directoryUrl.substring(0, directoryUrl.lastIndexOf("/"));
        return directory.substring(directory.lastIndexOf("/") + 1, directory.length()).trim();
    }

    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 日期格式校验
     *
     * @param dateStr
     * @return
     */
    public static boolean checkDate(String dateStr) {
        // 年份不能为0开始
        if (dateStr.startsWith("0")) {
            return false;
        }
        // 数字校验
        String pat = "\\d{4}-\\d{2}-\\d{2}";
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(dateStr);
        if (!m.matches()) {
            return false;
        }
        // 日期格式校验
        final DateTimeFormatter dateFormatter = DATE_FORMATTER.withResolverStyle(ResolverStyle.STRICT);
        try {
            dateFormatter.parse(dateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
