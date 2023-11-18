package com.yiling.open.third.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.ftp.dto.LocalCompareDTO;
import com.yiling.open.ftp.service.FlowFtpClientService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/8
 */
@Slf4j
@Service
public abstract class FlowAbstractTemplate {

    @Autowired
    public RocketMqProducerService rocketMqProducerService;
    @Autowired
    public FlowFtpClientService flowFtpClientService;

    @DubboReference
    private ErpClientApi erpClientApi;
    @DubboReference
    private ErpHeartApi erpHeartApi;

    public static final String startTime = "start_time";
    public static final String endTime = "end_time";

    /**
     * 采购流向请求方法
     *
     * @return 返回json字符串
     */
    protected abstract String requestPurchaseTab(Map<String, String> param);

    /**
     * 销售流向请求方法
     *
     * @return 返回json字符串
     */
    protected abstract String requestSaleTab(Map<String, String> param);

    /**
     * 库存流向请求方法
     *
     * @return 返回json字符串
     */
    protected abstract String requestGoodsBatchTab(Map<String, String> param);


    private JSONObject changeJsonObj(JSONObject jsonObj, Map<String, String> keyMap,Map<String, String> keyMapMany) {
        JSONObject resJson = new JSONObject();
        Set<String> keySet = jsonObj.keySet();
        for (String key : keySet) {
            String resKey = keyMap.get(key) == null ? key : keyMap.get(key);
            try {
                JSONObject jsonobj1 = jsonObj.getJSONObject(key);
                resJson.put(resKey, changeJsonObj(jsonobj1, keyMap,keyMapMany));
            } catch (Exception e) {
                try {
                    JSONArray jsonArr = jsonObj.getJSONArray(key);
                    resJson.put(resKey, changeJsonArr(jsonArr, keyMap,keyMapMany));
                } catch (Exception x) {
                    resJson.put(resKey, jsonObj.get(key));
                }
            }
        }

        if (MapUtil.isNotEmpty(keyMapMany)) {
            for (Map.Entry<String, String> entry : keyMapMany.entrySet()) {
                StringBuffer sb = new StringBuffer();
                String[] keys = StrUtil.split(entry.getKey(),"|");
                for (int i = 0; i < keys.length; i++) {
                    sb.append(jsonObj.get(keys[i])).append("|");
                }
                resJson.put(entry.getValue(), StrUtil.sub(sb, 0, sb.length() - 1));
            }
        }
        return resJson;
    }

