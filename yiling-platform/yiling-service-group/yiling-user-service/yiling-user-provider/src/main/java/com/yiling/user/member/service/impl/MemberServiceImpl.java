package com.yiling.user.member.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.user.enterprise.entity.EnterpriseDO;

import cn.hutool.core.collection.ListUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.MemberOrderStatusEnum;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.service.LockCustomerService;
import com.yiling.user.member.bo.MemberEquityBO;
import com.yiling.user.member.dao.MemberEquityRelationMapper;
import com.yiling.user.member.dao.MemberMapper;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.SaveMemberEquityRelationRequest;
import com.yiling.user.member.dto.request.UpdateMemberBuyStageRequest;
import com.yiling.user.member.dto.request.UpdateMemberRebateRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberSortRequest;
import com.yiling.user.member.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.EnterpriseMemberLogDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberEquityDO;
import com.yiling.user.member.entity.MemberEquityRelationDO;
import com.yiling.user.member.entity.MemberOrderCouponDO;
import com.yiling.user.member.entity.MemberOrderDO;
import com.yiling.user.member.enums.MemberOpenTypeEnum;
import com.yiling.user.member.enums.MemberOrderCouponUseStatusEnum;
import com.yiling.user.member.enums.MemberSourceEnum;
import com.yiling.user.member.service.EnterpriseMemberLogService;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberEquityRelationService;
import com.yiling.user.member.service.MemberEquityService;
import com.yiling.user.member.service.MemberOrderCouponService;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberService;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.service.StaffService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Service
@Slf4j
public class MemberServiceImpl extends BaseServiceImpl<MemberMapper, MemberDO> implements MemberService {

    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    MemberEquityRelationService memberEquityRelationService;
    @Autowired
    MemberEquityService memberEquityService;
    @Autowired
    MemberBuyRecordService memberBuyRecordService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    UserService userService;
    @Autowired
    StaffService staffService;
    @Autowired
    LockCustomerService lockCustomerService;
    @Autowired
    MemberOrderCouponService memberOrderCouponService;
    @Autowired
    EnterpriseMemberLogService enterpriseMemberLogService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @Lazy
    @Autowired
    MemberServiceImpl _this;

