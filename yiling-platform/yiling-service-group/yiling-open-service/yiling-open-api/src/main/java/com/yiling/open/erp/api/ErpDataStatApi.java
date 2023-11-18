package com.yiling.open.erp.api;

import java.util.List;

import com.yiling.open.erp.bo.ErpErpDataStatCountBO;
import com.yiling.open.erp.dto.request.QueryErpDataStatCountRequest;

/**
 * @Description:
 * @Author: shuang.zhang
 * @Date: 2018/10/9
 */
public interface ErpDataStatApi {

    /**
     * 从redis读取统计数据持久化到数据库
     */
    void saveDataStat();

    /**
     * 根据suid、开始/结束时间 统计请求数量
     * @param request
     * @return
     */
    List<ErpErpDataStatCountBO> getErpDataStatCount(QueryErpDataStatCountRequest request);

    /**
     * 昨天有无数据同步的企业信息统计
     */
    void erpSyncStatFlow();

    /**
     * 最新采集日期、最新流向日期统计（更新erpClient表）
     */
    void erpSyncLastestCollectDate();

    /**
     * 最新流向日期统计（更新erpClient表，并且发送mq给清洗队列）
     */
    void erpSyncLastestFlowDate();

    /**
     * 流向直连接口监控信息统计（过去30天未上传的天数）
     */
    void flowDirectConnectMonitor();

    /**
     * 日流向心跳统计
     */
    void flowCollectHeartStatistics();

    /**
     * 日流向数据统计
     */
    void flowCollectDataStatistics();

}
