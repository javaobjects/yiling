package com.yiling.user.member.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.member.dao.MemberReturnMapper;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.request.AddMemberReturnRequest;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.dto.request.RefundOrderRequest;
import com.yiling.user.member.dto.request.UpdateMemberAuthReturnRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnStatusRequest;
import com.yiling.user.member.dto.request.UpdateReturnEnterpriseMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.EnterpriseMemberLogDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberReturnDO;
import com.yiling.user.member.enums.EnterpriseMemberLogOpTypeEnum;
import com.yiling.user.member.enums.MemberReturnAuthStatusEnum;
import com.yiling.user.member.enums.MemberReturnStatusEnum;
import com.yiling.user.member.service.EnterpriseMemberLogService;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberReturnService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B-会员退款表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Slf4j
@Service
public class MemberReturnServiceImpl extends BaseServiceImpl<MemberReturnMapper, MemberReturnDO> implements MemberReturnService {

    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    EnterpriseMemberLogService enterpriseMemberLogService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    MemberReturnServiceImpl _this;


    @Override
    public boolean addMemberReturn(AddMemberReturnRequest request) {
        log.info("新增会员退款，退款信息：{}", JSONObject.toJSONString(request));
        MemberReturnDO memberReturnDO = PojoUtils.map(request, MemberReturnDO.class);
        memberReturnDO.setAuthStatus(MemberReturnAuthStatusEnum.WAIT.getCode());
        memberReturnDO.setReturnStatus(MemberReturnStatusEnum.WAITING.getCode());
        return this.save(memberReturnDO);
    }

    @Override
    public Page<MemberReturnDTO> queryMemberReturnPage(QueryMemberReturnPageRequest request) {
        QueryWrapper<MemberReturnDO> queryWrapper = WrapperUtils.getWrapper(request);
        Page<MemberReturnDO> memberReturnDOPage = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(memberReturnDOPage, MemberReturnDTO.class);
    }

    @Override
    public List<MemberReturnDTO> getReturnListByOrderNo(String orderNo) {
        LambdaQueryWrapper<MemberReturnDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberReturnDO::getOrderNo, orderNo);

