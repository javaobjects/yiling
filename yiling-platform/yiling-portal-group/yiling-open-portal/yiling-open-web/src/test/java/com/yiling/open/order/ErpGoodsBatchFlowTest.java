package com.yiling.open.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpErrorEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowStringDTO;
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
 * @author: houjie.sun
 * @date: 2022/4/21
 */
@Slf4j
public class ErpGoodsBatchFlowTest extends BaseTest {

    @DubboReference
    private FlowFtpApi flowFtpApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private String key = "erp-goods-batch-flow-local-compare";
    private String keyTime = "erp-goods-batch-flow-local-compare-time";

    @Test
    public void goodsBatchFlowSyncTest() {
        Result<String> result = handler();
        System.out.println(">>>>> result: " + JSON.toJSONString(result));
    }

    public Result<String> handler() {
        Long suId = 4333L;
        // "gb_time":"2022-05-05 17:18:30",
        String body = "[{\"gb_batch_no\":\"B2007002\",\"gb_end_time\":\"2023-06-30 00:00:00\",\"gb_license\":\"国药准字Z20050845\",\"gb_manufacturer\":\"石家庄以岭药业股份有限公司\",\"gb_name\":\"津力达颗粒\",\"gb_number\":6,\"gb_produce_address\":\"\",\"gb_produce_time\":\"2020-07-03 00:00:00\",\"gb_specifications\":\"9gx9袋\",\"gb_time\":\"2022-06-10 09:50:09\",\"gb_unit\":\"盒\",\"in_sn\":90388,\"su_dept_no\":\"\",\"oper_type\":1}]";

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
                    errorList.add(new ErpErrorEntity(obj.getString(""), e.getMessage()));
                } else {
                    errorList.add(new ErpErrorEntity(obj.getString("" + OpenConstants.SPLITE_SYMBOL + obj.getString("su_dept_no")), e.getMessage()));
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
        }

        if (CollectionUtils.isEmpty(errorList)) {
            return Result.success();
        } else {
            return Result.failed(JSON.toJSONString(errorList));
        }
    }

    private ErpGoodsBatchFlowDTO buildErpGoodsBatchFlowDTO(ErpGoodsBatchFlowStringDTO erpGoodsBatchFlowString) {
        ErpGoodsBatchFlowDTO erpGoodsBatchFlow = new ErpGoodsBatchFlowDTO();
        erpGoodsBatchFlow.setGbIdNo(erpGoodsBatchFlowString.getGbIdNo());
        erpGoodsBatchFlow.setInSn(erpGoodsBatchFlowString.getInSn());
        erpGoodsBatchFlow.setGbBatchNo(erpGoodsBatchFlowString.getGbBatchNo());
        erpGoodsBatchFlow.setGbTime(DateUtil.parse(erpGoodsBatchFlowString.getGbTime()));
        erpGoodsBatchFlow.setGbNumber(erpGoodsBatchFlowString.getGbNumber());
        erpGoodsBatchFlow.setGbEndTime(erpGoodsBatchFlowString.getGbEndTime());
        erpGoodsBatchFlow.setGbProduceTime(erpGoodsBatchFlowString.getGbProduceTime());
        erpGoodsBatchFlow.setGbProduceAddress(erpGoodsBatchFlowString.getGbProduceAddress());
        erpGoodsBatchFlow.setGbName(erpGoodsBatchFlowString.getGbName());
        erpGoodsBatchFlow.setGbLicense(erpGoodsBatchFlowString.getGbLicense());
        erpGoodsBatchFlow.setGbSpecifications(erpGoodsBatchFlowString.getGbSpecifications());
        erpGoodsBatchFlow.setGbUnit(erpGoodsBatchFlowString.getGbUnit());
        erpGoodsBatchFlow.setGbManufacturer(erpGoodsBatchFlowString.getGbManufacturer());
        erpGoodsBatchFlow.setSuDeptNo(erpGoodsBatchFlowString.getSuDeptNo());
        erpGoodsBatchFlow.setOperType(ObjectUtil.isNull(erpGoodsBatchFlowString.getOperType()) ? 1 : erpGoodsBatchFlowString.getOperType());
        return erpGoodsBatchFlow;
    }

}
