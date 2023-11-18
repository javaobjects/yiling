package com.yiling.b2b.app.enterprise.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.bo.JwtDataModel;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EnterpriseAuthSourceEnum;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.enterprise.form.EnterpriseCertificateForm;
import com.yiling.b2b.app.enterprise.form.UpdateEnterpriseInfoForm;
import com.yiling.b2b.app.enterprise.vo.EnterpriseExtraInfoVO;
import com.yiling.b2b.app.enterprise.vo.EnterpriseInfoVO;
import com.yiling.b2b.app.shop.vo.EnterpriseCertificateVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.api.CertificateAuthApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseAuthApi;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseInfoRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseCertificateTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业信息 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/21
 */
@Slf4j
@RestController
@RequestMapping("/enterprise")
@Api(tags = "企业管理接口")
public class EnterpriseController extends BaseController {

    /**
     * 虚拟账号过期时间（秒）：默认为40天（3600 * 24 * 40 = 3456000秒）
     */
    @Value("${common.user.virtualAccount.expired:3456000}")
    private Integer virtualExpired;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CertificateApi certificateApi;
    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    EnterpriseAuthApi enterpriseAuthApi;
    @DubboReference
    CertificateAuthApi certificateAuthApi;
    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    CouponApi couponApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    EmployeeApi employeeApi;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "获取企业信息")
    @GetMapping("/getEnterpriseInfo")
    public Result<EnterpriseInfoVO> getEnterpriseIntegralPage(@CurrentUser CurrentStaffInfo staffInfo) {
        //企业基本信息
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        EnterpriseInfoVO enterpriseInfoVO = PojoUtils.map(enterpriseDTO,EnterpriseInfoVO.class);

        // 企业类型对应的资质列表
        List<EnterpriseCertificateTypeEnum> enterpriseCertificateTypeEnumList = Objects.requireNonNull(EnterpriseTypeEnum.getByCode(enterpriseDTO.getType())).getCertificateTypeEnumList();
        // 已上传的企业资质列表
        List<EnterpriseCertificateDTO> enterpriseCertificateDTOList = certificateApi.listByEid(staffInfo.getCurrentEid());
        Map<Integer, EnterpriseCertificateDTO> enterpriseCertificateDtoMap = enterpriseCertificateDTOList.stream().collect(Collectors.toMap(EnterpriseCertificateDTO::getType, Function.identity()));

        List<EnterpriseCertificateVO> enterpriseCertificateVOList = CollUtil.newArrayList();
        enterpriseCertificateTypeEnumList.forEach(e -> {
            EnterpriseCertificateVO enterpriseCertificateVO = new EnterpriseCertificateVO();
            enterpriseCertificateVO.setType(e.getCode());
            enterpriseCertificateVO.setName(e.getName());
            enterpriseCertificateVO.setPeriodRequired(e.getCollectDate());
            enterpriseCertificateVO.setRequired(e.getMustExist());

            EnterpriseCertificateDTO enterpriseCertificateDTO = enterpriseCertificateDtoMap.get(e.getCode());
            if (enterpriseCertificateDTO != null) {
                enterpriseCertificateVO.setFileUrl(fileService.getUrl(enterpriseCertificateDTO.getFileKey(), FileTypeEnum.ENTERPRISE_CERTIFICATE));
                enterpriseCertificateVO.setFileKey(enterpriseCertificateDTO.getFileKey());
                enterpriseCertificateVO.setPeriodBegin(enterpriseCertificateDTO.getPeriodBegin());
                enterpriseCertificateVO.setPeriodEnd(enterpriseCertificateDTO.getPeriodEnd());
                enterpriseCertificateVO.setLongEffective(enterpriseCertificateDTO.getLongEffective());
            }
            enterpriseCertificateVOList.add(enterpriseCertificateVO);
        });
        enterpriseInfoVO.setCertificateVOList(enterpriseCertificateVOList);

        //设置是否可编辑
        EnterpriseAuthInfoDTO authInfoDTO = enterpriseAuthApi.getByEid(enterpriseDTO.getId());

        if (Objects.nonNull(authInfoDTO) && EnterpriseAuthStatusEnum.getByCode(authInfoDTO.getAuthStatus()) == EnterpriseAuthStatusEnum.UNAUTH ){
            enterpriseInfoVO.setEditStatus(0);
        } else {
            enterpriseInfoVO.setEditStatus(1);
        }
        if(Objects.nonNull(authInfoDTO) && Objects.nonNull(authInfoDTO.getAuthStatus())){
            enterpriseInfoVO.setTranscriptAuthStatus(authInfoDTO.getAuthStatus());
        }
        if(Objects.nonNull(authInfoDTO) && StrUtil.isNotEmpty(authInfoDTO.getAuthRejectReason())){
            enterpriseInfoVO.setAuthRejectReason(authInfoDTO.getAuthRejectReason());
        }

        return Result.success(enterpriseInfoVO);
    }

    @ApiOperation(value = "修改企业资质管理信息")
    @PostMapping("/updateEnterpriseInfo")
    public Result<Void> updateEnterpriseInfo(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid UpdateEnterpriseInfoForm form) {

        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid())).orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        List<EnterpriseCertificateDTO> certificateDtoList = certificateApi.listByEid(staffInfo.getCurrentEid());
        List<EnterpriseCertificateForm> certificateFromList = form.getCertificateFormList();
        log.info("B2B移动端修改企业资质管理信息请求入参：{}",JSONObject.toJSONString(form));

        //校验审核中的不能进行编辑
        EnterpriseAuthInfoDTO authInfoDTO = enterpriseAuthApi.getByEid(enterpriseDTO.getId());
        if(EnterpriseAuthStatusEnum.getByCode(enterpriseDTO.getAuthStatus()) == EnterpriseAuthStatusEnum.UNAUTH){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_GOING);
        }
        if(Objects.nonNull(authInfoDTO) && EnterpriseAuthStatusEnum.getByCode(authInfoDTO.getAuthStatus()) == EnterpriseAuthStatusEnum.UNAUTH){
            throw new BusinessException(UserErrorCode.ENTERPRISE_AUTH_GOING);
        }

        //校验企业资质必填项
        checkEnterpriseCertificate(enterpriseDTO.getType(),certificateFromList);

        //若仅修改了企业联系人和联系人手机号则直接数据更新，提示用户“修改成功”
        UpdateEnterpriseInfoRequest fromRequest = PojoUtils.map(form,UpdateEnterpriseInfoRequest.class);
        UpdateEnterpriseInfoRequest dtoRequest = PojoUtils.map(enterpriseDTO,UpdateEnterpriseInfoRequest.class);

        Result<Void> result = Result.success();
        //校验资质信息是否有更新
        boolean flag = checkCertificateUpdate(certificateDtoList, certificateFromList);

        if (!flag && fromRequest.equals(dtoRequest)) {
            //资质信息和基本信息都没有更新，只更新了联系人和手机的情况
            UpdateEnterpriseRequest enterpriseRequest = new UpdateEnterpriseRequest();
            enterpriseRequest.setContactor(fromRequest.getContactor());
            enterpriseRequest.setContactorPhone(fromRequest.getContactorPhone());
            enterpriseRequest.setId(enterpriseDTO.getId());
            enterpriseRequest.setLicenseNumber(enterpriseDTO.getLicenseNumber());
            enterpriseRequest.setOpUserId(staffInfo.getCurrentUserId());
            enterpriseApi.update(enterpriseRequest);
            result.setMessage("修改成功");
        } else {
            //否则就需要进行创建副本信息，并等待审核

            //添加企业信息副本
            EnterpriseAuthInfoRequest request = PojoUtils.map(form,EnterpriseAuthInfoRequest.class);
            request.setEid(staffInfo.getCurrentEid());
            request.setOpUserId(staffInfo.getCurrentUserId());
            request.setSource(EnterpriseAuthSourceEnum.B2B.getCode());
            Long enterpriseAuthId = enterpriseAuthApi.addEnterpriseAuth(request);

            //添加企业资质信息副本
            List<EnterpriseCertificateAuthInfoRequest> authInfoRequests = PojoUtils.map(form.getCertificateFormList(), EnterpriseCertificateAuthInfoRequest.class);
            authInfoRequests.forEach(enterpriseCertificateAuthInfoRequest ->{
                enterpriseCertificateAuthInfoRequest.setEnterpriseAuthId(enterpriseAuthId);
                enterpriseCertificateAuthInfoRequest.setEid(enterpriseDTO.getId());
                enterpriseCertificateAuthInfoRequest.setOpUserId(staffInfo.getCurrentUserId());
            });
            certificateAuthApi.addEnterpriseCertificateAuth(authInfoRequests);
            result.setMessage("您的企业资料已提交成功");
        }

        return result;
    }

    @ApiOperation(value = "我的-个人信息")
    @GetMapping("/getMyInfo")
    public Result<EnterpriseExtraInfoVO> get(@CurrentUser CurrentStaffInfo staffInfo) {
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        EnterpriseExtraInfoVO extraInfoVO = PojoUtils.map(enterpriseDTO,EnterpriseExtraInfoVO.class);
        //查询优惠券数量(孙厚杰提供接口)
        Integer couponNum = couponApi.getEffectiveCountByEid(staffInfo.getCurrentEid());
        extraInfoVO.setCoupons(couponNum);

        // 会员图标列表
        List<MemberEnterpriseBO> memberEnterpriseBOList = enterpriseMemberApi.getMemberListByEid(staffInfo.getCurrentEid());
        List<EnterpriseExtraInfoVO.MemberPicture> memberPictureList = memberEnterpriseBOList.stream().map(memberEnterpriseBO -> {
            EnterpriseExtraInfoVO.MemberPicture memberPicture = new EnterpriseExtraInfoVO.MemberPicture();
            memberPicture.setMemberId(memberEnterpriseBO.getMemberId());
            if (memberEnterpriseBO.getMemberFlag()) {
                memberPicture.setPictureUrl(fileService.getUrl(memberEnterpriseBO.getLightPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE));
            } else {
                memberPicture.setPictureUrl(fileService.getUrl(memberEnterpriseBO.getExtinguishPicture(), FileTypeEnum.MEMBER_LIGHT_PICTURE));
            }
            return memberPicture;
        }).collect(Collectors.toList());
        extraInfoVO.setMemberPictureList(memberPictureList);

        // 为了IOS旧版本兼容故此处暂时保留
        CurrentMemberDTO currentMember = Optional.ofNullable(memberApi.getCurrentMember(staffInfo.getCurrentEid())).orElse(new CurrentMemberDTO());
        extraInfoVO.setMemberStatus(currentMember.getCurrentMember());
        extraInfoVO.setStopGet(currentMember.getStopGet());
        extraInfoVO.setMemberName(currentMember.getName());
        extraInfoVO.setMemberDescription(currentMember.getDescription());

        //获取当前登录用户的手机号
        UserDTO userDTO = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElse(new UserDTO());
        extraInfoVO.setMobile(userDTO.getMobile());

        Integer integralValue = userIntegralApi.getUserIntegralByUid(staffInfo.getCurrentEid(), IntegralRulePlatformEnum.B2B.getCode());
        extraInfoVO.setIntegralValue(integralValue);

        //订单数量统计
        B2BOrderQuantityDTO orderQuantityDTO = orderB2BApi.countB2BOrderQuantity(staffInfo.getCurrentEid(), OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB);
        log.info("当前登录企业ID：{}，获取订单数量统计：{}", staffInfo.getCurrentEid(), JSONObject.toJSONString(orderQuantityDTO));
        PojoUtils.map(orderQuantityDTO,extraInfoVO);

        //查询采购商为当前企业的账期待还款订单数量
        List<PaymentDaysOrderDTO> paymentDaysOrderDTOList = paymentDaysAccountApi.getUnRepaymentOrderByCustomerEid(staffInfo.getCurrentEid());
        extraInfoVO.setUnRepaymentQuantity(paymentDaysOrderDTOList.size());

        // 是否虚拟账号登录
        boolean specialFlag = userApi.checkSpecialPhone(userDTO.getMobile());
        if (specialFlag) {
            EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), userDTO.getId());
            // 设置虚拟账号需绑定标识
            Date virtualCreateTime = employeeDTO.getCreateTime();
            extraInfoVO.setSpecialPhone(true);
            extraInfoVO.setMustChangeBind(DateUtil.compare(DateUtil.offsetSecond(virtualCreateTime, virtualExpired), new Date()) < 0);
        } else {
            // 设置虚拟账号需绑定标识
            extraInfoVO.setSpecialPhone(false);
            extraInfoVO.setMustChangeBind(false);
        }

        return Result.success(extraInfoVO);
    }

    /**
     * 校验企业资质信息
     * @param enterpriseType 企业类型
     * @param certificateList 企业资质list
     */
    private void checkEnterpriseCertificate(Integer enterpriseType , List<EnterpriseCertificateForm> certificateList) {
        if(CollUtil.isNotEmpty(certificateList)){

            Map<Integer, String> map = certificateList.stream().collect(
                    Collectors.toMap(EnterpriseCertificateForm::getType, EnterpriseCertificateForm::getFileKey, (k1, k2) -> k2));
            Map<Integer, EnterpriseCertificateForm> typeMap = certificateList.stream().collect(
                    Collectors.toMap(EnterpriseCertificateForm::getType, createEnterpriseCertificateRequest -> createEnterpriseCertificateRequest));

            EnterpriseTypeEnum typeCertificateEnum = Optional.ofNullable(EnterpriseTypeEnum.getByCode(enterpriseType))
                    .orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_FIND_CERTIFICATE_TYPE));

            //对必填的和需要证照日期的进行校验
            List<EnterpriseCertificateTypeEnum> must = typeCertificateEnum.getCertificateTypeEnumList();
            for (EnterpriseCertificateTypeEnum enums : must) {
                //必填
                if(enums.getMustExist()){
                    //证照日期必须存在
                    if(enums.getCollectDate()){
                        EnterpriseCertificateForm request = typeMap.get(enums.getCode());
                        if(Objects.isNull(request) || Objects.isNull(request.getPeriodBegin())){
                            throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
                        }

                        if((Objects.isNull(request.getPeriodEnd()) && request.getLongEffective() == 0 )){
                            throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "证照日期必填");
                        }
                    }

                    if (Objects.isNull(map.get(enums.getCode()))) {
                        throw new BusinessException(UserErrorCode.ENTERPRISE_CERTIFICATE_NOT_PASS, enums.getName() + "必填");
                    }
                }
            }
        }

    }

    /**
     * 检查资质信息是否发生了变更
     * @param certificateDtoList
     * @param certificateFromList
     * @return
     */
    private boolean checkCertificateUpdate(List<EnterpriseCertificateDTO> certificateDtoList, List<EnterpriseCertificateForm> certificateFromList) {

        certificateFromList = certificateFromList.stream().filter(enterpriseCertificateForm -> StrUtil.isNotEmpty(enterpriseCertificateForm.getFileKey())).collect(Collectors.toList());

        if(certificateDtoList.size() != certificateFromList.size()){
            return true;
        }

        List<Integer> dtoList = certificateDtoList.stream().map(EnterpriseCertificateDTO::getType).collect(Collectors.toList());

        for(EnterpriseCertificateForm form : certificateFromList){
            for (EnterpriseCertificateDTO certificateDTO : certificateDtoList) {
                if(!dtoList.contains(form.getType())){
                    return true;
                }

                if (EnterpriseCertificateTypeEnum.getByCode(certificateDTO.getType()) == EnterpriseCertificateTypeEnum.getByCode(form.getType())) {
                    if (!form.getFileKey().equals(certificateDTO.getFileKey())) {
                        return true;
                    }
                    if (Objects.nonNull(form.getPeriodBegin()) && !form.getPeriodBegin().equals(certificateDTO.getPeriodBegin())) {
                        return true;
                    }
                    if (Objects.nonNull(form.getPeriodEnd()) && !form.getPeriodEnd().equals(certificateDTO.getPeriodEnd())) {
                        return true;
                    }
                    if (Objects.nonNull(form.getLongEffective()) && !form.getLongEffective().equals(certificateDTO.getLongEffective())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
