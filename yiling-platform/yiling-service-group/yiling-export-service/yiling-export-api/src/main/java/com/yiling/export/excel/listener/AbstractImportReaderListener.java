package com.yiling.export.excel.listener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.hibernate.validator.HibernateValidator;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.handler.IEasyExcelVerifyHandler;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import com.yiling.export.excel.model.ImportResultModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractImportReaderListener<T extends EasyExcelBaseModel> implements ReadListener<T> {
    protected              ImportResultModel       importResultModel;
    protected final        Validator               validator              = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory()
            .getValidator();
    protected static final int                     BATCH_COUNT            = 100;
    private static final   String                  SERIAL_VERSION_UID     = "serialVersionUID";
    protected              List<T>                 cachedDataList         = new ArrayList<>(BATCH_COUNT);
    private                Map<String, Integer>    fieldColumnMapping;
    private                List<List<String>>      orgHeads;
    private                IEasyExcelVerifyHandler easyExcelVerifyHandler = null;
    private                boolean                 afterSave              = false;
    private                boolean                 onlyExportFail         = true;
    private                Integer                 headRowNumber=1;
    private                Map<String, Object>     paramMap               = null;


    private List<String> sheetNames;

    public List<List<String>> getOrgHeads() {
        return orgHeads;
    }

    public void setOrgHeads(List<List<String>> orgHeads) {
        this.orgHeads = orgHeads;
    }

    public void setImportResultModel(ImportResultModel importResultModel) {
        this.importResultModel = importResultModel;
    }

    public ImportResultModel getImportResultModel() {
        return this.importResultModel;
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            //todo添加失败记录
            EasyExcelBaseModel data = new EasyExcelBaseModel();
            data.setRowNum(excelDataConvertException.getRowIndex());
            data.setErrorMsg("数据类型不符合要求");
            data.setStatus(1);
            importResultModel.getFailList().add(data);
            log.error("第{}行，第{}列解析异常，数据为:{},异常：{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData(), excelDataConvertException.getMessage());
            return;
        }
        throw new AbstractImportReadException(exception.getMessage());
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.debug("解析到一条头数据:{}", JSON.toJSONString(headMap));
        // this.sheetNames=Optional.of(context.readSheetList().stream().map(ReadSheet::getSheetName).collect(Collectors.toList())).orElse(Lists.newArrayList());
        log.info("sheetNames={},orgHeads{}", context.readSheetHolder().getSheetName(), orgHeads);
        orgHeads = new ArrayList<>(16);
        for (final Map.Entry<Integer, ReadCellData<?>> entry : headMap.entrySet()) {
            orgHeads.add(Arrays.asList(entry.getValue().getStringValue()));
        }
    }


    @Override
    public void invoke(T data, AnalysisContext context) {
        log.debug("解析到一条数据:{}", JSON.toJSONString(data));
        try {
            validate(data, context);
            verifyData(data, context);
            data.setRowNum(context.readRowHolder().getRowIndex());
            data.setStatus(2);
            //cachedDataList.add(data);
            if (!afterSave) {
                if (cachedDataList.size() >= BATCH_COUNT) {
                    log.debug("批量处理{}", cachedDataList.size());
                    saveData(paramMap);
                    addData();
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    log.debug("清理缓存后{}", cachedDataList.size());
                }
            }
        } catch (ExcelDataValidException e) {
            data.setStatus(1);
            data.setErrorMsg(e.getMessage());
            data.setRowNum(e.getRowIndex());
        }finally {
            cachedDataList.add(data);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData(paramMap);
        addData();
    }

    /**
     * 加上存储数据库
     */
    public abstract void saveData(Map<String, Object>     paramMap);

    protected abstract EasyExcelVerifyHandlerResult verify(T model);

    public boolean isOnlyExportFail() {
        return onlyExportFail;
    }

    public void setOnlyExportFail(boolean onlyExportFail) {
        this.onlyExportFail = onlyExportFail;
    }

    public boolean isAfterSave() {
        return afterSave;
    }

    public void setAfterSave(boolean afterSave) {
        this.afterSave = afterSave;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Integer getHeadRowNumber() {
        return headRowNumber;
    }

    public void setHeadRowNumber(Integer headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    @Deprecated
    public boolean checkObjAllFieldsIsNull(T object) {
        if (null == object) {
            return true;
        }
        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                //只校验带ExcelProperty注解的属性
                ExcelProperty property = f.getAnnotation(ExcelProperty.class);
                if (property == null || SERIAL_VERSION_UID.equals(f.getName())) {
                    continue;
                }
                if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    object.setStatus(2);
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("对象解析失败:{}");
            object.setErrorMsg(e.getMessage());
            object.setStatus(1);
            return true;
        }
        return true;
    }

    /**
     * Valid.
     *
     * @param data    the data
     * @param context the context
     */
    protected final void verifyData(final T data, final AnalysisContext context) {
        EasyExcelVerifyHandlerResult easyExcelVerifyHandlerResult = this.verify(data);
        if (easyExcelVerifyHandlerResult == null) {
            return;
        }
        if (easyExcelVerifyHandlerResult.isSuccess()) {
            return;
        }
        final String field = easyExcelVerifyHandlerResult.getField();
        final String message = easyExcelVerifyHandlerResult.getMsg();
        final Object value = easyExcelVerifyHandlerResult.getValue();

        final Integer rowIndex = context.readRowHolder().getRowIndex();
        final Integer columnIndex = this.getColumnIndex(field, context);
        if (log.isDebugEnabled()) {
            log.debug("Validate field:[{}],message:[{}],value:[{}]", field, message, value);
        }
        throw new ExcelDataValidException(rowIndex, columnIndex, message);
    }

    /**
     * Valid.
     *
     * @param data    the data
     * @param context the context
     */
    protected final void validate(final T data, final AnalysisContext context) {
        final Optional<ConstraintViolation<T>> violationOptional = this.validator.validate(data, Default.class).stream().findFirst();
        log.debug("violationOptional的状态{}", violationOptional.isPresent());
        if (!violationOptional.isPresent()) {
            return;
        }
        final ConstraintViolation<T> violation = violationOptional.get();
        final String field = violation.getPropertyPath().toString();
        final String message = violation.getMessage();
        final Object value = violation.getInvalidValue();
        final Integer rowIndex = context.readRowHolder().getRowIndex();
        final Integer columnIndex = this.getColumnIndex(field, context);
        if (log.isDebugEnabled()) {
            log.debug("Validate field:[{}],message:[{}],value:[{}]", field, message, value);
        }
        throw new ExcelDataValidException(rowIndex, columnIndex, message);
    }

    public EasyExcelVerifyHandlerResult error(String message, String field, String value) {
        return new EasyExcelVerifyHandlerResult(false, message, field, value);
    }

    /**
     * 听过属性从context获取列号
     *
     * @param field   属性名称
     * @param context excel上下文
     * @return 列号
     */
    private Integer getColumnIndex(final String field, final AnalysisContext context) {
        if (Objects.nonNull(this.fieldColumnMapping)) {
            return this.fieldColumnMapping.get(field);
        }
        synchronized (this) {
            if (Objects.nonNull(this.fieldColumnMapping)) {
                return this.fieldColumnMapping.get(field);
            }
            this.fieldColumnMapping = new HashMap<>(16);
            final Map<Integer, Head> excelFileHead = context.readSheetHolder().excelReadHeadProperty().getHeadMap();
            for (final Map.Entry<Integer, Head> entry : excelFileHead.entrySet()) {
                this.fieldColumnMapping.put(entry.getValue().getFieldName(), entry.getKey());
            }
            return this.fieldColumnMapping.get(field);
        }
    }

    /**
     * 添加失败记录
     *
     * @param data
     */
    private void addFailList(T data) {
        importResultModel.getFailList().add(data);
    }

    /**
     * 添加成功条数
     */
    private void addData() {
        for (T t : cachedDataList) {
            if (t.getStatus() == 1) {
                addFailList(t);
            } else {
                importResultModel.getList().add(t);
            }
        }
    }
}
