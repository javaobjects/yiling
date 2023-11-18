package com.yiling.settlement.b2b.bo;

import java.util.List;

import com.yiling.settlement.b2b.entity.SettlementDO;
import com.yiling.settlement.b2b.entity.SettlementDetailDO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 结算单元数据
 * @author dexi.yao
 * @date 2022-01-14
 */
@Data
@Accessors(chain = true)
public class B2BSettlementMetadataBO {

	/**
	 * 结算单列表
	 */
	List<SettlementDO> settlementList;

	/**
	 * 货款结算单明细,ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
	 */
	List<SettlementDetailDO> goodsSettDetailList;

	/**
	 * 促销结算单明细,ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
	 */
	List<SettlementDetailDO> saleSettDetailList;

	/**
	 * 预售违约金明细,ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
	 */
	List<SettlementDetailDO> pdSettDetailList;

}
