package com.yiling.user.payment.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.PaymentTemporaryAuditStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.MainPartEnterpriseDTO;
import com.yiling.user.enterprise.entity.EnterpriseCustomerContactDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.service.EnterpriseCustomerContactService;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.payment.bo.PaymentDaysAmountBO;
import com.yiling.user.payment.dao.PaymentDaysAccountMapper;
import com.yiling.user.payment.dao.PaymentDaysTemporaryRecordMapper;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderAmountCountDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.PaymentRepaymentOrderDTO;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.ShortTimeQuotaDTO;
import com.yiling.user.payment.dto.request.ApplyPaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.CreatePaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.PaymentDaysCompanyRequest;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.QueryShortTimeQuotaAccountRequest;
import com.yiling.user.payment.dto.request.ReduceQuotaRequest;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;
import com.yiling.user.payment.dto.request.UpdateInvoiceTicketDiscountRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentDaysAccountRequest;
import com.yiling.user.payment.entity.PaymentDaysAccountDO;
import com.yiling.user.payment.entity.PaymentDaysAccountLogDO;
import com.yiling.user.payment.entity.PaymentDaysCompanyDO;
import com.yiling.user.payment.entity.PaymentDaysOrderDO;
import com.yiling.user.payment.entity.PaymentDaysTemporaryRecordDO;
import com.yiling.user.payment.enums.PaymentAccountTypeEnum;
import com.yiling.user.payment.service.PaymentDaysAccountLogService;
import com.yiling.user.payment.service.PaymentDaysAccountService;
import com.yiling.user.payment.service.PaymentDaysCompanyService;
import com.yiling.user.payment.service.PaymentDaysOrderService;
import com.yiling.user.payment.service.PaymentDaysTemporaryRecordService;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.enums.PaymentDaysLogTypeEnum;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 账期账户 Service 实现
 *
 * @author xuan.zhou
 * @date 2021/7/1
 */
@Service
@Slf4j
public class PaymentDaysAccountServiceImpl extends BaseServiceImpl<PaymentDaysAccountMapper, PaymentDaysAccountDO> implements PaymentDaysAccountService {

    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    PaymentDaysCompanyService paymentDaysCompanyService;
    @Autowired
    PaymentDaysOrderService paymentDaysOrderService;
    @Autowired
    PaymentDaysTemporaryRecordService paymentDaysTemporaryRecordService;
    @Autowired
    PaymentDaysAccountMapper paymentDaysAccountMapper;
    @Autowired
    PaymentDaysTemporaryRecordMapper paymentDaysTemporaryRecordMapper;
    @Autowired
    PaymentDaysAccountLogService paymentDaysAccountLogService;
    @Autowired
    UserService userService;
    @Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;
    @Autowired
    EnterpriseCustomerContactService enterpriseCustomerContactService;

    @Override
    public Page<PaymentDaysAccountDO> pageList(QueryPaymentDaysAccountPageListRequest request) {
        QueryWrapper<PaymentDaysAccountDO> queryWrapper = getPaymentDaysAccountQueryWrapper(request);
        if(Objects.isNull(queryWrapper)){
            return new Page<>();
        }

        return this.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
    }

    /**
     * 组装账期账户分页列表查询条件
     * @param request
     * @return
     */
    private QueryWrapper<PaymentDaysAccountDO> getPaymentDaysAccountQueryWrapper(QueryPaymentDaysAccountPageListRequest request) {
        QueryWrapper<PaymentDaysAccountDO> queryWrapper = new QueryWrapper<>();

        Integer status = request.getStatus();
        if (status != null && status != 0) {
            queryWrapper.lambda().eq(PaymentDaysAccountDO::getStatus, status);
        }

        List<Long> eidList =  request.getEidList();

        //类型：1-账期额度管理列表 2-采购商账期列表
        int type = request.getType() != null ? request.getType() : 1;
        if (type == 1){
            if (CollectionUtils.isNotEmpty(eidList)) {
                queryWrapper.lambda().in(PaymentDaysAccountDO::getEid, eidList);
            } else if (Objects.nonNull(request.getCurrentEid()) && request.getCurrentEid() != 0) {
                queryWrapper.lambda().eq(PaymentDaysAccountDO::getEid, request.getCurrentEid());
            }
            // 以岭的账期额度管理-商务，展示的数据是采购商的商务联系人为当前登录用户的账期信息
            if( CollUtil.isNotEmpty(request.getContactUserList())){
                LambdaQueryWrapper<EnterpriseCustomerContactDO> queryWrapperContact = new LambdaQueryWrapper<>();
                queryWrapperContact.in(EnterpriseCustomerContactDO::getContactUserId,request.getContactUserList());
                List<Long> customEidList = enterpriseCustomerContactService.list(queryWrapperContact)
                        .stream().map(EnterpriseCustomerContactDO::getCustomerEid).distinct().collect(Collectors.toList());
                if(CollUtil.isNotEmpty(customEidList)){
                    queryWrapper.lambda().in(PaymentDaysAccountDO::getCustomerEid,customEidList);
                }else{
                    return null;
                }
            }

        }else {
            if(Objects.nonNull(request.getCurrentEid()) && request.getCurrentEid() != 0){
                queryWrapper.lambda().eq(PaymentDaysAccountDO::getCustomerEid, request.getCurrentEid());
            }
        }

        if (StrUtil.isNotBlank(request.getCustomerName())) {
            queryWrapper.lambda().like(PaymentDaysAccountDO::getCustomerName, request.getCustomerName());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            queryWrapper.lambda().like(PaymentDaysAccountDO::getEname, request.getEname());
        }
        queryWrapper.lambda().orderByDesc(PaymentDaysAccountDO::getCreateTime).orderByDesc(PaymentDaysAccountDO::getId);

        return queryWrapper;
    }

    @Override
    public Page<QuotaOrderDTO> quotaOrderPage(QueryQuotaOrderRequest request) {
        getQuotaOrderPageQueryWrapper(request);
        Page<PaymentDaysOrderDO> page = paymentDaysOrderService.getQuotaOrderPage(request);

        return PojoUtils.map(page,QuotaOrderDTO.class);
    }

