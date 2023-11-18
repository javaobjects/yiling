package com.yiling.user.agreementv2.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.bo.AgreementAllProductFormBO;
import com.yiling.user.agreementv2.bo.AgreementCategoryProductFormBO;
import com.yiling.user.agreementv2.bo.AgreementDetailCommonFormBO;
import com.yiling.user.agreementv2.bo.AgreementOtherRebateBO;
import com.yiling.user.agreementv2.bo.AgreementRebatePayEnterpriseBO;
import com.yiling.user.agreementv2.bo.AgreementRebateTermsBO;
import com.yiling.user.agreementv2.bo.AgreementRebateTimeSegmentBO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementOtherRebateDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateBasicServiceRewardDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateControlAreaDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateControlCustomerTypeDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateGoodsGroupDTO;
import com.yiling.user.agreementv2.dto.AgreementRebatePayEnterpriseDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateProjectServiceRewardDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateScaleRebateDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateScopeDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateStageDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateTaskStageDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateTimeSegmentDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementOtherRebateRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateControlAreaRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateGoodsGroupRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateScopeRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateStageRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTaskStageRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTimeSegmentRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsGroupRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsRequest;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;
import com.yiling.user.agreementv2.entity.AgreementOtherRebateDO;
import com.yiling.user.agreementv2.entity.AgreementRebateBasicServiceRewardDO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlCustomerTypeDO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlDO;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsDO;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsGroupDO;
import com.yiling.user.agreementv2.entity.AgreementRebatePayEnterpriseDO;
import com.yiling.user.agreementv2.entity.AgreementRebateProjectServiceRewardDO;
import com.yiling.user.agreementv2.entity.AgreementRebateScaleRebateDO;
import com.yiling.user.agreementv2.entity.AgreementRebateScopeDO;
import com.yiling.user.agreementv2.entity.AgreementRebateStageDO;
import com.yiling.user.agreementv2.entity.AgreementRebateTaskStageDO;
import com.yiling.user.agreementv2.entity.AgreementRebateTermsDO;
import com.yiling.user.agreementv2.dao.AgreementRebateTermsMapper;
import com.yiling.user.agreementv2.entity.AgreementRebateTimeSegmentDO;
import com.yiling.user.agreementv2.enums.AgreementControlSaleConditionEnum;
import com.yiling.user.agreementv2.enums.AgreementControlSaleTypeEnum;
import com.yiling.user.agreementv2.enums.AgreementFirstTypeEnum;
import com.yiling.user.agreementv2.enums.AgreementGoodsRebateRuleTypeEnum;
import com.yiling.user.agreementv2.enums.AgreementRebatePayEnum;
import com.yiling.user.agreementv2.enums.AgreementTimeSegmentTypeSetEnum;
import com.yiling.user.agreementv2.enums.AgreementTypeEnum;
import com.yiling.user.agreementv2.enums.ManufacturerTypeEnum;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerService;
import com.yiling.user.agreementv2.service.AgreementOtherRebateService;
import com.yiling.user.agreementv2.service.AgreementRebateBasicServiceRewardService;
import com.yiling.user.agreementv2.service.AgreementRebateControlAreaService;
import com.yiling.user.agreementv2.service.AgreementRebateControlCustomerTypeService;
import com.yiling.user.agreementv2.service.AgreementRebateControlService;
import com.yiling.user.agreementv2.service.AgreementRebateGoodsGroupService;
import com.yiling.user.agreementv2.service.AgreementRebateGoodsService;
import com.yiling.user.agreementv2.service.AgreementRebatePayEnterpriseService;
import com.yiling.user.agreementv2.service.AgreementRebateProjectServiceRewardService;
import com.yiling.user.agreementv2.service.AgreementRebateScaleRebateService;
import com.yiling.user.agreementv2.service.AgreementRebateScopeService;
import com.yiling.user.agreementv2.service.AgreementRebateStageService;
import com.yiling.user.agreementv2.service.AgreementRebateTaskStageService;
import com.yiling.user.agreementv2.service.AgreementRebateTermsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreementv2.service.AgreementRebateTimeSegmentService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议返利条款表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Slf4j
@Service
public class AgreementRebateTermsServiceImpl extends BaseServiceImpl<AgreementRebateTermsMapper, AgreementRebateTermsDO> implements AgreementRebateTermsService {

    /**
     * 指定商业公司最大数量
     */
    public static final Integer MAX_BUSINESS_ENTERPRISE_NUM = 5;
    /**
     * 非商品返利最多阶梯6个
     */
    public static final Integer OTHER_REBATE_STAGE_NUM = 6;
    /**
     * 每个时段内商品组最多6个
     */
    public static final Integer MAX_GOODS_GROUP_NUM = 6;
    /**
     * 每个商品组内返利范围最多6个
     */
    public static final Integer MAX_REBATE_SCOPE_NUM = 6;

    @Autowired
    private AgreementRebatePayEnterpriseService agreementRebatePayEnterpriseService;
    @Autowired
    private AgreementOtherRebateService agreementOtherRebateService;
    @Autowired
    private AgreementRebateTimeSegmentService agreementRebateTimeSegmentService;
    @Autowired
    private AgreementRebateTaskStageService agreementRebateTaskStageService;
    @Autowired
    private AgreementRebateStageService agreementRebateStageService;
    @Autowired
    private AgreementRebateGoodsGroupService agreementRebateGoodsGroupService;
    @Autowired
    private AgreementRebateGoodsService agreementRebateGoodsService;
    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementRebateScopeService agreementRebateScopeService;
    @Autowired
    private AgreementRebateControlService agreementRebateControlService;
    @Autowired
    private AgreementRebateControlAreaService agreementRebateControlAreaService;
    @Autowired
    private AgreementRebateControlCustomerTypeService agreementRebateControlCustomerTypeService;
    @Autowired
    private AgreementManufacturerGoodsService manufacturerGoodsService;
    @Autowired
    private AgreementManufacturerService manufacturerService;
    @Autowired
    private AgreementRebateScaleRebateService agreementRebateScaleRebateService;
    @Autowired
    private AgreementRebateBasicServiceRewardService agreementRebateBasicServiceRewardService;
    @Autowired
    private AgreementRebateProjectServiceRewardService agreementRebateProjectServiceRewardService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAgreementRebateTerms(AddAgreementRebateTermsRequest request) {
        Long agreementId = request.getAgreementId();
        // 如果是否底价供货为是，则返利条款直接全部清空，不可编辑
        if (request.getReserveSupplyFlag() == 1) {
            AgreementRebateTermsDO termsDO = new AgreementRebateTermsDO();
            termsDO.setAgreementId(agreementId);
            termsDO.setReserveSupplyFlag(1);
            termsDO.setOpUserId(request.getOpUserId());
            return this.save(termsDO);
        }

        // 返利条款条款
        AgreementRebateTermsDO rebateTermsDo = PojoUtils.map(request, AgreementRebateTermsDO.class);
        rebateTermsDo.setCashAllSegmentFlag(request.getCashAllSegmentFlag() ? 1 : 0);
        rebateTermsDo.setCashChildSegmentFlag(request.getCashChildSegmentFlag() ? 1 : 0);
        this.save(rebateTermsDo);

        // 协议返利支付方指定商业公司
        if (AgreementRebatePayEnum.BUSINESS == AgreementRebatePayEnum.getByCode(request.getRebatePay())) {
            List<AgreementRebatePayEnterpriseDO> rebatePayEnterpriseDOList = PojoUtils.map(request.getAgreementRebatePayEnterpriseList(), AgreementRebatePayEnterpriseDO.class);
            rebatePayEnterpriseDOList.forEach(agreementRebatePayEnterpriseDO -> {
                agreementRebatePayEnterpriseDO.setAgreementId(agreementId);
                agreementRebatePayEnterpriseDO.setOpUserId(request.getOpUserId());
            });
            agreementRebatePayEnterpriseService.saveBatch(rebatePayEnterpriseDOList);
        }

        // 非商品返利
        List<AddAgreementOtherRebateRequest> agreementOtherRebateList = request.getAgreementOtherRebateList();
        if (CollUtil.isNotEmpty(agreementOtherRebateList)) {
            List<AgreementOtherRebateDO> agreementOtherRebateDoList = agreementOtherRebateList.stream().map(addAgreementOtherRebateRequest -> {
                AgreementOtherRebateDO agreementOtherRebateDo = PojoUtils.map(addAgreementOtherRebateRequest, AgreementOtherRebateDO.class);
                agreementOtherRebateDo.setAgreementId(agreementId);
                agreementOtherRebateDo.setTaxFlag(addAgreementOtherRebateRequest.getTaxFlag() ? 1 : 0);
                agreementOtherRebateDo.setOpUserId(request.getOpUserId());
                return agreementOtherRebateDo;
            }).collect(Collectors.toList());
            this.agreementOtherRebateService.saveBatch(agreementOtherRebateDoList);
        }

        // 时段设置
        this.setTimeSegmentStage(request);

        return true;
    }

