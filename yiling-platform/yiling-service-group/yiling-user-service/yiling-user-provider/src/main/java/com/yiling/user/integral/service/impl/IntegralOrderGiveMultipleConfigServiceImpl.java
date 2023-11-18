package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveMultipleConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.entity.IntegralOrderGiveMultipleConfigDO;
import com.yiling.user.integral.dao.IntegralOrderGiveMultipleConfigMapper;
import com.yiling.user.integral.enums.IntegralCustomerScopeEnum;
import com.yiling.user.integral.enums.IntegralPaymentMethodFlagEnum;
import com.yiling.user.integral.enums.IntegralUserTypeEnum;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;
import com.yiling.user.integral.service.IntegralOrderGiveMemberService;
import com.yiling.user.integral.service.IntegralOrderGiveMultipleConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralOrderGivePaymentMethodService;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分倍数配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderGiveMultipleConfigServiceImpl extends BaseServiceImpl<IntegralOrderGiveMultipleConfigMapper, IntegralOrderGiveMultipleConfigDO> implements IntegralOrderGiveMultipleConfigService {

    @Autowired
    IntegralOrderGiveConfigService integralOrderGiveConfigService;
    @Autowired
    IntegralOrderGivePaymentMethodService integralOrderGivePaymentMethodService;
    @Autowired
    IntegralOrderGiveMemberService integralOrderGiveMemberService;
    @Autowired
    MemberService memberService;

    @Override
    public List<GenerateMultipleConfigDTO> generateMultipleConfig(Long giveRuleId) {
        List<GenerateMultipleConfigDTO> configDTOList = ListUtil.toList();

        IntegralOrderGiveConfigDTO giveConfigDTO = integralOrderGiveConfigService.getByGiveRuleId(giveRuleId);
        Integer paymentMethodFlag = giveConfigDTO.getPaymentMethodFlag();
        // 如果客户范围为全部客户或指定客户，那么生成一条倍数配置
        if (IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ALL
                || IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN) {
            // 如果为全部支付方式
            if (IntegralPaymentMethodFlagEnum.ALL == IntegralPaymentMethodFlagEnum.getByCode(paymentMethodFlag)) {
                GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                configDTO.setUserType(giveConfigDTO.getUserType());
                // 用户类型名称（客户范围为1-全部客户 2-指定客户时展示客户范围名称；客户范围为3-指定客户范围时展示用户类型名称；用户类型为部分会员时展示会员名称）
                configDTO.setUserTypeName(IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()).getName());
                configDTO.setPaymentMethod(0);
                configDTOList.add(configDTO);
            } else {
                List<Integer> paymentMethodList = integralOrderGivePaymentMethodService.getByGiveRuleId(giveRuleId);
                paymentMethodList.forEach(paymentMethod -> {
                    GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                    configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                    configDTO.setUserType(giveConfigDTO.getUserType());
                    configDTO.setUserTypeName(IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()).getName());
                    configDTO.setPaymentMethod(paymentMethod);
                    configDTOList.add(configDTO);
                });
            }

        } else {
            // 如果客户范围为指定客户范围，那么根据用户类型生成倍数配置
            if (IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()) == IntegralUserTypeEnum.SOME_MEMBER) {
                // 部分会员
                List<Long> memberIdList = integralOrderGiveMemberService.getByGiveRuleId(giveRuleId);
                if (CollUtil.isNotEmpty(memberIdList)) {
                    Map<Long, String> memberMap = memberService.listByIds(memberIdList).stream().collect(Collectors.toMap(MemberDO::getId, MemberDO::getName));
                    memberIdList.forEach(memberId -> {
                        // 如果为全部支付方式
                        if (IntegralPaymentMethodFlagEnum.ALL == IntegralPaymentMethodFlagEnum.getByCode(paymentMethodFlag)) {
                            GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                            configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                            configDTO.setUserType(giveConfigDTO.getUserType());
                            configDTO.setMemberId(memberId);
                            configDTO.setUserTypeName(memberMap.get(memberId));
                            configDTO.setPaymentMethod(0);
                            configDTOList.add(configDTO);
                        } else {
                            List<Integer> paymentMethodList = integralOrderGivePaymentMethodService.getByGiveRuleId(giveRuleId);
                            paymentMethodList.forEach(paymentMethod -> {
                                GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                                configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                                configDTO.setUserType(giveConfigDTO.getUserType());
                                configDTO.setMemberId(memberId);
                                configDTO.setUserTypeName(memberMap.get(memberId));
                                configDTO.setPaymentMethod(paymentMethod);
                                configDTOList.add(configDTO);
                            });
                        }
                    });
                }

            } else {
                // 全部用户、普通用户、全部会员
                if (IntegralPaymentMethodFlagEnum.ALL == IntegralPaymentMethodFlagEnum.getByCode(paymentMethodFlag)) {
                    GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                    configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                    configDTO.setUserType(giveConfigDTO.getUserType());
                    configDTO.setUserTypeName(IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()).getName());
                    configDTO.setPaymentMethod(0);
                    configDTOList.add(configDTO);
                } else {
                    List<Integer> paymentMethodList = integralOrderGivePaymentMethodService.getByGiveRuleId(giveRuleId);
                    paymentMethodList.forEach(paymentMethod -> {
                        GenerateMultipleConfigDTO configDTO = new GenerateMultipleConfigDTO();
                        configDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
                        configDTO.setUserType(giveConfigDTO.getUserType());
                        configDTO.setUserTypeName(IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()).getName());
                        configDTO.setPaymentMethod(paymentMethod);
                        configDTOList.add(configDTO);
                    });
                }

            }

        }

        return configDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMultipleConfig(SaveIntegralMultipleConfigRequest request) {
        IntegralOrderGiveMultipleConfigDO configDO = new IntegralOrderGiveMultipleConfigDO();
        configDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<IntegralOrderGiveMultipleConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMultipleConfigDO::getGiveRuleId, request.getGiveRuleId());
        this.batchDeleteWithFill(configDO, wrapper);

        List<IntegralOrderGiveMultipleConfigDO> multipleConfigDOList = ListUtil.toList();
        List<SaveIntegralMultipleConfigRequest.SaveMultipleConfigRequest> multipleConfigRequestList = request.getMultipleConfigList();
        multipleConfigRequestList.forEach(multipleConfigRequest -> {
            IntegralOrderGiveMultipleConfigDO multipleConfigDO = PojoUtils.map(multipleConfigRequest, IntegralOrderGiveMultipleConfigDO.class);
            multipleConfigDO.setGiveRuleId(request.getGiveRuleId());
            multipleConfigDO.setOpUserId(request.getOpUserId());
            multipleConfigDOList.add(multipleConfigDO);
        });

        return this.saveBatch(multipleConfigDOList);
    }

    @Override
    public List<GenerateMultipleConfigDTO> getMultipleConfig(Long giveRuleId) {
        List<GenerateMultipleConfigDTO> configDTOList = ListUtil.toList();

        IntegralOrderGiveConfigDTO giveConfigDTO = integralOrderGiveConfigService.getByGiveRuleId(giveRuleId);
        // 获取倍数配置
        List<IntegralOrderGiveMultipleConfigDTO> multipleConfigDTOList = this.getMultipleConfigList(giveRuleId);

        Map<Long, String> memberMap = MapUtil.newHashMap();
        if (IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()) == IntegralUserTypeEnum.SOME_MEMBER) {
            List<Long> memberIdList = multipleConfigDTOList.stream().map(IntegralOrderGiveMultipleConfigDTO::getMemberId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(memberIdList)) {
                memberMap = memberService.listByIds(memberIdList).stream().collect(Collectors.toMap(BaseDO::getId, MemberDO::getName));
            }
        }

        Map<Long, String> finalMemberMap = memberMap;
        multipleConfigDTOList.forEach(giveMultipleConfigDTO -> {
            GenerateMultipleConfigDTO multipleConfigDTO = new GenerateMultipleConfigDTO();
            multipleConfigDTO.setGiveRuleId(giveRuleId);
            multipleConfigDTO.setCustomerScope(giveConfigDTO.getCustomerScope());
            multipleConfigDTO.setUserType(giveConfigDTO.getUserType());
            // 用户类型名称（客户范围为1-全部客户 2-指定客户时展示客户范围名称；客户范围为3-指定客户范围时展示用户类型名称；用户类型为部分会员时展示会员名称）
            if (IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ALL
                    || IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN) {
                multipleConfigDTO.setUserTypeName(IntegralCustomerScopeEnum.getByCode(giveConfigDTO.getCustomerScope()).getName());
            } else {
                // 如果客户范围为指定客户范围，用户类型为部分会员，那么展示会员名称
                if (IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()) == IntegralUserTypeEnum.SOME_MEMBER) {
                    multipleConfigDTO.setUserTypeName(finalMemberMap.get(giveMultipleConfigDTO.getMemberId()));
                    multipleConfigDTO.setMemberId(giveMultipleConfigDTO.getMemberId());
                } else {
                    // 如果为1-全部用户或2-普通用户或3-全部会员，展示用户类型名称
                    multipleConfigDTO.setUserTypeName(IntegralUserTypeEnum.getByCode(giveConfigDTO.getUserType()).getName());
                }
            }
            multipleConfigDTO.setPaymentMethod(giveMultipleConfigDTO.getPaymentMethod());
            multipleConfigDTO.setIntegralMultiple(giveMultipleConfigDTO.getIntegralMultiple());
            configDTOList.add(multipleConfigDTO);
        });

        return configDTOList;
    }

    public List<IntegralOrderGiveMultipleConfigDTO> getMultipleConfigList(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGiveMultipleConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveMultipleConfigDO::getGiveRuleId, giveRuleId);
        List<IntegralOrderGiveMultipleConfigDO> multipleConfigDOList = this.list(wrapper);
        return PojoUtils.map(multipleConfigDOList, IntegralOrderGiveMultipleConfigDTO.class);
    }

}
