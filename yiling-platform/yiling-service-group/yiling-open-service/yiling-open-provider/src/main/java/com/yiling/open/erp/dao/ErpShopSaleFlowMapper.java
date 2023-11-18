package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpShopSaleFlowDO;

/**
 * <p>
 * 连锁门店销售明细信息表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-03-20
 */
@Repository
public interface ErpShopSaleFlowMapper extends ErpEntityMapper, BaseMapper<ErpShopSaleFlowDO> {

    /**
     * 更改同步状态,通过Id和原有的状态修改状态
     *
     * @return
     */
    Integer updateSyncStatusByStatusAndId(@Param("id") Long id, @Param("syncStatus") Integer syncStatus,
                                          @Param("oldSyncStatus") Integer oldSyncStatus, @Param("syncMsg") String syncMsg);

    /**
     * @param id
     * @param syncStatus
     * @param syncMsg
     * @return
     */
    Integer updateSyncStatusAndMsg(@Param("id") Long id, @Param("syncStatus") Integer syncStatus,
                                   @Param("syncMsg") String syncMsg);

    /**
     * 获取某一天的流向
     * @param startSoTime 开始时间
     * @param endSoTime 结束时间
     * @param suId 公司编码
     * @return
     */
    List<ErpShopSaleFlowDO> findListBySuIdAndSoTime(@Param("startSoTime") Date startSoTime, @Param("endSoTime") Date endSoTime, @Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    /**
     * 删除op库数据
     * @param id
     * @return
     */
    Integer deleteFlowById(@Param("id") Long id);

    /**
     * 查询没有同步的纯销数据
     * @param requestList
     * @return
     */
    List<ErpShopSaleFlowDO> syncSaleFlowPage(@Param("requestList")List<QuerySaleFlowConditionRequest> requestList);

    /**
     * 解封同步OP库数据到线上
     *
     * @return
     */
    List<ErpShopSaleFlowDO> unLockSynShopSaleFlow(@Param("request") ErpFlowSealedUnLockRequest request);
}
