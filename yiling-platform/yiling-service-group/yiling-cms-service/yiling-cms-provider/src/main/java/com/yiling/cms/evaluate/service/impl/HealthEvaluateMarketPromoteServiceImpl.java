package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketAdviceDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketPromoteDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketPromoteRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketAdviceDO;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketPromoteDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateMarketPromoteMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateMarketPromoteService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评关联推广服务表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateMarketPromoteServiceImpl extends BaseServiceImpl<HealthEvaluateMarketPromoteMapper, HealthEvaluateMarketPromoteDO> implements HealthEvaluateMarketPromoteService {

    @Override
    public Long healthEvaluateMarketPromote(HealthEvaluateMarketPromoteRequest request) {
        HealthEvaluateMarketPromoteDO promoteDO = PojoUtils.map(request, HealthEvaluateMarketPromoteDO.class);
        this.saveOrUpdate(promoteDO);
        return promoteDO.getId();
    }

    @Override
    public Boolean delHealthEvaluateMarketPromoteById(Long id) {
        HealthEvaluateMarketPromoteDO entity = new HealthEvaluateMarketPromoteDO();
        entity.setId(id);
        int cnt = this.deleteByIdWithFill(entity);
        return cnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public List<HealthEvaluateMarketPromoteDTO> getMarketPromoteByEvaluateId(Long evaluateId) {
        QueryWrapper<HealthEvaluateMarketPromoteDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateMarketPromoteDO::getHealthEvaluateId, evaluateId);
        List<HealthEvaluateMarketPromoteDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateMarketPromoteDTO.class);
    }
}
