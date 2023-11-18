package com.yiling.dataflow.report.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dao.FlowWashInventoryReportMapper;
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.entity.FlowWashInventoryReportDO;
import com.yiling.dataflow.report.service.FlowWashInventoryReportService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向库存合并报表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-02
 */
@Service
@Slf4j
public class FlowWashInventoryReportServiceImpl extends BaseServiceImpl<FlowWashInventoryReportMapper, FlowWashInventoryReportDO> implements FlowWashInventoryReportService {


    private void setPermissionWrapper(FlowWashInventoryReportPageRequest request,QueryWrapper<FlowWashInventoryReportDO> queryWrapper) {

        if (CollectionUtils.isEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getProvinceNameList())) {

            queryWrapper.lambda().in(FlowWashInventoryReportDO::getCrmId, request.getCrmIdList());
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getProvinceNameList()) && CollectionUtil.isEmpty(request.getCrmIdList())) {

            queryWrapper.lambda().in(FlowWashInventoryReportDO::getProvinceName, request.getProvinceNameList());
            return;
        }

        queryWrapper.lambda().or(z -> z.in(FlowWashInventoryReportDO::getCrmId, request.getCrmIdList()).in(FlowWashInventoryReportDO::getProvinceName,request.getProvinceNameList()));
    }


    @Override
    public Page<FlowWashInventoryReportDTO> pageList(FlowWashInventoryReportPageRequest request) {

        QueryWrapper<FlowWashInventoryReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashInventoryReportDO::getSoMonth, request.getSoMonth());

        // 设置权限
        setPermissionWrapper(request,queryWrapper);

        if (request.getCrmId() != null && request.getCrmId()  != 0 ) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getCrmId, request.getCrmId());
        }


        if (StringUtils.isNotBlank(request.getName())) {
            queryWrapper.lambda().likeRight(FlowWashInventoryReportDO::getName, request.getName());
        }

        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getGoodsCode, request.getGoodsCode());
        }

        if (CollectionUtils.isNotEmpty(request.getGoodsCodeList())) {

            queryWrapper.lambda().in(FlowWashInventoryReportDO::getGoodsCode, request.getGoodsCodeList());
        }

        if (StringUtils.isNotBlank(request.getProvinceName())) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getProvinceName, request.getProvinceName());
        }
        if (StringUtils.isNotBlank(request.getCityName())) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getCityName, request.getCityName());
        }
        if (StringUtils.isNotBlank(request.getRegionName())) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getRegionName, request.getRegionName());
        }
        if (StringUtils.isNotBlank(request.getRepresentativeCode())) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getRepresentativeCode, request.getRepresentativeCode());
        }
        if (StringUtils.isNotBlank(request.getPostName())) {
            queryWrapper.lambda().likeRight(FlowWashInventoryReportDO::getPostName, request.getPostName());
        }

        if (request.getSupplierLevel() != null && request.getSupplierLevel() != 0) {

            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getSupplierLevel, request.getSupplierLevel());
        }

        if (request.getSupplierAttribute() != null && request.getSupplierAttribute() != 0) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getSupplierAttribute, request.getSupplierAttribute());
        }

        if (request.getHeadChainFlag() != null && request.getHeadChainFlag() != 0) {

            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getHeadChainFlag, request.getHeadChainFlag());
        }

        if (request.getMappingStatus() != null && request.getMappingStatus() != 0) {
            queryWrapper.lambda().eq(FlowWashInventoryReportDO::getMappingStatus, request.getMappingStatus());
        }

        queryWrapper.lambda().orderByDesc(FlowWashInventoryReportDO::getId);

        Page<FlowWashInventoryReportDO> reportFlowDOList = this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        Page<FlowWashInventoryReportDTO> reportPage = PojoUtils.map(reportFlowDOList, FlowWashInventoryReportDTO.class);

        if (reportPage == null || reportPage.getSize() == 0) {

            return new Page<>(request.getCurrent(),request.getSize(),reportPage.getTotal());
        }

        reportPage.getRecords().stream().forEach(t -> setRegionName(t));

        return reportPage;
    }

    private void setRegionName(FlowWashInventoryReportDTO t) {

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
    public Boolean removeByFmwtId(Long fmwtId) {

        QueryWrapper<FlowWashInventoryReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashInventoryReportDO::getFmwtId, fmwtId);

        return remove(queryWrapper);
    }
}
