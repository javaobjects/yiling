package com.yiling.sjms.flow.controller;


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
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.flow.api.FlowPurchaseChannelApi;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.JsonUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flow.form.QueryFlowPurchaseChannelPageForm;
import com.yiling.sjms.flow.form.SaveFlowPurchaseChannelForm;
import com.yiling.sjms.flow.vo.FlowPurchaseChannelVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelController
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Slf4j
@RestController
@RequestMapping("/flowPurchaseChannel")
@Api(tags = "采购渠道管理")
public class FlowPurchaseChannelController extends BaseController {

    @DubboReference
    private FlowPurchaseChannelApi flowPurchaseChannelApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "采购渠道查询", httpMethod = "POST")
    @PostMapping("/page")
    public Result<Page<FlowPurchaseChannelVO>> page(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowPurchaseChannelPageForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("采购渠道查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.success(new Page());
        }
        QueryFlowPurchaseChannelRequest request = PojoUtils.map(form, QueryFlowPurchaseChannelRequest.class);
        request.setUserDatascopeBO(userDatascopeBO);
        Page<FlowPurchaseChannelDTO> page = flowPurchaseChannelApi.pageList(request);
        return Result.success(PojoUtils.map(page,FlowPurchaseChannelVO.class));
    }

    @ApiOperation(value = "采购渠道删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam("id")Long id ){
        FlowPurchaseChannelDTO purchaseChannelDTO = flowPurchaseChannelApi.findById(id);
        if(null == purchaseChannelDTO){
            return Result.failed("删除的数据不存在");
        }
        Boolean b = flowPurchaseChannelApi.deleteById(id, userInfo.getCurrentUserId());
        if(b){
            return Result.success();
        }else {
            return Result.failed("删除失败");
        }
    }

    @ApiOperation(value = "采购渠道编辑", httpMethod = "POST")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowPurchaseChannelForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("采购渠道编辑权限:userDatascopeBO={}",JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.failed("没有编辑采购渠道权限");
        }
        FlowPurchaseChannelDTO channelDTO = flowPurchaseChannelApi.findById(form.getId());
        if(null == channelDTO){
            return Result.failed("采购渠道不存在");
        }
        CrmEnterpriseDTO orgDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmOrgId());
        if(null==orgDTO){
            return Result.failed("机构编码未找到对应标准机构");
        }
        if(OrgDatascopeEnum.PORTION.getCode().equals(userDatascopeBO.getOrgDatascope())){
            if(!userDatascopeBO.getOrgPartDatascopeBO().getCrmEids().contains(orgDTO.getId())
                && !userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes().contains(orgDTO.getProvinceCode())) {
                return Result.failed("没有编辑机构编码:" + orgDTO.getId() + "的权限");
            }
        }
        CrmEnterpriseDTO purchaseOrgDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmPurchaseOrgId());
        if(null==purchaseOrgDTO){
            return Result.failed("采购渠道机构编码未找到对应标准机构");
        }
        if(!CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode().equals(purchaseOrgDTO.getSupplyChainRole())){
            return Result.failed("采购渠道机构不是商业公司档案");
        }
        if(form.getCrmOrgId().equals(form.getCrmPurchaseOrgId())){
            return Result.failed("机构编码和采购渠道机构编码不能相同");
        }
        FlowPurchaseChannelDTO purchaseChannelDTO = flowPurchaseChannelApi.findByOrgIdAndPurchaseOrgId(form.getCrmOrgId(), form.getCrmPurchaseOrgId());
        if(null!=purchaseChannelDTO && !channelDTO.getId().equals(purchaseChannelDTO.getId())){
            return Result.failed("机构编码和采购渠道机构编码已经存在采购渠道");
        }
        SaveFlowPurchaseChannelRequest request = PojoUtils.map(form, SaveFlowPurchaseChannelRequest.class);
        request.setOrgName(orgDTO.getName());
        request.setPurchaseOrgName(purchaseOrgDTO.getName());
        request.setProvince(orgDTO.getProvinceName());
        request.setCity(orgDTO.getCityName());
        request.setRegion(orgDTO.getRegionName());
        request.setProvinceCode(orgDTO.getProvinceCode());
        request.setCityCode(orgDTO.getCityCode());
        request.setRegionCode(orgDTO.getRegionCode());
        request.setOpUserId(userInfo.getCurrentUserId());
        flowPurchaseChannelApi.save(request);
        return Result.success();
    }

    @ApiOperation(value = "采购渠道保存", httpMethod = "POST")
    @PostMapping("/save")
    public Result save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowPurchaseChannelForm form){
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("采购渠道保存权限:userDatascopeBO={}",JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if(null==userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())){
            return Result.failed("没有保存采购渠道权限");
        }
        form.setId(null);
        CrmEnterpriseDTO orgDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmOrgId());
        if(null==orgDTO){
            return Result.failed("机构编码未找到对应标准机构");
        }
        if(OrgDatascopeEnum.PORTION.getCode().equals(userDatascopeBO.getOrgDatascope())){
            if(!userDatascopeBO.getOrgPartDatascopeBO().getCrmEids().contains(orgDTO.getId())
                    && !userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes().contains(orgDTO.getProvinceCode())) {
                return Result.failed("没有新增机构编码:" + orgDTO.getId() + "的权限");
            }
        }
        CrmEnterpriseDTO purchaseOrgDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmPurchaseOrgId());
        if(null==purchaseOrgDTO){
            return Result.failed("采购渠道机构编码未找到对应标准机构");
        }
        if(!CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode().equals(purchaseOrgDTO.getSupplyChainRole())){
            return Result.failed("采购渠道机构不是商业公司档案");
        }
        if(form.getCrmOrgId().equals(form.getCrmPurchaseOrgId())){
            return Result.failed("机构编码和采购渠道机构编码不能相同");
        }
        FlowPurchaseChannelDTO purchaseChannelDTO = flowPurchaseChannelApi.findByOrgIdAndPurchaseOrgId(form.getCrmOrgId(), form.getCrmPurchaseOrgId());
        if(null!=purchaseChannelDTO){
            return Result.failed("机构编码和采购渠道机构编码已经存在采购渠道");
        }
        SaveFlowPurchaseChannelRequest request = PojoUtils.map(form, SaveFlowPurchaseChannelRequest.class);
        request.setOrgName(orgDTO.getName());
        request.setPurchaseOrgName(purchaseOrgDTO.getName());
        request.setProvince(orgDTO.getProvinceName());
        request.setCity(orgDTO.getCityName());
        request.setRegion(orgDTO.getRegionName());
        request.setProvinceCode(orgDTO.getProvinceCode());
        request.setCityCode(orgDTO.getCityCode());
        request.setRegionCode(orgDTO.getRegionCode());
        request.setOpUserId(userInfo.getCurrentUserId());
        flowPurchaseChannelApi.save(request);
        return Result.success();
    }
}
