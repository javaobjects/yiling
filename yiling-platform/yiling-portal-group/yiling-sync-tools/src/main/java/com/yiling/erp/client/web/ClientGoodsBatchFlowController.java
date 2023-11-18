package com.yiling.erp.client.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.util.CacheTaskUtil;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 库存流向
 *
 * @author: houjie.sun
 * @date: 2022/2/11
 */
@Slf4j
@Controller
@RequestMapping({ "/flow" })
public class ClientGoodsBatchFlowController {

    private static final String GB_ID_NO     = "gb_id_no";
    private static final String IN_SN = "in_sn";
    private static final String GB_NUMBER = "gb_number";
    private static final String GB_BATCH_NO = "gb_batch_no";

    @Autowired
    private TaskConfigDao       taskConfigDao;

    @Autowired
    private JobTaskService      jobTaskService;

    @Autowired
    public InitErpConfig        erpCommon;

    @Autowired
    public InitErpSysData       initErpSysData;

    @ResponseBody
    @RequestMapping(value = { "/saveGoodsBatchFlow.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject saveOrder(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        TaskConfig taskConfig = new TaskConfig();
        String openDock = request.getParameter("openDock");
        String frequencyValue = request.getParameter("frequency");
        String sqlContext = request.getParameter("sqlContext");
        if(Integer.parseInt(frequencyValue) > 60*24){
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP库存流向] 同步频率不能大于1440分钟(24小时)");
            return jSONObject;
        }
        if(1== CacheTaskUtil.getInstance().getCacheData(ErpTopicName.ErpGoodsBatchFlow.getTopicName())){
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP库存流向] 请先关闭同步任务");
            return jSONObject;
        }
        // 保存信息到本地信息
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskInterval(frequencyValue);
        taskConfig.setTaskKey(ErpTopicName.ErpGoodsBatchFlow.getErpKey());
        taskConfig.setTaskName("ERP库存流向信息");
        taskConfig.setTaskNo(ErpTopicName.ErpGoodsBatchFlow.getMethod());
        taskConfig.setTaskSQL(sqlContext);
        taskConfig.setTaskStatus(StringUtil.isEmpty(openDock) ? "0" : "1");
        taskConfig.setSpringId("clientGoodsBatchFlowService");
        taskConfig.setMethodName("syncData");

        try {
            //校验sql
            List<BaseErpEntity> dataList=initErpSysData.findDataByMethodNoAndSql(ErpTopicName.ErpGoodsBatchFlow.getMethod(),sqlContext, 0);
            Map<String, BaseErpEntity> map = initErpSysData.isRepeat(dataList);
            if (map == null) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 主键有重复数据,请检查"+ErpTopicName.ErpGoodsBatchFlow.getErpKey()+"字段");
                return jSONObject;
            }

