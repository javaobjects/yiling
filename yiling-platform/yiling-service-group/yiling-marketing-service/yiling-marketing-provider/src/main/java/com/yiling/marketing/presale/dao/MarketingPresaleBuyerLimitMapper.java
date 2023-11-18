package com.yiling.marketing.presale.dao;

import java.util.List;

import com.yiling.marketing.presale.dto.PresaleRelativeDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyerLimitDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 预售活动客户限制表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Repository
public interface MarketingPresaleBuyerLimitMapper extends BaseMapper<MarketingPresaleBuyerLimitDO> {

    List<PresaleRelativeDTO> getInfoByPresaleId(@Param("ids") List<Long> buyerLimit);
}
