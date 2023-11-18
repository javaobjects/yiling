package com.yiling.admin.hmc.goods.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.goods.form.AddGoodsPurchaseForm;
import com.yiling.admin.hmc.goods.form.QueryEnterpriseListForm;
import com.yiling.admin.hmc.goods.form.QueryGoodsPurchaseControlPageForm;
import com.yiling.admin.hmc.goods.form.UpdateGoodsPurchaseForm;
import com.yiling.admin.hmc.goods.vo.EnterpriseVO;
import com.yiling.admin.hmc.goods.vo.GoodsPurchaseControlVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.dto.request.AddGoodsPurchaseRequest;
import com.yiling.hmc.control.dto.request.QueryGoodsPurchaseControlPageRequest;
import com.yiling.hmc.control.dto.request.UpdateGoodsPurchaseRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/3/31
 */
@RestController
@Api(tags = "保险药品渠道管控")
@RequestMapping("/goods/purchase")
@Slf4j
public class GoodsPurchaseControlController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    @ApiOperation(value = "搜索渠道商")
    @GetMapping("queryEnterprise")
    public Result<CollectionObject<List<EnterpriseVO>>> queryEnterprise(@Valid QueryEnterpriseListForm queryEnterpriseListForm){
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(queryEnterpriseListForm.getName());
        request.setType(EnterpriseTypeEnum.BUSINESS.getCode());
        List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(request);
        List<EnterpriseVO> enterpriseVOS = PojoUtils.map(enterpriseListByName,EnterpriseVO.class);
        CollectionObject<List<EnterpriseVO>> listCollectionObject = new CollectionObject(enterpriseVOS);
        return  Result.success(listCollectionObject);
    }
    @ApiOperation(value = "编辑反显")
    @GetMapping("getDetail")
    public Result<GoodsPurchaseControlVO> getDetail(@RequestParam Long id){
        GoodsPurchaseControlDTO goodsPurchaseControlDTO = goodsPurchaseControlApi.getOneById(id);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(goodsPurchaseControlDTO.getSellerEid());
        GoodsPurchaseControlVO goodsPurchaseControlVO = new GoodsPurchaseControlVO();
        PojoUtils.map(goodsPurchaseControlDTO,goodsPurchaseControlVO);
        goodsPurchaseControlVO.setEnterpriseName(enterpriseDTO.getName()).setLicenseNumber(enterpriseDTO.getLicenseNumber());
        return Result.success(goodsPurchaseControlVO);
    }
    @ApiOperation(value = "编辑")
    @PostMapping("update")
    public Result<Boolean> update(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody UpdateGoodsPurchaseForm form){
        UpdateGoodsPurchaseRequest request = new UpdateGoodsPurchaseRequest();
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(form,request);
        goodsPurchaseControlApi.update(request);
        return Result.success(true);
    }

    @ApiOperation(value = "添加管控渠道")
    @PostMapping("add")
    public Result<Boolean> add(@Valid @RequestBody AddGoodsPurchaseForm addGoodsPurchaseForm){
        AddGoodsPurchaseRequest addGoodsPurchaseRequest = new AddGoodsPurchaseRequest();
        PojoUtils.map(addGoodsPurchaseForm,addGoodsPurchaseRequest);
        goodsPurchaseControlApi.add(addGoodsPurchaseRequest);
        return Result.success(true);
    }

    @ApiOperation(value = "管控渠道列表")
    @GetMapping("queryPage")
    public Result<Page<GoodsPurchaseControlVO>> queryPage(@Valid QueryGoodsPurchaseControlPageForm form){
        QueryGoodsPurchaseControlPageRequest pageRequest = new QueryGoodsPurchaseControlPageRequest();
        PojoUtils.map(form,pageRequest);
         if(StringUtils.isNotEmpty(form.getEnterpriseName())){
             QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
             request.setName(form.getEnterpriseName());
             request.setType(EnterpriseTypeEnum.BUSINESS.getCode());
             List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.getEnterpriseListByName(request);
             if(CollUtil.isEmpty(enterpriseDTOS)){
                 return Result.success(form.getPage());
             }
             pageRequest.setEidList(enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
         }
        Page<GoodsPurchaseControlDTO> goodsPurchaseControlDTOPage = goodsPurchaseControlApi.queryPage(pageRequest);
        if(goodsPurchaseControlDTOPage.getTotal()==0){
            return Result.success(form.getPage());
        }
        List<GoodsPurchaseControlDTO> goodsPurchaseControlDTOS = goodsPurchaseControlDTOPage.getRecords();
        List<Long> eidList = goodsPurchaseControlDTOS.stream().map(GoodsPurchaseControlDTO::getSellerEid).collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);

        goodsPurchaseControlDTOS.forEach(goodsPurchaseControlDTO -> {
            goodsPurchaseControlDTO.setLicenseNumber(enterpriseDTOMap.get(goodsPurchaseControlDTO.getSellerEid()).getLicenseNumber());
            goodsPurchaseControlDTO.setEnterpriseName(enterpriseDTOMap.get(goodsPurchaseControlDTO.getSellerEid()).getName());
        });
        Page<GoodsPurchaseControlVO> goodsPurchaseControlVOPage = PojoUtils.map(goodsPurchaseControlDTOPage,GoodsPurchaseControlVO.class);
        return Result.success(goodsPurchaseControlVOPage);
    }
}