package com.yiling.sjms.crm.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsGroupRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.form.QueryCrmGoodsGroupPageForm;
import com.yiling.sjms.crm.form.SaveOrUpdateCrmDepartProductGroupForm;
import com.yiling.sjms.crm.form.SaveOrUpdateCrmGoodsGroupForm;
import com.yiling.sjms.crm.form.SaveOrUpdateCrmGoodsRelationForm;
import com.yiling.sjms.crm.vo.CrmDepartmentGoodsGroupVO;
import com.yiling.sjms.crm.vo.CrmGoodsGroupRelationVO;
import com.yiling.sjms.crm.vo.CrmGoodsGroupVO;
import com.yiling.sjms.crm.vo.DepartmentVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsGroupController
 * @描述
 * @创建时间 2023/4/11
 * @修改人 shichen
 * @修改时间 2023/4/11
 **/
@Slf4j
@RestController
@RequestMapping("/crmGoodsGroup")
@Api(tags = "商品组管理")
public class CrmGoodsGroupController extends BaseController {
    @DubboReference
    private CrmGoodsGroupApi crmGoodsGroupApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmDepartProductGroupApi crmDepartProductGroupApi;

    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private EsbOrganizationApi esbOrganizationApi;

    @ApiOperation(value = "查询产品组分页", httpMethod = "POST")
    @PostMapping("/queryGroupPage")
    public Result<Page<CrmGoodsGroupVO>> queryGroupPage(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestBody QueryCrmGoodsGroupPageForm form){
        QueryCrmGoodsGroupPageRequest request = PojoUtils.map(form, QueryCrmGoodsGroupPageRequest.class);
        if(StringUtils.isNotBlank(form.getDepartment())){
            List<EsbOrganizationDTO> orgList = esbOrganizationApi.findByOrgName(form.getDepartment(),null);
            if(CollectionUtil.isEmpty(orgList)){
                return Result.success(new Page<>(form.getCurrent(),form.getSize()));
            }
            List<Long> departIds = orgList.stream().map(EsbOrganizationDTO::getOrgId).collect(Collectors.toList());
            request.setDepartIds(departIds);
        }

        Page<CrmGoodsGroupDTO> page = crmGoodsGroupApi.queryGroupPage(request);
        Page<CrmGoodsGroupVO> voPage = PojoUtils.map(page, CrmGoodsGroupVO.class);
        if(CollectionUtil.isNotEmpty(voPage.getRecords())){
            //获取产品组商品信息
            List<Long> groupIds = voPage.getRecords().stream().map(CrmGoodsGroupVO::getId).collect(Collectors.toList());
            Map<Long, List<CrmGoodsGroupRelationDTO>> goodsRelationMap = crmGoodsGroupApi.findGoodsRelationByGroupIds(groupIds);
            List<Long> goodsIds = goodsRelationMap.values().stream().flatMap(Collection::stream).map(CrmGoodsGroupRelationDTO::getGoodsId).distinct().collect(Collectors.toList());
            List<CrmGoodsInfoDTO> goodsList = crmGoodsInfoApi.findByIds(goodsIds);
            Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getId, CrmGoodsInfoDTO::getGoodsName));
            //获取产品组部门信息
            List<CrmDepartmentProductRelationDTO> departRelationList = crmDepartProductGroupApi.getByGroupIds(groupIds);
            List<CrmDepartmentGoodsGroupVO> departRelationVOList = PojoUtils.map(departRelationList, CrmDepartmentGoodsGroupVO.class);
            Map<Long, List<CrmDepartmentGoodsGroupVO>> departRelationMap = departRelationVOList.stream().collect(Collectors.groupingBy(CrmDepartmentGoodsGroupVO::getProductGroupId));

