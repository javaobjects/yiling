package com.yiling.export.export.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.export.export.dao.ExportTaskRecordMapper;
import com.yiling.export.export.dto.ExportSearchConditionDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;
import com.yiling.export.export.entity.ExportTaskRecordDO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.export.export.service.ExportTaskRecordService;
import com.yiling.export.export.util.ExportExcelUtil;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 下载中心服务
 *
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@Slf4j
@Service
public class ExportTaskRecordServiceImpl extends BaseServiceImpl<ExportTaskRecordMapper, ExportTaskRecordDO> implements ExportTaskRecordService {

    @Autowired
    private FileService fileService;

    @Autowired
    @Lazy
    ExportTaskRecordServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    public DataSourceTransactionManager transactionManager;

    @Override
    public Boolean saveExportTaskRecord(SaveExportTaskRequest request) {
        // 1分钟内只能一次操作
        String key = new StringBuilder(request.getOpUserId().toString()).append(":").append(request.getGroupName()).append(":")
                .append(request.getClassName()).toString();
        if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key))) {
            stringRedisTemplate.opsForValue().set(key.toString(), "1", 1, TimeUnit.MINUTES);
        } else {
            throw new BusinessException(ResultCode.FAILED, "1分钟内请勿重复操作下载功能");
        }

        String fileName = request.getFileName();
        if (StringUtils.isEmpty(fileName)) {
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "创建导出任务参数异常");
        }
        BaseExportQueryDataService baseService = (BaseExportQueryDataService) SpringUtils.getBean(request.getClassName());
        if(null == baseService){
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "下载业务不存在");
        }
        request.setFileName(this.getFileName(fileName,baseService.getFileSuffix()));
        ExportTaskRecordDO exportTaskRecord = PojoUtils.map(request, ExportTaskRecordDO.class);
        // 删除为空的条件
        List<ExportSearchConditionDTO> searchConditionList = request.getSearchConditionList();
        if (CollectionUtils.isNotEmpty(searchConditionList)) {
            for (int i = searchConditionList.size() - 1; i >= 0; i--) {
                ExportSearchConditionDTO con = searchConditionList.get(i);
                if (StringUtils.isEmpty(con.getName()) || StringUtils.isEmpty(con.getValue())) {
                    searchConditionList.remove(i);
                }
            }
        }
        exportTaskRecord.setSearchCondition(JSON.toJSONString(searchConditionList)).setStatus(0);
        this.save(exportTaskRecord);

        //发送mq队列 改为异步调用
        _this.sendMq("export_file_center", "",exportTaskRecord.getId().toString());
        /*if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            throw new BusinessException(ResultCode.PARAM_VALID_ERROR, "下载中心发送mq服务报错");
        }*/
        return true;
    }

    @Override
    public Page<ExportTaskRecordDO> pageList(QueryExportPageListRequest request) {
        LambdaQueryWrapper<ExportTaskRecordDO> lambdaQueryWrapper = new QueryWrapper<ExportTaskRecordDO>().lambda();
        lambdaQueryWrapper.eq(ExportTaskRecordDO::getCreateUser, request.getUserId());
        lambdaQueryWrapper.orderByDesc(ExportTaskRecordDO::getId);

        Page<ExportTaskRecordDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, lambdaQueryWrapper);
    }

    @Override
    public ExportTaskRecordDO findExportTaskRecord(Long userId, Long id) {
        LambdaQueryWrapper<ExportTaskRecordDO> lambdaQueryWrapper = new QueryWrapper<ExportTaskRecordDO>().lambda();
        lambdaQueryWrapper.eq(ExportTaskRecordDO::getId, id).eq(ExportTaskRecordDO::getCreateUser, userId);
        return this.getOne(lambdaQueryWrapper);
    }

    /**
     * 生成文件名称
     *
     * @param prifix
     * @return
     */
    private String getFileName(String prifix,String suffix) {
        // 生成日期格式字符串
        String dateStr = DateUtil.format(new Date(), "MMddHHmm");
        // 生成5位数字随机数
        String randStr = RandomUtil.randomNumbers(5);
        return new StringBuilder(prifix).append("_").append(dateStr).append(randStr).append(".").append(suffix).toString();
    }

    /**
     * 通过mq异步执行
     *
     * @param exportTaskRecordId
     */
    @Override
    public void syncUploadExportTask(Long exportTaskRecordId) {
        ExportTaskRecordDO record = this.getById(exportTaskRecordId);
        if (record == null) {
            log.error("[ExportTaskRecordService][uploadExportTask]数据不存在，任务id：{}", exportTaskRecordId);
            return;
        }
        if (record.getStatus() == 1) {
            log.error("[ExportTaskRecordService][uploadExportTask]文件上传成功，任务id：{}", exportTaskRecordId);
            return;
        }
        if (record.getStatus() == -1) {
            log.error("[ExportTaskRecordService][uploadExportTask]文件已经处理失败，任务id：{}", exportTaskRecordId);
            return;
        }
        long start = System.currentTimeMillis();
        log.info("[ExportTaskRecordService][uploadExportTask]开始查询数据，任务id：{}", exportTaskRecordId);
        // 1、查询数据并获取文件上传InputStream流
        InputStream inputStream = null;
        try {
            inputStream = this.getDataInputStream(exportTaskRecordId, record.getSearchCondition(), record.getClassName(), record.getFileName());
        } catch (BusinessException be){
            record.setStatus(-1).setRemark(be.getMessage()).setFinishTime(new Date());
            this.updateById(record);
            return;
        }catch (Exception e) {
            log.error("[ExportTaskRecordService][uploadExportTask]生成文件数据出现异常。任务id：{}", exportTaskRecordId, e);
            record.setStatus(-1).setRemark("生成文件数据出现异常，请稍后重试").setFinishTime(new Date());
            this.updateById(record);
            return;
        }
        // 2、上传到服务器
        FileInfo fileInfo = null;
        try {
            fileInfo = fileService.upload(inputStream, record.getFileName(), FileTypeEnum.FILE_EXPORT_CENTER);
        } catch (Exception e) {
            log.error("[ExportTaskRecordService][uploadExportTask]上传excel出现异常。任务id：{}", exportTaskRecordId, e);
            record.setStatus(-1).setRemark("excel上传服务器失败，请稍后重试").setFinishTime(new Date());
            this.updateById(record);
            return;
        }
        // 输出Excel文件，本地测试
        log.info("[ExportTaskRecordService][uploadExportTask]上传excel结束。任务id：{}，耗时：{}", exportTaskRecordId,
                (System.currentTimeMillis() - start));

        if (fileInfo == null) {
            log.error("[ExportTaskRecordService][uploadExportTask]数据上传失败，任务id：{}", exportTaskRecordId);
            return;
        }
        // 4、更新状态
        record.setStatus(1).setFinishTime(new Date()).setRequestUrl(fileInfo.getKey());
        this.updateById(record);
    }

    /**
     * 查询excel中的数据
     *
     * @param exportTaskRecordId
     * @param searchCondition
     * @param className
     * @return
     */
    private InputStream getDataInputStream(Long exportTaskRecordId, String searchCondition, String className, String fileName) {
        List<ExportSearchConditionDTO> searchConditionList = JSON.parseArray(searchCondition, ExportSearchConditionDTO.class);
        Map<String, Object> conditionMap = new HashMap<>(16);
        conditionMap.put("opUserId", Optional.ofNullable(this.getById(exportTaskRecordId)).orElse(new ExportTaskRecordDO()).getCreateUser());
        searchConditionList.forEach(e -> conditionMap.put(e.getName(), e.getValue()));
        try {
            BaseExportQueryDataService baseService = (BaseExportQueryDataService) SpringUtils.getBean(className);
            if(baseService.isReturnData()){
                QueryExportDataDTO queryExport = baseService.queryData(baseService.getParam(conditionMap));
                if (queryExport.getStatus().equals(2)) {
                    throw new BusinessException(ResultCode.FAILED,queryExport.getMessage());
                }
                if (null == queryExport.getSheets()) {
                    throw new BusinessException(ResultCode.FAILED,StringUtils.isEmpty(queryExport.getMessage()) ? "导出数据为空" : queryExport.getMessage());
                }
                log.info("[ExportTaskRecordService][uploadExportTask]查询数据结束，开始生成excel。任务id：{}", exportTaskRecordId);
                return getExcelInputStream(queryExport,exportTaskRecordId,baseService.getFileSuffix());
            }else {
                byte[] dataByte = baseService.getExportByte(baseService.getParam(conditionMap), fileName);
                if(null == dataByte || dataByte.length == 0){
                    throw new BusinessException(ResultCode.FAILED,"导出数据流为空");
                }
                log.info("[ExportTaskRecordService][uploadExportTask]获取数据流结束。任务id：{}", exportTaskRecordId);
                return  new ByteArrayInputStream(dataByte);
            }
        }catch (BusinessException be){
            throw be;
        }catch (Exception e) {
            log.error("[ExportTaskRecordService][queryData]获取上传数据流出现异常。任务id：{}，className：{}", exportTaskRecordId,
                    className, e);
            throw new BusinessException(ResultCode.FAILED, "系统异常，数据导出失败");
        }
    }

    /**
     * 实现类返回数据默认生成 excel 的上传流
     * @param queryExport
     * @param exportTaskRecordId
     * @return
     * @throws Exception
     */
    private InputStream getExcelInputStream(QueryExportDataDTO queryExport, Long exportTaskRecordId,String excelSuffix) throws Exception{
        ExcelType excelType=null;
        if("xls".equals(excelSuffix)){
            excelType = ExcelType.HSSF;
        }else if("xlsx".equals(excelSuffix)){
            excelType = ExcelType.XSSF;
        }
        //根据这里调整是否使用模版; 模版地址，数据，扩展名
        if(queryExport.isUseExcelTemplete()){
            byte[] excelByte = ExportExcelUtil.listToExcelByTemplete(queryExport.getSheets(), excelType,queryExport.getExcelTempletePath());
            log.info("[ExportTaskRecordService][uploadExportTask]生成模版excel结束，开始上传excel。任务id：{}。当前excel大小{}kb",
                    exportTaskRecordId, (excelByte == null ? 0 : (excelByte.length / 1024)));
            return new ByteArrayInputStream(excelByte);
        }
        byte[] excelByte = ExportExcelUtil.listToExcel(queryExport.getSheets(), excelType);
        log.info("[ExportTaskRecordService][uploadExportTask]生成excel结束，开始上传excel。任务id：{}。当前excel大小{}kb",
                exportTaskRecordId, (excelByte == null ? 0 : (excelByte.length / 1024)));
        return new ByteArrayInputStream(excelByte);
    }




    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
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
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
