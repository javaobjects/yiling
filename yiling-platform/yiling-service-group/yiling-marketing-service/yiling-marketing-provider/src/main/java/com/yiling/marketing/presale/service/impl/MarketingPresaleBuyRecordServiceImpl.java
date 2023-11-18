package com.yiling.marketing.presale.service.impl;

import java.util.List;
import java.util.Map;

import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.marketing.presale.dao.MarketingPresaleBuyRecordMapper;
import com.yiling.marketing.presale.service.MarketingPresaleBuyRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 预售活动购买记录表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-13
 */
@Service
public class MarketingPresaleBuyRecordServiceImpl extends BaseServiceImpl<MarketingPresaleBuyRecordMapper, MarketingPresaleBuyRecordDO> implements MarketingPresaleBuyRecordService {
    @Autowired
    private MarketingPresaleBuyRecordMapper marketingPresaleBuyRecordMapper;

    @Override
    public List<PresaleActivityDTO> getOrderCountBypresaleIds(List<Long> presaleIds) {
        return marketingPresaleBuyRecordMapper.getOrderCountBypresaleIds(presaleIds);
    }
}
