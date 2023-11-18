package com.yiling.user.agreementv2.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.bo.AgreementAttachmentBO;
import com.yiling.user.agreementv2.bo.AgreementAuthListItemBO;
import com.yiling.user.agreementv2.bo.AgreementDetailBO;
import com.yiling.user.agreementv2.bo.AgreementGoodsListItemBO;
import com.yiling.user.agreementv2.bo.AgreementImportListItemBO;
import com.yiling.user.agreementv2.bo.AgreementListItemBO;
import com.yiling.user.agreementv2.bo.AgreementMainTermsBO;
import com.yiling.user.agreementv2.bo.AgreementRebateTermsBO;
import com.yiling.user.agreementv2.bo.AgreementSettlementTermsBO;
import com.yiling.user.agreementv2.bo.AgreementSupplySalesTermsBO;
import com.yiling.user.agreementv2.dto.AgreementAttachmentDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesTermsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddRelationSubEnterpriseRequest;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementAuthPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementExistRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementMainTermsRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreementv2.dto.request.QueryImportAgreementListRequest;
import com.yiling.user.agreementv2.dto.request.UpdateArchiveAgreementRequest;
import com.yiling.user.agreementv2.dto.request.UpdateAuthAgreementRequest;
import com.yiling.user.agreementv2.entity.AgreementAttachmentDO;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.dao.AgreementMainTermsMapper;
import com.yiling.user.agreementv2.entity.AgreementManufacturerDO;
import com.yiling.user.agreementv2.entity.AgreementPaymentMethodDO;
import com.yiling.user.agreementv2.entity.AgreementRebateTermsDO;
import com.yiling.user.agreementv2.entity.AgreementSettlementMethodDO;
import com.yiling.user.agreementv2.entity.AgreementStatusLogDO;
import com.yiling.user.agreementv2.enums.AgreementAuthStatusEnum;
import com.yiling.user.agreementv2.enums.AgreementEffectStatusEnum;
import com.yiling.user.agreementv2.enums.AgreementEffectStatusProEnum;
import com.yiling.user.agreementv2.enums.AgreementFirstTypeEnum;
import com.yiling.user.agreementv2.enums.AgreementPayMethodEnum;
import com.yiling.user.agreementv2.enums.AgreementTypeEnum;
import com.yiling.user.agreementv2.enums.ManufacturerTypeEnum;
import com.yiling.user.agreementv2.service.AgreementAttachmentService;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerService;
import com.yiling.user.agreementv2.service.AgreementPaymentMethodService;
import com.yiling.user.agreementv2.service.AgreementRebateTermsService;
import com.yiling.user.agreementv2.service.AgreementRelationSubEnterpriseService;
import com.yiling.user.agreementv2.service.AgreementSettlementMethodService;
import com.yiling.user.agreementv2.service.AgreementSettlementTermsService;
import com.yiling.user.agreementv2.service.AgreementStatusLogService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesTermsService;
import com.yiling.user.agreementv2.utils.GenerateAgreementNoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.entity.UserDO;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议主条款表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
@Slf4j
@Service
public class AgreementMainTermsServiceImpl extends BaseServiceImpl<AgreementMainTermsMapper, AgreementMainTermsDO> implements AgreementMainTermsService {

