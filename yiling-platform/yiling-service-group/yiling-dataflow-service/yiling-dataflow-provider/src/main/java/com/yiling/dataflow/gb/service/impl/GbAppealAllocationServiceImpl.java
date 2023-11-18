package com.yiling.dataflow.gb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.dao.GbAppealAllocationMapper;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormAllocationPageRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.gb.service.GbAppealAllocationService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 流向团购申诉分配结果表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-05-15
 */
@Service
public class GbAppealAllocationServiceImpl extends BaseServiceImpl<GbAppealAllocationMapper, GbAppealAllocationDO> implements GbAppealAllocationService {

    @Override
    public Page<GbAppealAllocationDO> flowStatisticListPage(QueryGbAppealFormAllocationPageRequest request) {
        Page<GbAppealAllocationDO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.page(page, getListPageQueryWrapper(request));
    }

    @Override
    public List<GbAppealAllocationDO> listByAppealFormIdAndAllocationType(Long appealFormId, Integer allocationType) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(allocationType, "参数 allocationType 不能为空");
        LambdaQueryWrapper<GbAppealAllocationDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealAllocationDO::getAppealFormId, appealFormId);
        if (ObjectUtil.isNotNull(allocationType) && allocationType.intValue() != 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getAllocationType, allocationType);
        }
        List<GbAppealAllocationDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<Long> saveOrUpdateAllocationList(List<SubstractGbAppealFlowAllocationRequest> list) {
        Assert.notEmpty(list, "参数 list 不能为空");
        List<GbAppealAllocationDO> entityList = PojoUtils.map(list, GbAppealAllocationDO.class);
        if (ObjectUtil.isNull(entityList.get(0).getId())) {
            this.saveBatch(entityList);
        } else {
            this.updateBatchById(entityList);
        }
        return entityList.stream().map(GbAppealAllocationDO::getId).collect(Collectors.toList());
    }

    @Override
    public List<GbAppealAllocationDO> listByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId, Integer allocationType) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(flowWashId, "参数 flowWashId 不能为空");
        LambdaQueryWrapper<GbAppealAllocationDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealAllocationDO::getAppealFormId, appealFormId);
        lambdaQueryWrapper.eq(GbAppealAllocationDO::getFlowWashId, flowWashId);
        if (ObjectUtil.isNotNull(allocationType) && allocationType.intValue() > 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getAllocationType, allocationType);
        }
        List<GbAppealAllocationDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public int deleteByIdList(DeleteGbAppealFlowAllocationRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notEmpty(request.getIdList(), "参数 idList 不能为空");

        GbAppealAllocationDO entity = new GbAppealAllocationDO();
        entity.setOpUserId(request.getOpUserId());
        entity.setOpTime(request.getOpTime());
        LambdaQueryWrapper<GbAppealAllocationDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealAllocationDO::getId, request.getIdList());
        return this.batchDeleteWithFill(entity, lambdaQueryWrapper);
    }

    private LambdaQueryWrapper<GbAppealAllocationDO> getListPageQueryWrapper(QueryGbAppealFormAllocationPageRequest request) {
        LambdaQueryWrapper<GbAppealAllocationDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        Long appealFormId = request.getAppealFormId();
        if (ObjectUtil.isNotNull(appealFormId) && appealFormId.intValue() > 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getAppealFormId, appealFormId);
        }
        Integer allocationType = request.getAllocationType();
        if (ObjectUtil.isNotNull(allocationType) && allocationType.intValue() > 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getAllocationType, allocationType);
        }
        // 所属年月
        String matchMonth = request.getMatchMonth();
        if (StrUtil.isNotBlank(matchMonth)) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getMatchMonth, matchMonth);
        }
        // 团购编号
        String gbNo = request.getGbNo();
        if (StrUtil.isNotBlank(gbNo)) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getGbNo, gbNo);
        }
        // 团购月份
        String gbMonth = request.getGbMonth();
        if (StrUtil.isNotBlank(gbMonth)) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getGbMonth, gbMonth);
        }
        // 商业编码
        Long crmId = request.getCrmId();
        if (ObjectUtil.isNotNull(crmId) && crmId.intValue() > 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getCrmId, crmId);
        }
        // 机构名称
        String enterpriseName = request.getEnterpriseName();
        if (StrUtil.isNotBlank(enterpriseName)) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getEnterpriseName, enterpriseName);
        }
        // 标准产品编码
        Long goodsCode = request.getGoodsCode();
        if (ObjectUtil.isNotNull(goodsCode) && goodsCode.intValue() > 0) {
            lambdaQueryWrapper.eq(GbAppealAllocationDO::getGoodsCode, goodsCode);
        }
        String orderByDescField = request.getOrderByDescField();
        if (StrUtil.isNotBlank(orderByDescField)) {
            lambdaQueryWrapper.last(" order by " + orderByDescField + " desc");
        }
        return lambdaQueryWrapper;
    }
}
