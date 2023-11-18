package com.yiling.sjms.flow.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
import com.yiling.dataflow.flow.api.FlowEnterpriseSupplierMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowEnterpriseSupplierMappingPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flow.form.BatchSaveSupplierMappingForm;
import com.yiling.sjms.flow.form.QueryFlowEnterpriseSupplierMappingPageForm;
import com.yiling.sjms.flow.form.SaveFlowEnterpriseSupplierMappingForm;
import com.yiling.sjms.flow.vo.FlowMonthWashControlVO;
import com.yiling.sjms.flow.vo.MappingPageVO;
import com.yiling.sjms.flow.vo.FlowEnterpriseSupplierMappingVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingController
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Slf4j
@RestController
@RequestMapping("/flowEnterpriseSupplierMapping")
@Api(tags = "供应商对照关系管理")
public class FlowEnterpriseSupplierMappingController extends BaseController {

    @DubboReference
    FlowEnterpriseSupplierMappingApi flowEnterpriseSupplierMappingApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference
    private UserApi userApi;

    @ApiOperation(value = "供应商对照查询", httpMethod = "POST")
    @PostMapping("/querySupplierMappingPage")
    public Result<MappingPageVO<FlowEnterpriseSupplierMappingVO>> querySupplierMappingPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseSupplierMappingPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("供应商对照查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
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
        QueryFlowEnterpriseSupplierMappingPageRequest request = PojoUtils.map(form, QueryFlowEnterpriseSupplierMappingPageRequest.class);
        request.setUserDatascopeBO(userDatascopeBO);
        Page<FlowEnterpriseSupplierMappingDTO> page = flowEnterpriseSupplierMappingApi.pageList(request);
        MappingPageVO<FlowEnterpriseSupplierMappingVO> voPage = new MappingPageVO<>();
        voPage.setTotal(page.getTotal());
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());

        List<FlowEnterpriseSupplierMappingVO> voList = ListUtil.toLinkedList();

