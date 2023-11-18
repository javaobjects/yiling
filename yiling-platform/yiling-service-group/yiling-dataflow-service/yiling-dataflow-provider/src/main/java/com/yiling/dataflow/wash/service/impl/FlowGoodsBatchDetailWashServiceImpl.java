package com.yiling.dataflow.wash.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.wash.dao.FlowGoodsBatchDetailWashMapper;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.handler.FlowGoodsBatchDetailWashHandler;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Slf4j
@Service
public class FlowGoodsBatchDetailWashServiceImpl extends BaseServiceImpl<FlowGoodsBatchDetailWashMapper, FlowGoodsBatchDetailWashDO> implements FlowGoodsBatchDetailWashService {

    @Autowired
    private FlowGoodsBatchDetailWashHandler flowGoodsBatchDetailWashHandler;


//    @Autowired
//    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<FlowGoodsBatchDetailWashDO> listPage(QueryFlowGoodsBatchDetailWashPageRequest request) {
        if (request.getMappingStatus() != null) {
            if (WashMappingStatusEnum.GOODS_MISMATCH.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(0);
            }
            if (WashMappingStatusEnum.MATCH_SUCCESS.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(1);
            }
        }
        Page<FlowGoodsBatchDetailWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.listPage(request, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<FlowGoodsBatchDetailWashDO> flowGoodsBatchDetailWashDOList) {
        // 分页截取并插入
        if (CollUtil.isEmpty(flowGoodsBatchDetailWashDOList)) {
            return;
        }

        handleFlowGoodsBatchDetailWashDOList(flowGoodsBatchDetailWashDOList);
        int total = flowGoodsBatchDetailWashDOList.size();
        int pageCount;
        int size = 1000;

        if (total % size == 0) {
            pageCount = total / size;
        } else {
            pageCount = total / size + 1;
        }

        int fromIndex = 0;      // 开始索引
        int toIndex = 0;        // 结束索引
        for (int i = 0; i < pageCount; i++) {
            fromIndex = i * size;
            toIndex = (i + 1) * size;
            if (toIndex > total) {
                toIndex = total;
            }
            baseMapper.insertBatchSomeColumn(flowGoodsBatchDetailWashDOList.subList(fromIndex, toIndex));
        }
    }

    @Override
    public List<FlowGoodsBatchDetailWashDO> getByFmwtId(Long fmwtId) {
        LambdaQueryWrapper<FlowGoodsBatchDetailWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDetailWashDO::getFmwtId, fmwtId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<FlowGoodsBatchDetailWashDTO> getByYearMonth(QueryFlowGoodsBatchDetailWashListRequest request) {
        return PojoUtils.map(baseMapper.list(request), FlowGoodsBatchDetailWashDTO.class);
    }

    @Override
    public Page<FlowGoodsBatchDetailWashDTO> getPageByYearMonth(QueryFlowGoodsBatchDetailWashPageRequest request) {
        Page<FlowGoodsBatchDetailWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageByYearMonth(request, page), FlowGoodsBatchDetailWashDTO.class);
    }