    @Autowired
    private AgreementAttachmentService agreementAttachmentService;
    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementSupplySalesTermsService agreementSupplySalesTermsService;
    @Autowired
    private AgreementSettlementTermsService agreementSettlementTermsService;
    @Autowired
    private AgreementRebateTermsService agreementRebateTermsService;
    @Autowired
    private AgreementPaymentMethodService agreementPaymentMethodService;
    @Autowired
    private AgreementSettlementMethodService agreementSettlementMethodService;
    @Autowired
    private AgreementSupplySalesGoodsService agreementSupplySalesGoodsService;
    @Autowired
    private AgreementManufacturerService manufacturerService;
    @Autowired
    private AgreementManufacturerGoodsService manufacturerGoodsService;
    @Autowired
    private AgreementStatusLogService agreementStatusLogService;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgreementRelationSubEnterpriseService relationSubEnterpriseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAgreement(CreateAgreementRequest request) {
        log.info("新建协议入参数据 {}", JSONObject.toJSONString(request));
        // 协议主条款
        long sum = System.currentTimeMillis();
        long time = System.currentTimeMillis();
        request.getAgreementMainTerms().setOpUserId(request.getOpUserId());
        AgreementMainTermsDO agreementMainTermsDO = agreementMainTermsService.addAgreementMainTerms(request.getAgreementMainTerms());
        log.debug("新增协议主条款耗时：{}", System.currentTimeMillis() - time);
        time = System.currentTimeMillis();

        // 全局数据统一校验
        Long agreementId = agreementMainTermsDO.getId();
        request.getAgreementSupplySalesTerms().setAgreementId(agreementId);
        request.getAgreementSettlementTerms().setAgreementId(agreementId);
        request.getAgreementRebateTerms().setAgreementId(agreementId);
        this.checkGlobalTerms(request);

        // 协议供销条款
        request.getAgreementSupplySalesTerms().setAgreementId(agreementId);
        request.getAgreementSupplySalesTerms().setOpUserId(request.getOpUserId());
        agreementSupplySalesTermsService.addAgreementSupplySalesTerms(request.getAgreementSupplySalesTerms());
        log.debug("新增协议供销条款耗时：{}", System.currentTimeMillis() - time);
        time = System.currentTimeMillis();

        // 协议结算条款
        request.getAgreementSettlementTerms().setAgreementId(agreementId);
        request.getAgreementSettlementTerms().setOpUserId(request.getOpUserId());
        agreementSettlementTermsService.addAgreementSettlementTerms(request.getAgreementSettlementTerms());
        log.debug("新增协议结算条款耗时：{}", System.currentTimeMillis() - time);
        time = System.currentTimeMillis();

        // 协议返利条款
        request.getAgreementRebateTerms().setOpUserId(request.getOpUserId());
        agreementRebateTermsService.addAgreementRebateTerms(request.getAgreementRebateTerms());
        log.debug("新增协议返利条款耗时：{}", System.currentTimeMillis() - time);

        log.info("新建协议共耗时 {}", System.currentTimeMillis() - sum);

        return true;
    }

