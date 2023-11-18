package com.yiling.mall.userderegister.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.cms.question.api.QuestionApi;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.mall.userderegister.dao.UserDeregisterAccountMapper;
import com.yiling.mall.userderegister.entity.UserDeregisterAccountDO;
import com.yiling.mall.userderegister.service.UserDeregisterAccountService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.CountContinueOrderAndReturnDTO;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.system.dto.UserDeregisterAssistantValidDTO;
import com.yiling.user.system.dto.UserDeregisterValidDTO;
import com.yiling.user.system.enums.UserDeregisterAccountStatusEnum;
import com.yiling.user.system.enums.UserTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 注销账号表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Slf4j
@Service
public class UserDeregisterAccountServiceImpl extends BaseServiceImpl<UserDeregisterAccountMapper, UserDeregisterAccountDO> implements UserDeregisterAccountService {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    QuestionApi questionApi;

    @Override
    public List<UserDeregisterValidDTO> checkLogoutAccount(Long userId) {
        // 校验如果存在记录，只有已撤销的才能继续申请注销
        UserDeregisterAccountDO accountDO = this.getByUserId(userId);
        if (Objects.nonNull(accountDO) && !accountDO.getStatus().equals(UserDeregisterAccountStatusEnum.HAD_REVERT.getCode())) {
            throw new BusinessException(UserErrorCode.ACCOUNT_HAD_LOGOUT);
        }

        // 注销账号校验返回信息集合，为空才表示通过校验
        List<UserDeregisterValidDTO> userDeregisterValidDTOList = ListUtil.toList();

        // 获取当前用户属于的企业
        List<EnterpriseDTO> enterpriseDOList = enterpriseApi.listByUserId(userId, EnableStatusEnum.ENABLED);
        List<EnterpriseEmployeeDTO> employeeDOList = employeeApi.listByUserId(userId, EnableStatusEnum.ENABLED);
        Map<Long, Integer> statusMap = employeeDOList.stream().collect(Collectors.toMap(EnterpriseEmployeeDTO::getEid, EnterpriseEmployeeDTO::getAdminFlag));

        // 判断是企业管理员还是普通用户
        enterpriseDOList.forEach(enterpriseDO -> {
            // 是否为企业管理员：0-否 1-是
            Integer adminFlag = statusMap.get(enterpriseDO.getId());
            if (adminFlag == null || adminFlag == 0) {
                return;
            }

            List<String> rejectReasonList = ListUtil.toList();

            // 如果为企业管理员：需要判断所在企业是否存在已发货未签收，退货单未审核，账期未还款
            // 1.查询账期未还款
            List<Long> unRepaymentEidList = paymentDaysAccountApi.getPaymentDaysUnRepayment(enterpriseDO.getId());
            if (CollUtil.isNotEmpty(unRepaymentEidList)) {
                log.info("注销账号企业ID为{} 存在账期未还款的订单对应的账期供应商ID信息：{}", enterpriseDO.getId(), JSONObject.toJSONString(unRepaymentEidList));
                rejectReasonList.add(UserErrorCode.PAYMENT_DAYS_UNREPAYMENT.getMessage());
            }

            // 2.查询已发货未签收、退货单未审核的订单
            CountContinueOrderAndReturnDTO orderAndReturn = orderApi.getCountContinueOrderAndReturn(enterpriseDO.getId());
            if (Objects.nonNull(orderAndReturn) && orderAndReturn.getOrderContinueCount() > 0) {
                log.info("注销账号企业ID为{} 存在未签收的订单 订单数量：{}", enterpriseDO.getId(), orderAndReturn.getOrderContinueCount());
                rejectReasonList.add(UserErrorCode.HAVE_ORDER_NOT_SIGN.getMessage());
            }
            if (Objects.nonNull(orderAndReturn) && orderAndReturn.getOrderReturnContinueCount() > 0) {
                log.info("注销账号企业ID为{} 存在退货单未完结的订单 订单数量：{}", enterpriseDO.getId(), orderAndReturn.getOrderReturnContinueCount());
                rejectReasonList.add(UserErrorCode.HAVE_RETURN_ORDER_EXIST.getMessage());
            }

            if (CollUtil.isNotEmpty(rejectReasonList)) {
                UserDeregisterValidDTO userDeregisterValidDTO = new UserDeregisterValidDTO(enterpriseDO.getId(), enterpriseDO.getName(), rejectReasonList);
                userDeregisterValidDTOList.add(userDeregisterValidDTO);
            }
        });

        return userDeregisterValidDTOList;
    }