        return PojoUtils.map(this.list(wrapper), MemberReturnDTO.class);
    }

    @Override
    public boolean updateAuthStatus(UpdateMemberAuthReturnRequest request) {
        List<MqMessageBO> mqMessageBOList = _this.updateAuthStatusTx(request);

        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }

        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO> updateAuthStatusTx(UpdateMemberAuthReturnRequest request) {
        MemberReturnDO memberReturnDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_RETURN_RECORD_NOT_EXIST));
        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        // 同意
        if (MemberReturnAuthStatusEnum.PASS == MemberReturnAuthStatusEnum.getByCode(request.getAuthStatus())) {
            MemberReturnDO returnDO = new MemberReturnDO();
            returnDO.setId(memberReturnDO.getId());
            returnDO.setAuthStatus(MemberReturnAuthStatusEnum.PASS.getCode());
            returnDO.setReturnStatus(MemberReturnStatusEnum.RETURNING.getCode());
            returnDO.setOpUserId(request.getOpUserId());
            returnDO.setAuthTime(new Date());
            returnDO.setAuthUser(request.getOpUserId());
            this.updateById(returnDO);

            MemberBuyRecordDTO memberBuyRecordDTO = memberBuyRecordService.getBuyRecordByOrderNo(memberReturnDO.getOrderNo());

            // 发送MQ通知到订单服务，进行实际退款操作
            RefundOrderRequest refundOrderRequest = new RefundOrderRequest();
            refundOrderRequest.setOrderId(memberReturnDO.getOrderId());
            refundOrderRequest.setOrderNo(memberReturnDO.getOrderNo());
            refundOrderRequest.setReturnId(memberReturnDO.getId());
            refundOrderRequest.setRefundType(4);
            refundOrderRequest.setRefundAmount(memberReturnDO.getReturnAmount());
            refundOrderRequest.setReason(memberReturnDO.getReturnReason());
            refundOrderRequest.setRefundSource(2);
            refundOrderRequest.setSellerEid(Constants.YILING_EID);
            refundOrderRequest.setBuyerEid(memberReturnDO.getEid());
            refundOrderRequest.setPaymentAmount(memberReturnDO.getPayAmount());
            refundOrderRequest.setTotalAmount(memberReturnDO.getPayAmount());
            refundOrderRequest.setOpUserId(request.getOpUserId());

            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_ORDER_REFUND, "", JSONObject.toJSONString(refundOrderRequest));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageBOList.add(mqMessageBO);
            log.info("会员退款审核同意 发送MQ通知到订单服务进行实际退款操作 发送数据信息：{}", JSONObject.toJSONString(refundOrderRequest));

            // 此时有推广人就推到任务佣金模块、有推广方就推到返利模块
            if (Objects.nonNull(memberBuyRecordDTO.getPromoterId()) && memberBuyRecordDTO.getPromoterId() != 0) {
                // 发送MQ通知返利模块
                MqMessageBO rebateMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_REFUND_PUSH_REBATE, "", JSONObject.toJSONString(refundOrderRequest));
                rebateMessageBO = mqMessageSendApi.prepare(rebateMessageBO);
                mqMessageBOList.add(rebateMessageBO);
                log.info("会员退款审核同意 发送MQ通知返利模块 发送数据信息：{}", JSONObject.toJSONString(refundOrderRequest));
            }
            if (Objects.nonNull(memberBuyRecordDTO.getPromoterUserId()) && memberBuyRecordDTO.getPromoterUserId() != 0) {
                // 发送MQ通知佣金模块（部分退款不推送，全部退款才推送）
                if (memberReturnDO.getPayAmount().compareTo(memberReturnDO.getReturnAmount()) == 0) {
                    MqMessageBO taskMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_REFUND_PUSH_TASK, "", memberReturnDO.getOrderNo());
                    taskMessageBO = mqMessageSendApi.prepare(taskMessageBO);
                    mqMessageBOList.add(taskMessageBO);
                    log.info("会员退款审核同意 发送MQ通知佣金模块 发送数据信息：{}", memberReturnDO.getOrderNo());
                }

            }

        } else {
            // 驳回
            MemberReturnDO returnDO = new MemberReturnDO();
            returnDO.setId(memberReturnDO.getId());
            returnDO.setAuthStatus(MemberReturnAuthStatusEnum.REJECT.getCode());
            returnDO.setReturnStatus(0);
            returnDO.setOpUserId(request.getOpUserId());
            returnDO.setAuthTime(new Date());
            returnDO.setAuthUser(request.getOpUserId());
            this.updateById(returnDO);

            // 财务驳回后，更新提交的退款金额字段为0
            MemberBuyRecordDTO memberBuyRecordDTO = memberBuyRecordService.getBuyRecordByOrderNo(memberReturnDO.getOrderNo());
            MemberBuyRecordDO buyRecordDO = new MemberBuyRecordDO();
            buyRecordDO.setId(memberBuyRecordDTO.getId());
            buyRecordDO.setSubmitReturnAmount(BigDecimal.ZERO);
            this.memberBuyRecordService.updateById(buyRecordDO);
        }

        return mqMessageBOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReturnStatus(UpdateMemberReturnStatusRequest request) {
        if (Objects.isNull(request.getMemberReturnId()) || (MemberReturnStatusEnum.getByCode(request.getReturnStatus()) != MemberReturnStatusEnum.SUCCESS
                && MemberReturnStatusEnum.getByCode(request.getReturnStatus()) != MemberReturnStatusEnum.FAIL ) ) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        LambdaQueryWrapper<MemberReturnDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberReturnDO::getId, request.getMemberReturnId());
        wrapper.eq(MemberReturnDO::getAuthStatus, MemberReturnAuthStatusEnum.PASS.getCode());
        wrapper.in(MemberReturnDO::getReturnStatus, ListUtil.toList(MemberReturnStatusEnum.RETURNING.getCode(), MemberReturnStatusEnum.FAIL.getCode()));
        wrapper.last("limit 1");
        MemberReturnDO memberReturnDO = this.getOne(wrapper);
        if (Objects.isNull(memberReturnDO)) {
            log.error("会员退款回调更新退款状态，数据异常：{}", JSONObject.toJSONString(request));
            return true;
        }

        // 如果退款成功，更新会员购买记录的实际退款金额
        if (MemberReturnStatusEnum.getByCode(request.getReturnStatus()) == MemberReturnStatusEnum.SUCCESS) {
            // 获取更新前有效的会员购买记录数据
            List<MemberBuyRecordDTO> validMemberRecordList = memberBuyRecordService.getValidMemberRecordList(memberReturnDO.getEid(), memberReturnDO.getMemberId());

            MemberBuyRecordDTO memberBuyRecordDTO = memberBuyRecordService.getBuyRecordByOrderNo(memberReturnDO.getOrderNo());
            MemberBuyRecordDO buyRecordDO = new MemberBuyRecordDO();
            buyRecordDO.setId(memberBuyRecordDTO.getId());
            buyRecordDO.setReturnAmount(memberReturnDO.getReturnAmount());
            buyRecordDO.setOpUserId(memberBuyRecordDTO.getUpdateUser());
            this.memberBuyRecordService.updateById(buyRecordDO);

            EnterpriseMemberDO originalEnterpriseMemberDO = enterpriseMemberService.getEnterpriseMember(memberReturnDO.getEid(), memberReturnDO.getMemberId());
            // 会员退款更新企业会员
            UpdateReturnEnterpriseMemberRequest enterpriseMemberRequest = new UpdateReturnEnterpriseMemberRequest();
            enterpriseMemberRequest.setEid(memberReturnDO.getEid());
            enterpriseMemberRequest.setMemberId(memberReturnDO.getMemberId());
            enterpriseMemberRequest.setOrderNo(memberReturnDO.getOrderNo());
            enterpriseMemberRequest.setOpUserId(memberReturnDO.getAuthUser());
            enterpriseMemberService.updateReturnEnterpriseMember(enterpriseMemberRequest);

            // 当前这条记录是有效的才进行：重新计算当前企业会员的 会员购买记录的开始时间、结束时间、开通类型
            if (memberBuyRecordDTO.getEndTime().after(new Date())) {
                memberBuyRecordService.updateBuyRecordTime(memberBuyRecordDTO.getId(), validMemberRecordList, originalEnterpriseMemberDO);
            }

            // 增加企业会员操作日志记录
            this.insertEnterpriseMemberLog(memberBuyRecordDTO, originalEnterpriseMemberDO);
        }

        // 更新退款状态为退款成功或退款失败
        MemberReturnDO returnDO = new MemberReturnDO();
        returnDO.setId(memberReturnDO.getId());
        returnDO.setReturnStatus(request.getReturnStatus());
        returnDO.setOpUserId(memberReturnDO.getUpdateUser());
        log.info("会员退款回调成功，退款信息：{}", JSONObject.toJSONString(request));

        return this.updateById(returnDO);
    }

    /**
     * 插入企业会员日志记录
     *
     * @param memberBuyRecordDTO
     */
    private void insertEnterpriseMemberLog(MemberBuyRecordDTO memberBuyRecordDTO, EnterpriseMemberDO originalEnterpriseMemberDO) {
        EnterpriseMemberLogDO enterpriseMemberLogDO = new EnterpriseMemberLogDO();
        enterpriseMemberLogDO.setEid(memberBuyRecordDTO.getEid());
        enterpriseMemberLogDO.setEname(memberBuyRecordDTO.getEname());
        enterpriseMemberLogDO.setMemberId(memberBuyRecordDTO.getMemberId());
        enterpriseMemberLogDO.setMemberName(memberBuyRecordDTO.getMemberName());
        // 操作类型：1-开通 2-续费 3-退费 4-修改推广方
        enterpriseMemberLogDO.setOpType(EnterpriseMemberLogOpTypeEnum.RETURN.getCode());
        enterpriseMemberLogDO.setOrderNo(memberBuyRecordDTO.getOrderNo());
        enterpriseMemberLogDO.setOrderBuyRule(memberBuyRecordDTO.getBuyRule());
        enterpriseMemberLogDO.setPromoterId(memberBuyRecordDTO.getPromoterId());
        enterpriseMemberLogDO.setValidDays(memberBuyRecordDTO.getValidDays());
        enterpriseMemberLogDO.setBeforeStartTime(originalEnterpriseMemberDO.getStartTime());
        enterpriseMemberLogDO.setBeforeEndTime(originalEnterpriseMemberDO.getEndTime());
        enterpriseMemberLogDO.setAfterStartTime(originalEnterpriseMemberDO.getStartTime());
        // 到期时间 减去 退款订单的时长 等于 退款后企业会员的时长
        enterpriseMemberLogDO.setAfterEndTime(DateUtil.offsetDay(originalEnterpriseMemberDO.getEndTime(), -memberBuyRecordDTO.getValidDays()));

        enterpriseMemberLogService.save(enterpriseMemberLogDO);
    }

}
