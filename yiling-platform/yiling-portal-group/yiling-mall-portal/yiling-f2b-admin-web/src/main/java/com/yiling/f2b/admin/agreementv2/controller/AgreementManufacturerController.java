package com.yiling.f2b.admin.agreementv2.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import com.yiling.f2b.admin.agreementv2.form.AddAgreementManufacturerForm;
import com.yiling.f2b.admin.agreementv2.form.AddAgreementManufacturerGoodsForm;
import com.yiling.f2b.admin.agreementv2.form.DeleteAgreementManufacturerGoodsForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementManufacturerForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementManufacturerGoodsForm;
import com.yiling.f2b.admin.agreementv2.form.QueryManufactureGoodsPageListForm;
import com.yiling.f2b.admin.agreementv2.vo.AgreementManufacturerGoodsVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementManufacturerVO;
import com.yiling.f2b.admin.agreementv2.vo.EnterpriseItemVO;
import com.yiling.f2b.admin.agreementv2.vo.GoodsListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.user.agreementv2.api.AgreementManufacturerApi;
import com.yiling.user.agreementv2.dto.AgreementManufacturerDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerGoodsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;
import com.yiling.user.agreementv2.enums.AgreementFirstTypeEnum;
import com.yiling.user.agreementv2.enums.ManufacturerTypeEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 协议厂家管理表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
@Api(tags = "协议v2.0厂家管理")
@RestController
@RequestMapping("/agreementManufacturer")
public class AgreementManufacturerController extends BaseController {

