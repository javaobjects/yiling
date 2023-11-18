package com.yiling.goods.standard.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardGoodsPicMapper;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 标准库商品图片表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardGoodsPicServiceImpl extends BaseServiceImpl<StandardGoodsPicMapper, StandardGoodsPicDO> implements StandardGoodsPicService {

    /**
     * 通过standardId和标准商品规格ID查询规格图片
     *
     * @param standardId
     * @param sellSpecificationsId
     * @return
     */
    @Override
    public List<StandardGoodsPicDO> getStandardGoodsPic(Long standardId, Long sellSpecificationsId) {
        QueryWrapper<StandardGoodsPicDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsPicDO::getStandardId, standardId)
                .eq(StandardGoodsPicDO::getSellSpecificationsId, sellSpecificationsId)
                .orderByAsc(StandardGoodsPicDO::getPicOrder);

        return this.list(wrapper);
    }

    @Override
    public List<StandardGoodsPicDO> getStandardGoodsPicByStandardId(Long standardId) {
        QueryWrapper<StandardGoodsPicDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsPicDO::getStandardId, standardId);
        wrapper.lambda().orderByDesc(StandardGoodsPicDO::getSellSpecificationsId);
        return this.list(wrapper);
    }

    @Override
    public Map<Long, List<StandardGoodsPicDO>> getMapStandardGoodsPicByStandardId(List<Long> standardIds) {
        if (CollUtil.isEmpty(standardIds)) {
            return MapUtil.empty();
        }
        standardIds = standardIds.stream().filter(e -> !e.equals(0L)).collect(Collectors.toList());
        if (CollUtil.isEmpty(standardIds)) {
            return MapUtil.empty();
        }
        QueryWrapper<StandardGoodsPicDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StandardGoodsPicDO::getStandardId, standardIds);
        List<StandardGoodsPicDO> list = this.list(wrapper);
        Map<Long, List<StandardGoodsPicDO>> map = new HashMap<>();
        for (StandardGoodsPicDO standardGoodsPicDO : list) {
            if (map.containsKey(standardGoodsPicDO.getStandardId())) {
                map.get(standardGoodsPicDO.getStandardId()).add(standardGoodsPicDO);
            } else {
                List<StandardGoodsPicDO> standardGoodsPicDOList = new ArrayList<>();
                standardGoodsPicDOList.add(standardGoodsPicDO);
                map.put(standardGoodsPicDO.getStandardId(), standardGoodsPicDOList);
            }
        }
        return map;
    }

    /**
     * 批量新增规格图片
     *
     * @param list
     * @return
     */
    @Override
    public Boolean saveStandardGoodsPicBatch(List<StandardGoodsPicDO> list) {
        return this.saveBatch(list);
    }

    /**
     * 删除图片
     *
     * @param standardId 标准库商品
     * @param opUserId   操作人
     */
    @Override
    public void deleteStandardGoodsPicByStandardId(Long standardId, Long opUserId) {
        StandardGoodsPicDO picDO = new StandardGoodsPicDO();
        picDO.setUpdateTime(new Date());
        picDO.setUpdateUser(opUserId);
        QueryWrapper<StandardGoodsPicDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsPicDO::getStandardId, standardId);
        batchDeleteWithFill(picDO, wrapper);
    }


}
