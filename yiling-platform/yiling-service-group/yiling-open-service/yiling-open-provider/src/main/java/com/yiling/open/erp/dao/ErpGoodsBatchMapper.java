package com.yiling.open.erp.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.open.erp.entity.ErpGoodsBatchDO;

/**
 * 表erp_goods_batch的数据库操作类
 *
 * @author wanfei.zhang
 * @date 2016-07-06 17:12:07
 */
@Repository
public interface ErpGoodsBatchMapper extends ErpEntityMapper, BaseMapper<ErpGoodsBatchDO> {
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
    Integer retryGoodsBatch(@Param("id") Long id,
                            @Param("syncMsg") String syncMsg);

	/**
	 * 跟进Id获取单条记录
	 *
	 * @param id
	 * @return
	 */
	ErpGoodsBatchDO getById(@Param("id") Long id);

	/**
	 * 通过商业公司和内码获取所有批次信息
	 *
	 * @param suId
	 * @param inSn
	 * @param suDeptNo
	 * @return
	 */
	List<ErpGoodsBatchDO> findByGInSnAndExcludeOperType(@Param("suId") Long suId, @Param("inSn") String inSn,
                                                      @Param("suDeptNo") String suDeptNo);

	/**
	 * 根据商品内码查询的商品库存总量
	 *
	 * @param inSn
	 * @return
	 */
	BigDecimal findSUMGbNumByGInSn(@Param("suId") Long suId, @Param("inSn") String inSn,
                                   @Param("suDeptNo") String suDeptNo);

	/**
	 * 通过商品内码查询所有的库存信息
	 *
	 * @param list
	 * @param suDeptNo
	 * @param suId
	 * @return
	 */
	List<ErpGoodsBatchDO> findBySyncStatusAndInSnList(@Param("list") List<String> list,
                                                    @Param("suDeptNo") String suDeptNo, @Param("suId") Long suId);

	/**
	 * 通过suId获取库存信息
	 *
	 * @param suId
	 * @return
	 */
	List<ErpGoodsBatchDO> findMd5BySuId(@Param("suId") Long suId);

	List<ErpGoodsBatchDO> syncGoodsBatch();

    Integer updateOperTypeGoodsBatchFlowBySuId(@Param("suId") Long suId);
}