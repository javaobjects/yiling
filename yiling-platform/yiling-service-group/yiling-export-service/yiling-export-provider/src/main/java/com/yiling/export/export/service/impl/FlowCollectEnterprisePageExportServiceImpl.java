package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmEnterpriseEnameLevelEnum;
import com.yiling.dataflow.statistics.api.FlowDistributionEnterpriseApi;
import com.yiling.dataflow.statistics.dto.FlowDistributionEnterpriseDTO;
import com.yiling.export.export.bo.FlowCollectEnterprisePageListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.enums.ClientFlowModeEnum;
import com.yiling.open.erp.enums.ClientRunningStatusEnum;
import com.yiling.open.erp.enums.ErpFlowLevelEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向接口监控已对接企业列表导出
 *
 * @author: houjie.sun
 * @date: 2023/2/22
 */
@Slf4j
@Service("flowCollectEnterprisePageExportService")
public class FlowCollectEnterprisePageExportServiceImpl implements BaseExportQueryDataService<ErpClientQuerySjmsRequest> {

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    ErpClientApi erpClientApi;
    @DubboReference
    FlowDistributionEnterpriseApi flowDistributionEnterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("crmEnterpriseId", "经销商编码");
        FIELD.put("clientName", "经销商名称");
        FIELD.put("enameLevel", "经销商级别");
        FIELD.put("installEmployee", "接口实施负责人");
        FIELD.put("flowModeStr", "流向收集方式");
        FIELD.put("flowLevelStr", "流向级别");
        FIELD.put("depthTimeStr", "对接时间");
        FIELD.put("lastestCollectDateStr", "上次收集流向时间");
        FIELD.put("runningStatusStr", "状态");
        FIELD.put("description", "说明");

    }

    @Override
    public QueryExportDataDTO queryData(ErpClientQuerySjmsRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<Map<String, Object>> data = new ArrayList<>();

        // 校验数据权限、查询数据
        buildFlowCollectExportData(request, data);

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("流向接口监控导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ErpClientQuerySjmsRequest getParam(Map<String, Object> map) {
        ErpClientQuerySjmsRequest request = PojoUtils.map(map, ErpClientQuerySjmsRequest.class);
        String currentUserCode = map.getOrDefault("currentUserCode", "").toString();
        // 权限校验，获取当前用户负责的公司
        List<Long> crmEnterpriseIdList = getDataScopeEidList(currentUserCode);
        request.setCrmEnterpriseIdList(crmEnterpriseIdList);
        return request;
    }

    private void buildFlowCollectExportData(ErpClientQuerySjmsRequest request, List<Map<String, Object>> data) {
        // 经销商级别map
        Map<String, String> licenseNumberEnameLevelMap = new HashMap<>();
        // 经销商编码map
        Map<String, Long> licenseNumberCrmEnterpriseIdMap = new HashMap<>();
        // 经销商社会统一信用代码
        List<String> licenseNumberList = new ArrayList<>();
        // 数据权限
        boolean authorityFlag = checkAuth(request, licenseNumberEnameLevelMap, licenseNumberCrmEnterpriseIdMap, licenseNumberList);
        if (!authorityFlag) {
            return;
        }

        // 需要循环调用
        Page<ErpClientDTO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            request.setLicenseNumberList(licenseNumberList);
            page = erpClientApi.sjmsPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            for (ErpClientDTO erpClientDTO : page.getRecords()) {
                String enameLevelTemp = licenseNumberEnameLevelMap.get(erpClientDTO.getLicenseNumber());
                Long crmEnterpriseId = licenseNumberCrmEnterpriseIdMap.get(erpClientDTO.getLicenseNumber());
                FlowCollectEnterprisePageListBO flowCollectEnterprisePageListBO = new FlowCollectEnterprisePageListBO();
                flowCollectEnterprisePageListBO.setRkSuId(erpClientDTO.getRkSuId());
                flowCollectEnterprisePageListBO.setClientName(erpClientDTO.getClientName());
                flowCollectEnterprisePageListBO.setEnameLevel(StrUtil.isBlank(enameLevelTemp) ? "" : enameLevelTemp);
                flowCollectEnterprisePageListBO.setCrmEnterpriseId(ObjectUtil.isNull(crmEnterpriseId) ? 0L : crmEnterpriseId);
                flowCollectEnterprisePageListBO.setInstallEmployee(erpClientDTO.getInstallEmployee());
                flowCollectEnterprisePageListBO.setFlowModeStr(ClientFlowModeEnum.getFromCode(erpClientDTO.getFlowMode()).getDesc());
                flowCollectEnterprisePageListBO.setFlowLevelStr(ErpFlowLevelEnum.getFromCode(erpClientDTO.getFlowLevel()).getDesc());
                // 对接时间、上次收集流向时间
                String depthTimeStr = DateUtil.format(erpClientDTO.getDepthTime(), "yyyy-MM-dd HH:mm:ss");
                String lastestCollectDateStr = DateUtil.format(erpClientDTO.getLastestCollectDate(), "yyyy-MM-dd HH:mm:ss");
                if (ObjectUtil.equal("1970-01-01 00:00:00", depthTimeStr)) {
                    depthTimeStr = "- -";
                }
                if (ObjectUtil.equal("1970-01-01 00:00:00", lastestCollectDateStr)) {
                    lastestCollectDateStr = "- -";
                }
                flowCollectEnterprisePageListBO.setDepthTimeStr(depthTimeStr);
                flowCollectEnterprisePageListBO.setLastestCollectDateStr(lastestCollectDateStr);
                // 运行状态、说明
                buildRunningStatusAndDescription(erpClientDTO, flowCollectEnterprisePageListBO);

                data.add(BeanUtil.beanToMap(flowCollectEnterprisePageListBO));
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
    }

    private boolean checkAuth(ErpClientQuerySjmsRequest request, Map<String, String> licenseNumberEnameLevelMap,
                              Map<String, Long> licenseNumberCrmEnterpriseIdMap, List<String> licenseNumberList) {
        // 数据权限标识
        List<Long> crmEnterpriseIdList = request.getCrmEnterpriseIdList();
        if (null == crmEnterpriseIdList) {
            return false;
        }

        // 根据经销商搜索
        Long crmEnterpriseId = request.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmEnterpriseId) && 0 != crmEnterpriseId) {
            if (CollUtil.isNotEmpty(crmEnterpriseIdList) && !crmEnterpriseIdList.contains(crmEnterpriseId)) {
                return false;
            } else {
                crmEnterpriseIdList.removeAll(crmEnterpriseIdList);
                crmEnterpriseIdList.add(crmEnterpriseId);
            }
        }

        // 根据经销商级别搜索
        Integer enameLevelValue = request.getEnameLevelValue();
        String enameLevel = "";
        if (ObjectUtil.isNotNull(enameLevelValue) && 0 != enameLevelValue.intValue()) {
            CrmEnterpriseEnameLevelEnum enameLevelEnum = CrmEnterpriseEnameLevelEnum.getFromCode(enameLevelValue);
            if (ObjectUtil.isNull(enameLevelEnum)) {
                log.error("流向收集-流向接口监控-列表, 导出失败, 查询条件[经销商级别]字典值错误, enameLevelValue:{}", enameLevelValue);
                return false;
            }
            enameLevel = enameLevelEnum.getName();
        }

        // 根据crm_enterprise_id查询crm获取企业信息、类型为经销商的
//        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getDistributorEnterpriseByIds(crmEnterpriseIdList);
        List<CrmEnterpriseDTO> crmEnterpriseList = null;
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return false;
        }
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && StrUtil.isNotBlank(o.getLicenseNumber())).collect(Collectors.toList());
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return false;
        }
        List<Long> crmIdEffectiveList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getId).distinct().collect(Collectors.toList());

        // 获取企业的经销商级别、根据经销商级别筛选企业id
        // 符合经销商级别的企业
        List<FlowDistributionEnterpriseDTO> flowDistributionEnterpriseList = flowDistributionEnterpriseApi.getListByCrmIdListAndEnameLevel(crmIdEffectiveList, enameLevel);
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return false;
        }
        flowDistributionEnterpriseList = flowDistributionEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId() > 0 && StrUtil.isNotBlank(o.getEnameLevel())).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return false;
        }
        Map<Long, String> finalEnameLevelMap = flowDistributionEnterpriseList.stream().collect(Collectors.toMap(o -> o.getCrmEnterpriseId(), o -> o.getEnameLevel(), (v1, v2) -> v1));
        if (MapUtil.isEmpty(finalEnameLevelMap)) {
            return false;
        }

        // 过滤经销商级别有效的
        Set<Long> crmEnterpriseIdSet = finalEnameLevelMap.keySet();
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> crmEnterpriseIdSet.contains(o.getId())).collect(Collectors.toList());

        crmEnterpriseList.forEach(o -> {
            licenseNumberEnameLevelMap.put(o.getLicenseNumber(), finalEnameLevelMap.get(o.getId()));
            licenseNumberCrmEnterpriseIdMap.put(o.getLicenseNumber(), o.getId());
        });

        // 经销商社会统一信用代码
        licenseNumberList.addAll(new ArrayList<>(licenseNumberEnameLevelMap.keySet()));

        return true;
    }

    /**
     * 根据用户员工工号查询权限，获取负责的企业id列表
     *
     * @param currentUserCode 员工工号
     * @return
     */
    private List<Long> getDataScopeEidList(String currentUserCode) {
        // 根据用户员工工号查询权限，获取负责的企业id列表。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
//        List<Long> crmEnterpriseIdList = flowUserDatascopeApi.listAuthorizedEids(currentUserCode);
        List<Long> crmEnterpriseIdList = null;
        if (CollUtil.isNotEmpty(crmEnterpriseIdList)) {
            crmEnterpriseIdList = crmEnterpriseIdList.stream().filter(o -> ObjectUtil.isNotNull(o) && o > 0).distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(crmEnterpriseIdList)) {
                return null;
            }
        }
        return crmEnterpriseIdList;
    }

    private void buildRunningStatusAndDescription(ErpClientDTO erpClientDTO, FlowCollectEnterprisePageListBO flowCollectEnterprisePageListBO) {
        Integer clientStatus = erpClientDTO.getClientStatus();
        Integer syncStatus = erpClientDTO.getSyncStatus();
        if (ObjectUtil.equal(1, clientStatus) && ObjectUtil.equal(1, syncStatus)) {
            // 运行中：已激活，已开启同步
            flowCollectEnterprisePageListBO.setRunningStatusStr(ClientRunningStatusEnum.ON.getDesc());
            flowCollectEnterprisePageListBO.setDescription("");
        } else {
            // 未运行：未激活，或 未开启同步
            flowCollectEnterprisePageListBO.setRunningStatusStr(ClientRunningStatusEnum.OFF.getDesc());
            // 说明：
            if (ObjectUtil.equal(0, clientStatus)) {
                // 未激活
                flowCollectEnterprisePageListBO.setDescription("接口未激活，请确认客户是否继续合作");
            } else if (ObjectUtil.equal(1, clientStatus) && ObjectUtil.equal(0, syncStatus)) {
                // 已激活，未开启同步
                flowCollectEnterprisePageListBO.setDescription("接口未开启同步，可能是客户手动关闭接口或关闭服务器");
            }
        }
    }

}
