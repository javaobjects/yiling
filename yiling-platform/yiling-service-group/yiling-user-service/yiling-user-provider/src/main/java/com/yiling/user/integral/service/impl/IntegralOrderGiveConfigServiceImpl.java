package com.yiling.user.integral.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.user.integral.entity.IntegralOrderGiveConfigDO;
import com.yiling.user.integral.dao.IntegralOrderGiveConfigMapper;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseDO;
import com.yiling.user.integral.entity.IntegralOrderPlatformGoodsDO;
import com.yiling.user.integral.entity.IntegralOrderSellerDO;
import com.yiling.user.integral.enums.IntegralCustomerScopeEnum;
import com.yiling.user.integral.enums.IntegralEnterpriseTypeEnum;
import com.yiling.user.integral.enums.IntegralGoodsScopeEnum;
import com.yiling.user.integral.enums.IntegralMerchantScopeEnum;
import com.yiling.user.integral.enums.IntegralPaymentMethodFlagEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.integral.enums.IntegralUserTypeEnum;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.user.integral.service.IntegralOrderEnterpriseGoodsService;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseService;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseTypeService;
import com.yiling.user.integral.service.IntegralOrderGiveMemberService;
import com.yiling.user.integral.service.IntegralOrderGiveMultipleConfigService;
import com.yiling.user.integral.service.IntegralOrderGivePaymentMethodService;
import com.yiling.user.integral.service.IntegralOrderPlatformGoodsService;
import com.yiling.user.integral.service.IntegralOrderSellerService;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.service.EnterpriseMemberService;
import com.yiling.user.member.service.MemberBuyRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单送积分配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralOrderGiveConfigServiceImpl extends BaseServiceImpl<IntegralOrderGiveConfigMapper, IntegralOrderGiveConfigDO> implements IntegralOrderGiveConfigService {

    @Autowired
    IntegralOrderGiveEnterpriseTypeService integralOrderGiveEnterpriseTypeService;
    @Autowired
    IntegralOrderGiveEnterpriseService integralOrderGiveEnterpriseService;
    @Autowired
    IntegralOrderGivePaymentMethodService integralOrderGivePaymentMethodService;
    @Autowired
    IntegralGiveRuleService integralGiveRuleService;
    @Autowired
    IntegralOrderPlatformGoodsService integralOrderPlatformGoodsService;
    @Autowired
    IntegralOrderEnterpriseGoodsService integralOrderEnterpriseGoodsService;
    @Autowired
    IntegralOrderSellerService integralOrderSellerService;
    @Autowired
    IntegralOrderGiveMultipleConfigService integralOrderGiveMultipleConfigService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    IntegralOrderGiveMemberService integralOrderGiveMemberService;
    @Autowired
    EnterpriseService enterpriseService;

    @Override
    public IntegralOrderGiveConfigDTO getByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGiveConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGiveConfigDO::getGiveRuleId, giveRuleId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralOrderGiveConfigDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrderGiveIntegral(SaveOrderGiveIntegralRequest request) {
        // 校验：一个商品只能出现在一个规则里面
        this.valid(request);

        IntegralOrderGiveConfigDTO giveConfigDTO = this.getByGiveRuleId(request.getGiveRuleId());
        IntegralOrderGiveConfigDO orderGiveConfigDO = PojoUtils.map(request, IntegralOrderGiveConfigDO.class);
        if (Objects.isNull(giveConfigDTO)) {
            // 保存订单送积分配置
            this.save(orderGiveConfigDO);
        } else {
            // 更新配置
            orderGiveConfigDO.setId(giveConfigDTO.getId());
            this.updateById(orderGiveConfigDO);
        }

        // 如果为指定客户范围
        if (IntegralCustomerScopeEnum.getByCode(request.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN_SCOPE) {
            // 企业类型为指定类型
            List<Integer> enterpriseTypeList = request.getEnterpriseTypeList();
            if (IntegralEnterpriseTypeEnum.getByCode(request.getEnterpriseType()) == IntegralEnterpriseTypeEnum.ASSIGN && enterpriseTypeList.size() > 0) {
                integralOrderGiveEnterpriseTypeService.saveEnterpriseType(request.getGiveRuleId(), enterpriseTypeList, request.getOpUserId());
            }
        }
        // 如果为指定支付方式
        if (IntegralPaymentMethodFlagEnum.getByCode(request.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ASSIGN) {
            List<Integer> paymentMethodList = request.getPaymentMethodList();
            if (CollUtil.isNotEmpty(paymentMethodList)) {
                integralOrderGivePaymentMethodService.savePaymentMethod(request.getGiveRuleId(), paymentMethodList, request.getOpUserId());
            }
        }

        return true;
    }

    /**
     * 自动匹配订单送积分的倍数规则
     *
     * @param request
     * @return 生成订单送积分倍数配置
     */
    @Override
    public GenerateMultipleConfigDTO autoMatchRule(QueryIntegralGiveMatchRuleRequest request) {
        // 获取进行中的规则
        List<IntegralGiveRuleDTO> doingGiveRuleList = integralGiveRuleService.getDoingGiveRuleList();
        if (CollUtil.isEmpty(doingGiveRuleList)) {
            return null;
        }
        List<Long> ruleIdList = doingGiveRuleList.stream().map(BaseDTO::getId).collect(Collectors.toList());

        LambdaQueryWrapper<IntegralOrderGiveConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralOrderGiveConfigDO::getGiveRuleId, ruleIdList);
        List<IntegralOrderGiveConfigDO> giveConfigDOList = this.list(wrapper);
        if (CollUtil.isEmpty(giveConfigDOList)) {
            return null;
        }

        for (IntegralOrderGiveConfigDO orderGiveConfigDO : giveConfigDOList) {
            // 获取倍数配置
            List<GenerateMultipleConfigDTO> multipleConfigList = integralOrderGiveMultipleConfigService.getMultipleConfig(orderGiveConfigDO.getGiveRuleId());
            log.info("订单送积分自动匹配规则 订单号={} 规则ID={} 倍数配置={}", request.getOrderNO(), orderGiveConfigDO.getGiveRuleId(), JSONObject.toJSONString(multipleConfigList));

            // 全部商品
            if (IntegralGoodsScopeEnum.getByCode(orderGiveConfigDO.getGoodsScope()) == IntegralGoodsScopeEnum.ALL_GOODS) {
                return this.checkMerchantScope(request, orderGiveConfigDO, multipleConfigList);

            } else if (IntegralGoodsScopeEnum.getByCode(orderGiveConfigDO.getGoodsScope()) == IntegralGoodsScopeEnum.PLATFORM_SKU) {
                // 指定平台SKU
                List<IntegralOrderPlatformGoodsDO> platformGoodsDOList = integralOrderPlatformGoodsService.listPlatformGoodsByGiveRuleId(orderGiveConfigDO.getGiveRuleId());
                boolean flag = false;
                if (CollUtil.isNotEmpty(platformGoodsDOList)) {
                    for (IntegralOrderPlatformGoodsDO platformGoodsDO : platformGoodsDOList) {
                        if (platformGoodsDO.getStandardId().equals(request.getStandardId()) && platformGoodsDO.getSellSpecificationsId().equals(request.getSpecificationId())) {
                            flag = true;
                            break;
                        }
                    }
                }

                if (flag) {
                    // 商家范围的校验
                    return this.checkMerchantScope(request, orderGiveConfigDO, multipleConfigList);
                }

            } else if (IntegralGoodsScopeEnum.getByCode(orderGiveConfigDO.getGoodsScope()) == IntegralGoodsScopeEnum.SHOP_SKU) {
                // 指定店铺SKU
                List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsDOList = integralOrderEnterpriseGoodsService.listEnterpriseGoodsByRuleId(orderGiveConfigDO.getGiveRuleId());
                List<Long> goodsList = enterpriseGoodsDOList.stream().map(IntegralOrderEnterpriseGoodsDO::getGoodsId).distinct().collect(Collectors.toList());
                if (CollUtil.isNotEmpty(goodsList) && goodsList.contains(request.getGoodsId())) {
                    // 商家范围的校验
                    return this.checkMerchantScope(request, orderGiveConfigDO, multipleConfigList);
                }
            }

        }

        return null;
    }

    /**
     * 商家范围的校验
     *
     * @param request
     * @param orderGiveConfigDO
     * @param multipleConfigList
     * @return
     */
    private GenerateMultipleConfigDTO checkMerchantScope(QueryIntegralGiveMatchRuleRequest request, IntegralOrderGiveConfigDO orderGiveConfigDO, List<GenerateMultipleConfigDTO> multipleConfigList) {
        GenerateMultipleConfigDTO multipleConfigDTO = null;
        // 全部商家
        if (IntegralMerchantScopeEnum.ALL == IntegralMerchantScopeEnum.getByCode(orderGiveConfigDO.getMerchantScope())) {
            // 客户范围的校验
            multipleConfigDTO = this.checkCustomerScope(request, orderGiveConfigDO, multipleConfigList);

        } else {
            // 查询出设置的部分商家
            List<IntegralOrderSellerDO> sellerDOList = integralOrderSellerService.listSellerByGiveRuleId(orderGiveConfigDO.getGiveRuleId());
            List<Long> eidList = sellerDOList.stream().map(IntegralOrderSellerDO::getEid).distinct().collect(Collectors.toList());
            if (eidList.contains(request.getEid())) {
                // 客户范围的校验
                multipleConfigDTO = this.checkCustomerScope(request, orderGiveConfigDO, multipleConfigList);
            }

        }

        return multipleConfigDTO;
    }

    /**
     * 客户范围的校验
     *
     * @param request
     * @param orderGiveConfigDO
     * @param multipleConfigList
     * @return
     */
    private GenerateMultipleConfigDTO checkCustomerScope(QueryIntegralGiveMatchRuleRequest request, IntegralOrderGiveConfigDO orderGiveConfigDO, List<GenerateMultipleConfigDTO> multipleConfigList) {
        GenerateMultipleConfigDTO multipleConfigDTO = null;
        // 客户范围
        if (IntegralCustomerScopeEnum.getByCode(orderGiveConfigDO.getCustomerScope()) == IntegralCustomerScopeEnum.ALL) {
            // 支付方式
            if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                multipleConfigDTO = multipleConfigList.get(0);
            } else {
                for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                    if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                        multipleConfigDTO = configDTO;
                    }
                }
            }

        } else if (IntegralCustomerScopeEnum.getByCode(orderGiveConfigDO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN) {
            // 指定客户
            List<IntegralOrderGiveEnterpriseDO> orderGiveEnterpriseList = integralOrderGiveEnterpriseService.listByRuleIdAndEidList(orderGiveConfigDO.getGiveRuleId(), null);
            List<Long> eidList = orderGiveEnterpriseList.stream().map(IntegralOrderGiveEnterpriseDO::getEid).distinct().collect(Collectors.toList());
            if (eidList.contains(request.getUid())) {
                // 支付方式
                if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                    multipleConfigDTO = multipleConfigList.get(0);
                } else {
                    for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                        if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                            multipleConfigDTO = configDTO;
                        }
                    }
                }
            }

        } else if (IntegralCustomerScopeEnum.getByCode(orderGiveConfigDO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN_SCOPE) {
            // 企业类型
            if (IntegralEnterpriseTypeEnum.getByCode(orderGiveConfigDO.getEnterpriseType()) == IntegralEnterpriseTypeEnum.ALL) {
                multipleConfigDTO = this.checkUserType(request, orderGiveConfigDO, multipleConfigList);
            } else {
                List<Integer> enterpriseTypeList = integralOrderGiveEnterpriseTypeService.getEnterpriseTypeList(orderGiveConfigDO.getGiveRuleId());
                EnterpriseDO enterpriseDO = enterpriseService.getById(request.getUid());
                if (CollUtil.isNotEmpty(enterpriseTypeList) && Objects.nonNull(enterpriseDO) && enterpriseTypeList.contains(enterpriseDO.getType())) {
                    multipleConfigDTO = this.checkUserType(request, orderGiveConfigDO, multipleConfigList);
                }
            }
        }

        return multipleConfigDTO;
    }

    /**
     * 检查指定客户范围-用户类型
     *
     * @param request
     * @param orderGiveConfigDO
     * @param multipleConfigList
     * @return
     */
    private GenerateMultipleConfigDTO checkUserType(QueryIntegralGiveMatchRuleRequest request, IntegralOrderGiveConfigDO orderGiveConfigDO, List<GenerateMultipleConfigDTO> multipleConfigList) {
        GenerateMultipleConfigDTO multipleConfigDTO = null;
        // 全部用户
        if (IntegralUserTypeEnum.ALL_USER == IntegralUserTypeEnum.getByCode(orderGiveConfigDO.getUserType())) {
            // 支付方式
            if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                multipleConfigDTO = multipleConfigList.get(0);
            } else {
                for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                    if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                        multipleConfigDTO = configDTO;
                    }
                }
            }

        } else if (IntegralUserTypeEnum.NORMAL_USER == IntegralUserTypeEnum.getByCode(orderGiveConfigDO.getUserType())) {
            // 普通用户
            List<Long> memberList = enterpriseMemberService.getMemberByEid(request.getUid());
            if (CollUtil.isEmpty(memberList)) {
                // 支付方式
                if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                    multipleConfigDTO = multipleConfigList.get(0);
                } else {
                    for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                        if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                            multipleConfigDTO = configDTO;
                        }
                    }
                }
            }

        } else if (IntegralUserTypeEnum.ALL_MEMBER == IntegralUserTypeEnum.getByCode(orderGiveConfigDO.getUserType())) {
            // 所有会员
            List<Long> memberList = enterpriseMemberService.getMemberByEid(request.getUid());
            if (CollUtil.isNotEmpty(memberList)) {
                // 支付方式
                if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                    multipleConfigDTO = multipleConfigList.get(0);
                } else {
                    for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                        if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                            multipleConfigDTO = configDTO;
                        }
                    }
                }
            }

        } else if (IntegralUserTypeEnum.SOME_MEMBER == IntegralUserTypeEnum.getByCode(orderGiveConfigDO.getUserType())) {
            // 部分会员
            List<Long> memberList = enterpriseMemberService.getMemberByEid(request.getUid());
            if (CollUtil.isNotEmpty(memberList)) {
                List<Long> memberIdList = integralOrderGiveMemberService.getByGiveRuleId(orderGiveConfigDO.getGiveRuleId());
                if (CollUtil.containsAny(memberIdList, memberList)) {
                    // 获取到所有匹配的会员ID
                    BigDecimal integralMultiple = BigDecimal.ZERO;
                    GenerateMultipleConfigDTO generateMultipleConfigDTO = null;
                    for (GenerateMultipleConfigDTO configDTO : multipleConfigList) {
                        // 具体的会员
                        if (memberList.contains(configDTO.getMemberId())) {
                            // 支付方式匹配
                            if (IntegralPaymentMethodFlagEnum.getByCode(orderGiveConfigDO.getPaymentMethodFlag()) == IntegralPaymentMethodFlagEnum.ALL) {
                                if (configDTO.getIntegralMultiple().compareTo(integralMultiple) > 0) {
                                    integralMultiple = configDTO.getIntegralMultiple();
                                    generateMultipleConfigDTO = configDTO;
                                }
                            } else {
                                if (request.getPaymentMethod().equals(configDTO.getPaymentMethod())) {
                                    if (configDTO.getIntegralMultiple().compareTo(integralMultiple) > 0) {
                                        integralMultiple = configDTO.getIntegralMultiple();
                                        generateMultipleConfigDTO = configDTO;
                                    }
                                }
                            }

                        }
                    }

                    multipleConfigDTO = generateMultipleConfigDTO;

                }
            }

        }

        return multipleConfigDTO;
    }

    private void valid(SaveOrderGiveIntegralRequest request) {
        // 获取有效的积分发放规则
        List<IntegralGiveRuleDTO> validGiveRuleList = integralGiveRuleService.getValidGiveRuleList();
        // 保留时间重叠的进行下面的校验
        IntegralGiveRuleDO giveRuleDO = integralGiveRuleService.getById(request.getGiveRuleId());
        List<IntegralGiveRuleDTO> giveRuleDTOList = validGiveRuleList.stream().filter(integralGiveRuleDTO -> this.isOverlap(giveRuleDO.getStartTime(), giveRuleDO.getEndTime(), integralGiveRuleDTO.getStartTime(), integralGiveRuleDTO.getEndTime())).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(giveRuleDTOList)) {
            LambdaQueryWrapper<IntegralOrderGiveConfigDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(IntegralOrderGiveConfigDO::getGiveRuleId, giveRuleDTOList.stream().map(BaseDTO::getId).collect(Collectors.toList()));
            List<IntegralOrderGiveConfigDO> giveConfigDOList = this.list(wrapper);
            List<Long> allRuleIdList = giveConfigDOList.stream().map(IntegralOrderGiveConfigDO::getGiveRuleId).filter(giveRuleId -> giveRuleId.compareTo(request.getGiveRuleId()) != 0).distinct().collect(Collectors.toList());

            if (CollUtil.isNotEmpty(giveConfigDOList)) {
                // 当前规则（编辑时存在）
                IntegralOrderGiveConfigDTO giveConfigDTO = this.getByGiveRuleId(request.getGiveRuleId());

                // 已经存在全部商品则直接提示冲突
                giveConfigDOList.forEach(integralOrderGiveConfigDO -> {
                    if (integralOrderGiveConfigDO.getGoodsScope().equals(IntegralGoodsScopeEnum.ALL_GOODS.getCode())) {
                        throw new BusinessException(UserErrorCode.INTEGRAL_ORDER_GIVE_EXIST);
                    }
                });

                // 全部商品，直接提示无法添加
                if (Objects.nonNull(giveConfigDTO) && giveConfigDTO.getGiveRuleId().compareTo(request.getGiveRuleId()) != 0 && request.getGoodsScope().equals(IntegralGoodsScopeEnum.ALL_GOODS.getCode())) {
                    throw new BusinessException(UserErrorCode.INTEGRAL_ORDER_GIVE_EXIST);
                } else if (Objects.isNull(giveConfigDTO) && request.getGoodsScope().equals(IntegralGoodsScopeEnum.ALL_GOODS.getCode())) {
                    throw new BusinessException(UserErrorCode.INTEGRAL_ORDER_GIVE_EXIST);
                }

                if (request.getGoodsScope().equals(IntegralGoodsScopeEnum.PLATFORM_SKU.getCode())) {
                    List<IntegralOrderPlatformGoodsDO> goodsDOList = integralOrderPlatformGoodsService.listPlatformGoodsByGiveRuleId(request.getGiveRuleId());
                    List<Long> specificationsList = goodsDOList.stream().map(IntegralOrderPlatformGoodsDO::getSellSpecificationsId).distinct().collect(Collectors.toList());

                    List<IntegralOrderPlatformGoodsDO> platformGoodsDOList = integralOrderPlatformGoodsService.listByRuleIdListAndSpecificationsIdList(allRuleIdList, null);
                    List<Long> specificationsIdList = platformGoodsDOList.stream().map(IntegralOrderPlatformGoodsDO::getSellSpecificationsId).distinct().collect(Collectors.toList());

                    if (CollUtil.containsAny(specificationsIdList, specificationsList)) {
                        throw new BusinessException(UserErrorCode.INTEGRAL_ORDER_GIVE_EXIST);
                    }

                } else if (request.getGoodsScope().equals(IntegralGoodsScopeEnum.SHOP_SKU.getCode())) {
                    // 获取当前规则设置的店铺SKu
                    List<IntegralOrderEnterpriseGoodsDO> enterpriseGoodsDOList = integralOrderEnterpriseGoodsService.listEnterpriseGoodsByRuleId(request.getGiveRuleId());
                    List<Long> goodsIsList = enterpriseGoodsDOList.stream().map(IntegralOrderEnterpriseGoodsDO::getGoodsId).collect(Collectors.toList());

                    List<IntegralOrderEnterpriseGoodsDO> orderEnterpriseGoodsDOList = integralOrderEnterpriseGoodsService.listEnterpriseGoodsByRuleIdList(allRuleIdList);
                    List<Long> goodsList = orderEnterpriseGoodsDOList.stream().map(IntegralOrderEnterpriseGoodsDO::getGoodsId).collect(Collectors.toList());
                    if (CollUtil.containsAny(goodsList, goodsIsList)) {
                        throw new BusinessException(UserErrorCode.INTEGRAL_ORDER_GIVE_EXIST);
                    }
                }

            }
        }

    }

    /**
     * 两个时间段是否有重叠
     *
     * @param realStartTime 第一个时间段的开始时间
     * @param realEndTime 第一个时间段的结束时间
     * @param startTime 第二个时间段的开始时间
     * @param endTime 第二个时间段的结束时间
     * @return true 表示时间段有重合
     */
    public boolean isOverlap(Date realStartTime, Date realEndTime, Date startTime, Date endTime) {
        return startTime.before(realEndTime) && endTime.after(realStartTime);
    }

}
