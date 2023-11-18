package com.yiling.user.system.service.impl;
import com.google.common.collect.Lists;
import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;
import com.yiling.user.system.dto.request.AddOrRemoveMrSalesGoodsRequest;
import com.yiling.user.system.dto.request.UpdateMrSalesGoodsRequest;
import com.yiling.user.system.entity.MrSalesGoodsDetailDO;
import com.yiling.user.system.dao.MrSalesGoodsDetailMapper;
import com.yiling.user.system.service.MrSalesGoodsDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 医药代表可售药品配置信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-06-06
 */
@Service
public class MrSalesGoodsDetailServiceImpl extends BaseServiceImpl<MrSalesGoodsDetailMapper, MrSalesGoodsDetailDO> implements MrSalesGoodsDetailService {

    @Override
    public Map<Long, List<Long>> listByEmployeeIds(List<Long> employeeIds) {
        if (CollUtil.isEmpty(employeeIds)) {
            return MapUtil.empty();
        }

        QueryWrapper<MrSalesGoodsDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(MrSalesGoodsDetailDO::getEmployeeId, employeeIds);
        List<MrSalesGoodsDetailDO> list = this.list(queryWrapper);

        Map<Long, List<Long>> map = list.stream().collect(Collectors.groupingBy(MrSalesGoodsDetailDO::getEmployeeId, Collectors.mapping(MrSalesGoodsDetailDO::getGoodsId, Collectors.toList())));
        return map;
    }

    @Override
    public List<Long> listByEmployeeIdAndGoodsIds(Long employeeId, List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }

        QueryWrapper<MrSalesGoodsDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(MrSalesGoodsDetailDO::getEmployeeId, employeeId)
                .in(MrSalesGoodsDetailDO::getGoodsId, goodsIds);

        List<MrSalesGoodsDetailDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(MrSalesGoodsDetailDO::getGoodsId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Long> listEmoloyeeIdsByGoodsIds(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }

        QueryWrapper<MrSalesGoodsDetailDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(MrSalesGoodsDetailDO::getGoodsId, goodsIds);

        List<MrSalesGoodsDetailDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(MrSalesGoodsDetailDO::getEmployeeId).distinct().collect(Collectors.toList());
    }

    @Override
    public Boolean addOrRemove(AddOrRemoveMrSalesGoodsRequest request) {
        Long employeeId = request.getEmployeeId();
        List<Long> goodsIds = request.getGoodsIds();
        if (CollUtil.isEmpty(goodsIds)) {
            return true;
        }

        // 已添加商品ID集合
        List<Long> existsGoodsIds = this.listByEmployeeIdAndGoodsIds(employeeId, goodsIds);

        if (request.getOpType() == 1) {
            if (CollUtil.isNotEmpty(existsGoodsIds)) {
                goodsIds.removeAll(existsGoodsIds);
            }

            List<MrSalesGoodsDetailDO> entityList = CollUtil.newArrayList();
            goodsIds.forEach(e -> {
                MrSalesGoodsDetailDO entity = new MrSalesGoodsDetailDO();
                entity.setEmployeeId(employeeId);
                entity.setGoodsId(e);
                entity.setOpUserId(request.getOpUserId());
                entityList.add(entity);
            });

            this.saveBatch(entityList);
        } else {
            MrSalesGoodsDetailDO entity = new MrSalesGoodsDetailDO();
            entity.setOpUserId(request.getOpUserId());

            QueryWrapper<MrSalesGoodsDetailDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(MrSalesGoodsDetailDO::getEmployeeId, employeeId)
                    .in(MrSalesGoodsDetailDO::getGoodsId, existsGoodsIds);

            this.batchDeleteWithFill(entity, queryWrapper);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(UpdateMrSalesGoodsRequest request) {
        Long employeeId = request.getEmployeeId();
        List<Long> goodsIds = request.getGoodsIds();

        // 原来关联的可售药品ID集合
        List<Long> originGoodsIds = this.listByEmployeeIds(ListUtil.toList(employeeId)).get(employeeId);
        if (CollUtil.isEmpty(originGoodsIds)) {
            if (CollUtil.isNotEmpty(goodsIds)) {
                AddOrRemoveMrSalesGoodsRequest request1 = new AddOrRemoveMrSalesGoodsRequest();
                request1.setEmployeeId(employeeId);
                request1.setGoodsIds(goodsIds);
                request1.setOpType(1);
                request1.setOpUserId(request.getOpUserId());
                this.addOrRemove(request1);
            }
        } else {
            if (CollUtil.isEmpty(goodsIds)) {
                AddOrRemoveMrSalesGoodsRequest request1 = new AddOrRemoveMrSalesGoodsRequest();
                request1.setEmployeeId(employeeId);
                request1.setGoodsIds(originGoodsIds);
                request1.setOpType(2);
                request1.setOpUserId(request.getOpUserId());
                this.addOrRemove(request1);
            } else {
                AddOrRemoveMrSalesGoodsRequest request1 = new AddOrRemoveMrSalesGoodsRequest();
                request1.setEmployeeId(employeeId);
                request1.setOpUserId(request.getOpUserId());

                List<Long> addGoodsIds = goodsIds.stream().filter(e -> !originGoodsIds.contains(e)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(addGoodsIds)) {
                    request1.setGoodsIds(addGoodsIds);
                    request1.setOpType(1);
                    this.addOrRemove(request1);
                }

                List<Long> removeGoodsIds = originGoodsIds.stream().filter(e -> !goodsIds.contains(e)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(removeGoodsIds)) {
                    request1.setGoodsIds(removeGoodsIds);
                    request1.setOpType(2);
                    this.addOrRemove(request1);
                }
            }
        }

        return true;
    }

}
