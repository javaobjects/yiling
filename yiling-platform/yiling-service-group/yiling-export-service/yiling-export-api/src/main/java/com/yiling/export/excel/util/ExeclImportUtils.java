package com.yiling.export.excel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
@Slf4j
@Component
public class ExeclImportUtils<T> {
    @Lazy
    @Autowired
    ExeclImportUtils execlImportUtils;

    /**
     * excel文件上传包含图片的路径
     */
    public static final String EXECL_PATH = "/tmp/excel";

    /**
     * excel文件上传
     *
     * @param inputstream 文件流
     * @param pojoClass   导入对象实体
     * @param params      参数定义
     * @param <T>         具体实体
     * @return
     * @throws Exception
     */
    @Deprecated
    public static <T> ImportResultModel<T> importExcelMore(InputStream inputstream, Class<?> pojoClass,
                                                           ImportParams params) throws Exception {
        ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
        //通过上传文件返回结果在处理返回结果
        if (result == null) {
            return null;
        }

        ImportResultModel<T> resultModel = transImportResult(result, pojoClass);
        FileInfo fileInfo = uploadOss(result.getFailWorkbook().getSheetName(0), pojoClass, resultModel.getFailList(), resultModel.getList());
        if (fileInfo != null) {
            resultModel.setFailUrl(fileInfo.getUrl());
            resultModel.setResultUrl(fileInfo.getKey());
        }
        return resultModel;
    }


