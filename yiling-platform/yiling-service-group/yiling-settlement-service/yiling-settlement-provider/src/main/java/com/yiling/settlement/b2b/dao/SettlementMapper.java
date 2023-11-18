package com.yiling.settlement.b2b.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.entity.SettlementDO;

/**
 * <p>
 * b2b商家结算单表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Repository
public interface SettlementMapper extends BaseMapper<SettlementDO> {


	/**
	 * 查询平台结算金额信息
	 *
	 * @param today			今日零点
	 * @param yesterday		昨日零点
	 * @return
	 */
	SettlementAmountInfoDTO querySettlementAmountInfo(@Param("today") Date today,@Param("yesterday") Date yesterday,@Param("eid")Long eid);


}
