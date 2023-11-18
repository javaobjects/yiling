package com.yiling.export.imports.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.yiling.export.excel.listener.AbstractImportReadException;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.imports.listener.DemoTestListener;
import com.yiling.export.imports.listener.model.DemoTestModel;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * easyExcel导入工具类
 */
@Slf4j
public class EasyExcelUtils {
    /**
     * easyExcel导入工具类
     */
    public static ImportResultModel importExcelMore(InputStream inputStream, Class<?> pojoClass, AbstractImportReaderListener<?> readListener) throws Exception {
        long start = System.currentTimeMillis();
        ImportResultModel resultModel = new ImportResultModel();
        resultModel.setList(new ArrayList());
        resultModel.setFailList(new ArrayList());
        readListener.setImportResultModel(resultModel);
        try {
            EasyExcel.read(inputStream, pojoClass, readListener).sheet().headRowNumber(readListener.getHeadRowNumber()).doRead();
        } catch (final ExcelDataConvertException ex) {
            log.error("rowIndex:{},error:{}", ex.getRowIndex(), ex.getMessage());
        } catch (final AbstractImportReadException ex1) {
            log.error("error:{}", ex1.getMessage());
        } catch (final Exception ex) {
            log.error("error:{}", ex.getMessage());
        }
        if (Objects.nonNull(resultModel.getFailList())) {
            resultModel.setFailCount(resultModel.getFailList().size());
            log.info("fail:{}", resultModel.getFailList());
        }
        if (Objects.nonNull(resultModel.getList())) {
            resultModel.setSuccessCount(resultModel.getList().size());
            log.info("SuccessCount:{}", resultModel.getSuccessCount());
        }
        //只导出错误信息
        FileInfo fileInfo = null;
        List list = new ArrayList<>();
        if (readListener.isOnlyExportFail()) {
            list.addAll(resultModel.getFailList());
        }
        list.addAll(resultModel.getList());
        fileInfo = uploadOss("sheet1", pojoClass, list);
        if (fileInfo != null) {
            resultModel.setFailUrl(fileInfo.getUrl());
            resultModel.setResultUrl(fileInfo.getKey());
        }
        log.info("导入耗时：{}", System.currentTimeMillis() - start);
        return readListener.getImportResultModel();
    }

    /**
     * 上传oss
     *
     * @param sheetName
     * @param list
     * @return
     * @throws Exception
     */
    private static <T> FileInfo uploadOss(String sheetName, Class<?> failPojoClass, List<T> list) throws
            Exception {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        EasyExcel.write(bos).head(failPojoClass).sheet(sheetName).doWrite(list);

        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        FileService fileService = SpringUtils.getBean(FileService.class);
        FileInfo fileInfo = fileService.upload(is, "error.xlsx", FileTypeEnum.EXCEL_IMPORT_RESULT);
        return fileInfo;
    }

    /**
     * 新建新的文件并写入指定字节数组的数据
     *
     * @param fileName 文件目录及名称字符串
     * @param bytes    字节数组
     * @return 文件
     */
    private static File createFile(String fileName, byte[] bytes) {
        //参数校验
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        File newFile = new File(fileName);
        try {
            if (!newFile.exists()) {
                //创建文件
                newFile.createNewFile();
            }
            //创建输出流，写入数据
            FileOutputStream outputStream = new FileOutputStream(newFile);
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newFile;
    }

    public static void main(String[] args) throws Exception {
        DemoTestListener listener = new DemoTestListener();
        ImportResultModel model = new ImportResultModel();
        listener.setImportResultModel(model);
        File file = new File("/Users/xueli/Desktop/demotest2.xlsx");
        EasyExcelUtils.importExcelMore(new FileInputStream(file), DemoTestModel.class, listener);
    }
}
