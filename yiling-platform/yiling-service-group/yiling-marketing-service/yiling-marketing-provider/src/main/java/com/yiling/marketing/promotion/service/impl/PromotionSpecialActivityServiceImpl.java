package com.yiling.marketing.promotion.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.marketing.common.enums.PromotionErrorCode;
import com.yiling.marketing.common.util.PromotionAreaUtil;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;

import com.yiling.marketing.promotion.dao.PromotionSpecialActivityMapper;
import com.yiling.marketing.promotion.dto.AvtivityCenterDTO;
import com.yiling.marketing.promotion.dto.PromotionCheckContextDTO;
import com.yiling.marketing.promotion.dto.PromotionSecKillSpecialDTO;
import com.yiling.marketing.promotion.dto.PromotionSpecialActivityAppointmentPageDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityInfoDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityItemInfoDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialActivitySaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialEnterpriseSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialSaveRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionSecKillSpecialDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityEnterpriseDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;
import com.yiling.marketing.promotion.enums.PromotionPermittedTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionTerminalTypeEnum;
import com.yiling.marketing.promotion.service.PromotionActivityService;
import com.yiling.marketing.promotion.service.PromotionBuyRecordService;
import com.yiling.marketing.promotion.service.PromotionCombinationPackageService;
import com.yiling.marketing.promotion.service.PromotionEnterpriseLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsGiftLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsLimitService;
import com.yiling.marketing.promotion.service.PromotionSecKillSpecialService;
import com.yiling.marketing.promotion.service.PromotionSpecialActivityEnterpriseService;
import com.yiling.marketing.promotion.service.PromotionSpecialActivityService;
import com.yiling.marketing.promotion.service.PromotionSpecialAppointmentService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 专场活动主表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
@Slf4j
@Service
public class PromotionSpecialActivityServiceImpl extends BaseServiceImpl<PromotionSpecialActivityMapper, PromotionSpecialActivityDO> implements PromotionSpecialActivityService {

    @Autowired
    private PromotionEnterpriseLimitService enterpriseLimitService;

    @Autowired
    private PromotionSecKillSpecialService secKillSpecialService;

    @Autowired
    private PromotionGoodsGiftLimitService goodsGiftLimitService;

    @Autowired
    private PromotionGoodsLimitService goodsLimitService;

    @Autowired
    private GoodsGiftService goodsGiftService;

    @Autowired
    private PromotionBuyRecordService buyRecordService;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private MemberApi memberApi;

    @Autowired
    private SpringAsyncConfig springAsyncConfig;

    @Autowired
    private PromotionCombinationPackageService combinationPackageService;

    @Autowired
    private PromotionActivityService promotionActivityService;

    @Autowired
    private PromotionSpecialActivityEnterpriseService specialActivityEnterpriseService;

    @Autowired
    private PromotionSpecialAppointmentService specialAppointmentService;

    @DubboReference
    protected GoodsApi goodsApi;

    @DubboReference
    protected UserApi userApi;

    @DubboReference
    protected ShopApi shopApi;

    @DubboReference
    protected EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;