    @Override
    public AgreementRebateTermsDO getRebateTermsByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateTermsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateTermsDO::getAgreementId, agreementId);
        return this.getOne(wrapper);
    }

    @Override
    public AgreementRebateTermsBO getAgreementRebateTerms(Long agreementId) {
        long time = System.currentTimeMillis();
        // 返利条款主数据
        AgreementRebateTermsDO rebateTermsDO = Optional.ofNullable(this.getRebateTermsByAgreementId(agreementId)).orElseThrow(() -> new BusinessException(UserErrorCode.AGREEMENT_NOT_EXIST));

        AgreementRebateTermsBO rebateTermsBO = PojoUtils.map(rebateTermsDO, AgreementRebateTermsBO.class);
        rebateTermsBO.setCashAllSegmentFlag(rebateTermsDO.getCashAllSegmentFlag() == 1);
        rebateTermsBO.setCashChildSegmentFlag(rebateTermsDO.getCashChildSegmentFlag() == 1);
        if (rebateTermsDO.getReserveSupplyFlag() == 1) {
            return rebateTermsBO;
        }

        if (AgreementRebatePayEnum.BUSINESS == AgreementRebatePayEnum.getByCode(rebateTermsDO.getRebatePay())) {
            // 协议返利支付方指定商业公司
            List<AgreementRebatePayEnterpriseDTO> rebatePayEnterpriseList = agreementRebatePayEnterpriseService.getRebatePayEnterpriseList(agreementId);
            List<AgreementRebatePayEnterpriseBO> agreementRebatePayEnterpriseList = PojoUtils.map(rebatePayEnterpriseList, AgreementRebatePayEnterpriseBO.class);
            rebateTermsBO.setAgreementRebatePayEnterpriseList(agreementRebatePayEnterpriseList);
        }

        // 非商品返利
        List<AgreementOtherRebateDTO> otherRebateList = agreementOtherRebateService.getOtherRebateList(agreementId);
        List<AgreementOtherRebateBO> agreementOtherRebateList = otherRebateList.stream().map(agreementOtherRebateDTO -> {
            AgreementOtherRebateBO agreementOtherRebateBO = PojoUtils.map(agreementOtherRebateDTO, AgreementOtherRebateBO.class);
            agreementOtherRebateBO.setTaxFlag(agreementOtherRebateDTO.getTaxFlag() == 1);
            return agreementOtherRebateBO;
        }).collect(Collectors.toList());
        rebateTermsBO.setAgreementOtherRebateList(agreementOtherRebateList);

        // 开始获取和组装数据：时段、商品组、返利范围、返利任务量、返利阶梯
        List<AgreementRebateTimeSegmentBO> rebateTimeSegmentDetail = this.getRebateTimeSegmentDetail(agreementId, rebateTermsDO);
        rebateTermsBO.setAgreementRebateTimeSegmentList(rebateTimeSegmentDetail);
        log.debug("获取协议返利条款共耗时：{}", System.currentTimeMillis() - time);

        return rebateTermsBO;
    }

    /**
     * 获取和组装数据：时段、商品组、返利范围、返利任务量、返利阶梯
     *
     * @param agreementId
     * @param rebateTermsDO
     * @return
     */
    private List<AgreementRebateTimeSegmentBO> getRebateTimeSegmentDetail(Long agreementId, AgreementRebateTermsDO rebateTermsDO) {
        AgreementMainTermsDO mainTermsDO = agreementMainTermsService.getById(agreementId);
        // 返利时段
        List<AgreementRebateTimeSegmentDTO> rebateTimeSegmentDtoList = agreementRebateTimeSegmentService.getRebateTimeByAgreementId(agreementId);

        // 返利商品组map： key为 segmentId
        List<AgreementRebateGoodsGroupDTO> agreementRebateGoodsGroupDtoList = agreementRebateGoodsGroupService.getRebateGoodsGroupByAgreementId(agreementId);
        Map<Long, List<AgreementRebateGoodsGroupDTO>> goodsGroupDtoMap = agreementRebateGoodsGroupDtoList.stream().collect(Collectors.groupingBy(AgreementRebateGoodsGroupDTO::getSegmentId));

        // 返利范围map：key组合为 segmentId_goodsGroupId
        List<AgreementRebateScopeDTO> agreementRebateScopeDTOList = agreementRebateScopeService.getRebateScopeByAgreementId(agreementId);
        List<Long> rebateScopeIdList = agreementRebateScopeDTOList.stream().map(BaseDTO::getId).collect(Collectors.toList());
        Map<String, List<AgreementRebateScopeDTO>> rebateScopeMap = agreementRebateScopeDTOList.stream().collect(Collectors.groupingBy(scopeDTO -> scopeDTO.getSegmentId() + "_" + scopeDTO.getGroupId()));

        // 返利任务量map：key组合为 segmentId_goodsGroupId_rebateScopeId
        List<AgreementRebateTaskStageDTO> rebateTaskStageList = agreementRebateTaskStageService.getRebateTaskStageList(agreementId);
        Map<String, List<AgreementRebateTaskStageDTO>> rebateTaskStageMap = rebateTaskStageList.stream().collect(Collectors.groupingBy(
                taskStageDTO -> taskStageDTO.getSegmentId() + "_" + taskStageDTO.getGroupId() + "_" + taskStageDTO.getRebateScopeId()));

        // 返利阶梯map：key为 taskStageId
        List<Long> taskStageIdList = rebateTaskStageList.stream().map(BaseDTO::getId).collect(Collectors.toList());
        Map<Long, List<AgreementRebateStageDTO>> rebateStageMap = agreementRebateStageService.getStageMapByStageIdList(taskStageIdList);

        // 获取返利商品map：key为商品组ID
        List<AgreementRebateGoodsDTO> agreementRebateGoodsDtoList = agreementRebateGoodsService.getRebateGoodsByAgreementId(agreementId);
        Map<Long, List<AgreementRebateGoodsDTO>> rebateGoodsMap = agreementRebateGoodsDtoList.stream().collect(Collectors.groupingBy(AgreementRebateGoodsDTO::getGroupId));

        // 协议返利控销区域map：key为 rebateScopeId
        Map<Long, AgreementRebateControlAreaDTO> rebateControlAreaMap = agreementRebateControlAreaService.getRebateControlAreaMap(rebateScopeIdList);
        // 协议返利控销客户类型map：key为 rebateScopeId
        Map<Long, List<AgreementRebateControlCustomerTypeDTO>> controlCustomerTypeMap = agreementRebateControlCustomerTypeService.getControlCustomerTypeMap(rebateScopeIdList);

        // 开始设置：时段下所有维度数据
        List<AgreementRebateTimeSegmentBO> rebateTimeSegmentBOList = ListUtil.toList();
        rebateTimeSegmentDtoList.forEach(rebateTimeSegmentDto -> {

            AgreementRebateTimeSegmentBO agreementRebateTimeSegmentBo = PojoUtils.map(rebateTimeSegmentDto, AgreementRebateTimeSegmentBO.class);
            agreementRebateTimeSegmentBo.setScaleRebateFlag(rebateTimeSegmentDto.getScaleRebateFlag() == 1);
            agreementRebateTimeSegmentBo.setBasicServiceRewardFlag(rebateTimeSegmentDto.getBasicServiceRewardFlag() == 1);
            agreementRebateTimeSegmentBo.setProjectServiceRewardFlag(rebateTimeSegmentDto.getProjectServiceRewardFlag() == 1);

            Long segmentId = rebateTimeSegmentDto.getId();

            // 商品组维度
            List<AgreementRebateGoodsGroupDTO> rebateGoodsGroupDtoList = goodsGroupDtoMap.getOrDefault(segmentId, ListUtil.toList());
            if (rebateGoodsGroupDtoList.size() == 0 && AgreementGoodsRebateRuleTypeEnum.getByCode(rebateTermsDO.getGoodsRebateRuleType()) == AgreementGoodsRebateRuleTypeEnum.ALL) {
                // 如果为全品设置是没有商品组的，此时虚拟一层包装
                AgreementRebateGoodsGroupDTO rebateGoodsGroupDTO = new AgreementRebateGoodsGroupDTO();
                rebateGoodsGroupDTO.setAgreementId(agreementId);
                rebateGoodsGroupDTO.setSegmentId(segmentId);
                rebateGoodsGroupDTO.setId(0L);
                rebateGoodsGroupDtoList.add(rebateGoodsGroupDTO);
            }

            // 全品表单数据
            List<AgreementAllProductFormBO> allProductFormBOList = ListUtil.toList();
            // 分类表单数据
            List<List<AgreementCategoryProductFormBO>> allCategoryProductFormBOList = ListUtil.toList();

            // 分类设置场景：如果有6个商品组，那么1个为全商品组，5个为子商品组
            rebateGoodsGroupDtoList.forEach(rebateGoodsGroupDTO -> {

                // 如果是全品分类 or 分类设置的全商品组 才进行设置全品表单
                if (Objects.isNull(rebateGoodsGroupDTO.getGroupType()) || rebateGoodsGroupDTO.getGroupType() == 1) {
                    Long groupId = rebateGoodsGroupDTO.getId();
                    List<AgreementRebateScopeDTO> rebateScopeDtoList = rebateScopeMap.getOrDefault(segmentId + "_" + groupId, ListUtil.toList());
                    rebateScopeDtoList.forEach(rebateScopeDTO -> {
                        Long rebateScopeId = rebateScopeDTO.getId();
                        // 设置全品表单
                        AgreementDetailCommonFormBO commonFormBO = this.setRebateDetails(rebateTermsDO, rebateTaskStageMap, rebateStageMap, rebateControlAreaMap,
                                controlCustomerTypeMap, segmentId, groupId, rebateScopeDTO, rebateScopeId);
                        AgreementAllProductFormBO allProductFormBO = PojoUtils.map(commonFormBO, AgreementAllProductFormBO.class);
                        allProductFormBO.setEname(mainTermsDO.getEname());

                        allProductFormBOList.add(allProductFormBO);
                    });
                }

                // 如果是 分类设置的子商品组，才进行设置 分类表单
                if (Objects.nonNull(rebateGoodsGroupDTO.getGroupType()) && rebateGoodsGroupDTO.getGroupType() == 2) {
                    Long groupId = rebateGoodsGroupDTO.getId();
                    List<AgreementRebateScopeDTO> rebateScopeDtoList = rebateScopeMap.getOrDefault(segmentId + "_" + groupId, ListUtil.toList());
                    rebateScopeDtoList.forEach(rebateScopeDTO -> {
                        Long rebateScopeId = rebateScopeDTO.getId();

                        // 设置分类表单
                        List<AgreementRebateGoodsDTO> rebateGoodsDTOList = rebateGoodsMap.get(groupId);
                        List<AgreementCategoryProductFormBO> categoryProductFormBOList = PojoUtils.map(rebateGoodsDTOList, AgreementCategoryProductFormBO.class);
                        this.setManufacturerName(mainTermsDO, categoryProductFormBOList);
                        categoryProductFormBOList.forEach(categoryProductFormBO -> {
                            AgreementDetailCommonFormBO commonFormBO = this.setRebateDetails(rebateTermsDO, rebateTaskStageMap, rebateStageMap, rebateControlAreaMap,
                                    controlCustomerTypeMap, segmentId, groupId, rebateScopeDTO, rebateScopeId);
                            PojoUtils.map(commonFormBO, categoryProductFormBO);
                        });

                        allCategoryProductFormBOList.add(categoryProductFormBOList);

                    });
                }

            });

            agreementRebateTimeSegmentBo.setAllProductFormList(allProductFormBOList);
            agreementRebateTimeSegmentBo.setCategoryProductFormList(allCategoryProductFormBOList);
            rebateTimeSegmentBOList.add(agreementRebateTimeSegmentBo);
        });

        return rebateTimeSegmentBOList;
    }

    /**
     * 为分类表单设置厂家和品牌厂家
     *
     * @param categoryProductFormBOList
     */
    private void setManufacturerName(AgreementMainTermsDO mainTermsDO, List<AgreementCategoryProductFormBO> categoryProductFormBOList) {
        if (AgreementFirstTypeEnum.getByCode(mainTermsDO.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER
                || AgreementFirstTypeEnum.getByCode(mainTermsDO.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_BRAND) {
            List<Long> specificationGoodsIds = categoryProductFormBOList.stream().map(AgreementCategoryProductFormBO::getSpecificationGoodsId).collect(Collectors.toList());
            Map<Long, List<AgreementManufacturerGoodsDTO>> goodsSpecificationMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(specificationGoodsIds)) {
                goodsSpecificationMap = manufacturerGoodsService.getGoodsBySpecificationId(specificationGoodsIds);
            }

            // 获取厂家集合
            Set<Long> manufacturerIdSet = new HashSet<>();
            goodsSpecificationMap.forEach((specificationId, goodsList) -> goodsList.forEach(
                    agreementManufacturerGoodsDTO -> manufacturerIdSet.add(agreementManufacturerGoodsDTO.getManufacturerId())));
            Map<Long, AgreementManufacturerDO> manufacturerDoMap = MapUtil.newHashMap();
            if (CollUtil.isNotEmpty(manufacturerIdSet)) {
                manufacturerDoMap = manufacturerService.listByIds(ListUtil.toList(manufacturerIdSet)).stream().collect(Collectors.toMap(AgreementManufacturerDO::getId, Function.identity()));
            }

            for (AgreementCategoryProductFormBO agreementCategoryProductFormBO : categoryProductFormBOList) {
                List<AgreementManufacturerGoodsDTO> manufacturerGoodsDTOList = goodsSpecificationMap.get(agreementCategoryProductFormBO.getSpecificationGoodsId());
                if (CollUtil.isNotEmpty(manufacturerGoodsDTOList)) {
                    for (AgreementManufacturerGoodsDTO manufacturerGoodsDTO : manufacturerGoodsDTOList) {
                        AgreementManufacturerDO manufacturerDO = manufacturerDoMap.get(manufacturerGoodsDTO.getManufacturerId());
                        if (Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.PRODUCER == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                            agreementCategoryProductFormBO.setProducerManufacturer(manufacturerDO.getEname());
                        } else if(Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.BRAND == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                            agreementCategoryProductFormBO.setBrandManufacturer(manufacturerDO.getEname());
                        }
                    }
                }
            }

        }
    }

    /**
     * 协议返利数据组装成符合详情页的数据格式
     *
     * @param rebateTermsDO
     * @param rebateTaskStageMap
     * @param rebateStageMap
     * @param rebateControlAreaMap
     * @param controlCustomerTypeMap
     * @param segmentId
     * @param groupId
     * @param rebateScopeDTO
     * @param rebateScopeId
     * @return
     */
    private AgreementDetailCommonFormBO setRebateDetails(AgreementRebateTermsDO rebateTermsDO, Map<String, List<AgreementRebateTaskStageDTO>> rebateTaskStageMap, Map<Long, List<AgreementRebateStageDTO>> rebateStageMap, Map<Long, AgreementRebateControlAreaDTO> rebateControlAreaMap, Map<Long, List<AgreementRebateControlCustomerTypeDTO>> controlCustomerTypeMap, Long segmentId, Long groupId, AgreementRebateScopeDTO rebateScopeDTO, Long rebateScopeId) {
        AgreementDetailCommonFormBO commonFormBO = new AgreementDetailCommonFormBO();
        // 获取返利规则，多个分号分割
        StringBuffer rebateRuleBuffer = new StringBuffer();
        List<AgreementRebateTaskStageDTO> rebateTaskStageDtoList = rebateTaskStageMap.getOrDefault(segmentId + "_" + groupId + "_" + rebateScopeId, ListUtil.toList());
        rebateTaskStageDtoList.forEach(taskStageDTO -> {
            Long taskStageId = taskStageDTO.getId();
            List<AgreementRebateStageDTO> rebateStageDTOList = rebateStageMap.getOrDefault(taskStageId, ListUtil.toList());
            rebateStageDTOList.forEach(agreementRebateStageDTO -> {
                if (agreementRebateStageDTO.getFull().compareTo(BigDecimal.ZERO) != 0 && agreementRebateStageDTO.getBack().compareTo(BigDecimal.ZERO) != 0) {
                    rebateRuleBuffer.append("满").append(agreementRebateStageDTO.getFull()).append("返").append(agreementRebateStageDTO.getBack()).append(";");
                }
            });

        });
        commonFormBO.setRebateRule(rebateRuleBuffer.toString());

        // 任务量标准
        commonFormBO.setTaskStandard(rebateTermsDO.getTaskStandard());
        // 任务量，多个分号分割
        StringBuffer rebateTaskNumBuffer = new StringBuffer();
        StringBuffer rebateOverSumBackBuffer = new StringBuffer();
        rebateTaskStageDtoList.forEach(taskStageDTO -> {
            if (taskStageDTO.getTaskNum().compareTo(BigDecimal.ZERO) != 0) {
                rebateTaskNumBuffer.append(taskStageDTO.getTaskNum()).append(taskStageDTO.getTaskUnit() == 1 ? ("元" + ";") : ("盒" + ";"));
            }
            if (taskStageDTO.getOverSumBack().compareTo(BigDecimal.ZERO) != 0) {
                rebateOverSumBackBuffer.append(taskStageDTO.getOverSumBack()).append(taskStageDTO.getOverSumBackUnit() == 1 ? ("元" + ";") : ("%" + ";"));
            }
        });
        commonFormBO.setTaskNum(rebateTaskNumBuffer.toString());
        // 超任务量汇总返，多个分号分割
        commonFormBO.setOverTaskNum(rebateOverSumBackBuffer.toString());
        // 返利阶梯条件计算方法、返利计算规则
        commonFormBO.setRebateStageMethod(rebateTermsDO.getRebateStageMethod());
        commonFormBO.setRebateCalculateRule(rebateTermsDO.getRebateCalculateRule());
        commonFormBO.setRebateRuleType(rebateTermsDO.getRebateRuleType());

        // 控销类型
        commonFormBO.setControlSaleType(rebateScopeDTO.getControlSaleType());
        if (AgreementControlSaleTypeEnum.getByCode(rebateScopeDTO.getControlSaleType()) != AgreementControlSaleTypeEnum.NONE) {
            // 控销区域描述
            AgreementRebateControlAreaDTO controlAreaDTO = rebateControlAreaMap.getOrDefault(rebateScopeId, new AgreementRebateControlAreaDTO());
            commonFormBO.setDescription(controlAreaDTO.getDescription());
            commonFormBO.setJsonContent(controlAreaDTO.getJsonContent());
            // 客户类型，多个分号分割
            StringBuffer customerTypeBuffer = new StringBuffer();
            List<AgreementRebateControlCustomerTypeDTO> customerTypeDTOList = controlCustomerTypeMap.getOrDefault(rebateScopeId, ListUtil.toList());
            customerTypeDTOList.forEach(customerTypeDTO -> customerTypeBuffer.append(EnterpriseTypeEnum.getByCode(customerTypeDTO.getCustomerType()).getName()).append(";"));
            commonFormBO.setCustomerType(customerTypeBuffer.toString());
        }
        return commonFormBO;
    }

    /**
     * 时段设置
     *
     * @param request 请求参数
     */
    private void setTimeSegmentStage(AddAgreementRebateTermsRequest request) {
        Long agreementId = request.getAgreementId();

        List<AddAgreementRebateTimeSegmentRequest> rebateTimeSegmentList = request.getAgreementRebateTimeSegmentList();
        // 时段维度：全时段统一配置时，相当于一个时段
        rebateTimeSegmentList.forEach(agreementRebateTimeSegmentRequest -> {

            // 协议时段入库
            AgreementRebateTimeSegmentDO agreementRebateTimeSegmentDO = PojoUtils.map(agreementRebateTimeSegmentRequest, AgreementRebateTimeSegmentDO.class);
            agreementRebateTimeSegmentDO.setAgreementId(agreementId);
            agreementRebateTimeSegmentDO.setOpUserId(request.getOpUserId());
            this.agreementRebateTimeSegmentService.save(agreementRebateTimeSegmentDO);
            Long segmentId = agreementRebateTimeSegmentDO.getId();

            // 设置规模返利、基础服务奖励、项目服务奖励
            setScaleAndReward(request.getOpUserId(), agreementId, agreementRebateTimeSegmentRequest, agreementRebateTimeSegmentDO, segmentId);

            // 商品组维度设置
            setAgreementGoodsGroup(request, agreementId, agreementRebateTimeSegmentRequest, segmentId);

        });

    }

    /**
     * 设置规模返利、基础服务奖励、项目服务奖励
     *
     * @param opUserId 操作人
     * @param agreementId 协议ID
     * @param agreementRebateTimeSegmentRequest 时段请求对象
     * @param agreementRebateTimeSegmentDO 时段DO对象
     * @param segmentId 时段ID
     */
    private void setScaleAndReward(Long opUserId, Long agreementId, AddAgreementRebateTimeSegmentRequest agreementRebateTimeSegmentRequest,
                                   AgreementRebateTimeSegmentDO agreementRebateTimeSegmentDO, Long segmentId) {
        // 设置规模返利
        Boolean scaleRebateFlag = agreementRebateTimeSegmentRequest.getScaleRebateFlag();
        if (scaleRebateFlag != null && scaleRebateFlag) {
            agreementRebateTimeSegmentDO.setScaleRebateFlag(1);

            List<AgreementRebateScaleRebateDO> scaleRebateDOList = PojoUtils.map(agreementRebateTimeSegmentRequest.getAgreementScaleRebateList(), AgreementRebateScaleRebateDO.class);
            scaleRebateDOList.forEach(scaleRebateDO -> {
                scaleRebateDO.setAgreementId(agreementId);
                scaleRebateDO.setSegmentId(segmentId);
                scaleRebateDO.setOpUserId(opUserId);
            });
            agreementRebateScaleRebateService.saveBatch(scaleRebateDOList);
        }

        // 设置基础服务奖励
        Boolean basicServiceRewardFlag = agreementRebateTimeSegmentRequest.getBasicServiceRewardFlag();
        if (basicServiceRewardFlag != null && basicServiceRewardFlag) {
            agreementRebateTimeSegmentDO.setBasicServiceRewardFlag(1);

            List<AgreementRebateBasicServiceRewardDO> basicServiceRewardDOList = PojoUtils.map(agreementRebateTimeSegmentRequest.getAgreementRebateBasicServiceRewardList(), AgreementRebateBasicServiceRewardDO.class);
            basicServiceRewardDOList.forEach(basicServiceRewardDO -> {
                basicServiceRewardDO.setAgreementId(agreementId);
                basicServiceRewardDO.setSegmentId(segmentId);
                basicServiceRewardDO.setOpUserId(opUserId);
            });
            agreementRebateBasicServiceRewardService.saveBatch(basicServiceRewardDOList);
        }

        // 设置
        Boolean projectServiceRewardFlag = agreementRebateTimeSegmentRequest.getProjectServiceRewardFlag();
        if (projectServiceRewardFlag != null && projectServiceRewardFlag) {
            agreementRebateTimeSegmentDO.setProjectServiceRewardFlag(1);

            List<AgreementRebateProjectServiceRewardDO> projectServiceRewardDOList = PojoUtils.map(agreementRebateTimeSegmentRequest.getAgreementRebateProjectServiceRewardList(), AgreementRebateProjectServiceRewardDO.class);
            projectServiceRewardDOList.forEach(projectServiceRewardDO -> {
                projectServiceRewardDO.setAgreementId(agreementId);
                projectServiceRewardDO.setSegmentId(segmentId);
                projectServiceRewardDO.setOpUserId(opUserId);
            });
            agreementRebateProjectServiceRewardService.saveBatch(projectServiceRewardDOList);
        }
    }

    /**
     * 商品组维度设置
     *
     * @param request 请求参数
     * @param agreementId 协议ID
     * @param agreementRebateTimeSegmentRequest 时段请求对象
     * @param segmentId 时段ID
     */
    private void setAgreementGoodsGroup(AddAgreementRebateTermsRequest request, Long agreementId, AddAgreementRebateTimeSegmentRequest agreementRebateTimeSegmentRequest, Long segmentId) {

        // 商品组维度：分类设置时：全商品主任务当做一个商品组处理，和商品组1、商品组2平级 ；全品设置时：相当于只有一个商品组
        if (AgreementGoodsRebateRuleTypeEnum.ALL == AgreementGoodsRebateRuleTypeEnum.getByCode(request.getGoodsRebateRuleType())) {
            AddAgreementRebateGoodsGroupRequest agreementRebateGoodsGroupRequest = agreementRebateTimeSegmentRequest.getAgreementRebateGoodsGroupList().get(0);

            // 返利范围维度
            setAgreementRebateScope(request.getOpUserId(), agreementId, segmentId, agreementRebateGoodsGroupRequest.getAgreementRebateScopeList(), 0L);

        } else if (AgreementGoodsRebateRuleTypeEnum.CATEGORY == AgreementGoodsRebateRuleTypeEnum.getByCode(request.getGoodsRebateRuleType())) {
            // 协议返利商品组维度
            List<AddAgreementRebateGoodsGroupRequest> agreementRebateGoodsGroupList = agreementRebateTimeSegmentRequest.getAgreementRebateGoodsGroupList();

            agreementRebateGoodsGroupList.forEach(agreementRebateGoodsGroupRequest -> {
                // 商品组入库
                AgreementRebateGoodsGroupDO rebateGoodsGroupDO = PojoUtils.map(agreementRebateGoodsGroupRequest, AgreementRebateGoodsGroupDO.class);
                rebateGoodsGroupDO.setAgreementId(agreementId);
                rebateGoodsGroupDO.setSegmentId(segmentId);
                rebateGoodsGroupDO.setOpUserId(request.getOpUserId());
                this.agreementRebateGoodsGroupService.save(rebateGoodsGroupDO);
                Long goodsGroupId = rebateGoodsGroupDO.getId();

                // 协议返利商品组对应的商品集合
                List<AddAgreementRebateGoodsRequest> agreementRebateGoodsList = agreementRebateGoodsGroupRequest.getAgreementRebateGoodsList();
                List<AgreementRebateGoodsDO> rebateGoodsDOList = PojoUtils.map(agreementRebateGoodsList, AgreementRebateGoodsDO.class);
                rebateGoodsDOList.forEach(rebateGoodsDO -> {
                    rebateGoodsDO.setAgreementId(agreementId);
                    rebateGoodsDO.setGroupId(goodsGroupId);
                    rebateGoodsDO.setOpUserId(request.getOpUserId());
                });
                this.agreementRebateGoodsService.saveBatch(rebateGoodsDOList);

                // 返利范围维度
                setAgreementRebateScope(request.getOpUserId(), agreementId, segmentId, agreementRebateGoodsGroupRequest.getAgreementRebateScopeList(), goodsGroupId);

            });

        }
    }

    /**
     * 设置协议返利范围维度
     *
     * @param opUserId 操作人
     * @param agreementId 协议ID
     * @param segmentId 时段ID
     * @param agreementRebateScopeList 返利范围集合
     * @param goodsGroupId 商品组ID
     */
    private void setAgreementRebateScope(Long opUserId, Long agreementId, Long segmentId,
                                         List<AddAgreementRebateScopeRequest> agreementRebateScopeList, Long goodsGroupId) {
        // 返利范围维度
        agreementRebateScopeList.forEach(addAgreementRebateScopeRequest -> {
            // 插入返利范围入库
            AgreementRebateScopeDO agreementRebateScopeDO = PojoUtils.map(addAgreementRebateScopeRequest, AgreementRebateScopeDO.class);
            agreementRebateScopeDO.setAgreementId(agreementId);
            agreementRebateScopeDO.setSegmentId(segmentId);
            agreementRebateScopeDO.setOpUserId(opUserId);
            agreementRebateScopeDO.setGroupId(goodsGroupId);
            this.agreementRebateScopeService.save(agreementRebateScopeDO);
            Long rebateScopeId = agreementRebateScopeDO.getId();

            // 返利范围所对应的控销条件设置（如果有：区域和客户类型）
            this.setRebateScopeControl(addAgreementRebateScopeRequest, rebateScopeId, opUserId);

            // 每个返利范围对应的协议返利任务量阶梯维度
            this.setAgreementTaskStage(opUserId, agreementId, segmentId, rebateScopeId, addAgreementRebateScopeRequest.getAgreementRebateTaskStageList(), goodsGroupId);

        });
    }

    /**
     * 设置协议返利范围内的 控销条件、区域、客户类型
     * @param agreementRebateScopeRequest 返利范围请求参数
     * @param rebateScopeId 返利范围ID
     * @param opUserId 操作人ID
     */
    private void setRebateScopeControl(AddAgreementRebateScopeRequest agreementRebateScopeRequest, Long rebateScopeId, Long opUserId) {
        if (AgreementControlSaleTypeEnum.NONE != AgreementControlSaleTypeEnum.getByCode(agreementRebateScopeRequest.getControlSaleType())) {
            // 控销条件
            List<Integer> rebateControlList = agreementRebateScopeRequest.getAgreementRebateControlList();
            if (CollUtil.isNotEmpty(rebateControlList)) {
                List<AgreementRebateControlDO> rebateControlDOList = ListUtil.toList();
                rebateControlList.forEach(controlSaleCondition -> {
                    AgreementRebateControlDO agreementRebateControlDO = new AgreementRebateControlDO();
                    agreementRebateControlDO.setControlSaleCondition(controlSaleCondition);
                    agreementRebateControlDO.setRebateScopeId(rebateScopeId);
                    agreementRebateControlDO.setOpUserId(opUserId);
                    rebateControlDOList.add(agreementRebateControlDO);
                });
                this.agreementRebateControlService.saveBatch(rebateControlDOList);

                // 控销区域
                AddAgreementRebateControlAreaRequest rebateControlArea = agreementRebateScopeRequest.getAddAgreementRebateControlArea();
                if (Objects.nonNull(rebateControlArea) && StrUtil.isNotEmpty(rebateControlArea.getJsonContent())) {
                    rebateControlArea.setOpUserId(opUserId);
                    rebateControlArea.setRebateScopeId(rebateScopeId);
                    this.agreementRebateControlAreaService.saveArea(rebateControlArea);
                }

                // 控销客户类型
                List<Integer> rebateControlCustomerTypeRequestList = agreementRebateScopeRequest.getAgreementRebateControlCustomerType();
                if (CollUtil.isNotEmpty(rebateControlCustomerTypeRequestList)) {
                    List<AgreementRebateControlCustomerTypeDO> rebateControlCustomerTypeDOList = ListUtil.toList();
                    rebateControlCustomerTypeRequestList.forEach(customerType -> {
                        AgreementRebateControlCustomerTypeDO agreementRebateControlCustomerTypeDO = new AgreementRebateControlCustomerTypeDO();
                        agreementRebateControlCustomerTypeDO.setRebateScopeId(rebateScopeId);
                        agreementRebateControlCustomerTypeDO.setCustomerType(customerType);
                        agreementRebateControlCustomerTypeDO.setOpUserId(opUserId);
                        rebateControlCustomerTypeDOList.add(agreementRebateControlCustomerTypeDO);
                    });
                    this.agreementRebateControlCustomerTypeService.saveBatch(rebateControlCustomerTypeDOList);
                }

            }

        }
    }

    /**
     * 保存协议返利任务量阶梯和返利阶梯
     *
     * @param opUserId 操作人ID
     * @param agreementId 协议ID
     * @param segmentId 时段ID
     * @param rebateScopeId 返利范围ID
     * @param agreementRebateTaskStageList 协议返利任务量阶梯
     * @param goodsGroupId 商品组ID
     */
    private void setAgreementTaskStage(Long opUserId, Long agreementId, Long segmentId, Long rebateScopeId,
                                       List<AddAgreementRebateTaskStageRequest> agreementRebateTaskStageList , Long goodsGroupId) {
        // 协议返利任务量阶梯
        agreementRebateTaskStageList.forEach(agreementRebateTaskStageRequest -> {
            // 任务量阶梯入库
            AgreementRebateTaskStageDO rebateTaskStageDO = PojoUtils.map(agreementRebateTaskStageRequest, AgreementRebateTaskStageDO.class);
            rebateTaskStageDO.setAgreementId(agreementId);
            rebateTaskStageDO.setSegmentId(segmentId);
            rebateTaskStageDO.setRebateScopeId(rebateScopeId);
            rebateTaskStageDO.setGroupId(goodsGroupId);
            rebateTaskStageDO.setOpUserId(opUserId);
            this.agreementRebateTaskStageService.save(rebateTaskStageDO);

            // 协议返利阶梯维度
            List<AddAgreementRebateStageRequest> agreementRebateStageList = agreementRebateTaskStageRequest.getAgreementRebateStageList();

            List<AgreementRebateStageDO> rebateStageDOList = PojoUtils.map(agreementRebateStageList, AgreementRebateStageDO.class);
            rebateStageDOList.forEach(agreementRebateStageDO -> {
                agreementRebateStageDO.setTaskStageId(rebateTaskStageDO.getId());
                agreementRebateStageDO.setOpUserId(opUserId);
            });
            this.agreementRebateStageService.saveBatch(rebateStageDOList);

        });
    }

    /**
     * 校验返利条款参数
     *
     * @param agreementRequest 请求参数
     */
    @Override
    public void checkRebateTerms(CreateAgreementRequest agreementRequest) {
        AddAgreementRebateTermsRequest request = agreementRequest.getAgreementRebateTerms();
        if (request.getReserveSupplyFlag() == 1) {
            return;
        }
        // 校验每个时段内所有商品组的商品，必须在协议供销条款的商品内
        if (AgreementGoodsRebateRuleTypeEnum.getByCode(request.getGoodsRebateRuleType()) == AgreementGoodsRebateRuleTypeEnum.CATEGORY) {
            List<AddAgreementRebateGoodsRequest> allSegmentGoodsRequestList = ListUtil.toList();
            List<AddAgreementRebateTimeSegmentRequest> rebateTimeSegmentList = request.getAgreementRebateTimeSegmentList();
            rebateTimeSegmentList.forEach(rebateTimeSegmentRequest -> {
                List<AddAgreementRebateGoodsRequest> currentSegmentGoodsRequestList = ListUtil.toList();
                List<AddAgreementRebateGoodsGroupRequest> rebateGoodsGroupList = rebateTimeSegmentRequest.getAgreementRebateGoodsGroupList();
                rebateGoodsGroupList.forEach(rebateGoodsGroupRequest -> {
                    currentSegmentGoodsRequestList.addAll(rebateGoodsGroupRequest.getAgreementRebateGoodsList());
                    allSegmentGoodsRequestList.addAll(rebateGoodsGroupRequest.getAgreementRebateGoodsList());
                });

                List<Long> specificationGoodsIdList = currentSegmentGoodsRequestList.stream().map(AddAgreementRebateGoodsRequest::getSpecificationGoodsId).distinct().collect(Collectors.toList());
                if (specificationGoodsIdList.size() != currentSegmentGoodsRequestList.size()) {
                    throw new BusinessException(UserErrorCode.AGREEMENT_REBATE_GOODS_EXIST);
                }

            });

            // 获取供销条款的所有商品
            List<Long> allSpecificationGoodsId = allSegmentGoodsRequestList.stream().map(AddAgreementRebateGoodsRequest::getSpecificationGoodsId).distinct().collect(Collectors.toList());
            Map<Long, AddAgreementRebateGoodsRequest> rebateGoodsMap = allSegmentGoodsRequestList.stream().collect(Collectors.toMap(AddAgreementRebateGoodsRequest::getSpecificationGoodsId, Function.identity(), (k1,k2) -> k2));

            if (agreementRequest.getAgreementSupplySalesTerms().getAllLevelKindsFlag() == 0) {
                List<Long> allSupplyGoodsId = ListUtil.toList();
                List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList = agreementRequest.getAgreementSupplySalesTerms().getSupplySalesGoodsGroupList();
                supplySalesGoodsGroupList.forEach(supplySalesGoodsGroupRequest -> {
                    List<Long> ids = supplySalesGoodsGroupRequest.getSupplySalesGoodsList().stream().map(AddAgreementSupplySalesGoodsRequest::getSpecificationGoodsId).collect(Collectors.toList());
                    allSupplyGoodsId.addAll(ids);
                });

                // 校验协议返利商品是否在供销条款商品内
                allSpecificationGoodsId.forEach(specificationId -> {
                    if (!allSupplyGoodsId.contains(specificationId)) {
                        String message = StrFormatter.format(UserErrorCode.AGREEMENT_REBATE_GOODS_NOT_IN_SUPPLY_SALES.getMessage(),
                                rebateGoodsMap.getOrDefault(specificationId, new AddAgreementRebateGoodsRequest()).getGoodsName());
                        throw new BusinessException(UserErrorCode.AGREEMENT_REBATE_GOODS_NOT_IN_SUPPLY_SALES, message);
                    }
                });
            } else {
                AddAgreementMainTermsRequest mainTerms = agreementRequest.getAgreementMainTerms();
                if (AgreementFirstTypeEnum.getByCode(mainTerms.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER
                        || AgreementFirstTypeEnum.getByCode(mainTerms.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_BRAND) {
                    List<Long> manufactureGoodIds = manufacturerGoodsService.getManufactureGoodsListByEid(mainTerms.getEid()).stream().map(AgreementManufacturerGoodsDTO::getSpecificationGoodsId).distinct().collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(manufactureGoodIds)) {
                        // 校验协议返利商品是否在厂家商品内
                        allSpecificationGoodsId.forEach(specificationId -> {
                            if (!manufactureGoodIds.contains(specificationId)) {
                                String message = StrFormatter.format(UserErrorCode.AGREEMENT_REBATE_GOODS_NOT_IN_SUPPLY_SALES.getMessage(),
                                        rebateGoodsMap.getOrDefault(specificationId, new AddAgreementRebateGoodsRequest()).getGoodsName());
                                throw new BusinessException(UserErrorCode.AGREEMENT_REBATE_GOODS_NOT_IN_SUPPLY_SALES, message);
                            }
                        });
                    }
                }
            }

        }

        // 当“是否底价供货”的值为否时，“返利支付方”、“返利兑付方式”、“返利兑付时间”、“隔XX月兑付”为必选项
        if (Objects.isNull(request.getRebatePay()) || Objects.isNull(request.getRebateCashType()) || Objects.isNull(request.getRebateCashTime())
                || Objects.isNull(request.getRebateCashSegment()) || Objects.isNull(request.getRebateCashUnit())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        // 指定商业公司和数量校验
        if (AgreementRebatePayEnum.BUSINESS == AgreementRebatePayEnum.getByCode(request.getRebatePay())) {
            if (CollUtil.isEmpty(request.getAgreementRebatePayEnterpriseList())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
            if (request.getAgreementRebatePayEnterpriseList().size() > MAX_BUSINESS_ENTERPRISE_NUM) {
                throw new BusinessException(UserErrorCode.MORE_THAN_SET_BUSINESS_ENTERPRISE);
            }
        }

        // 非返利商品最多6个阶梯
        if (CollUtil.isNotEmpty(request.getAgreementOtherRebateList())) {
            if (request.getAgreementOtherRebateList().size() > OTHER_REBATE_STAGE_NUM) {
                throw new BusinessException(UserErrorCode.MORE_THAN_SET_OTHER_REBATE);
            }
        }

        // 是否有任务量
        if (request.getTaskFlag() == 1) {
            if (Objects.isNull(request.getTaskStandard())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        }

        // 时段和阶梯校验
        this.checkTimeSegmentStage(request, agreementRequest.getAgreementMainTerms().getAgreementType());

    }

    /**
     * 时段和阶梯校验
     *
     * @param request 请求参数
     */
    private void checkTimeSegmentStage(AddAgreementRebateTermsRequest request, Integer agreementType) {
        List<AddAgreementRebateTimeSegmentRequest> rebateTimeSegmentList = request.getAgreementRebateTimeSegmentList();
        if (CollUtil.isEmpty(rebateTimeSegmentList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        rebateTimeSegmentList.forEach(agreementRebateTimeSegmentRequest -> {

            // 时段维度：所有商品集合
            List<AddAgreementRebateGoodsRequest> agreementRebateGoodsRequestList = ListUtil.toList();

            List<AddAgreementRebateGoodsGroupRequest> rebateGoodsGroupList = agreementRebateTimeSegmentRequest.getAgreementRebateGoodsGroupList();
            // 每个时段内，商品组最多设置6个
            if (CollUtil.isEmpty(rebateGoodsGroupList)) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
            if (rebateGoodsGroupList.size() > MAX_GOODS_GROUP_NUM) {
                throw new BusinessException(UserErrorCode.MORE_THAN_SET_PRODUCT_GROUP);
            }

            // 全品设置，只能有一个虚拟商品组
            if (AgreementGoodsRebateRuleTypeEnum.ALL == AgreementGoodsRebateRuleTypeEnum.getByCode(request.getGoodsRebateRuleType())) {
                if (CollUtil.isEmpty(rebateGoodsGroupList) || rebateGoodsGroupList.size() > 1) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            } else {
                // 分类设置下，如果当前协议为ka协议，则校验规模返利、基础服务奖励、项目服务奖励
                if (AgreementTypeEnum.KA_AGREEMENT == AgreementTypeEnum.getByCode(agreementType)) {
                    // 校验规模返利阶梯集合
                    if (Objects.nonNull(agreementRebateTimeSegmentRequest.getScaleRebateFlag()) && agreementRebateTimeSegmentRequest.getScaleRebateFlag()) {
                        if (CollUtil.isEmpty(agreementRebateTimeSegmentRequest.getAgreementScaleRebateList())) {
                            throw new BusinessException(ResultCode.PARAM_MISS);
                        }
                    }
                    // 校验基础服务奖励阶梯集合
                    if (Objects.nonNull(agreementRebateTimeSegmentRequest.getBasicServiceRewardFlag()) && agreementRebateTimeSegmentRequest.getBasicServiceRewardFlag()) {
                        if (CollUtil.isEmpty(agreementRebateTimeSegmentRequest.getAgreementRebateBasicServiceRewardList())) {
                            throw new BusinessException(ResultCode.PARAM_MISS);
                        }
                    }
                    // 校验项目服务奖励阶梯集合
                    if (Objects.nonNull(agreementRebateTimeSegmentRequest.getProjectServiceRewardFlag()) && agreementRebateTimeSegmentRequest.getProjectServiceRewardFlag()) {
                        if (CollUtil.isEmpty(agreementRebateTimeSegmentRequest.getAgreementRebateProjectServiceRewardList())) {
                            throw new BusinessException(ResultCode.PARAM_MISS);
                        }
                    }
                }
            }

            rebateGoodsGroupList.forEach(addAgreementRebateGoodsGroupRequest -> {
                // 添加到所有商品内
                if (CollUtil.isNotEmpty(addAgreementRebateGoodsGroupRequest.getAgreementRebateGoodsList())) {
                    agreementRebateGoodsRequestList.addAll(addAgreementRebateGoodsGroupRequest.getAgreementRebateGoodsList());
                }

                List<AddAgreementRebateScopeRequest> agreementRebateScopeList = addAgreementRebateGoodsGroupRequest.getAgreementRebateScopeList();
                // 同个商品组内，如果存在返利范围选择了无的，则只允许存在一个返利范围的设置
                long noneScopeNum = agreementRebateScopeList.stream().filter(
                        addAgreementRebateScopeRequest -> AgreementControlSaleTypeEnum.NONE == AgreementControlSaleTypeEnum.getByCode(addAgreementRebateScopeRequest.getControlSaleType())).count();
                if (noneScopeNum > 0 && agreementRebateScopeList.size() > 1) {
                    throw new BusinessException(UserErrorCode.REBATE_SCOPE_SET_ERROR);
                }

                // 同个返利范围内，如果选择了黑名单或白名单，必须选择客户或区域
                agreementRebateScopeList.forEach(agreementRebateScopeRequest -> {
                    List<Integer> controlSaleConditionList = agreementRebateScopeRequest.getAgreementRebateControlList();
                    if (AgreementControlSaleTypeEnum.NONE != AgreementControlSaleTypeEnum.getByCode(agreementRebateScopeRequest.getControlSaleType())) {
                        if (CollUtil.isEmpty(controlSaleConditionList)) {
                            throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                        }

                        // 选择了区域或者客户类型，对应的值不能为空
                        if (controlSaleConditionList.contains(AgreementControlSaleConditionEnum.AREA.getCode())) {
                            if (Objects.isNull(agreementRebateScopeRequest.getAddAgreementRebateControlArea())
                                    || StrUtil.isEmpty(agreementRebateScopeRequest.getAddAgreementRebateControlArea().getJsonContent())) {
                                throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                            }
                        }
                        if (controlSaleConditionList.contains(AgreementControlSaleConditionEnum.CUSTOMER_TYPE.getCode())) {
                            if (CollUtil.isEmpty(agreementRebateScopeRequest.getAgreementRebateControlCustomerType())) {
                                throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                            }
                        }
                    }
                });

                // 协议返利任务量阶梯校验
                agreementRebateScopeList.forEach(this::checkAgreementRebateRule);

            });

            // 校验一个时段内的所有商品组之间是否存在重复商品
            if (CollUtil.isNotEmpty(agreementRebateGoodsRequestList)) {
                Map<Long, Long> map = agreementRebateGoodsRequestList.stream().collect(Collectors.groupingBy(AddAgreementRebateGoodsRequest::getSpecificationGoodsId, Collectors.counting()));
                map.forEach((specificationGoodsId, number) -> {
                    if (number > 1) {
                        throw new BusinessException(UserErrorCode.SAME_SEGMENT_EXIST_SAME_GOODS);
                    }
                });
            }

        });

        // 校验时间段和重叠
        checkSegmentDuplicate(request, rebateTimeSegmentList);

    }

    /**
     * 校验时间段和重叠
     *
     * @param request
     * @param rebateTimeSegmentList
     */
    private void checkSegmentDuplicate(AddAgreementRebateTermsRequest request, List<AddAgreementRebateTimeSegmentRequest> rebateTimeSegmentList) {
        List<AddAgreementRebateTimeSegmentRequest> agreementRebateTimeSegmentList = rebateTimeSegmentList.stream().filter(agreementRebateTimeSegment -> agreementRebateTimeSegment.getType().equals(2)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(agreementRebateTimeSegmentList)) {
            agreementRebateTimeSegmentList.sort(Comparator.comparing(AddAgreementRebateTimeSegmentRequest::getStartTime));

            AgreementMainTermsDO mainTermsDO = this.agreementMainTermsService.getById(request.getAgreementId());
            for (int i = 0; i < agreementRebateTimeSegmentList.size(); i++) {
                if (AgreementTimeSegmentTypeSetEnum.ALL_TIME != AgreementTimeSegmentTypeSetEnum.getByCode(request.getTimeSegmentTypeSet())) {
                    // 第一个子时段开始时间要等于协议开始时间
                    if (i == 0) {
                        Date firstStartTime = agreementRebateTimeSegmentList.get(i).getStartTime();
                        if (DateUtil.compare(firstStartTime, mainTermsDO.getStartTime()) != 0) {
                            throw new BusinessException(UserErrorCode.REBATE_TIME_SEGMENT_TIME_ERROR);
                        }
                    }

                    // 最后一个子时段结束时间要等于协议结束时间
                    if (i == agreementRebateTimeSegmentList.size() - 1) {
                        Date endTime = agreementRebateTimeSegmentList.get(i).getEndTime();
                        if (DateUtil.compare(endTime, mainTermsDO.getEndTime()) != 0) {
                            throw new BusinessException(UserErrorCode.REBATE_TIME_SEGMENT_TIME_ERROR);
                        }
                    }

                    if (agreementRebateTimeSegmentList.size() > 1) {
                        // 每一个时段开始时间，必须是上一个时段的结束时间的第二天
                        for (int j = 1; j < agreementRebateTimeSegmentList.size()+1; j++) {
                            // 前一个子时段的结束时间 + 1天，必须要等于下一个子时段的开始时间
                            DateTime beforeEndTime = DateUtil.offsetDay(agreementRebateTimeSegmentList.get(i).getEndTime(), 1);
                            if (DateUtil.compare(beforeEndTime, agreementRebateTimeSegmentList.get(j).getStartTime()) != 0) {
                                throw new BusinessException(UserErrorCode.REBATE_TIME_SEGMENT_TIME_ERROR);
                            }
                        }
                    }

                }

            }
        }
    }

    /**
     * 协议返利任务量阶梯校验
     *
     * @param agreementRebateScopeRequest
     */
    private void checkAgreementRebateRule(AddAgreementRebateScopeRequest agreementRebateScopeRequest) {
        // 协议返利任务量阶梯
        List<AddAgreementRebateTaskStageRequest> agreementRebateTaskStageList = agreementRebateScopeRequest.getAgreementRebateTaskStageList();
        if (CollUtil.isEmpty(agreementRebateTaskStageList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        agreementRebateTaskStageList.forEach(agreementRebateTaskStageRequest -> {
            // 协议返利阶梯
            List<AddAgreementRebateStageRequest> agreementRebateStageList = agreementRebateTaskStageRequest.getAgreementRebateStageList();
            if (CollUtil.isEmpty(agreementRebateStageList)) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }

            agreementRebateStageList.forEach(addAgreementRebateStageRequest -> {
                // 当设置满**元返**元时，返的元不能大于满的元
                if (addAgreementRebateStageRequest.getFull() != null && addAgreementRebateStageRequest.getBack() != null) {
                    if (addAgreementRebateStageRequest.getFullUnit() == 1 && addAgreementRebateStageRequest.getBackUnit() == 1) {
                        if (addAgreementRebateStageRequest.getBack().compareTo(addAgreementRebateStageRequest.getFull()) > 0) {
                            throw new BusinessException(UserErrorCode.FULL_NOT_MORE_THAN_BACK);
                        }
                    }
                }
            });

        });
    }
}
