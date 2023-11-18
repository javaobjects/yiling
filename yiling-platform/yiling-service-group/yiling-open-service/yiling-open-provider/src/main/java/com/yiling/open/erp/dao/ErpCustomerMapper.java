package com.yiling.open.erp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.entity.ErpCustomerDO;

/**
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Repository
public interface ErpCustomerMapper extends ErpEntityMapper, BaseMapper<ErpCustomerDO> {

    /**
     * 更改同步状态,通过Id和原有的状态修改状态
     * @return
     */
    Integer updateSyncStatusByStatusAndId(@Param("id") Long id, @Param("syncStatus") Integer syncStatus, @Param("oldSyncStatus") Integer oldSyncStatus, @Param("syncMsg") String syncMsg);

    /**
     *
     * @param id
     * @param syncStatus
     * @param syncMsg
     * @return
     */
    Integer updateSyncStatusAndMsg(@Param("id") Long id, @Param("syncStatus") Integer syncStatus, @Param("syncMsg") String syncMsg);

    /**
     * 获取erp库商品信息
     * @param ids
     * @return
     */
    List<ErpCustomerDO> findErpCustomerByIds(@Param("ids") List<Long> ids);

    /**
     * 通过商品内码查询商品信息
     * @param list
     * @param suDeptNo
     * @param suId
     * @return
     */
    List<ErpCustomerDO> findBySyncStatusAndInSnList(@Param("list") List<String> list, @Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    List<ErpCustomerDO> findMd5BySuId(@Param("suId") Long suId);

    List<ErpCustomerDO> syncCustomer();

    /**
     * 获取ErpCustomerId列表
     * @param request
     * @return
     */
    List<Long> getErpCustomerIdList(@Param("request") ErpCustomerQueryRequest request);

    /**
     * 分页获取ErpCustomer Id
     * @param page
     * @param request
     * @return
     */
    Page<Long> getPageListErpCustomerId(Page<ErpCustomerDO> page, @Param("request") ErpCustomerQueryRequest request);

    Integer updateOperTypeGoodsBatchFlowBySuId(@Param("suId") Long suId);
}
