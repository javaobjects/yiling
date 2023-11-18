package com.yiling.f2b.admin.procrelation.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yiling.f2b.admin.procrelation.form.AddAllGoodsToTemplateForm;
import com.yiling.f2b.admin.procrelation.form.AddGoodsToTemplateForm;
import com.yiling.f2b.admin.procrelation.form.DeleteProcTemplateForm;
import com.yiling.f2b.admin.procrelation.form.QueryProcTemplateGoodsOptionalPageForm;
import com.yiling.f2b.admin.procrelation.form.QueryTemplateGoodsListForm;
import com.yiling.f2b.admin.procrelation.form.QueryTemplatePageForm;
import com.yiling.f2b.admin.procrelation.form.SaveProcTemplateForm;
import com.yiling.f2b.admin.procrelation.form.UpdateTemplateGoodsRebateForm;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationGoodsListItemVO;
import com.yiling.f2b.admin.procrelation.vo.ProcTemplateGoodsListItemVO;
import com.yiling.f2b.admin.procrelation.vo.ProcTemplatePageListItemVO;
import com.yiling.f2b.admin.procrelation.vo.TemplateGoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.procrelation.api.PopProcGoodsTemplateApi;
import com.yiling.user.procrelation.api.PopProcTemplateApi;
import com.yiling.user.procrelation.dto.PopProcGoodsTemplateDTO;
import com.yiling.user.procrelation.dto.PopProcTemplateDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsToTemplateRequest;
import com.yiling.user.procrelation.dto.request.QueryProcTemplatePageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcTemplateRequest;
import com.yiling.user.procrelation.dto.request.UpdateGoodsRebateRequest;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@RestController
@RequestMapping("/procurementImport")
@Api(tags = "建采管理导入相关")
@Slf4j
public class ProcurementTemplateController extends BaseController {

    @DubboReference
    PopProcTemplateApi popProcTemplateApi;
    @DubboReference
    PopProcGoodsTemplateApi popProcGoodsTemplateApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;

    @ApiOperation(value = "分页查询模板列表")
    @PostMapping("/queryTemplatePage")
    public Result<Page<ProcTemplatePageListItemVO>> queryTemplatePage(@RequestBody @Valid QueryTemplatePageForm form) {
        QueryProcTemplatePageRequest request = PojoUtils.map(form, QueryProcTemplatePageRequest.class);
        //查询用户
        if (StrUtil.isNotBlank(form.getOperationUser())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameLike(form.getOperationUser());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if (CollUtil.isEmpty(staffList)) {
                return Result.success(form.getPage());
            }
            List<Long> userList = staffList.stream().map(Staff::getId).distinct().collect(Collectors.toList());
            request.setUserIdList(userList);

        }
        Page<PopProcTemplateDTO> page = popProcTemplateApi.queryPageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<ProcTemplatePageListItemVO> result = PojoUtils.map(page, ProcTemplatePageListItemVO.class);

        List<Long> userIds = page.getRecords().stream().map(PopProcTemplateDTO::getUpdateUser).collect(Collectors.toList());
        List<UserDTO> list = userApi.listByIds(new ArrayList<>(userIds));
        Map<Long, String> userMap = list.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        result.getRecords().forEach(e -> {
            e.setUpdateUserStr(userMap.getOrDefault(e.getUpdateUser(), ""));
        });

        return Result.success(result);
    }

    @ApiOperation(value = "查询模板信息")
    @GetMapping("/queryTemplateInfo")
    public Result<ProcTemplatePageListItemVO> queryTemplateInfo(@RequestParam Long id) {
        PopProcTemplateDTO templateDTO = popProcTemplateApi.queryTemplateById(id);
        if (templateDTO == null) {
            return Result.success();
        }
        List<UserDTO> list = userApi.listByIds(ListUtil.toList(templateDTO.getCreateUser(), templateDTO.getUpdateUser()));
        Map<Long, String> userMap = list.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        ProcTemplatePageListItemVO result = PojoUtils.map(templateDTO, ProcTemplatePageListItemVO.class);
        result.setUpdateUserStr(userMap.getOrDefault(templateDTO.getUpdateUser(), ""));
        return Result.success(result);
    }

