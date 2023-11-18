package com.yiling.erp.client.web;


import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

@Controller
@RequestMapping({"/order"})
public class ClientOrderPurchaseDeliveryController {


    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    public InitErpSysData initErpSysData;

    private static final String DELIVERY_NO = "delivery_no";
    private static final String DELIVERY_QUANTITY = "delivery_quantity";
    private static final String ORDER_DELIVERY_ERP_ID = "order_delivery_erp_id";

    @ResponseBody
    @RequestMapping(value = {"/getOrderPurchaseDelivery.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getOrderPurchaseDelivery(HttpServletRequest request, HttpServletResponse response) {
        TaskConfig taskConfig = null;
        List<TaskConfig> lists = null;
        try {
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + ErpTopicName.ErpOrderPurchaseDelivery.getMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        taskConfig = lists.get(0);
        return JSONObject.toJSONString(taskConfig, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty});
    }


    @ResponseBody
    @RequestMapping(value = {"/saveOrderPurchaseDelivery.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public JSONObject saveOrderPurchaseDelivery(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        TaskConfig taskConfig = new TaskConfig();
        String openDock = request.getParameter("openDock");
        String frequencyValue = request.getParameter("frequency");
        String sqlContext = request.getParameter("sqlContext");
        if(Integer.parseInt(frequencyValue) > 60*24){
            jSONObject.put("code", 100);
            jSONObject.put("message", "同步频率不能大于1440分钟(24小时)");
            return jSONObject;
        }
        if(1== CacheTaskUtil.getInstance().getCacheData(ErpTopicName.ErpOrderPurchaseDelivery.getTopicName())){
            jSONObject.put("code", 100);
            jSONObject.put("message", "请先关闭同步任务");
            return jSONObject;
        }
        //保存信息到本地信息
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskInterval(frequencyValue);
        taskConfig.setTaskKey(ErpTopicName.ErpOrderPurchaseDelivery.getErpKey());
        taskConfig.setTaskName("发货单同步");
        taskConfig.setTaskNo(ErpTopicName.ErpOrderPurchaseDelivery.getMethod());
        taskConfig.setTaskSQL(sqlContext);
        taskConfig.setTaskStatus(StringUtil.isEmpty(openDock) ? "0" : "1");
        taskConfig.setSpringId("clientOrderPurchaseDeliveryService");
        taskConfig.setMethodName("syncData");
        try {
            // sql是否包含字段 delivery_no
            if (!sqlContext.contains(DELIVERY_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "配置信息保存本地失败，sql中缺少字段：delivery_no");
                return jSONObject;
            }
            // sql是否包含字段 delivery_quantity
            if (!sqlContext.contains(DELIVERY_QUANTITY)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "配置信息保存本地失败，sql中缺少字段：delivery_quantity");
                return jSONObject;
            }
            // sql是否包含字段 order_delivery_erp_id
            if (!sqlContext.contains(ORDER_DELIVERY_ERP_ID)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "配置信息保存本地失败，sql中缺少字段：order_delivery_erp_id");
                return jSONObject;
            }
            //校验sql
            List<BaseErpEntity> dataList=initErpSysData.findDataByMethodNoAndSql(ErpTopicName.ErpOrderPurchaseDelivery.getMethod(),sqlContext, 0);
            Map<String, BaseErpEntity> map = initErpSysData.isRepeat(dataList);
            if (map == null) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "ERP系统发货单信息主键有重复数据,请检查"+ErpTopicName.ErpOrderPurchaseDelivery.getErpKey()+"字段");
                return jSONObject;
            }
            //保存本地
            if (!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "配置信息保存本地失败");
                return jSONObject;
            }

            if (taskConfig.getTaskStatus().equals("1")) {
                String interval = taskConfig.getTaskInterval();
                taskConfig.setTaskInterval(interval);
                jobTaskService.addJob(taskConfig);
            } else {
                jobTaskService.deleteJob(taskConfig);
            }
            jSONObject.put("code", 200);
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            e.printStackTrace();
        }
        return jSONObject;
    }

    /**
     * 清理全部缓存
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/deleteOrderPurchaseDelivery.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public JSONObject deleteOrderPurchaseDelivery(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            //先判断任务是否还在执行
            TaskConfig taskConfig = null;
            List<TaskConfig> lists = null;
            try {
                lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + ErpTopicName.ErpOrderPurchaseDelivery.getMethod());
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
            if(!CollectionUtils.isEmpty(taskConfigList)){
                for(TaskConfig runTaskConfig:taskConfigList){
                    if(runTaskConfig.getTaskNo().equals(ErpTopicName.ErpOrderPurchaseDelivery.getMethod())){
                        jSONObject.put("code", 100);
                        jSONObject.put("message", "程序还在运行中，请稍后再试");
                        return jSONObject;
                    }
                }
            }
//            log.info("[发货单同步] 清除客户端缓存数据");
            taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.ErpOrderPurchaseDelivery.getTopicName());
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            e.printStackTrace();
        }
        jSONObject.put("code", 200);
        jSONObject.put("message","删除缓存数据成功");
        return jSONObject;
    }
}
