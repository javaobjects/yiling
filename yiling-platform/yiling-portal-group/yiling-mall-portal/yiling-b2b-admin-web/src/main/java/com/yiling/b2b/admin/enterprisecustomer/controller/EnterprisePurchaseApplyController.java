package com.yiling.b2b.admin.enterprisecustomer.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
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
import com.yiling.b2b.admin.enterprisecustomer.form.QueryEnterprisePurchaseApplyPageForm;
import com.yiling.b2b.admin.enterprisecustomer.form.UpdatePurchaseApplyStatusForm;
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterpriseCertificateVO;
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterprisePurchaseApplyListItemVO;
import com.yiling.b2b.admin.enterprisecustomer.vo.EnterprisePurchaseApplyVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterprisePurchaseApplyDataTypeEnum;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.dto.request.UpdatePurchaseApplyStatusRequest;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台B2B-企业采购申请管理 Controller
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@RestController
@RequestMapping("/purchaseApply")
@Api(tags = "企业采购申请管理接口")
@Slf4j
public class EnterprisePurchaseApplyController extends BaseController {

    @DubboReference
    EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;
    @DubboReference
    CertificateApi certificateApi;

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "申请列表")
    @PostMapping("/pageList")
    public Result<Page<EnterprisePurchaseApplyListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryEnterprisePurchaseApplyPageForm form) {
        QueryEnterprisePurchaseApplyPageRequest request = PojoUtils.map(form,QueryEnterprisePurchaseApplyPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setDataType(EnterprisePurchaseApplyDataTypeEnum.BUY.getCode());
        Page<EnterprisePurchaseApplyBO> applyBoPage = enterprisePurchaseApplyApi.pageList(request);
        Page<EnterprisePurchaseApplyListItemVO> page = PojoUtils.map(applyBoPage, EnterprisePurchaseApplyListItemVO.class);
        page.getRecords().forEach(enterprisePurchaseApplyListItemVO -> enterprisePurchaseApplyListItemVO.setAddress(new StringJoiner(" ")
                .add(enterprisePurchaseApplyListItemVO.getProvinceName()).add(enterprisePurchaseApplyListItemVO.getCityName())
                .add(enterprisePurchaseApplyListItemVO.getRegionName()).add(enterprisePurchaseApplyListItemVO.getAddress()).toString()));

        return Result.success(page);
    }

    @ApiOperation(value = "获取详情")
    @GetMapping("/geDetail")
    public Result<EnterprisePurchaseApplyVO> geDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("customerEid") @ApiParam("采购商企业ID") Long customerEid) {
        EnterprisePurchaseApplyBO purchaseApplyBo = Optional.ofNullable(enterprisePurchaseApplyApi.getByCustomerEid(customerEid, staffInfo.getCurrentEid()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXIST_PURCHASE));
        EnterprisePurchaseApplyVO purchaseApplyVO = PojoUtils.map(purchaseApplyBo, EnterprisePurchaseApplyVO.class);
        purchaseApplyVO.setAddress(new StringJoiner(" ").add(purchaseApplyVO.getProvinceName()).add(purchaseApplyVO.getCityName())
                .add(purchaseApplyVO.getRegionName()).add(purchaseApplyVO.getAddress()).toString());

        // 已上传的企业资质列表
        List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(customerEid);
        Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDtoMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));
        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(purchaseApplyBo.getType())).getCertificateTypeEnumList();

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

    @ApiOperation(value = "审核")
    @PostMapping("/updateAuthStatus")
    @Log(title = "企业采购关系审核", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateAuthStatus(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdatePurchaseApplyStatusForm form) {
        UpdatePurchaseApplyStatusRequest request = PojoUtils.map(form,UpdatePurchaseApplyStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        enterprisePurchaseApplyApi.updateAuthStatus(request);

        return Result.success();
    }

}