    @Override
    public Page<SpecialActivityPageDTO> querySpecialActivity(SpecialActivityPageRequest request) {

        LambdaQueryWrapper<PromotionSpecialActivityDO> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getId() != null) {
            queryWrapper.eq(PromotionSpecialActivityDO::getId, request.getId());
        }
        if (StringUtils.isNotEmpty(request.getSpecialActivityName())) {
            queryWrapper.like(PromotionSpecialActivityDO::getSpecialActivityName, request.getSpecialActivityName());
        }
        if (request.getStartTime() != null) {
            queryWrapper.between(PromotionSpecialActivityDO::getEndTime, request.getStartTime(), request.getEndTime());
        }
        if (request.getBeginStartTime() != null) {
            queryWrapper.between(PromotionSpecialActivityDO::getStartTime, request.getBeginStartTime(), request.getBeginEndTime());
        }
        if (request.getStatus() != null && request.getStatus() == 1) {
            //1-未开始；2-进行中；3-已结束
            // 1启用 2停用
            queryWrapper.eq(PromotionSpecialActivityDO::getStatus, 1);
            queryWrapper.gt(PromotionSpecialActivityDO::getStartTime, new Date());
        }
        if (request.getStatus() != null && request.getStatus() == 2) {
            //1-未开始；2-进行中；3-已结束
            // 1启用 2停用
            queryWrapper.eq(PromotionSpecialActivityDO::getStatus, 1);
            queryWrapper.lt(PromotionSpecialActivityDO::getStartTime, new Date());
            queryWrapper.gt(PromotionSpecialActivityDO::getEndTime, new Date());
        }
        if (request.getStatus() != null && request.getStatus() == 3) {
            //1-未开始；2-进行中；3-已结束
            // 1启用 2停用
            queryWrapper.eq(PromotionSpecialActivityDO::getStatus, 2).or().lt(PromotionSpecialActivityDO::getEndTime, new Date());
        }
        if (request.getType() != null) {
            queryWrapper.eq(PromotionSpecialActivityDO::getType, request.getType());
        }
        queryWrapper.orderByDesc(PromotionSpecialActivityDO::getStartTime);
        Page<PromotionSpecialActivityDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Page<>();
        }
        // 获取创建人列表，用来获取创建人手机号码
        List<SpecialActivityPageDTO> specialActivityPageDTOS = new ArrayList<>();
        page.getRecords().forEach(item -> {
            specialActivityPageDTOS.add(PojoUtils.map(item, SpecialActivityPageDTO.class));
        });

        List<Long> createrIds = specialActivityPageDTOS.stream().map(SpecialActivityPageDTO::getCreateUser).collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(createrIds);
        if (CollectionUtils.isNotEmpty(userDTOS)) {
            Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            specialActivityPageDTOS.forEach(item -> {
                UserDTO userDTO = userDTOMap.get(item.getCreateUser());
                item.setCreaterName(userDTO.getName());
                item.setMobile(userDTO.getMobile());
                //progress 1待开始，2进行中，3已结束
                if (item.getStatus() == 1) {
                    if (item.getStartTime().after(new Date())) {
                        item.setProgress(1);
                    } else if (item.getEndTime().before(new Date())) {
                        item.setProgress(3);
                    } else {
                        item.setProgress(2);
                    }
                }
                // 活动状态下架，进度状态已结束
                if (item.getStatus() == 2) {
                    item.setProgress(3);
                }
            });
        }
        Page<SpecialActivityPageDTO> result = new Page<>(request.getCurrent(), request.getSize(), page.getTotal());
        result.setRecords(specialActivityPageDTOS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    public long savePromotionSpecialActivity(PromotionSpecialSaveRequest request) {
        PromotionSpecialActivitySaveRequest specialActivity = request.getPromotionSpecialActivitySave();
        PromotionSpecialActivityDO promotionSpecialActivityDO = PojoUtils.map(specialActivity, PromotionSpecialActivityDO.class);
        if (request.getId() == null) {
            // 校验名称是否重复
            QueryWrapper<PromotionSpecialActivityDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(PromotionSpecialActivityDO::getSpecialActivityName, promotionSpecialActivityDO.getSpecialActivityName()).eq(PromotionSpecialActivityDO::getStatus, 1).eq(PromotionSpecialActivityDO::getDelFlag, 0).gt(PromotionSpecialActivityDO::getEndTime, new Date());
            List<PromotionSpecialActivityDO> specialActivityDOS = this.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(specialActivityDOS)) {
                log.error("创建专场活动名称重复,  name -> {}", promotionSpecialActivityDO.getSpecialActivityName());
                throw new BusinessException(CouponActivityErrorCode.SPECIAL_ACTIVITY_EXITS);
            }
            this.save(promotionSpecialActivityDO);
        } else {
            promotionSpecialActivityDO.setId(request.getId());
            this.updateById(promotionSpecialActivityDO);
        }
        Long promotionSpecialActivityId = promotionSpecialActivityDO.getId();
        List<PromotionSpecialEnterpriseSaveRequest> enterpriseLimitList = request.getEnterpriseSaves();
        QueryWrapper<PromotionSpecialActivityEnterpriseDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PromotionSpecialActivityEnterpriseDO::getSpecialActivityId, promotionSpecialActivityId);
        //List<PromotionSpecialActivityEnterpriseDO> enterpriseDOList = specialActivityEnterpriseService.list(queryWrapper);
        PromotionSpecialActivityEnterpriseDO enterpriseDO1 = new PromotionSpecialActivityEnterpriseDO();
        enterpriseDO1.setOpUserId(request.getOpUserId());
        enterpriseDO1.setOpTime(new Date());
        enterpriseDO1.setDelFlag(1);
        specialActivityEnterpriseService.batchDeleteWithFill(enterpriseDO1, queryWrapper);
        enterpriseLimitList.forEach(item -> {
            PromotionSpecialActivityEnterpriseDO enterpriseDO = PojoUtils.map(item, PromotionSpecialActivityEnterpriseDO.class);
            enterpriseDO.setSpecialActivityId(promotionSpecialActivityId);
            specialActivityEnterpriseService.save(enterpriseDO);
        });
        return promotionSpecialActivityId;
    }

    @Override
    public SpecialActivityPageDTO querySpecialActivityInfo(Long id) {
        // 专场活动主表
        PromotionSpecialActivityDO specialActivityDO = this.getById(id);
        if (specialActivityDO == null) {
            return new SpecialActivityPageDTO();
        }
        SpecialActivityPageDTO specialActivityPageDTO = PojoUtils.map(specialActivityDO, SpecialActivityPageDTO.class);
        // 专场活动关联表
        QueryWrapper<PromotionSpecialActivityEnterpriseDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(PromotionSpecialActivityEnterpriseDO::getSpecialActivityId, id);
        objectQueryWrapper.lambda().eq(PromotionSpecialActivityEnterpriseDO::getDelFlag, 0);
        List<PromotionSpecialActivityEnterpriseDO> enterpriseDOList = specialActivityEnterpriseService.list(objectQueryWrapper);
        if (CollectionUtils.isEmpty(enterpriseDOList)) {
            return new SpecialActivityPageDTO();
        }

        List<Long> activityIds = enterpriseDOList.stream().map(PromotionSpecialActivityEnterpriseDO::getPromotionActivityId).collect(Collectors.toList());
        List<Long> enterpriseIds = enterpriseDOList.stream().map(PromotionSpecialActivityEnterpriseDO::getEid).collect(Collectors.toList());
        //活动关联的企业和营销活动信息
        List<PromotionActivityDO> activityDOList = promotionActivityService.listByIdList(activityIds);
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(enterpriseIds);
        // 找到参与活动企业和营销活动的名称
        if (CollectionUtils.isNotEmpty(activityDOList) && CollectionUtils.isNotEmpty(enterpriseDTOS)) {
            Map<Long, PromotionActivityDO> promotionActivityDOMap = activityDOList.stream().collect(Collectors.toMap(PromotionActivityDO::getId, Function.identity()));
            Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOS.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
            List<SpecialActivityEnterpriseDTO> enterpriseDTOS1 = PojoUtils.map(enterpriseDOList, SpecialActivityEnterpriseDTO.class);
            enterpriseDTOS1.forEach(item -> {
                PromotionActivityDO promotionActivityDO = promotionActivityDOMap.get(item.getPromotionActivityId());
                item.setPromotionActivityName(promotionActivityDO.getName());
                item.setStartTime(promotionActivityDO.getBeginTime());
                item.setEndTime(promotionActivityDO.getEndTime());
                EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(item.getEid());
                item.setPromotionActivityName(promotionActivityDO.getName());
                item.setEnterpriseName(enterpriseDTO.getName());
            });
            specialActivityPageDTO.setSpecialActivityEnterpriseDTOS(enterpriseDTOS1);
        }
        return specialActivityPageDTO;
    }

    @Override
    public Page<SpecialActivityEnterpriseDTO> pageActivityInfo(SpecialActivityPageRequest request) {
        Page<PromotionActivityDO> activityDOPage = new Page<>(request.getCurrent(), request.getSize());
        Page<SpecialActivityEnterpriseDTO> enterpriseDTOPage = this.baseMapper.pageActivityInfo(activityDOPage, request);
        List<SpecialActivityEnterpriseDTO> enterpriseDTOS = enterpriseDTOPage.getRecords();

        if (CollectionUtils.isNotEmpty(enterpriseDTOS)) {
            enterpriseDTOS.forEach(item -> {
                // 一个活动id和一个企业id结合起来是唯一的，不能再次添加.默认没有添加过.专场活动下线了，或者时间解释了，可以再次添加
                List<PromotionSpecialActivityDO> specialActivityInfo = this.baseMapper.getSpecialActivityInfo(item.getEid(), item.getPromotionActivityId());
                if (CollectionUtils.isNotEmpty(specialActivityInfo)) {
                    List<PromotionSpecialActivityDO> collect = specialActivityInfo.stream().filter(e -> e.getStatus() == 2 || e.getDelFlag() == 1 || e.getEndTime().before(new Date())).collect(Collectors.toList());
                    if (collect.size() != specialActivityInfo.size()) {
                        item.setIsUsed(true);
                    }
                }
            });
        }
        return enterpriseDTOPage;
    }

    @Override
    public boolean editStatusById(PromotionActivityStatusRequest request) {
        PromotionSpecialActivityDO specialActivityDO = PojoUtils.map(request, PromotionSpecialActivityDO.class);
        specialActivityDO.setUpdateUser(request.getOpUserId());
        specialActivityDO.setUpdateTime(request.getOpTime());
        // 停用不修改enterprise的delfalg，编辑删除的时候才有。别的地方使用关联主表状态
        return this.updateById(specialActivityDO);
    }

    @Override
    public Page<PromotionSpecialActivityAppointmentPageDTO> querySpecialActivityAppointment(SpecialActivityPageRequest request) {
        String enterpriseName = request.getSpecialActivityEnterpriseName();
        if(StringUtils.isNotEmpty(enterpriseName)){
            QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
            byNameRequest.setName(enterpriseName);
            byNameRequest.setType(EnterpriseTypeEnum.BUSINESS.getCode());
            List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(byNameRequest);
            if(CollectionUtils.isNotEmpty(enterpriseListByName)){
                List<Long> eids = enterpriseListByName.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            }
        }
        Page<PromotionSpecialAppointmentDO> activityDOPage = new Page<>(request.getCurrent(), request.getSize());
        Page<PromotionSpecialActivityAppointmentPageDTO> appointmentInfo = this.baseMapper.pageSpecialActivityAppointmentInfo(activityDOPage, request);
        if (CollectionUtils.isNotEmpty(appointmentInfo.getRecords())) {
            List<PromotionSpecialActivityAppointmentPageDTO> appointmentPageDTOS = appointmentInfo.getRecords();
            List<Long> userEIds = appointmentPageDTOS.stream().map(PromotionSpecialActivityAppointmentPageDTO::getAppointmentUserEid).collect(Collectors.toList());
            List<Long> userIds = appointmentPageDTOS.stream().map(PromotionSpecialActivityAppointmentPageDTO::getAppointmentUserId).collect(Collectors.toList());
            List<Long> eids = appointmentPageDTOS.stream().map(PromotionSpecialActivityAppointmentPageDTO::getSpecialActivityEnterpriseId).collect(Collectors.toList());
            //appointmentUserEId预约人所在企业  specialActivityEnterpriseId营销活动参与企业
            List<Long> ids = new ArrayList<>();
            ids.addAll(userEIds);
            ids.addAll(eids);
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(ids);
            List<UserDTO> userDTOS = userApi.listByIds(userIds);
            if (CollectionUtils.isNotEmpty(enterpriseDTOS) && CollectionUtils.isNotEmpty(userDTOS)) {
                Map<Long, String> enterpriseDTOMap = enterpriseDTOS.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
                Map<Long, String> userMobile = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getMobile));
                appointmentPageDTOS.forEach(item -> {
                    item.setAppointmentUserEnterpriseName(enterpriseDTOMap.get(item.getAppointmentUserEid()));
                    item.setSpecialActivityEnterpriseName(enterpriseDTOMap.get(item.getSpecialActivityEnterpriseId()));
                    item.setMobile(userMobile.get(item.getAppointmentUserId()));
                    item.getStartTime();
                    if (item.getStatus() == 1) {
                        if (item.getStartTime().after(new Date())) {
                            item.setProcess(1);
                        } else if (item.getEndTime().before(new Date())) {
                            item.setProcess(3);
                        } else {
                            item.setProcess(2);
                        }
                    }
                    // 活动状态下架，进度状态已结束
                    if (item.getStatus() == 2) {
                        item.setProcess(3);
                    }

                });
            }
        }
        return appointmentInfo;
    }

    @Override
    public AvtivityCenterDTO activityCenter(Long currentEid) {

        List<SpecialActivityPageDTO> specialActivityPageDTOS = this.baseMapper.activityCenter();
        if (CollectionUtils.isEmpty(specialActivityPageDTOS)) {
            return new AvtivityCenterDTO();
        }
        List<Long> eids = specialActivityPageDTOS.stream().map(SpecialActivityPageDTO::getEid).distinct().collect(Collectors.toList());

        Map<Long, Boolean> customerEid = shopApi.checkSaleAreaByCustomerEid(currentEid, eids);
        // 得到销售区域允许的专场活动
        List<Long> ids = specialActivityPageDTOS.stream().filter(item -> customerEid.get(item.getEid())).map(SpecialActivityPageDTO::getId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return new AvtivityCenterDTO();
        }

        specialActivityPageDTOS = specialActivityPageDTOS.stream().filter(item -> ids.contains(item.getId())).collect(Collectors.toList());
        specialActivityPageDTOS = new ArrayList<>(specialActivityPageDTOS.stream().collect(Collectors.toMap(SpecialActivityPageDTO::getId, Function.identity(), (oldValue, newValue) -> oldValue)).values());
        specialActivityPageDTOS.sort(Comparator.comparing(SpecialActivityPageDTO::getStartTime).reversed().thenComparing(SpecialActivityPageDTO::getCreateTime));
        // 通过专场活动id找到营销活动id，然后验证营销活动的限制
        List<Long> specialActivityIds = specialActivityPageDTOS.stream().map(SpecialActivityPageDTO::getId).collect(Collectors.toList());
        QueryWrapper<PromotionSpecialActivityEnterpriseDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().in(PromotionSpecialActivityEnterpriseDO::getSpecialActivityId, specialActivityIds);
        List<PromotionSpecialActivityEnterpriseDO> enterpriseDOList = specialActivityEnterpriseService.list(objectQueryWrapper);
        if (CollectionUtils.isEmpty(enterpriseDOList)) {
            return new AvtivityCenterDTO();
        }
        List<Long> promotionActivityId = enterpriseDOList.stream().map(PromotionSpecialActivityEnterpriseDO::getPromotionActivityId).collect(Collectors.toList());

        List<PromotionSecKillSpecialDO> secKillSpecialList = secKillSpecialService.queryByPromotionActivityIdList(promotionActivityId);
        Map<Long, PromotionSecKillSpecialDO> secKillSpecialMap = secKillSpecialList.stream().collect(Collectors.toMap(PromotionSecKillSpecialDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
        EnterpriseDTO enterprise = enterpriseApi.getById(currentEid);
        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(currentEid);
        List<PromotionSpecialActivityEnterpriseDO> canBuySpecialActivity = new ArrayList<>();
        enterpriseDOList.forEach(item -> {
            // 满赠，没有这些营销活动限制会员，销售区域，商家类型
            PromotionSecKillSpecialDO secKillSpecialDO = secKillSpecialMap.get(item.getPromotionActivityId());
            PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
            PromotionSecKillSpecialDTO secKillSpecialDTO = PojoUtils.map(secKillSpecialDO, PromotionSecKillSpecialDTO.class);
            contextDTO.setSecKillSpecialDTO(secKillSpecialDTO);
            contextDTO.setEnterprise(enterprise);
            contextDTO.setMember(member);
            boolean canBuy = terminalTypeCheck(contextDTO) && permittedAreaTypeCheck(contextDTO) && permittedEnterpriseTypeCheck(contextDTO);
            if (canBuy) {
                canBuySpecialActivity.add(item);
            }
        });
        List<Long> canBuySpecialActivityIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(canBuySpecialActivity)) {
            canBuySpecialActivityIds = canBuySpecialActivity.stream().map(PromotionSpecialActivityEnterpriseDO::getSpecialActivityId).collect(Collectors.toList());
        }
        AvtivityCenterDTO avtivityCenterDTO = new AvtivityCenterDTO();
        if (CollectionUtils.isNotEmpty(specialActivityPageDTOS)) {
            List<Long> finalCanBuySpecialActivityIds = canBuySpecialActivityIds;
            specialActivityPageDTOS = specialActivityPageDTOS.stream().filter(item -> item.getType() == 1 || (CollectionUtils.isNotEmpty(finalCanBuySpecialActivityIds) && finalCanBuySpecialActivityIds.contains(item.getId()))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(specialActivityPageDTOS)) {
                return new AvtivityCenterDTO();
            }
            List<String> giftLimit = new ArrayList<>();
            List<String> combination = new ArrayList<>();
            List<String> seckill = new ArrayList<>();
            List<String> specialPrice = new ArrayList<>();
            // 1-满赠,2-特价,3-秒杀,4-组合包
            AtomicInteger giftLimitNumber = new AtomicInteger();
            AtomicInteger combinationNumber = new AtomicInteger();
            AtomicInteger seckillNumber = new AtomicInteger();
            AtomicInteger specialPriceNumber = new AtomicInteger();
            specialActivityPageDTOS.forEach(item -> {
                if (item.getType() == 1) {
                    giftLimitNumber.getAndIncrement();
                    if (giftLimit.size() < 3) {
                        giftLimit.add(item.getSpecialActivityName());
                    }
                }
                if (item.getType() == 2) {
                    specialPriceNumber.getAndIncrement();
                    if (specialPrice.size() < 3) {
                        specialPrice.add(item.getSpecialActivityName());
                    }
                }
                if (item.getType() == 3) {
                    seckillNumber.getAndIncrement();
                    if (seckill.size() < 3) {
                        seckill.add(item.getSpecialActivityName());
                    }
                }
                if (item.getType() == 4) {
                    combinationNumber.getAndIncrement();
                    if (combination.size() < 3) {
                        combination.add(item.getSpecialActivityName());
                    }
                }
            });
            avtivityCenterDTO.setGiftLimitNumber(giftLimitNumber.get());
            avtivityCenterDTO.setCombinationNumber(combinationNumber.get());
            avtivityCenterDTO.setSpecialPriceNumber(specialPriceNumber.get());
            avtivityCenterDTO.setSeckillNumber(seckillNumber.get());
            avtivityCenterDTO.setGiftLimit(giftLimit);
            avtivityCenterDTO.setSpecialPrice(specialPrice);
            avtivityCenterDTO.setSeckill(seckill);
            avtivityCenterDTO.setCombination(combination);
        }
        return avtivityCenterDTO;
    }

    @Override
    public List<SpecialAvtivityInfoDTO> specialActivityInfo(SpecialActivityInfoRequest request) {
        // 企业名称查询到eids，然后过滤
        List<Long> enterpriseIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(request.getEname())) {
            QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
            enterprisePageListRequest.setName(request.getEname());
            enterprisePageListRequest.setCurrent(1);
            enterprisePageListRequest.setSize(100);
            //这个方法返回有专场活动的企业集合，页面必须显示名称匹配且有专场活动的企业
            enterpriseIds = this.baseMapper.pageSpecialActivityEidInfo(request);
            enterprisePageListRequest.setIds(enterpriseIds);
            Page<EnterpriseDTO> page = enterpriseApi.pageList(enterprisePageListRequest);
            List<EnterpriseDTO> enterpriseDTOList = page.getRecords();
            if (CollectionUtils.isEmpty(enterpriseDTOList)) {
                return new ArrayList<>();
            }
            if (CollectionUtils.isNotEmpty(enterpriseDTOList)) {
                enterpriseIds = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                request.setEids(enterpriseIds);
            }
        } else {
            enterpriseIds = this.baseMapper.pageSpecialActivityEidInfo(request);
            request.setEids(enterpriseIds);
        }
        if (CollectionUtils.isEmpty(enterpriseIds)) {
            return new ArrayList<>();
        }
        // 过滤掉销售区域不符合的
        Map<Long, Boolean> customerEids = shopApi.checkSaleAreaByCustomerEid(request.getCurrentEid(), enterpriseIds);
        enterpriseIds = enterpriseIds.stream().filter(item -> customerEids.get(item)).collect(Collectors.toList());

        List<SpecialAvtivityItemInfoDTO> specialAvtivityItemInfoDTOS = this.baseMapper.pageSpecialActivityInfo(request);
        //要有顺序，商户顺序 map会打乱顺序
        List<SpecialAvtivityInfoDTO> specialAvtivityInfoDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(specialAvtivityItemInfoDTOS)) {
            //过滤掉不能购买的营销活动
            if (request.getType() != 1) {
                List<Long> promotionActivityId = specialAvtivityItemInfoDTOS.stream().map(SpecialAvtivityItemInfoDTO::getPromotionActivityId).collect(Collectors.toList());
                List<PromotionSecKillSpecialDO> secKillSpecialList = secKillSpecialService.queryByPromotionActivityIdList(promotionActivityId);
                Map<Long, PromotionSecKillSpecialDO> secKillSpecialMap = secKillSpecialList.stream().collect(Collectors.toMap(PromotionSecKillSpecialDO::getPromotionActivityId, o -> o, (k1, k2) -> k1));
                EnterpriseDTO enterprise = enterpriseApi.getById(request.getCurrentEid());
                CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(request.getCurrentEid());
                List<SpecialAvtivityItemInfoDTO> canBuySpecialActivity = new ArrayList<>();
                specialAvtivityItemInfoDTOS.forEach(item -> {
                    // 满赠，没有这些营销活动限制会员，销售区域，商家类型
                    PromotionSecKillSpecialDO secKillSpecialDO = secKillSpecialMap.get(item.getPromotionActivityId());
                    PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
                    PromotionSecKillSpecialDTO secKillSpecialDTO = PojoUtils.map(secKillSpecialDO, PromotionSecKillSpecialDTO.class);
                    contextDTO.setSecKillSpecialDTO(secKillSpecialDTO);
                    contextDTO.setEnterprise(enterprise);
                    contextDTO.setMember(member);
                    boolean canBuy = terminalTypeCheck(contextDTO) && permittedAreaTypeCheck(contextDTO) && permittedEnterpriseTypeCheck(contextDTO);
                    if (canBuy) {
                        canBuySpecialActivity.add(item);
                    }
                });
                if (CollectionUtils.isEmpty(canBuySpecialActivity)) {
                    return new ArrayList<>();
                }
                specialAvtivityItemInfoDTOS = canBuySpecialActivity;
            }
            //找到我的预约
            Long userId = request.getUserId();
            QueryWrapper<PromotionSpecialAppointmentDO> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().eq(PromotionSpecialAppointmentDO::getAppointmentUserEid, request.getCurrentEid());
            objectQueryWrapper.lambda().eq(PromotionSpecialAppointmentDO::getDelFlag, 0);
            List<PromotionSpecialAppointmentDO> appointmentDOS = specialAppointmentService.list(objectQueryWrapper);
            specialAvtivityItemInfoDTOS.forEach(item -> {
                Optional<PromotionSpecialAppointmentDO> matchEd = appointmentDOS.stream().filter(e -> LongIsEquals(e.getSpecialActivityId(), item.getSpecialActivityId()) && LongIsEquals(e.getSpecialActivityEnterpriseId(), item.getSpecialActivityEnterpriseId())).findAny();
                if (matchEd.isPresent()) {
                    item.setIsAppointment(true);
                } else {
                    item.setIsAppointment(false);
                }
            });
            //List<Long> eids = specialAvtivityItemInfoDTOS.stream().map(SpecialAvtivityItemInfoDTO::getEid).collect(Collectors.toList());
            // 获取企业信息
            List<Long> sortedEids = enterpriseIds;
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(sortedEids);
            Map<Long, String> eidNames = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
            Map<Long, List<SpecialAvtivityItemInfoDTO>> specialActivityListGroupByEid = specialAvtivityItemInfoDTOS.stream().collect(Collectors.groupingBy(SpecialAvtivityItemInfoDTO::getEid));
            // 获取活动信息
            //从数据库查到
            // 内部是否预约，外部起配，是否建材
            List<ShopDTO> shopDTOS = shopApi.listShopByEidList(sortedEids);
            // 判断是否建采

            Map<Long, Integer> longBooleanMap = enterprisePurchaseApplyApi.getPurchaseApplyStatus(sortedEids, request.getCurrentEid());
            Map<Long, BigDecimal> startAmount = shopDTOS.stream().collect(Collectors.toMap(ShopDTO::getShopEid, ShopDTO::getStartAmount));
            if (longBooleanMap == null || startAmount == null) {
                throw new BusinessException(PromotionErrorCode.PROMOTION_GET_ENTERPRISE_INFO_ERROR);
            }
            sortedEids.forEach(item -> {
                SpecialAvtivityInfoDTO specialAvtivityInfoDTO = new SpecialAvtivityInfoDTO();
                specialAvtivityInfoDTO.setEid(item);
                // 是否建采
                specialAvtivityInfoDTO.setContract(longBooleanMap.get(item) == 3);
                // 起配数量
                specialAvtivityInfoDTO.setMinSize(startAmount.get(item));
                specialAvtivityInfoDTO.setEnterpriseName(eidNames.get(item));
                List<SpecialAvtivityItemInfoDTO> specialAvtivityItem = specialActivityListGroupByEid.get(item);
                // 过滤掉不能购买的营销火哦的那个
                if(CollectionUtils.isNotEmpty(specialAvtivityItem)){
                    specialAvtivityItem.sort(Comparator.comparing(SpecialAvtivityItemInfoDTO::getStartTime).reversed().thenComparing(SpecialAvtivityItemInfoDTO::getPromotionActivityId));
                    specialAvtivityInfoDTO.setActivityInfo(specialAvtivityItem);
                }
                // 如果没有专场活动的企业不展示企业信息
                if (CollectionUtils.isNotEmpty(specialAvtivityInfoDTO.getActivityInfo())) {
                    specialAvtivityInfoDTOS.add(specialAvtivityInfoDTO);
                }
            });
        }
        return specialAvtivityInfoDTOS;
    }

    private boolean LongIsEquals(Long target, Long source) {
        if (target == null || source == null) {
            return false;
        }
        if (target.toString().equals(source.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 终端身份校验
     *
     * @param context
     * @return
     */
    public boolean terminalTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        // 终端身份 1-会员，2-非会员
        Integer terminalType = context.getSecKillSpecialDTO().getTerminalType();

        CurrentMemberForMarketingDTO member = context.getMember();
        Integer currentMember = member.getCurrentMember();

        if (PromotionTerminalTypeEnum.MEMBER.getType().equals(terminalType) && Integer.valueOf(1).equals(currentMember)) {
            return true;
        }
        if (PromotionTerminalTypeEnum.NOT_MEMBER.getType().equals(terminalType) && Integer.valueOf(0).equals(currentMember)) {
            return true;
        }
        // 如果终端身份是3，表示不限制，不做校验
        if (PromotionTerminalTypeEnum.ALL.getType().equals(terminalType)) {
            return true;
        }
        log.info("[terminalTypeCheck]终端身份校验不通过，参数terminalType：{}, currentMember: {}", terminalType, currentMember);
        return false;
    }

    /**
     * 允许购买区域校验
     *
     * @param context
     * @return
     */
    public boolean permittedAreaTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        // 允许购买区域 1-全部，2-部分
        Integer permittedAreaType = context.getSecKillSpecialDTO().getPermittedAreaType();

        // 允许购买区域明细json
        String permittedAreaDetail = context.getSecKillSpecialDTO().getPermittedAreaDetail();

        // 所属区域编码
        String regionCode = context.getEnterprise().getRegionCode();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedAreaType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedAreaType) && StringUtils.isNotBlank(permittedAreaDetail)) {
            boolean checkResult = PromotionAreaUtil.check(permittedAreaDetail, regionCode);
            log.info("[terminalTypeCheck]购买区域校验结果，PromotionAreaUtil.check：{}，参数：{}", checkResult, context);
            return checkResult;

        }
        log.info("[terminalTypeCheck]购买区域校验不通过，参数：{}", context);
        return false;
    }

    /**
     * 允许购买企业类型校验
     *
     * @param context
     * @return
     */
    public boolean permittedEnterpriseTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        Integer type = context.getEnterprise().getType();

        // 企业类型 1-全部，2-部分
        Integer permittedEnterpriseType = context.getSecKillSpecialDTO().getPermittedEnterpriseType();

        // 企业类型json
        String permittedEnterpriseDetail = context.getSecKillSpecialDTO().getPermittedEnterpriseDetail();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedEnterpriseType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedEnterpriseType) && StringUtils.isNotBlank(permittedEnterpriseDetail)) {
            List<Integer> integers = JSONObject.parseArray(permittedEnterpriseDetail, Integer.class);
            if (CollUtil.isNotEmpty(integers) && integers.contains(type)) {
                return true;
            }
        }
        log.info("[permittedEnterpriseTypeCheck]购买企业类型校验不通过，参数：{}", context);
        return false;
    }

}
