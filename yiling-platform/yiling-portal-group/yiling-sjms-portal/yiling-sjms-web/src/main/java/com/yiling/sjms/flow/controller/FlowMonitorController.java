package com.yiling.sjms.flow.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmEnterpriseEnameLevelEnum;
import com.yiling.dataflow.statistics.api.FlowDistributionEnterpriseApi;
import com.yiling.dataflow.statistics.dto.FlowDistributionEnterpriseDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.bo.SjmsFlowCollectStatisticsCountBO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQuerySjmsRequest;
import com.yiling.open.erp.enums.ClientRunningStatusEnum;
import com.yiling.sjms.flow.form.QueryErpEnterprisePageForm;
import com.yiling.sjms.flow.vo.FlowCollectEnterprisePageDetailVO;
import com.yiling.sjms.flow.vo.FlowCollectEnterprisePageVO;
import com.yiling.sjms.flow.vo.FlowDistributionEnterpriseVO;
import com.yiling.sjms.flow.vo.FlowMonitorNoDataSyncEnterpriseVO;
import com.yiling.sjms.flow.vo.FlowMonitorStatisticsCountVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Slf4j
@RestController
@RequestMapping("/flow/monitor")
@Api(tags = "流向接口监控")
public class FlowMonitorController {

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    ErpClientApi erpClientApi;
    @DubboReference
    FlowDistributionEnterpriseApi flowDistributionEnterpriseApi;

