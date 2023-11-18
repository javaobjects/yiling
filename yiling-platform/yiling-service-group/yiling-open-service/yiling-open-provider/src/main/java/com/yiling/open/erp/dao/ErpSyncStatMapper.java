package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.bo.ErpErpDataStatCountBO;
import com.yiling.open.erp.dto.request.ErpSyncStatPageRequest;
import com.yiling.open.erp.dto.request.QueryErpDataStatCountRequest;
import com.yiling.open.erp.entity.ErpSyncStatDO;

/**
 * @Description:
 * @Author: shuang.zhang
 * @Date: 2018/10/11
 */
public interface ErpSyncStatMapper extends BaseMapper<ErpSyncStatDO> {

    int save(ErpSyncStatDO erpSyncStat);

    Integer queryInvokeNums(@Param("suId") Long suId, @Param("taskNo") String taskNo, @Param("statDate") String statDate, @Param("statHour") Integer statHour);

    /**
     * 根据suid、开始/结束时间 统计心跳数量
     * @param request
     * @return
     */
    List<ErpErpDataStatCountBO> getErpDataStatCount(@Param("request") QueryErpDataStatCountRequest request);

    Page<ErpSyncStatDO> page(Page<ErpSyncStatDO> page, @Param("request") ErpSyncStatPageRequest request);

    List<ErpSyncStatDO> getOneBySuidAndSuDeptNoAndStatDate(@Param("suId") Long suId, @Param("suDeptNo") String suDeptNo, @Param("statDate") String statDate, @Param("taskNoList") List<String> taskNoList);

    List<ErpSyncStatDO> listBySuidAndSuDeptNoAndStatDateAndTaskNoList(@Param("suId") Long suId, @Param("suDeptNo") String suDeptNo, @Param("statDateStart") String statDateStart, @Param("statDateEnd") String statDateEnd, @Param("taskNoList") List<String> taskNoList);

    Date getMaxTaskTimeByEid(@Param("suId") Long suId, @Param("suDeptNo") String suDeptNo, @Param("taskNoList") List<String> taskNoList);

}