    /**
     * 全局数据统一校验
     *
     * @param request
     */
    public void checkGlobalTerms(CreateAgreementRequest request) {
        agreementSupplySalesTermsService.validAgreementSupplySalesTerms(request.getAgreementSupplySalesTerms());
        agreementSettlementTermsService.checkSettlementTerms(request.getAgreementSettlementTerms());
        agreementRebateTermsService.checkRebateTerms(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AgreementMainTermsDO addAgreementMainTerms(AddAgreementMainTermsRequest request) {
        // 基础校验
        this.checkAgreementMainTerms(request);

        AgreementMainTermsDO agreementMainTermsDO = PojoUtils.map(request, AgreementMainTermsDO.class);
        agreementMainTermsDO.setAuthStatus(AgreementAuthStatusEnum.WAITING.getCode());
        // 生成协议流水号和编号
        Integer serialNo = this.generateSerialNo(request.getAgreementType());
        agreementMainTermsDO.setAgreementSerialNo(serialNo);
        String agreementNo = GenerateAgreementNoUtils.generateNo(request.getAgreementType(), serialNo);
        agreementMainTermsDO.setAgreementNo(agreementNo);
        // 协议负责人名称
        if (Objects.nonNull(request.getMainUser()) && request.getMainUser() != 0) {
            agreementMainTermsDO.setMainUserName(request.getMainUser() == 1 ? request.getFirstSignUserName() : request.getSecondSignUserName());
        }
        // 保存主协议内容
        this.save(agreementMainTermsDO);

        // 保存协议附件
        if (CollUtil.isNotEmpty(request.getAgreementAttachmentList())) {
            log.debug("协议附件信息：{}", JSONObject.toJSONString(request.getAgreementAttachmentList()));
            List<AgreementAttachmentDO> attachmentDOList = PojoUtils.map(request.getAgreementAttachmentList(), AgreementAttachmentDO.class);
            attachmentDOList.forEach(agreementAttachmentDO -> agreementAttachmentDO.setAgreementId(agreementMainTermsDO.getId()));
            agreementAttachmentService.saveBatch(attachmentDOList);
        }

        // 保存协议乙方关联子公司
        if (CollUtil.isNotEmpty(request.getSecondSubEidList())) {
            AddRelationSubEnterpriseRequest subEnterpriseRequest = new AddRelationSubEnterpriseRequest();
            subEnterpriseRequest.setAgreementId(agreementMainTermsDO.getId());
            subEnterpriseRequest.setSecondEid(agreementMainTermsDO.getSecondEid());
            subEnterpriseRequest.setSubEidList(request.getSecondSubEidList());
            subEnterpriseRequest.setOpUserId(request.getOpUserId());
            relationSubEnterpriseService.addRelation(subEnterpriseRequest);
        }

        return agreementMainTermsDO;
    }

    /**
     * 生成协议流水号
     *
     * @param agreementType
     * @return
     */
    @Override
    public Integer generateSerialNo(Integer agreementType){
        QueryWrapper<AgreementMainTermsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AgreementMainTermsDO::getAgreementType, agreementType);
        wrapper.select("max(agreement_serial_no) as agreementSerialNo");
        AgreementMainTermsDO mainTermsDo = agreementMainTermsService.getOne(wrapper);

        if (Objects.isNull(mainTermsDo)) {
            return 1;
        } else {
            return mainTermsDo.getAgreementSerialNo() + 1;
        }
    }

    @Override
    public Page<AgreementListItemBO> queryAgreementPage(QueryAgreementPageListRequest request) {
        request.setAuthStatus(AgreementAuthStatusEnum.PASS.getCode());
        request.setStartTime(request.getStartTime() != null ? DateUtil.beginOfDay(request.getStartTime()) : null);
        request.setEndTime(request.getEndTime() != null ? DateUtil.endOfDay(request.getEndTime()) : null);
        request.setStartAuthPassTime(request.getStartAuthPassTime() != null ? DateUtil.beginOfDay(request.getStartAuthPassTime()) : null);
        request.setEndAuthPassTime(request.getEndAuthPassTime() != null ? DateUtil.endOfDay(request.getEndAuthPassTime()) : null);
        request.setStartCreateTime(request.getStartCreateTime() != null ? DateUtil.beginOfDay(request.getStartCreateTime()) : null);
        request.setEndCreateTime(request.getEndCreateTime() != null ? DateUtil.endOfDay(request.getEndCreateTime()) : null);
        Page<AgreementListItemBO> agreementPage = this.baseMapper.queryAgreementPage(request.getPage(), request);

        agreementPage.getRecords().forEach(agreementListItemBO -> {
            // 支付方式
            List<AgreementPaymentMethodDO> paymentMethodDOList = agreementPaymentMethodService.queryList(agreementListItemBO.getId());
            List<Integer> payMethodList = paymentMethodDOList.stream().map(AgreementPaymentMethodDO::getPayMethod).collect(Collectors.toList());
            List<String> payMethodNameList = payMethodList.stream().map(payMethod -> AgreementPayMethodEnum.getByCode(payMethod).getName()).collect(Collectors.toList());
            agreementListItemBO.setPayMethodList(payMethodNameList);
            // 结算方式
            List<String> settlementMethodList = ListUtil.toList();
            AgreementSettlementMethodDO settlementMethodDO = agreementSettlementMethodService.getByAgreementId(agreementListItemBO.getId());
            if (settlementMethodDO.getAdvancePaymentFlag() == 1) {
                settlementMethodList.add("预付款");
            }
            if (settlementMethodDO.getPaymentDaysFlag() == 1) {
                settlementMethodList.add("账期");
            }
            if (settlementMethodDO.getActualSalesSettlementFlag() == 1) {
                settlementMethodList.add("实销实结");
            }
            if (settlementMethodDO.getPayDeliveryFlag() == 1) {
                settlementMethodList.add("货到付款");
            }
            if (settlementMethodDO.getPressGroupFlag() == 1) {
                settlementMethodList.add("压批");
            }
            if (settlementMethodDO.getCreditFlag() == 1) {
                settlementMethodList.add("授信");
            }
            agreementListItemBO.setSettlementMethodList(settlementMethodList);
            // 协议品种数量
            Integer goodsNumber = agreementSupplySalesGoodsService.getSupplyGoodsNumber(agreementListItemBO.getId());
            agreementListItemBO.setGoodsNumber(goodsNumber);
            // 协议生效状态
            if (AgreementEffectStatusEnum.getByCode(agreementListItemBO.getEffectStatus()) != AgreementEffectStatusEnum.STOP
                    && AgreementEffectStatusEnum.getByCode(agreementListItemBO.getEffectStatus()) != AgreementEffectStatusEnum.INVALID) {
                // 有效：当前时间在协议的生效期内，该协议为有效协议；
                // 过期：当前时间晚于协议的生效期，该协议为过期协议；
                // 中止：在协议有效期内，由于某种原因需要将协议中止，经有权限的账号申请并审核通过后，协议生效状态变为中止；中止的协议，在中止前参与返利计算；
                // 作废：在协议有效期内，由于某种原因需要将协议作废，经有权限的账号申请并审核通过后，协议生效状态变为作废；作废的协议始终不参与返利计算；协议作废后不再展示在协议明细和我的协议列表中，展示存放在作废协议列表中。
                // 排期：当前时间在协议的生效期之前，该协议为排期协议；
                Date startTime = DateUtil.beginOfDay(agreementListItemBO.getStartTime());
                Date endTime = DateUtil.endOfDay(agreementListItemBO.getEndTime());
                if (DateUtil.isIn(new Date(), startTime, endTime)) {
                    agreementListItemBO.setEffectStatus(AgreementEffectStatusProEnum.VALID.getCode());
                } else if (DateUtil.compare(new Date(), endTime) > 0) {
                    agreementListItemBO.setEffectStatus(AgreementEffectStatusProEnum.OVERDUE.getCode());
                } else if (DateUtil.compare(new Date(), startTime) < 0) {
                    agreementListItemBO.setEffectStatus(AgreementEffectStatusProEnum.SCHEDULING.getCode());
                }
            }

        });

        return agreementPage;
    }

    @Override
    public Page<AgreementGoodsListItemBO> queryAgreementGoodsPage(QueryAgreementGoodsPageRequest request) {
        Page<AgreementSupplySalesGoodsDTO> rebateGoodsDoPage = agreementSupplySalesGoodsService.getSupplyGoodsByAgreementId(request);
        Page<AgreementGoodsListItemBO> itemBoPage = PojoUtils.map(rebateGoodsDoPage, AgreementGoodsListItemBO.class);
        if (CollUtil.isEmpty(itemBoPage.getRecords())) {
            return itemBoPage;
        }

        // 获取协议返利条款
        AgreementRebateTermsDO rebateTermsDo = agreementRebateTermsService.getRebateTermsByAgreementId(request.getId());
        // 获取协议供销条款
        AgreementSupplySalesTermsDTO salesTermsDto = agreementSupplySalesTermsService.getSalesTermsByAgreementId(request.getId());
        // 协议主条款
        AgreementMainTermsDO mainTermsDo = this.getById(request.getId());

        // 获取规格ID集合
        List<Long> specificationIdList = itemBoPage.getRecords().stream().map(AgreementGoodsListItemBO::getSpecificationGoodsId).distinct().collect(Collectors.toList());
        Map<Long, List<AgreementManufacturerGoodsDTO>> goodsSpecificationMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(specificationIdList)) {
            goodsSpecificationMap = manufacturerGoodsService.getGoodsBySpecificationId(specificationIdList);
        }

        // 获取厂家集合
        Set<Long> manufacturerIdSet = new HashSet<>();
        goodsSpecificationMap.forEach((specificationId, goodsList) -> goodsList.forEach(
                agreementManufacturerGoodsDTO -> manufacturerIdSet.add(agreementManufacturerGoodsDTO.getManufacturerId())));
        Map<Long, AgreementManufacturerDO> manufacturerDoMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(manufacturerIdSet)) {
            manufacturerDoMap = manufacturerService.listByIds(manufacturerIdSet).stream().collect(Collectors.toMap(BaseDO::getId, Function.identity()));
        }

