package com.yiling.admin.erp.enterprise.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.admin.erp.enterprise.form.ClientTaskConfigForm;
import com.yiling.admin.erp.enterprise.form.DeleteCacheClientTaskConfigForm;
import com.yiling.admin.erp.enterprise.form.QueryClientTaskConfigForm;
import com.yiling.admin.erp.enterprise.form.QuerySqlForm;
import com.yiling.admin.erp.enterprise.form.SqlExecuteClientTaskConfigForm;
import com.yiling.admin.erp.enterprise.form.UpdateClientLogForm;
import com.yiling.admin.erp.enterprise.form.UpdateClientToolForm;
import com.yiling.admin.erp.enterprise.vo.ClientTaskConfigVO;
import com.yiling.admin.erp.enterprise.vo.QueryJsonVO;
import com.yiling.admin.erp.enterprise.vo.SelectTaskConfigVO;
import com.yiling.admin.erp.redis.factory.RedisServiceFactory;
import com.yiling.admin.erp.redis.service.RedisService;
import com.yiling.admin.erp.redis.util.RedisKey;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.open.erp.api.ErpConfigApi;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.api.ErpGoodsApi;
import com.yiling.open.erp.api.ErpGoodsBatchApi;
import com.yiling.open.erp.api.ErpGoodsBatchFlowApi;
import com.yiling.open.erp.api.ErpGoodsCustomerPriceApi;
import com.yiling.open.erp.api.ErpGoodsGroupPriceApi;
import com.yiling.open.erp.api.ErpTaskInterfaceApi;
import com.yiling.open.erp.dto.ClientRequestDTO;
import com.yiling.open.erp.dto.ClientTaskConfigDTO;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.CommandStatusEnum;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/5/24
 */
@Api(tags = "ERP工具远程执行命令")
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/command")
public class CommandController {

    private static String guardUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/erp/erp-guard-tools/erp-guard-tools.jar";

    @Value("${open.war.version:''}")
    private String version;
    @Value("${open.war.name:''}")
    private String name;
    @Value("${open.war.description:''}")
    private String description;

    @DubboReference
    private ErpConfigApi         erpConfigApi;
    @DubboReference
    private ErpTaskInterfaceApi  erpTaskInterfaceApi;
    @DubboReference(async = true)
    private ErpGoodsBatchFlowApi erpGoodsBatchFlowApi;
    @DubboReference(async = true)
    private ErpGoodsBatchApi     erpGoodsBatchApi;
    @DubboReference(async = true)
    private ErpGoodsCustomerPriceApi erpGoodsCustomerPriceApi;
    @DubboReference(async = true)
    private ErpGoodsGroupPriceApi erpGoodsGroupPriceApi;
    @DubboReference(async = true)
    private ErpGoodsApi erpGoodsApi;
    @DubboReference(async = true)
    private ErpCustomerApi erpCustomerApi;

    private static final RedisService redisService = RedisServiceFactory.getRedisService();

    @ApiOperation(value = "获取本地任务", httpMethod = "GET")
    @GetMapping("/task/list")
    public Result<List<SelectTaskConfigVO>> taskList(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("suId") Long suId) {
        List<ClientTaskConfigDTO> clientTaskConfigDTOList = erpConfigApi.getTaskConfigListBySuid(suId);
        List<SelectTaskConfigVO> taskList = PojoUtils.map(clientTaskConfigDTOList, SelectTaskConfigVO.class);
        return Result.success(taskList);
    }

