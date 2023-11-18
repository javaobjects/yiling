package com.yiling.admin.hmc.welfare.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.hmc.enterprise.form.EnterprisePageForm;
import com.yiling.admin.hmc.enterprise.vo.EnterpriseVO;
import com.yiling.admin.hmc.welfare.form.DeleteDrugWelfareEnterpriseForm;
import com.yiling.admin.hmc.welfare.form.DrugWelfareEnterprisePageForm;
import com.yiling.admin.hmc.welfare.form.SaveDrugWelfareEnterpriseForm;
import com.yiling.admin.hmc.welfare.vo.DrugWelfareEnterpriseVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.api.DrugWelfareEnterpriseApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.DrugWelfareEnterpriseDTO;
import com.yiling.hmc.welfare.dto.request.DeleteDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareEnterprisePageRequest;
import com.yiling.hmc.welfare.dto.request.SaveDrugWelfareEnterpriseRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 药品福利商家 前端控制器
 * </p>
 *
 * @author: hongyang.zhang
 * @data: 2022/09/26
 */
@Api(tags = "药品福利商家接口")
@RestController
@RequestMapping("/drug/welfare/enterprise")
public class DrugWelfareEnterpriseController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    DrugWelfareEnterpriseApi drugWelfareEnterpriseApi;

    @DubboReference
    DrugWelfareApi drugWelfareApi;


    @ApiOperation(value = "福利计划添加商家时选择商家列表")
    @PostMapping("/pageEnterprise")
    public Result<Page<EnterpriseVO>> pageEnterprise(@RequestBody EnterprisePageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        List<Integer> typeList = Lists.newArrayList();
        typeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
        typeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
        typeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());
        typeList.add(EnterpriseTypeEnum.PHARMACY.getCode());
        typeList.add(EnterpriseTypeEnum.HOSPITAL.getCode());
        typeList.add(EnterpriseTypeEnum.CLINIC.getCode());
        request.setInTypeList(typeList);
        if (StringUtils.isNotBlank(form.getEname())) {
            request.setName(form.getEname());
        }
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        Page<EnterpriseDTO> pageList = enterpriseApi.pageList(request);
        Page<EnterpriseVO> voPage = PojoUtils.map(pageList, EnterpriseVO.class);
        return Result.success(voPage);
    }



    @ApiOperation(value = "药品福利计划终端商家列表")
    @PostMapping("/queryPage")
    public Result<Page<DrugWelfareEnterpriseVO>> queryPage(@RequestBody DrugWelfareEnterprisePageForm form) {
        DrugWelfareEnterprisePageRequest request = PojoUtils.map(form, DrugWelfareEnterprisePageRequest.class);
        Page<DrugWelfareEnterpriseDTO> page = drugWelfareEnterpriseApi.pageList(request);
        Page<DrugWelfareEnterpriseVO> voPage = PojoUtils.map(page, DrugWelfareEnterpriseVO.class);
        if (voPage.getTotal() == 0) {
            return Result.success(voPage);
        }
        //福利计划id
        List<Long> drugWelfareIdList = voPage.getRecords().stream().map(e -> e.getDrugWelfareId()).distinct().collect(Collectors.toList());
        List<DrugWelfareDTO> list = drugWelfareApi.getByIdList(drugWelfareIdList);
        Map<Long, DrugWelfareDTO> drugWelfareMap = list.stream().collect(Collectors.toMap(DrugWelfareDTO::getId, Function.identity()));
        voPage.getRecords().forEach(e -> {
            e.setDrugWelfareName(drugWelfareMap.get(e.getDrugWelfareId()).getName());
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "保存药品福利计划与商家关系")
    @PostMapping("/save")
    public Result save(@RequestBody @Valid SaveDrugWelfareEnterpriseForm form, @CurrentUser CurrentAdminInfo currentAdminInfo) {
        SaveDrugWelfareEnterpriseRequest request = PojoUtils.map(form, SaveDrugWelfareEnterpriseRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        EnterpriseDTO enterprise = enterpriseApi.getById(request.getEid());
        if (EnterpriseStatusEnum.DISABLED.getCode().equals(enterprise.getStatus())){
            return  Result.failed("商家已停用");
        }
        Boolean b = drugWelfareEnterpriseApi.saveDrugWelfareEnterprise(request);
        if (b) {
            return Result.success();
        } else {
            return Result.failed("保存失败");
        }
    }

    @ApiOperation(value = "删除药品福利计划与商家关系")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteDrugWelfareEnterpriseForm form, @CurrentUser CurrentAdminInfo currentAdminInfo) {
        DeleteDrugWelfareEnterpriseRequest request = PojoUtils.map(form, DeleteDrugWelfareEnterpriseRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        Boolean b = drugWelfareEnterpriseApi.deleteDrugWelfareEnterprise(request);
        if (b) {
            return Result.success();
        } else {
            return Result.failed("删除失败");
        }
    }

}