    @ApiOperation(value = "首页-流向接口监控-统计")
    @PostMapping("/statistics/getCount")
    public Result<FlowMonitorStatisticsCountVO> getCount(@CurrentUser CurrentSjmsUserInfo userInfo) {
        FlowMonitorStatisticsCountVO vo = new FlowMonitorStatisticsCountVO();
        // 数据权限。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
        // 统计用户所负责的客户数量时，包含没有进行erp对接的客户、即eid=0的
//        List<Long> crmEnterpriseIdList = flowUserDatascopeApi.listAuthorizedEids(userInfo.getCurrentUserCode());
        List<Long> crmEnterpriseIdList = null;
        if (null == crmEnterpriseIdList) {
            return Result.success(vo);
        }
        // 根据crm_enterprise_id查询crm获取企业信息、类型为经销商的
//        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getDistributorEnterpriseByIds(crmEnterpriseIdList);
        List<CrmEnterpriseDTO> crmEnterpriseList = null;
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(vo);
        }
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && StrUtil.isNotBlank(o.getLicenseNumber())).collect(Collectors.toList());
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(vo);
        }

        List<Long> crmIdEffectiveList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getId).distinct().collect(Collectors.toList());
        String enameLevel = "";

        // 获取企业的经销商级别、根据经销商级别筛选企业id
        // 符合经销商级别的企业
        List<FlowDistributionEnterpriseDTO> flowDistributionEnterpriseList = flowDistributionEnterpriseApi.getListByCrmIdListAndEnameLevel(crmIdEffectiveList, enameLevel);
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(vo);
        }
        flowDistributionEnterpriseList = flowDistributionEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId() > 0 && StrUtil.isNotBlank(o.getEnameLevel())).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(vo);
        }

        // 过滤经销商级别有效的
        List<Long> crmEnterpriseIdTemp = flowDistributionEnterpriseList.stream().map(FlowDistributionEnterpriseDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> crmEnterpriseIdTemp.contains(o.getId())).collect(Collectors.toList());

        // 用户负责的客户数量
        Integer customerCount = crmEnterpriseList.size();
        // 根据社会统一信用代码列表获取统计
        List<String> licenseNumberList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getLicenseNumber).distinct().collect(Collectors.toList());

        // 根据社会统一信用代码统计
        vo = PojoUtils.map(erpClientApi.getSjmsFlowMonitorStatisticsCount(licenseNumberList), FlowMonitorStatisticsCountVO.class);
        vo.setCustomerCount(customerCount);
        // 已部署接口的客户数量 和 占总客户数量的比例
        BigDecimal deployAndCustomerCountRatio = excuteDeployAndCustomerCountRatio(vo.getRunningCount(), customerCount);
        vo.setDeployAndCustomerCountRatio(deployAndCustomerCountRatio);
        return Result.success(vo);
    }

    private BigDecimal excuteDeployAndCustomerCountRatio(Integer runningCount, Integer customerCount) {
        BigDecimal deployAndCustomerCountRatio = BigDecimal.ZERO;
        if (customerCount > 0) {
            // 已部署接口的客户数量 改为取 运行中数量
            BigDecimal deployInterfaceCountDecimal = new BigDecimal(runningCount.toString());
            BigDecimal customerCountDecimal = new BigDecimal(customerCount.toString());
            BigDecimal oneHundred = new BigDecimal("100");
            // 已部署接口占总客户数量百分比
            deployAndCustomerCountRatio = oneHundred.multiply(deployInterfaceCountDecimal).divide(customerCountDecimal, 2, BigDecimal.ROUND_HALF_UP);
        }
        return deployAndCustomerCountRatio;
    }

    @ApiOperation(value = "首页-流向接口监控-未上传流向天数列表")
    @PostMapping("/statistics/getNoDataSyncEnterpriseList")
    public Result<List<FlowMonitorNoDataSyncEnterpriseVO>> getNoDataSyncEnterpriseList(@CurrentUser CurrentSjmsUserInfo userInfo) {
        // 数据权限。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
        List<Long> crmEnterpriseIdList = getDataScopeEidList(userInfo.getCurrentUserCode());
        if (null == crmEnterpriseIdList) {
            return Result.success(ListUtil.empty());
        }
        // 根据crm_enterprise_id查询crm获取企业信息、类型为经销商的
//        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getDistributorEnterpriseByIds(crmEnterpriseIdList);
        List<CrmEnterpriseDTO> crmEnterpriseList = null;
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(ListUtil.empty());
        }
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && StrUtil.isNotBlank(o.getLicenseNumber())).collect(Collectors.toList());
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(ListUtil.empty());
        }

        List<Long> crmIdEffectiveList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getId).distinct().collect(Collectors.toList());
        String enameLevel = "";

        // 获取企业的经销商级别、根据经销商级别筛选企业id
        // 符合经销商级别的企业
        List<FlowDistributionEnterpriseDTO> flowDistributionEnterpriseList = flowDistributionEnterpriseApi.getListByCrmIdListAndEnameLevel(crmIdEffectiveList, enameLevel);
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(ListUtil.empty());
        }
        flowDistributionEnterpriseList = flowDistributionEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId() > 0 && StrUtil.isNotBlank(o.getEnameLevel())).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(ListUtil.empty());
        }

        // 过滤经销商级别有效的
        List<Long> crmEnterpriseIdTemp = flowDistributionEnterpriseList.stream().map(FlowDistributionEnterpriseDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> crmEnterpriseIdTemp.contains(o.getId())).collect(Collectors.toList());

        // 根据社会统一信用代码列表获取统计
        List<String> licenseNumberList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getLicenseNumber).distinct().collect(Collectors.toList());

        // 未上传流向天数最大的前10个企业的列表，仅包含流向对接
        List<FlowMonitorNoDataSyncEnterpriseVO> list = PojoUtils.map(erpClientApi.getSjmsNoDataSyncEnterpriseList(licenseNumberList), FlowMonitorNoDataSyncEnterpriseVO.class);
        return Result.success(list);
    }

    @ApiOperation(value = "流向收集-流向接口监控-经销商查询", httpMethod = "GET")
    @GetMapping("/collect/queryEnterpriseList")
    public Result<List<FlowDistributionEnterpriseVO>> queryEnterpriseList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "clientName") String clientName) {
        if (StrUtil.isBlank(clientName)) {
            throw new BusinessException(ResultCode.FAILED, "经销商名称不能为空");
        }
        // 数据权限。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
        List<Long> crmEnterpriseIdList = getDataScopeEidList(userInfo.getCurrentUserCode());
        if (null == crmEnterpriseIdList) {
            return Result.success(ListUtil.empty());
        }
        // 根据crm_id列表、查询用户负责的企业信息列表，名称筛选，取前50个 经销商
        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.listByIdsAndName(crmEnterpriseIdList, clientName);
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(ListUtil.empty());
        }

        List<FlowDistributionEnterpriseVO> list = new ArrayList<>();
        crmEnterpriseList.forEach(o -> {
            FlowDistributionEnterpriseVO vo = new FlowDistributionEnterpriseVO();
            vo.setCrmEnterpriseId(o.getId());
            vo.setCode(o.getCode());
            vo.setClientName(o.getName());
            list.add(vo);
        });
        return Result.success(list);
    }

    @ApiOperation(value = "流向收集-流向接口监控-列表", httpMethod = "POST")
    @PostMapping("/collect/queryListPage")
    public Result<FlowCollectEnterprisePageVO<FlowCollectEnterprisePageDetailVO>> queryListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryErpEnterprisePageForm form) {
        FlowCollectEnterprisePageVO<FlowCollectEnterprisePageDetailVO> page = new FlowCollectEnterprisePageVO<>();
        // 数据权限。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
        List<Long> crmEnterpriseIdList = getDataScopeEidList(userInfo.getCurrentUserCode());
        if (null == crmEnterpriseIdList) {
            return Result.success(page);
        }

        // 根据经销商搜索
        Long crmId = form.getCrmEnterpriseId();
        if (ObjectUtil.isNotNull(crmId) && 0 != crmId) {
            if (CollUtil.isNotEmpty(crmEnterpriseIdList) && !crmEnterpriseIdList.contains(crmId)) {
                return Result.success(page);
            } else {
                crmEnterpriseIdList.removeAll(crmEnterpriseIdList);
                crmEnterpriseIdList.add(crmId);
            }
        }

        // 根据经销商级别搜索
        Integer enameLevelValue = form.getEnameLevelValue();
        String enameLevel = "";
        if (ObjectUtil.isNotNull(enameLevelValue) && 0 != enameLevelValue.intValue()) {
            CrmEnterpriseEnameLevelEnum enameLevelEnum = CrmEnterpriseEnameLevelEnum.getFromCode(enameLevelValue);
            if (ObjectUtil.isNull(enameLevelEnum)) {
                throw new BusinessException(ResultCode.FAILED, "查询条件[经销商级别]字典值错误，请确认后再操作");
            }
            enameLevel = enameLevelEnum.getName();
        }

        // 根据crm_enterprise_id查询crm获取企业信息、类型为经销商的
//        List<CrmEnterpriseDTO> crmEnterpriseList = crmEnterpriseApi.getDistributorEnterpriseByIds(crmEnterpriseIdList);
        List<CrmEnterpriseDTO> crmEnterpriseList = null;
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(page);
        }
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && StrUtil.isNotBlank(o.getLicenseNumber())).collect(Collectors.toList());
        if (CollUtil.isEmpty(crmEnterpriseList)){
            return Result.success(page);
        }
        List<Long> crmIdEffectiveList = crmEnterpriseList.stream().map(CrmEnterpriseDTO::getId).distinct().collect(Collectors.toList());

        // 获取企业的经销商级别、根据经销商级别筛选企业id
        // 符合经销商级别的企业
        List<FlowDistributionEnterpriseDTO> flowDistributionEnterpriseList = flowDistributionEnterpriseApi.getListByCrmIdListAndEnameLevel(crmIdEffectiveList, enameLevel);
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(page);
        }
        flowDistributionEnterpriseList = flowDistributionEnterpriseList.stream().filter(o -> StrUtil.isNotBlank(o.getCode()) && ObjectUtil.isNotNull(o.getCrmEnterpriseId()) && o.getCrmEnterpriseId() > 0 && StrUtil.isNotBlank(o.getEnameLevel())).collect(Collectors.toList());
        if (CollUtil.isEmpty(flowDistributionEnterpriseList)) {
            return Result.success(page);
        }
        Map<Long, String> finalEnameLevelMap = flowDistributionEnterpriseList.stream().collect(Collectors.toMap(o -> o.getCrmEnterpriseId(), o -> o.getEnameLevel(), (v1, v2) -> v1));
        if (MapUtil.isEmpty(finalEnameLevelMap)) {
            return Result.success(page);
        }
        // 过滤经销商级别有效的
        Set<Long> crmEnterpriseIdSet = finalEnameLevelMap.keySet();
        crmEnterpriseList = crmEnterpriseList.stream().filter(o -> crmEnterpriseIdSet.contains(o.getId())).collect(Collectors.toList());

        // 经销商级别map
        Map<String, String> licenseNumberEnameLevelMap = new HashMap<>();
        // 经销商编码map
        Map<String, Long> licenseNumberCrmEnterpriseIdMap = new HashMap<>();
        crmEnterpriseList.forEach(o -> {
            licenseNumberEnameLevelMap.put(o.getLicenseNumber(), finalEnameLevelMap.get(o.getId()));
            licenseNumberCrmEnterpriseIdMap.put(o.getLicenseNumber(), o.getId());
        });

        // 经销商社会统一信用代码
        List<String> licenseNumberList = new ArrayList<>(licenseNumberEnameLevelMap.keySet());
        // 用户负责的客户数量
        Integer customerCount = crmEnterpriseList.size();

        // 查询已对接的企业信息
        ErpClientQuerySjmsRequest request = PojoUtils.map(form, ErpClientQuerySjmsRequest.class);
        request.setLicenseNumberList(licenseNumberList);
        Page<ErpClientDTO> erpClientDTOPage = erpClientApi.sjmsPage(request);
        if (ObjectUtil.isNull(erpClientDTOPage) || CollUtil.isEmpty(erpClientDTOPage.getRecords())) {
            return Result.success(page);
        }
        page.setRecords(PojoUtils.map(erpClientDTOPage.getRecords(), FlowCollectEnterprisePageDetailVO.class));
        page.setCurrent(erpClientDTOPage.getCurrent());
        page.setSize(erpClientDTOPage.getSize());
        page.setTotal(erpClientDTOPage.getTotal());
        // crm经销商编码、运行状态、说明
        buildRunningStatusAndDescription(page, licenseNumberCrmEnterpriseIdMap, licenseNumberEnameLevelMap);

        // 总数
        int totalCount = Long.valueOf(page.getTotal()).intValue();
        // 经销商数量
        page.setCustomerCount(buildCustomerCount(form, crmId, customerCount, totalCount));

        // 根据分页企业id统计 运行中、未上传昨日流向、超3天未上传流向、超7天未上传流向
        SjmsFlowCollectStatisticsCountBO flowCollectStatisticsBO = erpClientApi.getSjmsFlowCollectStatisticsCount(request);
        // 已部署接口的客户数量 和 占总客户数量的比例，前端改为取运行中数量
        BigDecimal deployAndCustomerCountRatio = excuteDeployAndCustomerCountRatio(flowCollectStatisticsBO.getRunningCount(), page.getCustomerCount());
        page.setDeployAndCustomerCountRatio(deployAndCustomerCountRatio);
        // 运行中接口数量
        page.setRunningCount(flowCollectStatisticsBO.getRunningCount());
        // 未上传昨日流向
        page.setNoDataYesterdayCount(flowCollectStatisticsBO.getNoDataYesterdayCount());
        // 超3天未上传流向
        page.setNoDataMoreThan3DaysCount(flowCollectStatisticsBO.getNoDataMoreThan3DaysCount());
        // 超7天未上传流向
        page.setNoDataMoreThan7DaysCount(flowCollectStatisticsBO.getNoDataMoreThan7DaysCount());
        return Result.success(page);
    }

    private void buildRunningStatusAndDescription(FlowCollectEnterprisePageVO<FlowCollectEnterprisePageDetailVO> page, Map<String, Long> licenseNumberCrmEnterpriseIdMap,
                                                  Map<String, String> licenseNumberEnameLevelMap) {
        page.getRecords().stream().forEach(o -> {
            // crm经销商编码
            Long crmEnterpriseId = licenseNumberCrmEnterpriseIdMap.get(o.getLicenseNumber());
            o.setCrmEnterpriseId(crmEnterpriseId);
            // 经销商级别
            String enameLevel = licenseNumberEnameLevelMap.get(o.getLicenseNumber());
            int enameLevelValue = 0;
            CrmEnterpriseEnameLevelEnum enameLevelEnum = CrmEnterpriseEnameLevelEnum.getFromName(enameLevel);
            if (ObjectUtil.isNotNull(enameLevelEnum)){
                enameLevelValue = enameLevelEnum.getCode();
            }
            o.setEnameLevelValue(enameLevelValue);
            // 运行状态、说明
            Integer clientStatus = o.getClientStatus();
            Integer syncStatus = o.getSyncStatus();
            if (ObjectUtil.equal(1, clientStatus) && ObjectUtil.equal(1, syncStatus)) {
                // 运行中：已激活，已开启同步
                o.setRunningStatus(ClientRunningStatusEnum.ON.getCode());
                o.setDescription("");
            } else {
                // 未运行：未激活，或 未开启同步
                o.setRunningStatus(ClientRunningStatusEnum.OFF.getCode());
                // 说明：
                if (ObjectUtil.equal(0, clientStatus)) {
                    // 未激活
                    o.setDescription("接口未激活，请确认客户是否继续合作");
                } else if (ObjectUtil.equal(1, clientStatus) && ObjectUtil.equal(0, syncStatus)) {
                    // 已激活，未开启同步
                    o.setDescription("接口未开启同步，可能是客户手动关闭接口或关闭服务器");
                }
            }
        });
    }

    private int buildCustomerCount(QueryErpEnterprisePageForm form, Long crmId, Integer customerCount, int totalCount) {
        int resultCount = 0;
        // 经销商数量赋值：
        //     1.筛选条件为空时，取客户数量
        //     2.筛选条件仅有 经销商级别，取根据此条件查询的客户数量
        //     3.筛选条件有 流向收集方式、对接时间、上次收集时间、状态、流向级别，任何一个条件时，取分页查询到的条数
        if (ObjectUtil.isNull(form)) {
            resultCount = customerCount;
        } else {
            boolean enameLevelFlag = (ObjectUtil.isNotNull(form.getEnameLevelValue()) && form.getEnameLevelValue().intValue() != 0) ? true : false;
            boolean crmIdFlag = (ObjectUtil.isNotNull(crmId) && crmId.intValue() != 0) ? true : false;
            boolean flowModeFlag = (ObjectUtil.isNotNull(form.getFlowMode()) && form.getFlowMode().intValue() != 0) ? true : false;
            boolean runningStatusFlag = (ObjectUtil.isNotNull(form.getRunningStatus()) && form.getRunningStatus().intValue() != 0) ? true : false;
            boolean flowLevelStatusFlag = (ObjectUtil.isNotNull(form.getFlowLevel()) && form.getFlowLevel().intValue() != -1) ? true : false;
            boolean depthTimeFlag = (ObjectUtil.isNotNull(form.getDepthTimeStart()) && ObjectUtil.isNotNull(form.getDepthTimeEnd())) ? true : false;
            boolean lastestCollectDateFlag = (ObjectUtil.isNotNull(form.getLastestCollectDateStart()) && ObjectUtil.isNotNull(form.getLastestCollectDateEnd())) ? true : false;

            if (!enameLevelFlag && !crmIdFlag && !flowModeFlag && !runningStatusFlag && !flowLevelStatusFlag && !depthTimeFlag && !lastestCollectDateFlag) {
                resultCount = customerCount;
            } else if (enameLevelFlag && !crmIdFlag && !flowModeFlag && !runningStatusFlag && !flowLevelStatusFlag && !depthTimeFlag && !lastestCollectDateFlag) {
                resultCount = customerCount;
            } else if (crmIdFlag || flowModeFlag || runningStatusFlag || flowLevelStatusFlag || depthTimeFlag || lastestCollectDateFlag) {
                resultCount = totalCount;
            }
        }
        return resultCount;
    }

    /**
     * 根据用户员工工号查询权限，获取负责的企业crm主键id列表
     *
     * @param currentUserCode 员工工号
     * @return
     */
    private List<Long> getDataScopeEidList(String currentUserCode) {
        // 根据用户员工工号查询权限，获取负责的crm企业的主键id列表：crm_enterprise_id。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据
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

}
