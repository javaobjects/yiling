package com.yiling.erp.client.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yiling.erp.client.util.Utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/5/20
 */
@Service("clientLogCleanService")
@Slf4j
public class ClientLogCleanServiceImpl {

    private static final int DATE_COUNT = 5;

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final long BYTE = 1;

    private static final long KILO_BYTE = 1024 * BYTE;

    private static final long MEGA_BYTE = 1024 * KILO_BYTE;

    private static final String[] LOG_ARRAY = { "erpLog.log", "erpLog-info.log", "erpLog-debug.log", "erpLog-error.log" };

    private static final int FILE_LAST_NUM = 500;


    @Scheduled(cron = "0 0 5 * * ?")
    public void cleanLong() {
        try {
            String logPath = System.getProperty("catalina.home").concat("/logs");
            cleanLogFiles(logPath);
        } catch (Exception e) {
            log.error("ClientLogCleanServiceImpl, 清理日志失败", e);
        }
    }


    /***
     * 删除10天前的日志文件
     *
     * @param obj
     * @return
     */
    public static void cleanLogFiles(Object obj) {
        File directory;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        if (directory.isFile()) {
            String fileCreateTime = getFileCreateTime(directory);
            Boolean cleanFileFlag = isCleanFile(fileCreateTime);
            if (cleanFileFlag) {
                directory.setReadable(true,false);
                directory.setWritable(true,false);
                directory.delete();
            }
            // "erpLog.log", "erpLog-info.log", "erpLog-debug.log", "erpLog-error.log"
            // 文件大小超过10M的，取最后500行覆盖原内容
//            if (ArrayUtil.contains(LOG_ARRAY, directory.getName()) && directory.length() >= 10 * MEGA_BYTE) {
//                long lineNum = getLineNumber(directory);
//                if (lineNum > 500) {
//                    List<String> fileContentList = readLastLine(directory, FILE_LAST_NUM);
//                    fileContentList.add(">>>>>>>>>> 增加一行，写入后在第一行");
//                    writeLineContent(directory, fileContentList);
//                }
//            }
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                cleanLogFiles(fileOne);
            }
        }
    }

//    public static void main(String[] args) {
//        String logPath = "E:\\logs\\logs";
//        cleanLogFiles(logPath);

        //        File file = new File("D:\\Java\\apache-tomcat-9.0.52\\logs\\erpLog.log");
        //        System.out.println(">>>>> path:" + file.getPath());
        //        System.out.println(">>>>> name:" + file.getName());


        //        List<String> list = new ArrayList<>();
        //        list.add("1");
        //        list.add("2");
        //        list.add("3");
        //        for (int i = list.size() - 1; i >= 0; i--) {
        //            System.out.println(">>>>> content:" + list.get(i));
        //        }
//    }

    /**
     * 获取文件创建时间
     *
     * @param file
     * @return
     */
    public static String getFileCreateTime(File file) {
        if (file == null) {
            return null;
        }

        // 文件名的创建时间
        Date endDate = new Date();
        List<String> dateStrList = Utils.dateList(DATE_COUNT, endDate);
        String fileName = file.getName();
        String fileNameTime = "";
        for (String dateStr : dateStrList) {
            if (fileName.contains(dateStr)) {
                fileNameTime = dateStr;
                break;
            }
        }

        if (StrUtil.isNotBlank(fileNameTime)) {
            return fileNameTime;
        } else {
            String year = String.valueOf(DateUtil.thisYear());
            String oldYear = String.valueOf(DateUtil.thisYear() - 1);
            if (fileName.contains(oldYear) || fileName.contains(year)) {
                String startWithYear = fileName.substring(fileName.indexOf(year));
                if (startWithYear.contains("_")) {
                    fileNameTime = startWithYear.substring(0, startWithYear.lastIndexOf("_"));
                } else {
                    fileNameTime = startWithYear.substring(0, startWithYear.lastIndexOf("-") + 3);
                }
                return fileNameTime;
            }
        }
        return null;
    }

    /**
     * 日期与当前时间差是否大于10天
     *
     * @param fileCreateTime
     * @return
     */
    public static Boolean isCleanFile(String fileCreateTime) {
        if (StrUtil.isBlank(fileCreateTime)) {
            return false;
        }
        Date startDate = DateUtil.parse(fileCreateTime, DATE_FORMAT);
        // 时间差是否大于10天
        Date date = new Date();
        long offset = DateUtil.betweenDay(startDate, date, false);
        if (offset >= DATE_COUNT) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件行数
     *
     * @param file
     * @return
     */
    public static long getLineNumber(File file) {
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                log.error("ClientLogCleanServiceImpl, 获取日志文件行数异常");
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取文件最后xx行的内容
     *
     * @param file
     * @param lastNum
     * @return
     */
    public static List<String> readLastLine(File file, int lastNum) {
        List<String> list = new ArrayList<>();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8)) {
            String line = "";
            while ((line = reader.readLine()) != null && list.size() < lastNum) {
                list.add(line);
            }
        } catch (IOException e) {
            log.error("ClientLogCleanServiceImpl, 获取日志文件内容异常");
            e.printStackTrace();
        }
        return list;
    }

    //    public static List<String> readLastLine(File file, int lastNum) {
    //        List<String> list = new ArrayList<>();
    //        InputStream fileInputStream;
    //        InputStreamReader inputStreamReader;
    //        BufferedReader bufferedReader;
    //        try {
    //            fileInputStream = new FileInputStream(file);
    //            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
    //            bufferedReader = new BufferedReader(inputStreamReader);
    //            String line = "";
    //            while ((line = bufferedReader.readLine()) != null && list.size() < lastNum) {
    //                list.add(line);
    //            }
    //        } catch (FileNotFoundException e) {
    //            log.error("ClientLogCleanServiceImpl, 获取日志文件内容异常");
    //            e.printStackTrace();
    //        } catch (UnsupportedEncodingException e) {
    //            log.error("ClientLogCleanServiceImpl, 获取日志文件内容异常");
    //            e.printStackTrace();
    //        } catch (IOException e) {
    //            log.error("ClientLogCleanServiceImpl, 获取日志文件内容异常");
    //            e.printStackTrace();
    //        }
    //        return list;
    //    }

    /**
     * 最后xx行的内容倒序写入文件
     *
     * @param file
     * @param list
     */
    public static void writeLineContent(File file, List<String> list) {
        if (CollUtil.isEmpty(list)) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write("");
            } catch (IOException e) {
                log.error("ClientLogCleanServiceImpl, 写入日志文件内容异常");
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    log.error("ClientLogCleanServiceImpl, 关闭写入日志文件内容异常");
                    e.printStackTrace();
                }
            }
            return;
        }

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = list.size() - 1; i >= 0; i--) {
                String content = list.get(i);
                bufferedWriter.write(content + "\t\n");
            }
        } catch (FileNotFoundException e) {
            log.error("ClientLogCleanServiceImpl, 写入日志文件内容异常");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            log.error("ClientLogCleanServiceImpl, 写入日志文件内容异常");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("ClientLogCleanServiceImpl, 写入日志文件内容异常");
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                outputStreamWriter.close();
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("ClientLogCleanServiceImpl, 关闭写入日志文件内容异常");
                e.printStackTrace();
            }
        }
    }

}
