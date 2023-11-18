package com.yiling.export.excel.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.export.excel.dao.ExcelTaskRecordMapper;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.export.excel.dto.request.QueryExcelTaskRecordPageListRequest;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.entity.ExcelTaskConfigDO;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.service.ExcelTaskConfigService;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.afterturn.easypoi.exception.excel.enums.ExcelImportEnum;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 导入日志表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Slf4j
@Service
public class ExcelTaskRecordServiceImpl extends BaseServiceImpl<ExcelTaskRecordMapper, ExcelTaskRecordDO> implements ExcelTaskRecordService {

    @Autowired
    FileService                fileService;
    @Autowired
    ExcelTaskConfigService     excelTaskConfigService;
    @Autowired
    StringRedisTemplate        stringRedisTemplate;
    @Autowired
    @Lazy
    ExcelTaskRecordServiceImpl _this;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public void syncImportExcelTask(Long excelTaskRecordId) {
        ExcelTaskRecordDO record = this.getById(excelTaskRecordId);
        if (record == null) {
            log.error("[ExcelTaskRecordService][syncImportExcelTask]数据不存在，任务id：{}", excelTaskRecordId);
            return;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, record.getCreateUser());
        paramMap.put(MyMetaHandler.FIELD_OP_EID, record.getEid());
        paramMap.put(ImportConstants.FIELD_OP_CLASS_NAME, record.getClassName());
        paramMap.put(ImportConstants.FIELD_VERIFY_HANDLER_BEAN_NAME, record.getVerifyHandlerBeanName());
        paramMap.put(ImportConstants.FIELD_IMPORT_DATA_HANDLER_BEAN_NAME, record.getImportDataHandlerBeanName());
        paramMap.put(ImportConstants.FIELD_MODEL_CLASS, record.getModelClass());
        paramMap.put(ImportConstants.TASK_CONFIG_ID, record.getTaskConfigId());
        paramMap.put(ImportConstants.TASK_RECORD_ID, record.getId());
        paramMap.put(ImportConstants.PARAM, record.getParam());
        log.info("paramMap{}", paramMap);
        Integer i = this.baseMapper.updateExcelTaskRecord(excelTaskRecordId, 0, 1);
        if (i != 1) {
            log.error("[ExcelTaskRecordService][syncImportExcelTask]消费数据状态不正确，任务id：{}", excelTaskRecordId);
            return;
        }

        ExcelTaskConfigDO excelTaskConfigDO = excelTaskConfigService.getById(record.getTaskConfigId());
        if (excelTaskConfigDO == null) {
            log.error("[ExcelTaskRecordService][syncImportExcelTask]配置信息不存在，任务id：{}", excelTaskRecordId);
            return;
        }

        ImportResultModel importResultModel = null;
        try {
            String url = fileService.getUrl(record.getRequestUrl(), FileTypeEnum.EXCEL_IMPORT_RESULT);
            byte[] bytes = HttpUtil.downloadBytes(url);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BaseExcelImportService baseExcelImportService = (BaseExcelImportService) SpringUtils.getBean(record.getClassName());
            paramMap.put(ImportConstants.BYTE, bytes);
            importResultModel = baseExcelImportService.importExcel(inputStream, excelTaskConfigDO.getLimitNumber(), paramMap);
        } catch (BusinessException be) {
            record.setStatus(3).setRemark(be.getMessage()).setFinishTime(new Date());
            this.updateById(record);
            return;
        } catch (Exception e) {
            log.error("[ExcelTaskRecordService][syncImportExcelTask]文件解析出现异常。任务id：{}", excelTaskRecordId, e);
            String remark = "文件解析出现异常，请稍后重试";
            if (Objects.equals(ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE.getMsg(), e.getMessage())) {
                remark = ExcelImportEnum.IS_NOT_A_VALID_TEMPLATE.getMsg();
            }
            record.setStatus(3).setRemark(remark).setFinishTime(new Date());
            this.updateById(record);
            return;
        }
        if (importResultModel == null) {
            record.setStatus(3).setRemark("文件解析数据为空").setFinishTime(new Date());
            this.updateById(record);
            return;
        }
        record.setStatus(2).setFinishTime(new Date())
                .setSuccessNumber(importResultModel.getSuccessCount())
                .setFailNumber(importResultModel.getFailCount())
                .setResultUrl(importResultModel.getResultUrl());
        this.updateById(record);
    }

    @Override
    public Long saveExcelTaskRecord(SaveExcelTaskRecordRequest request) {
        String fileName = request.getFileName();
        if (StringUtils.isEmpty(fileName)) {
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "创建导入任务参数异常");
        }
        BaseExcelImportService baseService = (BaseExcelImportService) SpringUtils.getBean(request.getClassName());
        if (null == baseService) {
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "导入业务不存在");
        }
        ExcelTaskRecordDO exportTaskRecord = PojoUtils.map(request, ExcelTaskRecordDO.class);
        this.save(exportTaskRecord);

        //发送mq队列 改为异步调用
        _this.sendMq(Constants.TOPIC_IMPORT_CREATE_EXCEL, Constants.TAG_TOPIC_IMPORT_CREATE_EXCEL,exportTaskRecord.getId().toString());
        return exportTaskRecord.getId();
    }

    @Override
    public Page<ExcelTaskRecordDTO> pageList(QueryExcelTaskRecordPageListRequest request) {
        LambdaQueryWrapper<ExcelTaskRecordDO> lambdaQueryWrapper = new QueryWrapper<ExcelTaskRecordDO>().lambda();
        lambdaQueryWrapper.eq(ExcelTaskRecordDO::getTaskConfigId, request.getTaskConfigId())
                .eq(ExcelTaskRecordDO::getSource, request.getSource())
                .eq(ExcelTaskRecordDO::getEid, request.getEid());
        Date createTimeStart = request.getCreateTimeStart();
        if (ObjectUtil.isNotNull(createTimeStart)) {
            lambdaQueryWrapper.ge(ExcelTaskRecordDO::getCreateTime, DateUtil.beginOfDay(createTimeStart));
        }
        Date createTimeEnd = request.getCreateTimeEnd();
        if (ObjectUtil.isNotNull(createTimeEnd)) {
            lambdaQueryWrapper.le(ExcelTaskRecordDO::getCreateTime, DateUtil.endOfDay(createTimeEnd));
        }

        lambdaQueryWrapper.orderByDesc(ExcelTaskRecordDO::getId);

        Page<ExcelTaskRecordDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(this.page(page, lambdaQueryWrapper), ExcelTaskRecordDTO.class);
    }

    @Override
    public ExcelTaskRecordDTO findExcelTaskRecordById(Long id) {
        return PojoUtils.map(this.getById(id), ExcelTaskRecordDTO.class);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
