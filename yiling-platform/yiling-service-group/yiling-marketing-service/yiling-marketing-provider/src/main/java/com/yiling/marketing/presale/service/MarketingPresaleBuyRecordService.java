package com.yiling.marketing.presale.service;

import java.util.List;
import java.util.Map;

import com.yiling.marketing.presale.dto.PresaleActivityBuyRecorderDTO;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 预售活动购买记录表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-13
 */
public interface MarketingPresaleBuyRecordService extends BaseService<MarketingPresaleBuyRecordDO> {

    List<PresaleActivityDTO> getOrderCountBypresaleIds(List<Long> presaleIds);
}