            voPage.getRecords().forEach(group->{
                List<CrmGoodsGroupRelationVO> goodsRelationVOList = PojoUtils.map(goodsRelationMap.getOrDefault(group.getId(), ListUtil.empty()), CrmGoodsGroupRelationVO.class);
                goodsRelationVOList.forEach(relation->{
                    relation.setGoodsName(goodsMap.get(relation.getGoodsId()));
                });
                group.setGoodsRelationList(goodsRelationVOList);
                group.setDepartmentRelationList(departRelationMap.get(group.getId()));
            });
        }
        return Result.success(voPage);
    }


    @ApiOperation(value = "保存产品组", httpMethod = "POST")
    @PostMapping("/saveGroup")
    public Result saveGroup(@CurrentUser CurrentSjmsUserInfo userinfo,@RequestBody SaveOrUpdateCrmGoodsGroupForm form){
        log.info("保存产品组参数，form:{}", JSONUtil.toJsonStr(form));
        if(StringUtils.isBlank(form.getName())){
            return Result.failed("产品组名称为空");
        }
        String checkGoodsMsg = this.checkGoodsRelation(form.getGoodsRelationList());
        if(StringUtils.isNotBlank(checkGoodsMsg)){
            return Result.failed(checkGoodsMsg);
        }
        String checkDepartmentMsg = this.checkDepartmentRelation(form.getDepartmentRelationList(),null,form.getName());
        if(StringUtils.isNotBlank(checkDepartmentMsg)){
            return Result.failed(checkGoodsMsg);
        }
        SaveOrUpdateCrmGoodsGroupRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsGroupRequest.class);
        request.setId(null);
        request.setOpUserId(userinfo.getCurrentUserId());
        crmGoodsGroupApi.saveGroup(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑产品组", httpMethod = "POST")
    @PostMapping("/editGroup")
    public Result editGroup(@CurrentUser CurrentSjmsUserInfo userinfo,@RequestBody SaveOrUpdateCrmGoodsGroupForm form){
        log.info("编辑产品组参数，form:{}", JSONUtil.toJsonStr(form));
        if(StringUtils.isBlank(form.getName())){
            return Result.failed("产品组名称为空");
        }
        if(null== form.getId() || form.getId()==0){
            return Result.failed("产品组id为空");
        }
        String checkGoodsMsg = this.checkGoodsRelation(form.getGoodsRelationList());
        if(StringUtils.isNotBlank(checkGoodsMsg)){
            return Result.failed(checkGoodsMsg);
        }
        String checkDepartmentMsg = this.checkDepartmentRelation(form.getDepartmentRelationList(),form.getId(),form.getName());
        if(StringUtils.isNotBlank(checkDepartmentMsg)){
            return Result.failed(checkDepartmentMsg);
        }
        SaveOrUpdateCrmGoodsGroupRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsGroupRequest.class);
        request.setOpUserId(userinfo.getCurrentUserId());
        crmGoodsGroupApi.editGroup(request);
        return Result.success();
    }

    @ApiOperation(value = "部门编码获取部门接口", httpMethod = "GET")
    @GetMapping("/getDepartByDepartId")
    public Result<DepartmentVO> getDepartmentByDepartId(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestParam("departId")Long departId){
        if(null == departId){
            return Result.failed("部门编码为空");
        }
        EsbOrganizationDTO orgDTO = esbOrganizationApi.getByOrgId(departId);
        return Result.success(PojoUtils.map(orgDTO,DepartmentVO.class));
    }
    @ApiOperation(value = "获取产品组详情", httpMethod = "GET")
    @GetMapping("/getGroupDetail")
    public Result<CrmGoodsGroupVO> getGroupDetail(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestParam("groupId")Long groupId){
        CrmGoodsGroupDTO group = crmGoodsGroupApi.findGroupById(groupId);
        if(null==group){
            return Result.failed("产品组不存在");
        }
        CrmGoodsGroupVO groupVO = PojoUtils.map(group, CrmGoodsGroupVO.class);
        List<CrmGoodsGroupRelationVO> goodsRelationVOList = PojoUtils.map(crmGoodsGroupApi.findGoodsRelationByGroupId(groupId), CrmGoodsGroupRelationVO.class);
        if(CollectionUtil.isNotEmpty(goodsRelationVOList)){
            List<Long> goodsIds = goodsRelationVOList.stream().map(CrmGoodsGroupRelationVO::getGoodsId).distinct().collect(Collectors.toList());
            List<CrmGoodsInfoDTO> goodsList = crmGoodsInfoApi.findByIds(goodsIds);
            Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getId, CrmGoodsInfoDTO::getGoodsName));
            goodsRelationVOList.forEach(goodsRelation->{
                goodsRelation.setGoodsName(goodsMap.get(goodsRelation.getGoodsId()));
            });
        }
        //获取关联部门并判断是否有数据引用
        List<CrmDepartmentGoodsGroupVO> departmentRelationVOList = PojoUtils.map(crmDepartProductGroupApi.getByGroupId(groupId), CrmDepartmentGoodsGroupVO.class);