        for (AgreementGoodsListItemBO agreementGoodsListItemBO : itemBoPage.getRecords()) {
            // 返利条款相关
            PojoUtils.map(rebateTermsDo, agreementGoodsListItemBO);
            // 购进渠道
            agreementGoodsListItemBO.setBuyChannel(salesTermsDto.getBuyChannel());
            // 生产厂家和品牌厂家
            if (AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER == AgreementFirstTypeEnum.getByCode(mainTermsDo.getFirstType())
                    || AgreementFirstTypeEnum.INDUSTRIAL_BRAND == AgreementFirstTypeEnum.getByCode(mainTermsDo.getFirstType())) {

                List<AgreementManufacturerGoodsDTO> manufacturerGoodsDTOList = goodsSpecificationMap.get(agreementGoodsListItemBO.getSpecificationGoodsId());
                if (CollUtil.isNotEmpty(manufacturerGoodsDTOList)) {
                    for (AgreementManufacturerGoodsDTO agreementManufacturerGoodsDTO : manufacturerGoodsDTOList) {
                        AgreementManufacturerDO manufacturerDO = manufacturerDoMap.get(agreementManufacturerGoodsDTO.getManufacturerId());
                        if (Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.PRODUCER == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                            agreementGoodsListItemBO.setManufacturerName(manufacturerDO.getEname());
                        } else if(Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.BRAND == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                            agreementGoodsListItemBO.setBrandManufacturerName(manufacturerDO.getEname());
                        }
                    }
                }

            }
        }

