package com.yiling.user.agreementv2.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.bo.AgreementPaymentMethodBO;
import com.yiling.user.agreementv2.bo.AgreementSettlementMethodBO;
import com.yiling.user.agreementv2.bo.AgreementSettlementTermsBO;
import com.yiling.user.agreementv2.dto.request.AddAgreementPaymentMethodRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSettlementMethodRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSettlementTermsRequest;
import com.yiling.user.agreementv2.entity.AgreementPaymentMethodDO;
import com.yiling.user.agreementv2.entity.AgreementSettlementMethodDO;
import com.yiling.user.agreementv2.service.AgreementPaymentMethodService;
import com.yiling.user.agreementv2.service.AgreementSettlementMethodService;
import com.yiling.user.agreementv2.service.AgreementSettlementTermsService;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议结算条款 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Slf4j
@Service
public class AgreementSettlementTermsServiceImpl implements AgreementSettlementTermsService {

    @Autowired
    private AgreementSettlementMethodService agreementSettlementMethodService;
    @Autowired
    private AgreementPaymentMethodService agreementPaymentMethodService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAgreementSettlementTerms(AddAgreementSettlementTermsRequest request) {
        // 支付方式
        List<AddAgreementPaymentMethodRequest> paymentMethodRequestList = request.getAgreementPaymentMethodList();
        if (CollUtil.isNotEmpty(paymentMethodRequestList)) {
            List<AgreementPaymentMethodDO> agreementPaymentMethodDoList = PojoUtils.map(paymentMethodRequestList, AgreementPaymentMethodDO.class);
            agreementPaymentMethodDoList.forEach(agreementPaymentMethodDO -> {
                agreementPaymentMethodDO.setAgreementId(request.getAgreementId());
                agreementPaymentMethodDO.setOpUserId(request.getOpUserId());
            });
            agreementPaymentMethodService.saveBatch(agreementPaymentMethodDoList);
        }

        // 结算方式
        AddAgreementSettlementMethodRequest settlementMethodRequest = request.getAgreementSettlementMethod();
        if (Objects.nonNull(settlementMethodRequest)) {
            AgreementSettlementMethodDO settlementMethodDO = PojoUtils.map(settlementMethodRequest, AgreementSettlementMethodDO.class);
            settlementMethodDO.setAgreementId(request.getAgreementId());
            settlementMethodDO.setAdvancePaymentFlag(settlementMethodRequest.getAdvancePaymentFlag() ? 1 : 0);
            settlementMethodDO.setPaymentDaysFlag(settlementMethodRequest.getPaymentDaysFlag() ? 1 : 0);
            settlementMethodDO.setActualSalesSettlementFlag(settlementMethodRequest.getActualSalesSettlementFlag() ? 1 : 0);
            settlementMethodDO.setPayDeliveryFlag(settlementMethodRequest.getPayDeliveryFlag() ? 1 : 0);
            settlementMethodDO.setPressGroupFlag(settlementMethodRequest.getPressGroupFlag() ? 1 : 0);
            settlementMethodDO.setCreditFlag(settlementMethodRequest.getCreditFlag() ? 1 : 0);
            settlementMethodDO.setOpUserId(request.getOpUserId());
            settlementMethodDO.setBuyerId(Objects.isNull(settlementMethodDO.getBuyerId()) ? request.getOpUserId() : settlementMethodDO.getBuyerId());
            agreementSettlementMethodService.save(settlementMethodDO);
        }

        return true;
    }

    @Override
    public AgreementSettlementTermsBO getAgreementSettlementTerms(Long agreementId) {
        long time = System.currentTimeMillis();
        AgreementSettlementTermsBO settlementTermsBO = new AgreementSettlementTermsBO();

        List<AgreementPaymentMethodDO> paymentMethodDOList = agreementPaymentMethodService.queryList(agreementId);
        settlementTermsBO.setAgreementPaymentMethodList(PojoUtils.map(paymentMethodDOList, AgreementPaymentMethodBO.class));

        AgreementSettlementMethodDO settlementMethodDO = agreementSettlementMethodService.getByAgreementId(agreementId);
        AgreementSettlementMethodBO settlementMethodBO = PojoUtils.map(settlementMethodDO, AgreementSettlementMethodBO.class);
        settlementMethodBO.setActualSalesSettlementFlag(settlementMethodDO.getActualSalesSettlementFlag() == 1);
        settlementMethodBO.setAdvancePaymentFlag(settlementMethodDO.getAdvancePaymentFlag() == 1);
        settlementMethodBO.setCreditFlag(settlementMethodDO.getCreditFlag() == 1);
        settlementMethodBO.setPayDeliveryFlag(settlementMethodDO.getPayDeliveryFlag() == 1);
        settlementMethodBO.setPaymentDaysFlag(settlementMethodDO.getPaymentDaysFlag() == 1);
        settlementMethodBO.setPressGroupFlag(settlementMethodDO.getPressGroupFlag() == 1);
        settlementMethodBO.setBuyerName(Optional.ofNullable(userService.getById(settlementMethodDO.getBuyerId())).orElse(new UserDO()).getName());
        settlementTermsBO.setAgreementSettlementMethod(settlementMethodBO);
        log.debug("获取协议结算条款共耗时：{}", System.currentTimeMillis() - time);

        return settlementTermsBO;
    }

    /**
     * 基础校验
     *
     * @param request 请求参数
     */
    @Override
    public void checkSettlementTerms(AddAgreementSettlementTermsRequest request) {
        AddAgreementSettlementMethodRequest agreementSettlementMethod = request.getAgreementSettlementMethod();
        if (Objects.nonNull(agreementSettlementMethod)) {
            // 如果为账期结算，则结算周期必填
            if (agreementSettlementMethod.getPaymentDaysFlag()) {
                if (Objects.isNull(agreementSettlementMethod.getPaymentDaysSettlementPeriod())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            }

            // 如果为实销实结，则周期、结算日等必填
            if (agreementSettlementMethod.getActualSalesSettlementFlag()) {
                if (Objects.isNull(agreementSettlementMethod.getActualSalesSettlementPeriod())
                        || Objects.isNull(agreementSettlementMethod.getActualSalesSettlementDay())
                        || Objects.isNull(agreementSettlementMethod.getActualSalesSettlementPayDays())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            }

            // 如果为压批，则压批次数必填
            if (agreementSettlementMethod.getPressGroupFlag()) {
                if (Objects.isNull(agreementSettlementMethod.getPressGroupNumber())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            }

            // 如果为授信，则授信金额必填
            if (agreementSettlementMethod.getCreditFlag()) {
                if (Objects.isNull(agreementSettlementMethod.getCreditAmount())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            }
        }
    }
}