    @Override
    public PaymentDaysOrderAmountCountDTO getOrderAmountCount(QueryQuotaOrderRequest request) {
        PaymentDaysAccountDO daysAccountDo = Optional.ofNullable(this.getById(request.getAccountId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS));

        getQuotaOrderPageQueryWrapper(request);
        List<PaymentDaysOrderDO> list = paymentDaysOrderService.getQuotaOrderList(request);

        BigDecimal usedAmount = list.stream().map(PaymentDaysOrderDO::getUsedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal returnAmount = list.stream().map(PaymentDaysOrderDO::getReturnAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal repaymentAmount = list.stream().map(PaymentDaysOrderDO::getRepaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        //订单金额（支付的时候是多少钱，那订单金额就是多少钱） ，已使用金额 = 订单金额 - 退款金额
        BigDecimal sumUsedAmount = usedAmount.subtract(returnAmount);
        //待还款金额 = 订单金额 - 退款金额 - 已还款金额
        BigDecimal unRepaymentAmount = usedAmount.subtract(returnAmount).subtract(repaymentAmount);

        return new PaymentDaysOrderAmountCountDTO().setOrderAmount(usedAmount).setUsedAmount(sumUsedAmount)
                .setRepaymentAmount(repaymentAmount).setReturnAmount(returnAmount).setUnRepaymentAmount(unRepaymentAmount).setEname(daysAccountDo.getEname())
                .setCustomerName(daysAccountDo.getCustomerName()).setType(daysAccountDo.getType());
    }

    @Override
    public PaymentDaysAccountDTO getPaymentDaysAccountById(Long accountId) {
        return PojoUtils.map(this.getById(accountId),PaymentDaysAccountDTO.class);
    }

    @Override
    public List<PaymentDaysAccountDTO> getByCustomerEid(Long customerEid) {
        LambdaQueryWrapper<PaymentDaysAccountDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentDaysAccountDO::getCustomerEid,customerEid);
        queryWrapper.ne(PaymentDaysAccountDO::getType,PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode());

        List<Long> eidList = enterpriseService.listSubEids(Constants.YILING_EID);
        eidList.add(Constants.YILING_EID);
        //根据产品需求：pop里授信的账期理论上授信主体都是以岭，这样的账期不要查询给B2B；大运河授权的也不要查询给B2B
        queryWrapper.notIn(PaymentDaysAccountDO::getEid,eidList);

        return PojoUtils.map(this.list(queryWrapper),PaymentDaysAccountDTO.class);
    }

    /**
     * 根据采购商ID查询存在未还款的账期供应商ID
     *
     * @param customerEid
     * @return
     */
    @Override
    public List<Long> getPaymentDaysUnRepayment(Long customerEid) {
        return getIdList(customerEid, null);
    }

    /**
     * 根据供应商ID查询存在未收款的账期的供应商ID
     *
     * @param eid
     * @return
     */
    @Override
    public List<Long> getPaymentDaysUnReceive(Long eid) {
        return getIdList(null, eid);
    }

    private List<Long> getIdList(Long customerEid, Long eid) {
        List<Long> idList = ListUtil.toList();
        LambdaQueryWrapper<PaymentDaysAccountDO> wrapper = new LambdaQueryWrapper<>();
        if (customerEid != null) {
            wrapper.eq(PaymentDaysAccountDO::getCustomerEid, customerEid);
        } else {
            wrapper.eq(PaymentDaysAccountDO::getEid, eid);
        }
        wrapper.eq(PaymentDaysAccountDO::getStatus, EnableStatusEnum.ENABLED.getCode());
        List<PaymentDaysAccountDO> list = this.list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return idList;
        }

        list.forEach(paymentDaysAccountDO -> {
            // 计算待还款的金额 = 订单金额 - 退款金额 - 已还款金额
            QueryQuotaOrderRequest orderRequest = new QueryQuotaOrderRequest();
            orderRequest.setAccountId(paymentDaysAccountDO.getId());
            orderRequest.setType(3);
            List<PaymentDaysOrderDO> paymentDaysOrderDOList = paymentDaysOrderService.getQuotaOrderList(orderRequest);

            if (CollUtil.isEmpty(paymentDaysOrderDOList)) {
                return;
            }
            BigDecimal usedAmount = paymentDaysOrderDOList.stream().map(PaymentDaysOrderDO::getUsedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal returnAmount = paymentDaysOrderDOList.stream().map(PaymentDaysOrderDO::getReturnAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal repaymentAmount = paymentDaysOrderDOList.stream().map(PaymentDaysOrderDO::getRepaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 待还款金额 = 订单金额 - 退款金额 - 已还款金额
            BigDecimal unRepaymentAmount = usedAmount.subtract(returnAmount).subtract(repaymentAmount);
            if (unRepaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                idList.add(paymentDaysAccountDO.getEid());
            }
        });

        return idList;
    }

    @Override
    public PaymentRepaymentOrderDTO getRepaymentOrderAmount(QueryQuotaOrderRequest request) {
        PaymentDaysAccountDO daysAccountDo = Optional.ofNullable(this.getById(request.getAccountId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS));

        getQuotaOrderPageQueryWrapper(request);
        List<PaymentDaysOrderDO> list = paymentDaysOrderService.getQuotaOrderList(request);

        BigDecimal usedAmount = list.stream().map(PaymentDaysOrderDO::getUsedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal returnAmount = list.stream().map(PaymentDaysOrderDO::getReturnAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal repaymentAmount = list.stream().map(PaymentDaysOrderDO::getRepaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal orderAmount;
        BigDecimal totalUseAmount;
        BigDecimal historyUseAmount = BigDecimal.ZERO;
        BigDecimal historyRepaymentAmount = BigDecimal.ZERO;
        BigDecimal totalRepaymentAmount;
        if(request.getPaymentDaysType() == null || request.getPaymentDaysType() == 1){
            //订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额)
            //POP账期：订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额) 即 订单金额 = 使用金额 - 驳回退货单的退款金额
            orderAmount = usedAmount.subtract(returnAmount);

            //已使用额度=历史已使用额度 + 该企业所有订单的‘订单金额’的和
            historyUseAmount = daysAccountDo.getHistoryUseAmount();
            totalUseAmount = historyUseAmount.add(orderAmount);

            //已还款额度=历史已还款额度 + 该企业所有订单的‘已还款金额’的和
            historyRepaymentAmount = daysAccountDo.getHistoryRepaymentAmount();
            totalRepaymentAmount = historyRepaymentAmount.add(repaymentAmount);

        } else {
            //B2B账期：订单金额 = 支付总金额
            orderAmount = usedAmount;
            totalUseAmount = orderAmount.subtract(returnAmount);
            totalRepaymentAmount = repaymentAmount;

        }
        //待还款金额 = 订单金额 – 退款金额 - 已还款金额
        BigDecimal unRepaymentAmount = orderAmount.subtract(returnAmount).subtract(repaymentAmount);

        return new PaymentRepaymentOrderDTO().setOrderAmount(orderAmount).setReturnAmount(returnAmount).setRepaymentAmount(repaymentAmount)
                .setUnRepaymentAmount(unRepaymentAmount).setType(daysAccountDo.getType())
                .setEname(daysAccountDo.getEname()).setCustomerName(daysAccountDo.getCustomerName())
                .setHistoryUsedAmount(historyUseAmount).setTotalUsedAmount(totalUseAmount)
                .setHistoryRepaymentAmount(historyRepaymentAmount).setTotalRepaymentAmount(totalRepaymentAmount);
    }

    /**
     * 组装账期绑定订单列表查询条件
     * @param request
     * @return
     */
    private void getQuotaOrderPageQueryWrapper(QueryQuotaOrderRequest request) {
        if (request.getStartTime()!=null) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (request.getEndTime()!=null) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
    }

    @Override
    public Page<ShortTimeQuotaDTO> shortTimeQuotaPage(QueryShortTimeQuotaAccountRequest request) {

        QueryWrapper<PaymentDaysTemporaryRecordDO> queryWrapper = getShortTimeQuotaPageQueryWrapper(request);
        Page<PaymentDaysTemporaryRecordDO> page = paymentDaysTemporaryRecordService.page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);

        List<Long> userIdList = page.getRecords().stream().map(PaymentDaysTemporaryRecordDO::getUpdateUser).distinct().collect(Collectors.toList());
        Map<Long, String> userMap = MapUtil.newHashMap();
        if(CollUtil.isNotEmpty(userIdList)){
            userMap = userService.listByIds(userIdList).stream().collect(Collectors.toMap(UserDO::getId, UserDO::getName, (k1, k2) -> k2));
        }

        Page<ShortTimeQuotaDTO> timeQuotaDtoPage = PojoUtils.map(page,ShortTimeQuotaDTO.class);
        List<ShortTimeQuotaDTO> shortTimeQuotaList = getShortTimeQuotaDtoList(page, userMap);
        timeQuotaDtoPage.setRecords(shortTimeQuotaList);
        return timeQuotaDtoPage;

    }

    /**
     * 根据临时额度记录数据处理
     * @param page
     * @param userMap
     * @return
     */
    private List<ShortTimeQuotaDTO> getShortTimeQuotaDtoList(Page<PaymentDaysTemporaryRecordDO> page, Map<Long, String> userMap) {
        return page.getRecords().stream().map(paymentDaysTemporaryRecordDO -> {
            PaymentDaysAccountDO daysAccountDo = this.getById(paymentDaysTemporaryRecordDO.getAccountId());

            ShortTimeQuotaDTO shortTimeQuotaDTO = PojoUtils.map(daysAccountDo,ShortTimeQuotaDTO.class);
            shortTimeQuotaDTO.setShortTimeQuota(paymentDaysTemporaryRecordDO.getTemporatyAmount());
            shortTimeQuotaDTO.setAfterAgreementTotalAmount(shortTimeQuotaDTO.getAvailableAmount().add(paymentDaysTemporaryRecordDO.getTemporatyAmount()));
            shortTimeQuotaDTO.setAuditStatus(paymentDaysTemporaryRecordDO.getAuditStatus());
            shortTimeQuotaDTO.setCreateTime(paymentDaysTemporaryRecordDO.getCreateTime());

            //已使用额度=历史已使用额度 + 该企业所有订单的‘订单金额’的和
            shortTimeQuotaDTO.setUsedAmount(daysAccountDo.getUsedAmount().add(daysAccountDo.getHistoryUseAmount()));
            //已还款额度=历史已还款额度 + 该企业所有订单的‘已还款金额’的和
            shortTimeQuotaDTO.setRepaymentAmount(daysAccountDo.getRepaymentAmount().add(daysAccountDo.getHistoryRepaymentAmount()));
            //未还款额度 = 已使用额度 – 已还款额度
            shortTimeQuotaDTO.setUnRepaymentAmount(shortTimeQuotaDTO.getUsedAmount().subtract(shortTimeQuotaDTO.getRepaymentAmount()));
            //到期未还款额度 = 账期到期的订单的待还款额度的和
            BigDecimal exprUnRepaymentAmount = getExprUnRepaymentAmount(daysAccountDo.getId());
            shortTimeQuotaDTO.setExprUnRepaymentAmount(exprUnRepaymentAmount);

            if(!paymentDaysTemporaryRecordDO.getAuditStatus().equals(PaymentTemporaryAuditStatusEnum.UNAUDIT.getCode())){
                shortTimeQuotaDTO.setUpdateUser(userMap.get(paymentDaysTemporaryRecordDO.getUpdateUser()));
                shortTimeQuotaDTO.setUpdateUserId(paymentDaysTemporaryRecordDO.getUpdateUser());
                shortTimeQuotaDTO.setUpdateTime(paymentDaysTemporaryRecordDO.getUpdateTime());
            }else{
                shortTimeQuotaDTO.setUpdateUser(null);
                shortTimeQuotaDTO.setUpdateTime(null);
            }
            return shortTimeQuotaDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取到期未还款金额
     * @param accountId
     * @return
     */
    private BigDecimal getExprUnRepaymentAmount(Long accountId) {
        LambdaQueryWrapper<PaymentDaysOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentDaysOrderDO::getAccountId, accountId);
        queryWrapper.le(PaymentDaysOrderDO::getExpirationTime, new Date());
        queryWrapper.last("and used_amount - repayment_amount - return_amount > 0");
        List<PaymentDaysOrderDO> daysOrderDoList = paymentDaysOrderService.list(queryWrapper);
        BigDecimal totalReturnAmount = daysOrderDoList.stream().map(PaymentDaysOrderDO::getReturnAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalUseAmount = daysOrderDoList.stream().map(PaymentDaysOrderDO::getUsedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalRepaymentAmount = daysOrderDoList.stream().map(PaymentDaysOrderDO::getRepaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalUseAmount.subtract(totalReturnAmount).subtract(totalRepaymentAmount);
    }

    /**
     * 组装临时额度申请分页列表查询条件
     * @param request
     * @return
     */
    private QueryWrapper<PaymentDaysTemporaryRecordDO> getShortTimeQuotaPageQueryWrapper(QueryShortTimeQuotaAccountRequest request) {
        QueryWrapper<PaymentDaysTemporaryRecordDO> queryWrapper = new QueryWrapper<>();

        int status = request.getStatus();
        if (status != 0) {
            queryWrapper.lambda().eq(PaymentDaysTemporaryRecordDO::getAuditStatus, status);
        }

        List<Long> eidList = request.getEidList();
        if (CollectionUtils.isNotEmpty(eidList)) {
            queryWrapper.lambda().in(PaymentDaysTemporaryRecordDO::getEid, eidList);
        }

        if(StrUtil.isNotEmpty(request.getCustomerName())){
            queryWrapper.lambda().like(PaymentDaysTemporaryRecordDO::getCustomerName, request.getCustomerName());
        }

        if (Objects.nonNull(request.getOrderType()) && request.getOrderType() == 1) {
            queryWrapper.lambda().orderByDesc(PaymentDaysTemporaryRecordDO::getCreateTime);
        } else {
            //根据2021年8月13日产品需求：排序顺序：待审核、已拒绝、已通过，状态相同的按照时间正序排列
            queryWrapper.lambda().last("ORDER BY CASE audit_status WHEN 1 THEN 1 WHEN 3 THEN 2 WHEN 2 THEN 3 END, create_time asc ");
        }

        return queryWrapper;
    }

    @Override
    public Page<PaymentDaysOrderDTO> expireDayOrderPage(QueryExpireDayOrderRequest request) {
        Page<PaymentDaysOrderDO> page = paymentDaysOrderService.pageList(request);
        return PojoUtils.map(page,PaymentDaysOrderDTO.class);
    }

    @Override
    public boolean create(CreatePaymentDaysAccountRequest request) {
        QueryWrapper<PaymentDaysAccountDO> queryWrapper = new QueryWrapper<>();
        PaymentDaysAccountDO entity = PojoUtils.map(request, PaymentDaysAccountDO.class);
        queryWrapper.lambda().eq(PaymentDaysAccountDO::getCustomerEid, request.getCustomerEid());
        queryWrapper.lambda().eq(PaymentDaysAccountDO::getEid, request.getEid());

        List<PaymentDaysAccountDO> list = this.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(list)){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_EXISTS);
        }

        entity.setAvailableAmount(request.getTotalAmount());
        if(PaymentAccountTypeEnum.getByCode(request.getType()) == PaymentAccountTypeEnum.NOT_YL_PAYMENT_ACCOUNT){
            if(request.getPlatformType() != PlatformEnum.B2B){
                entity.setTotalAmount(BigDecimal.ZERO);
            }
        }

        if(request.getPlatformType() != PlatformEnum.B2B){
            entity.setStartTime(DateUtil.beginOfDay(entity.getStartTime()));
            if (Objects.nonNull(entity.getEndTime())) {
                entity.setEndTime(DateUtil.endOfDay(entity.getEndTime()));
            }
        }

        entity.setCustomerName(Optional.ofNullable(enterpriseService.getById(request.getCustomerEid())).orElse(new EnterpriseDO()).getName());
        entity.setEname(Optional.ofNullable(enterpriseService.getById(request.getEid())).orElse(new EnterpriseDO()).getName());

        return this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdatePaymentDaysAccountRequest request) {
        PaymentDaysAccountDO paymentDaysAccountDO = PojoUtils.map(request, PaymentDaysAccountDO.class);
        PaymentDaysAccountDO daysAccountDO = Optional.ofNullable(this.getById(request.getId()))
                .orElseThrow(()->new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS));

        paymentDaysAccountDO.setPeriod(request.getPeriod());
        if(request.getPlatformType() != PlatformEnum.B2B){
            paymentDaysAccountDO.setStartTime(DateUtil.beginOfDay(paymentDaysAccountDO.getStartTime()));
            if (Objects.nonNull(paymentDaysAccountDO.getEndTime())) {
                paymentDaysAccountDO.setEndTime(DateUtil.endOfDay(paymentDaysAccountDO.getEndTime()));
            }
        }
        paymentDaysAccountDO.setUpdateUser(request.getOpUserId());

        //如果为以岭则更新账期金额、可用金额；如果为非以岭只更新可用金额；如果为工业直属的也当做以岭来处理
        if (daysAccountDO.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode()) ||
                daysAccountDO.getType().equals(PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode()) || PlatformEnum.B2B == request.getPlatformType()) {
            paymentDaysAccountDO.setTotalAmount(request.getTotalAmount());
            paymentDaysAccountDO.setAvailableAmount(request.getTotalAmount().subtract(daysAccountDO.getTotalAmount()));
            return paymentDaysAccountMapper.yilingUpdate(paymentDaysAccountDO) > 0;
        } else {
            paymentDaysAccountDO.setAvailableAmount(request.getTotalAmount());
            return paymentDaysAccountMapper.feiYilingUpdate(paymentDaysAccountDO) > 0;
        }
    }

    @Override
    public PaymentDaysAccountDO getByCustomerEid(Long eid, Long customerEid) {
        LambdaQueryWrapper<PaymentDaysAccountDO> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery
                .eq(PaymentDaysAccountDO::getEid, eid)
                .eq(PaymentDaysAccountDO::getCustomerEid, customerEid)
                .last("limit 1");
        return this.getOne(lambdaQuery);
    }

    @Override
    public BigDecimal getAvailableAmountByCustomerEid(Long eid, Long customerEid ,PlatformEnum platformEnum) {
        PaymentDaysAccountDO entity = this.getByCustomerEid(eid, customerEid);
        if (entity == null) {
            return BigDecimal.ZERO;
        }

        if (EnableStatusEnum.getByCode(entity.getStatus()) != EnableStatusEnum.ENABLED) {
            return BigDecimal.ZERO;
        }

        //不为B2B才校验开始结束时间
        if (Objects.isNull(platformEnum) || PlatformEnum.B2B != platformEnum) {
            Date now = new Date();
            if (now.before(entity.getStartTime())) {
                return BigDecimal.ZERO;
            }
            if (entity.getLongEffective() == 0 && now.after(entity.getEndTime())) {
                return BigDecimal.ZERO;
            }
        }

        return entity.getTotalAmount().add(entity.getTemporaryAmount()).add(entity.getRepaymentAmount()).subtract(entity.getUsedAmount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reduceQuota(ReduceQuotaRequest request) {
        // 账户校验
        PaymentDaysAccountDO account = Optional.ofNullable(this.getByCustomerEid(request.getEid(), request.getCustomerEid()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS));

        // 账户状态校验
        if (EnableStatusEnum.getByCode(account.getStatus()) == EnableStatusEnum.DISABLED) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_DISABLED);
        }
        log.info("扣减账期额度接口请求入参：{}，账期账户信息：{}", JSONObject.toJSONString(request) , JSONObject.toJSONString(account));

        // 账户有效期校验
        Date now = new Date();
        if(Objects.isNull(request.getPlatformEnum()) || request.getPlatformEnum() == PlatformEnum.POP){
            if (now.before(account.getStartTime())) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_OUT_OF_CYCLE);
            }
            if (account.getLongEffective() == 0 && now.after(account.getEndTime())) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_OUT_OF_CYCLE);
            }
        }

        //幂等校验：同一笔订单禁止重复支付
        LambdaQueryWrapper<PaymentDaysOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentDaysOrderDO::getOrderId, request.getOrderId());
        queryWrapper.eq(PaymentDaysOrderDO::getAccountId,account.getId());
        PaymentDaysOrderDO daysOrder = paymentDaysOrderService.getOne(queryWrapper);
        if(Objects.nonNull(daysOrder)){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_HAD_PAY);
        }

        // 本次使用额度
        BigDecimal thisTimeUseAmount = request.getUseAmount();

        //根据账户类型区分扣款方式
        if(account.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode()) ||
                account.getType().equals(PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode()) || PlatformEnum.B2B == request.getPlatformEnum()){
            // 账户可用额度
            BigDecimal availableAmount = account.getTotalAmount().add(account.getTemporaryAmount()).subtract(account.getUsedAmount()).add(account.getRepaymentAmount());
            if (availableAmount.compareTo(thisTimeUseAmount) < 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CUSTOMER_BALANCE_NOT_ENOUGH);
            }
            if(account.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode())){
                //1.扣减集团额度，只有以岭的才需要使用集团额度，非以岭和工业都不需要
                reduceCompanyAmount(thisTimeUseAmount);
            }

