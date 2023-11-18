package com.yiling.user.member.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.user.common.enums.NoEnum;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.member.dao.MemberImportOpenMapper;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberRebateRequest;
import com.yiling.user.member.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberImportOpenDO;
import com.yiling.user.member.entity.MemberOrderDO;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;
import com.yiling.user.member.service.MemberBuyStageService;
import com.yiling.user.member.service.MemberImportOpenService;
import com.yiling.user.member.service.MemberOrderService;
import com.yiling.user.member.service.MemberService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B-会员导入批量开通表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-12
 */
@Slf4j
@Service
public class MemberImportOpenServiceImpl extends BaseServiceImpl<MemberImportOpenMapper, MemberImportOpenDO> implements MemberImportOpenService {

    @Autowired
    MemberBuyStageService memberBuyStageService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberOrderService memberOrderService;
    @Autowired
    RedisService redisService;
    @Autowired
    MemberBuyRecordService memberBuyRecordService;

    @Lazy
    @Autowired
    MemberImportOpenServiceImpl _this;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    @Override
    public boolean importBuyMember(ImportOpenMemberRequest request){

        List<MqMessageBO> mqMessageBOList = _this.createBuyMemberData(request);

        //首次开通则发送MQ通知优惠券那边处理
        if (CollUtil.isNotEmpty(mqMessageBOList)) {
            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MqMessageBO> createBuyMemberData(ImportOpenMemberRequest importOpenMemberRequest) {

        // 1.生成会员订单
        String orderNo = this.gen(NoEnum.MEMBER_ORDER_NO);
        if (StrUtil.isEmpty(orderNo)) {
            log.error("生成订单号失败={}", JSONObject.toJSONString(importOpenMemberRequest));
            return ListUtil.toList();
        }

        Long opUserId = importOpenMemberRequest.getOpUserId();
        Long eid = importOpenMemberRequest.getEid();
        Long buyStageId = importOpenMemberRequest.getBuyStageId();
        MemberBuyStageDO buyStageDO = memberBuyStageService.getById(buyStageId);

        EnterpriseDO enterpriseDO = Optional.ofNullable(enterpriseService.getById(eid)).orElse(new EnterpriseDO());
        MemberDO memberDO = Optional.ofNullable(memberService.getById(buyStageDO.getMemberId())).orElse(new MemberDO());

        MemberOrderDO memberOrderDO = PojoUtils.map(importOpenMemberRequest, MemberOrderDO.class);
        memberOrderDO.setOrderNo(orderNo);
        memberOrderDO.setStatus(20);
        memberOrderDO.setRemark("通过运营指定文档数据批量开通会员");
        memberOrderService.save(memberOrderDO);

        // 2.生成企业会员
        LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseMemberDO::getEid, eid);
        queryWrapper.eq(EnterpriseMemberDO::getMemberId, importOpenMemberRequest.getMemberId());
        queryWrapper.last("limit 1");
        EnterpriseMemberDO enterpriseMemberDO = enterpriseMemberService.getOne(queryWrapper);
        Date originalStartTime = Objects.nonNull(enterpriseMemberDO) ? enterpriseMemberDO.getStartTime() : null;
        Date originalEndTime = Objects.nonNull(enterpriseMemberDO) ? enterpriseMemberDO.getEndTime() : null;

        Date now = new Date();
        boolean firstStatus = false;
        if (Objects.nonNull(enterpriseMemberDO)) {
            // 查看是否到期了：到期了就重新设置开始时间和结束时间；没有到期则直接增加结束时间即可
            if (enterpriseMemberDO.getEndTime().before(now)) {
                enterpriseMemberDO.setStartTime(now);
                enterpriseMemberDO.setEndTime(DateUtil.offsetDay(now, buyStageDO.getValidTime()));
            } else {
                // 开通了会员（此时为续费），会员开通时间（续费时此值不变，到期时间往后延续）
                enterpriseMemberDO.setEndTime(DateUtil.offsetDay(enterpriseMemberDO.getEndTime(), buyStageDO.getValidTime()));
            }
            enterpriseMemberDO.setOpUserId(opUserId);
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
            enterpriseMemberDO.setOpUserId(opUserId);
            enterpriseMemberService.save(enterpriseMemberDO);
        }

        // 3.生成会员购买记录
        MemberBuyRecordDO buyRecordDO = memberService.insertMemberBuyRecord(opUserId, memberOrderDO, buyStageDO, enterpriseDO, memberDO.getName(),
                importOpenMemberRequest.getSource());
        // 插入企业会员日志表
        memberService.insertEnterpriseMemberLog(buyRecordDO, enterpriseMemberDO, originalStartTime, originalEndTime);

        List<MqMessageBO> mqMessageBOList = ListUtil.toList();
        // 首次开通则发送MQ通知优惠券那边处理
        if (firstStatus) {
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_MEMBER_COUPON_AUTOMATIC_SEND, "", eid.toString(), MqDelayLevel.FIVE_SECONDS);
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageBOList.add(mqMessageBO);
        }
        // 有推广人的会员购买成功推送MQ通知
        if (Objects.nonNull(memberOrderDO.getPromoterUserId()) && memberOrderDO.getPromoterUserId() != 0) {
            UpdateUserTaskMemberRequest memberRequest = memberService.getUpdateUserTaskMemberRequest(memberOrderDO, eid, enterpriseDO, 0L);
            MqMessageBO messageBO = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_PROMOTER_NOTIFY, "", JSONObject.toJSONString(memberRequest));
            messageBO = mqMessageSendApi.prepare(messageBO);
            mqMessageBOList.add(messageBO);
        }
        // 发送消息通知返利那边
        UpdateMemberRebateRequest rebateRequest = new UpdateMemberRebateRequest();
        rebateRequest.setEid(eid);
        rebateRequest.setOrderNo(memberOrderDO.getOrderNo());
        rebateRequest.setPayAmount(memberOrderDO.getPayAmount());
        rebateRequest.setCreateTime(memberOrderDO.getCreateTime());
        rebateRequest.setPromoterId(memberOrderDO.getPromoterId());
        rebateRequest.setSource(buyRecordDO.getSource());
        rebateRequest.setOriginalPrice(memberOrderDO.getOriginalPrice());
        rebateRequest.setMemberId(memberOrderDO.getMemberId());
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS, "", JSONObject.toJSONString(rebateRequest));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        mqMessageBOList.add(mqMessageBO);

        // 购买会员成功通知策略满赠模块
        MqMessageBO strategyMessage = new MqMessageBO(Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS_STRATEGY_SEND, "", memberOrderDO.getOrderNo(), MqDelayLevel.FIVE_SECONDS);
        strategyMessage = mqMessageSendApi.prepare(strategyMessage);
        mqMessageBOList.add(strategyMessage);

        return mqMessageBOList;
    }

    /**
     * 私有方法，仅供导入生成会员使用
     *
     * @param noEnum
     * @return
     */
    private String gen(NoEnum noEnum) {
        String randomStr = RandomUtil.randomNumbers(noEnum.getRandomNum());

        StringBuilder businessNo = new StringBuilder();
        businessNo.append(noEnum.getPrefix());
        switch(noEnum.getMiddelPartMode()){
            case DATESTR:
                String dateStr = DateUtil.format(new Date(), DATE_FORMAT);
                businessNo.append(dateStr).append(randomStr).toString();
                break;
            case RANDOM:
                businessNo.append(randomStr).toString();
                break;
        }

        // 校验单号是否重复
        String key = RedisKey.generate("NO", noEnum.name(), businessNo.toString());
        if (redisService.hasKey(key)) {
            return this.gen(noEnum);
        } else {
            redisService.set(key, "1", 5);
            return businessNo.toString();
        }
    }


}
