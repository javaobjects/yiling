package com.yiling.sjms.manor.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import com.google.common.collect.Lists;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationManorApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.bo.CrmRelationManorBO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sjms.agency.form.SubmitAgencyExtendChangeForm;
import com.yiling.sjms.agency.form.UpdateArchiveStatusForm;
import com.yiling.sjms.manor.api.ManorChangeApi;
import com.yiling.sjms.manor.bo.ManorChangeBO;
import com.yiling.sjms.manor.dto.HospitalManorChangeFormDTO;
import com.yiling.sjms.manor.dto.request.DeleteManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.ManorChangeFormRequest;
import com.yiling.sjms.manor.dto.request.QueryChangePageRequest;
import com.yiling.sjms.manor.dto.request.UpdateArchiveRequest;
import com.yiling.sjms.manor.form.ManorChangeForm;
import com.yiling.sjms.manor.form.QueryChangePageForm;
import com.yiling.sjms.manor.vo.EnterpriseRelationManorVO;
import com.yiling.sjms.manor.vo.ManorChangeFormVO;
import com.yiling.sjms.manor.vo.ManorChangeVO;
import com.yiling.sjms.manor.vo.ManorVO;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 医院辖区变更
 * @author: gxl
 * @date: 2023/5/12
 */
@Slf4j
@RestController
@RequestMapping("/manor/change")
@Api(tags = "医院辖区变更")
public class ManorChangeController extends BaseController {
    @DubboReference
    ManorChangeApi manorChangeApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    CrmEnterpriseRelationManorApi crmEnterpriseRelationManorApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;
    @DubboReference
    private CrmManorApi crmManorApi;

    @ApiOperation(value = "新增或编辑")
    @PostMapping("save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid ManorChangeForm form){
        ManorChangeFormRequest request = PojoUtils.map(form,ManorChangeFormRequest.class);
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setOpUserId(userInfo.getCurrentUserId());
        return Result.success(manorChangeApi.save(request));
    }

    @ApiOperation(value = "分页列表")
    @GetMapping(value = "queryPage")
    public Result<Page<ManorChangeFormVO>> listPage(@Valid QueryChangePageForm form){
        QueryChangePageRequest request = new QueryChangePageRequest();
        request.setFormId(form.getFormId());
        Page<HospitalManorChangeFormDTO> hospitalManorChangeFormDTOPage = manorChangeApi.listPage(request);
        Page<ManorChangeFormVO> manorChangeFormVOPage = PojoUtils.map(hospitalManorChangeFormDTOPage,ManorChangeFormVO.class);
         return Result.success(manorChangeFormVOPage);
    }

    @ApiOperation(value = "删除单个单据")
    @GetMapping(value = "deleteOne")
    public Result<Boolean> delete(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam Long id){
        DeleteManorChangeFormRequest request = new DeleteManorChangeFormRequest();
        request.setId(id).setOpUserId(userInfo.getCurrentUserId());
        manorChangeApi.deleteById(request);
        return Result.success(true);
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid SubmitAgencyExtendChangeForm form){
        SubmitFormBaseRequest request = new SubmitFormBaseRequest();
        request.setId(form.getFormId()).setEmpId(userInfo.getCurrentUserCode()).setOpUserId(userInfo.getCurrentUserId());
        manorChangeApi.submit(request);
        return Result.success(true);
    }

    @ApiOperation(value = "修改归档状态")
    @PostMapping(value = "/updateArchiveStatus")
    public Result<Boolean> updateArchiveStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateArchiveStatusForm form) {
        UpdateArchiveRequest request = new UpdateArchiveRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(form.getId());
        request.setArchiveStatus(form.getArchiveStatus());
        manorChangeApi.updateArchiveStatusById(request);
        return Result.success(true);
    }

    @ApiOperation(value = "根据所选机构id查询绑定的品种和辖区")
    @GetMapping("queryManorRelList")
    public Result<CollectionObject<EnterpriseRelationManorVO>>  queryManorRelList(Long crmEnterpriseId){
        List<CrmRelationManorBO> crmEnterpriseRelationManorDTOS = crmEnterpriseRelationManorApi.queryList(crmEnterpriseId);
        if(CollUtil.isEmpty(crmEnterpriseRelationManorDTOS)){
            return Result.success(new CollectionObject<>(null));
        }
        List<Long> categoryIds =crmEnterpriseRelationManorDTOS.stream().map(CrmRelationManorBO::getCategoryId).distinct().collect(Collectors.toList());
        List<CrmGoodsCategoryDTO> goodsCategoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
        //品种信息
        Map<Long, CrmGoodsCategoryDTO> goodsCategoryDTOMap = goodsCategoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, Function.identity()));
        List<EnterpriseRelationManorVO> enterpriseRelationManorVOS = Lists.newArrayListWithExpectedSize(crmEnterpriseRelationManorDTOS.size());
        crmEnterpriseRelationManorDTOS.forEach(manor->{
            EnterpriseRelationManorVO manorVO = new EnterpriseRelationManorVO();
            CrmGoodsCategoryDTO crmGoodsCategoryDTO = goodsCategoryDTOMap.get(manor.getCategoryId());
            if(Objects.nonNull(crmGoodsCategoryDTO)){
                manorVO.setCategoryName(crmGoodsCategoryDTO.getName());
            }else{
                manorVO.setCategoryName("");
            }
            manorVO.setManorName(manor.getManorName()).setManorNo(manor.getManorNo()).setManorId(manor.getCrmManorId())
            .setCategoryId(manor.getCategoryId());
            enterpriseRelationManorVOS.add(manorVO);
        });
        return Result.success(new CollectionObject<>(enterpriseRelationManorVOS));
    }
    @ApiOperation(value = "根据辖区名称搜索辖区信息")
    @GetMapping("queryManorList")
    public Result<CollectionObject<ManorVO>> queryManorList(String name){
        QueryCrmManorPageRequest request = new QueryCrmManorPageRequest();
        request.setName(name).setSize(50);
        Page<CrmManorDTO> crmManorDTOPage = crmManorApi.pageList(request);
        List<ManorVO> manorVOS = PojoUtils.map(crmManorDTOPage.getRecords(), ManorVO.class);
        return Result.success(new CollectionObject(manorVOS));
    }


    @ApiOperation(value = "编辑反显")
    @GetMapping("getDetail")
    public Result<ManorChangeVO> getDetail(@ApiParam(value = "辖区变更明细id")@RequestParam Long id){
        ManorChangeBO manorChangeBO = manorChangeApi.queryByFormId(id);
        return Result.success(PojoUtils.map(manorChangeBO,ManorChangeVO.class));
    }
}