    /**
     * excel文件上传
     *
     * @param inputstream
     * @param pojoClass
     * @param params
     * @param importDataHandler
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel<T> importExcelMore(InputStream inputstream, Class<?> pojoClass,
                                                           ImportParams params, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        Long start = System.currentTimeMillis();
        ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
        //通过上传文件返回结果在处理返回结果
        if (result == null) {
            return null;
        }
        log.info("数据准备耗时：{}", System.currentTimeMillis() - start);
        ImportResultModel<T> resultModel = executeData(transImportResult(result, pojoClass), pojoClass, importDataHandler, paramMap);
        FileInfo fileInfo = uploadOss(result.getFailWorkbook().getSheetName(0), pojoClass, resultModel.getFailList(), resultModel.getList());
        if (fileInfo != null) {
            resultModel.setFailUrl(fileInfo.getUrl());
            resultModel.setResultUrl(fileInfo.getKey());
        }
        log.info("导入耗时：{}", System.currentTimeMillis() - start);
        return resultModel;
    }

    /**
     * excel文件上传（检查异常，只要存在任意错误的数据则全部不入库）
     *
     * @param inputstream
     * @param pojoClass
     * @param params
     * @param importDataHandler
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel<T> importExcelAllTrue(InputStream inputstream, Class<?> pojoClass,
                                                              ImportParams params, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        Long start = System.currentTimeMillis();
        ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
        //通过上传文件返回结果在处理返回结果
        if (result == null) {
            return null;
        }
        log.info("数据准备耗时：{}", System.currentTimeMillis() - start);
        ImportResultModel<T> resultModel = executeAllTrueData(transImportResult(result, pojoClass), pojoClass, importDataHandler, paramMap);
        FileInfo fileInfo = uploadOss(result.getFailWorkbook().getSheetName(0), pojoClass, resultModel.getFailList(), resultModel.getList());
        if (fileInfo != null) {
            resultModel.setResultUrl(fileInfo.getKey());
        }
        log.info("导入耗时：{}", System.currentTimeMillis() - start);
        return resultModel;
    }

    public <T> ImportResultModel<T> parallelImportExcelMore(InputStream inputstream, Class<?> pojoClass,
                                                            ImportParams params, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        Long start = System.currentTimeMillis();
        ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
        //通过上传文件返回结果在处理返回结果
        if (result == null) {
            return null;
        }
        log.info("数据准备耗时：{}", System.currentTimeMillis() - start);
        ImportResultModel<T> resultModel = parallelExecuteData(transImportResult(result, pojoClass), pojoClass, importDataHandler, paramMap);
        FileInfo fileInfo = uploadOss(result.getFailWorkbook().getSheetName(0), pojoClass, resultModel.getFailList(), resultModel.getList());
        if (fileInfo != null) {
            resultModel.setFailUrl(fileInfo.getUrl());
            resultModel.setResultUrl(fileInfo.getKey());
        }
        log.info("并行导入耗时：{}", System.currentTimeMillis() - start);
        return resultModel;
    }

    /**
     * 多线程并行执行方法
     *
     * @param resultModel
     * @param pojoClass
     * @param importDataHandler
     * @param paramMap
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> ImportResultModel parallelExecuteData(ImportResultModel<T> resultModel, Class<?> pojoClass, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        List<T> allSuccessList = resultModel.getList();
        //以200为一组，切割成多个list
        List<List<T>> successLists = ListUtils.partition(allSuccessList, 200);
        //所有异步执行结果list
        List<Future<ImportResultModel>> futures = new ArrayList<>();
        //异常list
        List<T> failList = Lists.newArrayList();

        successLists.forEach(successList -> {
            try {
                //异步执行execute
                Future<ImportResultModel> future = execlImportUtils.executeDataByThread(successList, pojoClass, importDataHandler, paramMap);
                futures.add(future);
            } catch (Exception e) {
                log.error(e.getMessage());
                failList.addAll(successList);
            }
        });
        //最终导入结果初始化
        ImportResultModel<T> allResultModel = new ImportResultModel();
        allResultModel.setList(Lists.newArrayList());
        allResultModel.setFailCount(resultModel.getFailCount());
        allResultModel.setSuccessCount(0);
        allResultModel.setFailList(resultModel.getFailList());
        allResultModel.setSheetName(resultModel.getSheetName());
        //循环获取异步结果，并合并结果集
        for (Future<ImportResultModel> future : futures) {
            //不能在异步方法（executeDataByThread）上直接使用get()获取结果，需单独调用，否则为同步获取结果导致并行无效
            ImportResultModel model = future.get();
            allResultModel.setFailCount(allResultModel.getFailCount() + model.getFailCount());
            allResultModel.setSuccessCount(allResultModel.getSuccessCount() + model.getSuccessCount());
            allResultModel.getList().addAll(model.getList());
            allResultModel.getFailList().addAll(model.getFailList());
        }

        if (failList.size() > 0) {
            allResultModel.setFailCount(allResultModel.getFailCount() + failList.size());
            Iterator<T> it = failList.iterator();
            while (it.hasNext()) {
                T t = it.next();
                invokeStatus(t, pojoClass, "失败");
                invokeErrorMsg(t, pojoClass, "系统异常");

                allResultModel.getFailList().add(t);
            }
        }


        return allResultModel;
    }

    /**
     * 单个list异步线程池执行
     *
     * @param successList
     * @param pojoClass
     * @param importDataHandler
     * @param paramMap
     * @param <T>
     * @return
     * @throws Exception
     */
    @Async("excelExecutor")
    public <T> Future<ImportResultModel> executeDataByThread(List<T> successList, Class<?> pojoClass, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        long start = System.currentTimeMillis();
        //由于executeData方法单条失败数据存在remove操作，多线程执行保证list操作独立性使用CopyOnWriteArrayList
        CopyOnWriteArrayList<T> calList = new CopyOnWriteArrayList(successList);
        ImportResultModel<T> resultModel = new ImportResultModel();
        resultModel.setFailCount(0);
        resultModel.setSuccessCount(calList.size());
        resultModel.setList(calList);
        resultModel.setFailList(Lists.newArrayList());
        ImportResultModel importResultModel = executeData(resultModel, pojoClass, importDataHandler, paramMap);
        log.info("{}异步执行完成，时间为：{}", Thread.currentThread().getName(), System.currentTimeMillis() - start);
        return new AsyncResult<>(importResultModel);
    }

