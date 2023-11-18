package com.yiling.admin.data.center.enterprise.controller;

import java.util.Date;
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
import com.yiling.admin.data.center.enterprise.form.QueryEnterpriseAuthPageForm;
import com.yiling.admin.data.center.enterprise.form.UpdateEnterpriseAuthForm;
import com.yiling.admin.data.center.enterprise.vo.CommonEnterpriseInfoVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseAuthInfoUpdateVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseAuthInfoVO;
import com.yiling.admin.data.center.enterprise.vo.EnterpriseCertificateVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.request.CustomerMessageBuildRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CertificateAuthApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseAuthApi;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseAuthPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseAuthTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.usercustomer.api.UserCustomerApi;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业审核模块 Controller
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@RestController
@RequestMapping("/enterpriseAuth")
@Api(tags = "企业审核模块接口")
@Slf4j
public class EnterpriseAuthController extends BaseController {

    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseAuthApi enterpriseAuthApi;
    @DubboReference
    CertificateAuthApi certificateAuthApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    MessageApi messageApi;
    @DubboReference
    UserCustomerApi userCustomerApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "获取企业认证审核分页列表")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseAuthInfoVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid QueryEnterpriseAuthPageForm form) {
        QueryEnterpriseAuthPageRequest request = PojoUtils.map(form, QueryEnterpriseAuthPageRequest.class);

        Page<EnterpriseAuthInfoDTO> page = enterpriseAuthApi.pageList(request);
        List<EnterpriseAuthInfoDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        Page<EnterpriseAuthInfoVO> pageVO = PojoUtils.map(page, EnterpriseAuthInfoVO.class);

        List<Long> authUserIdList = records.stream().map(EnterpriseAuthInfoDTO::getAuthUser).distinct().collect(Collectors.toList());
        Map<Long, String> userNameMap = userApi.listByIds(authUserIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        pageVO.getRecords().forEach(enterpriseAuthInfoVO -> enterpriseAuthInfoVO.setAuthUserName(userNameMap.get(enterpriseAuthInfoVO.getAuthUser())));

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取企业审核详情（首次认证）")
    @GetMapping("/get")
    public Result<EnterpriseAuthInfoVO> get(@ApiParam(value = "企业ID", required = true) @RequestParam("id") Long id) {
        EnterpriseAuthInfoDTO enterpriseAuthInfoDTO = Optional.ofNullable(enterpriseAuthApi.getByEid(id)).orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        EnterpriseAuthInfoVO enterpriseAuthInfoVO = PojoUtils.map(enterpriseAuthInfoDTO,EnterpriseAuthInfoVO.class);

        if(EnterpriseAuthTypeEnum.getByCode(enterpriseAuthInfoDTO.getAuthType()) != EnterpriseAuthTypeEnum.FIRST){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_STATUS_ERROR);
        }

        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseAuthInfoDTO.getType())).getCertificateTypeEnumList();
        // 已上传的企业资质列表
        List<EnterpriseCertificateAuthInfoDTO> certificateAuthInfoDTOList = certificateAuthApi.getCertificateAuthInfoListByAuthId(enterpriseAuthInfoDTO.getId());
        Map<Integer, EnterpriseCertificateAuthInfoDTO> enterpriseCertificateDtoMap = certificateAuthInfoDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateAuthInfoDTO::getType, Function.identity()));

        //获取企业资质信息
        List<EnterpriseCertificateVO> enterpriseCertificateVOList = getCertificateVoList(enterpriseCertificateTypeEnumList, enterpriseCertificateDtoMap);
        enterpriseAuthInfoVO.setCertificateVOList(enterpriseCertificateVOList);

        return Result.success(enterpriseAuthInfoVO);
    }

    @ApiOperation(value = "获取企业审核详情（资质更新）")
    @GetMapping("/getCertificateUpdate")
    public Result<EnterpriseAuthInfoUpdateVO> getCertificateUpdate(@ApiParam(value = "企业ID", required = true) @RequestParam("id") Long id) {
        EnterpriseAuthInfoDTO enterpriseAuthInfoDTO = Optional.ofNullable(enterpriseAuthApi.getByEid(id)).orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        if(EnterpriseAuthTypeEnum.getByCode(enterpriseAuthInfoDTO.getAuthType()) == EnterpriseAuthTypeEnum.FIRST){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_STATUS_ERROR);
        }

        // 获取原始企业信息
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(id);
        // 获取等待审核的企业信息
        EnterpriseAuthInfoUpdateVO enterpriseAuthInfoUpdateVO = PojoUtils.map(enterpriseAuthInfoDTO, EnterpriseAuthInfoUpdateVO.class);

        // 设置原企业基本信息
        CommonEnterpriseInfoVO originEnterpriseInfo = PojoUtils.map(enterpriseDTO, CommonEnterpriseInfoVO.class);
        originEnterpriseInfo.setEid(enterpriseDTO.getId());
        enterpriseAuthInfoUpdateVO.setOriginEnterpriseInfo(originEnterpriseInfo);
        // 设置更新的企业基本信息：企业名称、信用代码、区域等
        setEnterpriseAuthInfoUpdate(enterpriseAuthInfoUpdateVO, enterpriseDTO, enterpriseAuthInfoDTO);

        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseAuthInfoDTO.getType())).getCertificateTypeEnumList();
        Map<Integer, EnterpriseCertificateTypeEnum> typeEnumMap = enterpriseCertificateTypeEnumList.stream().collect(Collectors.toMap(EnterpriseCertificateTypeEnum::getCode, Function.identity()));

        // 企业原资质列表
        List<EnterpriseCertificateDTO> certificateDTOList = certificateApi.listByEid(id);
        Map<Integer, EnterpriseCertificateDTO> originalCertificateMap = certificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity(), (k1, k2) -> k2));
        List<EnterpriseCertificateVO> originalCertificateVOList = PojoUtils.map(certificateDTOList, EnterpriseCertificateVO.class);
        setCertificateInfo(typeEnumMap, originalCertificateVOList);

        // 待审核的企业资质列表
        List<EnterpriseCertificateAuthInfoDTO> certificateAuthInfoDTOList = certificateAuthApi.getCertificateAuthInfoListByAuthId(enterpriseAuthInfoDTO.getId());
        List<EnterpriseCertificateVO> enterpriseCertificateVOList = PojoUtils.map(certificateAuthInfoDTOList, EnterpriseCertificateVO.class);
        setCertificateInfo(typeEnumMap, enterpriseCertificateVOList);

        // 选出有更新的资质信息
        List<EnterpriseCertificateVO> haveChangeList = ListUtil.toList();

        enterpriseCertificateVOList.forEach(enterpriseCertificateVO -> {
            // 更新的企业资质与原始资质比较，不同则表示更新
            EnterpriseCertificateDTO originalCertificate = originalCertificateMap.get(enterpriseCertificateVO.getType());
            if (Objects.isNull(originalCertificate)) {
                haveChangeList.add(enterpriseCertificateVO);
            } else if (!StrUtil.equals(originalCertificate.getFileKey(), enterpriseCertificateVO.getFileKey())) {
                haveChangeList.add(enterpriseCertificateVO);
            } else if (!originalCertificate.getPeriodBegin().equals(enterpriseCertificateVO.getPeriodBegin())) {
                haveChangeList.add(enterpriseCertificateVO);
            } else if (!originalCertificate.getPeriodEnd().equals(enterpriseCertificateVO.getPeriodEnd())) {
                haveChangeList.add(enterpriseCertificateVO);
            } else if (!originalCertificate.getLongEffective().equals(enterpriseCertificateVO.getLongEffective())) {
                haveChangeList.add(enterpriseCertificateVO);
            }
        });
        enterpriseAuthInfoUpdateVO.getUpdateEnterpriseInfo().setCertificateVOList(haveChangeList);
        enterpriseAuthInfoUpdateVO.getOriginEnterpriseInfo().setCertificateVOList(originalCertificateVOList);

        return Result.success(enterpriseAuthInfoUpdateVO);
    }

    private void setCertificateInfo(Map<Integer, EnterpriseCertificateTypeEnum> typeEnumMap, List<EnterpriseCertificateVO> originalCertificateVOList) {
        originalCertificateVOList.forEach(enterpriseCertificateVO -> {
            EnterpriseCertificateTypeEnum certificateTypeEnum = typeEnumMap.get(enterpriseCertificateVO.getType());
            enterpriseCertificateVO.setName(certificateTypeEnum.getName());
            enterpriseCertificateVO.setPeriodRequired(certificateTypeEnum.getCollectDate());
            enterpriseCertificateVO.setRequired(certificateTypeEnum.getMustExist());
            enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateVO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
        });
    }

    @ApiOperation(value = "审核")
    @PostMapping("/updateAuth")
    @Log(title = "审核",businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateAuth(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateEnterpriseAuthForm form) {
        UpdateEnterpriseAuthRequest request = PojoUtils.map(form,UpdateEnterpriseAuthRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());

        Boolean result = enterpriseAuthApi.updateAuth(request);
        if (result) {
            // 审核失败：调用消息插入
            insertMessage(request);
        }

        return Result.success();
    }

    private void insertMessage(UpdateEnterpriseAuthRequest request) {
        // 审核失败并且是销售助手添加的客户：调用消息插入
        try {
            if (EnterpriseAuthStatusEnum.AUTH_FAIL == EnterpriseAuthStatusEnum.getByCode(request.getAuthStatus())) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getId());

                // 校验是否为销售助手添加的客户
                if (Objects.nonNull(enterpriseDTO.getSource()) && EnterpriseSourceEnum.getByCode(enterpriseDTO.getSource()) == EnterpriseSourceEnum.SALE_ASSISTANT_ADD){
                    // 查询出用户客户信息：获取到该企业对应的用户
                    QueryUserCustomerRequest customerRequest = new QueryUserCustomerRequest();
                    customerRequest.setCustomerEid(enterpriseDTO.getId());
                    List<UserCustomerDTO> customerDTOList = userCustomerApi.queryCustomerList(customerRequest);
                    List<Long> userIdList = customerDTOList.stream().map(UserCustomerDTO::getUserId).collect(Collectors.toList());

                    userIdList.forEach(userId -> {
                        CustomerMessageBuildRequest buildRequest = new CustomerMessageBuildRequest();
                        buildRequest.setCode(enterpriseDTO.getId().toString());
                        buildRequest.setName(enterpriseDTO.getName());
                        buildRequest.setCreateTime(new Date());
                        buildRequest.setUserId(userId);
                        buildRequest.setEid(enterpriseDTO.getId());
                        buildRequest.setSourceEnum(SourceEnum.SA);
                        buildRequest.setMessageRoleEnum(MessageRoleEnum.TO_USER);
                        buildRequest.setMessageNodeEnum(MessageNodeEnum.CUSTOMER_AUTH_FAIL);
                        messageApi.buildCustomerMessage(buildRequest);
                    });
                }
            }
        }catch (Exception e) {
            log.error("审核失败后插入消息错误：", e);
        }

    }


    private List<EnterpriseCertificateVO> getCertificateVoList(List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList,
                                                               Map<Integer, EnterpriseCertificateAuthInfoDTO> enterpriseCertificateDtoMap) {

        return enterpriseCertificateTypeEnumList.stream().map(e -> {
            EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
            enterpriseCertificateVO.setType(e.getCode());
            enterpriseCertificateVO.setName(e.getName());
            enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
            enterpriseCertificateVO.setRequired(e.getMustExist());

            EnterpriseCertificateAuthInfoDTO enterpriseCertificateDTO = enterpriseCertificateDtoMap.get(e.getCode());
            if (Objects.nonNull(enterpriseCertificateDTO)) {
                enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
                enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
                enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
                enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
                enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
            }
            return enterpriseCertificateVO;
        }).collect(Collectors.toList());
    }

    /**
     * 设置企业基本信息是否有更新
     *
     * @param enterpriseAuthInfoUpdateVO 返回对象
     * @param enterpriseDTO 原企业信息
     * @param authInfoDTO 等待审核的企业信息
     */
    private void setEnterpriseAuthInfoUpdate(EnterpriseAuthInfoUpdateVO enterpriseAuthInfoUpdateVO, EnterpriseDTO enterpriseDTO, EnterpriseAuthInfoDTO authInfoDTO) {
        CommonEnterpriseInfoVO updateEnterpriseInfo = new CommonEnterpriseInfoVO();
        updateEnterpriseInfo.setEid(enterpriseDTO.getId());
        updateEnterpriseInfo.setType(enterpriseDTO.getType());

        if(!StrUtil.equals(authInfoDTO.getName(), enterpriseDTO.getName())){
            updateEnterpriseInfo.setName(authInfoDTO.getName());
        }
        if(!StrUtil.equals(authInfoDTO.getLicenseNumber(), enterpriseDTO.getLicenseNumber())){
            updateEnterpriseInfo.setLicenseNumber(authInfoDTO.getLicenseNumber());
        }
        if(!StrUtil.equals(authInfoDTO.getAddress(), enterpriseDTO.getAddress()) || !StrUtil.equals(authInfoDTO.getProvinceCode(), enterpriseDTO.getProvinceCode()) ||
                !StrUtil.equals(authInfoDTO.getCityCode(), enterpriseDTO.getCityCode()) || !StrUtil.equals(authInfoDTO.getRegionCode(), enterpriseDTO.getRegionCode())) {
            updateEnterpriseInfo.setAddress(authInfoDTO.getAddress());
            updateEnterpriseInfo.setProvinceCode(authInfoDTO.getProvinceCode());
            updateEnterpriseInfo.setProvinceName(authInfoDTO.getProvinceName());
            updateEnterpriseInfo.setCityCode(authInfoDTO.getCityCode());
            updateEnterpriseInfo.setCityName(authInfoDTO.getCityName());
            updateEnterpriseInfo.setRegionCode(authInfoDTO.getRegionCode());
            updateEnterpriseInfo.setRegionName(authInfoDTO.getRegionName());
        }
        enterpriseAuthInfoUpdateVO.setUpdateEnterpriseInfo(updateEnterpriseInfo);
    }

}
