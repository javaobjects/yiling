package com.yiling.marketing.presale.dao;

import java.util.List;
import java.util.Map;

import com.yiling.marketing.presale.dto.PresaleActivityBuyRecorderDTO;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 预售活动购买记录表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-13
 */
@Repository
public interface MarketingPresaleBuyRecordMapper extends BaseMapper<MarketingPresaleBuyRecordDO> {

    List<PresaleActivityBuyRecorderDTO> getGooodsBuyCount(@Param("goodsIds") List<Long> goodsIds, @Param("buyEid")Long buyEid,@Param("presaleIds") List<Long> presaleIds);

    List<PresaleActivityDTO> getOrderCountBypresaleIds(@Param("orderIds")List<Long> presaleIds);
}
