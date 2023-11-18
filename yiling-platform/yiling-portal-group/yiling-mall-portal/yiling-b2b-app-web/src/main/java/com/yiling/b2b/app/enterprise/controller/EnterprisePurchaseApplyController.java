package com.yiling.b2b.app.enterprise.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.enterprise.form.QueryEnterprisePurchaseApplyPageForm;
import com.yiling.b2b.app.enterprise.vo.EnterprisePurchaseApplyListItemVO;
import com.yiling.b2b.app.enterprise.vo.EnterprisePurchaseApplyVO;
import com.yiling.b2b.app.shop.vo.EnterpriseCertificateVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterprisePurchaseApplyDataTypeEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDetailDTO;
import com.yiling.user.enterprise.dto.request.AddPurchaseApplyRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B-移动端 企业采购申请 Controller
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@Slf4j
@RestController
@RequestMapping("/purchaseApply")
@Api(tags = "企业采购申请接口")
public class EnterprisePurchaseApplyController extends BaseController {

    @DubboReference
    EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    ShopApi shopApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    LocationApi locationApi;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "获取采购关系详情")
    @GetMapping("/getSellerDetail")
    public Result<EnterprisePurchaseApplyVO> getSellerDetail(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("eid") @ApiParam("供应商ID") Long eid) {
        EnterprisePurchaseApplyBO purchaseApplyBO = enterprisePurchaseApplyApi.getByEid(staffInfo.getCurrentEid(), eid);

        Integer enterpriseType;
        EnterprisePurchaseApplyVO purchaseApplyVO;
        if (Objects.isNull(purchaseApplyBO)) {
            // 获取当前查看的供应商企业信息
            EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(eid)).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
            enterpriseType = enterpriseDTO.getType();
            purchaseApplyVO = PojoUtils.map(enterpriseDTO,EnterprisePurchaseApplyVO.class);
            purchaseApplyVO.setEid(enterpriseDTO.getId());
            purchaseApplyVO.setAuthStatus(4);
            purchaseApplyVO.setAuthRejectReason(null);
        } else {
            enterpriseType = purchaseApplyBO.getType();
            purchaseApplyVO = PojoUtils.map(purchaseApplyBO,EnterprisePurchaseApplyVO.class);
        }

        ShopDTO shopDTO = Optional.ofNullable(shopApi.getShop(eid)).orElse(new ShopDTO());
        // 店铺简介 和 店铺Logo
        purchaseApplyVO.setShopDesc(shopDTO.getShopDesc());
        purchaseApplyVO.setShopLogo(shopDTO.getShopLogo());
        // 销售区域编码
        List<EnterpriseSalesAreaDetailDTO> saleAreaDetailList = enterpriseApi.getEnterpriseSaleAreaDetail(eid);
        List<String> regionCodeList = saleAreaDetailList.stream().map(EnterpriseSalesAreaDetailDTO::getAreaCode).collect(Collectors.toList());
        // 根据区域编码获取省份名称
        List<String> salesProvinceNameList = locationApi.getProvinceNameByRegionCode(regionCodeList);
        purchaseApplyVO.setSalesProvinceNameList(salesProvinceNameList);

        // 已上传的企业资质列表
        List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(eid);
        Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDtoMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));
        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseType)).getCertificateTypeEnumList();

        List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
        enterpriseCertificateTypeEnumList.forEach(e -> {
            EnterpriseCertificateDTO enterpriseCertificateDTO = enterpriseCertificateDtoMap.get(e.getCode());
            if (enterpriseCertificateDTO != null) {
                EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
                enterpriseCertificateVO.setType(e.getCode());
                enterpriseCertificateVO.setName(e.getName());
                enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
                enterpriseCertificateVO.setRequired(e.getMustExist());

                enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
                enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
                enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
                enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
                enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
                enterpriseCertificateVOList.add(enterpriseCertificateVO);
            }
        });
        purchaseApplyVO.setCertificateVOList(enterpriseCertificateVOList);

        return Result.success(purchaseApplyVO);
    }

    @ApiOperation(value = "提交申请采购")
    @GetMapping("/applyPurchase")
    public Result<Void> applyPurchase(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("eid") @ApiParam("供应商ID") Long eid) {
        AddPurchaseApplyRequest request = new AddPurchaseApplyRequest();
        request.setEid(eid);
        request.setCustomerEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        enterprisePurchaseApplyApi.addPurchaseApply(request);

        return Result.success();
    }

    @ApiOperation(value = "采购关系分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterprisePurchaseApplyListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryEnterprisePurchaseApplyPageForm form) {
        // 分页查询
        QueryEnterprisePurchaseApplyPageRequest request = PojoUtils.map(form,QueryEnterprisePurchaseApplyPageRequest.class);
        request.setCustomerEid(staffInfo.getCurrentEid());
        request.setDataType(EnterprisePurchaseApplyDataTypeEnum.SELL.getCode());
        Page<EnterprisePurchaseApplyBO> purchaseApplyBoPage = enterprisePurchaseApplyApi.pageList(request);
        Page<EnterprisePurchaseApplyListItemVO> page = PojoUtils.map(purchaseApplyBoPage, EnterprisePurchaseApplyListItemVO.class);

        // 查询店铺信息，获取店铺logo
        List<Long> eidList = page.getRecords().stream().map(EnterprisePurchaseApplyListItemVO::getEid).collect(Collectors.toList());
        List<ShopDTO> shopDTOList = shopApi.listShopByEidList(eidList);
        Map<Long, String> shopMap = shopDTOList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, ShopDTO::getShopLogo,(k1,k2) -> k2));
        page.getRecords().forEach(enterprisePurchaseVO -> enterprisePurchaseVO.setShopLogo(shopMap.get(enterprisePurchaseVO.getEid())));

        return Result.success(page);
    }


}
