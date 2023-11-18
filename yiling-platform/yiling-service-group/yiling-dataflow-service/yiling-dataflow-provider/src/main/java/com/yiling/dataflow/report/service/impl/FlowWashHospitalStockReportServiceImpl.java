package com.yiling.dataflow.report.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dao.FlowWashHospitalStockReportMapper;
import com.yiling.dataflow.report.dto.FlowWashHospitalStockReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashHospitalStockReportDO;
import com.yiling.dataflow.report.service.FlowWashHospitalStockReportService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 医疗进销存报表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
@Service
public class FlowWashHospitalStockReportServiceImpl extends BaseServiceImpl<FlowWashHospitalStockReportMapper, FlowWashHospitalStockReportDO> implements FlowWashHospitalStockReportService {

    private void setPermissionWrapper(FlowWashHospitalStockReportPageRequest request,QueryWrapper<FlowWashHospitalStockReportDO> queryWrapper) {

        if (CollectionUtils.isEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {

            queryWrapper.lambda().in(FlowWashHospitalStockReportDO::getCrmId, request.getCrmIdList());
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getProvinceNameList()) && CollectionUtil.isEmpty(request.getCrmIdList())) {

            queryWrapper.lambda().in(FlowWashHospitalStockReportDO::getProvinceName, request.getProvinceNameList());
            return;
        }

        queryWrapper.lambda().or(z -> z.in(FlowWashHospitalStockReportDO::getCrmId, request.getCrmIdList()).in(FlowWashHospitalStockReportDO::getProvinceName,request.getProvinceNameList()));
    }


    @Override
    public Page<FlowWashHospitalStockReportDTO> pageList(FlowWashHospitalStockReportPageRequest request) {

        QueryWrapper<FlowWashHospitalStockReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getSoMonth, request.getSoMonth());

        if (request.getCrmId() != null && request.getCrmId()  != 0 ) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getCrmId, request.getCrmId());
        }

        // 设置权限
        this.setPermissionWrapper(request,queryWrapper);

        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().likeRight(FlowWashHospitalStockReportDO::getName, request.getName());
        }

        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getGoodsCode, request.getGoodsCode());
        }

        if (StringUtils.isNotBlank(request.getGoodsName())) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getGoodsName, request.getGoodsName());
        }

        if (StringUtils.isNotBlank(request.getProvinceName())) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getProvinceName, request.getProvinceName());
        }

        if (StringUtils.isNotBlank(request.getCityName())) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getCityName, request.getCityName());
        }

        if (StringUtils.isNotBlank(request.getRegionName())) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getRegionName, request.getRegionName());
        }

        if (StringUtils.isNotBlank(request.getBusinessProvince())) {
            queryWrapper.lambda().likeRight(FlowWashHospitalStockReportDO::getBusinessProvince, request.getBusinessProvince());
        }

        if (StringUtils.isNotBlank(request.getBusinessDepartment())) {
            queryWrapper.lambda().likeRight(FlowWashHospitalStockReportDO::getBusinessDepartment, request.getBusinessDepartment());
        }

        if (request.getNationalGrade() != null && request.getNationalGrade() != 0) {

            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getNationalGrade, request.getNationalGrade());
        }

        if (request.getYlLevel() != null && request.getYlLevel() != 0) {

            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getYlLevel, request.getYlLevel());
        }

        if (CollectionUtil.isNotEmpty(request.getGoodsCodeList())) {

            queryWrapper.lambda().in(FlowWashHospitalStockReportDO::getGoodsCode, request.getGoodsCodeList());
        }

        if (request.getHideFlag() != null && request.getHideFlag()  != 0) {
            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getHideFlag, request.getHideFlag());
        }

        queryWrapper.lambda().orderByDesc(FlowWashHospitalStockReportDO::getId);

        Page<FlowWashHospitalStockReportDO> reportFlowDOList = this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        Page<FlowWashHospitalStockReportDTO> reportPage = PojoUtils.map(reportFlowDOList, FlowWashHospitalStockReportDTO.class);

        if (reportPage == null || reportPage.getSize() == 0) {

            return new Page<>(request.getCurrent(),request.getSize(),reportPage.getTotal());
        }


        reportPage.getRecords().stream().forEach(t -> setRegionName(t));

        return reportPage;

    }

    private void setRegionName(FlowWashHospitalStockReportDTO t) {

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
        QueryWrapper<FlowWashHospitalStockReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getSoMonth, request.getSoMonth());

        if (request.getCrmId() != null) {

            queryWrapper.lambda().eq(FlowWashHospitalStockReportDO::getCrmId, request.getCrmId());
        }

        return this.remove(queryWrapper);
    }
}
