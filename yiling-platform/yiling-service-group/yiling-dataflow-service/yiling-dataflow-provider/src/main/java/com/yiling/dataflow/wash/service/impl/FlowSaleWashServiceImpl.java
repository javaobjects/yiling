package com.yiling.dataflow.wash.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.wash.dao.FlowSaleWashMapper;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmOrganizationInfoRequest;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.dataflow.wash.enums.FlowDataWashStatusEnum;
import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.dataflow.wash.handler.FlowSaleWashHandler;
import com.yiling.dataflow.wash.service.FlowSaleWashService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/2
 */
@Slf4j
@Service
public class FlowSaleWashServiceImpl extends BaseServiceImpl<FlowSaleWashMapper, FlowSaleWashDO> implements FlowSaleWashService {

    @Autowired
    private FlowSaleWashHandler flowSaleWashHandler;

//    @Autowired
//    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Override
    public Page<FlowSaleWashDO> listPage(QueryFlowSaleWashPageRequest request) {
        if (request.getMappingStatus() != null) {
            if (WashMappingStatusEnum.BOTH_MISMATCH.getCode().equals(request.getMappingStatus())
                    || WashMappingStatusEnum.GOODS_MISMATCH.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(0);
            }
            if (WashMappingStatusEnum.BOTH_MISMATCH.getCode().equals(request.getMappingStatus())
                    || WashMappingStatusEnum.CUSTOM_MISMATCH.getCode().equals(request.getMappingStatus())) {
                request.setOrganizationMappingStatus(0);
            }
            if (WashMappingStatusEnum.MATCH_SUCCESS.getCode().equals(request.getMappingStatus())) {
                request.setGoodsMappingStatus(1);
                request.setOrganizationMappingStatus(1);
            }
        }
        Page<FlowSaleWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return baseMapper.listPage(request, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<FlowSaleWashDO> flowSaleWashDOList) {
        // 分页截取并插入
        if (CollUtil.isEmpty(flowSaleWashDOList)) {
            return;
        }
        handleFlowSaleWashDOList(flowSaleWashDOList);

        int total = flowSaleWashDOList.size();
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
            baseMapper.insertBatchSomeColumn(flowSaleWashDOList.subList(fromIndex, toIndex));
        }
    }

