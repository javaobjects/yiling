package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import com.yiling.open.erp.dto.request.QuerySaleFlowConditionRequest;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.dto.request.ErpFlowControlFailRequest;
import com.yiling.open.erp.dto.request.QueryErpFlowControlPageRequest;
import com.yiling.open.erp.entity.ErpFlowControlDO;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-22
 */
@Repository
public interface ErpFlowControlMapper extends ErpEntityMapper, BaseMapper<ErpFlowControlDO> {

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

    List<ErpFlowControlDO> syncFlowControl();

    List<ErpFlowControlDO> syncFlowControlPage(@Param("requestList")List<QuerySaleFlowConditionRequest> requestList);

    /**
     * 根据id更新同步失败的状态、信息、统计数量
     *
     * @param request
     * @return
     */
    Integer updateSyncStatusAndMsgAndFailnumber(@Param("request") ErpFlowControlFailRequest request);

    /**
     * 根据文件时间删除 erp_flow_control 中数据
     *
     * @param fileTimeEnd
     * @param suId
     * @return
     */
    Integer deleteByfileTime(@Param("fileTimeEnd") String fileTimeEnd, @Param("suId") Long suId);

    /**
     * 分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<ErpFlowControlDO> page(Page page, @Param("request") QueryErpFlowControlPageRequest request);

    /**
     * 通过商业公司编号、部门编号获取第一次对接时间
     * @param suId
     * @param suDeptNo
     */
    Date getInitDateBySuIdAndSuDeptNo(@Param("suId")Long suId,@Param("suDeptNo")String suDeptNo);
}
