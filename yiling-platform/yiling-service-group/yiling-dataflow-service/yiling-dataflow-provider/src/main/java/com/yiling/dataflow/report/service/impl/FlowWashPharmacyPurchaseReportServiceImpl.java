package com.yiling.dataflow.report.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dao.FlowWashPharmacyPurchaseReportMapper;
import com.yiling.dataflow.report.dto.FlowWashPharmacyPurchaseReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashPharmacyPurchaseReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashPharmacyPurchaseReportDO;
import com.yiling.dataflow.report.service.FlowWashPharmacyPurchaseReportService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 零售购进报表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
@Service
public class FlowWashPharmacyPurchaseReportServiceImpl extends BaseServiceImpl<FlowWashPharmacyPurchaseReportMapper, FlowWashPharmacyPurchaseReportDO> implements FlowWashPharmacyPurchaseReportService {


    private void setPermissionWrapper(FlowWashPharmacyPurchaseReportPageRequest request,QueryWrapper<FlowWashPharmacyPurchaseReportDO> queryWrapper) {

        if (CollectionUtils.isEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {

            queryWrapper.lambda().in(FlowWashPharmacyPurchaseReportDO::getCrmId, request.getCrmIdList());
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getProvinceNameList()) && CollectionUtil.isEmpty(request.getCrmIdList())) {

            queryWrapper.lambda().in(FlowWashPharmacyPurchaseReportDO::getProvinceName, request.getProvinceNameList());
            return;
        }

        queryWrapper.lambda().or(z -> z.in(FlowWashPharmacyPurchaseReportDO::getCrmId, request.getCrmIdList()).in(FlowWashPharmacyPurchaseReportDO::getProvinceName,request.getProvinceNameList()));
    }


    @Override
    public Page<FlowWashPharmacyPurchaseReportDTO> pageList(FlowWashPharmacyPurchaseReportPageRequest request) {

        QueryWrapper<FlowWashPharmacyPurchaseReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getSoMonth, request.getSoMonth());

        // 设置权限
        setPermissionWrapper(request,queryWrapper);

        if (request.getCrmId() != null && request.getCrmId()  != 0 ) {
            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getCrmId, request.getCrmId());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().likeRight(FlowWashPharmacyPurchaseReportDO::getName, request.getName());
        }

        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getGoodsCode, request.getGoodsCode());
        }

        if (StringUtils.isNotBlank(request.getProvinceName())) {
            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getProvinceName, request.getProvinceName());
        }

        if (StringUtils.isNotBlank(request.getCityName())) {
            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getCityName, request.getCityName());
        }

        if (StringUtils.isNotBlank(request.getRegionName())) {
            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getRegionName, request.getRegionName());
        }

        if (StringUtils.isNotBlank(request.getBusinessProvince())) {
            queryWrapper.lambda().likeRight(FlowWashPharmacyPurchaseReportDO::getBusinessProvince, request.getBusinessProvince());
        }

        if (StringUtils.isNotBlank(request.getBusinessDepartment())) {
            queryWrapper.lambda().likeRight(FlowWashPharmacyPurchaseReportDO::getBusinessDepartment, request.getBusinessDepartment());
        }

        if (request.getPharmacyLevel() != null && request.getPharmacyLevel() != 0) {

            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getPharmacyLevel, request.getPharmacyLevel());
        }
        if (request.getPharmacyAttribute() != null && request.getPharmacyAttribute() != 0) {

            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getPharmacyAttribute, request.getPharmacyAttribute());
        }

        if (request.getPharmacyType() != null && request.getPharmacyType() != 0) {

            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getPharmacyType, request.getPharmacyType());
        }

        queryWrapper.lambda().orderByDesc(FlowWashPharmacyPurchaseReportDO::getId);

        Page<FlowWashPharmacyPurchaseReportDO> reportFlowDOList = this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        Page<FlowWashPharmacyPurchaseReportDTO> reportPage = PojoUtils.map(reportFlowDOList, FlowWashPharmacyPurchaseReportDTO.class);

        if (reportPage == null || reportPage.getSize() == 0) {

            return new Page<>(request.getCurrent(),request.getSize(),reportPage.getTotal());
        }

        reportPage.getRecords().stream().forEach(t -> setRegionName(t));

        return reportPage;

    }

    private void setRegionName(FlowWashPharmacyPurchaseReportDTO t) {

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

        QueryWrapper<FlowWashPharmacyPurchaseReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getSoMonth, request.getSoMonth());

        if (request.getCrmId() != null) {

            queryWrapper.lambda().eq(FlowWashPharmacyPurchaseReportDO::getCrmId, request.getCrmId());
        }

        return this.remove(queryWrapper);
    }
}