    @ApiOperation(value = "获取任务详情", httpMethod = "POST")
    @PostMapping("/task/get")
    public Result<ClientTaskConfigVO> getTask(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryClientTaskConfigForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.SqlSelectTask.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());

        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject == null) {
            return Result.failed("任务执行失败");
        }
        String code = jsonObject.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            String date = jsonObject.getString("message");
            if (StrUtil.isEmpty(date)) {
                return Result.failed("没有获取到对应任务信息");
            }
            ClientTaskConfigVO clientTaskConfigVO = JSONObject.parseObject(date, ClientTaskConfigVO.class);
            clientTaskConfigVO.setSuId(form.getSuId());
            ErpTaskInterfaceDTO erpTaskInterfaceDTO = erpTaskInterfaceApi.findErpTaskInterfaceByTaskNo(clientTaskConfigVO.getTaskNo());
            clientTaskConfigVO.setDescribe(erpTaskInterfaceDTO.getJsonMapping());
            return Result.success(clientTaskConfigVO);
        }
        return Result.failed(jsonObject.getString("message"));
    }

    @ApiOperation(value = "保存本地任务", httpMethod = "POST")
    @PostMapping("/task/save")
    public Result<BoolObject> saveTask(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid ClientTaskConfigForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.SqlSaveTask.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());

        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject == null) {
            return Result.failed("任务执行失败");
        }
        String code = jsonObject.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            return Result.success(new BoolObject(true));
        }
        return Result.failed(jsonObject.getString("message"));
    }

    @ApiOperation(value = "执行本地任务", httpMethod = "POST")
    @PostMapping("/task/sqlExecute")
    public Result<String> sqlExecute(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SqlExecuteClientTaskConfigForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.SqlExecute.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());

        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject json = JSON.parseObject(message);
        String code = json.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            JSONArray jsonArray = json.getJSONArray("rows");
            if(null == jsonArray || 0 == jsonArray.size()){
                return Result.failed("查询数据为空");
            }
            ErpTaskInterfaceDTO erpTaskInterfaceDTO = erpTaskInterfaceApi.findErpTaskInterfaceByTaskNo(form.getTaskNo());
            if (StrUtil.isEmpty(erpTaskInterfaceDTO.getJsonMapping())) {
                return Result.success(message);
            }
            JSONObject jsonObject = JSONObject.parseObject(erpTaskInterfaceDTO.getJsonMapping());
            Map<String, String> keyPurchaseMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                keyPurchaseMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            JSONArray purchaseJsonObject = changeJsonArr(jsonArray, keyPurchaseMap);
            json.remove("rows");
            json.put("rows", purchaseJsonObject);
            return Result.success(json.toJSONString());
        } else {
            return Result.failed(json.getString("rows"));
        }
    }

    @ApiOperation(value = "删除任务缓存", httpMethod = "POST")
    @PostMapping("/task/deleteCache")
    public Result<BoolObject> deleteCache(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCacheClientTaskConfigForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.deleteCache.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());

        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject == null) {
            return Result.failed("任务执行失败");
        }
        String code = jsonObject.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            //软删除op库所有库存信息
            if (form.getTaskNo().equals(ErpTopicName.ErpCustomer.getMethod())) {
                erpCustomerApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpGoodsBatch.getMethod())) {
                erpGoodsBatchApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpGoodsBatchFlow.getMethod())) {
                erpGoodsBatchFlowApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpGoodsPrice.getMethod())) {
                erpGoodsCustomerPriceApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpGroupPrice.getMethod())) {
                erpGoodsGroupPriceApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpGoods.getMethod())) {
                erpGoodsApi.updateOperTypeGoodsBatchFlowBySuId(form.getSuId());
            } else if (form.getTaskNo().equals(ErpTopicName.ErpOrderSend.getMethod())) {
            }
            return Result.success(new BoolObject(true));
        }
        return Result.failed(jsonObject.getString("message"));
    }

    @ApiOperation(value = "版本升级", httpMethod = "POST")
    @PostMapping("/client/update")
    public Result<BoolObject> clientUpdate(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateClientToolForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.upgradeCommand.getMethod());

        redisService.remove(RedisKey.generate("update_log", clientRequestDTO.getSuId() + ""));

        String packageUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/erp/war/".concat(version).concat("/yiling-client-web.war");
        ClientToolVersionDTO clientToolVersion = new ClientToolVersionDTO();
        clientToolVersion.setPackageUrl(packageUrl);
        clientToolVersion.setVersion(version);
        clientToolVersion.setName(name);
        clientToolVersion.setDescription(description);
        clientToolVersion.setGuardUrl(guardUrl);

        clientRequestDTO.setMessage(JSON.toJSONString(clientToolVersion));
        clientRequestDTO.setSuId(form.getSuId());

        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject == null) {
            return Result.failed("任务执行失败");
        }
        String code = jsonObject.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            return Result.success(new BoolObject(true));
        }
        return Result.failed(jsonObject.getString("message"));
    }

    @ApiOperation(value = "版本升级日志输出", httpMethod = "POST")
    @PostMapping("/client/updateLog")
    public Result<Map<String, Object>> updateLog(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateClientLogForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.upgradeLog.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());
        Map<String, Object> hashMap = new HashMap<>();
        try {
            String status = "0";
            List<String> listMessage = new ArrayList<>();
            do {
                String message = redisService.rpop(RedisKey.generate("update_log", clientRequestDTO.getSuId() + ""));
                if (StrUtil.isEmpty(message)) {
                    break;
                }
                listMessage.add(message);
                if (message.equals("守护进程退出-任务执行完成")) {
                    status = "1";
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(100);
            } while (true);
            hashMap.put("message", listMessage);
            hashMap.put("status", status);
        } catch (Exception e) {
            log.error("抽取工远程执行失败", e);
        }
        return Result.success(hashMap);
    }

    public static void main(String[] args) {
        //        redisService.lpush("aaaa","zhangshuang1");
        //        redisService.lpush("aaaa","zhangshuang2");
        //        redisService.lpush("aaaa","zhangshuang3");
        //        redisService.lpush("aaaa","zhangshuang4");
        String list = redisService.rpop("aaaa");
        System.out.println(list);
    }

    @ApiOperation(value = "查询sql", httpMethod = "POST")
    @PostMapping("/sql/query")
    public Result<QueryJsonVO> query(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QuerySqlForm form) {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setTrace(IdUtil.simpleUUID());
        clientRequestDTO.setTaskNo(ErpTopicName.SqlSelect.getMethod());
        clientRequestDTO.setMessage(JSON.toJSONString(form));
        clientRequestDTO.setSuId(form.getSuId());
        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
        if (MapUtil.isNotEmpty(map)) {
            if (map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
                return Result.failed("任务还在执行中");
            }
        }
        map = new HashMap<>();
        map.put(OpenConstants.status_flag, CommandStatusEnum.ser_send.getCode());
        map.put(OpenConstants.task_flag, JSON.toJSONString(clientRequestDTO));
        redisService.hmset(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""), 30);
        String message = getResult(clientRequestDTO);
        if (StrUtil.isEmpty(message)) {
            return Result.failed("任务信息返回空值");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject == null) {
            return Result.failed("任务执行失败");
        }
        String code = jsonObject.getString("code");
        if (StrUtil.isNotEmpty(code) && code.equals("200")) {
            QueryJsonVO queryJsonVO=new QueryJsonVO();
            queryJsonVO.setTotal(jsonObject.getInteger("total"));
            queryJsonVO.setJsonData(jsonObject.getString("rows"));
            return Result.success(queryJsonVO);
        }
        return Result.failed(jsonObject.getString("rows"));
    }

    private String getResult(ClientRequestDTO clientRequestDTO) {
        String message = "";
        try {
            long end = System.currentTimeMillis() + 1000 * 30;
            while (System.currentTimeMillis() < end) {
                Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", clientRequestDTO.getSuId() + ""));
                if (MapUtil.isNotEmpty(map)) {
                    String status = map.get(OpenConstants.status_flag);
                    if (status.equals(CommandStatusEnum.client_complete.getCode())) {
                        message = map.get(OpenConstants.task_flag);
                        break;
                    }
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (Exception e) {
            log.error("抽取工远程执行失败", e);
        }
        return message;
    }


    private JSONObject changeJsonObj(JSONObject jsonObj, Map<String, String> keyMap) {
        JSONObject resJson = new JSONObject();
        Set<String> keySet = jsonObj.keySet();
        for (String key : keySet) {
            String resKey = keyMap.get(key) == null ? key : keyMap.get(key);
            try {
                JSONObject jsonobj1 = jsonObj.getJSONObject(key);
                resJson.put(resKey, changeJsonObj(jsonobj1, keyMap));
            } catch (Exception e) {
                try {
                    JSONArray jsonArr = jsonObj.getJSONArray(key);
                    resJson.put(resKey, changeJsonArr(jsonArr, keyMap));
                } catch (Exception x) {
                    resJson.put(resKey, jsonObj.get(key));
                }
            }
        }
        return resJson;
    }

    private JSONArray changeJsonArr(JSONArray jsonArr, Map<String, String> keyMap) {
        JSONArray resJson = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            resJson.add(changeJsonObj(jsonObj, keyMap));
        }
        return resJson;
    }
}
