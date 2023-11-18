package com.yiling.open.erp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.entity.ErpGoodsGroupPriceDO;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-03
 */
@Repository
public interface ErpGoodsGroupPriceMapper extends ErpEntityMapper, BaseMapper<ErpGoodsGroupPriceDO> {

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
     * @param id
     * @param syncMsg
     * @return
     */
    Integer retryGoodsGroupPrice(@Param("id") Long id,
                                   @Param("syncMsg") String syncMsg);

    /**
     * 重新同步商品定价
     * @return
     */
    List<ErpGoodsGroupPriceDO> syncGoodsGroupPrice();

    /**
     * 获取erp的客户定价
     * @param suId
     * @return
     */
    List<ErpGoodsGroupPriceDO> getGoodsGroupPriceBySuId(@Param("suId") Long suId);

    List<ErpGoodsGroupPriceDO> findBySyncStatusAndInSnList(@Param("list") List<String> list,
                                                              @Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

    Integer updateOperTypeGoodsBatchFlowBySuId(@Param("suId") Long suId);

}
