package com.yiling.dataflow.report.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dao.FlowWashSupplyStockReportMapper;
import com.yiling.dataflow.report.dto.FlowWashSupplyStockReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSupplyStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashSupplyStockReportDO;
import com.yiling.dataflow.report.service.FlowWashSupplyStockReportService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 商业进销存报表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
@Service
public class FlowWashSupplyStockReportServiceImpl extends BaseServiceImpl<FlowWashSupplyStockReportMapper, FlowWashSupplyStockReportDO> implements FlowWashSupplyStockReportService {

    private void setPermissionWrapper(FlowWashSupplyStockReportPageRequest request,QueryWrapper<FlowWashSupplyStockReportDO> queryWrapper) {

        if (CollectionUtils.isEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {
            queryWrapper.lambda().in(FlowWashSupplyStockReportDO::getCrmId, request.getCrmIdList());
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getProvinceNameList()) && CollectionUtil.isEmpty(request.getCrmIdList())) {
            queryWrapper.lambda().in(FlowWashSupplyStockReportDO::getProvinceName, request.getProvinceNameList());
            return;
        }

        queryWrapper.lambda().or(z -> z.in(FlowWashSupplyStockReportDO::getCrmId, request.getCrmIdList()).in(FlowWashSupplyStockReportDO::getProvinceName,request.getProvinceNameList()));
    }


    @Override
    public Page<FlowWashSupplyStockReportDTO> pageList(FlowWashSupplyStockReportPageRequest request) {
        QueryWrapper<FlowWashSupplyStockReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getSoMonth, request.getSoMonth());

        // 设置权限
        setPermissionWrapper(request,queryWrapper);

        if (request.getCrmId() != null && request.getCrmId()  != 0 ) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getCrmId, request.getCrmId());
        }

        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().likeRight(FlowWashSupplyStockReportDO::getName, request.getName());
        }

        if (request.getGoodsCode()  != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getGoodsCode, request.getGoodsCode());
        }

        if (CollectionUtil.isNotEmpty(request.getGoodsCodeList())) {
            queryWrapper.lambda().in(FlowWashSupplyStockReportDO::getGoodsCode, request.getGoodsCodeList());
        }

        if (StringUtils.isNotBlank(request.getProvinceName())) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getProvinceName, request.getProvinceName());
        }

        if (StringUtils.isNotBlank(request.getCityName())) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getCityName, request.getCityName());
        }

        if (StringUtils.isNotBlank(request.getRegionName())) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getRegionName, request.getRegionName());
        }

        if (StringUtils.isNotBlank(request.getBusinessProvince())) {
            queryWrapper.lambda().likeRight(FlowWashSupplyStockReportDO::getBusinessProvince, request.getBusinessProvince());
        }

        if (StringUtils.isNotBlank(request.getBusinessDepartment())) {
            queryWrapper.lambda().likeRight(FlowWashSupplyStockReportDO::getBusinessDepartment, request.getBusinessDepartment());
        }

        if (request.getSupplierLevel() != null && request.getSupplierLevel() != 0) {

            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getSupplierLevel, request.getSupplierLevel());
        }

        if (request.getSupplierAttribute() != null && request.getSupplierAttribute() != 0) {

            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getSupplierAttribute, request.getSupplierAttribute());
        }

        if (request.getHeadChainFlag() != null && request.getHeadChainFlag() != 0) {

            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getHeadChainFlag, request.getHeadChainFlag());
        }

        if (request.getHideFlag() != null && request.getHideFlag()  != 0) {
            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getHideFlag, request.getHideFlag());
        }

        queryWrapper.lambda().orderByDesc(FlowWashSupplyStockReportDO::getId);

        Page<FlowWashSupplyStockReportDO> reportFlowDOList = this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        Page<FlowWashSupplyStockReportDTO> reportPage = PojoUtils.map(reportFlowDOList, FlowWashSupplyStockReportDTO.class);

        if (reportPage == null || reportPage.getSize() == 0) {

            return new Page<>(request.getCurrent(),request.getSize(),reportPage.getTotal());
        }


        reportPage.getRecords().stream().forEach(t -> setRegionName(t));

        return reportPage;

    }


    private void setRegionName(FlowWashSupplyStockReportDTO t) {

        StringBuilder regionName = new StringBuilder();

        if (StringUtils.isNotBlank(t.getProvinceName())) {
            regionName.append(t.getProvinceName());
        }

        if (StringUtils.isNotBlank(t.getCityName())) {
            if (StringUtils.isNotBlank(regionName)) {
                regionName.append(Constants.SEPARATOR_MIDDLELINE);
            }
            regionName.append(t.getCityName());
        }

        if (StringUtils.isNotBlank(t.getRegionName())) {

            if (StringUtils.isNotBlank(regionName)) {
                regionName.append(Constants.SEPARATOR_MIDDLELINE);
            }
            regionName.append(t.getRegionName());
        }

        t.setRegionName(regionName.toString());

        if (ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(t.getLockTime(), "yyyy-MM-dd HH:mm:ss"))) {
            t.setLockTime(null);
        }

        if (ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(t.getLastUnLockTime(), "yyyy-MM-dd HH:mm:ss"))) {
            t.setLastUnLockTime(null);
        }
    }


    @Override
    public boolean removeByCrmId(RemoveFlowWashStockReportRequest request) {

        QueryWrapper<FlowWashSupplyStockReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getSoMonth, request.getSoMonth());

        if (request.getCrmId() != null) {

            queryWrapper.lambda().eq(FlowWashSupplyStockReportDO::getCrmId, request.getCrmId());
        }

        return this.remove(queryWrapper);
    }
}