        if(CollectionUtil.isNotEmpty(page.getRecords())){
            List<Long> updateUserIds = page.getRecords().stream().map(FlowEnterpriseSupplierMappingDTO::getUpdateUser).filter(userId -> userId > 0).distinct().collect(Collectors.toList());
            List<UserDTO> userList = ListUtil.empty();
            if(CollectionUtil.isNotEmpty(updateUserIds)){
                userList = userApi.listByIds(updateUserIds);
            }
            Map<Long, String> userMap = userList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            page.getRecords().forEach(mappingDTO->{
                FlowEnterpriseSupplierMappingVO mappingVO = PojoUtils.map(mappingDTO, FlowEnterpriseSupplierMappingVO.class);
                //操作人赋值
                mappingVO.setOperator(userMap.getOrDefault(mappingDTO.getUpdateUser(), "admin"));
                voList.add(mappingVO);
            });
        }
        voPage.setRecords(voList);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null != controlDTO){
            voPage.setSupplierMappingWashControl(PojoUtils.map(controlDTO, FlowMonthWashControlVO.class));
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "供应商未对照查询", httpMethod = "POST")
    @PostMapping("/querySupplierUnmappedPage")
    public Result<MappingPageVO<FlowEnterpriseSupplierMappingVO>> querySupplierUnmappedPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseSupplierMappingPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("供应商未对照查询权限:userDatascopeBO={}",JSONUtil.toJsonStr(userDatascopeBO));
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
        QueryFlowEnterpriseSupplierMappingPageRequest request = PojoUtils.map(form, QueryFlowEnterpriseSupplierMappingPageRequest.class);
        request.setOrderByUploadTime(true);
        request.setCrmOrgId(0L);
        request.setUserDatascopeBO(userDatascopeBO);
        Page<FlowEnterpriseSupplierMappingDTO> page = flowEnterpriseSupplierMappingApi.pageList(request);
        MappingPageVO<FlowEnterpriseSupplierMappingVO> voPage = new MappingPageVO<>();
        voPage.setTotal(page.getTotal());
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());
        voPage.setRecords(PojoUtils.map(page.getRecords(),FlowEnterpriseSupplierMappingVO.class));
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null != controlDTO){
            voPage.setSupplierMappingWashControl(PojoUtils.map(controlDTO, FlowMonthWashControlVO.class));
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "供应商对照删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam("id")Long id){
        FlowEnterpriseSupplierMappingDTO supplierMappingDTO = flowEnterpriseSupplierMappingApi.findById(id);
        if(null == supplierMappingDTO){
            return Result.failed("删除的数据不存在");
        }
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null == controlDTO){
            return Result.failed("月流向清洗未开启,操作失败！");
        }
        Boolean b = flowEnterpriseSupplierMappingApi.deleteById(id, userInfo.getCurrentUserId());
        if(b){
            Date currentDate = new Date();
            Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
            int lastMonth=DateUtil.month(lastMonthTime)+1;
            int lastYear = DateUtil.year(lastMonthTime);
            FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
            if(null!=lastMonthControlDTO && 2==lastMonthControlDTO.getWashStatus()){
                //发送删除前数值给上月月流向刷新
                flowEnterpriseSupplierMappingApi.sendRefreshSupplierFlowMq(ListUtil.toList(supplierMappingDTO));
            }
            return Result.success();
        }else {
            return Result.failed("删除失败");
        }
    }

    @ApiOperation(value = "供应商对照编辑", httpMethod = "POST")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid SaveFlowEnterpriseSupplierMappingForm form){
        FlowEnterpriseSupplierMappingDTO supplierMappingDTO = flowEnterpriseSupplierMappingApi.findById(form.getId());
        if(null == supplierMappingDTO){
            return Result.failed("供应商对照关系不存在");
        }
        if(supplierMappingDTO.getCrmEnterpriseId().equals(form.getCrmOrgId())){
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
        SaveFlowEnterpriseSupplierMappingRequest request = PojoUtils.map(form,SaveFlowEnterpriseSupplierMappingRequest.class);
        request.setOrgLicenseNumber(crmEnterpriseDTO.getLicenseNumber());
        flowEnterpriseSupplierMappingApi.save(request);
        Date currentDate = new Date();
        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth=DateUtil.month(lastMonthTime)+1;
        int lastYear = DateUtil.year(lastMonthTime);
        FlowMonthWashControlDTO lastMonthControlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        if(null!=lastMonthControlDTO && 2==lastMonthControlDTO.getWashStatus()){
            //发送更新前数值给上月月流向刷新
            flowEnterpriseSupplierMappingApi.sendRefreshSupplierFlowMq(ListUtil.toList(supplierMappingDTO));
        }
        return Result.success();
    }

    @ApiOperation(value = "供应商对照批量保存编辑", httpMethod = "POST")
    @PostMapping("/batchSave")
    public Result<Map<String, String>> batchSave(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid BatchSaveSupplierMappingForm form){
        if(CollectionUtil.isEmpty(form.getSaveList())){
            return Result.failed("提交更新数据为空，提交失败！");
        }
        List<SaveFlowEnterpriseSupplierMappingRequest> requestList = PojoUtils.map(form.getSaveList(), SaveFlowEnterpriseSupplierMappingRequest.class);
        List<Long> crmEnterpriseIdList = requestList.stream().map(SaveFlowEnterpriseSupplierMappingRequest::getCrmOrgId).collect(Collectors.toList());
        List<CrmEnterpriseDTO> orgList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterpriseIdList);
        Map<Long, CrmEnterpriseDTO> enterpriseMap = orgList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));

        List<Long> idList = requestList.stream().filter(request->null != request.getId() && request.getId()>0).map(SaveFlowEnterpriseSupplierMappingRequest::getId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(idList) || idList.size()!=requestList.size()){
            return Result.failed("提交数据异常，提交失败！");
        }
        List<FlowEnterpriseSupplierMappingDTO> mappingList = flowEnterpriseSupplierMappingApi.findByIds(idList);
        Map<Long, FlowEnterpriseSupplierMappingDTO> mappingMap = mappingList.stream().collect(Collectors.toMap(FlowEnterpriseSupplierMappingDTO::getId, Function.identity()));

        for (SaveFlowEnterpriseSupplierMappingRequest request:requestList){
            if(null==request.getCrmOrgId() || request.getCrmOrgId()<=0){
                return Result.failed("提交数据异常，提交失败！");
            }
            FlowEnterpriseSupplierMappingDTO mappingDTO = mappingMap.get(request.getId());
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
        Boolean b = flowEnterpriseSupplierMappingApi.batchUpdateById(requestList);
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
        String controlMsg="【"+lastYear+"."+lastMonth+"】月流向的供应商对照清洗日程不存在，无法更新！";
        if(null!=controlDTO){
            if(controlDTO.getWashStatus()==2){
                List<FlowEnterpriseSupplierMappingDTO> refreshList = requestList.stream().map(request -> {
                    FlowEnterpriseSupplierMappingDTO mappingDTO = new FlowEnterpriseSupplierMappingDTO();
                    mappingDTO.setId(request.getId());
                    mappingDTO.setCrmOrgId(0L);
                    return mappingDTO;
                }).collect(Collectors.toList());
                flowEnterpriseSupplierMappingApi.sendRefreshSupplierFlowMq(refreshList);
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

}
