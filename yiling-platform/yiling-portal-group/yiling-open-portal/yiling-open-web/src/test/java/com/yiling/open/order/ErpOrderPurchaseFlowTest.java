package com.yiling.open.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpErrorEntity;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowStringDTO;
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
 * @date: 2022/4/21
 */
@Slf4j
public class ErpOrderPurchaseFlowTest extends BaseTest {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @Test
    public void orderPurchaseFlowInterfaceSyncTest() {
        Result<String> result = handler();
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    public Result<String> handler() {
        Long suId = 73L;
        String body = "[{\"PO_NO\":\"23762323\",\"PO_TIME\":\"2022-04-20 10:00:36\",\"ENTERPRISE_INNER_CODE\":\"16888\",\"ENTERPRISE_NAME\":\"北京市通州区永顺镇北马庄社区卫生服务站通州区\",\"PO_BATCH_NO\":\"2109012\",\"PO_QUANTITY\":-21.0,\"PO_PRODUCT_TIME\":\"2021-09-05 00:00:00\",\"PO_EFFECTIVE_TIME\":\"2024-02-29 00:00:00\",\"PO_PRICE\":23.57,\"GOODS_IN_SN\":\"70036\",\"GOODS_NAME\":\"连花清瘟颗粒\",\"PO_LICENSE\":\"国药准字Z20100040\",\"PO_SPECIFICATIONS\":\"6g*10袋\",\"PO_UNIT\":\"盒\",\"PO_MANUFACTURER\":\"北京以岭药业有限公司\",\"PO_SOURCE\":\"非大运河采购\",\"OPER_TYPE\":1.0},{\"PO_NO\":\"23762218\",\"PO_TIME\":\"2022-04-20T11:44:38\",\"ENTERPRISE_INNER_CODE\":\"15023\",\"ENTERPRISE_NAME\":\"北京市门头沟区妇幼保健院.\",\"PO_BATCH_NO\":\"2009029\",\"PO_QUANTITY\":20.0,\"PO_PRODUCT_TIME\":\"2020-09-29 00:00:00\",\"PO_EFFECTIVE_TIME\":\"2023-02-28 00:00:00\",\"PO_PRICE\":11.09,\"GOODS_IN_SN\":\"12914\",\"GOODS_NAME\":\"连花清瘟胶囊\",\"PO_LICENSE\":\"国药准字Z20040063\",\"PO_SPECIFICATIONS\":\"0.35gX24s\",\"PO_UNIT\":\"盒\",\"PO_MANUFACTURER\":\"石家庄以岭药业股份有限公司\",\"PO_SOURCE\":\"非大运河采购\",\"OPER_TYPE\":1.0}]";

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
                ErpPurchaseFlowStringDTO erpPurchaseFlowStringDTO = JSON.parseObject(json, ErpPurchaseFlowStringDTO.class);
                ErpPurchaseFlowDTO erpPurchaseFlowDTO = buildErpPurchaseFlowDTO(erpPurchaseFlowStringDTO);
                // 数据校验
                ValidationResult validationResult = ValidationUtils.validate(erpPurchaseFlowDTO);
                if (validationResult.hasError()) {
                    errorList.add(new ErpErrorEntity(erpPurchaseFlowDTO.getErpPrimaryKey(), validationResult.getErrorString()));
                    continue;
                }
                erpPurchaseFlowDTO.setSuId(suId);
                dataMap.put(erpPurchaseFlowDTO.getErpPrimaryKey(), erpPurchaseFlowDTO);
            } catch (Exception e) {
                JSONObject obj = JSON.parseObject(json);
                if (StrUtil.isEmpty(obj.getString("fileKey"))) {
                    errorList.add(new ErpErrorEntity(obj.getString(""), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(
                            obj.getString("" + OpenConstants.SPLITE_SYMBOL + obj.getString("fileKey")), e.getMessage()));
                }
                continue;
            }
        }
        //判断topic是否存在
        if (!CollectionUtils.isEmpty(dataMap.values())) {
            SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpPurchaseFlow.getTopicName(), suId + "",
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

    private ErpPurchaseFlowDTO buildErpPurchaseFlowDTO(ErpPurchaseFlowStringDTO erpPurchaseFlowStringDTO) {
        ErpPurchaseFlowDTO erpPurchaseFlowDTO = new ErpPurchaseFlowDTO();
        erpPurchaseFlowDTO.setPoId(erpPurchaseFlowStringDTO.getPoId());
        erpPurchaseFlowDTO.setPoNo(erpPurchaseFlowStringDTO.getPoNo());
        erpPurchaseFlowDTO.setPoTime(DateUtil.parse(erpPurchaseFlowStringDTO.getPoTime()));
        erpPurchaseFlowDTO.setEnterpriseInnerCode(erpPurchaseFlowStringDTO.getEnterpriseInnerCode());
        erpPurchaseFlowDTO.setEnterpriseName(erpPurchaseFlowStringDTO.getEnterpriseName());
        erpPurchaseFlowDTO.setPoBatchNo(erpPurchaseFlowStringDTO.getPoBatchNo());
        erpPurchaseFlowDTO.setPoQuantity(erpPurchaseFlowStringDTO.getPoQuantity());
        erpPurchaseFlowDTO.setPoProductTime(DateUtil.parse(erpPurchaseFlowStringDTO.getPoProductTime()));
        erpPurchaseFlowDTO.setPoEffectiveTime(DateUtil.parse(erpPurchaseFlowStringDTO.getPoEffectiveTime()));
        erpPurchaseFlowDTO.setPoPrice(erpPurchaseFlowStringDTO.getPoPrice());
        erpPurchaseFlowDTO.setGoodsInSn(erpPurchaseFlowStringDTO.getGoodsInSn());
        erpPurchaseFlowDTO.setGoodsName(erpPurchaseFlowStringDTO.getGoodsName());
        erpPurchaseFlowDTO.setPoLicense(erpPurchaseFlowStringDTO.getPoLicense());
        erpPurchaseFlowDTO.setPoSpecifications(erpPurchaseFlowStringDTO.getPoSpecifications());
        erpPurchaseFlowDTO.setPoUnit(erpPurchaseFlowStringDTO.getPoUnit());
        erpPurchaseFlowDTO.setPoManufacturer(erpPurchaseFlowStringDTO.getPoManufacturer());
        erpPurchaseFlowDTO.setPoSource(erpPurchaseFlowStringDTO.getPoSource());
        erpPurchaseFlowDTO.setSuDeptNo(erpPurchaseFlowStringDTO.getSuDeptNo());
        erpPurchaseFlowDTO.setOperType(erpPurchaseFlowStringDTO.getOperType());
        return erpPurchaseFlowDTO;
    }

}