            // sql是否包含字段 in_sn
            if (!sqlContext.contains(IN_SN)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 保存本地失败，sql中缺少字段，药品内码："+ IN_SN);
                return jSONObject;
            }
            // 数量 gb_number
            if (!sqlContext.contains(GB_NUMBER)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 保存本地失败，sql中缺少字段，库存数量："+ GB_NUMBER);
                return jSONObject;
            }
            // 批号 gb_batch_no
            if (!sqlContext.contains(GB_BATCH_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 保存本地失败，sql中缺少字段，批次号："+ GB_BATCH_NO);
                return jSONObject;
            }
            // sql是否包含字段 gb_id_no
            if (!sqlContext.contains(GB_ID_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 保存本地失败，sql中缺少字段，Erp库存流水ID主键："+ GB_ID_NO);
                return jSONObject;
            }

            List<ErpGoodsBatchFlowDTO> erpGoodsBatchFlowList = PojoUtils.map(dataList, ErpGoodsBatchFlowDTO.class);
            // in_sn 字段校验
            List<ErpGoodsBatchFlowDTO> goodsInnerSnNullList = erpGoodsBatchFlowList.stream().filter(d -> StrUtil.isBlank(d.getInSn())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsInnerSnNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 字段 in_sn 有为空的,请检查 in_sn 字段");
                return jSONObject;
            }
            // gb_number 字段校验
            List<ErpGoodsBatchFlowDTO> goodsNumberNullList = erpGoodsBatchFlowList.stream().filter(d -> ObjectUtil.isNull(d.getGbNumber())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsNumberNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 字段 gb_number 有为空的,请检查 gb_number 字段");
                return jSONObject;
            }
            // gb_batch_no 字段校验
            List<ErpGoodsBatchFlowDTO> goodsBatchNoNullList = erpGoodsBatchFlowList.stream().filter(d -> StrUtil.isBlank(d.getGbBatchNo())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsBatchNoNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 字段 gb_batch_no 有为空的,请检查 gb_batch_no 字段");
                return jSONObject;
            }
            // gb_id_no 字段校验
            List<ErpGoodsBatchFlowDTO> goodsIdNoNullList = erpGoodsBatchFlowList.stream().filter(d -> StrUtil.isBlank(d.getGbIdNo())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsIdNoNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP库存流向] 字段 gb_id_no 有为空的,请检查 gb_id_no 字段");
                return jSONObject;
            }

            if(!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)){
                jSONObject.put("code",100);
                jSONObject.put("message","[ERP库存流向] 配置信息保存本地失败");
                return jSONObject;
            }

            if(taskConfig.getTaskStatus().equals("1")) {
                String interval = taskConfig.getTaskInterval();
                taskConfig.setTaskInterval(interval);
                jobTaskService.addJob(taskConfig);
            }else{
                jobTaskService.deleteJob(taskConfig);
            }
            jSONObject.put("code", 200);
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            log.error("ERP库存流向任务信息保存失败，error -> {}", e);
            e.printStackTrace();
        }
        return jSONObject;
    }

    @ResponseBody
    @RequestMapping(value = { "/getGoodsBatchFlowInfo.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String getOrderInfo(HttpServletRequest request, HttpServletResponse response) {
        TaskConfig taskConfig = null;
        List<TaskConfig> lists = null;
        try {
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
                "select * from task_config where taskNo=" + ErpTopicName.ErpGoodsBatchFlow.getMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        taskConfig = lists.get(0);
        return JSONObject.toJSONString(taskConfig, new SerializerFeature[] { SerializerFeature.WriteNullStringAsEmpty });
    }

    /**
     * 清理全部缓存
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/deleteGoodsBatchFlowInfoCache.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject deleteCustomerCache(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            //先判断任务是否还在执行
            TaskConfig taskConfig = null;
            List<TaskConfig> lists = null;
            try {
                lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
                    "select * from task_config where taskNo=" + ErpTopicName.ErpGoodsBatchFlow.getMethod());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CollectionUtils.isEmpty(lists)) {
                return null;
            }
            taskConfig = lists.get(0);
            String taskStatus = taskConfig.getTaskStatus();
            if (taskStatus.equals("1")) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "请先关闭同步任务");
                return jSONObject;
            }

            //判断任务是否还在执行
            List<TaskConfig> taskConfigList = jobTaskService.getRunningJob();
            if (!CollectionUtils.isEmpty(taskConfigList)) {
                for (TaskConfig runTaskConfig : taskConfigList) {
                    if (runTaskConfig.getTaskNo().equals(ErpTopicName.ErpGoodsBatchFlow.getMethod())) {
                        jSONObject.put("code", 100);
                        jSONObject.put("message", "程序还在运行中，请稍后再试");
                        return jSONObject;
                    }
                }
            }
            initErpSysData.eleteCache(erpCommon.getSysConfig(), ErpTopicName.ErpGoodsBatchFlow);
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            e.printStackTrace();
        }
        jSONObject.put("code", 200);
        jSONObject.put("message", "删除缓存数据成功");
        return jSONObject;
    }

}
