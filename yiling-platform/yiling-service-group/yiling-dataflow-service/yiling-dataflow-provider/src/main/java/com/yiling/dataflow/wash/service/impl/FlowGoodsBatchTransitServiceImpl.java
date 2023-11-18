package com.yiling.dataflow.wash.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dao.FlowGoodsBatchTransitMapper;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchTransitDO;
import com.yiling.dataflow.wash.service.FlowGoodsBatchTransitService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 在途订单库存、终端库存表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-03-06
 */
@Service
public class FlowGoodsBatchTransitServiceImpl extends BaseServiceImpl<FlowGoodsBatchTransitMapper, FlowGoodsBatchTransitDO> implements FlowGoodsBatchTransitService {

//    @Override
//    public List<FlowGoodsBatchTransitDTO> listByMonthAndCrmIdAndGoodsCodeList(String month, Long crmId, List<Long> goodsCodeList, Integer goodsBatcgType) {
//        Assert.notBlank(month, "参数 month 不能为空");
//        Assert.notNull(crmId, "参数 crmId 不能为空");
//        Assert.notEmpty(goodsCodeList, "参数 goodsCodeList 不能为空");
//        LambdaQueryWrapper<FlowGoodsBatchTransitDO> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(FlowGoodsBatchTransitDO::getGbDetailMonth, month);
//        queryWrapper.eq(FlowGoodsBatchTransitDO::getCrmEnterpriseId, crmId);
//        queryWrapper.eq(FlowGoodsBatchTransitDO::getGoodsBatchType, goodsBatcgType);
//        queryWrapper.in(FlowGoodsBatchTransitDO::getCrmGoodsCode, goodsCodeList);
//        List<FlowGoodsBatchTransitDO> list = this.list(queryWrapper);
//        if (CollUtil.isEmpty(list)) {
//            return ListUtil.empty();
//        }
//        return PojoUtils.map(list, FlowGoodsBatchTransitDTO.class);
//    }

