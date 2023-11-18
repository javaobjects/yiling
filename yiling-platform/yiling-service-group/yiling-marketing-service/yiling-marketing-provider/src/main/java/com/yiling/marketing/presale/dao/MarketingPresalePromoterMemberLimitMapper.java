package com.yiling.marketing.presale.dao;

import java.util.List;

import com.yiling.marketing.presale.dto.PresaleRelativeDTO;
import com.yiling.marketing.presale.entity.MarketingPresalePromoterMemberLimitDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 预售活动推广方会员表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Repository
public interface MarketingPresalePromoterMemberLimitMapper extends BaseMapper<MarketingPresalePromoterMemberLimitDO> {

    List<PresaleRelativeDTO> getInfoByPresaleId(@Param("ids")List<Long> promoterLimit);
}