            // 2、使用客户额度
            int n = this.baseMapper.use(account.getId(), thisTimeUseAmount);
            if (n == 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CUSTOMER_BALANCE_NOT_ENOUGH);
            }
        }else{
            // 账户可用额度
            BigDecimal availableAmount = account.getAvailableAmount();
            if (availableAmount.compareTo(thisTimeUseAmount) < 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CUSTOMER_BALANCE_NOT_ENOUGH);
            }

            // 2、使用客户额度
            int n = this.baseMapper.feiYilingUse(account.getId(), thisTimeUseAmount);
            if (n == 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CUSTOMER_BALANCE_NOT_ENOUGH);
            }
        }

        // 3、保存账期订单明细
        PaymentDaysOrderDO paymentDaysOrderDO = new PaymentDaysOrderDO();
        paymentDaysOrderDO.setOrderId(request.getOrderId());
        paymentDaysOrderDO.setOrderNo(request.getOrderNo());
        paymentDaysOrderDO.setEid(account.getEid());
        paymentDaysOrderDO.setEname(account.getEname());
        paymentDaysOrderDO.setCustomerEid(account.getCustomerEid());
        paymentDaysOrderDO.setCustomerName(account.getCustomerName());
        paymentDaysOrderDO.setAccountId(account.getId());
        paymentDaysOrderDO.setPeriod(account.getPeriod());
        paymentDaysOrderDO.setUsedAmount(thisTimeUseAmount);
        paymentDaysOrderDO.setUsedTime(now);
        paymentDaysOrderDO.setExpirationTime(DateUtil.offsetDay(now, account.getPeriod()));
        paymentDaysOrderDO.setOpUserId(request.getOpUserId());
        paymentDaysOrderService.save(paymentDaysOrderDO);

        // 4、保存账期额度使用日志
        PaymentDaysAccountLogDO paymentDaysAccountLogDO = new PaymentDaysAccountLogDO();
        paymentDaysAccountLogDO.setAccountId(account.getId());
        paymentDaysAccountLogDO.setBusinessNo(request.getOrderNo());
        paymentDaysAccountLogDO.setBusinessType(PaymentDaysLogTypeEnum.PAY.getCode());
        paymentDaysAccountLogDO.setChangedAmount(BigDecimal.ZERO.subtract(thisTimeUseAmount));
        paymentDaysAccountLogDO.setCreateTime(new Date());
        paymentDaysAccountLogDO.setCreateUser(request.getOpUserId());
        return paymentDaysAccountLogService.save(paymentDaysAccountLogDO);
    }

    private void reduceCompanyAmount(BigDecimal thisTimeUseAmount) {
        // 集团账期信息
        PaymentDaysCompanyDO companyAccount = paymentDaysCompanyService.get();
        // 集团可用额度
        BigDecimal companyAvailableAmount = companyAccount.getTotalAmount().subtract(companyAccount.getUsedAmount()).add(companyAccount.getRepaymentAmount());
        if (companyAvailableAmount.compareTo(thisTimeUseAmount) < 0) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_GROUP_BALANCE_NOT_ENOUGH);
        }

        // 1、使用集团额度
        paymentDaysCompanyService.use(thisTimeUseAmount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refund(RefundPaymentDaysAmountRequest request) {
        PaymentDaysAccountDTO paymentDaysAccountDTO = Optional.ofNullable(this.getPaymentAccountDetailByOrderId(request.getOrderId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_NOT_EXISTS));
        log.info("退还账期额度接口请求入参：{}，当前账期账户信息：{}",JSONObject.toJSONString(request) , JSONObject.toJSONString(paymentDaysAccountDTO));

        //默认区分是否为B2B的账期（根据开始时间和结束时间区分，B2B的账期开始时间和结束时间均为空）
        if ( Objects.isNull(request.getPlatformEnum()) ) {
            if(paymentDaysAccountDTO.getStartTime().compareTo(paymentDaysAccountDTO.getEndTime()) == 0) {
                request.setPlatformEnum(PlatformEnum.B2B);
            }
        }

        //接口幂等性校验：防止订单那边重复调用退款接口（使用金额 - 已经退款的金额 = 待退款金额 >= 当前需要退款的金额）
        PaymentDaysOrderDO paymentDaysOrderDo = paymentDaysOrderService.getPaymentDaysOrderDo(request.getOrderId());
        if(paymentDaysOrderDo.getUsedAmount().subtract(paymentDaysOrderDo.getReturnAmount()).compareTo(request.getRefundAmount()) < 0){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_RETURN_ERROR);
        }

        // 类型：1-以岭 2-非以岭
        if(paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode()) ||
                paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode()) || PlatformEnum.B2B == request.getPlatformEnum()){
            // 退还账期额度
            this.baseMapper.refund(paymentDaysAccountDTO.getId(), request.getRefundAmount());
            if(paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode())){
                // 退还集团账期额度，只有以岭的才需要退，非以岭和工业都不需要
                paymentDaysCompanyService.refund(request.getRefundAmount());
            }
        }else{
            // 退还账期额度
            this.baseMapper.feiYilingRefund(paymentDaysAccountDTO.getId(), request.getRefundAmount());
        }

        // 更新账期订单退款金额
        updatePaymentOrderAmount(request.getOrderId(),request.getRefundAmount(),request.getOpUserId(),1, paymentDaysAccountDTO, request.getReturnNo());

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTicketDiscountAmount(UpdateInvoiceTicketDiscountRequest request) {
        PaymentDaysAccountDTO paymentDaysAccountDTO = Optional.ofNullable(this.getPaymentAccountDetailByOrderId(request.getOrderId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_NOT_EXISTS));

        //接口幂等性校验：防止订单被重复调用更新票折金额
        PaymentDaysOrderDO paymentDaysOrderDo = paymentDaysOrderService.getPaymentDaysOrderDo(request.getOrderId());
        log.info("调用开票后扣除票折金额接口入参：{}，当前账期订单数据：{}",JSONObject.toJSONString(request),JSONObject.toJSONString(paymentDaysOrderDo));
        //开票时：减去票折的金额，发票作废时加上票折的金额
        if(paymentDaysOrderDo.getUsedAmount().compareTo(paymentDaysOrderDo.getReturnAmount()) == 0){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_ALL_RETURN);
        }
        if(request.getInvoiceType() == 1){
            if(paymentDaysOrderDo.getUsedAmount().compareTo(request.getTicketDiscountAmount()) < 0){
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_TICKET_MORE);
            }
        }

        if(paymentDaysOrderDo.getUsedAmount().subtract(paymentDaysOrderDo.getReturnAmount()).compareTo(request.getTicketDiscountAmount()) < 0){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_RETURN_ERROR);
        }

        BigDecimal ticketDiscountAmount = request.getTicketDiscountAmount();
        if(request.getInvoiceType() == 2){
            ticketDiscountAmount = request.getTicketDiscountAmount().negate();
        }

        if(paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode()) ||
                paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode()) || PlatformEnum.B2B == request.getPlatformEnum()){
            this.baseMapper.refund(paymentDaysAccountDTO.getId(), ticketDiscountAmount);
        }else{
            this.baseMapper.feiYilingRefund(paymentDaysAccountDTO.getId(), ticketDiscountAmount);
        }

        if(paymentDaysAccountDTO.getType().equals(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode())){
            // 更新集团账期额度
            paymentDaysCompanyService.refund(ticketDiscountAmount);
        }
        // 更新账期订单票折金额
        updatePaymentOrderAmount(request.getOrderId(),ticketDiscountAmount,request.getOpUserId(),2, paymentDaysAccountDTO, null);

        return true;
    }

    private void updatePaymentOrderAmount(Long orderId ,BigDecimal updateAmount, Long opUserId, Integer type , PaymentDaysAccountDTO paymentDaysAccountDTO, String returnNo) {

        if (type == 1) {
            //更新账期订单退款金额
            paymentDaysOrderService.updateReturnAmountByOrderId(orderId, updateAmount, opUserId);
        } else {
            paymentDaysOrderService.updateOrderTicketDiscountAmount(orderId, updateAmount, opUserId);
        }

        //根据退货单id查询退货单
        PaymentDaysOrderDO paymentDaysOrderDO = Optional.ofNullable(paymentDaysOrderService.getOne(
                new LambdaQueryWrapper<PaymentDaysOrderDO>().eq(PaymentDaysOrderDO::getOrderId,orderId)))
                .orElseThrow(()->new BusinessException(UserErrorCode.PAYMENT_DAYS_ORDER_NOT_EXISTS));

        // 保存账期额度使用日志
        PaymentDaysAccountLogDO paymentDaysAccountLogDO = new PaymentDaysAccountLogDO();
        paymentDaysAccountLogDO.setAccountId(paymentDaysAccountDTO.getId());
        paymentDaysAccountLogDO.setBusinessNo(paymentDaysOrderDO.getOrderNo());
        paymentDaysAccountLogDO.setBusinessType(type == 1 ? PaymentDaysLogTypeEnum.REFUND.getCode() : PaymentDaysLogTypeEnum.TICKET_DISCOUNT_AMOUNT.getCode());
        paymentDaysAccountLogDO.setChangedAmount(updateAmount);
        paymentDaysAccountLogDO.setCreateTime(new Date());
        paymentDaysAccountLogDO.setCreateUser(opUserId);
        if(null != returnNo && 1 == type) {
            paymentDaysAccountLogDO.setRemark("退货单" + returnNo + "退还账期");
        }
        if(type == 2){
            paymentDaysAccountLogDO.setRemark(updateAmount.compareTo(BigDecimal.ZERO) < 0 ? "发票作废":"开票");
        }

        paymentDaysAccountLogService.save(paymentDaysAccountLogDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean applyQuota(ApplyPaymentDaysAccountRequest request) {
        //校验是否存在待审核的临时额度申请记录
        Optional.ofNullable(request.getTemporaryAmount()).orElseThrow(()->new BusinessException(UserErrorCode.PAYMENT_DAYS_TEMPORARY_NOT_NULL));
        QueryWrapper<PaymentDaysTemporaryRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentDaysTemporaryRecordDO::getAccountId,request.getAccountId());
        wrapper.lambda().eq(PaymentDaysTemporaryRecordDO::getAuditStatus,PaymentTemporaryAuditStatusEnum.UNAUDIT.getCode());
        List<PaymentDaysTemporaryRecordDO> ptrList = paymentDaysTemporaryRecordService.list(wrapper);
        if(CollectionUtils.isNotEmpty(ptrList)){
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_SHORT_BALANCE_EXIST);
        }

        //校验申请的临时额度是否超过集团的可用额度
        PaymentDaysCompanyDO daysCompanyDO = paymentDaysCompanyService.get();
        BigDecimal availableAmount = daysCompanyDO.getTotalAmount().subtract(daysCompanyDO.getUsedAmount()).add(daysCompanyDO.getRepaymentAmount());
        if (availableAmount.compareTo(request.getTemporaryAmount()) < 0) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_COMPANY_MORE_THAN);
        }

        PaymentDaysAccountDO paymentDaysAccountDO = this.getById(request.getAccountId());
        PaymentDaysTemporaryRecordDO paymentDaysTemporaryRecordDO = new PaymentDaysTemporaryRecordDO();
        paymentDaysTemporaryRecordDO.setAccountId(request.getAccountId());
        paymentDaysTemporaryRecordDO.setEid(paymentDaysAccountDO.getEid());
        paymentDaysTemporaryRecordDO.setEname(paymentDaysAccountDO.getEname());
        paymentDaysTemporaryRecordDO.setCustomerEid(paymentDaysAccountDO.getCustomerEid());
        paymentDaysTemporaryRecordDO.setCustomerName(paymentDaysAccountDO.getCustomerName());
        paymentDaysTemporaryRecordDO.setAuditStatus(PaymentTemporaryAuditStatusEnum.UNAUDIT.getCode());
        paymentDaysTemporaryRecordDO.setCreateUser(request.getOpUserId());
        paymentDaysTemporaryRecordDO.setTemporatyAmount(request.getTemporaryAmount());
        return paymentDaysTemporaryRecordService.save(paymentDaysTemporaryRecordDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkQuota(ApplyPaymentDaysAccountRequest request) {
            QueryWrapper<PaymentDaysTemporaryRecordDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(PaymentDaysTemporaryRecordDO::getAccountId,request.getAccountId());
            wrapper.lambda().in(PaymentDaysTemporaryRecordDO::getAuditStatus,PaymentTemporaryAuditStatusEnum.UNAUDIT.getCode());
            PaymentDaysTemporaryRecordDO paymentDaysTemporaryRecordDO = Optional.ofNullable(paymentDaysTemporaryRecordService.getOne(wrapper))
                    .orElseThrow(()->new BusinessException(UserErrorCode.PAYMENT_DAYS_TEMPORARY_STATUS_ERROR));

            //审核通过审核
            if(request.getStatus() == PaymentTemporaryAuditStatusEnum.AUDIT_PASS.getCode()){

                PaymentDaysTemporaryRecordDO temporaryRecordDO = new PaymentDaysTemporaryRecordDO();
                temporaryRecordDO.setId(paymentDaysTemporaryRecordDO.getId());
                temporaryRecordDO.setOpUserId(request.getOpUserId());
                temporaryRecordDO.setAuditStatus(PaymentTemporaryAuditStatusEnum.AUDIT_PASS.getCode());
                boolean res = paymentDaysTemporaryRecordService.updateById(temporaryRecordDO);
                if(!res){
                    throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CHECK_FALSE);
                }

                PaymentDaysAccountDO paymentDaysAccountDO = this.getById(paymentDaysTemporaryRecordDO.getAccountId());
                paymentDaysAccountDO.setTemporaryAmount(paymentDaysTemporaryRecordDO.getTemporatyAmount());
                paymentDaysAccountDO.setAvailableAmount(paymentDaysTemporaryRecordDO.getTemporatyAmount());
                paymentDaysAccountMapper.yilingUpdateTemporary(paymentDaysAccountDO);

                //保存账期额度日志
                PaymentDaysAccountLogDO paymentDaysAccountLogDO = new PaymentDaysAccountLogDO();
                paymentDaysAccountLogDO.setAccountId(request.getAccountId());
                paymentDaysAccountLogDO.setBusinessType(PaymentDaysLogTypeEnum.TEMP_AMOUNT.getCode());
                paymentDaysAccountLogDO.setChangedAmount(paymentDaysTemporaryRecordDO.getTemporatyAmount());
                paymentDaysAccountLogDO.setCreateTime(new Date());
                paymentDaysAccountLogDO.setCreateUser(request.getOpUserId());
                paymentDaysAccountLogService.save(paymentDaysAccountLogDO);

            }else{
                PaymentDaysTemporaryRecordDO temporaryRecordDO = new PaymentDaysTemporaryRecordDO();
                temporaryRecordDO.setId(paymentDaysTemporaryRecordDO.getId());
                temporaryRecordDO.setOpUserId(request.getOpUserId());
                temporaryRecordDO.setAuditStatus(PaymentTemporaryAuditStatusEnum.AUDIT_REJECT.getCode());
                boolean res = paymentDaysTemporaryRecordService.updateById(temporaryRecordDO);
                if(!res){
                    throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_CHECK_FALSE);
                }
            }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateCompanyDetail(PaymentDaysCompanyRequest request) {
        PaymentDaysCompanyDO paymentDaysCompanyDO = paymentDaysCompanyService.get();
        paymentDaysCompanyDO.setTotalAmount(request.getTotalAmount());
        return paymentDaysCompanyService.saveOrUpdate(paymentDaysCompanyDO);
    }

    @Override
    public PaymentDaysAccountDTO getPaymentAccountDetailByOrderId(Long orderId) {
        QueryWrapper<PaymentDaysOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentDaysOrderDO::getOrderId,orderId);
        PaymentDaysOrderDO paymentDaysOrderDO = paymentDaysOrderService.getOne(wrapper);

        if(Objects.isNull(paymentDaysOrderDO)){
            return null;
        }
        return PojoUtils.map(this.getById(paymentDaysOrderDO.getAccountId()),PaymentDaysAccountDTO.class);
    }

    @Override
    public PaymentDaysAmountBO countTotalAmount(Long accountId) {
        QueryWrapper<PaymentDaysOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentDaysOrderDO::getAccountId,accountId);
        List<PaymentDaysOrderDO> list = paymentDaysOrderService.list(wrapper);

        PaymentDaysAmountBO paymentDaysAmountBO = new PaymentDaysAmountBO();
        paymentDaysAmountBO.setTotalReturnAmount(list.stream().map(PaymentDaysOrderDO::getReturnAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        //已使用额度=支付总金额（账期订单表的used_amount就是支付总金额）-退款金额=0
        paymentDaysAmountBO.setTotalPayAmount(list.stream().map(PaymentDaysOrderDO::getUsedAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        paymentDaysAmountBO.setTotalUsedAmount(paymentDaysAmountBO.getTotalPayAmount().subtract(paymentDaysAmountBO.getTotalReturnAmount()));

        return paymentDaysAmountBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyAuditRefund(RefundPaymentDaysAmountRequest request) {

        QueryWrapper<PaymentDaysOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PaymentDaysOrderDO::getOrderId,request.getOrderId());

        PaymentDaysOrderDO paymentDaysOrderDO = paymentDaysOrderService.getOne(wrapper);

        if (paymentDaysOrderDO == null) {

            log.info("modifyAuditRefund:{}",request);

            return false;
        }

        // 校验退款金额是否超过是使用金额
        if (request.getRefundAmount().compareTo(paymentDaysOrderDO.getUsedAmount()) == 1) {

            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_RETURN_ERROR);
        }

        // 表示反审后的退款金额和反审前的退款金额是一致的，无需处理改逻辑
     /*   if (paymentDaysOrderDO.getReturnAmount().compareTo(request.getRefundAmount()) == 0) {

            log.info("modifyAuditRefund:{}",request);
            return true;
        }*/

        // 修改前反审金额
        BigDecimal beforeReturnAmount = paymentDaysOrderDO.getReturnAmount();
        // 本次操作退款金额
        BigDecimal orderRefundAmount = request.getRefundAmount();
        // 删除操作前退款数据
        paymentDaysOrderService.deleteByIdWithFill(paymentDaysOrderDO);

        PaymentDaysOrderDO newPaymentDaysOrderDo = PojoUtils.map(paymentDaysOrderDO,PaymentDaysOrderDO.class);
        newPaymentDaysOrderDo.setId(null);
        newPaymentDaysOrderDo.setReturnAmount(NumberUtil.sub(beforeReturnAmount,request.getRefundAmount()));
        // 更新账期订单退款金额
        paymentDaysOrderService.saveOrUpdate(newPaymentDaysOrderDo);

        // 获取负数金额
        request.setRefundAmount(NumberUtil.mul(request.getRefundAmount(),new BigDecimal("-1")));

        PaymentDaysAccountDTO paymentDaysAccountDTO = this.getPaymentAccountDetailByOrderId(request.getOrderId());

        // 退还账期额度
        if(paymentDaysAccountDTO.getType() == 1 || paymentDaysAccountDTO.getType() == 3 || PlatformEnum.B2B == request.getPlatformEnum()){
            this.baseMapper.refund(paymentDaysAccountDTO.getId(), request.getRefundAmount());
        }else{
            this.baseMapper.feiYilingRefund(paymentDaysAccountDTO.getId(), request.getRefundAmount());
        }

        if (paymentDaysAccountDTO.getType() == 1){
            // 退还集团账期额度
            paymentDaysCompanyService.refund(request.getRefundAmount());
        }

        // 保存账期额度使用日志
        PaymentDaysAccountLogDO paymentDaysAccountLogDO = new PaymentDaysAccountLogDO();
        paymentDaysAccountLogDO.setAccountId(paymentDaysAccountDTO.getId());
        paymentDaysAccountLogDO.setBusinessNo(newPaymentDaysOrderDo.getOrderNo());
        paymentDaysAccountLogDO.setBusinessType(PaymentDaysLogTypeEnum.CONVERT_CHANGE.getCode());
        paymentDaysAccountLogDO.setChangedAmount(request.getRefundAmount());
        paymentDaysAccountLogDO.setCreateTime(new Date());
        paymentDaysAccountLogDO.setCreateUser(request.getOpUserId());
        paymentDaysAccountLogService.save(paymentDaysAccountLogDO);

        return true;

    }

    @Override
    public MainPartEnterpriseDTO listMainPart(Long currentEid, Long currentUserId) {
        MainPartEnterpriseDTO mainPartEnterpriseDto = new MainPartEnterpriseDTO();
        EnterpriseDO enterpriseDo = Optional.ofNullable(enterpriseService.getById(currentEid)).orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

        List<EnterpriseDTO> list = ListUtil.toList();
        //工业的授信主体为它自己；以岭的授信主体为它的几个子企业；非以岭的授信主体也是当前登录企业
        if(Constants.YILING_EID.equals(currentEid)){
            LambdaQueryWrapper<EnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EnterpriseDO::getChannelId, EnterpriseChannelEnum.INDUSTRY.getCode());
            queryWrapper.ne(EnterpriseDO::getParentId, 0);
            list.addAll(PojoUtils.map(enterpriseService.list(queryWrapper),EnterpriseDTO.class));
            mainPartEnterpriseDto.setShowMainPart(1);

        }else{
            if(enterpriseDo.getChannelId().compareTo(EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode()) == 0){
                list.add(PojoUtils.map(enterpriseDo,EnterpriseDTO.class));
                mainPartEnterpriseDto.setShowMainPart(1);
            }else{
                mainPartEnterpriseDto.setShowMainPart(0);
            }
        }

        mainPartEnterpriseDto.setEnterpriseList(list);
        return mainPartEnterpriseDto;
    }

}