    @Override
    public void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request) {
        FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO = baseMapper.selectById(request.getId());
        flowGoodsBatchDetailWashDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowGoodsBatchDetailWashDO.setCrmGoodsName(request.getCrmGoodsName());
        flowGoodsBatchDetailWashDO.setCrmGoodsSpecifications(request.getCrmGoodsSpecifications());
        flowGoodsBatchDetailWashDO.setConversionQuantity(request.getConversionQuantity());
        // 重设对照状态
        flowGoodsBatchDetailWashHandler.setMappingStatus(flowGoodsBatchDetailWashDO);

        if (request.getCrmGoodsCode() == null || request.getCrmGoodsCode() == 0) {
            flowGoodsBatchDetailWashDO.setEnterpriseCersId(0L);
        } else {
            try {
                Long enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowGoodsBatchDetailWashDO.getCrmGoodsCode(), flowGoodsBatchDetailWashDO.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowGoodsBatchDetailWashDO.getYear(), flowGoodsBatchDetailWashDO.getMonth()));
                flowGoodsBatchDetailWashDO.setEnterpriseCersId(enterpriseCersId);
            } catch (Exception e) {
                log.error("月流向商品对照修改，flowGoodsBatchDetailWashDO.id={}，三者关系查询失败，year={}, month={}", request.getId(), flowGoodsBatchDetailWashDO.getYear(), flowGoodsBatchDetailWashDO.getMonth(), e);
            }
        }
        baseMapper.updateById(flowGoodsBatchDetailWashDO);
    }

    @Override
    public Integer goodsUnMappingStatusCount(Long fmwtId) {
        LambdaQueryWrapper<FlowGoodsBatchDetailWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowGoodsBatchDetailWashDO::getFmwtId, fmwtId);
        wrapper.eq(FlowGoodsBatchDetailWashDO::getGoodsMappingStatus, 0);
        wrapper.in(FlowGoodsBatchDetailWashDO::getWashStatus, Arrays.asList(FlowDataWashStatusEnum.NORMAL.getCode(), FlowDataWashStatusEnum.DUPLICATE.getCode()));
        return baseMapper.selectCount(wrapper);
    }


    private void handleFlowGoodsBatchDetailWashDOList(List<FlowGoodsBatchDetailWashDO> flowGoodsBatchDetailWashDOList) {
        for (FlowGoodsBatchDetailWashDO flowGoodsBatchDetailWashDO : flowGoodsBatchDetailWashDOList) {
            flowGoodsBatchDetailWashDO.setId(null);
            flowGoodsBatchDetailWashDO.setCrmEnterpriseId(flowGoodsBatchDetailWashDO.getCrmEnterpriseId() != null ? flowGoodsBatchDetailWashDO.getCrmEnterpriseId() : 0);
            flowGoodsBatchDetailWashDO.setEid(flowGoodsBatchDetailWashDO.getEid() != null ? flowGoodsBatchDetailWashDO.getEid() : 0);
            flowGoodsBatchDetailWashDO.setGbIdNo(flowGoodsBatchDetailWashDO.getGbIdNo() != null ? flowGoodsBatchDetailWashDO.getGbIdNo() : "");
            flowGoodsBatchDetailWashDO.setGbDetailMonth(flowGoodsBatchDetailWashDO.getGbDetailMonth() != null ? flowGoodsBatchDetailWashDO.getGbDetailMonth() : DateUtil.format(flowGoodsBatchDetailWashDO.getGbDetailTime(), "yyyy-MM"));
            flowGoodsBatchDetailWashDO.setInSn(flowGoodsBatchDetailWashDO.getInSn() != null ? flowGoodsBatchDetailWashDO.getInSn() : "");
            flowGoodsBatchDetailWashDO.setGbProduceTime(flowGoodsBatchDetailWashDO.getGbProduceTime() != null ? flowGoodsBatchDetailWashDO.getGbProduceTime() : "");
            flowGoodsBatchDetailWashDO.setGbEndTime(flowGoodsBatchDetailWashDO.getGbEndTime() != null ? flowGoodsBatchDetailWashDO.getGbEndTime() : "");
            flowGoodsBatchDetailWashDO.setGbLicense(flowGoodsBatchDetailWashDO.getGbLicense() != null ? flowGoodsBatchDetailWashDO.getGbLicense() : "");
            flowGoodsBatchDetailWashDO.setGbSource(flowGoodsBatchDetailWashDO.getGbSource() != null ? flowGoodsBatchDetailWashDO.getGbSource() : "");
            flowGoodsBatchDetailWashDO.setProvinceCode(flowGoodsBatchDetailWashDO.getProvinceCode() != null ? flowGoodsBatchDetailWashDO.getProvinceCode() : "");
            flowGoodsBatchDetailWashDO.setProvinceName(flowGoodsBatchDetailWashDO.getProvinceName() != null ? flowGoodsBatchDetailWashDO.getProvinceName() : "");
            flowGoodsBatchDetailWashDO.setCityCode(flowGoodsBatchDetailWashDO.getCityCode() != null ? flowGoodsBatchDetailWashDO.getCityCode() : "");
            flowGoodsBatchDetailWashDO.setCityName(flowGoodsBatchDetailWashDO.getCityName() != null ? flowGoodsBatchDetailWashDO.getCityName() : "");
            flowGoodsBatchDetailWashDO.setRegionCode(flowGoodsBatchDetailWashDO.getRegionCode() != null ? flowGoodsBatchDetailWashDO.getRegionCode() : "");
            flowGoodsBatchDetailWashDO.setRegionName(flowGoodsBatchDetailWashDO.getRegionName() != null ? flowGoodsBatchDetailWashDO.getRegionName() : "");
            flowGoodsBatchDetailWashDO.setCrmGoodsCode(flowGoodsBatchDetailWashDO.getCrmGoodsCode() != null ? flowGoodsBatchDetailWashDO.getCrmGoodsCode() : 0);
            flowGoodsBatchDetailWashDO.setCrmGoodsName(flowGoodsBatchDetailWashDO.getCrmGoodsName() != null ? flowGoodsBatchDetailWashDO.getCrmGoodsName() : "");
            flowGoodsBatchDetailWashDO.setCrmGoodsSpecifications(flowGoodsBatchDetailWashDO.getCrmGoodsSpecifications() != null ? flowGoodsBatchDetailWashDO.getCrmGoodsSpecifications() : "");
            flowGoodsBatchDetailWashDO.setConversionQuantity(flowGoodsBatchDetailWashDO.getConversionQuantity() != null ? flowGoodsBatchDetailWashDO.getConversionQuantity() : new BigDecimal(0));
            flowGoodsBatchDetailWashDO.setCollectTime(flowGoodsBatchDetailWashDO.getCollectTime() != null ? flowGoodsBatchDetailWashDO.getCollectTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowGoodsBatchDetailWashDO.setEnterpriseCersId(flowGoodsBatchDetailWashDO.getEnterpriseCersId() != null ? flowGoodsBatchDetailWashDO.getEnterpriseCersId() : 0);
            flowGoodsBatchDetailWashDO.setGoodsMappingStatus(flowGoodsBatchDetailWashDO.getGoodsMappingStatus() != null ? flowGoodsBatchDetailWashDO.getGoodsMappingStatus() : 0);
            flowGoodsBatchDetailWashDO.setErrorFlag(flowGoodsBatchDetailWashDO.getErrorFlag() != null ? flowGoodsBatchDetailWashDO.getErrorFlag() : 0);
            flowGoodsBatchDetailWashDO.setErrorMsg(flowGoodsBatchDetailWashDO.getErrorMsg() != null ? flowGoodsBatchDetailWashDO.getErrorMsg() : "");
            flowGoodsBatchDetailWashDO.setDelFlag(flowGoodsBatchDetailWashDO.getDelFlag() != null ? flowGoodsBatchDetailWashDO.getDelFlag() : 0);
        }
    }
}
