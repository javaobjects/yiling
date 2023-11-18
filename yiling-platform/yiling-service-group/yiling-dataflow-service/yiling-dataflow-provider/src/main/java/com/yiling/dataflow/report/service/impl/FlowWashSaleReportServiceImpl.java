package com.yiling.dataflow.report.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yiling.dataflow.report.dao.FlowWashSaleReportMapper;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.CreateFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateSaleReportRelationShipRequest;
import com.yiling.dataflow.report.entity.FlowWashSaleReportDO;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向销售合并报表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-01
 */
@Service
@Slf4j
public class FlowWashSaleReportServiceImpl extends BaseServiceImpl<FlowWashSaleReportMapper, FlowWashSaleReportDO> implements FlowWashSaleReportService {


    private void setPermissionWrapper(FlowWashSaleReportPageRequest request, QueryWrapper<FlowWashSaleReportDO> queryWrapper) {

        if (CollectionUtils.isEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getSupplierProvinceNameList())) {
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getCrmIdList()) && CollectionUtil.isEmpty(request.getSupplierProvinceNameList())) {

            queryWrapper.lambda().in(FlowWashSaleReportDO::getCrmId, request.getCrmIdList());
            return;
        }

        if (CollectionUtils.isNotEmpty(request.getSupplierProvinceNameList()) && CollectionUtil.isEmpty(request.getCrmIdList())) {

            queryWrapper.lambda().in(FlowWashSaleReportDO::getSupplierProvinceName, request.getSupplierProvinceNameList());
            return;
        }

        queryWrapper.lambda().or(z -> z.in(FlowWashSaleReportDO::getCrmId, request.getCrmIdList()).in(FlowWashSaleReportDO::getSupplierProvinceName, request.getSupplierProvinceNameList()));
    }


    @Override
    public Page<FlowWashSaleReportDTO> pageList(FlowWashSaleReportPageRequest request) {

        Asserts.check(StringUtils.isNotBlank(request.getYear()), "年份为空!");
        Asserts.check(StringUtils.isNotBlank(request.getMonth()), "月份为空!");

        QueryWrapper<FlowWashSaleReportDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(FlowWashSaleReportDO::getYear, request.getYear());
        queryWrapper.lambda().eq(FlowWashSaleReportDO::getMonth, request.getMonth());

        // 设置权限
        this.setPermissionWrapper(request, queryWrapper);

        if (request.getCrmId() != null && request.getCrmId() != 0) {
            queryWrapper.lambda().eq(FlowWashSaleReportDO::getCrmId, request.getCrmId());
        }

        if (request.getCustomerCrmId() != null && request.getCustomerCrmId() != 0) {
            queryWrapper.lambda().eq(FlowWashSaleReportDO::getCustomerCrmId, request.getCustomerCrmId());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getEname, request.getEname());
        }
        if (StringUtils.isNotBlank(request.getSoGoodsName())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getSoGoodsName, request.getSoGoodsName());
        }
        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(FlowWashSaleReportDO::getGoodsCode, request.getGoodsCode());
        }
        if (CollectionUtils.isNotEmpty(request.getGoodsCodeList())) {

            queryWrapper.lambda().in(FlowWashSaleReportDO::getGoodsCode, request.getGoodsCodeList());
        }
        if (StringUtils.isNotBlank(request.getDepartment())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getDepartment, request.getDepartment());
        }
        if (StringUtils.isNotBlank(request.getBusinessDepartment())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getBusinessDepartment, request.getBusinessDepartment());
        }
        if (StringUtils.isNotBlank(request.getProvincialArea())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getProvincialArea, request.getProvincialArea());
        }
        if (StringUtils.isNotBlank(request.getBusinessProvince())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getBusinessProvince, request.getBusinessProvince());
        }

        if (StringUtils.isNotBlank(request.getRegionName())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getDistrictCounty, request.getRegionName());
        }

        if (StringUtils.isNotBlank(request.getOriginalEnterpriseName())) {

            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getOriginalEnterpriseName, request.getOriginalEnterpriseName());
        }

        if (StringUtils.isNotBlank(request.getSoSpecifications())) {
            queryWrapper.lambda().likeRight(FlowWashSaleReportDO::getSoSpecifications, request.getSoSpecifications());
        }

        if (StringUtils.isNotBlank(request.getRepresentativeCode())) {
            queryWrapper.lambda().eq(FlowWashSaleReportDO::getRepresentativeCode, request.getRepresentativeCode());
        }

        if (request.getMappingStatus() != null && request.getMappingStatus() != 0) {
            queryWrapper.lambda().eq(FlowWashSaleReportDO::getMappingStatus, request.getMappingStatus());
        }

        if (CollectionUtils.isNotEmpty(request.getFlowClassifyList())) {
            queryWrapper.lambda().in(FlowWashSaleReportDO::getFlowClassify, request.getFlowClassifyList());
        }

        if (request.getIsChainFlag() != null && request.getIsChainFlag() != 0) {

            queryWrapper.lambda().eq(FlowWashSaleReportDO::getIsChainFlag, request.getIsChainFlag());
        }

        if (request.getIsLockFlag() != null && request.getIsLockFlag() != 0) {

            queryWrapper.lambda().eq(FlowWashSaleReportDO::getIsLockFlag, request.getIsLockFlag());
        }

        if (request.getSoTime() != null ) {

            queryWrapper.lambda().eq(FlowWashSaleReportDO::getSoTime,request.getSoTime());
        }


        if (request.getDataScope() != null ) {
            if (FlowWashSaleReportPageRequest.DataScopeEnum.GT == request.getDataScope()) {
                queryWrapper.lambda().gt(FlowWashSaleReportDO::getFinalQuantity,BigDecimal.ZERO);
            } else if (FlowWashSaleReportPageRequest.DataScopeEnum.GE == request.getDataScope()) {
                queryWrapper.lambda().ge(FlowWashSaleReportDO::getFinalQuantity,BigDecimal.ZERO);
            } else if (FlowWashSaleReportPageRequest.DataScopeEnum.LT == request.getDataScope()) {
                queryWrapper.lambda().lt(FlowWashSaleReportDO::getFinalQuantity,BigDecimal.ZERO);
            } else if (FlowWashSaleReportPageRequest.DataScopeEnum.LE == request.getDataScope()) {
                queryWrapper.lambda().le(FlowWashSaleReportDO::getFinalQuantity,BigDecimal.ZERO);
            }
        }

        queryWrapper.select("id as id ");

        queryWrapper.lambda().orderByDesc(FlowWashSaleReportDO::getId);

        Page<Map<String, Object>> page = new Page<>(request.getCurrent(), request.getSize());
        // 优化sql,关掉
        page.setOptimizeCountSql(false);
        page.setHitCount(true);

        Page<Map<String, Object>> pageMaps = this.pageMaps(page, queryWrapper);

        if (pageMaps == null || pageMaps.getSize() == 0) {

            return new Page<>(request.getCurrent(),request.getSize(),pageMaps.getTotal());
        }

        List<Long> idList = pageMaps.getRecords().stream().map(t -> Long.valueOf(t.get("id").toString())).collect(Collectors.toList());

        // 500 分批一次
        List<List<Long>> subList = Lists.partition(idList, 500);

        // 按照创建时间排序
        List<FlowWashSaleReportDO> flowWashSaleReportDOList = subList.stream()
                .map(t -> this.listByIds(t))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(FlowWashSaleReportDO::getCreateTime).reversed())
                .collect(Collectors.toList());

        Page<FlowWashSaleReportDTO> reportPage = new Page<>(request.getCurrent(), request.getSize(),pageMaps.getTotal());

        reportPage.setRecords(PojoUtils.map(flowWashSaleReportDOList,FlowWashSaleReportDTO.class));

        reportPage.getRecords().stream().forEach(t -> {

            if (ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(t.getLockTime(), "yyyy-MM-dd HH:mm:ss"))) {
                t.setLockTime(null);
            }

            if (ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(t.getLastUnLockTime(), "yyyy-MM-dd HH:mm:ss"))) {
                t.setLastUnLockTime(null);
            }
        });

        return reportPage;
    }

    @Override
    public Boolean removeByFmwtId(Long fmwtId) {
        QueryWrapper<FlowWashSaleReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowWashSaleReportDO::getFmwtId, fmwtId);

        return remove(queryWrapper);
    }


    @Override
    public List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds) {

        return this.listByFlowSaleWashIds(flowSaleWashIds, null);
    }

    @Override
    public List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds, List<FlowClassifyEnum> flowClassifyEnumList) {

        Assert.notEmpty(flowSaleWashIds, "流向Id不能为空!");
        QueryWrapper<FlowWashSaleReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FlowWashSaleReportDO::getFlowSaleWashId, flowSaleWashIds);

        if (CollectionUtil.isNotEmpty(flowClassifyEnumList)) {

            queryWrapper.lambda().in(FlowWashSaleReportDO::getFlowClassify, flowClassifyEnumList.stream().map(t -> t.getCode()).collect(Collectors.toList()));
        }

        return PojoUtils.map(this.baseMapper.selectList(queryWrapper), FlowWashSaleReportDTO.class);
    }

    @Override
    public List<FlowWashSaleReportDO> listByFlowKey(List<String> flowKeys) {

        Assert.notEmpty(flowKeys, "流向Id不能为空!");
        QueryWrapper<FlowWashSaleReportDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FlowWashSaleReportDO::getFlowKey, flowKeys);

        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean batchUpdateSaleReportRelationShip(List<UpdateSaleReportRelationShipRequest> relationShipRequests) {

        List<FlowWashSaleReportDO> flowWashSaleReportDOList = relationShipRequests.stream().map(t -> {

            FlowWashSaleReportDO reportDO = new FlowWashSaleReportDO();
            reportDO.setId(t.getId());
            reportDO.setDepartment(t.getDepartment());
            reportDO.setBusinessDepartment(t.getBusinessDepartment());
            reportDO.setBusinessProvince(t.getBusinessProvince());
            reportDO.setSuperiorSupervisorCode(t.getSuperiorSupervisorCode());
            reportDO.setSuperiorSupervisorName(t.getSuperiorSupervisorName());
            reportDO.setRepresentativeCode(t.getRepresentativeCode());
            reportDO.setRepresentativeName(t.getRepresentativeName());

            return reportDO;

        }).collect(Collectors.toList());


        return this.updateBatchById(flowWashSaleReportDOList);
    }


    @Override
    public List<Long> listCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest) {


        return this.baseMapper.listCrmIdByCondition(reportRequest);
    }


    @Override
    public List<Long> listCustomerCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest) {

        return this.baseMapper.listCustomerCrmIdByCondition(reportRequest);
    }


    @Override
    public boolean removeByFlowSaleWashId(Long flowWashSaleId, FlowClassifyEnum flowClassifyEnum) {

        Assert.notNull(flowWashSaleId, "流向Id不能为空");
        Assert.notNull(flowClassifyEnum, "流向类型不能为空");

        QueryWrapper<FlowWashSaleReportDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(FlowWashSaleReportDO::getFlowSaleWashId, flowWashSaleId);
        wrapper.lambda().eq(FlowWashSaleReportDO::getFlowClassify, flowClassifyEnum.getCode());

        return this.remove(wrapper);
    }


    @Override
    public boolean removeByFlowSaleWashId(List<Long> flowWashSaleIds, FlowClassifyEnum flowClassifyEnum) {
        Assert.notEmpty(flowWashSaleIds, "流向Id不能为空");
        Assert.notNull(flowClassifyEnum, "流向类型不能为空");

        QueryWrapper<FlowWashSaleReportDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(FlowWashSaleReportDO::getFlowSaleWashId, flowWashSaleIds);
        wrapper.lambda().eq(FlowWashSaleReportDO::getFlowClassify, flowClassifyEnum.getCode());

        return this.remove(wrapper);
    }

    @Override
    public FlowWashSaleReportDTO getFlowWashSale(Long flowWashSaleId, FlowClassifyEnum flowClassifyEnum) {

        Assert.notNull(flowWashSaleId, "流向Id不能为空");
        Assert.notNull(flowClassifyEnum, "流向类型不能为空");

        QueryWrapper<FlowWashSaleReportDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().eq(FlowWashSaleReportDO::getFlowSaleWashId, flowWashSaleId);
        wrapper.lambda().eq(FlowWashSaleReportDO::getFlowClassify, flowClassifyEnum.getCode());
        wrapper.lambda().last("limit 1");

        return PojoUtils.map(this.getOne(wrapper), FlowWashSaleReportDTO.class);
    }


    @Override
    public BigDecimal sumTotalMoney(SumFlowWashReportRequest reportRequest) {

        Assert.notBlank(reportRequest.getYear(), "年份");
        Assert.notBlank(reportRequest.getMonth(), "月份");

        QueryWrapper<FlowWashSaleReportDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(FlowWashSaleReportDO::getYear,reportRequest.getYear());
        wrapper.lambda().eq(FlowWashSaleReportDO::getMonth,reportRequest.getMonth());

        if (reportRequest.getCrmId() != null && reportRequest.getCrmId() != 0) {

            wrapper.lambda().eq(FlowWashSaleReportDO::getCrmId,reportRequest.getCrmId());
        }

        if (reportRequest.getCustomerCrmId() != null && reportRequest.getCustomerCrmId() != 0) {

            wrapper.lambda().eq(FlowWashSaleReportDO::getCustomerCrmId,reportRequest.getCustomerCrmId());
        }

        if (reportRequest.getMappingStatus() != null && reportRequest.getMappingStatus() != 0) {

            wrapper.lambda().eq(FlowWashSaleReportDO::getMappingStatus,reportRequest.getMappingStatus());
        }

        if (reportRequest.getIsLockFlag() != null && reportRequest.getIsLockFlag() != 0) {

            wrapper.lambda().eq(FlowWashSaleReportDO::getIsLockFlag,reportRequest.getIsLockFlag());
        }

        if (CollectionUtil.isNotEmpty(reportRequest.getFlowClassifyList())) {

            wrapper.lambda().in(FlowWashSaleReportDO::getFlowClassify,reportRequest.getFlowClassifyList());
        }

        wrapper.select("IFNULL(sum(so_total_amount),0.00) as 'total_amount' ");

        Map<String, Object> result = this.getMap(wrapper);
        BigDecimal totalAmount = new BigDecimal(String.valueOf(result.get("total_amount")));

        return totalAmount;
    }


    @Override
    public boolean createByFlowWashRecord(List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests) {

        Assert.notEmpty(createFlowWashSaleReportRequests, "参数不能为空");

        Preconditions.checkArgument(createFlowWashSaleReportRequests.size() <= 200, "更新长度超过200,请分批处理!");

        List<String> flowKeylist = createFlowWashSaleReportRequests.stream().map(t -> t.getOldFlowKey()).collect(Collectors.toList());

        List<FlowWashSaleReportDO> flowWashSaleReportDOList = this.listByFlowKey(flowKeylist);

        boolean checkResult = true;

        if (CollectionUtil.isEmpty(flowWashSaleReportDOList) || flowWashSaleReportDOList.size() != createFlowWashSaleReportRequests.size()) {

            checkResult = false;
        }

        Preconditions.checkArgument(checkResult, "未查询到相关源流向!");

        Map<String, FlowWashSaleReportDO> flowWashSaleReportDOMap = flowWashSaleReportDOList.stream().collect(Collectors.toMap(FlowWashSaleReportDO::getFlowKey, Function.identity(), (e1, e2) -> e2));

        List<FlowWashSaleReportDO> createReportList =  createFlowWashSaleReportRequests.stream().map(t -> {

            FlowWashSaleReportDO reportDO =  flowWashSaleReportDOMap.get(t.getOldFlowKey());

            if (reportDO == null) {

                throw new RuntimeException("源流向未查询到!");
            }

            // 计入年月
            String recordMonth = DateUtil.format(DateUtil.parse(t.getYear() + "-" + t.getMonth(), "yyyy-MM"), "yyyy-MM");

            FlowWashSaleReportDO updateDo = PojoUtils.map(reportDO,FlowWashSaleReportDO.class);
            updateDo.setId(null);
            updateDo.setFlowSaleWashId(t.getFlowWashId());
            updateDo.setWashTime(t.getWashTime());
            updateDo.setFlowClassify(t.getFlowClassify());
            updateDo.setFlowKey(t.getFlowKey());
            updateDo.setFinalQuantity(t.getQty());
            reportDO.setComplainType(t.getComplainType());
            reportDO.setYear(Optional.ofNullable(t.getYear()).map(z -> z.toString()).orElse(""));
            reportDO.setMonth(Optional.ofNullable(t.getMonth()).map(z -> z.toString()).orElse(""));
            reportDO.setRecordMonth(recordMonth);
            reportDO.setFileName("");
            reportDO.setSoTotalAmount(NumberUtil.mul(updateDo.getFinalQuantity(), reportDO.getSalesPrice()));

            return reportDO;

        }).collect(Collectors.toList());

        return this.saveBatch(createReportList);
    }
}
