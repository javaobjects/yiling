package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.yiling.open.erp.dto.ErpFlowControlDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowStringDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/9/27
 */
@RestController
@Slf4j
public class ErpOrderSaleFlowController extends OpenBaseController {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @DubboReference
    private ErpClientApi erpClientApi;
    @DubboReference
    private ErpHeartApi erpHeartApi;

    /**
     * 销售流向数据打包后同步
     * 客户端工具使用
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/orderSaleFlow/sync")
    public Result<String> customerSync(HttpServletRequest request, HttpServletResponse response) {
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
            ErpFlowControlDTO erpFlowControlDTO;
            try {
                erpFlowControlDTO = JSON.parseObject(json, ErpFlowControlDTO.class);
                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpFlowControlDTO);
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpFlowControlDTO.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpFlowControlDTO.setSuId(suId);
                dataMap.put(erpFlowControlDTO.getErpPrimaryKey(), erpFlowControlDTO);
            } catch (Exception e) {
                JSONObject obj = JSON.parseObject(json);
                if (StrUtil.isEmpty(obj.getString("fileKey"))) {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey()), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(
                        obj.getString(erpTaskInterface.getTaskKey() + OpenConstants.SPLITE_SYMBOL + obj.getString("fileKey")), e.getMessage()));
                }
                continue;
            }
        }
        //判断topic是否存在
        if (!CollectionUtils.isEmpty(dataMap.values())) {
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpFlowControl.getTopicName(), suId + "",
                DateUtil.formatDate(new Date()), JSON.toJSONString(dataMap.values()));
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
     * 销售流向数据同步
     * 调接口使用
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/orderSaleFlowInterface/sync")
    public Result<String> orderSaleFlowInterfaceSync(HttpServletRequest request, HttpServletResponse response) {
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
                ErpSaleFlowStringDTO erpSaleFlowStringDTO = JSON.parseObject(json, ErpSaleFlowStringDTO.class);
                ErpSaleFlowDTO erpSaleFlowDTO = buildErpSaleFlowDTO(erpSaleFlowStringDTO);

                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpSaleFlowDTO);
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpSaleFlowDTO.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpSaleFlowDTO.setSuId(suId);
                dataMap.put(erpSaleFlowDTO.getErpPrimaryKey(), erpSaleFlowDTO);
            } catch (Exception e) {
                JSONObject obj = JSON.parseObject(json);
                if (StrUtil.isEmpty(obj.getString("fileKey"))) {
                    errorList.add(new ErpErrorEntity(obj.getString(erpTaskInterface.getTaskKey()), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(
                            obj.getString(erpTaskInterface.getTaskKey() + OpenConstants.SPLITE_SYMBOL + obj.getString("fileKey")), e.getMessage()));
                }
                continue;
            }
        }
        //判断topic是否存在
        if (!CollectionUtils.isEmpty(dataMap.values())) {
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpSaleFlow.getTopicName(), suId + "",
                    DateUtil.formatDate(new Date()), JSON.toJSONString(dataMap.values()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                for (BaseErpEntity baseErpEntity : dataMap.values()) {
                    errorList.add(new ErpErrorEntity(baseErpEntity.getErpPrimaryKey(), "消息发送失败"));
                }
            }
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
        sysHeartBeat.setRuntaskIds(ErpTopicName.ErpSaleFlow.getMethod());
        sysHeartBeat.setVersions("");
        sysHeartBeat.setMac("");
        sysHeartBeat.setIp("");
        sysHeartBeat.setSuId(suId);
        sysHeartBeat.setSuName("");
        erpHeartApi.insertErpHeart(sysHeartBeat);
    }

    private ErpSaleFlowDTO buildErpSaleFlowDTO(ErpSaleFlowStringDTO erpSaleFlowStringDTO) {
        ErpSaleFlowDTO erpSaleFlowDTO = new ErpSaleFlowDTO();
        erpSaleFlowDTO.setSoId(erpSaleFlowStringDTO.getSoId());
        erpSaleFlowDTO.setSoNo(erpSaleFlowStringDTO.getSoNo());
        erpSaleFlowDTO.setSoTime(DateUtil.parse(erpSaleFlowStringDTO.getSoTime()));
        erpSaleFlowDTO.setOrderTime(DateUtil.parse(erpSaleFlowStringDTO.getOrderTime()));
        erpSaleFlowDTO.setEnterpriseInnerCode(erpSaleFlowStringDTO.getEnterpriseInnerCode());
        erpSaleFlowDTO.setEnterpriseName(erpSaleFlowStringDTO.getEnterpriseName());
        erpSaleFlowDTO.setSoBatchNo(erpSaleFlowStringDTO.getSoBatchNo());
        erpSaleFlowDTO.setSoQuantity(erpSaleFlowStringDTO.getSoQuantity());
        erpSaleFlowDTO.setSoProductTime(DateUtil.parse(erpSaleFlowStringDTO.getSoProductTime()));
        erpSaleFlowDTO.setSoEffectiveTime(DateUtil.parse(erpSaleFlowStringDTO.getSoEffectiveTime()));
        erpSaleFlowDTO.setSoPrice(erpSaleFlowStringDTO.getSoPrice());
        erpSaleFlowDTO.setGoodsInSn(erpSaleFlowStringDTO.getGoodsInSn());
        erpSaleFlowDTO.setGoodsName(erpSaleFlowStringDTO.getGoodsName());
        erpSaleFlowDTO.setSoLicense(erpSaleFlowStringDTO.getSoLicense());
        erpSaleFlowDTO.setSoSpecifications(erpSaleFlowStringDTO.getSoSpecifications());
        erpSaleFlowDTO.setSoUnit(erpSaleFlowStringDTO.getSoUnit());
        erpSaleFlowDTO.setSoManufacturer(erpSaleFlowStringDTO.getSoManufacturer());
        erpSaleFlowDTO.setSoSource(erpSaleFlowStringDTO.getSoSource());
        erpSaleFlowDTO.setSuDeptNo(erpSaleFlowStringDTO.getSuDeptNo());
        erpSaleFlowDTO.setOperType(erpSaleFlowStringDTO.getOperType());
        return erpSaleFlowDTO;
    }

}
