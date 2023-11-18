package com.yiling.sjms.flow.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.UnlockCustomerMatchingInfoApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerMatchingInfoDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerMatchingInfoPageRequest;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseCustomerMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.api.EsFlowEnterpriseCustomerMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseCustomerMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseCustomerMappingSearchRequest;
import com.yiling.sjms.flow.form.BatchSaveCustomerMappingForm;
import com.yiling.sjms.flow.form.QueryFlowEnterpriseCustomerMappingPageForm;
import com.yiling.sjms.flow.form.SaveFlowEnterpriseCustomerMappingForm;
import com.yiling.sjms.flow.vo.FlowMonthWashControlVO;
import com.yiling.sjms.flow.vo.MappingPageVO;
import com.yiling.sjms.flow.vo.FlowEnterpriseCustomerMappingVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingApiController
 * @描述 客户对照关系Controller
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@RestController
@RequestMapping("/flowEnterpriseCustomerMapping")
@Api(tags = "客户对照关系管理")
public class FlowEnterpriseCustomerMappingController extends BaseController {

    @DubboReference
    private EsFlowEnterpriseCustomerMappingApi esFlowEnterpriseCustomerMappingApi;

    @DubboReference
    private FlowEnterpriseCustomerMappingApi flowEnterpriseCustomerMappingApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private UnlockCustomerMatchingInfoApi unlockCustomerMatchingInfoApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "客户对照查询", httpMethod = "POST")
    @PostMapping("/searchCustomerMapping")
    public Result<MappingPageVO<FlowEnterpriseCustomerMappingVO>> search(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseCustomerMappingPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("客户对照查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.success(new MappingPageVO());
        }
        if(null!=form.getStartUpdateTime() || null != form.getEndUpdateTime()){
            if(null!=form.getStartUpdateTime()){
                if(null == form.getEndUpdateTime()){
                    return Result.failed("最后操作时间结束为空");
                }
            }else {
                return Result.failed("最后操作时间开始为空");
            }
            form.setStartUpdateTime(DateUtil.beginOfDay(form.getStartUpdateTime()));
            form.setEndUpdateTime(DateUtil.endOfDay(form.getEndUpdateTime()));
            if(form.getStartUpdateTime().getTime()>form.getEndUpdateTime().getTime()){
                return Result.failed("开始时间必须小于结束时间");
            }
        }
        EsFlowEnterpriseCustomerMappingSearchRequest request = PojoUtils.map(form, EsFlowEnterpriseCustomerMappingSearchRequest.class);
        request.setSortField("update_time");
        //权限
        request.setOrgDatascope(userDatascopeBO.getOrgDatascope());
        if(null!=userDatascopeBO.getOrgPartDatascopeBO()){
            request.setCrmEnterpriseIds(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }
        EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO = esFlowEnterpriseCustomerMappingApi.searchFlowEnterpriseCustomerMapping(request);
        MappingPageVO<FlowEnterpriseCustomerMappingVO> page = this.toPageVO(esAggregationDTO);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null != controlDTO){
            page.setCustomerMappingWashControl(PojoUtils.map(controlDTO, FlowMonthWashControlVO.class));
        }
        return Result.success(page);
    }

    @ApiOperation(value = "客户未对照查询", httpMethod = "POST")
    @PostMapping("/searchCustomerUnmapped")
    public Result<MappingPageVO<FlowEnterpriseCustomerMappingVO>> searchCustomerUnmapped(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseCustomerMappingPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("客户未对照查询权限:userDatascopeBO={}",JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.success(new MappingPageVO());
        }
        if(null!=form.getStartLastUploadTime() || null != form.getEndLastUploadTime()){
            if(null!=form.getStartLastUploadTime()){
                if(null == form.getEndLastUploadTime()){
                    return Result.failed("最后上传时间结束为空");
                }
            }else {
                return Result.failed("最后上传时间开始为空");
            }
            form.setStartLastUploadTime(DateUtil.beginOfDay(form.getStartLastUploadTime()));
            form.setEndLastUploadTime(DateUtil.endOfDay(form.getEndLastUploadTime()));
            if(form.getStartLastUploadTime().getTime()>form.getEndLastUploadTime().getTime()){
                return Result.failed("开始时间必须小于结束时间");
            }
        }
        EsFlowEnterpriseCustomerMappingSearchRequest request = PojoUtils.map(form, EsFlowEnterpriseCustomerMappingSearchRequest.class);
        request.setSortField("last_upload_time");
        request.setCrmOrgId(0L);
        //部分权限
        request.setOrgDatascope(userDatascopeBO.getOrgDatascope());
        if(null!=userDatascopeBO.getOrgPartDatascopeBO()){
            request.setCrmEnterpriseIds(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }
        EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO = esFlowEnterpriseCustomerMappingApi.searchFlowEnterpriseCustomerMapping(request);
        MappingPageVO<FlowEnterpriseCustomerMappingVO> page = this.toPageVO(esAggregationDTO, form.getRecommendFlag());
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null != controlDTO){
            page.setCustomerMappingWashControl(PojoUtils.map(controlDTO, FlowMonthWashControlVO.class));
        }
        return Result.success(page);
    }

    @ApiOperation(value = "客户对照删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam("id")Long id){
        FlowEnterpriseCustomerMappingDTO customerMappingDTO = flowEnterpriseCustomerMappingApi.findById(id);
        if(null == customerMappingDTO){
            return Result.failed("删除的数据不存在");
        }
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null == controlDTO){
            return Result.failed("月流向清洗未开启,操作失败！");
        }
        Boolean b = flowEnterpriseCustomerMappingApi.deleteById(id, userInfo.getCurrentUserId());
        if(b){
            Date currentDate = new Date();
            Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
            int lastMonth=DateUtil.month(lastMonthTime)+1;
            int lastYear = DateUtil.year(lastMonthTime);
            FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
            if(null!=lastMonthControlDTO && 2==lastMonthControlDTO.getWashStatus()){
                //发送删除前数值给上月月流向刷新
                flowEnterpriseCustomerMappingApi.sendRefreshCustomerFlowMq(ListUtil.toList(customerMappingDTO));
            }
            return Result.success();
        }else {
            return Result.failed("删除失败");
        }
    }

    @ApiOperation(value = "客户对照编辑", httpMethod = "POST")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid SaveFlowEnterpriseCustomerMappingForm form){
        FlowEnterpriseCustomerMappingDTO customerMappingDTO = flowEnterpriseCustomerMappingApi.findById(form.getId());
        if(null == customerMappingDTO){
            return Result.failed("客户对照关系不存在");
        }
        if(customerMappingDTO.getCrmEnterpriseId().equals(form.getCrmOrgId())){
            return Result.failed("标准机构编码与经销商编码相同！");
        }
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null == controlDTO){
            return Result.failed("月流向清洗未开启,操作失败！");
        }
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmOrgId());
        if(null==crmEnterpriseDTO || !crmEnterpriseDTO.getName().equals(form.getOrgName())){
            return Result.failed("标准机构编码和标准机构名称未找到对应标准机构");
        }
        SaveFlowEnterpriseCustomerMappingRequest request = PojoUtils.map(form,SaveFlowEnterpriseCustomerMappingRequest.class);
        request.setOrgLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        flowEnterpriseCustomerMappingApi.save(request);
        Date currentDate = new Date();
        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth=DateUtil.month(lastMonthTime)+1;
        int lastYear = DateUtil.year(lastMonthTime);
        FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        if(null!=lastMonthControlDTO && 2==lastMonthControlDTO.getWashStatus()){
            //发送更新前数值给上月月流向刷新
            flowEnterpriseCustomerMappingApi.sendRefreshCustomerFlowMq(ListUtil.toList(customerMappingDTO));
        }
        return Result.success();
    }

    @ApiOperation(value = "客户对照批量保存编辑", httpMethod = "POST")
    @PostMapping("/batchSave")
    public Result<Map<String, String>> batchSave(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid BatchSaveCustomerMappingForm form){
        if(CollectionUtil.isEmpty(form.getSaveList())){
            return Result.failed("提交更新数据为空，提交失败！");
        }
        List<SaveFlowEnterpriseCustomerMappingRequest> requestList = PojoUtils.map(form.getSaveList(), SaveFlowEnterpriseCustomerMappingRequest.class);
        List<Long> crmEnterpriseIdList = requestList.stream().map(SaveFlowEnterpriseCustomerMappingRequest::getCrmOrgId).collect(Collectors.toList());
        List<CrmEnterpriseDTO> orgList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterpriseIdList);
        Map<Long, CrmEnterpriseDTO> enterpriseMap = orgList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));

        List<Long> idList = requestList.stream().filter(request->null != request.getId() && request.getId()>0).map(SaveFlowEnterpriseCustomerMappingRequest::getId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(idList) || idList.size()!=requestList.size()){
            return Result.failed("提交数据异常，提交失败！");
        }
        List<FlowEnterpriseCustomerMappingDTO> mappingList = flowEnterpriseCustomerMappingApi.findByIds(idList);
        Map<Long, FlowEnterpriseCustomerMappingDTO> mappingMap = mappingList.stream().collect(Collectors.toMap(FlowEnterpriseCustomerMappingDTO::getId, Function.identity()));

        for (SaveFlowEnterpriseCustomerMappingRequest request:requestList){
            if(null==request.getCrmOrgId() || request.getCrmOrgId()<=0){
                return Result.failed("提交数据异常，提交失败！");
            }
            FlowEnterpriseCustomerMappingDTO mappingDTO = mappingMap.get(request.getId());
            if(request.getCrmOrgId().equals(mappingDTO.getCrmEnterpriseId())){
                return Result.failed("标准机构编码与经销商编码相同！");
            }
            CrmEnterpriseDTO org = enterpriseMap.get(request.getCrmOrgId());
            if(null == org){
                return Result.failed("您输入的标准机构编码"+request.getCrmOrgId()+"不存在");
            }
            if(!org.getName().equals(request.getOrgName())){
                return Result.failed("您输入的标准机构名称"+request.getOrgName()+"不正确");
            }
            request.setOrgLicenseNumber(org.getLicenseNumber());
            request.setOpUserId(userInfo.getCurrentUserId());
        }
        Boolean b = flowEnterpriseCustomerMappingApi.batchUpdateById(requestList);
        if(!b){
            return Result.failed("更新标准机构信息失败！");
        }
        Date currentDate = new Date();
        HashMap<String, String> resultMap = new HashMap<>();
        String currentDateFormat = DateUtil.format(currentDate, "yyyy.MM.dd");
        String startDayFormat = DateUtil.format(currentDate, "yyyy.MM")+".01";
        resultMap.put("dayFlowRefreshMsg","系统将自动刷新【"+startDayFormat+"-"+currentDateFormat+"】期间的日流向 ！");

        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth=DateUtil.month(lastMonthTime)+1;
        int lastYear = DateUtil.year(lastMonthTime);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        String controlMsg="【"+lastYear+"."+lastMonth+"】月流向的客户对照清洗日程不存在，无法更新！";
        if(null!=controlDTO){
            if(controlDTO.getWashStatus()==2){
                List<FlowEnterpriseCustomerMappingDTO> refreshList = requestList.stream().map(request -> {
                    FlowEnterpriseCustomerMappingDTO mappingDTO = new FlowEnterpriseCustomerMappingDTO();
                    mappingDTO.setId(request.getId());
                    mappingDTO.setCrmOrgId(0L);
                    return mappingDTO;
                }).collect(Collectors.toList());
                flowEnterpriseCustomerMappingApi.sendRefreshCustomerFlowMq(refreshList);
                controlMsg="系统将自动刷新【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】的月流向！";
            }else if(controlDTO.getWashStatus()==3){
                controlMsg="【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】月流向的客户对照阶段已手动关闭，无法更新！";
            }else{
                controlMsg="【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】月流向的客户对照阶段未开启，无法更新！";
            }
//            FlowMonthWashControlMappingStatusEnum controlStatusEnum = FlowMonthWashControlMappingStatusEnum.getByCode(controlDTO.getCustomerMappingStatus());
//            switch (controlStatusEnum){
//                case IN_PROGRESS:
//                case MANUAL_OPEN:
//                    //发送mq
//                    List<FlowEnterpriseCustomerMappingDTO> refreshList = requestList.stream().map(request -> {
//                        FlowEnterpriseCustomerMappingDTO mappingDTO = new FlowEnterpriseCustomerMappingDTO();
//                        mappingDTO.setId(request.getId());
//                        mappingDTO.setCrmOrgId(0L);
//                        return mappingDTO;
//                    }).collect(Collectors.toList());
//                    flowEnterpriseCustomerMappingApi.sendRefreshCustomerFlowMq(refreshList);
//                    controlMsg="系统将自动刷新【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】的月流向！";
//                    break;
//                case CLOSE:
//                    controlMsg="【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】月流向的客户对照阶段已过期关闭，无法更新！";
//                    break;
//                case MANUAL_CLOSE:
//                    controlMsg="【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】月流向的客户对照阶段已手动关闭，无法更新！";
//                    break;
//                default:
//                    controlMsg="【"+controlDTO.getYear()+"."+controlDTO.getMonth()+"】月流向的客户对照阶段未开启，无法更新！";
//                    break;
//            }
        }
        resultMap.put("monthFlowRefreshMsg",controlMsg);
        return Result.success(resultMap);
    }

    private MappingPageVO<FlowEnterpriseCustomerMappingVO> toPageVO(EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO){
        return toPageVO(esAggregationDTO, 0);
    }

    private MappingPageVO<FlowEnterpriseCustomerMappingVO> toPageVO(EsAggregationDTO<EsFlowEnterpriseCustomerMappingDTO> esAggregationDTO, Integer recommendFlag){
        MappingPageVO<FlowEnterpriseCustomerMappingVO> page = new MappingPageVO<>();
        page.setTotal(esAggregationDTO.getTotal());
        page.setCurrent(esAggregationDTO.getCurrent());
        page.setSize(esAggregationDTO.getSize());
        LinkedList<FlowEnterpriseCustomerMappingVO> pageData = ListUtil.toLinkedList();
        if(CollectionUtil.isNotEmpty(esAggregationDTO.getData())){
            List<Long> idList = esAggregationDTO.getData().stream().map(BaseDTO::getId).collect(Collectors.toList());
            List<FlowEnterpriseCustomerMappingDTO> dtoList = flowEnterpriseCustomerMappingApi.findByIds(idList);
            if(CollectionUtil.isNotEmpty(dtoList)){
                Map<Long, FlowEnterpriseCustomerMappingDTO> dtoMap = dtoList.stream().collect(Collectors.toMap(FlowEnterpriseCustomerMappingDTO::getId, Function.identity()));
                idList.forEach(id->{
                    FlowEnterpriseCustomerMappingDTO mappingDTO = dtoMap.get(id);
                    if(null != mappingDTO){
                        FlowEnterpriseCustomerMappingVO flowEnterpriseCustomerMappingVO = PojoUtils.map(mappingDTO, FlowEnterpriseCustomerMappingVO.class);
                        // 设置经销商省市区
                        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(mappingDTO.getCrmEnterpriseId());
                        if (crmEnterpriseDTO != null) {
                            flowEnterpriseCustomerMappingVO.setProvince(crmEnterpriseDTO.getProvinceName());
                            flowEnterpriseCustomerMappingVO.setCity(crmEnterpriseDTO.getCityName());
                            flowEnterpriseCustomerMappingVO.setRegion(crmEnterpriseDTO.getRegionName());
                        }

                        if (recommendFlag != null && recommendFlag == 1) {
                            setRecommendCustomer(flowEnterpriseCustomerMappingVO, mappingDTO);
                        }

                        pageData.add(flowEnterpriseCustomerMappingVO);
                    }
                });
            }
        }
        page.setRecords(pageData);
        return page;
    }

    private void setRecommendCustomer(FlowEnterpriseCustomerMappingVO flowEnterpriseCustomerMappingVO, FlowEnterpriseCustomerMappingDTO mappingDTO) {
        CrmEnterpriseDTO crmOrgEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(mappingDTO.getRecommendOrgCrmId());
        if (crmOrgEnterpriseDTO != null) {
            // 机构编码
            flowEnterpriseCustomerMappingVO.setCrmOrgId(crmOrgEnterpriseDTO.getId());
            flowEnterpriseCustomerMappingVO.setCustomerProvince(crmOrgEnterpriseDTO.getProvinceName());
            flowEnterpriseCustomerMappingVO.setCustomerCity(crmOrgEnterpriseDTO.getCityName());
            flowEnterpriseCustomerMappingVO.setCustomerRegion(crmOrgEnterpriseDTO.getRegionName());
            flowEnterpriseCustomerMappingVO.setOrgLicenseNumber(crmOrgEnterpriseDTO.getLicenseNumber());
        }
        // 设置推荐度>90列表
        QueryUnlockCustomerMatchingInfoPageRequest request = new QueryUnlockCustomerMatchingInfoPageRequest();
        request.setCurrent(1);
        request.setSize(10);    // 只查10条
        request.setCustomerName(mappingDTO.getFlowCustomerName());
        Page<UnlockCustomerMatchingInfoDTO> unlockCustomerMatchingInfoDTOPage = unlockCustomerMatchingInfoApi.getPageList(request);

        List<Long> crmOrgIdList = unlockCustomerMatchingInfoDTOPage.getRecords().stream().map(UnlockCustomerMatchingInfoDTO::getRecommendCrmId).collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseDTOList = crmEnterpriseApi.getCrmEnterpriseListById(crmOrgIdList);

        List<FlowEnterpriseCustomerMappingVO.RecommendCustomerInfoVO> recommendList = new ArrayList<>();
        for (Long crmOrgId : crmOrgIdList) {
            CrmEnterpriseDTO orgEnterprise = crmEnterpriseDTOList.stream().filter(c -> c.getId().equals(crmOrgId)).findAny().orElse(null);
            FlowEnterpriseCustomerMappingVO.RecommendCustomerInfoVO recommendCustomerInfo = PojoUtils.map(orgEnterprise, FlowEnterpriseCustomerMappingVO.RecommendCustomerInfoVO.class);
            recommendList.add(recommendCustomerInfo);
        }
        flowEnterpriseCustomerMappingVO.setRecommendList(recommendList);
    }
}