    @Override
    public Page<FlowGoodsBatchTransitDO> listPage(QueryFlowGoodsBatchTransitPageRequest request) {
        Page<FlowGoodsBatchTransitDO> page = new Page<>(request.getCurrent(), request.getSize());
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


    @Override
    public List<FlowGoodsBatchTransitDO> listByEnterpriseAndSupplyIdAndGoodsCode(Integer goodsBatchType, List<Long> crmEnterpriseIdList, List<Long> crmSupplyIdList, List<Long> crmGoodsCodeList) {
        Assert.notNull(goodsBatchType, "参数 goodsBatchType 不能为空");
        Assert.notEmpty(crmEnterpriseIdList, "参数 crmEnterpriseIdList 不能为空");
        Assert.notEmpty(crmGoodsCodeList, "参数 crmGoodsCodeList 不能为空");
        LambdaQueryWrapper<FlowGoodsBatchTransitDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getCrmEnterpriseId, crmEnterpriseIdList);
        if (CollUtil.isNotEmpty(crmSupplyIdList)) {
            lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getSupplyCrmEnterpriseId, crmSupplyIdList);
        }
        lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getCrmGoodsCode, crmGoodsCodeList);
        List<FlowGoodsBatchTransitDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public Boolean batchSave(List<SaveFlowGoodsBatchTransitRequest> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        list.forEach(o -> {
            if (ObjectUtil.isNull(o.getGoodsBatchType()) || o.getGoodsBatchType().intValue() == 0){
                throw new ServiceException("参数 goodsBatchType 不能为空、不能为0");
            }
        });

        List<FlowGoodsBatchTransitDO> saveList = PojoUtils.map(list, FlowGoodsBatchTransitDO.class);
        return this.saveBatch(saveList, 1000);
    }

    @Override
    public Boolean batchUpdate(List<UpdateFlowGoodsBatchTransitRequest> list) {
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        list = list.stream().filter(o -> ObjectUtil.isNotNull(o.getId()) && o.getId() > 0).distinct().collect(Collectors.toList());
        List<FlowGoodsBatchTransitDO> updateList = PojoUtils.map(list, FlowGoodsBatchTransitDO.class);
        return this.updateBatchById(updateList, 1000);
    }

    @Override
    public FlowGoodsBatchTransitDO getById(Long id) {
        if (ObjectUtil.isNull(id) || 0 == id.intValue()) {
            return null;
        }
        LambdaQueryWrapper<FlowGoodsBatchTransitDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getId, id);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public int deleteById(Long id, Long currentUserId) {
        if (ObjectUtil.isNull(id) || 0 == id.intValue()) {
            return 0;
        }
        FlowGoodsBatchTransitDO entity = new FlowGoodsBatchTransitDO();
        entity.setId(id);
        entity.setUpdateUser(currentUserId);
        entity.setUpdateTime(new Date());
        return this.deleteByIdWithFill(entity);
    }

    private LambdaQueryWrapper<FlowGoodsBatchTransitDO> getListPageQueryWrapper(QueryFlowGoodsBatchTransitPageRequest request) {
        LambdaQueryWrapper<FlowGoodsBatchTransitDO> lambdaQueryWrapper = new LambdaQueryWrapper();

        // 所属月份
        String gbDetailMonth = request.getGbDetailMonth();
        if (StrUtil.isNotBlank(gbDetailMonth)) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getGbDetailMonth, gbDetailMonth);
        }

        // 搜索经销商编码
        Long crmEnterpriseId = request.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmEnterpriseId) && 0 != crmEnterpriseId.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getCrmEnterpriseId, crmEnterpriseId);
        }
        // 数据权限经销商编码、所属省份编码
        List<String> crmProvinceCodeList = request.getCrmProvinceCodeList();
        List<Long> crmEnterpriseIdList = request.getCrmEnterpriseIdList();
        boolean crmIdListFlag = CollUtil.isNotEmpty(crmEnterpriseIdList) ? true : false;
        boolean crmProvinceCodeListFlag = CollUtil.isNotEmpty(crmProvinceCodeList) ? true : false;
        if (crmIdListFlag && crmProvinceCodeListFlag){
            lambdaQueryWrapper.and((wrapper) -> {
                wrapper.in(FlowGoodsBatchTransitDO::getCrmEnterpriseId, crmEnterpriseIdList)
                        .or()
                        .in(FlowGoodsBatchTransitDO::getCrmProvinceCode, crmProvinceCodeList);
            });
        } else if (crmIdListFlag){
            lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getCrmEnterpriseId, crmEnterpriseIdList);
        } else if (crmProvinceCodeListFlag){
            lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getCrmProvinceCode, crmProvinceCodeList);
        }

        // 采购渠道机构编码
        Long supplyCrmEnterpriseId = request.getSupplyCrmEnterpriseId();
        if (ObjectUtil.isNotNull(supplyCrmEnterpriseId) && 0 != supplyCrmEnterpriseId.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getSupplyCrmEnterpriseId, supplyCrmEnterpriseId);
        }

        // 商品编码
        Long crmGoodsCode = request.getCrmGoodsCode();
        List<Long> crmGoodsCodeList = request.getCrmGoodsCodeList();
        if (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue() && CollUtil.isNotEmpty(crmGoodsCodeList)) {
            if (crmGoodsCodeList.contains(crmGoodsCode)){
                lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getCrmGoodsCode, crmGoodsCode);
            } else {
                lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getCrmGoodsCode, -1L);
            }
        } else if (ObjectUtil.isNotNull(crmGoodsCode) && 0 != crmGoodsCode.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getCrmGoodsCode, crmGoodsCode);
        } else if (CollUtil.isNotEmpty(crmGoodsCodeList)){
            lambdaQueryWrapper.in(FlowGoodsBatchTransitDO::getCrmGoodsCode, crmGoodsCodeList);
        }

        // 商品批次号
        String gbBatchNo = request.getGbBatchNo();
        if (StrUtil.isNotBlank(gbBatchNo)) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getGbBatchNo, gbBatchNo);
        }

        // 库存类型：1-在途订单库存 2-终端库存
        Integer goodsBatchType = request.getGoodsBatchType();
        if (ObjectUtil.isNotNull(goodsBatchType) && 0 != goodsBatchType.intValue()) {
            lambdaQueryWrapper.eq(FlowGoodsBatchTransitDO::getGoodsBatchType, goodsBatchType);
        }

        // 操作时间：
        // 1、更新时间 = '1970-01-01 00:00:00' and 创建时间 >= ${start} and 创建时间 <= ${end}
        // 2、更新时间 > '1970-01-01 00:00:00' and 更新时间 >= ${start} and 更新时间 <= ${end}
        if (ObjectUtil.isNotNull(request.getOpTimeStart()) && ObjectUtil.isNotNull(request.getOpTimeEnd())) {
            Date opTimeStart = DateUtil.beginOfDay(request.getOpTimeStart());
            Date opTimeEnd = DateUtil.endOfDay(request.getOpTimeEnd());
            lambdaQueryWrapper.and((wrapper) -> {
                wrapper.eq(FlowGoodsBatchTransitDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(FlowGoodsBatchTransitDO::getCreateTime, opTimeStart).le(FlowGoodsBatchTransitDO::getCreateTime, opTimeEnd)
                        .or()
                        .gt(FlowGoodsBatchTransitDO::getUpdateTime, DateUtil.parse("1970-01-01 00:00:00"))
                        .ge(FlowGoodsBatchTransitDO::getUpdateTime, opTimeStart).le(FlowGoodsBatchTransitDO::getUpdateTime, opTimeEnd);
            });
        }

        lambdaQueryWrapper.orderByDesc(FlowGoodsBatchTransitDO::getCreateTime);
        return lambdaQueryWrapper;
    }

}
