package com.yiling.sjms.flowcollect.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.yiling.dataflow.flowcollect.api.FlowCollectUnuploadReasonApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.api.FlowCollectHeartStatisticsApi;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flowcollect.form.QueryDayCollectStatisticsForm;
import com.yiling.sjms.flowcollect.vo.FlowDayHeartStatisticsDetailVO;
import com.yiling.sjms.flowcollect.vo.FlowDayHeartStatisticsVO;
import com.yiling.sjms.flowcollect.vo.FlowDayStatisticsTableVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日流向收集心跳统计
 */
@Slf4j
@RestController
@RequestMapping(value = "/flow/collect/heart")
@Api(tags = "日流向心跳统计")
public class FlowDayHeartStatisticsController {
    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    private FlowCollectHeartStatisticsApi flowCollectHeartStatisticsApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    private FlowCollectUnuploadReasonApi flowCollectUnuploadReasonApi;

    @ApiOperation(value = "流向收集日流向心跳统计-列表", httpMethod = "post")
    @PostMapping("list")
    public Result<FlowDayStatisticsTableVO> list(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryDayCollectStatisticsForm form) {
        FlowDayStatisticsTableVO result = new FlowDayStatisticsTableVO();
        //时间戳
        result.setDateHeaders(buildDateHeader());
//        // 权限控制
//        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
//        log.info("流向权限查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
//        //代表没权限返回空
//        if (null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())) {
//            result.setPage(new Page<>());
//            return Result.success(result);
//        }
//        if (OrgDatascopeEnum.PORTION.getCode().equals(userDatascopeBO.getOrgDatascope()) && CollUtil.isEmpty(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids()) && CollUtil.isEmpty(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes())) {
//            result.setPage(new Page<>());
//            return Result.success(result);
//        }
        QueryDayCollectStatisticsRequest request = PojoUtils.map(form, QueryDayCollectStatisticsRequest.class);
//        if (null != userDatascopeBO.getOrgPartDatascopeBO()) {
//            request.setUserDatascopeBO(userDatascopeBO);
//        }
        //固定经销商信息
        Page<FlowDayHeartStatisticsBO> page = flowCollectHeartStatisticsApi.page(request);
        //处理人员姓名
        buildEsbNameInfo(page.getRecords());
        buildFlowDayHeartStatisticsDetailVO(page.getRecords());
        //返回结果
        Page<FlowDayHeartStatisticsVO> pageVo = PojoUtils.map(page, FlowDayHeartStatisticsVO.class);
        result.setPage(pageVo);
        return Result.success(result);
    }

    /**
     * 处理明细数据
     * @param records
     */
    private void buildFlowDayHeartStatisticsDetailVO(List<FlowDayHeartStatisticsBO> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        //根据ID查询明细中数据
        List<Long> flowIds = records.stream().map(FlowDayHeartStatisticsBO::getId).collect(Collectors.toList());
        List<FlowDayHeartStatisticsDetailBO> flowDetails = flowCollectHeartStatisticsApi.listDetailsByFchsIds(flowIds);
        //根据fcsID分组
        Map<Long, List<FlowDayHeartStatisticsDetailBO>> fcsDetailsMap = flowDetails.stream().collect(Collectors.groupingBy(FlowDayHeartStatisticsDetailBO::getFchId));
        log.info("records:",records);
        log.info("fcsDetailsMap:",fcsDetailsMap);

        QueryFlowCollectUnUploadRequest requestReason=new QueryFlowCollectUnUploadRequest();
        //加载原因list
        List<Long> crmIds=records.stream().map(FlowDayHeartStatisticsBO::getCrmEnterpriseId).collect(Collectors.toList());
        requestReason.setCrmEnterpriseIdList(crmIds);
        requestReason.setStartDate(DateUtil.offsetDay(new Date(), -30));
        requestReason.setEndDate(DateUtil.offsetDay(new Date(), -1));
        List<FlowCollectUnuploadReasonDTO> flowCollectUnuploadReasonDTOS = Optional.ofNullable(flowCollectUnuploadReasonApi.listByCrmIdAndDate(requestReason)).orElse(ListUtil.empty());
        Map<String,List<FlowCollectUnuploadReasonDTO>> reasonMapList= flowCollectUnuploadReasonDTOS.stream().collect(Collectors.groupingBy(this::reasonGroupBy));
        records.stream().forEach(e -> {
            if(CollUtil.isNotEmpty(fcsDetailsMap.get(e.getId()))){
                e.setDateHeadersInfoMap(fcsDetailsMap.get(e.getId()).stream().collect(Collectors.toMap(FlowDayHeartStatisticsDetailBO::getDataTime, m -> m, (v1, v2) -> v1)));
            }
        });
        buildReasonInfo(records,reasonMapList);
    }

