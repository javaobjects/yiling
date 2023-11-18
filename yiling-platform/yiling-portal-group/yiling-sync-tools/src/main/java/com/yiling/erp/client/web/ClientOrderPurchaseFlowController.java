package com.yiling.erp.client.web;

import java.util.Date;
import java.util.List;
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
import com.yiling.erp.client.util.Utils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购订单流向
 *
 * @author: houjie.sun
 * @date: 2021/9/22
 */
@Slf4j
@Controller
@RequestMapping({ "/flow" })
public class ClientOrderPurchaseFlowController {

    private static final String PO_TIME = "po_time";
    private static final String ENTERPRISE_INNER_CODE = "enterprise_inner_code";
    private static final String GOODS_IN_SN = "goods_in_sn";
    private static final String PO_BATCH_NO = "po_batch_no";
    private static final String PO_QUANTITY = "po_quantity";
    private static final String PO_PRICE = "po_price";

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    public InitErpSysData initErpSysData;

    @ResponseBody
    @RequestMapping(value = { "/saveOrderPurchaseFlow.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject saveOrder(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        TaskConfig taskConfig = new TaskConfig();
        String openDock = request.getParameter("openDock");
        String flowDateCount = request.getParameter("flowDateCount");
        String frequency = request.getParameter("frequency");
        taskConfig.setTaskInterval(frequency);
        if (1 == CacheTaskUtil.getInstance().getCacheData(ErpTopicName.ErpPurchaseFlow.getTopicName())) {
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP采购订单流向] 请先关闭同步任务");
            return jSONObject;
        }
        // 流向同步天数必须为正整数
        if (!Utils.checkFlowDateCount(flowDateCount)) {
            jSONObject.put("code", 100);
            jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，同步天数请输入正整数");
            return jSONObject;
        }
        String sqlContext = request.getParameter("sqlContext");

        //保存信息到本地信息
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskKey(ErpTopicName.ErpPurchaseFlow.getErpKey());
        taskConfig.setTaskName("ERP采购订单流向信息");
        taskConfig.setTaskNo(ErpTopicName.ErpPurchaseFlow.getMethod());
        taskConfig.setTaskSQL(sqlContext);
        taskConfig.setTaskStatus(StringUtil.isEmpty(openDock) ? "0" : "1");
        taskConfig.setSpringId("clientOrderPurchaseFlowService");
        taskConfig.setMethodName("syncData");
        // 流向查询天数
        taskConfig.setFlowDateCount(flowDateCount);
//        // 流向任务调度时间
//        String jobTime = cn.hutool.core.date.DateUtil.format(new Date(), ErpConstants.FORMATE_DAY_TIME_JOB);
//        taskConfig.setTaskInterval(jobTime);

        try {
            // sql是否包含字段 po_time
            if (!sqlContext.contains(PO_TIME)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段：po_time");
                return jSONObject;
            }
            // sql是否包含字段 enterprise_inner_code
            if (!sqlContext.contains(ENTERPRISE_INNER_CODE)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段，客户内码：enterprise_inner_code");
                return jSONObject;
            }
            // sql是否包含字段 goods_in_sn
            if (!sqlContext.contains(GOODS_IN_SN)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段，商品内码：goods_in_sn");
                return jSONObject;
            }
            // sql是否包含字段 po_batch_no
            if (!sqlContext.contains(PO_BATCH_NO)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段，批次号：po_batch_no");
                return jSONObject;
            }
            // sql是否包含字段 po_quantity
            if (!sqlContext.contains(PO_QUANTITY)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段，采购数量：po_quantity");
                return jSONObject;
            }
            // sql是否包含字段 po_price
            if (!sqlContext.contains(PO_PRICE)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败，sql中缺少字段，价格：po_price");
                return jSONObject;
            }

            //校验sql
            List<BaseErpEntity> dataList = initErpSysData.findDataByMethodNoAndSql(ErpTopicName.ErpPurchaseFlow.getMethod(), sqlContext, 0);
            // 数据的po_time字段校验
            List<ErpPurchaseFlowDTO> erpPurchaseFlowList = PojoUtils.map(dataList, ErpPurchaseFlowDTO.class);
            List<ErpPurchaseFlowDTO> poTimeNullList = erpPurchaseFlowList.stream().filter(d -> ObjectUtil.isNull(d.getPoTime())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(poTimeNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 po_time 有为空的,请检查 po_time 字段");
                return jSONObject;
            }
            // enterprise_inner_code 字段校验
            List<ErpPurchaseFlowDTO> enterpriseInnerCodeNullList = erpPurchaseFlowList.stream().filter(d -> StrUtil.isBlank(d.getEnterpriseInnerCode())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(enterpriseInnerCodeNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 enterprise_inner_code 有为空的,请检查 enterprise_inner_code 字段");
                return jSONObject;
            }
            // goods_in_sn 字段校验
            List<ErpPurchaseFlowDTO> goodsInnerSnNullList = erpPurchaseFlowList.stream().filter(d -> StrUtil.isBlank(d.getGoodsInSn())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsInnerSnNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 goods_in_sn 有为空的,请检查 goods_in_sn 字段");
                return jSONObject;
            }
            // po_batch_no 字段校验
            List<ErpPurchaseFlowDTO> goodsBatchNoNullList = erpPurchaseFlowList.stream().filter(d -> StrUtil.isBlank(d.getPoBatchNo())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(goodsBatchNoNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 po_batch_no 有为空的,请检查 po_batch_no 字段");
                return jSONObject;
            }
            // po_quantity 字段校验
            List<ErpPurchaseFlowDTO> poQuantityNullList = erpPurchaseFlowList.stream().filter(d -> ObjectUtil.isNull(d.getPoQuantity())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(poQuantityNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 po_quantity 有为空的,请检查 po_quantity 字段");
                return jSONObject;
            }
            // po_price 字段校验
            List<ErpPurchaseFlowDTO> poPriceNullList = erpPurchaseFlowList.stream().filter(d -> ObjectUtil.isNull(d.getPoPrice())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(poPriceNullList)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 字段 po_price 有为空的,请检查 po_price 字段");
                return jSONObject;
            }

            //保存本地
            if (!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)) {
                jSONObject.put("code", 100);
                jSONObject.put("message", "[ERP采购订单流向] 配置信息保存本地失败");
                return jSONObject;
            }

            if (taskConfig.getTaskStatus().equals("1")) {
                jobTaskService.addJob(taskConfig);
            } else {
                jobTaskService.deleteJob(taskConfig);
            } jSONObject.put("code", 200);
        } catch (Exception e) {
            jSONObject.put("code", 100);
            jSONObject.put("message", e.getMessage());
            log.error("ERP采购订单流向任务信息保存失败，error -> {}", e);
            e.printStackTrace();
        } return jSONObject;
    }

    @ResponseBody
    @RequestMapping(value = { "/getOrderPurchaseFlowInfo.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public String getOrderInfo(HttpServletRequest request, HttpServletResponse response) {
        TaskConfig taskConfig = null;
        List<TaskConfig> lists = null;
        try {
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + ErpTopicName.ErpPurchaseFlow.getMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(lists)) {
            return null;
        }
        taskConfig = lists.get(0);
        return JSONObject.toJSONString(taskConfig, new SerializerFeature[]{ SerializerFeature.WriteNullStringAsEmpty });
    }

    /**
     * 清理全部缓存
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/deleteOrderPurchaseFlowInfoCache.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject deleteCustomerCache(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            //先判断任务是否还在执行
            TaskConfig taskConfig = null;
            List<TaskConfig> lists = null;
            try {
                lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo=" + ErpTopicName.ErpPurchaseFlow.getMethod());
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
                    if (runTaskConfig.getTaskNo().equals(ErpTopicName.ErpPurchaseFlow.getMethod())) {
                        jSONObject.put("code", 100);
                        jSONObject.put("message", "程序还在运行中，请稍后再试");
                        return jSONObject;
                    }
                }
            }
            //            log.info("[采购订单流向信息同步] 清除客户端缓存数据");
            taskConfigDao.deleteSqliteDataAll(InitErpConfig.DB_PATH, InitErpConfig.DATA_NAME, ErpTopicName.ErpPurchaseFlow.getTopicName());
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
