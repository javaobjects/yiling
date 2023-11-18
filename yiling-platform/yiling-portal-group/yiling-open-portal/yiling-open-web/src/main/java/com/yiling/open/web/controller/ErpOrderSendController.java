package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpErrorEntity;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpOrderSendController extends OpenBaseController {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    /**
     * 发货处理逻辑比较特殊，如果send_type有值需要重新排序
     * @param request
     * @param response
     * @return
     */
    @ErpLogAnnotation
    @PostMapping(path = "/order/deliver")
    public Result<String> deliver(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
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
            ErpOrderSendDTO erpOrderSend;
            try {
                erpOrderSend = JSON.parseObject(json, ErpOrderSendDTO.class);
                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpOrderSend);
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpOrderSend.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpOrderSend.setSuId(suId);
                dataMap.put(erpOrderSend.getErpPrimaryKey(), erpOrderSend);
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
        if(!CollectionUtils.isEmpty(dataMap.values())) {
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpOrderSend.getTopicName(), suId + "", DateUtil.formatDate(new Date()), JSON.toJSONString(dataMap.values()));
            if (sendResult == null || !sendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
                for (BaseErpEntity baseErpEntity : dataMap.values()) {
                    errorList.add(new ErpErrorEntity(baseErpEntity.getErpPrimaryKey(), "消息发送失败"));
                }
            }
        }

        if(CollectionUtils.isEmpty(errorList)) {
            return Result.success();
        }else{
            return Result.failed(JSON.toJSONString(errorList));
        }
    }


}