    @Override
    public List<UserDeregisterAssistantValidDTO> checkAssistantLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum) {
        // 校验如果存在记录，只有已撤销的才能继续申请注销
        UserDeregisterAccountDO accountDO = this.getByUserId(userId);
        if (Objects.nonNull(accountDO) && !accountDO.getStatus().equals(UserDeregisterAccountStatusEnum.HAD_REVERT.getCode())) {
            throw new BusinessException(UserErrorCode.ACCOUNT_HAD_LOGOUT);
        }

        EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(eid, userId);

        List<String> rejectReasonList = ListUtil.toList();

        // 以岭人员
        if (userTypeEnum == UserTypeEnum.YILING) {
            // 1.企业级别校验（管理员）：不允许注销
            if (Objects.nonNull(employeeDTO) && employeeDTO.getAdminFlag() == 1) {
                throw new BusinessException(UserErrorCode.YILING_ADMIN_DEREGISTER_FAIL);
            }

            // 2.个人级别校验（管理员/非管理员）：药代：未解答的疑问
            this.checkMr(userId, eid, employeeDTO, rejectReasonList);

        } else  if (userTypeEnum == UserTypeEnum.XIAOSANYUAN) {
            // 小三元
            if (Objects.nonNull(employeeDTO) && employeeDTO.getAdminFlag() == 1) {

                CountContinueOrderAndReturnDTO orderAndReturn = orderApi.getCountContinueOrderAssistant(eid);
                if (Objects.nonNull(orderAndReturn)) {
                    // 1.企业级别校验（管理员）：未完结的销售订单/采购订单、未完结的退货单、未收款/未还款的账期
                    if (orderAndReturn.getOrderContinueCount() > 0) {
                        log.info("注销账号企业ID={} 用户ID={} 未完结的采购订单数量：{}", eid, userId, orderAndReturn.getOrderContinueCount());
                        rejectReasonList.add(UserErrorCode.HAVE_UNDO_PURCHASE_ORDER.getMessage());
                    }
                    if (orderAndReturn.getSellerOrderContinueCount() > 0) {
                        log.info("注销账号企业ID={} 用户ID={} 未完结的销售订单数量：{}", eid, userId, orderAndReturn.getOrderReturnContinueCount());
                        rejectReasonList.add(UserErrorCode.HAVE_UNDO_SALE_ORDER.getMessage());
                    }
                    if (orderAndReturn.getOrderReturnContinueCount() > 0) {
                        log.info("注销账号企业ID={} 用户ID={} 未完结的退货单数量：{}", eid, userId, orderAndReturn.getOrderReturnContinueCount());
                        rejectReasonList.add(UserErrorCode.HAVE_RETURN_ORDER_EXIST.getMessage());
                    }
                }

                // 未收款/未还款的账期
                List<Long> unRepaymentEidList = paymentDaysAccountApi.getPaymentDaysUnRepayment(eid);
                if (CollUtil.isNotEmpty(unRepaymentEidList)) {
                    log.info("注销账号企业ID={} 用户iD={} 存在账期未还款的订单对应的账期供应商ID信息：{}", eid, userId, JSONObject.toJSONString(unRepaymentEidList));
                    rejectReasonList.add(UserErrorCode.PAYMENT_DAYS_UNREPAYMENT.getMessage());
                }
                List<Long> unReceiveEidList = paymentDaysAccountApi.getPaymentDaysUnReceive(eid);
                if (CollUtil.isNotEmpty(unReceiveEidList)) {
                    log.info("注销账号企业ID={} 用户iD={} 存在账期未收款的订单对应的账期供应商ID信息：{}", eid, userId, JSONObject.toJSONString(unReceiveEidList));
                    rejectReasonList.add(UserErrorCode.PAYMENT_DAYS_UNRECEIVE.getMessage());
                }
            }

            // 2.个人级别校验（管理员/非管理员）：药代：未解答的疑问
            this.checkMr(userId, eid, employeeDTO, rejectReasonList);

        }

        List<UserDeregisterAssistantValidDTO> list = ListUtil.toList();

        if (CollUtil.isNotEmpty(rejectReasonList)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            UserDeregisterAssistantValidDTO assistantValidDTO = new UserDeregisterAssistantValidDTO();
            assistantValidDTO.setEname(enterpriseDTO.getName());
            assistantValidDTO.setType(1);
            assistantValidDTO.setRejectReasonList(rejectReasonList);
            list.add(assistantValidDTO);
        }

        return list;
    }

    @Override
    public List<UserDeregisterAssistantValidDTO> checkMrLogoutAccount(Long userId, Long eid, UserTypeEnum userTypeEnum) {
        // 校验如果存在记录，只有已撤销的才能继续申请注销
        UserDeregisterAccountDO accountDO = this.getByUserId(userId);
        if (Objects.nonNull(accountDO) && !accountDO.getStatus().equals(UserDeregisterAccountStatusEnum.HAD_REVERT.getCode())) {
            throw new BusinessException(UserErrorCode.ACCOUNT_HAD_LOGOUT);
        }

        // 注销账号校验返回信息集合，为空才表示通过校验
        List<String> rejectReasonList = ListUtil.toList();

        EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(eid, userId);
        // 以岭人员
        if (userTypeEnum == UserTypeEnum.YILING) {
            // 1.企业级别校验（管理员）：不允许注销
            if (Objects.nonNull(employeeDTO) && employeeDTO.getAdminFlag() == 1) {
                throw new BusinessException(UserErrorCode.YILING_ADMIN_DEREGISTER_FAIL);
            }
        }
        // 2.个人级别校验（管理员/非管理员）：药代：未解答的疑问
        this.checkMr(userId, eid, employeeDTO, rejectReasonList);

        List<UserDeregisterAssistantValidDTO> list = ListUtil.toList();

        if (CollUtil.isNotEmpty(rejectReasonList)) {
            UserDeregisterAssistantValidDTO assistantValidDTO = new UserDeregisterAssistantValidDTO();
            assistantValidDTO.setEname(Optional.ofNullable(enterpriseApi.getById(eid)).orElse(new EnterpriseDTO()).getName());
            assistantValidDTO.setType(1);
            assistantValidDTO.setRejectReasonList(rejectReasonList);
            list.add(assistantValidDTO);
        }

        return list;
    }

    /**
     * 个人级别校验-药代
     *
     * @param userId 用户ID
     * @param eid 企业ID
     * @param employeeDTO 企业员工
     * @param rejectReasonList 拒绝原因
     */
    private void checkMr(Long userId, Long eid, EnterpriseEmployeeDTO employeeDTO, List<String> rejectReasonList) {
        // 2.个人级别校验（管理员/非管理员）：药代：未解答的疑问
        if (Objects.nonNull(employeeDTO) && EmployeeTypeEnum.getByCode(employeeDTO.getType()) == EmployeeTypeEnum.MR) {
            Integer notReplyQuestion = questionApi.getNotReplyQuestion(userId);
            if (notReplyQuestion > 0) {
                log.info("注销账号企业ID={} 用户ID={} 存在未解答的疑问数量：{}", eid, userId, notReplyQuestion);
                rejectReasonList.add(UserErrorCode.HAVE_NO_ANSWER_QUESTION.getMessage());
            }
        }
    }

    public UserDeregisterAccountDO getByUserId(Long userId) {
        LambdaQueryWrapper<UserDeregisterAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDeregisterAccountDO::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

}