    @Override
    public List<FlowSaleWashDO> getByFmwtId(Long fmwtId) {
        LambdaQueryWrapper<FlowSaleWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleWashDO::getFmwtId, fmwtId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<FlowSaleWashDTO> getByYearMonth(QueryFlowSaleWashListRequest request) {
        return PojoUtils.map(baseMapper.list(request), FlowSaleWashDTO.class);
    }

    @Override
    public Page<FlowSaleWashDTO> getPageByYearMonth(QueryFlowSaleWashPageRequest request) {
        Page<FlowSaleWashDO> page = new Page<>(request.getCurrent(), request.getSize());
        return PojoUtils.map(baseMapper.getPageByYearMonth(request, page), FlowSaleWashDTO.class);
    }

    @Override
    public void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request) {
        FlowSaleWashDO flowSaleWashDO = baseMapper.selectById(request.getId());
        flowSaleWashDO.setCrmGoodsCode(request.getCrmGoodsCode());
        flowSaleWashDO.setCrmGoodsName(request.getCrmGoodsName());
        flowSaleWashDO.setCrmGoodsSpecifications(request.getCrmGoodsSpecifications());
        flowSaleWashDO.setConversionQuantity(request.getConversionQuantity());
        // 重设对照状态
        flowSaleWashHandler.setMappingStatus(flowSaleWashDO);

        if (request.getCrmGoodsCode() == null || request.getCrmGoodsCode() == 0) {
            flowSaleWashDO.setEnterpriseCersId(0L);
        } else {
            try {
                // 经销商
                Long enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowSaleWashDO.getCrmGoodsCode(), flowSaleWashDO.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(flowSaleWashDO.getYear(), flowSaleWashDO.getMonth()));
                flowSaleWashDO.setEnterpriseCersId(enterpriseCersId);

                // 客户
                if (flowSaleWashDO.getCrmOrganizationId() > 0) {
                    Long orgCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowSaleWashDO.getCrmGoodsCode(), flowSaleWashDO.getOrganizationCersId(), BackupUtil.generateTableSuffix(flowSaleWashDO.getYear(), flowSaleWashDO.getMonth()));
                    flowSaleWashDO.setOrganizationCersId(orgCersId);
                    // 设置非锁标识
                    setFlowSaleWashOrgUnlockFlag(flowSaleWashDO, orgCersId);

                }
            } catch (Exception e) {
                log.error("月流向商品对照修改，flowSaleWashDO.id={}，三者关系查询失败，year={}, month={}", request.getId(), flowSaleWashDO.getYear(), flowSaleWashDO.getMonth(), e);
            }
        }
        baseMapper.updateById(flowSaleWashDO);
    }

    @Override
    public void updateCrmOrganizationInfo(UpdateCrmOrganizationInfoRequest request) {
        FlowSaleWashDO flowSaleWashDO = baseMapper.selectById(request.getId());
        flowSaleWashDO.setCrmOrganizationId(request.getCrmOrganizationId());
        flowSaleWashDO.setCrmOrganizationName(request.getCrmOrganizationName());
        // 重设对照状态
        flowSaleWashHandler.setMappingStatus(flowSaleWashDO);

        Long enterpriseCersId = 0L;
        if (request.getCrmOrganizationId() == null || request.getCrmOrganizationId() == 0 || flowSaleWashDO.getCrmGoodsCode() == 0) {
            flowSaleWashDO.setOrganizationCersId(0L);
        } else {
            try {
                enterpriseCersId = crmEnterpriseRelationShipService.listSuffixByOrgIdAndGoodsCode(flowSaleWashDO.getCrmGoodsCode(), flowSaleWashDO.getCrmOrganizationId(), BackupUtil.generateTableSuffix(flowSaleWashDO.getYear(), flowSaleWashDO.getMonth()));
                flowSaleWashDO.setOrganizationCersId(enterpriseCersId);

                // 设置非锁标识
                setFlowSaleWashOrgUnlockFlag(flowSaleWashDO, enterpriseCersId);
            } catch (Exception e) {
                log.error("月流向商品对照修改，flowSaleWashDO.id={}，三者关系查询失败，year={}, month={}", request.getId(), flowSaleWashDO.getYear(), flowSaleWashDO.getMonth(), e);
            }
        }
        baseMapper.updateById(flowSaleWashDO);
    }

    @Override
    public Integer goodsUnMappingStatusCount(Long fmwtId) {
        LambdaQueryWrapper<FlowSaleWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleWashDO::getFmwtId, fmwtId);
        wrapper.eq(FlowSaleWashDO::getGoodsMappingStatus, 0);
        wrapper.in(FlowSaleWashDO::getWashStatus, Arrays.asList(FlowDataWashStatusEnum.NORMAL.getCode(), FlowDataWashStatusEnum.DUPLICATE.getCode()));
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public Integer organizationUnMappingStatusCount(Long fmwtId) {
        LambdaQueryWrapper<FlowSaleWashDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowSaleWashDO::getFmwtId, fmwtId);
        wrapper.eq(FlowSaleWashDO::getOrganizationMappingStatus, 0);
        wrapper.in(FlowSaleWashDO::getWashStatus, Arrays.asList(FlowDataWashStatusEnum.NORMAL.getCode(), FlowDataWashStatusEnum.DUPLICATE.getCode()));
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public void setFlowSaleWashOrgUnlockFlag(FlowSaleWashDO flowSaleWashDO, Long enterpriseCersId) {
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(flowSaleWashDO.getCrmOrganizationId(), BackupUtil.generateTableSuffix(flowSaleWashDO.getYear(), flowSaleWashDO.getMonth()));
        if (CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode().equals(crmEnterpriseDO.getSupplyChainRole())) {     // 如果是商业公司，则为锁定流向
            if (crmEnterpriseDO.getBusinessCode() == 1) {   // 有效
                flowSaleWashDO.setUnlockFlag(0);    // 锁定流向
            } else {    // 无效
                flowSaleWashDO.setUnlockFlag(1);    // 锁定流向
            }
        } else {
            if (enterpriseCersId != null && enterpriseCersId > 0) {
                flowSaleWashDO.setUnlockFlag(0);    // 锁定流向
            } else {
                flowSaleWashDO.setUnlockFlag(1);    // 非锁流向
            }
        }
    }



    private void handleFlowSaleWashDOList(List<FlowSaleWashDO> flowSaleWashDOList) {
        for (FlowSaleWashDO flowSaleWashDO : flowSaleWashDOList) {
            flowSaleWashDO.setId(null);
            flowSaleWashDO.setCrmEnterpriseId(flowSaleWashDO.getCrmEnterpriseId() != null ? flowSaleWashDO.getCrmEnterpriseId() : 0);
            flowSaleWashDO.setFmwtId(flowSaleWashDO.getFmwtId() != null ? flowSaleWashDO.getFmwtId() : 0);
            flowSaleWashDO.setYear(flowSaleWashDO.getYear() != null ? flowSaleWashDO.getYear() : 0);
            flowSaleWashDO.setMonth(flowSaleWashDO.getMonth() != null ? flowSaleWashDO.getMonth() : 0);
            flowSaleWashDO.setEid(flowSaleWashDO.getEid() != null ? flowSaleWashDO.getEid() : 0);
            flowSaleWashDO.setName(flowSaleWashDO.getName() != null ? flowSaleWashDO.getName() : "");
            flowSaleWashDO.setSoId(flowSaleWashDO.getSoId() != null ? flowSaleWashDO.getSoId() : "");
            flowSaleWashDO.setSoNo(flowSaleWashDO.getSoNo() != null ? flowSaleWashDO.getSoNo() : "");
            flowSaleWashDO.setEnterpriseInnerCode(flowSaleWashDO.getEnterpriseInnerCode() != null ? flowSaleWashDO.getEnterpriseInnerCode() : "");
            flowSaleWashDO.setCrmOrganizationId(flowSaleWashDO.getCrmOrganizationId() != null ? flowSaleWashDO.getCrmOrganizationId() : 0);
            flowSaleWashDO.setCrmOrganizationName(flowSaleWashDO.getCrmOrganizationName() != null ? flowSaleWashDO.getCrmOrganizationName() : "");
            flowSaleWashDO.setLicenseNumber(flowSaleWashDO.getLicenseNumber() != null ? flowSaleWashDO.getLicenseNumber() : "");
            flowSaleWashDO.setSoTime(flowSaleWashDO.getSoTime() != null ? flowSaleWashDO.getSoTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowSaleWashDO.setSoMonth(flowSaleWashDO.getSoMonth() != null ? flowSaleWashDO.getSoMonth() : DateUtil.format(flowSaleWashDO.getSoTime(), "yyyy-MM"));
            flowSaleWashDO.setOrderTime(flowSaleWashDO.getOrderTime() != null ? flowSaleWashDO.getOrderTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowSaleWashDO.setGoodsInSn(flowSaleWashDO.getGoodsInSn() != null ? flowSaleWashDO.getGoodsInSn() : "");
            flowSaleWashDO.setGoodsName(flowSaleWashDO.getGoodsName() != null ? flowSaleWashDO.getGoodsName() : "");
            flowSaleWashDO.setSoSpecifications(flowSaleWashDO.getSoSpecifications() != null ? flowSaleWashDO.getSoSpecifications() : "");
            flowSaleWashDO.setSoBatchNo(flowSaleWashDO.getSoBatchNo() != null ? flowSaleWashDO.getSoBatchNo() : "");
            flowSaleWashDO.setSoProductTime(flowSaleWashDO.getSoProductTime() != null ? flowSaleWashDO.getSoProductTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowSaleWashDO.setSoEffectiveTime(flowSaleWashDO.getSoEffectiveTime() != null ? flowSaleWashDO.getSoEffectiveTime() : DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
            flowSaleWashDO.setSoQuantity(flowSaleWashDO.getSoQuantity() != null ? flowSaleWashDO.getSoQuantity() : new BigDecimal(0));
            flowSaleWashDO.setSoUnit(flowSaleWashDO.getSoUnit() != null ? flowSaleWashDO.getSoUnit() : "");
            flowSaleWashDO.setSoPrice(flowSaleWashDO.getSoPrice() != null ? flowSaleWashDO.getSoPrice() : new BigDecimal(0));
            flowSaleWashDO.setConversionQuantity(flowSaleWashDO.getConversionQuantity() != null ? flowSaleWashDO.getConversionQuantity() : new BigDecimal(0));
            flowSaleWashDO.setSoManufacturer(flowSaleWashDO.getSoManufacturer() != null ? flowSaleWashDO.getSoManufacturer() : "");
            flowSaleWashDO.setSoLicense(flowSaleWashDO.getSoLicense() != null ? flowSaleWashDO.getSoLicense() : "");
            flowSaleWashDO.setSoSource(flowSaleWashDO.getSoSource() != null ? flowSaleWashDO.getSoSource() : "");

            flowSaleWashDO.setProvinceCode(flowSaleWashDO.getProvinceCode() != null ? flowSaleWashDO.getProvinceCode() : "");
            flowSaleWashDO.setProvinceName(flowSaleWashDO.getProvinceName() != null ? flowSaleWashDO.getProvinceName() : "");
            flowSaleWashDO.setCityCode(flowSaleWashDO.getCityCode() != null ? flowSaleWashDO.getCityCode() : "");
            flowSaleWashDO.setCityName(flowSaleWashDO.getCityName() != null ? flowSaleWashDO.getCityName() : "");
            flowSaleWashDO.setRegionCode(flowSaleWashDO.getRegionCode() != null ? flowSaleWashDO.getRegionCode() : "");
            flowSaleWashDO.setRegionName(flowSaleWashDO.getRegionName() != null ? flowSaleWashDO.getRegionName() : "");
            flowSaleWashDO.setCrmGoodsCode(flowSaleWashDO.getCrmGoodsCode() != null ? flowSaleWashDO.getCrmGoodsCode() : 0);
            flowSaleWashDO.setCrmGoodsName(flowSaleWashDO.getCrmGoodsName() != null ? flowSaleWashDO.getCrmGoodsName() : "");
            flowSaleWashDO.setCrmGoodsSpecifications(flowSaleWashDO.getCrmGoodsSpecifications() != null ? flowSaleWashDO.getCrmGoodsSpecifications() : "");
            flowSaleWashDO.setEnterpriseCersId(flowSaleWashDO.getEnterpriseCersId() != null ? flowSaleWashDO.getEnterpriseCersId() : 0);
            flowSaleWashDO.setOrganizationCersId(flowSaleWashDO.getOrganizationCersId() != null ? flowSaleWashDO.getOrganizationCersId() : 0);
            flowSaleWashDO.setGoodsMappingStatus(flowSaleWashDO.getGoodsMappingStatus() != null ? flowSaleWashDO.getGoodsMappingStatus() : 0);
            flowSaleWashDO.setOrganizationMappingStatus(flowSaleWashDO.getOrganizationMappingStatus() != null ? flowSaleWashDO.getOrganizationMappingStatus() : 0);
            flowSaleWashDO.setErrorFlag(flowSaleWashDO.getErrorFlag() != null ? flowSaleWashDO.getErrorFlag() : 0);
            flowSaleWashDO.setErrorMsg(flowSaleWashDO.getErrorMsg() != null ? flowSaleWashDO.getErrorMsg() : "");
            flowSaleWashDO.setRepresentativeCode(flowSaleWashDO.getRepresentativeCode() != null ? flowSaleWashDO.getRepresentativeCode() : "");
            flowSaleWashDO.setFlowClassify(flowSaleWashDO.getFlowClassify() != null ? flowSaleWashDO.getFlowClassify() : 0);
            flowSaleWashDO.setUnlockFlag(flowSaleWashDO.getUnlockFlag() != null ? flowSaleWashDO.getUnlockFlag() : 0);
            flowSaleWashDO.setCreateType(Convert.toInt(flowSaleWashDO.getCreateType(), 0));
            flowSaleWashDO.setSourceId(Convert.toLong(flowSaleWashDO.getSourceId(), 0L));
            flowSaleWashDO.setErrorMsg(flowSaleWashDO.getErrorMsg() != null ? flowSaleWashDO.getErrorMsg() : "");
            flowSaleWashDO.setDelFlag(flowSaleWashDO.getDelFlag() != null ? flowSaleWashDO.getDelFlag() : 0);
        }
    }

}
