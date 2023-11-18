package com.yiling.dataflow.wash.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dao.FlowGoodsBatchSafeMapper;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchSafeDO;
import com.yiling.dataflow.wash.service.FlowGoodsBatchSafeService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 安全库存表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-03-06
 */
@Service
public class FlowGoodsBatchSafeServiceImpl extends BaseServiceImpl<FlowGoodsBatchSafeMapper, FlowGoodsBatchSafeDO> implements FlowGoodsBatchSafeService {

    @Override
    public Page<FlowGoodsBatchSafeDO> listPage(QueryFlowGoodsBatchSafePageRequest request) {
        Page<FlowGoodsBatchSafeDO> page = new Page<>(request.getCurrent(), request.getSize());
        Long crmGoodsCode = request.getCrmGoodsCode();
        List<Long> crmGoodsCodeList = request.getCrmGoodsCodeList();
        // 标准商品名称、规格同时查询
        if (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue() && CollUtil.isNotEmpty(crmGoodsCodeList)) {
            if (!crmGoodsCodeList.contains(crmGoodsCode)) {
                return page;
            }
        }
        return this.page(page, getListPageQueryWrapper(request));
    }

    private LambdaQueryWrapper<FlowGoodsBatchSafeDO> getListPageQueryWrapper(QueryFlowGoodsBatchSafePageRequest request) {
        LambdaQueryWrapper<FlowGoodsBatchSafeDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        // 搜索机构编码
        Long crmEnterpriseId = request.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmEnterpriseId) && 0 != crmEnterpriseId.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchSafeDO::getCrmEnterpriseId, crmEnterpriseId);
        }
        // 数据权限经销商编码、所属省份编码
        List<String> crmProvinceCodeList = request.getCrmProvinceCodeList();
        List<Long> crmEnterpriseIdList = request.getCrmEnterpriseIdList();
        boolean crmIdListFlag = CollUtil.isNotEmpty(crmEnterpriseIdList) ? true : false;
        boolean crmProvinceCodeListFlag = CollUtil.isNotEmpty(crmProvinceCodeList) ? true : false;
        if (crmIdListFlag && crmProvinceCodeListFlag){
            lambdaQueryWrapper.and((wrapper) -> {
                wrapper.in(FlowGoodsBatchSafeDO::getCrmEnterpriseId, crmEnterpriseIdList)
                        .or()
                        .in(FlowGoodsBatchSafeDO::getCrmProvinceCode, crmProvinceCodeList);
            });
        } else if (crmIdListFlag){
            lambdaQueryWrapper.in(FlowGoodsBatchSafeDO::getCrmEnterpriseId, crmEnterpriseIdList);
        } else if (crmProvinceCodeListFlag){
            lambdaQueryWrapper.in(FlowGoodsBatchSafeDO::getCrmProvinceCode, crmProvinceCodeList);
        }

        // 商品编码
        Long crmGoodsCode = request.getCrmGoodsCode();
        List<Long> crmGoodsCodeList = request.getCrmGoodsCodeList();
        if (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue() && CollUtil.isNotEmpty(crmGoodsCodeList)) {
            if (crmGoodsCodeList.contains(crmGoodsCode)){
                lambdaQueryWrapper.eq(FlowGoodsBatchSafeDO::getCrmGoodsCode, crmGoodsCode);
            } else {
                lambdaQueryWrapper.eq(FlowGoodsBatchSafeDO::getCrmGoodsCode, -1L);
            }
        } else if (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchSafeDO::getCrmGoodsCode, crmGoodsCode);
        } else if (CollUtil.isNotEmpty(crmGoodsCodeList)){
            lambdaQueryWrapper.in(FlowGoodsBatchSafeDO::getCrmGoodsCode, crmGoodsCodeList);
        }
        // 操作时间：
        // 1、更新时间 = '1970-01-01 00:00:00' and 创建时间 >= ${start} and 创建时间 <= ${end}
        // 2、更新时间 > '1970-01-01 00:00:00' and 更新时间 >= ${start} and 更新时间 <= ${end}
        if (ObjectUtil.isNotNull(request.getOpTimeStart()) && ObjectUtil.isNotNull(request.getOpTimeEnd())) {
            Date opTimeStart = DateUtil.beginOfDay(request.getOpTimeStart());
            Date opTimeEnd = DateUtil.endOfDay(request.getOpTimeEnd());
            lambdaQueryWrapper.and((wrapper) -> {
                wrapper.eq(FlowGoodsBatchSafeDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(FlowGoodsBatchSafeDO::getCreateTime, opTimeStart).le(FlowGoodsBatchSafeDO::getCreateTime, opTimeEnd)
                        .or()
                        .gt(FlowGoodsBatchSafeDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(FlowGoodsBatchSafeDO::getUpdateTime, opTimeStart).le(FlowGoodsBatchSafeDO::getUpdateTime, opTimeEnd);
            });
        }

        lambdaQueryWrapper.orderByDesc(FlowGoodsBatchSafeDO::getCreateTime);
        return lambdaQueryWrapper;
    }

}
