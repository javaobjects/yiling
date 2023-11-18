package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpErrorEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowStringDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;
import com.yiling.open.erp.validation.group.InterFaceValidation;
import com.yiling.open.ftp.api.FlowFtpApi;
import com.yiling.open.ftp.dto.LocalCompareDTO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Slf4j
@RestController
public class ErpGoodsBatchFlowController extends OpenBaseController {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @DubboReference
    private FlowFtpApi flowFtpApi;
    @DubboReference
    private ErpClientApi erpClientApi;
    @DubboReference
    private ErpHeartApi erpHeartApi;

    private String key = "erp-goods-batch-flow-local-compare";
    private String keyTime = "erp-goods-batch-flow-local-compare-time";

    /**
     * pop客户信息同步
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/goodsBatchFlow/sync")
    public Result<String> goodsBatchFlowSync(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        List<String> stringList = JSON.parseArray(body, String.class);
        if (CollectionUtils.isEmpty(stringList)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        // 保存错误信息
        List<ErpErrorEntity> errorList = new ArrayList<>();
        // 保存去重后的请求数据
        Map<String, BaseErpEntity> dataMap = new LinkedHashMap<>();

        for (String json : stringList) {
            try {
                ErpGoodsBatchFlowStringDTO erpGoodsBatchFlowString = JSON.parseObject(json, ErpGoodsBatchFlowStringDTO.class);
                ErpGoodsBatchFlowDTO erpGoodsBatchFlow = buildErpGoodsBatchFlowDTO(erpGoodsBatchFlowString);

                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpGoodsBatchFlow);
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpGoodsBatchFlow.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpGoodsBatchFlow.setSuId(suId);
                dataMap.put(erpGoodsBatchFlow.getErpPrimaryKey(), erpGoodsBatchFlow);
            } catch (Exception e) {
                JSONObject obj = JSON.parseObject(json);
                if (StrUtil.isEmpty(obj.getString("su_dept_no"))) {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey()), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey() + OpenConstants.SPLITE_SYMBOL + obj.getString("su_dept_no")), e.getMessage()));
                }
                continue;
            }
        }

        //判断topic是否存在
        if (!CollectionUtils.isEmpty(dataMap.values())) {
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatchFlow.getTopicName(), suId + "", DateUtil.formatDateTime(new Date()), JSON.toJSONString(dataMap.values()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                for (BaseErpEntity baseErpEntity : dataMap.values()) {
                    errorList.add(new ErpErrorEntity(baseErpEntity.getErpPrimaryKey(), "消息发送失败"));
                }
            }

        }

        if (CollectionUtils.isEmpty(errorList)) {
            return Result.success();
        } else {
            return Result.failed(JSON.toJSONString(errorList));
        }
    }


    /**
     * pop客户信息同步
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/goodsBatchFlowInterface/sync")
    public Result<String> goodsBatchFlowInterfaceSync(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        List<String> stringList = JSON.parseArray(body, String.class);
        if (CollectionUtils.isEmpty(stringList)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        // 保存错误信息
        List<ErpErrorEntity> errorList = new ArrayList<>();
        // 保存去重后的请求数据
        Map<String, BaseErpEntity> dataMap = new LinkedHashMap<>();
        Set<Date> times = new HashSet<>();
        for (String json : stringList) {
            try {
                ErpGoodsBatchFlowStringDTO erpGoodsBatchFlowString = JSON.parseObject(json, ErpGoodsBatchFlowStringDTO.class);
                ErpGoodsBatchFlowDTO erpGoodsBatchFlow = buildErpGoodsBatchFlowDTO(erpGoodsBatchFlowString);
                erpGoodsBatchFlow.setGbIdNo(IdUtil.fastSimpleUUID());
                times.add(erpGoodsBatchFlow.getGbTime());

                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpGoodsBatchFlow, new Class[]{ InterFaceValidation.class });
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpGoodsBatchFlow.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpGoodsBatchFlow.setSuId(suId);
                dataMap.put(erpGoodsBatchFlow.getErpPrimaryKey(), erpGoodsBatchFlow);
            } catch (Exception e) {
                JSONObject obj = JSON.parseObject(json);
                if (StrUtil.isEmpty(obj.getString("su_dept_no"))) {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey()), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey() + OpenConstants.SPLITE_SYMBOL + obj.getString("su_dept_no")), e.getMessage()));
                }
                continue;
            }
        }

        if (times.size() > 1) {
            return Result.failed("库存流向入库时间不一致");
        }

        //判断topic是否存在
        if (!CollectionUtils.isEmpty(dataMap.values())) {
            Date nowDate = new Date();
            Object dateObject = stringRedisTemplate.opsForHash().get(key, String.valueOf(suId));
            if (dateObject != null) {
                Date dateTime = DateUtil.parseDateTime(String.valueOf(dateObject));
                if (DateUtil.compare(dateTime, times.iterator().next()) == 0) {
                   Object dateObjectTime= stringRedisTemplate.opsForHash().get(keyTime, String.valueOf(suId));
                   if(dateObjectTime!=null){
                       nowDate = DateUtil.parseDateTime(String.valueOf(dateObjectTime));
                   }
                }
            }

            stringRedisTemplate.opsForHash().put(key, String.valueOf(suId), DateUtil.formatDateTime(times.iterator().next()));
            stringRedisTemplate.opsForHash().put(keyTime, String.valueOf(suId), DateUtil.formatDateTime(nowDate));
            stringRedisTemplate.expire(key,24, TimeUnit.HOURS);
            stringRedisTemplate.expire(keyTime,24, TimeUnit.HOURS);
            LocalCompareDTO localCompareDTO=new LocalCompareDTO();
            localCompareDTO.setDataList(new ArrayList<>(dataMap.values()));
            localCompareDTO.setSuId(suId);
            localCompareDTO.setTime(nowDate);
            flowFtpApi.localCompare(localCompareDTO);
            // 记录心跳最后时间
            lastHeartBeatTime(suId);
        }

        if (CollectionUtils.isEmpty(errorList)) {
            return Result.success();
        } else {
            return Result.failed(JSON.toJSONString(errorList));
        }
    }

    /**
     * 记录心跳最后时间
     *
     * @param suId
     */
    private void lastHeartBeatTime(Long suId){
        // 保存心跳最新时间
        UpdateHeartBeatTimeRequest erpClientRequest = new UpdateHeartBeatTimeRequest();
        erpClientRequest.setSuId(suId);
        erpClientRequest.setOpUserId(0L);
        erpClientRequest.setVersions("");
        erpClientApi.updateHeartBeatTimeBySuid(erpClientRequest);
        // 保存心跳
        SysHeartBeatDTO sysHeartBeat = new SysHeartBeatDTO();
        sysHeartBeat.setProcessId("");
        sysHeartBeat.setRunPath("");
        sysHeartBeat.setRuntaskIds(ErpTopicName.ErpGoodsBatchFlow.getMethod());
        sysHeartBeat.setVersions("");
        sysHeartBeat.setMac("");
        sysHeartBeat.setIp("");
        sysHeartBeat.setSuId(suId);
        sysHeartBeat.setSuName("");
        erpHeartApi.insertErpHeart(sysHeartBeat);
    }

