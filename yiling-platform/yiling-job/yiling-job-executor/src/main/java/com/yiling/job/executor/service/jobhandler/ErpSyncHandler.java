package com.yiling.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.EasIncrementStampApi;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.api.ErpDataStatApi;
import com.yiling.open.erp.api.ErpFlowControlApi;
import com.yiling.open.erp.api.ErpGoodsApi;
import com.yiling.open.erp.api.ErpGoodsBatchApi;
import com.yiling.open.erp.api.ErpGoodsBatchFlowApi;
import com.yiling.open.erp.api.ErpGoodsCustomerPriceApi;
import com.yiling.open.erp.api.ErpGoodsGroupPriceApi;
import com.yiling.open.erp.api.ErpOrderPurchaseDeliveryApi;
import com.yiling.open.erp.api.ErpOrderSendApi;
import com.yiling.open.erp.api.ErpPurchaseFlowApi;
import com.yiling.open.erp.api.ErpSaleFlowApi;
import com.yiling.open.erp.api.ErpSettlementApi;
import com.yiling.open.erp.api.ErpShopSaleFlowApi;
import com.yiling.open.ftp.api.FlowFtpApi;
import com.yiling.open.heart.api.ErpClientNoHeartApi;
import com.yiling.user.enterprise.api.CustomerApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author shuan
 */
@Component
@Slf4j
public class ErpSyncHandler {

    @DubboReference(timeout = 1000 * 60)
    private GoodsApi goodsApi;

    @DubboReference(async = true)
    private ErpClientApi erpClientApi;

    @DubboReference
    private EasIncrementStampApi easIncrementStampApi;

    @DubboReference(async = true)
    private CustomerApi customerApi;

    @DubboReference(async = true)
    private ErpGoodsApi erpGoodsApi;

    @DubboReference(async = true)
    private ErpGoodsBatchApi erpGoodsBatchApi;

    @DubboReference(async = true)
    private ErpOrderSendApi erpOrderSendApi;

    @DubboReference(async = true)
    private ErpGoodsCustomerPriceApi erpGoodsCustomerPriceApi;

    @DubboReference(async = true)
    private ErpGoodsGroupPriceApi erpGoodsGroupPriceApi;

    @DubboReference(async = true)
    private ErpSettlementApi erpSettlementApi;

    @DubboReference(async = true)
    private ErpCustomerApi erpCustomerApi;

    @DubboReference(async = true)
    private ErpFlowControlApi erpFlowControlApi;

    @DubboReference(async = true)
    private ErpDataStatApi erpDataStatApi;

    @DubboReference(async = true)
    private ErpPurchaseFlowApi erpPurchaseFlowApi;

    @DubboReference(async = true)
    private ErpSaleFlowApi erpSaleFlowApi;

    @DubboReference(async = true)
    private ErpGoodsBatchFlowApi erpGoodsBatchFlowApi;

    @DubboReference(async = true)
    private FlowFtpApi flowFtpApi;

    @DubboReference(async = true)
    private ErpOrderPurchaseDeliveryApi erpOrderPurchaseDeliveryApi;

    @DubboReference(async = true)
    private ErpClientNoHeartApi erpClientNoHeartApi;

    @DubboReference(async = true)
    private ErpShopSaleFlowApi erpShopSaleFlowApi;

