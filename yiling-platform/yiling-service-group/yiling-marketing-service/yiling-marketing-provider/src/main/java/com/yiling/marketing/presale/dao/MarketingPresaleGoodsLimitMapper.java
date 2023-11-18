package com.yiling.marketing.presale.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.entity.MarketingPresaleGoodsLimitDO;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 预售活动商品表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Repository
public interface MarketingPresaleGoodsLimitMapper extends BaseMapper<MarketingPresaleGoodsLimitDO> {

    Page<MarketingPresaleActivityDO> pageList(Page<StrategyActivityDO> objectPage, @Param("request")QueryStrategyActivityPageRequest request);

    Integer getBuyerNumber(@Param("id")Long id);

    Integer getMemberNumber(@Param("id")Long id);

    Integer getPromoterNumber(@Param("id")Long id);
}