    private ErpGoodsBatchFlowDTO buildErpGoodsBatchFlowDTO(ErpGoodsBatchFlowStringDTO erpGoodsBatchFlowString) {
        ErpGoodsBatchFlowDTO erpGoodsBatchFlow = new ErpGoodsBatchFlowDTO();
        erpGoodsBatchFlow.setGbIdNo(erpGoodsBatchFlowString.getGbIdNo());
        erpGoodsBatchFlow.setInSn(erpGoodsBatchFlowString.getInSn());
        erpGoodsBatchFlow.setGbBatchNo(erpGoodsBatchFlowString.getGbBatchNo());
        erpGoodsBatchFlow.setGbTime(DateUtil.parse(erpGoodsBatchFlowString.getGbTime()));
        erpGoodsBatchFlow.setGbNumber(erpGoodsBatchFlowString.getGbNumber());
        erpGoodsBatchFlow.setGbProduceAddress(erpGoodsBatchFlowString.getGbProduceAddress());
        erpGoodsBatchFlow.setGbName(erpGoodsBatchFlowString.getGbName());
        erpGoodsBatchFlow.setGbLicense(erpGoodsBatchFlowString.getGbLicense());
        erpGoodsBatchFlow.setGbSpecifications(erpGoodsBatchFlowString.getGbSpecifications());
        erpGoodsBatchFlow.setGbUnit(erpGoodsBatchFlowString.getGbUnit());
        erpGoodsBatchFlow.setGbManufacturer(erpGoodsBatchFlowString.getGbManufacturer());
        erpGoodsBatchFlow.setSuDeptNo(erpGoodsBatchFlowString.getSuDeptNo());
        erpGoodsBatchFlow.setOperType(ObjectUtil.isNull(erpGoodsBatchFlowString.getOperType()) ? 1 : erpGoodsBatchFlowString.getOperType());
        // 生产日期、有效期
        String gbProduceTime = erpGoodsBatchFlowString.getGbEndTime();
        String gbEndTime = erpGoodsBatchFlowString.getGbProduceTime();
        Date gbProduceTimeDate = StrUtil.isBlank(gbProduceTime) ? null : DateUtil.parse(gbProduceTime.trim());
        Date gbEndTimeDate = StrUtil.isBlank(gbEndTime) ? null : DateUtil.parse(gbEndTime.trim());
        erpGoodsBatchFlow.setGbEndTime(ObjectUtil.isNull(gbProduceTimeDate) ? "" : DateUtil.format(gbProduceTimeDate, "yyyy-MM-dd HH:mm:ss"));
        erpGoodsBatchFlow.setGbProduceTime(ObjectUtil.isNull(gbEndTimeDate) ? "" : DateUtil.format(gbEndTimeDate, "yyyy-MM-dd HH:mm:ss"));
        return erpGoodsBatchFlow;
    }

}
