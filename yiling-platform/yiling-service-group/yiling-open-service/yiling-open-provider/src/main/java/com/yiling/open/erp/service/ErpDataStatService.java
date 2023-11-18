package com.yiling.open.erp.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.request.ErpSyncStatPageRequest;
import com.yiling.open.erp.entity.ErpSyncStatDO;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2018/10/9
 */
public interface ErpDataStatService {
    /**
     * 发送统计数据到local mq
     * @param obj
     */
    boolean sendDataStat(BaseErpEntity obj);

    /**
     * 从redis读取统计数据持久化到数据库
     */
    void saveDataStat();

    /**
     * 查询列表分页
     *
     * @param request
     * @return
     */
    Page<ErpSyncStatDO> page(ErpSyncStatPageRequest request);

    /**
     * 根据总公司id、分公司编码、统计时间，查询一条数据
     *
     * @param suId
     * @param suDeptNo
     * @param statDate
     * @return
     */
    List<ErpSyncStatDO> getOneBySuidAndSuDeptNoAndStatDate(Long suId, String suDeptNo, String statDate, List<String> taskNoList);

    /**
     * 根据总公司id、分公司编码、统计时间、任务编码，查询列表
     *
     * @param suId 总公司id
     * @param suDeptNo 分公司编码
     * @param statDateStart 统计时间开始
     * @param statDateEnd 统计时间结束
     * @param taskNoList 任务编码列表
     * @return
     */
    List<ErpSyncStatDO> listBySuidAndSuDeptNoAndStatDateAndTaskNoList(Long suId, String suDeptNo, String statDateStart, String statDateEnd, List<String> taskNoList);

    /**
     * 根据企业id获取最新的收集时间
     *
     * @return
     */
    Date getMaxTaskTimeByEid(Long suId, String suDeptNo, List<String> taskNoList);

    void flowCollectHeartStatistics();

    void flowCollectDataStatistics();

}