    /**
     * excel文件上传
     *
     * @param inputstream
     * @param pojoClass
     * @param params
     * @param importDataHandler
     * @param opUserId
     * @param sheetName
     * @param <T>
     * @return
     * @throws Exception
     */
    @Deprecated
    public static <T> ImportResultModel<T> importExcelMore(InputStream inputstream, Class<?> pojoClass,
                                                           ImportParams params,
                                                           ImportDataHandler importDataHandler,
                                                           Long opUserId,
                                                           String sheetName) throws Exception {
        Long start = System.currentTimeMillis();
        ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
        //通过上传文件返回结果在处理返回结果
        if (result == null) {
            return null;
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, opUserId);

        ImportResultModel<T> resultModel = executeData(transImportResult(result, pojoClass, sheetName), pojoClass, importDataHandler, paramMap);
        FileInfo fileInfo = uploadOss(sheetName, pojoClass, resultModel.getFailList(), resultModel.getList());
        if (fileInfo != null) {
            resultModel.setFailUrl(fileInfo.getUrl());
            resultModel.setResultUrl(fileInfo.getKey());
        }
        log.info("导入耗时：{}", System.currentTimeMillis() - start);
        return resultModel;
    }

    /**
     * excel 商品多sheet文件上传
     *
     * @param file
     * @param pojoClassList
     * @param paramsList
     * @param importDataHandlerList
     * @param paramMap
     * @param sheetNameList
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel<T> importExcelMore(MultipartFile file, List<Class<?>> pojoClassList,
                                                           List<ImportParams> paramsList,
                                                           List<ImportDataHandler> importDataHandlerList,
                                                           List<String> sheetNameList,
                                                           Map<String, Object> paramMap) throws Exception {
        Long start = System.currentTimeMillis();
        ImportResultModel<T> resultModel = new ImportResultModel();
//        List<ImportResultModel.ImportResultDetailModel> listDetailModel = new ArrayList<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Integer failCount = 0;
        Integer successCount = 0;
        for (int i = 0; i < pojoClassList.size(); i++) {
            Class<?> pojoClass = pojoClassList.get(i);
            ImportParams params = paramsList.get(i);
            ImportDataHandler importDataHandler = importDataHandlerList.get(i);
            String sheetName = sheetNameList.get(i);
            ExcelImportResult<T> result = new ExcelImportService().importExcelByIs(file.getInputStream(), pojoClass, params, true);
            //通过上传文件返回结果在处理返回结果
            if (result == null) {
                return null;
            }
            log.info("size is", result.getList());
            ImportResultModel<T> resultModelSheet = executeData(transImportResult(result, pojoClass, sheetName), pojoClass, importDataHandler, paramMap);
            if(CollectionUtil.isNotEmpty(resultModelSheet.getFailList())){
                Map<String, Object> map = new HashMap<>();
                ExportParams exportParams = new ExportParams();
                exportParams.setSheetName(sheetName);
                map.put("title", exportParams);
                map.put("entity", pojoClass);
                map.put("data", resultModelSheet.getFailList());
                list.add(map);
            }
            successCount = successCount + resultModelSheet.getSuccessCount();
            failCount = failCount + resultModelSheet.getFailCount();
//            for (ImportResultModel.ImportResultDetailModel sheet : resultModelSheet.getImportResultDetailList()) {
//                sheet.setSheetName(resultModelSheet.getSheetName());
//            }
//            listDetailModel.addAll(resultModelSheet.getImportResultDetailList());
        }
        if(CollectionUtil.isNotEmpty(list)){
            FileInfo fileInfo = uploadOss(list, ExcelType.XSSF);
            if (fileInfo != null) {
                resultModel.setFailUrl(fileInfo.getUrl());
                resultModel.setResultUrl(fileInfo.getKey());
            }
        }
        resultModel.setFailCount(failCount);
        resultModel.setSuccessCount(successCount);
//        resultModel.setImportResultDetailList(listDetailModel);
        log.info("导入耗时：{}", System.currentTimeMillis() - start);
        return resultModel;
    }

    /**
     * 执行数据入库操作
     *
     * @param resultModel
     * @param pojoClass
     * @param importDataHandler
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel executeData(ImportResultModel<T> resultModel, Class<?> pojoClass, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        List<T> successList = resultModel.getList();
        importDataHandler.execute(successList, paramMap);
//        List<T> newSuccessList = new ArrayList<>(successList);
        extracted(resultModel, pojoClass, successList);
        return resultModel;
    }

    /**
     * 执行数据入库操作（只要存在失败的数据，就不插入数据库）
     *
     * @param resultModel
     * @param pojoClass
     * @param importDataHandler
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel executeAllTrueData(ImportResultModel<T> resultModel, Class<?> pojoClass, ImportDataHandler importDataHandler, Map<String, Object> paramMap) throws Exception {
        List<T> successList = resultModel.getList();
        if (CollUtil.isEmpty(resultModel.getFailList()) && CollUtil.isNotEmpty(successList)) {
            importDataHandler.execute(successList, paramMap);
        }
        extracted(resultModel, pojoClass, successList);
        return resultModel;
    }

    private static <T> void extracted(ImportResultModel<T> resultModel, Class<?> pojoClass, List<T> successList) throws IllegalAccessException {
        Iterator<T> it = successList.iterator();
        while (it.hasNext()) {
            T t = it.next();
            Field[] fields = null;
            if (t instanceof BaseImportModel) {
                fields = BaseImportModel.class.getDeclaredFields();
            } else {
                fields = pojoClass.getDeclaredFields();
            }
            String msg = "";
            for (Field field : fields) {
                field.setAccessible(true);
                if ("errorMsg".equals(field.getName())) {
                    Object value = field.get(t);
                    msg = (String) value;
                }
            }
            if (StringUtils.isNotEmpty(msg)) {
                resultModel.setFailCount(resultModel.getFailCount() + 1);
                resultModel.setSuccessCount(resultModel.getSuccessCount() - 1);
                resultModel.getFailList().add(t);
                it.remove();
            }
        }
    }

    /**
     * 上传oss
     *
     * @param sheetName
     * @param pojoClass
     * @param failList
     * @return
     * @throws Exception
     */
    public static <T> FileInfo uploadOss(String sheetName, Class<?> pojoClass, List<T> failList, List<T> list) throws
            Exception {
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName(sheetName);
        if (CollectionUtil.isEmpty(failList) && CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<T> listAll = new ArrayList();
        listAll.addAll(list);
        listAll.addAll(failList);
        Collections.sort(listAll, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                try {
                    Method method1 = o1.getClass().getMethod("getRowNum");
                    Integer i1 = (Integer) method1.invoke(o1);
                    Method method2 = o2.getClass().getMethod("getRowNum");
                    Integer i2 = (Integer) method2.invoke(o2);
                    return i1.compareTo(i2);
                } catch (Exception e) {
                }
                return 0;
            }
        });
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, listAll);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        FileService fileService = SpringUtils.getBean(FileService.class);
        FileInfo fileInfo = fileService.upload(is, "error.xls", FileTypeEnum.EXCEL_IMPORT_RESULT);
        //todo 删除文件路径里面的图片--定时删除
        return fileInfo;
    }

    /**
     * 多sheet上传oss
     *
     * @param list 多个Map key title 对应表格Title key entity 对应表格对应实体 key data  Collection 数据
     * @return
     * @throws Exception
     */
    private static FileInfo uploadOss(List<Map<String, Object>> list, ExcelType type) throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(list, type);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        byte[] barray = bos.toByteArray();
        InputStream is = new ByteArrayInputStream(barray);
        FileService fileService = SpringUtils.getBean(FileService.class);
        FileInfo fileInfo = fileService.upload(is, "error.xlsx", FileTypeEnum.EXCEL_IMPORT_RESULT);
        //todo 删除文件路径里面的图片--定时删除
        return fileInfo;
    }

    /**
     * 返回结果在处理返回结果
     *
     * @param result
     * @param pojoClass
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> ImportResultModel transImportResult(ExcelImportResult<T> result, Class<?> pojoClass) throws
            Exception {
        ImportResultModel importResultModel = new ImportResultModel();
        List<T> failList = result.getFailList();
        List<T> successList = result.getList();
        Integer failCount = failList.size();
        Integer successCount = successList.size();
        //循环失败的信息
        for (T t : failList) {
            invokeStatus(t, pojoClass, "失败");
        }

        //循环成功的信息，然后判断在成功的数据是否存在重复数据
        Map<String, Set<String>> repetitionMap = new HashMap<>();
        Iterator<T> it = successList.iterator();
        while (it.hasNext()) {
            T t = it.next();
            invokeStatus(t, pojoClass, "成功");
            Field[] fields = pojoClass.getDeclaredFields();
            Map<String, StringBuffer> groupMap = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                ExcelRepet excelRepet = field.getAnnotation(ExcelRepet.class);
                Excel excel = field.getAnnotation(Excel.class);
                if (excelRepet != null && excel != null) {
                    String groupName = excelRepet.groupName();
                    if (StringUtils.isEmpty(groupName)) {
                        groupName = excel.name();
                    }
                    if (groupMap.containsKey(groupName)) {
                        groupMap.get(groupName).append(field.get(t));
                    } else {
                        StringBuffer str = new StringBuffer(String.valueOf(field.get(t)));
                        groupMap.put(groupName, str);
                    }
                }

                if (excel != null && excel.type() == 2) {
                    String url = (String) field.get(t);
                    if (StringUtils.isNotEmpty(url)) {
                        File imageUrl = new File(url);
                        FileService fileService = SpringUtils.getBean(FileService.class);
                        FileInfo fileInfo = fileService.upload(FileUtil.getInputStream(imageUrl), imageUrl.getName(), FileTypeEnum.GOODS_PICTURE);
                        field.set(t, fileInfo.getKey());
                    }
                }
            }
            //迭代groupMap里面的数据
            StringBuffer repetMessge = new StringBuffer("");
            for (Map.Entry<String, StringBuffer> groupBuffer : groupMap.entrySet()) {
                if (repetitionMap.containsKey(groupBuffer.getKey())) {
                    Set<String> valueSet = repetitionMap.get(groupBuffer.getKey());
                    if (valueSet.contains(groupBuffer.getValue().toString())) {
                        repetMessge.append(groupBuffer.getKey()).append(":有重复数据;");
                    } else {
                        valueSet.add(groupBuffer.getValue().toString());
                    }
                } else {
                    Set<String> set = new HashSet<>();
                    set.add(groupBuffer.getValue().toString());
                    repetitionMap.put(groupBuffer.getKey(), set);
                }
            }
            String repetStr = repetMessge.toString();
            if (StringUtils.isNotEmpty(repetStr)) {
                String param = StringUtils.isNotEmpty(repetStr) ? repetStr.substring(0, repetStr.length() - 1) : "--";
                invokeErrorMsg(t, pojoClass, param);
                invokeStatus(t, pojoClass, "失败");

                failCount = failCount + 1;
                successCount = successCount - 1;
                failList.add(t);
                //移除有重复的数据
                it.remove();
            }
        }

        importResultModel.setList(successList);
        importResultModel.setSuccessCount(successCount);
        importResultModel.setFailCount(failCount);
        importResultModel.setFailList(failList);
        importResultModel.setSheetName(result.getFailWorkbook().getSheetName(0));
        return importResultModel;
    }

    /**
     * 返回结果在处理返回结果
     *
     * @param result
     * @param pojoClass
     * @param sheetName 页面名称
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> ImportResultModel transImportResult(ExcelImportResult<T> result,
                                                          Class<?> pojoClass,
                                                          String sheetName) throws Exception {
        ImportResultModel importResultModel = new ImportResultModel();
        List<T> failList = result.getFailList();
        List<T> successList = result.getList();
        Integer failCount = failList.size();
        Integer successCount = successList.size();
        //循环失败的信息
        for (T t : failList) {
            invokeStatus(t, pojoClass, "失败");
        }

        //循环成功的信息，然后判断在成功的数据是否存在重复数据
        Map<String, Set<String>> repetitionMap = new HashMap<>();
        Iterator<T> it = successList.iterator();
        while (it.hasNext()) {
            T t = it.next();
            invokeStatus(t, pojoClass, "成功");
            Field[] fields = pojoClass.getDeclaredFields();
            Map<String, StringBuffer> groupMap = new HashMap<>();
            StringBuffer hint = new StringBuffer("");
            Integer number = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                ExcelRepet excelRepet = field.getAnnotation(ExcelRepet.class);
                Excel excel = field.getAnnotation(Excel.class);
                if (excelRepet != null && excel != null) {
                    String groupName = excelRepet.groupName();
                    if (StringUtils.isEmpty(groupName)) {
                        groupName = excel.name();
                    }
                    if (groupMap.containsKey(groupName)) {
                        groupMap.get(groupName).append(field.get(t));
                    } else {
                        StringBuffer str = new StringBuffer(String.valueOf(field.get(t)));
                        groupMap.put(groupName, str);
                    }
                }

                if (excel != null && excel.type() == 2) {
                    String url = (String) field.get(t);
                    if (StringUtils.isNotEmpty(url)) {
                        File imageUrl = new File(url);
                        FileService fileService = SpringUtils.getBean(FileService.class);
                        FileInfo fileInfo = fileService.upload(FileUtil.getInputStream(imageUrl), imageUrl.getName(), FileTypeEnum.GOODS_PICTURE);
                        field.set(t, fileInfo.getKey());
                    }
                }

            }
            //迭代groupMap里面的数据
            StringBuffer repetMessge = new StringBuffer("");
            for (Map.Entry<String, StringBuffer> groupBuffer : groupMap.entrySet()) {
                if (repetitionMap.containsKey(groupBuffer.getKey())) {
                    Set<String> valueSet = repetitionMap.get(groupBuffer.getKey());
                    if (valueSet.contains(groupBuffer.getValue().toString())) {
                        repetMessge.append(groupBuffer.getKey()).append(":有重复数据;");
                    } else {
                        valueSet.add(groupBuffer.getValue().toString());
                    }
                } else {
                    Set<String> set = new HashSet<>();
                    set.add(groupBuffer.getValue().toString());
                    repetitionMap.put(groupBuffer.getKey(), set);
                }
            }
            String repetStr = repetMessge.toString();
            if (StringUtils.isNotEmpty(repetStr)) {
                String param = StringUtils.isNotEmpty(repetStr) ? repetStr.substring(0, repetStr.length() - 1) : "--";
                invokeErrorMsg(t, pojoClass, param);
                invokeStatus(t, pojoClass, "失败");

                failCount = failCount + 1;
                successCount = successCount - 1;
                failList.add(t);
                //移除有重复的数据
                it.remove();
            }
        }

        importResultModel.setList(successList);
        importResultModel.setSuccessCount(successCount);
        importResultModel.setFailCount(failCount);
        importResultModel.setFailList(failList);
        importResultModel.setSheetName(sheetName);
        return importResultModel;
    }

    private static <T> void invokeStatus(T t, Class<?> pojoClass, String message) {
        try {
            Class paramClass = Class.forName("java.lang.String");
            Method methodStatus = pojoClass.getMethod("setStatus", paramClass);
            methodStatus.invoke(t, (Object) message);
        } catch (Exception e) {
        }
    }

    private static <T> void invokeErrorMsg(T t, Class<?> pojoClass, String message) {
        try {
            Class paramClass = Class.forName("java.lang.String");
            Method methodStatus = pojoClass.getMethod("setErrorMsg", paramClass);
            methodStatus.invoke(t, (Object) message);
        } catch (Exception e) {
        }
    }
}
