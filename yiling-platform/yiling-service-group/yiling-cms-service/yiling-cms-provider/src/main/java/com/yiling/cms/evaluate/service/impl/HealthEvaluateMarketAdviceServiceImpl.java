package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dao.HealthEvaluateMarketAdviceMapper;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketAdviceDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketAdviceRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketAdviceDO;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketGoodsDO;
import com.yiling.cms.evaluate.service.HealthEvaluateMarketAdviceService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评关联改善建议表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateMarketAdviceServiceImpl extends BaseServiceImpl<HealthEvaluateMarketAdviceMapper, HealthEvaluateMarketAdviceDO> implements HealthEvaluateMarketAdviceService {

    @Override
    public Long healthEvaluateMarketAdvice(HealthEvaluateMarketAdviceRequest request) {
        HealthEvaluateMarketAdviceDO adviceDO = PojoUtils.map(request, HealthEvaluateMarketAdviceDO.class);
        this.saveOrUpdate(adviceDO);
        return adviceDO.getId();
    }

    @Override
    public Boolean delHealthEvaluateMarketAdviceById(Long id) {
        HealthEvaluateMarketAdviceDO entity = new HealthEvaluateMarketAdviceDO();
        entity.setId(id);
        int cnt = this.deleteByIdWithFill(entity);
        return cnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public List<HealthEvaluateMarketAdviceDTO> getMarketAdviceByEvaluateId(Long evaluateId) {
        QueryWrapper<HealthEvaluateMarketAdviceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateMarketAdviceDO::getHealthEvaluateId, evaluateId);
        List<HealthEvaluateMarketAdviceDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateMarketAdviceDTO.class);
    }
}