    @JobLog
    @XxlJob("erpSyncFlowControlHandler")
    public ReturnT<String> erpSyncFlowControl(String param) throws Exception {
        log.info("任务开始：流向信息同步");
        long start = System.currentTimeMillis();
        erpFlowControlApi.syncFlowControl();

        log.info("任务结束：流向信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncCustomerHandler")
    public ReturnT<String> erpSyncCustomer(String param) throws Exception {
        log.info("任务开始：客户信息同步");
        long start = System.currentTimeMillis();
        erpCustomerApi.syncCustomer();

        log.info("任务结束：客户信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncSettlementHandler")
    public ReturnT<String> erpSyncSettlement(String param) throws Exception {
        log.info("任务开始：客户信息同步");
        long start = System.currentTimeMillis();
        erpSettlementApi.syncSettlement();
        log.info("任务结束：客户信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncGoodsHandler")
    public ReturnT<String> erpSyncGoods(String param) throws Exception {
        log.info("任务开始：商品信息同步");
        long start = System.currentTimeMillis();
        erpGoodsApi.syncGoods();
        log.info("任务结束：商品信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncEasGoodsBatchHandler")
    public ReturnT<String> erpSyncEasGoodsBatch(String param) {
        log.info("任务开始：eas库存信息同步");
        long start = System.currentTimeMillis();
        erpGoodsBatchApi.syncEasGoodsBatch();
        log.info("任务结束：eas库存信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncGoodsBatchHandler")
    public ReturnT<String> erpSyncGoodsBatch(String param) {
        erpGoodsBatchApi.syncGoodsBatch();
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncEasGoodsPriceHandler")
    public ReturnT<String> erpSyncEasGoodsPrice(String param) {
        log.info("任务开始：eas客户定价信息同步");
        long start = System.currentTimeMillis();
        erpGoodsCustomerPriceApi.syncEasGoodsCustomerPrice();
        log.info("任务结束：eas客户定价信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncDayunheGoodsPriceHandler")
    public ReturnT<String> erpSyncDayunheGoodsPrice(String param) {
        log.info("任务开始：大运河工业客户定价信息同步");
        long start = System.currentTimeMillis();
        erpGoodsCustomerPriceApi.syncDayunheGoodsCustomerPrice();
        log.info("任务结束：大运河工业客户定价信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncGoodsPriceHandler")
    public ReturnT<String> erpSyncGoodsPrice(String param) {
        log.info("任务开始：客户定价信息同步");
        long start = System.currentTimeMillis();
        erpGoodsCustomerPriceApi.syncGoodsCustomerPrice();
        log.info("任务结束：客户定价信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncGoodsGroupPriceHandler")
    public ReturnT<String> erpSyncGoodsGroupPrice(String param) {
        log.info("任务开始：客户分组定价信息同步");
        long start = System.currentTimeMillis();
        erpGoodsGroupPriceApi.syncGoodsGroupPrice();
        log.info("任务结束：客户分组定价信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncOrderSendHandler")
    public ReturnT<String> erpSyncOrderSend(String param) throws Exception {
        log.info("任务开始：发货单信息同步");
        long start = System.currentTimeMillis();
        erpOrderSendApi.syncOrderSend();
        log.info("任务结束：发货单信息同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncDataStatHandler")
    public ReturnT<String> erpSyncDataStat(String param) throws Exception {
        log.info("任务开始：统计请求次数同步");
        long start = System.currentTimeMillis();
        erpDataStatApi.saveDataStat();

        log.info("任务结束：统计请求次数同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncPurchaseFlowHandler")
    public ReturnT<String> erpSyncPurchaseFlow(String param) throws Exception {
        log.info("任务开始：采购单流向同步");
        long start = System.currentTimeMillis();
        erpPurchaseFlowApi.syncPurchaseFlow();

        log.info("任务结束：采购单流向同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncSaleFlowHandler")
    public ReturnT<String> erpSyncSaleFlow(String param) throws Exception {
        log.info("任务开始：销售单流向同步");
        long start = System.currentTimeMillis();
        erpSaleFlowApi.syncSaleFlow();

        log.info("任务结束：销售单流向同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncGoodsBatchFlowHandler")
    public ReturnT<String> erpSyncGoodsBatchFlow(String param) throws Exception {
        log.info("任务开始：库存流向同步");
        long start = System.currentTimeMillis();
        erpGoodsBatchFlowApi.syncGoodsBatchFlow();

        log.info("任务结束：库存流向同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpOrderPurchaseDeliveryHandler")
    public ReturnT<String> erpSyncOrderPurchaseDelivery(String param) throws Exception {
        log.info("任务开始：采购入库单同步");
        long start = System.currentTimeMillis();
        erpOrderPurchaseDeliveryApi.syncOrderPurchaseDelivery();

        log.info("任务结束：采购入库单同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncExcelFlowHandler")
    public ReturnT<String> erpSyncExcelFlow(String param) throws Exception {
        log.info("任务开始：excle对接流向同步");
        long start = System.currentTimeMillis();
        flowFtpApi.readFlowExcel();

        log.info("任务结束：excle对接流向同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpClientNoHeartHandler")
    public ReturnT<String> erpClientNoHeart(String param) throws Exception {
        log.info("job任务开始：昨天无心跳的企业信息统计开始");
        long start = System.currentTimeMillis();
        erpClientApi.handleErpClientsNoHeartBetween24h();
        log.info("job任务结束：erp昨天无心跳企业信息统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncStatFlowHandler")
    public ReturnT<String> erpSyncStatFlow(String param) throws Exception {
        log.info("job任务开始：昨天有数据同步的企业信息统计开始");
        long start = System.currentTimeMillis();
        // 昨天有无数据同步的企业信息统计
        erpDataStatApi.erpSyncStatFlow();
        // 最新采集日期统计
        erpDataStatApi.erpSyncLastestCollectDate();
        log.info("job任务结束：昨天有数据同步的企业信息统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncLastestFlowDateHandler")
    public ReturnT<String> erpSyncLastestFlowDate(String param) throws Exception {
        log.info("job任务开始：最新流向日期统计开始");
        long start = System.currentTimeMillis();
        // 最新流向日期统计
        erpDataStatApi.erpSyncLastestFlowDate();
        log.info("job任务结束：最新流向日期统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpClientDataInitStatusHandler")
    public ReturnT<String> erpClientDataInitStatus(String param) throws Exception {
        log.info("job任务开始：数据初始化状态更新开始");
        long start = System.currentTimeMillis();
        erpClientApi.erpClientDataInitStatusUpdate();
        log.info("job任务结束：数据初始化状态更新结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowDirectConnectMonitorHandler")
    public ReturnT<String> flowDirectConnectMonitor(String param) throws Exception {
        log.info("job任务开始：流向直连接口监控信息统计开始");
        long start = System.currentTimeMillis();
        // 流向直连接口监控信息统计
        erpDataStatApi.flowDirectConnectMonitor();
        log.info("job任务结束：流向直连接口监控信息统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowCollectHeartStatisticsHandler")
    public ReturnT<String> flowCollectHeartStatistics(String param) throws Exception {
        log.info("job任务开始：日流向心跳统计开始");
        long start = System.currentTimeMillis();
        // 流向直连接口监控信息统计
        erpDataStatApi.flowCollectHeartStatistics();
        log.info("job任务结束：日流向心跳统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("flowCollectDataStatisticsHandler")
    public ReturnT<String> flowCollectDataStatistics(String param) throws Exception {
        log.info("job任务开始：日流向数据统计开始");
        long start = System.currentTimeMillis();
        // 流向直连接口监控信息统计
        erpDataStatApi.flowCollectDataStatistics();
        log.info("job任务结束：日流向数据统计结束。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpSyncShopSaleFlowHandler")
    public ReturnT<String> erpSyncShopSaleFlow(String param) throws Exception {
        log.info("任务开始：连锁纯销流向同步");
        long start = System.currentTimeMillis();
        erpShopSaleFlowApi.syncShopSaleFlow();

        log.info("任务结束：连锁纯销流向同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }
}
