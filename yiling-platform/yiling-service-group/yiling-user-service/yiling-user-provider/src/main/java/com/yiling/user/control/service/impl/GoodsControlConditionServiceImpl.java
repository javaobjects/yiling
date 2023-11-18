package com.yiling.user.control.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.control.dao.GoodsControlConditionMapper;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.entity.GoodsControlConditionDO;
import com.yiling.user.control.service.GoodsControlConditionService;

/**
 * <p>
 * 商品控销条件表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
@Service
public class GoodsControlConditionServiceImpl extends BaseServiceImpl<GoodsControlConditionMapper, GoodsControlConditionDO> implements GoodsControlConditionService {

    @Override
    public List<Long> getValueIdByControlId(Long controlId) {
        QueryWrapper<GoodsControlConditionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlConditionDO::getControlId, controlId);
        List<GoodsControlConditionDO> list = this.list(queryWrapper);
        return list.stream().map(e -> e.getConditionValue()).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<GoodsControlConditionDO>> getValueIdByControlIds(List<Long> controlIds) {
        QueryWrapper<GoodsControlConditionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsControlConditionDO::getControlId, controlIds);
        List<GoodsControlConditionDO> list = this.list(queryWrapper);
        return list.stream().collect(Collectors.groupingBy(GoodsControlConditionDO::getControlId));
    }

    @Override
    public Boolean deleteCustomer(DeleteGoodsControlRequest request) {
        GoodsControlConditionDO goodsControlConditionDO = new GoodsControlConditionDO();
        goodsControlConditionDO.setDelFlag(1);
        goodsControlConditionDO.setOpUserId(request.getOpUserId());

        QueryWrapper<GoodsControlConditionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlConditionDO::getControlId, request.getControlId()).in(GoodsControlConditionDO::getConditionValue, request.getConditionValue());
        return this.batchDeleteWithFill(goodsControlConditionDO, queryWrapper) > 0;
    }

    @Override
    public Boolean deleteControlCondition(Long controlId, Long operUserId) {
        GoodsControlConditionDO goodsControlConditionDO = new GoodsControlConditionDO();
        goodsControlConditionDO.setDelFlag(1);
        goodsControlConditionDO.setOpUserId(operUserId);

        QueryWrapper<GoodsControlConditionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsControlConditionDO::getControlId, controlId);
        return this.batchDeleteWithFill(goodsControlConditionDO, queryWrapper) > 0;
    }
}
