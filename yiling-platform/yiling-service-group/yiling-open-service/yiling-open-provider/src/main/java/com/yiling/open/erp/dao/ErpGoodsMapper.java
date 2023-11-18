/**
 * 
 */
package com.yiling.open.erp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.open.erp.entity.ErpGoodsDO;


/**
 * @Author: shuang.zhang
 * @Email: shuang.zhang@rograndec.com
 * @CreateDate: 2018年7月17日
 * @Version: 1.0
 */
@Repository
public interface ErpGoodsMapper extends ErpEntityMapper, BaseMapper<ErpGoodsDO> {
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
    List<ErpGoodsDO> findErpGoodsByIds(@Param("ids") List<Long> ids);

    /**
     * 通过商品内码查询商品信息
     * @param list
     * @param suDeptNo
     * @param suId
     * @return
     */
    List<ErpGoodsDO> findBySyncStatusAndInSnList(@Param("list") List<String> list, @Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    List<ErpGoodsDO> findMd5BySuId(@Param("suId") Long suId);

    List<ErpGoodsDO> syncGoods();

    Integer updateOperTypeGoodsBatchFlowBySuId(@Param("suId") Long suId);

}