//        if(CollectionUtil.isNotEmpty(departmentRelationVOList)){
//            List<Long> departRelationIds = departmentRelationVOList.stream().map(CrmDepartmentGoodsGroupVO::getId).distinct().collect(Collectors.toList());
//            Map<Long, Boolean> useMap = crmEnterpriseRelationShipApi.getUseByDepartRelationIds(departRelationIds);
//            departmentRelationVOList.forEach(departRelation->{
//                departRelation.setIsDataUse(useMap.getOrDefault(departRelation.getId(),false));
//            });
//        }
        departmentRelationVOList.forEach(departRelation->{
            departRelation.setIsDataUse(false);
        });
        groupVO.setDepartmentRelationList(departmentRelationVOList);
        groupVO.setGoodsRelationList(goodsRelationVOList);
        return Result.success(groupVO);
    }

    private String checkGoodsRelation(List<SaveOrUpdateCrmGoodsRelationForm> goodsRelationList){
        String checkMsg = "";
        if(CollectionUtil.isNotEmpty(goodsRelationList)){
            List<Long> goodsCodeList = goodsRelationList.stream().map(SaveOrUpdateCrmGoodsRelationForm::getGoodsCode).distinct().collect(Collectors.toList());
            if(goodsRelationList.size()!=goodsCodeList.size()){
                return "存在重复商品编码";
            }
            List<CrmGoodsInfoDTO> goodsList = crmGoodsInfoApi.findByCodeList(goodsCodeList);
            Map<Long, CrmGoodsInfoDTO> goodsMap = goodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity(),(e1,e2)->e1));
            List<Long> notExistsGoodsCode = ListUtil.toList();
            goodsRelationList.forEach(goodsRelation->{
                CrmGoodsInfoDTO goodsInfo = goodsMap.get(goodsRelation.getGoodsCode());
                if(null == goodsInfo){
                    notExistsGoodsCode.add(goodsRelation.getGoodsCode());
                }else {
                    goodsRelation.setGoodsId(goodsInfo.getId());
                }
            });
            if(CollectionUtil.isNotEmpty(notExistsGoodsCode)){
                String codeMsg = StringUtils.join(notExistsGoodsCode, ",");
                checkMsg="商品编码不存在商品，商品编码："+codeMsg;
            }
        }
        return checkMsg;
    }
    private String checkDepartmentRelation(List<SaveOrUpdateCrmDepartProductGroupForm> departmentRelationList,Long groupId,String groupName){
        String checkMsg = "";
        if(CollectionUtil.isNotEmpty(departmentRelationList)){
            List<Long> departIds = departmentRelationList.stream().map(SaveOrUpdateCrmDepartProductGroupForm::getDepartmentId).distinct().collect(Collectors.toList());
            if(departmentRelationList.size() != departIds.size()){
                return "存在重复部门编码";
            }
            //部门数据封装
            List<EsbOrganizationDTO> orgList = esbOrganizationApi.listByOrgIds(departIds);
            Map<Long, EsbOrganizationDTO> orgMap = orgList.stream().collect(Collectors.toMap(EsbOrganizationDTO::getOrgId, Function.identity()));
            for(SaveOrUpdateCrmDepartProductGroupForm form:departmentRelationList){
                EsbOrganizationDTO org = orgMap.get(form.getDepartmentId());
                if(null==org){
                    return "部门编码："+form.getDepartmentId()+"不是有效部门";
                }
                form.setDepartment(org.getOrgName());
                form.setProductGroupId(groupId);
                form.setProductGroup(groupName);
            }
        }
        return checkMsg;
    }
}
