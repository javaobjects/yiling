package com.yiling.marketing.presale.dao;

import java.util.List;

import com.yiling.marketing.presale.dto.PresaleRelativeDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleMemberLimitDO;
import com.yiling.framework.common.base.BaseMapper;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 预售活动会员方案表 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Repository
public interface MarketingPresaleMemberLimitMapper extends BaseMapper<MarketingPresaleMemberLimitDO> {

    List<PresaleRelativeDTO> getInfoByPresaleId(@RequestParam("ids")List<Long> memberLimit);
}
