package com.yiling.erp.client.web;

import java.util.Date;
import java.util.List;

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
import com.yiling.erp.client.util.Utils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpShopSaleFlowDTO;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 连锁门店销售订单流向
 * @author: houjie.sun
 * @date: 2023/3/20
 */
@Controller
@RequestMapping({ "/flow" })
public class ClientShopOrderSaleFlowController {

    private static final String SHOP_NO = "shop_no";
    private static final String SHOP_NAME = "shop_name";
    private static final String SO_TIME = "so_time";
    private static final String GOODS_IN_SN = "goods_in_sn";
    private static final String SO_BATCH_NO = "so_batch_no";
    private static final String SO_QUANTITY = "so_quantity";
    private static final String SO_PRICE = "so_price";

    @Autowired
    private TaskConfigDao       taskConfigDao;

    @Autowired
    private JobTaskService      jobTaskService;

    @Autowired
    public InitErpConfig        erpCommon;

    @Autowired
    public InitErpSysData       initErpSysData;

    @ResponseBody
    @RequestMapping(value = { "/saveShopSaleFlow.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject saveOrder(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        TaskConfig taskConfig = new TaskConfig();
        String openDock = request.getParameter("openDock");
        String flowDateCount = request.getParameter("flowDateCount");
        String frequency = request.getParameter("frequency");
        taskConfig.setTaskInterval(frequency);
        if(1== CacheTaskUtil.getInstance().getCacheData(ErpTopicName.ErpShopSaleFlow.getTopicName())){
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP连锁门店销售订单流向] 请先关闭同步任务");
            return jSONObject;
        }
        // 流向同步天数必须为正整数
        if(!Utils.checkFlowDateCount(flowDateCount)){
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，同步天数请输入正整数");
            return jSONObject;
        }
        String sqlContext = request.getParameter("sqlContext");

        //保存信息到本地信息
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskKey(ErpTopicName.ErpShopSaleFlow.getErpKey());
        taskConfig.setTaskName("ERP连锁门店销售订单流向信息");
        taskConfig.setTaskNo(ErpTopicName.ErpShopSaleFlow.getMethod());
        taskConfig.setTaskSQL(sqlContext);
        taskConfig.setTaskStatus(StringUtil.isEmpty(openDock) ? "0" : "1");
        taskConfig.setSpringId("clientShopOrderSaleFlowService");
        taskConfig.setMethodName("syncData");
        // 流向查询天数
        taskConfig.setFlowDateCount(flowDateCount);
//        // 流向任务调度时间
//        String jobTime = cn.hutool.core.date.DateUtil.format(new Date(), ErpConstants.FORMATE_DAY_TIME_JOB);
//        taskConfig.setTaskInterval(jobTime);

        try {
            // sql是否包含字段 shop_no
            if (!sqlContext.contains(SHOP_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段：shop_no");
                return jSONObject;
            }
            // sql是否包含字段 shop_name
            if (!sqlContext.contains(SHOP_NAME)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段：shop_name");
                return jSONObject;
            }
            // sql是否包含字段 po_time
            if (!sqlContext.contains(SO_TIME)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段：so_time");
                return jSONObject;
            }
            // sql是否包含字段 goods_in_sn
            if (!sqlContext.contains(GOODS_IN_SN)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段，商品内码：goods_in_sn");
                return jSONObject;
            }
            // sql是否包含字段 so_batch_no
            if (!sqlContext.contains(SO_BATCH_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段，批次号：so_batch_no");
                return jSONObject;
            }
            // sql是否包含字段 so_quantity
            if (!sqlContext.contains(SO_QUANTITY)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段，销售数量：so_quantity");
                return jSONObject;
            }
            // sql是否包含字段 so_price
            if (!sqlContext.contains(SO_PRICE)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败，sql中缺少字段，销售数量：so_price");
                return jSONObject;
            }

            // 校验sql
            List<BaseErpEntity> dataList = initErpSysData.findDataByMethodNoAndSql(ErpTopicName.ErpShopSaleFlow.getMethod(), sqlContext, Integer.parseInt(flowDateCount));
            List<ErpShopSaleFlowDTO> erpShopSaleFlowList = PojoUtils.map(dataList, ErpShopSaleFlowDTO.class);
            for (ErpShopSaleFlowDTO d : erpShopSaleFlowList) {
                // shop_no 字段校验
                if (StrUtil.isBlank(d.getShopNo())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] 字段 shop_no 有为空的,请检查 shop_no 字段");
                    return jSONObject;
                }
                // shop_name 字段校验
                if (StrUtil.isBlank(d.getShopName())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] 字段 shop_name 有为空的,请检查 shop_name 字段");
                    return jSONObject;
                }
                // so_time 字段校验
                if (ObjectUtil.isNull(d.getSoTime())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] 字段 so_time 有为空的,请检查 so_time 字段");
                    return jSONObject;
                }
                // goods_in_sn 字段校验
                if (StrUtil.isBlank(d.getGoodsInSn())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] goods_in_sn 有为空的,请检查 goods_in_sn 字段");
                    return jSONObject;
                }
                // so_batch_no 字段校验
                if (StrUtil.isBlank(d.getSoBatchNo())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] so_batch_no 有为空的,请检查 so_batch_no 字段");
                    return jSONObject;
                }
                // so_quantity 字段校验
                if (ObjectUtil.isNull(d.getSoQuantity())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] so_quantity 有为空的,请检查 so_quantity 字段");
                    return jSONObject;
                }
                // so_price 字段校验
                if (ObjectUtil.isNull(d.getSoPrice())) {
                    jSONObject.put("code", 100);
                    jSONObject.put("message", "[ERP连锁门店销售订单流向] so_price 有为空的,请检查 so_price 字段");
                    return jSONObject;
                }
            }

            // 保存本地
            if (!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP连锁门店销售订单流向] 配置信息保存本地失败");
                return jSONObject;
            }

            if (taskConfig.getTaskStatus().equals("1")) {
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

    @ResponseBody
    @RequestMapping(value = { "/getShopSaleFlowInfo.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String getOrderInfo(HttpServletRequest request, HttpServletResponse response) {
        TaskConfig taskConfig = null;
        List<TaskConfig> lists = null;
        try {
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
                "select * from task_config where taskNo=" + ErpTopicName.ErpShopSaleFlow.getMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        taskConfig = lists.get(0);
        return JSONObject.toJSONString(taskConfig, SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 清理全部缓存
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/deleteShopSaleFlowInfoCache.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject deleteCustomerCache(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            //先判断任务是否还在执行
            TaskConfig taskConfig = null;
            List<TaskConfig> lists = null;
            try {
                lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,
                    "select * from task_config where taskNo=" + ErpTopicName.ErpShopSaleFlow.getMethod());
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
                    if (runTaskConfig.getTaskNo().equals(ErpTopicName.ErpShopSaleFlow.getMethod())) {
                        jSONObject.put("code", 100);
                        jSONObject.put("message", "程序还在运行中，请稍后再试");
                        return jSONObject;
                    }
                }
            }
            //            log.info("[ERP连锁门店销售订单流向] 清除客户端缓存数据");
            taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.ErpShopSaleFlow.getTopicName());
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
