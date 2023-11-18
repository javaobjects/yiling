package com.yiling.cms.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketGoodsRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketGoodsDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateMarketGoodsMapper;
import com.yiling.cms.evaluate.service.HealthEvaluateMarketGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 健康测评关联营销商品表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateMarketGoodsServiceImpl extends BaseServiceImpl<HealthEvaluateMarketGoodsMapper, HealthEvaluateMarketGoodsDO> implements HealthEvaluateMarketGoodsService {

    @Override
    public Long healthEvaluateMarketGoods(HealthEvaluateMarketGoodsRequest request) {
        HealthEvaluateMarketGoodsDO goodsDO = PojoUtils.map(request, HealthEvaluateMarketGoodsDO.class);
        this.saveOrUpdate(goodsDO);
        return goodsDO.getId();
    }

    @Override
    public Boolean delHealthEvaluateMarketGoodsById(Long id) {
        HealthEvaluateMarketGoodsDO entity = new HealthEvaluateMarketGoodsDO();
        entity.setId(id);
        int cnt = this.deleteByIdWithFill(entity);
        return cnt > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public List<HealthEvaluateMarketGoodsDTO> getMarketGoodsByEvaluateId(Long evaluateId) {
        QueryWrapper<HealthEvaluateMarketGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateMarketGoodsDO::getHealthEvaluateId, evaluateId);
        List<HealthEvaluateMarketGoodsDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateMarketGoodsDTO.class);
    }
}