    @DubboReference
    AgreementManufacturerApi agreementManufacturerApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @ApiOperation(value = "分页查询厂家")
    @PostMapping("/queryManufacturerListPage")
    public Result<Page<AgreementManufacturerVO>> queryManufacturerListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementManufacturerForm form) {
        QueryAgreementManufacturerRequest request = PojoUtils.map(form, QueryAgreementManufacturerRequest.class);

        Page<AgreementManufacturerDTO> manufacturerDtoPage = agreementManufacturerApi.queryManufacturerListPage(request);
        Page<AgreementManufacturerVO> page = PojoUtils.map(manufacturerDtoPage, AgreementManufacturerVO.class);

        List<Long> userIdList = manufacturerDtoPage.getRecords().stream().map(AgreementManufacturerDTO::getCreateUser).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(userIdList)) {
            Map<Long, String> userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(BaseDTO::getId, UserDTO::getName, (k1, k2) -> k2));
            page.getRecords().forEach(agreementManufacturerVO -> agreementManufacturerVO.setCreateUserName(userMap.get(agreementManufacturerVO.getCreateUser())));
        }

        return Result.success(page);
    }

    @ApiOperation(value = "新增厂家")
    @PostMapping("/addManufacturer")
    @Log(title = "新增厂家", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addManufacturer(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddAgreementManufacturerForm form) {
        AddAgreementManufacturerRequest request = PojoUtils.map(form, AddAgreementManufacturerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        agreementManufacturerApi.addManufacturer(request);
        return Result.success();
    }

    @ApiOperation(value = "删除厂家")
    @GetMapping("/deleteManufacturer")
    @Log(title = "删除厂家", businessType = BusinessTypeEnum.DELETE)
    public Result<Void> deleteManufacturer(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        agreementManufacturerApi.deleteManufacturer(id, staffInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "厂家商品分页列表")
    @PostMapping("/queryManufacturerGoodsListPage")
    public Result<Page<AgreementManufacturerGoodsVO>> queryManufacturerGoodsListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementManufacturerGoodsForm form) {
        QueryAgreementManufacturerGoodsRequest request = PojoUtils.map(form, QueryAgreementManufacturerGoodsRequest.class);

        Page<AgreementManufacturerGoodsDTO> goodsDtoPage = agreementManufacturerApi.queryManufacturerGoodsListPage(request);
        Page<AgreementManufacturerGoodsVO> voPage = PojoUtils.map(goodsDtoPage, AgreementManufacturerGoodsVO.class);

        Map<Long, Integer> manufacturerMap = MapUtil.newHashMap();
        List<Long> manufactureIdList = voPage.getRecords().stream().map(AgreementManufacturerGoodsVO::getManufacturerId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(manufactureIdList)) {
            manufacturerMap = agreementManufacturerApi.listByIds(manufactureIdList).stream().collect(Collectors.toMap(BaseDTO::getId, AgreementManufacturerDTO::getType, (k1, k2) -> k2));
        }

        Map<Long, Integer> finalManufacturerMap = manufacturerMap;
        voPage.getRecords().forEach(agreementManufacturerGoodsVO -> {
            Integer type = finalManufacturerMap.get(agreementManufacturerGoodsVO.getManufacturerId());

            // 如果为生产厂家，则查询是否存在该商品对应的品牌厂家
            if (Objects.nonNull(type)) {
                String manufacturerName = agreementManufacturerApi.getNameBySpecificationAndType(agreementManufacturerGoodsVO.getSpecificationGoodsId(), type);

                if (ManufacturerTypeEnum.PRODUCER == ManufacturerTypeEnum.getByCode(type)) {
                    String name = agreementManufacturerApi.getNameBySpecificationAndType(agreementManufacturerGoodsVO.getSpecificationGoodsId(), ManufacturerTypeEnum.BRAND.getCode());
                    agreementManufacturerGoodsVO.setBrandManufacturerName(name);
                    agreementManufacturerGoodsVO.setManufacturerName(manufacturerName);
                } else if (ManufacturerTypeEnum.BRAND == ManufacturerTypeEnum.getByCode(type)) {
                    String name = agreementManufacturerApi.getNameBySpecificationAndType(agreementManufacturerGoodsVO.getSpecificationGoodsId(), ManufacturerTypeEnum.PRODUCER.getCode());
                    agreementManufacturerGoodsVO.setManufacturerName(name);
                    agreementManufacturerGoodsVO.setBrandManufacturerName(manufacturerName);
                }
            }

        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "添加厂家商品")
    @PostMapping("/addManufacturerGoods")
    @Log(title = "添加厂家商品", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addManufacturerGoods(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddAgreementManufacturerGoodsForm form) {
        List<AddAgreementManufacturerGoodsRequest> list = PojoUtils.map(form.getList(), AddAgreementManufacturerGoodsRequest.class);
        list.forEach(addAgreementManufacturerGoodsRequest -> addAgreementManufacturerGoodsRequest.setManufacturerId(form.getManufacturerId()));

        agreementManufacturerApi.addManufacturerGoods(list);
        return Result.success();
    }

    @ApiOperation(value = "删除厂家商品")
    @PostMapping("/deleteManufacturerGoods")
    @Log(title = "删除厂家商品", businessType = BusinessTypeEnum.DELETE)
    public Result<Void> deleteManufacturerGoods(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid DeleteAgreementManufacturerGoodsForm form) {
        agreementManufacturerApi.deleteManufacturerGoods(form.getIdList(), staffInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "根据企业ID查询中台企业")
    @GetMapping("/getEnterpriseById")
    public Result<EnterpriseItemVO> getEnterpriseById(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        EnterpriseItemVO enterpriseItemVO = new EnterpriseItemVO(enterpriseDTO.getId(), enterpriseDTO.getName());

        return Result.success(enterpriseItemVO);
    }

    @ApiOperation(value = "选择商品分页列表（查询中台商品库/厂家商品：厂家关联商品选择商品、协议供销条款选择商品都使用此接口）")
    @PostMapping("/queryGoodsPageList")
    public Result<Page<GoodsListItemVO>> queryGoodsPageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryManufactureGoodsPageListForm form) {

        Page<GoodsListItemVO> page;

        if (form.getEid() != null && (AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER == AgreementFirstTypeEnum.getByCode(form.getFirstType())
                || AgreementFirstTypeEnum.INDUSTRIAL_BRAND == AgreementFirstTypeEnum.getByCode(form.getFirstType())) ) {

                AgreementManufacturerDTO manufacturerDTO = agreementManufacturerApi.getByEid(form.getEid());
                QueryAgreementManufacturerGoodsRequest request = PojoUtils.map(form, QueryAgreementManufacturerGoodsRequest.class);
                request.setManufacturerId(manufacturerDTO.getId());

                Page<AgreementManufacturerGoodsDTO> goodsDtoPage = agreementManufacturerApi.queryManufacturerGoodsListPage(request);
                page = PojoUtils.map(goodsDtoPage, GoodsListItemVO.class);
                return Result.success(page);

        } else {
            StandardSpecificationPageRequest request = PojoUtils.map(form, StandardSpecificationPageRequest.class);
            request.setStandardId(form.getGoodsId());
            request.setStandardGoodsName(form.getGoodsName());
            Page<StandardGoodsSpecificationDTO> specificationDtoPage = standardGoodsSpecificationApi.getSpecificationPageByGoods(request);

            List<GoodsListItemVO> itemVOList = specificationDtoPage.getRecords().stream().map(standardGoodsSpecificationDTO -> {
                GoodsListItemVO goodsListItemVO = new GoodsListItemVO();
                goodsListItemVO.setGoodsId(standardGoodsSpecificationDTO.getStandardId());
                goodsListItemVO.setSpecificationGoodsId(standardGoodsSpecificationDTO.getId());
                goodsListItemVO.setGoodsName(standardGoodsSpecificationDTO.getName());
                goodsListItemVO.setSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
                goodsListItemVO.setManufacturerName(standardGoodsSpecificationDTO.getManufacturer());
                return goodsListItemVO;
            }).collect(Collectors.toList());

            page = PojoUtils.map(specificationDtoPage, GoodsListItemVO.class);
            page.setRecords(itemVOList);
        }

        return Result.success(page);
    }

}
