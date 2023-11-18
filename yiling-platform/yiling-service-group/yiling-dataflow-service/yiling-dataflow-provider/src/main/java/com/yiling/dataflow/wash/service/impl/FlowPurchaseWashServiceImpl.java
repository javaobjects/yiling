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
import com.yiling.dataflow.wash.dao.FlowPurchaseWashMapper;
import com.yiling.dataflow.wash.dto.FlowPurchaseWashDTO;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmOrganizationInfoRequest;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.handler.FlowPurchaseWashHandler;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Slf4j
@Service
public class FlowPurchaseWashServiceImpl extends BaseServiceImpl<FlowPurchaseWashMapper, FlowPurchaseWashDO> implements FlowPurchaseWashService {

    @Autowired
    private FlowPurchaseWashHandler flowPurchaseWashHandler;


//    @Autowired
//    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Override
    public Page<FlowPurchaseWashDO> listPage(QueryFlowPurchaseWashPageRequest request) {
        if (request.getMappingStatus() != null) {
            if (WashMappingStatusEnum.GOODS_MISMATCH.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(0);
            }
            if (WashMappingStatusEnum.MATCH_SUCCESS.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(1);
            }
        }
        Page<FlowPurchaseWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.listPage(request, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<FlowPurchaseWashDO> flowPurchaseWashDOList) {
        // 分页截取并插入
        if (CollUtil.isEmpty(flowPurchaseWashDOList)) {
            return;
        }
        handleFlowPurchaseWashDOList(flowPurchaseWashDOList);
        int total = flowPurchaseWashDOList.size();
        int pageCount;
        int size = 1000;

        if (total % size == 0) {
            pageCount = total / size;
        } else {
            pageCount = total / size + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引
        for (int i = 0; i < pageCount; i++) {
            fromIndex = i * size;
            toIndex = (i + 1) * size;
            if (toIndex > total) {
                toIndex = total;
            }
            baseMapper.insertBatchSomeColumn(flowPurchaseWashDOList.subList(fromIndex, toIndex));
        }
    }

    @Override
    public List<FlowPurchaseWashDO> getByFmwtId(Long fmwtId) {
        LambdaQueryWrapper<FlowPurchaseWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseWashDO::getFmwtId, fmwtId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<FlowPurchaseWashDTO> getByYearMonth(QueryFlowPurchaseWashListRequest request) {
        return PojoUtils.map(baseMapper.list(request), FlowPurchaseWashDTO.class);
    }

    @Override
    public Page<FlowPurchaseWashDTO> getPageByYearMonth(QueryFlowPurchaseWashPageRequest request) {
        Page<FlowPurchaseWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageByYearMonth(request, page), FlowPurchaseWashDTO.class);
    }

    @Override
    public void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request) {
        FlowPurchaseWashDO flowPurchaseWashDO = baseMapper.selectById(request.getId());
        flowPurchaseWashDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowPurchaseWashDO.setCrmGoodsName(request.getCrmGoodsName());
        flowPurchaseWashDO.setCrmGoodsSpecifications(request.getCrmGoodsSpecifications());
        flowPurchaseWashDO.setConversionQuantity(request.getConversionQuantity());
        // 重设对照状态
        flowPurchaseWashHandler.setMappingStatus(flowPurchaseWashDO);

        if (request.getCrmGoodsCode() == null || request.getCrmGoodsCode() == 0) {
            flowPurchaseWashDO.setEnterpriseCersId(0L);
        } else {
            try {
                Long enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowPurchaseWashDO.getCrmGoodsCode(), flowPurchaseWashDO.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowPurchaseWashDO.getYear(), flowPurchaseWashDO.getMonth()));
                flowPurchaseWashDO.setEnterpriseCersId(enterpriseCersId);
            } catch (Exception e) {
                log.error("月流向商品对照修改，flowPurchaseWashDO.id={}，三者关系查询失败，year={}, month={}", request.getId(), flowPurchaseWashDO.getYear(), flowPurchaseWashDO.getMonth(), e);
            }
        }
        baseMapper.updateById(flowPurchaseWashDO);
    }

    @Override
    public void updateCrmOrganizationInfo(UpdateCrmOrganizationInfoRequest request) {
        FlowPurchaseWashDO flowPurchaseWashDO = baseMapper.selectById(request.getId());
        flowPurchaseWashDO.setCrmOrganizationId(request.getCrmOrganizationId());
        flowPurchaseWashDO.setCrmOrganizationName(request.getCrmOrganizationName());
        // 重设对照状态
        flowPurchaseWashHandler.setMappingStatus(flowPurchaseWashDO);

        baseMapper.updateById(flowPurchaseWashDO);
    }

    @Override
    public Integer organizationUnMappingStatusCount(Long fmwtId) {
        LambdaQueryWrapper<FlowPurchaseWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseWashDO::getFmwtId, fmwtId);
        wrapper.eq(FlowPurchaseWashDO::getOrganizationMappingStatus, 0);
        wrapper.in(FlowPurchaseWashDO::getWashStatus, Arrays.asList(FlowDataWashStatusEnum.NORMAL.getCode(), FlowDataWashStatusEnum.DUPLICATE.getCode()));
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public Integer goodsUnMappingStatusCount(Long fmwtId) {
        LambdaQueryWrapper<FlowPurchaseWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowPurchaseWashDO::getFmwtId, fmwtId);
        wrapper.eq(FlowPurchaseWashDO::getGoodsMappingStatus, 0);
        wrapper.in(FlowPurchaseWashDO::getWashStatus, Arrays.asList(FlowDataWashStatusEnum.NORMAL.getCode(), FlowDataWashStatusEnum.DUPLICATE.getCode()));
        return baseMapper.selectCount(wrapper);
    }

    private void handleFlowPurchaseWashDOList(List<FlowPurchaseWashDO> flowPurchaseWashDOList) {
        for (FlowPurchaseWashDO flowPurchaseWashDO : flowPurchaseWashDOList) {
            flowPurchaseWashDO.setId(null);
            flowPurchaseWashDO.setCrmEnterpriseId(flowPurchaseWashDO.getCrmEnterpriseId() != null ? flowPurchaseWashDO.getCrmEnterpriseId() : 0);
            flowPurchaseWashDO.setEid(flowPurchaseWashDO.getEid() != null ? flowPurchaseWashDO.getEid() : 0);
            flowPurchaseWashDO.setPoId(flowPurchaseWashDO.getPoId() != null ? flowPurchaseWashDO.getPoId() : "");
            flowPurchaseWashDO.setPoNo(flowPurchaseWashDO.getPoNo() != null ? flowPurchaseWashDO.getPoNo() : "");
            flowPurchaseWashDO.setEnterpriseInnerCode(flowPurchaseWashDO.getEnterpriseInnerCode() != null ? flowPurchaseWashDO.getEnterpriseInnerCode() : "");
            flowPurchaseWashDO.setCrmOrganizationId(Convert.toLong(flowPurchaseWashDO.getCrmOrganizationId(), 0L));
            flowPurchaseWashDO.setCrmOrganizationName(Convert.toStr(flowPurchaseWashDO.getCrmOrganizationName(), ""));
            flowPurchaseWashDO.setPoMonth(flowPurchaseWashDO.getPoMonth() != null ? flowPurchaseWashDO.getPoMonth() : DateUtil.format(flowPurchaseWashDO.getPoTime(), "yyyy-MM"));
            flowPurchaseWashDO.setOrderTime(flowPurchaseWashDO.getOrderTime() != null ? flowPurchaseWashDO.getOrderTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowPurchaseWashDO.setGoodsInSn(flowPurchaseWashDO.getGoodsInSn() != null ? flowPurchaseWashDO.getGoodsInSn() : "");
            flowPurchaseWashDO.setPoProductTime(flowPurchaseWashDO.getPoProductTime() != null ? flowPurchaseWashDO.getPoProductTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowPurchaseWashDO.setPoEffectiveTime(flowPurchaseWashDO.getPoEffectiveTime() != null ? flowPurchaseWashDO.getPoEffectiveTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowPurchaseWashDO.setPoSource(flowPurchaseWashDO.getPoSource() != null ? flowPurchaseWashDO.getPoSource() : "");
            flowPurchaseWashDO.setConversionQuantity(flowPurchaseWashDO.getConversionQuantity() != null ? flowPurchaseWashDO.getConversionQuantity() : new BigDecimal(0));
            flowPurchaseWashDO.setPoLicense(flowPurchaseWashDO.getPoLicense() != null ? flowPurchaseWashDO.getPoLicense() : "");
            flowPurchaseWashDO.setProvinceCode(flowPurchaseWashDO.getProvinceCode() != null ? flowPurchaseWashDO.getProvinceCode() : "");
            flowPurchaseWashDO.setProvinceName(flowPurchaseWashDO.getProvinceName() != null ? flowPurchaseWashDO.getProvinceName() : "");
            flowPurchaseWashDO.setCityCode(flowPurchaseWashDO.getCityCode() != null ? flowPurchaseWashDO.getCityCode() : "");
            flowPurchaseWashDO.setCityName(flowPurchaseWashDO.getCityName() != null ? flowPurchaseWashDO.getCityName() : "");
            flowPurchaseWashDO.setRegionCode(flowPurchaseWashDO.getRegionCode() != null ? flowPurchaseWashDO.getRegionCode() : "");
            flowPurchaseWashDO.setRegionName(flowPurchaseWashDO.getRegionName() != null ? flowPurchaseWashDO.getRegionName() : "");
            flowPurchaseWashDO.setCrmGoodsCode(flowPurchaseWashDO.getCrmGoodsCode() != null ? flowPurchaseWashDO.getCrmGoodsCode() : 0);
            flowPurchaseWashDO.setCrmGoodsName(flowPurchaseWashDO.getCrmGoodsName() != null ? flowPurchaseWashDO.getCrmGoodsName() : "");
            flowPurchaseWashDO.setCrmGoodsSpecifications(flowPurchaseWashDO.getCrmGoodsSpecifications() != null ? flowPurchaseWashDO.getCrmGoodsSpecifications() : "");
            flowPurchaseWashDO.setEnterpriseCersId(flowPurchaseWashDO.getEnterpriseCersId() != null ? flowPurchaseWashDO.getEnterpriseCersId() : 0);
            flowPurchaseWashDO.setGoodsMappingStatus(flowPurchaseWashDO.getGoodsMappingStatus() != null ? flowPurchaseWashDO.getGoodsMappingStatus() : 0);
            flowPurchaseWashDO.setOrganizationMappingStatus(Convert.toInt(flowPurchaseWashDO.getOrganizationMappingStatus(), 0));
            flowPurchaseWashDO.setErrorFlag(flowPurchaseWashDO.getErrorFlag() != null ? flowPurchaseWashDO.getErrorFlag() : 0);
            flowPurchaseWashDO.setErrorMsg(flowPurchaseWashDO.getErrorMsg() != null ? flowPurchaseWashDO.getErrorMsg() : "");
            flowPurchaseWashDO.setDelFlag(flowPurchaseWashDO.getDelFlag() != null ? flowPurchaseWashDO.getDelFlag() : 0);
        }
    }

}