    @Override
    public Page<MemberDTO> queryListPage(QueryMemberRequest request) {
        LambdaQueryWrapper<MemberDO> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getMemberId()) && request.getMemberId() != 0) {
            queryWrapper.eq(MemberDO::getId, request.getMemberId());
        }
        if (StrUtil.isNotEmpty(request.getMemberName())) {
            queryWrapper.like(MemberDO::getName, request.getMemberName());
        }
        if (Objects.nonNull(request.getStartCreateTime())) {
            queryWrapper.ge(MemberDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            queryWrapper.le(MemberDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        queryWrapper.orderByDesc(MemberDO::getSort, MemberDO::getUpdateTime);
        return PojoUtils.map(this.page(request.getPage(), queryWrapper), MemberDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean stopGet(Long id, Long opUserId) {
        MemberDO memberDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_NOT_EXIST));

        memberDO.setStopGet(memberDO.getStopGet() == 1 ? 0 : 1);
        memberDO.setOpUserId(opUserId);

        return this.updateById(memberDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createMember(CreateMemberRequest request) {
        if (CollUtil.isEmpty(request.getMemberBuyStageList()) || CollUtil.isEmpty(request.getMemberEquityList())) {
            return false;
        }

        // 会员名称不能重复
        MemberDO member = this.getMemberByName(request.getName());
        if (Objects.nonNull(member)) {
            throw new BusinessException(UserErrorCode.MEMBER_NAME_EXIST);
        }

        // 最新产品需求：存在多个会员，创建会员
        MemberDO memberDO = PojoUtils.map(request, MemberDO.class);
        this.save(memberDO);

        //创建会员收费条件
        request.getMemberBuyStageList().forEach(createMemberBuyStageRequest -> {
            MemberBuyStageDO memberBuyStageDO = PojoUtils.map(createMemberBuyStageRequest, MemberBuyStageDO.class);
            memberBuyStageDO.setMemberId(memberDO.getId());
            memberBuyStageDO.setMemberName(memberDO.getName());
            memberBuyStageDO.setOpUserId(request.getOpUserId());
            memberBuyStageService.save(memberBuyStageDO);
        });

        //创建会员权益关系
        List<SaveMemberEquityRelationRequest> memberEquityList = request.getMemberEquityList();
        memberEquityList.forEach(memberEquityRelationRequest -> {
            memberEquityRelationRequest.setMemberId(memberDO.getId());
            memberEquityRelationRequest.setOpUserId(request.getOpUserId());
        });
        List<MemberEquityRelationDO> equityRelationDOList = PojoUtils.map(memberEquityList, MemberEquityRelationDO.class);
        memberEquityRelationService.saveBatch(equityRelationDOList);

        return true;
    }

    private MemberDO getMemberByName(String name) {
        LambdaQueryWrapper<MemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberDO::getName, name);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public MemberDetailDTO getMember(Long id) {
        //会员信息
        MemberDetailDTO memberDetailDTO = Optional.ofNullable(PojoUtils.map(this.getById(id), MemberDetailDTO.class)).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_NOT_EXIST));

        //会员购买条件
        LambdaQueryWrapper<MemberBuyStageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberBuyStageDO::getMemberId, id);
        List<MemberBuyStageDO> memberBuyStageDOList = memberBuyStageService.list(queryWrapper);
        memberDetailDTO.setMemberBuyStageList(PojoUtils.map(memberBuyStageDOList, MemberBuyStageDTO.class));

        //会员权益
        List<MemberEquityBO> memberEquityBOList = this.getMemberEquity(id);
        memberDetailDTO.setMemberEquityList(memberEquityBOList);

        return memberDetailDTO;
    }

    private List<MemberEquityBO> getMemberEquity(Long memberId) {
        LambdaQueryWrapper<MemberEquityRelationDO> relationWrapper = new LambdaQueryWrapper<>();
        relationWrapper.eq(MemberEquityRelationDO::getMemberId, memberId);
        relationWrapper.orderByDesc(MemberEquityRelationDO::getSort, MemberEquityRelationDO::getUpdateTime);
        List<MemberEquityRelationDO> relationDOList = memberEquityRelationService.list(relationWrapper);
        List<Long> equityIdList = relationDOList.stream().map(MemberEquityRelationDO::getEquityId).collect(Collectors.toList());
        if (CollUtil.isEmpty(equityIdList)) {
            return ListUtil.toList();
        }
        Map<Long, Integer> sortMap = relationDOList.stream().collect(Collectors.toMap(MemberEquityRelationDO::getEquityId, MemberEquityRelationDO::getSort, (k1, k2) -> k2));

        // 查询出会员权益，保证排序
        List<MemberEquityDO> memberEquityDOList = memberEquityService.listByIds(equityIdList).stream().filter(memberEquityDO -> memberEquityDO.getStatus() == 1).collect(Collectors.toList());
        Map<Long, MemberEquityDO> map = memberEquityDOList.stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));

        List<MemberEquityBO> memberEquityList = ListUtil.toList();
        equityIdList.forEach(equityId -> {
            if (Objects.nonNull(map.get(equityId))) {
                MemberEquityBO memberEquityBO = PojoUtils.map(map.get(equityId), MemberEquityBO.class);
                memberEquityBO.setSort(sortMap.getOrDefault(equityId, 0));
                memberEquityList.add(memberEquityBO);
            }
        });
        return memberEquityList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMember(UpdateMemberRequest request) {
        if (CollUtil.isEmpty(request.getMemberBuyStageList()) || CollUtil.isEmpty(request.getMemberEquityList())) {
            return false;
        }

        //更新会员
        MemberDO memberDO = PojoUtils.map(request, MemberDO.class);
        this.updateById(memberDO);

        //更新会员获得条件
        updateMemberBuyStage(request, memberDO);

        //更新会员权益
        updateMemberEquity(request, memberDO);

        return true;
    }

    @Override
    public CurrentMemberDTO getCurrentMember(Long currentEid, Long memberId) {
        CurrentMemberDTO memberDto = PojoUtils.map(this.getById(memberId), CurrentMemberDTO.class);
        if (Objects.isNull(memberDto)) {
            throw new BusinessException(UserErrorCode.MEMBER_NOT_EXIST);
        }

        //查询是否为会员用户
        EnterpriseMemberDO enterpriseMemberDO = enterpriseMemberService.getEnterpriseMember(currentEid, memberId);
        if (Objects.nonNull(enterpriseMemberDO)) {
            if (enterpriseMemberDO.getEndTime().after(new Date())) {
                memberDto.setCurrentMember(1);
            } else {
                memberDto.setCurrentMember(0);
            }
            memberDto.setStartTime(enterpriseMemberDO.getStartTime());
            memberDto.setEndTime(enterpriseMemberDO.getEndTime());

            // 获取当前正在生效的推广方设置
            List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordService.getCurrentValidMemberRecord(currentEid);
            List<MemberBuyRecordDTO> currentBuyRecordList = memberBuyRecordDTOList.stream().filter(memberBuyRecordDTO -> memberBuyRecordDTO.getMemberId().compareTo(memberId) == 0).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(currentBuyRecordList)) {
                MemberBuyRecordDTO memberBuyRecordDTO = currentBuyRecordList.get(0);
                memberDto.setPromoterId(memberBuyRecordDTO.getPromoterId());
                memberDto.setPromoterUserId(memberBuyRecordDTO.getPromoterUserId());
            }

        } else {
            memberDto.setCurrentMember(0);
            String date = "1970-01-01 00:00:00";
            memberDto.setStartTime(DateUtil.parseDate(date));
            memberDto.setEndTime(DateUtil.parseDate(date));
        }

        //获取会员权益信息
        List<MemberEquityBO> memberEquityList = this.getMemberEquity(memberDto.getId());
        memberDto.setMemberEquityList(memberEquityList);

        //获取会员购买条件
        LambdaQueryWrapper<MemberBuyStageDO> wrapper = new LambdaQueryWrapper<MemberBuyStageDO>().eq(MemberBuyStageDO::getMemberId, memberDto.getId());
        List<MemberBuyStageDO> memberBuyStageDoList = memberBuyStageService.list(wrapper);
        memberDto.setMemberBuyStageList(PojoUtils.map(memberBuyStageDoList, MemberBuyStageDTO.class));

        return memberDto;
    }

    @Override
    public Boolean openMember(OpenMemberRequest request) {

        List<MqMessageBO> mqMessageBOList = _this.saveOpenMember(request);

        //首次开通则发送MQ通知优惠券那边处理
        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }

        return true;
    }

    @GlobalTransactional
    public List<MqMessageBO> saveOpenMember(OpenMemberRequest request){
        if(StrUtil.isEmpty(request.getOrderNo())){
            throw new BusinessException(UserErrorCode.MEMBER_ORDER_NOT_EXIST);
        }
        if (MemberOrderStatusEnum.getByCode(request.getStatus()) == MemberOrderStatusEnum.PAY_SUCCESS && StrUtil.isEmpty(request.getTradeNo())) {
            throw new BusinessException(UserErrorCode.MEMBER_TRADE_NOT_NULL);
        }
        log.info("回调开通/续费会员请求入参：{}", JSONObject.toJSONString(request));

        LambdaQueryWrapper<MemberOrderDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MemberOrderDO::getOrderNo, request.getOrderNo());
        MemberOrderDO memberOrderDO = Optional.ofNullable(memberOrderService.getOne(lambdaQueryWrapper)).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_ORDER_NOT_EXIST));
        //订单状态校验：如果已经为支付成功状态，那么直接过滤不处理
        if (MemberOrderStatusEnum.getByCode(memberOrderDO.getStatus()) == MemberOrderStatusEnum.PAY_SUCCESS) {
            log.info("回调开通/续费会员订单状态已经为支付成功了 当前会员订单号：{}", memberOrderDO.getOrderNo());
            return null;
        }

        memberOrderDO.setTradeNo(request.getTradeNo());
        //更新会员订单表信息
        memberOrderDO.setOpUserId(request.getOpUserId());
        memberOrderDO.setOpTime(new Date());
        memberOrderDO.setStatus(request.getStatus());
        memberOrderDO.setPayWay(request.getPayWay());
        memberOrderDO.setPayChannel(request.getPayChannel());
        if (MemberOrderStatusEnum.getByCode(request.getStatus()) == MemberOrderStatusEnum.PAY_FAIL) {
            log.info("回调开通/续费会员订单支付失败已取消 会员订单号={}", memberOrderDO.getOrderNo());
            memberOrderService.updateById(memberOrderDO);
            // 增加会员订单使用的优惠券退回记录
            this.returnCoupon(request, memberOrderDO);

            return null;
        }

        memberOrderService.updateById(memberOrderDO);

        Long eid = memberOrderDO.getEid();
        MemberBuyStageDO buyStageDO = Optional.ofNullable(memberBuyStageService.getById(memberOrderDO.getBuyStageId())).orElseThrow(() -> new BusinessException(UserErrorCode.BUY_STAGE_NOT_EXIST));

        EnterpriseMemberDO enterpriseMemberDO = enterpriseMemberService.getEnterpriseMember(eid, memberOrderDO.getMemberId());
        Date originalStartTime = Objects.nonNull(enterpriseMemberDO) ? enterpriseMemberDO.getStartTime() : null;
        Date originalEndTime = Objects.nonNull(enterpriseMemberDO) ? enterpriseMemberDO.getEndTime() : null;

        boolean firstStatus = false;
        Date now = new Date();
        if (Objects.nonNull(enterpriseMemberDO)) {
            // 查看是否到期了：到期了就重新设置开始时间和结束时间；没有到期则直接增加结束时间即可
            if (enterpriseMemberDO.getEndTime().before(now)) {
                enterpriseMemberDO.setStartTime(now);
                enterpriseMemberDO.setEndTime(DateUtil.offsetDay(now, buyStageDO.getValidTime()));
            } else {
                // 开通了会员（此时为续费），会员开通时间（续费时此值不变，到期时间往后延续）
                enterpriseMemberDO.setEndTime(DateUtil.offsetDay(enterpriseMemberDO.getEndTime(), buyStageDO.getValidTime()));
            }
            enterpriseMemberDO.setOpUserId(request.getOpUserId());
            enterpriseMemberService.updateById(enterpriseMemberDO);

        } else {
            // 标识为首次开通
            firstStatus = true;

            // 未开通或者已经过期了 （此时为开通）
            enterpriseMemberDO = new EnterpriseMemberDO();
            enterpriseMemberDO.setOpenTime(now);
            enterpriseMemberDO.setMemberId(buyStageDO.getMemberId());
            enterpriseMemberDO.setEid(eid);
            enterpriseMemberDO.setEname(memberOrderDO.getEname());
            enterpriseMemberDO.setStartTime(now);
            enterpriseMemberDO.setEndTime(DateUtil.offsetDay(now, buyStageDO.getValidTime()));
            enterpriseMemberDO.setOpUserId(request.getOpUserId());
            enterpriseMemberService.save(enterpriseMemberDO);

        }

        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(eid)).orElse(new EnterpriseDO());
        MemberDO memberDO = Optional.ofNullable(this.getById(buyStageDO.getMemberId())).orElse(new MemberDO());

        // 生成购买会员记录
        MemberBuyRecordDO buyRecordDO = this.insertMemberBuyRecord(request.getOpUserId(), memberOrderDO, buyStageDO, enterpriseDO, memberDO.getName(), null);
        // 插入企业会员日志表
        this.insertEnterpriseMemberLog(buyRecordDO, enterpriseMemberDO, originalStartTime, originalEndTime);

        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        // 首次开通则发送MQ通知优惠券那边处理
        if (firstStatus) {
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_COUPON_AUTOMATIC_SEND, "", eid.toString(), MqDelayLevel.FIVE_SECONDS);
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageBOList.add(mqMessageBO);
            log.info("首次开通会员 发送MQ通知优惠券那边处理 企业ID：{}", eid);
        }
        // 有推广人的会员购买成功推送MQ通知
        if (Objects.nonNull(memberOrderDO.getPromoterUserId()) && memberOrderDO.getPromoterUserId() != 0) {
            // 非锁定的客户才需要发送通知
            LockCustomerDTO lockCustomerDTO = lockCustomerService.getByLicenseNumber(enterpriseDO.getLicenseNumber());
            if (Objects.isNull(lockCustomerDTO) || lockCustomerDTO.getStatus() == 1) {
                UpdateUserTaskMemberRequest memberRequest = this.getUpdateUserTaskMemberRequest(memberOrderDO, eid, enterpriseDO, request.getOpUserId());
                MqMessageBO messageBO = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_PROMOTER_NOTIFY, "", JSONObject.toJSONString(memberRequest));
                messageBO = mqMessageSendApi.prepare(messageBO);
                mqMessageBOList.add(messageBO);
            }
        }
        // 发送消息通知返利那边
        UpdateMemberRebateRequest rebateRequest = new UpdateMemberRebateRequest();
        rebateRequest.setEid(eid);
        rebateRequest.setOrderNo(memberOrderDO.getOrderNo());
        rebateRequest.setPayAmount(memberOrderDO.getPayAmount());
        rebateRequest.setOriginalPrice(memberOrderDO.getOriginalPrice());
        rebateRequest.setCreateTime(memberOrderDO.getCreateTime());
        rebateRequest.setPromoterId(memberOrderDO.getPromoterId());
        rebateRequest.setSource(buyRecordDO.getSource());
        rebateRequest.setMemberId(memberOrderDO.getMemberId());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS, "", JSONObject.toJSONString(rebateRequest));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageBOList.add(mqMessageBO);
        log.info("开通或续费会员 发送MQ通知返利那边 企业ID：{}，会员购买记录ID：{}", eid, buyRecordDO.getId());

        // 购买会员成功通知策略满赠模块
        MqMessageBO strategyMessage = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS_STRATEGY_SEND, "", memberOrderDO.getOrderNo(), MqDelayLevel.FIVE_SECONDS);
        strategyMessage = mqMessageSendApi.prepare(strategyMessage);
        mqMessageBOList.add(strategyMessage);

        return mqMessageBOList;
    }

    @Override
    public void insertEnterpriseMemberLog(MemberBuyRecordDO buyRecordDO, EnterpriseMemberDO enterpriseMemberDO, Date originalStartTime, Date originalEndTime) {
        EnterpriseMemberLogDO enterpriseMemberLogDO = new EnterpriseMemberLogDO();
        enterpriseMemberLogDO.setEid(buyRecordDO.getEid());
        enterpriseMemberLogDO.setEname(buyRecordDO.getEname());
        enterpriseMemberLogDO.setMemberId(buyRecordDO.getMemberId());
        enterpriseMemberLogDO.setMemberName(buyRecordDO.getMemberName());
        enterpriseMemberLogDO.setOrderNo(buyRecordDO.getOrderNo());
        enterpriseMemberLogDO.setOrderBuyRule(buyRecordDO.getBuyRule());
        enterpriseMemberLogDO.setPromoterId(buyRecordDO.getPromoterId());
        // 操作类型：1-开通 2-续费 3-退费 4-修改推广方
        enterpriseMemberLogDO.setOpType(buyRecordDO.getOpenType());
        enterpriseMemberLogDO.setValidDays(buyRecordDO.getValidDays());
        enterpriseMemberLogDO.setBeforeStartTime(originalStartTime);
        enterpriseMemberLogDO.setBeforeEndTime(originalEndTime);
        enterpriseMemberLogDO.setAfterStartTime(enterpriseMemberDO.getStartTime());
        enterpriseMemberLogDO.setAfterEndTime(enterpriseMemberDO.getEndTime());
        enterpriseMemberLogDO.setOpUserId(buyRecordDO.getOpUserId());
        enterpriseMemberLogService.save(enterpriseMemberLogDO);
    }

    /**
     * 增加会员订单使用的优惠券退回记录
     *
     * @param request
     * @param memberOrderDO
     */
    private void returnCoupon(OpenMemberRequest request, MemberOrderDO memberOrderDO) {
        Long couponId = memberOrderCouponService.getMemberOrderCouponId(memberOrderDO.getOrderNo());
        if (Objects.nonNull(couponId) && couponId != 0) {
            LambdaQueryWrapper<MemberOrderCouponDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemberOrderCouponDO::getCouponId, couponId);
            wrapper.eq(MemberOrderCouponDO::getOrderNo, memberOrderDO.getOrderNo());

            MemberOrderCouponDO memberOrderCouponDO = new MemberOrderCouponDO();
            memberOrderCouponDO.setUseStatus(MemberOrderCouponUseStatusEnum.RETURN.getCode());
            memberOrderCouponDO.setOpUserId(request.getOpUserId());
            memberOrderCouponService.update(memberOrderCouponDO, wrapper);
            log.info("会员订单支付失败退回使用的优惠券 优惠券ID={} 订单号={} 企业ID={}", couponId, memberOrderDO.getOrderNo(), memberOrderDO.getEid());
        }
    }

    /**
     * 生成购买会员记录
     *
     * @param opUserId 操作人ID
     * @param memberOrderDO 会员订单对象
     * @param buyStageDO 购买条件对象
     * @param enterpriseDO 企业对象
     * @param memberName 会员名称
     * @param source 来源
     * @return
     */
    @Override
    public MemberBuyRecordDO insertMemberBuyRecord(Long opUserId, MemberOrderDO memberOrderDO, MemberBuyStageDO buyStageDO, EnterpriseDO enterpriseDO,
                                                   String memberName, Integer source) {
        MemberBuyRecordDO buyRecordDO = new MemberBuyRecordDO();
        buyRecordDO.setMemberId(buyStageDO.getMemberId());
        buyRecordDO.setMemberName(memberName);
        buyRecordDO.setValidDays(buyStageDO.getValidTime());
        buyRecordDO.setBuyRule(buyStageDO.getPrice() + "元购买" + buyStageDO.getValidTime() + "天");
        buyRecordDO.setEid(memberOrderDO.getEid());
        buyRecordDO.setEname(enterpriseDO.getName());
        buyRecordDO.setProvinceCode(enterpriseDO.getProvinceCode());
        buyRecordDO.setProvinceName(enterpriseDO.getProvinceName());
        buyRecordDO.setCityCode(enterpriseDO.getCityCode());
        buyRecordDO.setCityName(enterpriseDO.getCityName());
        buyRecordDO.setRegionCode(enterpriseDO.getRegionCode());
        buyRecordDO.setRegionName(enterpriseDO.getRegionName());
        buyRecordDO.setPayMethod(memberOrderDO.getPayMethod());
        buyRecordDO.setPayAmount(memberOrderDO.getPayAmount());
        buyRecordDO.setPromoterId(memberOrderDO.getPromoterId());
        buyRecordDO.setPromoterName(memberOrderDO.getPromoterName());
        buyRecordDO.setPromoterUserId(memberOrderDO.getPromoterUserId());
        buyRecordDO.setPromoterUserName(memberOrderDO.getPromoterUserName());
        buyRecordDO.setContactorPhone(enterpriseDO.getContactorPhone());
        buyRecordDO.setOrderNo(memberOrderDO.getOrderNo());
        buyRecordDO.setOpUserId(opUserId);
        buyRecordDO.setPayChannel(memberOrderDO.getPayChannel());
        buyRecordDO.setOriginalPrice(memberOrderDO.getOriginalPrice());
        buyRecordDO.setDiscountAmount(memberOrderDO.getDiscountAmount());

        // 来源：只要是导入开通的就按excel指定的来源处理
        Date startTime = new Date();
        if (Objects.nonNull(source) && source != 0) {
            buyRecordDO.setSource(source);
        } else {
            if (memberOrderDO.getPromoterId() == 0 && memberOrderDO.getPromoterUserId() == 0) {
                buyRecordDO.setSource(MemberSourceEnum.B2B_NATURE.getCode());
            } else if (memberOrderDO.getPromoterId() != 0 && memberOrderDO.getPromoterUserId() == 0) {
                buyRecordDO.setSource(MemberSourceEnum.B2B_ENTERPRISE.getCode());
            } else if ((memberOrderDO.getPromoterId() != 0 && memberOrderDO.getPromoterUserId() != 0) || (memberOrderDO.getPromoterId() == 0 && memberOrderDO.getPromoterUserId() != 0)) {
                buyRecordDO.setSource(MemberSourceEnum.ASSISTANT.getCode());
            }
        }

        // 开通类型：查询是否存在开通记录，只要开通记录就算即使是退款的
        Integer count = memberBuyRecordService.selectCountRecords(memberOrderDO.getEid(), memberOrderDO.getMemberId());
        buyRecordDO.setOpenType(count <= 0 ? MemberOpenTypeEnum.FIRST.getCode() : MemberOpenTypeEnum.RENEWAL.getCode());

        // 会员开始结束时间优化，优化为第二购买记录为第一期截止时间之后
        MemberBuyRecordDTO lastBuyRecordDto = memberBuyRecordService.getLastBuyRecord(memberOrderDO.getEid(), memberOrderDO.getMemberId());
        if (Objects.isNull(lastBuyRecordDto)) {
            buyRecordDO.setStartTime(startTime);
            buyRecordDO.setEndTime(DateUtil.offsetDay(startTime, buyStageDO.getValidTime()));
        } else {
            buyRecordDO.setStartTime(lastBuyRecordDto.getEndTime());
            buyRecordDO.setEndTime(DateUtil.offsetDay(lastBuyRecordDto.getEndTime(), buyStageDO.getValidTime()));
        }

        memberBuyRecordService.save(buyRecordDO);
        return buyRecordDO;
    }

    @Override
    public UpdateUserTaskMemberRequest getUpdateUserTaskMemberRequest(MemberOrderDO memberOrderDO, Long eid, EnterpriseDO enterpriseDO, Long opUserId) {
        UpdateUserTaskMemberRequest memberRequest = new UpdateUserTaskMemberRequest();
        memberRequest.setOrderNo(memberOrderDO.getOrderNo());
        memberRequest.setPushType(1);
        memberRequest.setMemberId(memberOrderDO.getMemberId());
        memberRequest.setMemberStageId(memberOrderDO.getBuyStageId());
        memberRequest.setEid(enterpriseDO.getId());
        memberRequest.setEname(enterpriseDO.getName());
        memberRequest.setPromoterId(memberOrderDO.getPromoterId());
        memberRequest.setPromoterUserId(memberOrderDO.getPromoterUserId());
        memberRequest.setContactorPhone(enterpriseDO.getContactorPhone());
        memberRequest.setContactorUserId(Optional.ofNullable(staffService.getByMobile(enterpriseDO.getContactorPhone())).orElse(new Staff()).getId());
        memberRequest.setCreateUser(opUserId);
        memberRequest.setTradeTime(memberOrderDO.getCreateTime());
        log.info("有推广人的会员购买成功推送MQ通知 开通企业ID：{}，推广人ID：{}", eid, memberOrderDO.getPromoterId());
        return memberRequest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSort(UpdateMemberSortRequest request) {
        MemberDO memberDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.MEMBER_NOT_EXIST));

        MemberDO member = new MemberDO();
        member.setId(memberDO.getId());
        member.setSort(request.getSort());
        member.setOpUserId(request.getOpUserId());
        return this.updateById(member);
    }

    @Override
    public CurrentMemberForMarketingDTO getCurrentMemberForMarketing(Long eid) {
        CurrentMemberForMarketingDTO currentMemberDTO = new CurrentMemberForMarketingDTO();
        currentMemberDTO.setCurrentMember(0);
        boolean enterpriseMemberStatus = enterpriseMemberService.getEnterpriseMemberStatus(eid);
        if(enterpriseMemberStatus){
            currentMemberDTO.setCurrentMember(1);
        }
        List<MemberBuyRecordDTO> currentValidMemberRecord = memberBuyRecordService.getCurrentValidMemberRecord(eid);
        if(CollectionUtils.isNotEmpty(currentValidMemberRecord)){
            List<Long> members = currentValidMemberRecord.stream().map(MemberBuyRecordDTO::getMemberId).distinct().collect(Collectors.toList());
            currentMemberDTO.setMemeberIds(members);
            List<Long> promoterIds = currentValidMemberRecord.stream().map(MemberBuyRecordDTO::getPromoterId).distinct().collect(Collectors.toList());
            currentMemberDTO.setPromoterIds(promoterIds);
            if(CollectionUtils.isNotEmpty(promoterIds)){
                currentMemberDTO.setPromoterId(promoterIds.get(0));
            }
        }
        return currentMemberDTO;
    }

    @Override
    public List<MemberSimpleDTO> queryAllList() {
        LambdaQueryWrapper<MemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberDO::getStopGet, 0);
        queryWrapper.orderByDesc(MemberDO::getSort, MemberDO::getUpdateTime);
        return PojoUtils.map(this.list(queryWrapper), MemberSimpleDTO.class);
    }

    private void updateMemberEquity(UpdateMemberRequest request, MemberDO memberDO) {
        List<SaveMemberEquityRelationRequest> memberEquityList = request.getMemberEquityList();
        if (CollUtil.isEmpty(memberEquityList)) {
            return;
        }

        MemberEquityRelationDO equityRelationDO = new MemberEquityRelationDO();
        equityRelationDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<MemberEquityRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberEquityRelationDO::getMemberId, memberDO.getId());
        memberEquityRelationService.batchDeleteWithFill(equityRelationDO, wrapper);

        List<MemberEquityRelationDO> equityRelationDOList = PojoUtils.map(memberEquityList, MemberEquityRelationDO.class);
        equityRelationDOList.forEach(memberEquityRelationDO -> {
            memberEquityRelationDO.setMemberId(memberDO.getId());
            memberEquityRelationDO.setOpUserId(request.getOpUserId());
        });
        memberEquityRelationService.saveBatch(equityRelationDOList);
    }

    private void updateMemberBuyStage(UpdateMemberRequest request, MemberDO memberDO) {
        LambdaQueryWrapper<MemberBuyStageDO> eq = new LambdaQueryWrapper<MemberBuyStageDO>().eq(MemberBuyStageDO::getMemberId, memberDO.getId());
        List<Long> buyStageList = memberBuyStageService.list(eq).stream().map(BaseDO::getId).collect(Collectors.toList());

        List<Long> requestList = request.getMemberBuyStageList().stream().map(UpdateMemberBuyStageRequest::getId).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> removeList = buyStageList.stream().filter(aLong -> !requestList.contains(aLong)).collect(Collectors.toList());

        request.getMemberBuyStageList().forEach(buyStageRequest -> {
            MemberBuyStageDO memberBuyStageDO = PojoUtils.map(buyStageRequest, MemberBuyStageDO.class);
            if (Objects.nonNull(buyStageRequest.getId())) {
                memberBuyStageService.updateById(memberBuyStageDO);
            } else {
                memberBuyStageDO.setMemberId(memberDO.getId());
                memberBuyStageDO.setMemberName(memberDO.getName());
                memberBuyStageService.save(memberBuyStageDO);
            }
        });

        if (CollUtil.isNotEmpty(removeList)) {
            MemberBuyStageDO memberBuyStageDO = new MemberBuyStageDO();
            memberBuyStageDO.setOpUserId(request.getOpUserId());
            LambdaQueryWrapper<MemberBuyStageDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(MemberBuyStageDO::getId, removeList);
            memberBuyStageService.batchDeleteWithFill(memberBuyStageDO, queryWrapper);
        }
    }

}