    @ApiOperation(value = "保存或更新模板")
    @PostMapping("/saveTemplate")
    public Result<Long> saveTemplate(@RequestBody @Valid SaveProcTemplateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        SaveProcTemplateRequest request = PojoUtils.map(form, SaveProcTemplateRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Long templateId = popProcTemplateApi.saveProcTemplate(request);
        return Result.success(templateId);
    }


    @ApiOperation(value = "删除模板")
    @PostMapping("/deleteTemplate")
    public Result<Boolean> deleteTemplate(@RequestBody @Valid DeleteProcTemplateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        PopProcTemplateDTO templateDTO = popProcTemplateApi.queryTemplateById(form.getId());
        if (templateDTO == null) {
            return Result.failed("模板不存在");
        }
        Boolean isSuccess = popProcTemplateApi.deleteById(form.getId(), staffInfo.getCurrentUserId());
        return Result.success(isSuccess);
    }


    @ApiOperation(value = "查询工业主体")
    @GetMapping("/mainPart/list")
    public Result<List<SimpleEnterpriseVO>> getMainPartList() {
        List<EnterpriseDTO> list = enterpriseApi.listMainPart();
        return Result.success(PojoUtils.map(list, SimpleEnterpriseVO.class));
    }


    @ApiOperation(value = "根据采购模板id查询可采商品列表")
    @PostMapping("/queryProcTempalteGoodsOptionalPage")
    public Result<Page<ProcTemplateGoodsListItemVO>> queryProcTempalteGoodsOptionalPage(@RequestBody @Valid QueryProcTemplateGoodsOptionalPageForm form) {
        PopProcTemplateDTO template = popProcTemplateApi.queryTemplateById(form.getTemplateId());
        if (ObjectUtil.isNull(template)) {
            return Result.failed("模板不存在");
        }
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(ListUtil.toList(form.getFactoryEid()));

        Page<ProcTemplateGoodsListItemVO> page = PojoUtils.map(popGoodsApi.queryPopGoodsPageList(request), ProcTemplateGoodsListItemVO.class);
        List<PopProcGoodsTemplateDTO> procGoodsTemplateList = popProcGoodsTemplateApi.queryGoodsList(form.getTemplateId(), form.getFactoryEid());
        if (CollUtil.isEmpty(procGoodsTemplateList)) {
            return Result.success(page);
        }

        List<Long> procRelationGoodsIdList = procGoodsTemplateList.stream().map(PopProcGoodsTemplateDTO::getGoodsId).collect(Collectors.toList());
        page.getRecords().forEach(e -> {
            //判断商品是否已经被选了
            if (procRelationGoodsIdList.contains(e.getId())) {
                e.setGoodsSelStatus(true);
            }
        });
        return Result.success(page);
    }


    @ApiOperation(value = "向模板中添加商品")
    @PostMapping("/addGoodsToTemplate")
    public Result<Boolean> addGoodsToTemplate(@RequestBody @Valid AddGoodsToTemplateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        PopProcTemplateDTO template = popProcTemplateApi.queryTemplateById(form.getTemplateId());
        if (ObjectUtil.isNull(template)) {
            return Result.failed("模板不存在");
        }
        AddGoodsToTemplateRequest request = PojoUtils.map(form, AddGoodsToTemplateRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean isSuccess = popProcGoodsTemplateApi.addGoodsToTemplate(template.getId(), ListUtil.toList(request));
        return Result.success(isSuccess);
    }


    @ApiOperation(value = "根据采购模板id添加全部可采商品列表")
    @PostMapping("/addAllGoodsToTemplate")
    public Result<Boolean> addAllGoodsToTemplate(@RequestBody @Valid AddAllGoodsToTemplateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        PopProcTemplateDTO template = popProcTemplateApi.queryTemplateById(form.getTemplateId());
        if (ObjectUtil.isNull(template)) {
            return Result.failed("模板不存在");
        }
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(ListUtil.toList(form.getFactoryEid()));

        List<ProcRelationGoodsListItemVO> list = PojoUtils.map(popGoodsApi.queryPopGoodsList(request), ProcRelationGoodsListItemVO.class);
        List<PopProcGoodsTemplateDTO> procRelationGoodsList = popProcGoodsTemplateApi.queryGoodsList(form.getTemplateId(), form.getFactoryEid());

        List<Long> procRelationGoodsIdList = procRelationGoodsList.stream().map(PopProcGoodsTemplateDTO::getGoodsId).collect(Collectors.toList());
        //仅添加可选的商品
        List<ProcRelationGoodsListItemVO> optionalGoods = list.stream().filter(e -> !procRelationGoodsIdList.contains(e.getId())).collect(Collectors.toList());

        List<AddGoodsToTemplateRequest> goodsList = ListUtil.toList();
        optionalGoods.forEach(e -> {
            AddGoodsToTemplateRequest var = PojoUtils.map(e, AddGoodsToTemplateRequest.class);
            var.setTemplateId(template.getId());
            var.setFactoryEid(form.getFactoryEid());
            var.setGoodsId(e.getId());
            var.setGoodsName(e.getName());
            var.setOpUserId(staffInfo.getCurrentUserId());
            var.setOpTime(new Date());
            goodsList.add(var);
        });
        Boolean isSuccess = popProcGoodsTemplateApi.addGoodsToTemplate(form.getTemplateId(), goodsList);
        return Result.success(isSuccess);
    }

    @ApiOperation(value = "查詢模板下的商品列表")
    @PostMapping("/queryTemplateGoodsList")
    public Result<List<TemplateGoodsVO>> queryTemplateGoodsList(@RequestBody @Valid QueryTemplateGoodsListForm form) {
        PopProcTemplateDTO templateDTO = popProcTemplateApi.queryTemplateById(form.getTemplateId());
        if (templateDTO == null) {
            return Result.failed("模板不存在");
        }
        List<PopProcGoodsTemplateDTO> goodsTemplateDTOList = popProcGoodsTemplateApi.queryGoodsList(templateDTO.getId(), form.getFactoryEid());

        return Result.success(PojoUtils.map(goodsTemplateDTOList, TemplateGoodsVO.class));
    }

    @ApiOperation(value = "从模板中移除商品")
    @PostMapping("/deleteTemplateGoods")
    public Result<Boolean> deleteTemplateGoods(@RequestBody @Valid DeleteProcTemplateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        PopProcGoodsTemplateDTO goodsTemplateDTO = popProcGoodsTemplateApi.queryTemplateGoodsById(form.getId());
        if (goodsTemplateDTO == null) {
            return Result.failed("商品不存在");
        }
        Boolean isSuccess = popProcGoodsTemplateApi.deleteGoods(form.getId(), staffInfo.getCurrentUserId());
        return Result.success(isSuccess);
    }


    @ApiOperation(value = "批量设置优惠")
    @PostMapping("/updateTemplateGoodsRebate")
    public Result<Boolean> updateTemplateGoodsRebate(@RequestBody @Valid UpdateTemplateGoodsRebateForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        UpdateGoodsRebateRequest request = PojoUtils.map(form, UpdateGoodsRebateRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Boolean isSuccess = popProcGoodsTemplateApi.updateGoodsRebate(request);
        return Result.success(isSuccess);
    }


}
