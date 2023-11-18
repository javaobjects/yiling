package com.yiling.sjms.gb.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.UpdateSaleReportRelationShipRequest;
import com.yiling.sjms.gb.api.GbOrgManagerApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 维护销售报表中三者关系数据
 * @author zhigang.guo
 * @date: 2023/3/21
 */
@Slf4j
@Component
public class ReportRelationShipCompleteHandler {

    @DubboReference(timeout = 10 * 1000)
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference(timeout = 10 * 1000)
    FlowWashReportApi washReportApi;

    @DubboReference(timeout = 10 * 1000)
    GbOrgManagerApi gbOrgManagerApi;


    public boolean  completeSaleReportRelationShip (List<String> flowWashIds) {
        List<Long> flowWashIdList = flowWashIds.stream().map(t -> Long.valueOf(t)).collect(Collectors.toList());

        List<FlowWashSaleReportDTO> flowWashSaleReportDTOS = washReportApi.listByFlowSaleWashIds(flowWashIdList);

        if (CollectionUtil.isEmpty(flowWashSaleReportDTOS)) {

            return true;
        }

        List<String> representativeCodeList = flowWashSaleReportDTOS.stream().filter(t -> StringUtils.isNotBlank(t.getRepresentativeCode())).map(t -> t.getRepresentativeCode()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(representativeCodeList)) {

            return true;
        }

        List<EsbEmployeeDTO> esbEmployeeDTOs = esbEmployeeApi.listByEmpIds(representativeCodeList);
        if (CollectionUtil.isEmpty(esbEmployeeDTOs)) {

            return true;
        }

        List<Long> postCodes = Lists.newArrayList();
        Map<String, List<EsbEmployeeDTO>> collectList = esbEmployeeDTOs.stream().collect(Collectors.groupingBy(EsbEmployeeDTO::getEmpId));
        List<UpdateSaleReportRelationShipRequest> updateSaleReportRelationShipRequests =  flowWashSaleReportDTOS.stream().filter(t -> StringUtils.isNotBlank(t.getRepresentativeCode())).map(t -> {
            List<EsbEmployeeDTO> esbEmployeeDTOS = collectList.get(t.getRepresentativeCode());
            if (CollectionUtil.isEmpty(esbEmployeeDTOS)) {

                return null;
            }
            List<EsbEmployeeDTO> use = esbEmployeeDTOS.stream().filter(item -> item.getStatus().equals("正式") || item.getStatus().equals("试用")).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(use)) {

                return null;
            }
            EsbEmployeeDTO one = use.get(0);

            UpdateSaleReportRelationShipRequest reportRelationShipRequest = new UpdateSaleReportRelationShipRequest();
            reportRelationShipRequest.setId(t.getId());
            reportRelationShipRequest.setDepartment(one.getDepartment());
            reportRelationShipRequest.setBusinessDepartment(one.getYxDept());
            reportRelationShipRequest.setBusinessProvince(one.getYxProvince());
            reportRelationShipRequest.setBusinessArea(one.getYxArea());
            reportRelationShipRequest.setSuperiorSupervisorCode(one.getSuperior());
            reportRelationShipRequest.setSuperiorSupervisorName(one.getSuperiorName());
            reportRelationShipRequest.setRepresentativeCode(one.getEmpId());
            reportRelationShipRequest.setRepresentativeName(one.getEmpName());
            reportRelationShipRequest.setPostName(one.getJobName());
            reportRelationShipRequest.setPostCode(one.getJobId());

            postCodes.add(one.getJobId());

            return reportRelationShipRequest;

        }).filter(t -> ObjectUtil.isNotNull(t)).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(updateSaleReportRelationShipRequests)) {

            return true;
        }

        Map<Long, EsbOrganizationDTO> longEsbOrganizationDTOMap = gbOrgManagerApi.listByOrgIds(postCodes);
        updateSaleReportRelationShipRequests.forEach(t -> {
            EsbOrganizationDTO dto =  longEsbOrganizationDTOMap.getOrDefault(t.getPostCode(),new EsbOrganizationDTO());
            t.setDepartment(dto.getOrgName());
        });

       return washReportApi.batchUpdateSaleReportRelationShip(updateSaleReportRelationShipRequests);
    }
}
