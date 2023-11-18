package com.yiling.mall.member.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.mall.member.dao.EnterpriseMemberMapper;
import com.yiling.mall.member.dao.MemberMapper;
import com.yiling.mall.member.entity.EnterpriseMemberDO;
import com.yiling.mall.member.entity.MemberDO;
import com.yiling.mall.member.enums.MemberExpirationReminderTypeEnum;
import com.yiling.mall.member.service.MemberService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 会员 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/12/13
 */
@Service
@Slf4j
public class MemberServiceImpl extends BaseServiceImpl<MemberMapper, MemberDO> implements MemberService {

    @Autowired
    EnterpriseMemberMapper enterpriseMemberMapper;

    @DubboReference
    SmsApi smsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private final String specialPhonePrefix = "111,110";

    @Override
    public Boolean memberExpirationReminderHandler() {
        LambdaQueryWrapper<MemberDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberDO::getStopGet, 0);
        List<MemberDO> memberDOList = this.list(wrapper);
        if(CollUtil.isEmpty(memberDOList)){
            return true;
        }

        memberDOList.forEach(memberDO -> {

            if(memberDO.getRenewalWarn() == 0){
                log.info("B2B会员到期续费未开启续卡提醒，会员信息：{}",JSONObject.toJSONString(memberDO));
                return;
            }

            //到期前提醒天数
            Integer warnDays = memberDO.getWarnDays();

            LambdaQueryWrapper<EnterpriseMemberDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EnterpriseMemberDO::getMemberId, memberDO.getId());
            queryWrapper.gt(EnterpriseMemberDO::getEndTime,new Date());
            List<EnterpriseMemberDO> enterpriseMemberDoList = enterpriseMemberMapper.selectList(queryWrapper);

            //符合本次检查需要提醒的企业会员
            List<EnterpriseMemberDO> needReminderMemberList = ListUtil.toList();
            enterpriseMemberDoList.forEach(enterpriseMemberDO -> {
                DateTime offsetDay = DateUtil.offsetDay(enterpriseMemberDO.getEndTime(), -warnDays);
                boolean sameDay = DateUtil.isSameDay(offsetDay, new Date());
                if(sameDay){
                    needReminderMemberList.add(enterpriseMemberDO);
                }
            });

            needReminderMemberList.forEach(enterpriseMemberDO -> {

                try {
                    //发送短信提醒
                    String content = StrFormatter.format(MemberExpirationReminderTypeEnum.EXPIRATION_REMINDER.getTemplateContent(),
                            enterpriseMemberDO.getEname() , memberDO.getName() , warnDays);

                    EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(enterpriseMemberDO.getEid()))
                            .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

                    if (StrUtil.isEmpty(enterpriseDTO.getContactorPhone())) {
                        log.info("B2B会员到期续费提醒发送短信时，企业ID为{}的企业手机号为空，取消发送", enterpriseDTO.getId());
                        return;
                    }
                    // 虚拟号码校验
                    String prefix = enterpriseDTO.getContactorPhone().substring(0, 3);
                    String[] phonePrefixArr = specialPhonePrefix.split(",");
                    if (Arrays.asList(phonePrefixArr).contains(prefix)) {
                        log.info("B2B会员到期续费提醒发送短信时，企业ID为{}的企业手机号为虚拟号码，取消发送", enterpriseDTO.getId());
                        return;
                    }

                    boolean result = smsApi.send(enterpriseDTO.getContactorPhone(), content, SmsTypeEnum.EXPIRATION_REMINDER);
                    log.info("B2B会员到期续费提醒发送短信完成，短信内容：{}，发送结果：{}" , content , result);

                }catch (Exception e){
                    log.error("B2B会员到期续费提醒发送短信异常：{}" , e.getMessage());
                }

            });

        });

        return true;
    }


}