    /**
     * 设置流向
     *
     * @param records
     */
    private void buildEsbNameInfo(List<FlowDayHeartStatisticsBO> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        //流向打取人工号获取批量
        List<String> flowJobEmpIds = Optional.ofNullable(records.stream().filter(m -> StringUtils.isNotBlank(m.getFlowJobNumber())).map(FlowDayHeartStatisticsBO::getFlowJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
        List<String> commerceJobNumbers = Optional.ofNullable(records.stream().filter(m -> StringUtils.isNotBlank(m.getCommerceJobNumber())).map(FlowDayHeartStatisticsBO::getCommerceJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
        flowJobEmpIds.addAll(commerceJobNumbers);
        //流向打取人人员列表
        List<EsbEmployeeDTO> flowEsbEmployeeDTOS = CollectionUtil.isEmpty(flowJobEmpIds) ? Lists.newArrayList() : esbEmployeeApi.listByEmpIds(flowJobEmpIds);
        //流向打取人人员列表Map
        Map<String, EsbEmployeeDTO> flowEsbEmployeeDTOSMap = flowEsbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, m -> m, (v1, v2) -> v1));
        QueryFlowCollectUnUploadRequest requestReason=new QueryFlowCollectUnUploadRequest();
        //加载原因list
        List<Long> crmIds=records.stream().map(FlowDayHeartStatisticsBO::getCrmEnterpriseId).collect(Collectors.toList());
        requestReason.setCrmEnterpriseIdList(crmIds);
        requestReason.setStartDate(DateUtil.offsetDay(new Date(), -30));
        requestReason.setEndDate(DateUtil.offsetDay(new Date(), -1));
        List<FlowCollectUnuploadReasonDTO> flowCollectUnuploadReasonDTOS = Optional.ofNullable(flowCollectUnuploadReasonApi.listByCrmIdAndDate(requestReason)).orElse(ListUtil.empty());
        Map<String,List<FlowCollectUnuploadReasonDTO>> reasonMapList= flowCollectUnuploadReasonDTOS.stream().collect(Collectors.groupingBy(this::reasonGroupBy));
        records.stream().forEach(m -> {
            //流向打取人和商业负责人
            EsbEmployeeDTO flowEmp = flowEsbEmployeeDTOSMap.get(m.getFlowJobNumber());
            EsbEmployeeDTO commerceEmp = flowEsbEmployeeDTOSMap.get(m.getCommerceJobNumber());
            m.setFlowLiablePerson(Objects.nonNull(flowEmp) ? flowEmp.getEmpName() : null);
            m.setCommerceLiablePerson(Objects.nonNull(commerceEmp) ? commerceEmp.getEmpName() : null);
        });
        buildReasonInfo(records,reasonMapList);
    }

    public static List<String> buildDateHeader() {
        List<String> dateHeader = new ArrayList<>();
        //默认30内，不包括当天

        for (int i = 1; i < 31; i++) {
            dateHeader.add(DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd"));
        }
        return dateHeader;
    }
    private  void  buildReasonInfo(List<FlowDayHeartStatisticsBO> records,Map<String,List<FlowCollectUnuploadReasonDTO>> reasonMapList){
        if(CollUtil.isEmpty(reasonMapList)){
            return;
        }
        records.stream().forEach(e -> {
            if(CollUtil.isNotEmpty(e.getDateHeadersInfoMap())){
                dynamicDateTable(e,reasonMapList);
            }
        });
    }
    private void dynamicDateTable(FlowDayHeartStatisticsBO item,  Map<String,List<FlowCollectUnuploadReasonDTO>> reasonMapList) {
        for (Map.Entry<String, FlowDayHeartStatisticsDetailBO> entry : item.getDateHeadersInfoMap().entrySet()) {
            String mapKey = entry.getKey();
            FlowDayHeartStatisticsDetailBO mapValue = entry.getValue();
            // 1-未上传 2-已上传
            if (Integer.valueOf(1).equals(mapValue.getFlowStatus())) {
                //key 为crmId和日期
                if (CollUtil.isNotEmpty(reasonMapList.get(item.getCrmEnterpriseId()+mapKey))) {
                    mapValue.setReasonList(reasonMapList.get(item.getCrmEnterpriseId()+mapKey));
                }

            }
        }
    }
    /**
     * 原因分组key
     * @param dto
     * @return
     */
    public String reasonGroupBy(FlowCollectUnuploadReasonDTO dto){
        return  dto.getCrmEnterpriseId()+DateUtil.format(dto.getStatisticsTime(),"yyyy-MM-dd");
    }
    public static void main(String[] args) {
        System.out.println(DateUtil.offsetDay(new Date(), -1));
    }
}
