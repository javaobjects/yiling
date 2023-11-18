package com.yiling.settlement.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.bo.MemberOrderSyncBO;
import com.yiling.settlement.report.dao.MemberSyncMapper;
import com.yiling.settlement.report.entity.MemberSyncDO;
import com.yiling.settlement.report.enums.MemberRefundStatusEnum;
import com.yiling.settlement.report.enums.ReportErrorCode;
import com.yiling.settlement.report.enums.ReportSettStatusEnum;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.service.MemberSyncService;
import com.yiling.user.common.enums.MemberOrderStatusEnum;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.enums.MemberReturnAuthStatusEnum;
import com.yiling.user.member.enums.MemberSourceEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 报表的会员订单表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Slf4j
@Service
public class MemberSyncServiceImpl extends BaseServiceImpl<MemberSyncMapper, MemberSyncDO> implements MemberSyncService {

    @DubboReference
    MemberOrderApi memberOrderApi;
    @DubboReference
    MemberReturnApi memberReturnApi;

    @Override
    public MemberOrderSyncBO queryMemberOrderSync(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            return null;
        }
        LambdaQueryWrapper<MemberSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberSyncDO::getOrderNo, orderNo);
        return PojoUtils.map(getOne(wrapper), MemberOrderSyncBO.class);
    }

    @Override
    public List<MemberOrderSyncBO> queryMemberOrderSyncBatch(List<String> orderNo) {
        if (CollUtil.isEmpty(orderNo)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<MemberSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MemberSyncDO::getOrderNo, orderNo);
        List<MemberSyncDO> list = list(wrapper);

        return PojoUtils.map(list, MemberOrderSyncBO.class);
    }

    @Override
    public List<MemberOrderSyncBO> queryOrderByEid(Long eid, Date startTime, Date endTime) {
        LambdaQueryWrapper<MemberSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberSyncDO::getPromoterId, eid);
        wrapper.in(MemberSyncDO::getSource, ListUtil.toList(MemberSourceEnum.B2B_ENTERPRISE.getCode(), MemberSourceEnum.ASSISTANT.getCode()));
        wrapper.eq(MemberSyncDO::getStatus, MemberOrderStatusEnum.PAY_SUCCESS.getCode());
        wrapper.eq(MemberSyncDO::getReportSettStatus, ReportSettStatusEnum.UN_CALCULATE.getCode());
        wrapper.ge(MemberSyncDO::getOrderCreateTime, startTime);
        wrapper.le(MemberSyncDO::getOrderCreateTime, endTime);
        return PojoUtils.map(list(wrapper), MemberOrderSyncBO.class);
    }

    @Override
    public List<MemberOrderSyncBO> queryOrderByReportId(Long reportId) {
        LambdaQueryWrapper<MemberSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberSyncDO::getReportId, reportId);
        return PojoUtils.map(list(wrapper), MemberOrderSyncBO.class);
    }

    @Override
    public Boolean createMemberOrderSync(MemberSyncDO memberSyncDO) {
        MemberOrderSyncBO dbOrder = queryMemberOrderSync(memberSyncDO.getOrderNo());
        if (ObjectUtil.isNotNull(dbOrder)) {
            return Boolean.TRUE;
        }
        memberSyncDO.setStatus(MemberOrderStatusEnum.PAY_SUCCESS.getCode());
        memberSyncDO.setOrderCreateTime(memberSyncDO.getCreateTime());
        memberSyncDO.setOpTime(new Date());
        return save(memberSyncDO);
    }

    @Override
    public Boolean createMemberOrderRefund(MemberSyncDO memberSyncDO) {
        MemberOrderSyncBO dbOrder = queryMemberOrderSync(memberSyncDO.getOrderNo());
        if (ObjectUtil.isNull(dbOrder)) {
            log.error("会员退款同步失败，原因退款通知时在会员订单同步表查无此订单，订单号={}", memberSyncDO.getOrderNo());
            return Boolean.FALSE;
        }
        //订单金额
        BigDecimal payAmount = dbOrder.getPayAmount();
        BigDecimal amount = payAmount.subtract(memberSyncDO.getRefundAmount());
        MemberSyncDO aDo = new MemberSyncDO();
        aDo.setId(dbOrder.getId());
        aDo.setRefundAmount(memberSyncDO.getRefundAmount());
        aDo.setAmount(amount);
        aDo.setRefundStatus(MemberRefundStatusEnum.REFUND.getCode());
        aDo.setRefundTime(new Date());
        aDo.setOpTime(new Date());

        return updateById(aDo);
    }

    @Override
    public Boolean updatePromoter(String orderNo, Long promoterId, Integer source) {
        MemberOrderSyncBO dbOrder = queryMemberOrderSync(orderNo);
        if (ObjectUtil.isNull(dbOrder)) {
            log.error("会员推广方同步失败，原因更新推广方通知时在会员订单同步表查无此订单，订单号={}", orderNo);
            return Boolean.FALSE;
        }
        MemberSyncDO aDo = new MemberSyncDO();
        aDo.setId(dbOrder.getId());
        aDo.setOpTime(new Date());
        aDo.setPromoterId(promoterId);
        aDo.setSource(source);
        return updateById(aDo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectMemberOrder(Long reportId, ReportStatusEnum statusEnum) {
        LambdaQueryWrapper<MemberSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MemberSyncDO::getReportId, reportId);
        List<MemberSyncDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        MemberSyncDO memberSyncDO = new MemberSyncDO();
        memberSyncDO.setReportSettStatus(ReportSettStatusEnum.UN_CALCULATE.getCode());
        memberSyncDO.setReportStatus(statusEnum.getCode());
        memberSyncDO.setReportId(0L);
        memberSyncDO.setOpUserId(0L);
        memberSyncDO.setOpTime(new Date());
        boolean isSuccess = update(memberSyncDO, wrapper);
        if (!isSuccess) {
            log.error("驳回b2b报表时，更新会员订单同步表失败，参数={}，报表id={}", memberSyncDO, reportId);
            throw new ServiceException(ReportErrorCode.REPORT_REJECT_FAIL.getMessage());
        }
    }

    @Override
    public Boolean initMemberOrderData() {
        //分页查询同步失败的订单列表
        int current = 1;
        Page<MemberOrderDTO> page;
        QueryMemberOrderPageRequest request = new QueryMemberOrderPageRequest();
        //分页查询同步失败的订单列表
        do {
            List<MemberSyncDO> data = ListUtil.toList();
            request.setCurrent(current);
            request.setSize(100);
            request.setStatus(MemberOrderStatusEnum.PAY_SUCCESS.getCode());
            //分页查询同步失败的订单列表
            page = memberOrderApi.queryMemberOrderPage(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //查询是否已经同步
            List<MemberOrderDTO> records = page.getRecords();
            List<MemberOrderSyncBO> existenceOrder = queryMemberOrderSyncBatch(records.stream().map(MemberOrderDTO::getOrderNo).collect(Collectors.toList()));
            List<String> existenceOrderNoList = existenceOrder.stream().map(MemberOrderSyncBO::getOrderNo).collect(Collectors.toList());

            records.forEach(e -> {
                //已经同步的订单进行忽略
                if (existenceOrderNoList.contains(e.getOrderNo())) {
                    return;
                }
                //查询退款
                MemberSyncDO var = PojoUtils.map(e, MemberSyncDO.class);
                QueryMemberReturnPageRequest returnPageRequest = new QueryMemberReturnPageRequest();
                returnPageRequest.setOrderNo(var.getOrderNo());
                Page<MemberReturnDTO> returnDTOPage = memberReturnApi.queryMemberReturnPage(returnPageRequest);
                var.setAmount(e.getPayAmount());

                if (CollUtil.isNotEmpty(returnDTOPage.getRecords())) {
                    MemberReturnDTO memberReturnDTO = returnDTOPage.getRecords().get(0);
                    //如果退款已审核
                    if (ObjectUtil.equal(MemberReturnAuthStatusEnum.PASS.getCode(), memberReturnDTO.getAuthStatus())) {
                        BigDecimal payAmount = var.getPayAmount();
                        BigDecimal amount = payAmount.subtract(memberReturnDTO.getReturnAmount());
                        var.setRefundAmount(memberReturnDTO.getReturnAmount());
                        var.setAmount(amount);
                        var.setRefundStatus(MemberRefundStatusEnum.REFUND.getCode());
                        var.setRefundTime(memberReturnDTO.getAuthTime());
                    }
                }

                var.setOrderCreateTime(e.getCreateTime());
                var.setId(null);
                var.setOpTime(new Date());
                var.setOpUserId(0L);
                data.add(var);
            });

            current = current + 1;
            boolean saveBatch = saveBatch(data);
            if (!saveBatch) {
                log.error("初始返利报表B2B会员订单数据 据失败，参数={}", data);
            }
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return Boolean.TRUE;
    }
}