        return itemBoPage;
    }

    /**
     * 获取整个协议详情
     *
     * @param id 协议ID
     * @return
     */
    @Override
    public AgreementDetailBO getDetail(Long id) {
        long time = System.currentTimeMillis();
        AgreementDetailBO agreementDetailBO = new AgreementDetailBO();
        agreementDetailBO.setAgreementMainTerms(this.getAgreementMainTerms(id));
        agreementDetailBO.setAgreementSupplySalesTerms(agreementSupplySalesTermsService.getSupplySalesTerms(id));
        agreementDetailBO.setAgreementSettlementTerms(agreementSettlementTermsService.getAgreementSettlementTerms(id));
        agreementDetailBO.setAgreementRebateTerms(agreementRebateTermsService.getAgreementRebateTerms(id));
        agreementDetailBO.setAgreementStatusLogList(agreementStatusLogService.getByAgreementId(id));
        log.debug("获取协议详情共耗时：{}", System.currentTimeMillis() - time);

        return agreementDetailBO;
    }

    @Override
    public Page<AgreementAuthListItemBO> queryAgreementAuthPage(QueryAgreementAuthPageRequest request) {
        request.setNotAuthStatusList(ListUtil.toList(AgreementAuthStatusEnum.ARCHIVE.getCode()));
        LambdaQueryWrapper<AgreementMainTermsDO> wrapper = this.getAuthListWrapper(request);
        return PojoUtils.map(this.page(request.getPage(), wrapper), AgreementAuthListItemBO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAuthAgreement(UpdateAuthAgreementRequest request) {
        AgreementMainTermsDO mainTermsDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.AGREEMENT_NOT_EXIST));
        if (AgreementAuthStatusEnum.REJECT == AgreementAuthStatusEnum.getByCode(request.getAuthStatus())) {
            if (StrUtil.isEmpty(request.getAuthRejectReason())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        }

        // 更新协议审核状态
        AgreementMainTermsDO authTermsDo = new AgreementMainTermsDO();
        authTermsDo.setId(mainTermsDO.getId());
        authTermsDo.setAuthStatus(request.getAuthStatus());
        authTermsDo.setAuthRejectReason(request.getAuthRejectReason());
        authTermsDo.setPaperNo(request.getPaperNo());
        authTermsDo.setAuthUser(request.getOpUserId());
        authTermsDo.setAuthTime(new Date());
        authTermsDo.setOpUserId(request.getOpUserId());
        this.updateById(authTermsDo);

        // 新增协议审核日志
        AgreementStatusLogDO agreementStatusLogDO = new AgreementStatusLogDO();
        agreementStatusLogDO.setAgreementId(mainTermsDO.getId());
        agreementStatusLogDO.setAuthStatus(request.getAuthStatus());
        agreementStatusLogDO.setAuthTime(new Date());
        agreementStatusLogDO.setAuthUser(request.getOpUserId());
        agreementStatusLogDO.setAuthRejectReason(request.getAuthRejectReason());
        agreementStatusLogDO.setOpUserId(request.getOpUserId());
        this.agreementStatusLogService.save(agreementStatusLogDO);

        return true;
    }

    @Override
    public List<AgreementImportListItemBO> queryImportAgreementList(QueryImportAgreementListRequest request) {
        List<AgreementImportListItemBO> importListItemBOList = this.baseMapper.queryImportAgreementList(request);
        importListItemBOList.forEach(agreementImportListItemBO -> {
            // 协议附件
            List<AgreementAttachmentDTO> agreementAttachmentDTOList = agreementAttachmentService.getByAgreementId(agreementImportListItemBO.getId());
            agreementImportListItemBO.setAgreementAttachmentList(PojoUtils.map(agreementAttachmentDTOList, AgreementAttachmentBO.class));

            // 结算条款
            AgreementSettlementTermsBO settlementTerms = agreementSettlementTermsService.getAgreementSettlementTerms(agreementImportListItemBO.getId());
            agreementImportListItemBO.setAgreementSettlementTerms(settlementTerms);

            Integer supplyGoodsNumber = agreementSupplySalesGoodsService.getSupplyGoodsNumber(agreementImportListItemBO.getId());
            agreementImportListItemBO.setGoodsNumber(supplyGoodsNumber);

            // 供销条款
            AgreementSupplySalesTermsBO supplySalesTerms = agreementSupplySalesTermsService.getSupplySalesTerms(agreementImportListItemBO.getId());
            agreementImportListItemBO.setAgreementSupplySalesTerms(supplySalesTerms);

            // 返利条款
            AgreementRebateTermsBO rebateTermsBO = PojoUtils.map(agreementRebateTermsService.getRebateTermsByAgreementId(agreementImportListItemBO.getId()), AgreementRebateTermsBO.class);
            agreementImportListItemBO.setAgreementRebateTerms(rebateTermsBO);

        });

        return importListItemBOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArchiveAgreement(UpdateArchiveAgreementRequest request) {
        AgreementMainTermsDO mainTermsDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.AGREEMENT_NOT_EXIST));

        if (AgreementAuthStatusEnum.getByCode(mainTermsDO.getAuthStatus()) != AgreementAuthStatusEnum.PASS) {
            throw new BusinessException(UserErrorCode.AGREEMENT_NOT_CAN_ARCHIVE);
        }
        if (DateUtil.compare(new Date(), mainTermsDO.getEndTime()) < 0) {
            throw new BusinessException(UserErrorCode.AGREEMENT_NOT_CAN_ARCHIVE);
        }

        AgreementMainTermsDO termsDO = new AgreementMainTermsDO();
        termsDO.setId(request.getId());
        termsDO.setAuthStatus(AgreementAuthStatusEnum.ARCHIVE.getCode());
        termsDO.setAuthTime(new Date());
        termsDO.setAuthUser(request.getOpUserId());
        termsDO.setArchiveNo(request.getArchiveNo());
        termsDO.setArchiveRemark(request.getArchiveRemark());
        termsDO.setOpUserId(request.getOpUserId());
        return this.updateById(termsDO);
    }

    /**
     * 获取主协议页数据
     *
     * @param id
     * @return
     */
    public AgreementMainTermsBO getAgreementMainTerms(Long id) {
        long time = System.currentTimeMillis();
        // 协议主条款设置
        AgreementMainTermsDO mainTermsDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.AGREEMENT_NOT_EXIST));
        AgreementMainTermsBO mainTermsBO = PojoUtils.map(mainTermsDO, AgreementMainTermsBO.class);
        if (Objects.nonNull(mainTermsBO.getBusinessEid()) && mainTermsBO.getBusinessEid() != 0) {
            mainTermsBO.setBusinessName(Optional.ofNullable(enterpriseService.getById(mainTermsBO.getBusinessEid())).orElse(new EnterpriseDO()).getName());
        }
        mainTermsBO.setCreateUserName(Optional.ofNullable(userService.getById(mainTermsDO.getCreateUser())).orElse(new UserDO()).getName());
        // 协议附件
        List<AgreementAttachmentDTO> attachmentDtoList = agreementAttachmentService.getByAgreementId(id);
        mainTermsBO.setAgreementAttachmentList(PojoUtils.map(attachmentDtoList, AgreementAttachmentBO.class));
        log.debug("获取协议主条款共耗时：{}", System.currentTimeMillis() - time);
        return mainTermsBO;
    }

    /**
     * 校验协议类型
     *
     * @param request
     */
    public void checkAgreementMainTerms(AddAgreementMainTermsRequest request) {
        // 校验协议类型：工业-生产厂家，则协议类型选项为：一级协议、二级协议、临时协议、KA连锁协议
        List<AgreementTypeEnum> agreementTypeEnumList = Objects.requireNonNull(AgreementFirstTypeEnum.getByCode(request.getFirstType())).getAgreementTypeEnumList();
        if (!agreementTypeEnumList.contains(AgreementTypeEnum.getByCode(request.getAgreementType()))) {
            throw new BusinessException(UserErrorCode.FIRST_AGREEMENT_TYPE_ERROR);
        }
        // 活动协议为是时：商家运营签订人必填
        if (Objects.nonNull(request.getActiveFlag()) && request.getActiveFlag() == 1) {
            if (Objects.isNull(request.getBusinessOperatorId()) || request.getBusinessOperatorId() == 0 || StrUtil.isEmpty(request.getBusinessOperatorName())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        }
        // 协议类型为KA协议时：KA协议类型必填
        if (Objects.nonNull(request.getAgreementType()) && AgreementTypeEnum.KA_AGREEMENT == AgreementTypeEnum.getByCode(request.getAgreementType())) {
            if (Objects.isNull(request.getAgreementType()) || request.getAgreementType() == 0) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
        }
        // 校验协议如果存在，则不允许创建
        QueryAgreementExistRequest existRequest = new QueryAgreementExistRequest();
        existRequest.setAgreementType(request.getAgreementType());
        existRequest.setFirstType(request.getFirstType());
        existRequest.setEid(request.getEid());
        existRequest.setSecondEid(request.getSecondEid());
        existRequest.setStartTime(request.getStartTime());
        existRequest.setEndTime(request.getEndTime());
        String agreementNo = this.checkAgreementExist(existRequest);
        if (StrUtil.isNotEmpty(agreementNo)) {
            String message = StrFormatter.format(UserErrorCode.AGREEMENT_EXIST.getMessage(), agreementNo);
            throw new BusinessException(UserErrorCode.AGREEMENT_EXIST, message);
        }
    }

    @Override
    public String checkAgreementExist(QueryAgreementExistRequest request){
        // 校验“甲方类型”、“协议类型”、“甲方”、“乙方”、“生效日期”、“至”6个字段的值完全一样时，给出提示
        LambdaQueryWrapper<AgreementMainTermsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementMainTermsDO::getFirstType, request.getFirstType());
        wrapper.eq(AgreementMainTermsDO::getAgreementType, request.getAgreementType());
        wrapper.eq(AgreementMainTermsDO::getEid, request.getEid());
        wrapper.eq(AgreementMainTermsDO::getSecondEid, request.getSecondEid());
        Date startStartTime = DateUtil.beginOfDay(request.getStartTime());
        Date startEndTime = DateUtil.endOfDay(request.getStartTime());
        wrapper.between(AgreementMainTermsDO::getStartTime, startStartTime, startEndTime);
        Date endStartTime = DateUtil.beginOfDay(request.getEndTime());
        Date endEndTime = DateUtil.endOfDay(request.getEndTime());
        wrapper.between(AgreementMainTermsDO::getEndTime, endStartTime, endEndTime);
        wrapper.last("limit 1");

        return Optional.ofNullable(this.getOne(wrapper)).orElse(new AgreementMainTermsDO()).getAgreementNo();

    }

    @Override
    public List<AgreementMainTermsDO> getAgreementMainTerms(QueryAgreementMainTermsRequest request){
        LambdaQueryWrapper<AgreementMainTermsDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getEid()) && request.getEid() != 0) {
            wrapper.eq(AgreementMainTermsDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getFirstType()) && request.getFirstType() != 0) {
            wrapper.eq(AgreementMainTermsDO::getFirstType, request.getFirstType());
        }
        if (Objects.nonNull(request.getSecondEid()) && request.getSecondEid() != 0) {
            wrapper.eq(AgreementMainTermsDO::getSecondEid, request.getSecondEid());
        }
        if (Objects.nonNull(request.getAgreementType()) && request.getAgreementType() != 0) {
            wrapper.eq(AgreementMainTermsDO::getAgreementType, request.getAgreementType());
        }
        if (Objects.nonNull(request.getAuthStatus()) && request.getAuthStatus() != 0) {
            wrapper.eq(AgreementMainTermsDO::getAuthStatus, request.getAuthStatus());
        }
        if (Objects.nonNull(request.getEffectStatus()) && request.getEffectStatus() != 0) {
            wrapper.eq(AgreementMainTermsDO::getEffectStatus, request.getEffectStatus());
        }

        return this.list(wrapper);
    }

    public LambdaQueryWrapper<AgreementMainTermsDO> getAuthListWrapper(QueryAgreementAuthPageRequest request){
        LambdaQueryWrapper<AgreementMainTermsDO> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(request.getAgreementNo())) {
            wrapper.like(AgreementMainTermsDO::getAgreementNo, request.getAgreementNo());
        }
        if (Objects.nonNull(request.getAgreementType()) && request.getAgreementType() != 0) {
            wrapper.eq(AgreementMainTermsDO::getAgreementType, request.getAgreementType());
        }
        if (Objects.nonNull(request.getStartCreateTime())) {
            wrapper.ge(AgreementMainTermsDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            wrapper.le(AgreementMainTermsDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if (Objects.nonNull(request.getAuthStatus()) && request.getAuthStatus() != 0) {
            wrapper.eq(AgreementMainTermsDO::getAuthStatus, request.getAuthStatus());
        }
        if (CollUtil.isNotEmpty(request.getNotAuthStatusList())) {
            wrapper.notIn(AgreementMainTermsDO::getAuthStatus, request.getNotAuthStatusList());
        }
        if (StrUtil.isNotEmpty(request.getMainUserName())) {
            wrapper.eq(AgreementMainTermsDO::getMainUserName, request.getMainUserName());
        }
        if (Objects.nonNull(request.getBillsType()) && request.getBillsType() != 0) {
            wrapper.eq(AgreementMainTermsDO::getBillsType, request.getBillsType());
        }
        if (StrUtil.isNotEmpty(request.getEname())) {
            wrapper.like(AgreementMainTermsDO::getEname, request.getEname());
        }
        if (StrUtil.isNotEmpty(request.getSecondName())) {
            wrapper.like(AgreementMainTermsDO::getSecondName, request.getSecondName());
        }

        return wrapper;
    }

}