    private JSONArray changeJsonArr(JSONArray jsonArr, Map<String, String> keyMap,Map<String, String> keyMapMany) {
        JSONArray resJson = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            resJson.add(changeJsonObj(jsonObj, keyMap,keyMapMany));
        }
        return resJson;
    }

    public String getJSONArrayByKey(cn.hutool.json.JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }

        String jsonStr=JSONUtil.toJsonStr(jsonObj);
        if (StrUtil.contains(jsonStr,"[{")&&StrUtil.contains(jsonStr,"}]")) {
            cn.hutool.json.JSONArray jsonArray = jsonObj.getJSONArray(key);
            return JSONUtil.toJsonStr(jsonArray);
        } else {
            cn.hutool.json.JSONArray a = new cn.hutool.json.JSONArray();
            a.add(jsonObj.getJSONObject(key));
            return JSONUtil.toJsonStr(a);
        }
    }


    /**
     * 发送数据方法
     *
     * @return 如果有数据
     */
    public void process(Map<String, String> param, String purchaseMapping, String saleMapping, String goodsBatchMapping, Long suId,Integer flowDay) {
        String purchaseJson = requestPurchaseTab(param);

        //先把json转成map
        if (!JSONUtil.isJson(purchaseJson) || !JSONUtil.isJson(purchaseMapping)) {
            log.info("对接的数据不满足json格式Json:{},data:{},suId{}", purchaseJson, purchaseMapping, suId);
        } else {
            Map<String, String> keyPurchaseMap = new HashMap<>();
            Map<String, String> keyPurchaseManyMap = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(purchaseMapping);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if(entry.getKey().contains("|")) {
                    keyPurchaseManyMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }else {
                    keyPurchaseMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            JSONArray purchaseJsonObject = changeJsonArr(JSONArray.parseArray(purchaseJson), keyPurchaseMap,keyPurchaseManyMap);
            List<ErpPurchaseFlowDTO> erpPurchaseFlowDTOList = JSONArray.parseArray(purchaseJsonObject.toJSONString(), ErpPurchaseFlowDTO.class);
            if (CollUtil.isNotEmpty(erpPurchaseFlowDTOList)) {
                erpPurchaseFlowDTOList.stream().forEach(e->{
                    e.setId(null);
                    e.setSuId(suId);
                    e.setOperType(1);
                });
                // 记录心跳最后时间
                this.lastHeartBeatTime(suId, ErpTopicName.ErpPurchaseFlow.getMethod());
                flowFtpClientService.purchaseFlowCompare(suId,flowDay,erpPurchaseFlowDTOList);
            }
        }

        String saleJson = requestSaleTab(param);

        //先把json转成map
        if (!JSONUtil.isJson(saleJson) || !JSONUtil.isJson(saleMapping)) {
            log.info("对接的数据不满足json格式Json:{},data:{},suId{}", saleJson, saleMapping, suId);
        } else {
            Map<String, String> keySaleMap = new HashMap<>();
            Map<String, String> keySaleManyMap = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(saleMapping);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if(entry.getKey().contains("|")) {
                    keySaleManyMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }else {
                    keySaleMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            JSONArray saleJsonObject = changeJsonArr(JSONArray.parseArray(saleJson), keySaleMap,keySaleManyMap);
            List<ErpSaleFlowDTO> erpSaleFlowDTOList = JSONArray.parseArray(saleJsonObject.toJSONString(), ErpSaleFlowDTO.class);
            if (CollUtil.isNotEmpty(erpSaleFlowDTOList)) {
                erpSaleFlowDTOList.stream().forEach(e->{
                    e.setId(null);
                    e.setSuId(suId);
                    e.setOperType(1);
                });
                // 记录心跳最后时间
                this.lastHeartBeatTime(suId, ErpTopicName.ErpSaleFlow.getMethod());
                flowFtpClientService.saleFlowCompare(suId,flowDay,erpSaleFlowDTOList);
            }
        }

        String goodsBatchJson = requestGoodsBatchTab(param);

        //先把json转成map
        if (!JSONUtil.isJson(goodsBatchJson) || !JSONUtil.isJson(goodsBatchMapping)) {
            log.info("对接的数据不满足json格式Json:{},data:{},suId{}", goodsBatchJson, goodsBatchMapping, suId);
        } else {
            Map<String, String> keyGoodsBatchMap = new HashMap<>();
            Map<String, String> keyGoodsBatchManyMap = new HashMap<>();
            JSONObject jsonObject = JSONObject.parseObject(goodsBatchMapping);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if(entry.getKey().contains("|")){
                    keyGoodsBatchManyMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }else {
                    keyGoodsBatchMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            JSONArray goodsBatchJsonObject = changeJsonArr(JSONArray.parseArray(goodsBatchJson), keyGoodsBatchMap,keyGoodsBatchManyMap);
            List<ErpGoodsBatchFlowDTO> erpGoodsBatchFlowDTOList = JSONArray.parseArray(goodsBatchJsonObject.toJSONString(), ErpGoodsBatchFlowDTO.class);
            List<BaseErpEntity> baseErpEntityList = new ArrayList<>();
            if (CollUtil.isNotEmpty(erpGoodsBatchFlowDTOList)) {
                for (ErpGoodsBatchFlowDTO erpGoodsBatchFlowDTO : erpGoodsBatchFlowDTOList) {
                    erpGoodsBatchFlowDTO.setId(null);
                    erpGoodsBatchFlowDTO.setGbIdNo(IdUtil.fastSimpleUUID());
                    erpGoodsBatchFlowDTO.setSuId(suId);
                    erpGoodsBatchFlowDTO.setOperType(1);
                    baseErpEntityList.add(erpGoodsBatchFlowDTO);
                }
                LocalCompareDTO localCompareDTO=new LocalCompareDTO();
                localCompareDTO.setTime(null);
                localCompareDTO.setDataList(baseErpEntityList);
                localCompareDTO.setSuId(suId);
                // 记录心跳最后时间
                this.lastHeartBeatTime(suId, ErpTopicName.ErpGoodsBatchFlow.getMethod());
                flowFtpClientService.localCompare(localCompareDTO);
            }
        }
    }

    protected String dateTimeFormatCovertToDate(String timeStr) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(timeStr);
            time = sdf2.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return time;
    }

    /**
     * 记录心跳最后时间
     *
     * @param suId
     */
    private void lastHeartBeatTime(Long suId, String methodName){
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
        sysHeartBeat.setRuntaskIds(methodName);
        sysHeartBeat.setVersions("");
        sysHeartBeat.setMac("");
        sysHeartBeat.setIp("");
        sysHeartBeat.setSuId(suId);
        sysHeartBeat.setSuName("");
        erpHeartApi.insertErpHeart(sysHeartBeat);
    }
}
