package com.yiling.user.agreement.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.bo.AgreementPageListItemBO;
import com.yiling.user.agreement.bo.AgreementRoleBO;
import com.yiling.user.agreement.bo.EntThirdAgreementInfoBO;
import com.yiling.user.agreement.bo.StatisticsAgreementDTO;
import com.yiling.user.agreement.dao.AgreementMapper;
import com.yiling.user.agreement.dto.AgreementConditionDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.AgreementPolicyDTO;
import com.yiling.user.agreement.dto.EntPageListItemDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.YearAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.CloseAgreementRequest;
import com.yiling.user.agreement.dto.request.EditAgreementRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementCountRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.QueryEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementRequest;
import com.yiling.user.agreement.dto.request.UpdateAgreementRequest;
import com.yiling.user.agreement.entity.AgreementConditionDO;
import com.yiling.user.agreement.entity.AgreementConditionSnapshotDO;
import com.yiling.user.agreement.entity.AgreementDO;
import com.yiling.user.agreement.entity.AgreementGoodsDO;
import com.yiling.user.agreement.entity.AgreementMultipleConditionDO;
import com.yiling.user.agreement.entity.AgreementMultipleConditionSnapshotDO;
import com.yiling.user.agreement.entity.AgreementSnapshotDO;
import com.yiling.user.agreement.entity.GoodsSnapshotDO;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementCloseEnum;
import com.yiling.user.agreement.enums.AgreementConditionRuleEnum;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementMultipleConditionEnum;
import com.yiling.user.agreement.enums.AgreementRebateCycleEnum;
import com.yiling.user.agreement.enums.AgreementRoleEnum;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.agreement.enums.QueryAgreementStatusEnum;
import com.yiling.user.agreement.service.AgreementConditionService;
import com.yiling.user.agreement.service.AgreementConditionSnapshotService;
import com.yiling.user.agreement.service.AgreementGoodsPurchaseRelationService;
import com.yiling.user.agreement.service.AgreementGoodsService;
import com.yiling.user.agreement.service.AgreementMultipleConditionService;
import com.yiling.user.agreement.service.AgreementMultipleConditionSnapshotService;
import com.yiling.user.agreement.service.AgreementRebateOrderService;
import com.yiling.user.agreement.service.AgreementService;
import com.yiling.user.agreement.service.AgreementSnapshotService;
import com.yiling.user.agreement.service.GoodsSnapshotService;
import com.yiling.user.enterprise.dto.request.AddCustomerRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.service.EnterpriseCustomerService;
import com.yiling.user.enterprise.service.EnterprisePurchaseRelationService;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.system.service.UserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 协议表 服务实现类
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@Service
@Slf4j
public class AgreementServiceImpl extends BaseServiceImpl<AgreementMapper, AgreementDO> implements AgreementService {

    @Autowired
    UserService                               userService;
    @Autowired
    EnterpriseService                         enterpriseService;
    @Autowired
    AgreementGoodsService                     agreementGoodsService;
    @Autowired
    AgreementConditionService                 agreementConditionService;
    @Autowired
    AgreementMultipleConditionService         agreementMultipleConditionService;
    @Autowired
    EnterprisePurchaseRelationService         enterprisePurchaseRelationService;
    @Autowired
    AgreementGoodsPurchaseRelationService     agreementGoodsPurchaseRelationService;
    @Autowired
    AgreementSnapshotService                  agreementSnapshotService;
    @Autowired
    AgreementConditionSnapshotService         agreementConditionSnapshotService;
    @Autowired
    AgreementMultipleConditionSnapshotService agreementMultipleConditionSnapshotService;
    @Autowired
    EnterpriseCustomerService                 enterpriseCustomerService;
    @Autowired
    GoodsSnapshotService                      goodsSnapshotService;
    @Autowired
    AgreementRebateOrderService               agreementRebateOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(SaveAgreementRequest request) {
        AgreementDO agreementDO = PojoUtils.map(request, AgreementDO.class);
        this.save(agreementDO);
        //添加协议商品
        if (CollectionUtils.isNotEmpty(request.getAgreementGoodsList())) {
            List<AgreementGoodsDO> agreementGoodsList = PojoUtils.map(request.getAgreementGoodsList(), AgreementGoodsDO.class);
            agreementGoodsList.forEach(e -> {
                e.setAgreementId(agreementDO.getId());
                e.setOpUserId(request.getOpUserId());
            });
            agreementGoodsService.saveBatch(agreementGoodsList);
            if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(request.getCategory())) {
                //添加商品和渠道商的关系
                List<Long> goodsIds = agreementGoodsList.stream().map(AgreementGoodsDO::getGoodsId).collect(Collectors.toList());
                agreementGoodsPurchaseRelationService.updateBuyerGatherByGid(goodsIds, agreementDO.getSecondEid(), request.getOpUserId());
            }
        }

        List<AgreementMultipleConditionDO> agreementMultipleConditionDOList = new ArrayList<>();

        //添加返利条件
        if (CollUtil.isNotEmpty(request.getAgreementConditionList())) {
            List<AgreementConditionDO> agreementConditionDOList = PojoUtils.map(request.getAgreementConditionList(), AgreementConditionDO.class);
            agreementConditionDOList.forEach(e -> {
                e.setAgreementId(agreementDO.getId());
                PojoUtils.map(request, e);
            });
            agreementConditionService.saveBatch(agreementConditionDOList);
            //支付方式
            List<Integer> payTypeList = request.getAgreementConditionList().get(0).getPayTypeValues();
            if (CollUtil.isNotEmpty(payTypeList)) {
                for (Integer payType : payTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.PAY_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(payType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
            //回款方式
            List<Integer> backAmountTypeList = request.getAgreementConditionList().get(0).getBackAmountTypeValues();
            if (CollUtil.isNotEmpty(backAmountTypeList)) {
                for (Integer backAmountType : backAmountTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.BACK_AMOUNT_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(backAmountType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
        }
        //添加多选值
        {
            //返利形式
            List<Integer> restitutionTypeList = request.getRestitutionTypeValues();
            if (CollUtil.isNotEmpty(restitutionTypeList)) {
                for (Integer restitutionType : restitutionTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.RESTITUTION_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(restitutionType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
            agreementMultipleConditionService.saveBatch(agreementMultipleConditionDOList);
        }
        //只有年度协议才添加采购关系
        if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(request.getCategory())) {
            //添加采购关系
            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
            addCustomerRequest.setCustomerEid(request.getThirdEid());
            addCustomerRequest.setEid(request.getSecondEid());
            addCustomerRequest.setSource(4);
            addCustomerRequest.setAddPurchaseRelationFlag(true);
            return enterpriseCustomerService.add(addCustomerRequest);
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveForImport(SaveAgreementRequest request) {
        AgreementDO agreementDO = PojoUtils.map(request, AgreementDO.class);
        this.save(agreementDO);
        //添加协议商品
        if (CollectionUtils.isNotEmpty(request.getAgreementGoodsList())) {
            List<AgreementGoodsDO> agreementGoodsList = PojoUtils.map(request.getAgreementGoodsList(), AgreementGoodsDO.class);
            agreementGoodsList.forEach(e -> {
                e.setAgreementId(agreementDO.getId());
                e.setOpUserId(request.getOpUserId());
            });
            Boolean isSuccess=agreementGoodsService.saveBatch(agreementGoodsList);
            if (!isSuccess){
                log.error("保存协议时新增协议商品失败，参数={}",agreementGoodsList);
                throw new ServiceException(ResultCode.FAILED);
            }
            if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(request.getCategory())) {
                //添加商品和渠道商的关系
                List<Long> goodsIds = agreementGoodsList.stream().map(AgreementGoodsDO::getGoodsId).collect(Collectors.toList());
                isSuccess=agreementGoodsPurchaseRelationService.updateBuyerGatherByGid(goodsIds, agreementDO.getSecondEid(), request.getOpUserId());
                if (!isSuccess){
                    log.error("保存协议时添加商品和渠道商的关系，参数={}",goodsIds);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
        }

        List<AgreementMultipleConditionDO> agreementMultipleConditionDOList = new ArrayList<>();

        //添加返利条件
        if (CollUtil.isNotEmpty(request.getAgreementConditionList())) {
            List<AgreementConditionDO> agreementConditionDOList = PojoUtils.map(request.getAgreementConditionList(), AgreementConditionDO.class);
            agreementConditionDOList.forEach(e -> {
                e.setAgreementId(agreementDO.getId());
                PojoUtils.map(request, e);
            });
            Boolean isSuccess=agreementConditionService.saveBatch(agreementConditionDOList);
            if (!isSuccess){
                log.error("保存协议时新增协议条件失败，参数={}",agreementConditionDOList);
                throw new ServiceException(ResultCode.FAILED);
            }
            //支付方式
            List<Integer> payTypeList = request.getAgreementConditionList().get(0).getPayTypeValues();
            if (CollUtil.isNotEmpty(payTypeList)) {
                for (Integer payType : payTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.PAY_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(payType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
            //回款方式
            List<Integer> backAmountTypeList = request.getAgreementConditionList().get(0).getBackAmountTypeValues();
            if (CollUtil.isNotEmpty(backAmountTypeList)) {
                for (Integer backAmountType : backAmountTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.BACK_AMOUNT_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(backAmountType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
        }
        //添加多选值
        {
            //返利形式
            List<Integer> restitutionTypeList = request.getRestitutionTypeValues();
            if (CollUtil.isNotEmpty(restitutionTypeList)) {
                for (Integer restitutionType : restitutionTypeList) {
                    AgreementMultipleConditionDO agreementMultipleConditionDO = new AgreementMultipleConditionDO();
                    agreementMultipleConditionDO.setAgreementId(agreementDO.getId());
                    agreementMultipleConditionDO.setConditionType(AgreementMultipleConditionEnum.RESTITUTION_TYPE.getCode());
                    agreementMultipleConditionDO.setConditionValue(restitutionType);
                    agreementMultipleConditionDO.setOpUserId(request.getOpUserId());
                    agreementMultipleConditionDOList.add(agreementMultipleConditionDO);
                }
            }
            if (CollUtil.isNotEmpty(agreementMultipleConditionDOList)){
                Boolean isSuccess=agreementMultipleConditionService.saveBatch(agreementMultipleConditionDOList);
                if (!isSuccess){
                    log.error("保存协议时新增多级条件时失败，参数={}",agreementMultipleConditionDOList);
                    throw new ServiceException(ResultCode.FAILED);
                }
            }
        }
        //只有年度协议才添加采购关系
        if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(request.getCategory())) {
            //添加采购关系
            AddCustomerRequest addCustomerRequest = new AddCustomerRequest();
            addCustomerRequest.setCustomerEid(request.getThirdEid());
            addCustomerRequest.setEid(request.getSecondEid());
            addCustomerRequest.setSource(4);
            addCustomerRequest.setAddPurchaseRelationFlag(true);
            boolean isSuccess = enterpriseCustomerService.add(addCustomerRequest);
            if (!isSuccess){
                log.error("保存协议时新增采购关系失败，参数={}",addCustomerRequest);
                throw new ServiceException(ResultCode.FAILED);
            }
        }
        return agreementDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(EditAgreementRequest request) {
        AgreementDO agreement = this.getById(request.getId());
        if (agreement == null) {
            return false;
        }

        if (CollectionUtils.isNotEmpty(request.getAgreementGoodsList())) {
            List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsService.getAgreementGoodsAgreementIdByList(agreement.getId());
            List<Long> goodsIds = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());

            List<AgreementGoodsDO> agreementGoodsList = PojoUtils.map(request.getAgreementGoodsList(), AgreementGoodsDO.class);
            agreementGoodsList.stream().filter(f -> !goodsIds.contains(f.getGoodsId())).forEach(e -> {
                e.setAgreementId(request.getId());
                e.setOpUserId(request.getOpUserId());
            });
            agreementGoodsService.saveBatch(agreementGoodsList);

            if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(agreement.getCategory())) {
                //添加商品和渠道商的关系
                List<Long> newGoodsIds = agreementGoodsList.stream().map(AgreementGoodsDO::getGoodsId).collect(Collectors.toList());
                agreementGoodsPurchaseRelationService.updateBuyerGatherByGid(newGoodsIds, agreement.getSecondEid(), request.getOpUserId());
            }
        }
        //如果新增品的是补充协议则打快照递增版本号
        if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(agreement.getCategory())) {
            //只有修改了条件才进行快照
            //查询协议下的条件
            List<AgreementConditionDO> conditionList = agreementConditionService.getAgreementConditionListByAgreementId(ListUtil
                    .toList(request.getId())).get(request.getId());
            //查询多选条件
            Map<String, List<Integer>> multipleCondition = agreementMultipleConditionService.getConditionValueByAgreementId(request.getId());
            //查询协议商品
            List<AgreementGoodsDTO> goodsList = agreementGoodsService.getAgreementGoodsAgreementIdByList(request.getId());
            //增加版本
            Integer version = agreement.getVersion();
            //更新版本号
            AgreementDO updateDO = new AgreementDO();
            updateDO.setId(agreement.getId());
            updateDO.setVersion(version + 1);
            updateDO.setOpUserId(request.getOpUserId());
            boolean isUpdate = updateById(updateDO);
            if (!isUpdate) {
                throw new BusinessException(AgreementErrorCode.AGREEMENT_UPDATE);
            }
            //协议快照
            agreementSnapshot(agreement, conditionList, multipleCondition, goodsList, request.getOpUserId());
            //重新计算
            ClearAgreementConditionCalculateRequest clearRequest=new ClearAgreementConditionCalculateRequest();
            clearRequest.setAgreementIds(Arrays.asList(agreement.getId()));
            clearRequest.setOpUserId(request.getOpUserId());
            agreementRebateOrderService.clearDiscountAmountByOrderIdsAndAgreementIds(clearRequest);
        }
        return true;
    }


    /**
     * 根据主键Id获取协议详情信息
     *
     * @param id
     * @return
     */
    @Override
    public AgreementDTO getAgreementDetailsInfo(Long id) {
        AgreementDO agreement = this.getById(id);
        AgreementDTO agreementBasicInfo = PojoUtils.map(agreement, AgreementDTO.class);
        return agreementBasicInfo;
    }

    @Override
    public List<AgreementDTO> getAgreementList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.toList();
        }
        List<AgreementDO> agreementList = listByIds(idList);
        List<AgreementDTO> dtoList = PojoUtils.map(agreementList, AgreementDTO.class);
        return dtoList;
    }

//    @Override
//    public AgreementConditionDTO getAgreementConditionById(Long conditionId) {
//        return PojoUtils.map(agreementConditionService.getById(conditionId), AgreementConditionDTO.class);
//    }


    @Override
    public SupplementAgreementDetailDTO querySupplementAgreementsDetail(Long id) {
        List<SupplementAgreementDetailDTO> supplementList = querySupplementAgreementsDetailList(ListUtil.toList(id));
        if (CollUtil.isEmpty(supplementList)) {
            return null;
        }
        return supplementList.get(0);
    }

    @Override
    public List<SupplementAgreementDetailDTO> querySupplementAgreementsDetailList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.toList();
        }
        List<AgreementDO> list = this.listByIds(ids);
        Map<Long, List<AgreementConditionDO>> agreementConditionMap = agreementConditionService.getAgreementConditionListByAgreementId(ids);
        Map<Long, Map<String, List<Integer>>> multipleMap = agreementMultipleConditionService.getConditionValueByAgreementId(ids);
        List<SupplementAgreementDetailDTO> detailDTOS = new ArrayList<>();
        for (AgreementDO agreementDO : list) {
            log.info("协议Id={}开始计算",agreementDO.getId());
            SupplementAgreementDetailDTO dto = PojoUtils.map(agreementDO, SupplementAgreementDetailDTO.class);

            List<AgreementConditionDO> conditionDos = agreementConditionMap.get(agreementDO.getId());
            if (CollUtil.isNotEmpty(conditionDos)) {
                //查询协议下条件
                List<AgreementConditionDTO> conditionDTOList = PojoUtils.map(conditionDos, AgreementConditionDTO.class);

                //判断是否有多选的情况
                {
                    Map<String, List<Integer>> map = multipleMap.getOrDefault(agreementDO.getId(),new HashMap<>());
                    if (dto.getRestitutionType().equals(1)) {
                        dto.setRestitutionTypeValues(map.get(AgreementMultipleConditionEnum.RESTITUTION_TYPE.getCode()));
                    }
                    if (conditionDos.get(0).getPayType().equals(1)) {
                        conditionDTOList.forEach(e -> e.setPayTypeValues(map.get(AgreementMultipleConditionEnum.PAY_TYPE.getCode())));
                    }
                    if (conditionDos.get(0).getBackAmountType().equals(1)) {
                        conditionDTOList.forEach(e -> e.setBackAmountTypeValues(map.get(AgreementMultipleConditionEnum.BACK_AMOUNT_TYPE.getCode())));
                    }
                }

                //如果条件规则类型为梯度
                if (AgreementConditionRuleEnum.GRADIENT.getCode().equals(agreementDO.getConditionRule())) {
                    List<AgreementPolicyDTO> policyDTOS = PojoUtils.map(conditionDos, AgreementPolicyDTO.class);
                    dto.setPolicys(policyDTOS);
                } else {
                    AgreementPolicyDTO policyDTO = PojoUtils.map(conditionDos.get(0), AgreementPolicyDTO.class);
                    dto.setPolicys(ListUtil.toList(policyDTO));
                }
                dto.setAgreementsConditionList(conditionDTOList);

            }
            detailDTOS.add(dto);
        }
        return detailDTOS;
    }

    @Override
    public Integer countAgreementByTimeAndOther(QueryAgreementCountRequest request) {
        QueryWrapper<AgreementDO> queryWrappers = new QueryWrapper();
        //甲方
        queryWrappers.lambda().eq(AgreementDO::getEid, request.getEid())
                .eq(AgreementDO::getSecondEid, request.getSecondEid())
                .eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode())
                .eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                //时间范围
                .and(wrapper -> wrapper.between(AgreementDO::getStartTime, request.getStartTime(), request.getEndTime()).or().between(AgreementDO::getEndTime, request.getStartTime(), request.getEndTime()));
        return count(queryWrappers);
    }

    @Override
    public Map<Long, Long> queryTempAgreementCount(List<Long> agreementIdList, AgreementModeEnum modeEnum) {
        Map<Long, Long> result;
        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
        if (modeEnum != null) {
            queryWrapper.lambda().eq(AgreementDO::getMode, modeEnum.getCode());
        }
        queryWrapper.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
                .in(AgreementDO::getParentId, agreementIdList);

        List<AgreementDO> agreementDOS = list(queryWrapper);
        result = agreementDOS.stream().collect(Collectors.groupingBy(AgreementDO::getParentId, Collectors.counting()));
        return result;
    }

	@Override
	public Map<Long, List<AgreementDTO>> queryTempAgreement(List<Long> agreementIdList, AgreementModeEnum modeEnum) {
		Map<Long, List<AgreementDTO>> result;
		QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
		if (modeEnum != null) {
			queryWrapper.lambda().eq(AgreementDO::getMode, modeEnum.getCode());
		}
		queryWrapper.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
				.in(AgreementDO::getParentId, agreementIdList);

		List<AgreementDO> agreementDOS = list(queryWrapper);
		result = PojoUtils.map(agreementDOS,AgreementDTO.class).stream().collect(Collectors.groupingBy(AgreementDTO::getParentId));
		return result;
	}

	@Override
    public Page<AgreementPageListItemBO> queryAgreementsPageList(QueryAgreementPageListRequest request) {
        Page<AgreementPageListItemBO> result;

        QueryWrapper<AgreementDO> queryWrappers = new QueryWrapper();
        //父协议id列表为空则查询主体协议
        if (CollUtil.isEmpty(request.getParentAgreementIds())) {
            //乙方
            queryWrappers.lambda().eq(AgreementDO::getSecondEid, request.getQueryEid());
            //只查询年度协议
            queryWrappers.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode());
        } else {
            //根据年度协议id查询补充协议
            queryWrappers.lambda().eq(AgreementDO::getMode, AgreementModeEnum.SECOND_AGREEMENTS.getCode())
                    .in(AgreementDO::getParentId, request.getParentAgreementIds());
        }
        //如果传了协议id
        queryWrappers.lambda().eq(ObjectUtil.isNotNull(request.getId()),AgreementDO::getId,request.getId());
        //如果传了主体id
        queryWrappers.lambda().eq(ObjectUtil.isNotNull(request.getEid()),AgreementDO::getEid,request.getEid());
        //如果传了履约开始时间
        queryWrappers.lambda().ge(ObjectUtil.isNotNull(request.getStartTime()),AgreementDO::getStartTime,request.getStartTime());
        //如果传了履约结束时间
        queryWrappers.lambda().le(ObjectUtil.isNotNull(request.getEndTime()),AgreementDO::getEndTime,request.getEndTime());
        //进行中
        if (QueryAgreementStatusEnum.HAVE_IN_HAND.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                    .le(AgreementDO::getStartTime, new Date())
                    .ge(AgreementDO::getEndTime, new Date());
        }
        //未开始
        if (QueryAgreementStatusEnum.NOT_STARTED.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                    .gt(AgreementDO::getStartTime, new Date());
        }
        //已停用
        if (QueryAgreementStatusEnum.OUT_OF_SERVICE.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.CLOSE.getCode());
        }
        //已过期
        if (QueryAgreementStatusEnum.EXPIRED.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                    .lt(AgreementDO::getEndTime, new Date());
        }
        //未开始&进行中
        if (QueryAgreementStatusEnum.HAVE_IN_HAND_NOT_STARTED.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                    .ge(AgreementDO::getEndTime, new Date());
        }
		queryWrappers.lambda().orderByDesc(AgreementDO::getCreateTime);
        Page<AgreementDO> page = this.baseMapper.selectPage(new Page<>(request.getCurrent(), request.getSize()), queryWrappers);
        result = PojoUtils.map(page, AgreementPageListItemBO.class);
        return result;
    }

    @Override
    public Page<AgreementPageListItemBO> querySupplementAgreementsPageListByEids(QueryThirdAgreementPageListRequest request) {
        Page<AgreementPageListItemBO> result;

        QueryWrapper<AgreementDO> queryWrappers = new QueryWrapper();
        //指定乙丙方
        queryWrappers.lambda().and(wrapper -> {
            wrapper.eq(AgreementDO::getSecondEid, request.getQueryEid()).eq(AgreementDO::getThirdEid, request.getEid())
                    .eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode()).eq(AgreementDO::getMode, AgreementModeEnum.THIRD_AGREEMENTS.getCode())
                    .or()
                    .eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode()).eq(AgreementDO::getMode, AgreementModeEnum.THIRD_AGREEMENTS.getCode())
                    .eq(AgreementDO::getSecondEid, request.getEid()).eq(AgreementDO::getThirdEid, request.getQueryEid());
        });

        //进行中
        if (QueryAgreementStatusEnum.HAVE_IN_HAND.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().and(wrapper -> {
                wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                        .le(AgreementDO::getStartTime, new Date())
                        .ge(AgreementDO::getEndTime, new Date());
            });
        }
        //未开始
        if (QueryAgreementStatusEnum.NOT_STARTED.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().and(wrapper -> {
                wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                        .gt(AgreementDO::getStartTime, new Date());
            });
        }
        //已停用
        if (QueryAgreementStatusEnum.OUT_OF_SERVICE.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().and(wrapper -> {
                wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.CLOSE.getCode());
            });
        }
        //已过期
        if (QueryAgreementStatusEnum.EXPIRED.getCode().equals(request.getAgreementStatus())) {
            queryWrappers.lambda().and(wrapper -> {
                wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                        .lt(AgreementDO::getEndTime, new Date());
            });
        }
        Page<AgreementDO> page = this.baseMapper.selectPage(new Page<>(request.getCurrent(), request.getSize()), queryWrappers);
        result = PojoUtils.map(page, AgreementPageListItemBO.class);

        return result;
    }

	@Override
	public List<AgreementPageListItemBO> querySupplementAgreementsByEids(QueryThirdAgreementPageListRequest request) {
		List<AgreementPageListItemBO> result;

		QueryWrapper<AgreementDO> queryWrappers = new QueryWrapper();
		//指定乙丙方
		queryWrappers.lambda().and(wrapper -> {
			wrapper.eq(AgreementDO::getSecondEid, request.getQueryEid()).eq(AgreementDO::getThirdEid, request.getEid())
					.eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode()).eq(AgreementDO::getMode, AgreementModeEnum.THIRD_AGREEMENTS.getCode())
					.or()
					.eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode()).eq(AgreementDO::getMode, AgreementModeEnum.THIRD_AGREEMENTS.getCode())
					.eq(AgreementDO::getSecondEid, request.getEid()).eq(AgreementDO::getThirdEid, request.getQueryEid());
		});

		//进行中
		if (QueryAgreementStatusEnum.HAVE_IN_HAND.getCode().equals(request.getAgreementStatus())) {
			queryWrappers.lambda().and(wrapper -> {
				wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
						.le(AgreementDO::getStartTime, new Date())
						.ge(AgreementDO::getEndTime, new Date());
			});
		}
		//未开始
		if (QueryAgreementStatusEnum.NOT_STARTED.getCode().equals(request.getAgreementStatus())) {
			queryWrappers.lambda().and(wrapper -> {
				wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
						.gt(AgreementDO::getStartTime, new Date());
			});
		}
		//已停用
		if (QueryAgreementStatusEnum.OUT_OF_SERVICE.getCode().equals(request.getAgreementStatus())) {
			queryWrappers.lambda().and(wrapper -> {
				wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.CLOSE.getCode());
			});
		}
		//已过期
		if (QueryAgreementStatusEnum.EXPIRED.getCode().equals(request.getAgreementStatus())) {
			queryWrappers.lambda().and(wrapper -> {
				wrapper.eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
						.lt(AgreementDO::getEndTime, new Date());
			});
		}
		List<AgreementDO> list = this.baseMapper.selectList( queryWrappers);
		result = PojoUtils.map(list, AgreementPageListItemBO.class);

		return result;
	}

	@Override
    public Page<ThirdAgreementEntPageListItemDTO> queryThirdAgreementsEntPageList(QueryThirdAgreementEntListRequest request) {
        //协议角色信息集合
        Set<AgreementRoleBO> agreementRoleBOS = new HashSet<>();

        //查询与queryEid企业 所有有关系的三方协议
        Page<AgreementDO> page = new Page<>(request.getCurrent(), request.getSize());
        Page<AgreementDO> agreementDOSPage = this.baseMapper.queryPageListThirdEntIdList(page, request.getEid());

        for (AgreementDO item : agreementDOSPage.getRecords()) {
            Long secondEid;
            Integer role;
            if (!request.getEid().equals(item.getSecondEid())) {
                //角色肯定的购买方 乙方
                role = AgreementRoleEnum.SECOND.getCode();
                secondEid = item.getSecondEid();
            } else {
                //角色肯定的第三方 丙方
                role = AgreementRoleEnum.THIRD.getCode();
                secondEid = item.getThirdEid();
            }
            AgreementRoleBO agreementRoleBO = new AgreementRoleBO(secondEid, role);

            agreementRoleBOS.add(agreementRoleBO);
        }
        Page<ThirdAgreementEntPageListItemDTO> result = PojoUtils.map(agreementDOSPage, ThirdAgreementEntPageListItemDTO.class);
        List<ThirdAgreementEntPageListItemDTO> listItemDTO = PojoUtils.map(new ArrayList<>(agreementRoleBOS), ThirdAgreementEntPageListItemDTO.class);
        result.setRecords(listItemDTO);
        return result;
    }


    @Override
    public EntThirdAgreementInfoBO statisticsAgreement() {
        //总年度协议个数
        Integer yearCount = count(Wrappers.<AgreementDO>query().lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode()));
        //总临时协议个数
        Integer tempCount = count(Wrappers.<AgreementDO>query().lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode()));
        EntThirdAgreementInfoBO bo = new EntThirdAgreementInfoBO();
        bo.setYearAgreementCount(yearCount);
        bo.setTempAgreementCount(tempCount);
        return bo;
    }

    @Override
    public List<StatisticsAgreementDTO> statisticsAgreementByEid(List<Long> eidList, Long queryEid) {
        List<StatisticsAgreementDTO> result = ListUtil.toList();
        //主协议map
        Map<Long, List<AgreementDO>> mainAgreement;
        //补充协议map
        Map<Long, List<AgreementDO>> supplementAgreement;
        //三方协议map
        Map<Long, List<AgreementDO>> supplementThirdAgreement;
        Map<Long, List<AgreementDO>> supplementSecondAgreement;
        //所有的三方协议
        List<AgreementDO> thirdList;

        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
        //是否指定了queryEid
        if (ObjectUtil.isNull(queryEid)) {
            queryWrapper.lambda()
                    .and(wrapper -> {
                        wrapper.in(AgreementDO::getThirdEid, eidList).or().in(AgreementDO::getSecondEid, eidList);
                    });
//			queryWrapper.lambda().in(AgreementDO::getThirdEid, eidList);
        } else {
            queryWrapper.lambda()
                    .and(wrapper -> {
                        wrapper.in(AgreementDO::getThirdEid, eidList).eq(AgreementDO::getSecondEid, queryEid)
                                .or()
                                .in(AgreementDO::getSecondEid, eidList).eq(AgreementDO::getThirdEid, queryEid);
                    });
        }

        List<AgreementDO> list = this.baseMapper.selectList(queryWrapper);
        //生成主协议map
        mainAgreement = list.stream().filter(e -> AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(e.getCategory()))
                .collect(Collectors.groupingBy(AgreementDO::getSecondEid));
        //生成补充协议-双方协议map
//		supplementAgreement = list.stream().filter(e -> AgreementModeEnum.SECOND_AGREEMENTS.getCode()
//				.equals(e.getMode()) && AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(e.getCategory()))
//				.collect(Collectors.groupingBy(AgreementDO::getSecondEid));
        supplementAgreement = list.stream().filter(e -> AgreementModeEnum.SECOND_AGREEMENTS.getCode()
                .equals(e.getMode()) && AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(e.getCategory()))
                .collect(Collectors.groupingBy(AgreementDO::getThirdEid));
        //生成三方协议map
        supplementThirdAgreement = list.stream().filter(e -> AgreementModeEnum.THIRD_AGREEMENTS.getCode().equals(e.getMode()))
                .collect(Collectors.groupingBy(AgreementDO::getThirdEid));
        supplementSecondAgreement = list.stream().filter(e -> AgreementModeEnum.THIRD_AGREEMENTS.getCode().equals(e.getMode()))
                .collect(Collectors.groupingBy(AgreementDO::getSecondEid));

        thirdList = list.stream().filter(e -> AgreementModeEnum.THIRD_AGREEMENTS.getCode().equals(e.getMode())).collect(Collectors.toList());

        eidList.forEach(eid -> {
            StatisticsAgreementDTO bo = new StatisticsAgreementDTO();
            bo.setEid(eid);
            //年度协议个数
            if (mainAgreement.containsKey(eid)) {
                bo.setYearAgreementList(mainAgreement.get(eid).stream().map(AgreementDO::getId).collect(Collectors.toList()));
            } else {
                bo.setYearAgreementList(ListUtil.toList());
            }
            //补充协议-双方协议个数
            if (supplementAgreement.containsKey(eid)) {
                bo.setTempAgreementList(supplementAgreement.get(eid).stream().map(AgreementDO::getId).collect(Collectors.toList()));
            } else {
                bo.setTempAgreementList(ListUtil.toList());
            }
            //补充协议-三方协议个数
            List<Long> thirdAgreementsList = ListUtil.toList();
            if (supplementSecondAgreement.containsKey(eid)) {
                thirdAgreementsList.addAll(supplementSecondAgreement.get(eid).stream().map(AgreementDO::getId).collect(Collectors.toList()));
            }
            if (supplementThirdAgreement.containsKey(eid)) {
                thirdAgreementsList.addAll(supplementThirdAgreement.get(eid).stream().map(AgreementDO::getId).collect(Collectors.toList()));
            }
            thirdAgreementsList = thirdAgreementsList.stream().distinct().collect(Collectors.toList());
            bo.setThirdAgreementsList(thirdAgreementsList);
            //三方签订家数(只有queryEid为null时才统计三方协议签订家数)
            Set<Long> eids = new HashSet<>();
            if (ObjectUtil.isNull(queryEid)) {
                List<AgreementDO> dos = thirdList.stream().filter(e -> e.getSecondEid().equals(eid) || e.getThirdEid().equals(eid)).collect(Collectors.toList());
                for (AgreementDO item : dos) {
                    //当前eid是协议中的乙方则取丙方eid
                    if (eid.equals(item.getSecondEid())) {
                        eids.add(item.getThirdEid());
                    } else {
                        eids.add(item.getSecondEid());
                    }
                }
            }
            bo.setThirdAgreementsCustomerList(new ArrayList<>(eids));

            result.add(bo);
        });

        return result;
    }
//	@Override
//	public StatisticsAgreementBO statisticsAgreementByEid(Long eid) {
//		StatisticsAgreementBO bo = new StatisticsAgreementBO();
//		//第三方供应商
//		QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
//		queryWrapper.lambda()
//				.eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
//				.eq(AgreementDO::getMode, AgreementModeEnum.THIRD_AGREEMENTS.getCode())
//				.and(wrapper -> wrapper.eq(AgreementDO::getThirdEid, eid).or().eq(AgreementDO::getSecondEid, eid));
//		List<AgreementDO> list = this.baseMapper.selectList(queryWrapper);
//		Set<Long> eids = new HashSet<>();
//		for (AgreementDO item : list) {
//			if (!eid.equals(item.getSecondEid())) {
//				eids.add(item.getSecondEid());
//			} else {
//				eids.add(item.getThirdEid());
//			}
//		}
//		bo.setThirdAgreementsCustomerList(new ArrayList<>(eids));
//
//		//主协议个数
//		QueryWrapper<AgreementDO> yearWrapper = new QueryWrapper<>();
//		yearWrapper.lambda()
//				.eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode())
//				.and(wrapper -> wrapper.eq(AgreementDO::getThirdEid, eid).or().eq(AgreementDO::getSecondEid, eid));
//		List<AgreementDO> yearList = this.baseMapper.selectList(yearWrapper);
//		Set<Long> yearIds = new HashSet<>();
//		for (AgreementDO item : yearList) {
//			yearIds.add(item.getId());
//		}
//		bo.setYearAgreementList(new ArrayList<>(yearIds));
//		//临时协议个数
//		QueryWrapper<AgreementDO> tempWrapper = new QueryWrapper<>();
//		tempWrapper.lambda()
//				.eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
//				.and(wrapper -> wrapper.eq(AgreementDO::getThirdEid, eid).or().eq(AgreementDO::getSecondEid, eid));
//		List<AgreementDO> tempList = this.baseMapper.selectList(tempWrapper);
//		Set<Long> tempIds = new HashSet<>();
//		for (AgreementDO item : tempList) {
//			tempIds.add(item.getId());
//		}
//		bo.setTempAgreementList(new ArrayList<>(tempIds));
//		return bo;
//	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean agreementClose(CloseAgreementRequest request) {
        AgreementDO agreementDO = getById(request.getAgreementId());
        if (agreementDO == null) {
            return Boolean.FALSE;
        }
        //判断状态是否启用
        if (!QueryAgreementStatusEnum.HAVE_IN_HAND.getCode().equals(agreementDO.getStatus())) {
            return Boolean.FALSE;
        }
        //停用
        if (AgreementCloseEnum.CLOSE.getCode().equals(request.getOpType())) {
            //进行中的才能停用
            if (agreementDO.getStartTime().compareTo(new Date()) < 0 && agreementDO.getEndTime().compareTo(new Date()) > 0) {
                //临时协议
                if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(agreementDO.getCategory())) {
                    agreementDO.setStatus(AgreementStatusEnum.CLOSE.getCode());
                    agreementDO.setOpUserId(request.getOpUserId());
                    agreementDO.setStopTime(new Date());
                    updateById(agreementDO);
                    return tempAgreementStop(agreementDO.getId(), agreementDO.getSecondEid(), agreementDO.getThirdEid(), new Date());

                }
                //年度协议
                if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(agreementDO.getCategory())) {
                    agreementDO.setStatus(AgreementStatusEnum.CLOSE.getCode());
                    agreementDO.setOpUserId(request.getOpUserId());
                    agreementDO.setStopTime(new Date());
                    updateById(agreementDO);
                    boolean update = yearAgreementStop(agreementDO);
                    if (update) {
                        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(AgreementDO::getParentId, agreementDO.getId());
                        List<AgreementDO> list = list(queryWrapper);
                        //停用年度协议下的补充协议
                        list.forEach(e->{
                            e.setStatus(AgreementStatusEnum.CLOSE.getCode());
                            e.setOpUserId(request.getOpUserId());
                            e.setStopTime(new Date());
                            boolean var = updateById(e);
                            if (!var){
                                log.error("停用年度协议下的补充协议失败，参数={}",e);
                                throw new ServiceException(ResultCode.FAILED);
                            }
                            var = tempAgreementStop(e.getId(), e.getSecondEid(), e.getThirdEid(), new Date());
                            if (!var){
                                log.error("停用年度协议下的补充协议失败，参数={}",e);
                                throw new ServiceException(ResultCode.FAILED);
                            }
                        });
                        
//                        AgreementDO updateData = new AgreementDO();
//                        updateData.setOpUserId(request.getOpUserId());
//                        updateData.setStopTime(new Date());
//                        updateData.setStatus(AgreementStatusEnum.CLOSE.getCode());
//                        update(updateData, queryWrapper);
                        return Boolean.TRUE;
                    }
                }
            } else {
                throw new BusinessException(AgreementErrorCode.AGREEMENT_STOP);
            }
        }
        //删除
        if (AgreementCloseEnum.DELETE.getCode().equals(request.getOpType())) {
            //未开始的才能删除
            if (agreementDO.getStartTime().compareTo(new Date()) > 0) {
                //临时协议
                if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(agreementDO.getCategory())) {
                    agreementDO.setOpUserId(request.getOpUserId());
                    int row = deleteByIdWithFill(agreementDO);
                    if (row > 0) {
                        return Boolean.TRUE;
                    } else {
                        throw new BusinessException(AgreementErrorCode.AGREEMENT_DELETE_FAILED);
                    }
                }
                //年度协议
                if (AgreementCategoryEnum.YEAR_AGREEMENT.getCode().equals(agreementDO.getCategory())) {
                    agreementDO.setOpUserId(request.getOpUserId());
                    int row = deleteByIdWithFill(agreementDO);
                    if (row > 0) {
                        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(AgreementDO::getParentId, agreementDO.getId());
                        AgreementDO updateData = new AgreementDO();
                        updateData.setOpUserId(request.getOpUserId());
                        batchDeleteWithFill(updateData, queryWrapper);
                        return Boolean.TRUE;
                    } else {
                        throw new BusinessException(AgreementErrorCode.AGREEMENT_DELETE_FAILED);
                    }
                }
            } else {
                throw new BusinessException(AgreementErrorCode.AGREEMENT_DELETE_INVALIDATE);
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public YearAgreementDetailDTO queryYearAgreementsDetail(Long id) {
        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgreementDO::getId, id)
                .eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode());
        AgreementDO agreementDO = this.getOne(queryWrapper);
        if (agreementDO == null) {
            return null;
        }
        YearAgreementDetailDTO dto = PojoUtils.map(agreementDO, YearAgreementDetailDTO.class);
        //设置补充协议数量
        Map<Long, Long> tempAgreementCount = this.queryTempAgreementCount(ListUtil.toList(id), AgreementModeEnum.SECOND_AGREEMENTS);
        dto.setSupplementAgreementNum(tempAgreementCount.getOrDefault(id, 0L).intValue());
        return dto;
    }

    @Override
    public List<AgreementDTO> queryAgreementList(List<Long> eidList, AgreementModeEnum mode, AgreementCategoryEnum agreementCategoryEnum) {
        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();

        if (mode != null) {
            queryWrapper.lambda().eq(AgreementDO::getMode, mode.getCode());
        }
        if (agreementCategoryEnum != null) {
            queryWrapper.lambda().eq(AgreementDO::getCategory, agreementCategoryEnum.getCode());
        }
//        //双方协议
//        if (AgreementModeEnum.SECOND_AGREEMENTS.equals(mode)) {
//            queryWrapper.lambda().in(AgreementDO::getSecondEid, eidList);
//        } else if (AgreementModeEnum.THIRD_AGREEMENTS.equals(mode) || mode == null) {
//            //三方协议
//            queryWrapper.lambda().and(wrapper -> wrapper.in(AgreementDO::getThirdEid, eidList).or().in(AgreementDO::getSecondEid, eidList));
//        }
        queryWrapper.lambda().and(wrapper -> wrapper.in(AgreementDO::getThirdEid, eidList).or().in(AgreementDO::getSecondEid, eidList));
//		queryWrapper.lambda().in(AgreementDO::getThirdEid, eidList);
        List<AgreementDO> agreementList = list(queryWrapper);
        return PojoUtils.map(agreementList, AgreementDTO.class);
    }

    @Override
    public List<AgreementDTO> queryAgreementListByBuyerEid(Long buyerEid) {
        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
                .eq(AgreementDO::getRebateCycle, 2)
                .eq(AgreementDO::getThirdEid, buyerEid);

        List<AgreementDO> agreementList = list(queryWrapper);
        return PojoUtils.map(agreementList, AgreementDTO.class);
    }

    @Override
    public List<SupplementAgreementDetailDTO> queryStartSupplementAgreementsDetail() {
        QueryWrapper<AgreementDO> queryWrappers = new QueryWrapper();
        queryWrappers.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode());
//        queryWrappers.lambda().eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode());
        queryWrappers.lambda().eq(AgreementDO::getRebateCycle, AgreementRebateCycleEnum.REBATE_POOL.getCode());
        queryWrappers.lambda().le(AgreementDO::getStartTime, new Date());

        List<AgreementDO> list = list(queryWrappers);
        List<Long> ids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        return this.querySupplementAgreementsDetailList(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAgreement(UpdateAgreementRequest request) {
        Boolean result = Boolean.FALSE;
        //协议
        AgreementDO agreement;
        //查询协议下的条件
        List<AgreementConditionDO> conditionList;
        //协议多选条件
        Map<String, List<Integer>> multipleCondition;
        //协议下商品
        List<AgreementGoodsDTO> goodsList;

        //打快照协议、条件、多选
        //查询协议、条件
        QueryWrapper<AgreementDO> agreementWrapper = new QueryWrapper<>();
        agreementWrapper.lambda().eq(AgreementDO::getId, request.getId());

        agreement = getOne(agreementWrapper);
        //查询协议下的条件
        conditionList = agreementConditionService.getAgreementConditionListByAgreementId(ListUtil
                .toList(request.getId())).get(request.getId());
        //查询多选条件
        multipleCondition = agreementMultipleConditionService.getConditionValueByAgreementId(request.getId());
        //查询协议商品
        goodsList = agreementGoodsService.getAgreementGoodsAgreementIdByList(request.getId());

        //更新协议、条件--修改年度协议必然不会进入此处
        if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(request.getCategory()) && CollUtil.isNotEmpty(request.getConditionFormList())) {
            List<AgreementConditionDO> updateConditionList = PojoUtils.map(request.getConditionFormList(), AgreementConditionDO.class);
            result = agreementConditionService.updateBatchById(updateConditionList);
            //如果条件更新失败
            if (!result) {
                throw new BusinessException(AgreementErrorCode.AGREEMENT_UPDATE);
            }
        }
        //开始更新协议主表
        AgreementDO updateAgreement = new AgreementDO();
        updateAgreement.setId(request.getId());
        updateAgreement.setRemark(request.getRemark());
        updateAgreement.setOpUserId(request.getOpUserId());
        //只有修改了条件才进行快照
        if (result.equals(Boolean.TRUE)) {
            //增加版本
            Integer version = agreement.getVersion();
            //如果有更新条件
            updateAgreement.setVersion(version + 1);
            //协议快照
            agreementSnapshot(agreement, conditionList, multipleCondition, goodsList, request.getOpUserId());
            //重新计算
            ClearAgreementConditionCalculateRequest clearRequest=new ClearAgreementConditionCalculateRequest();
            clearRequest.setAgreementIds(Arrays.asList(agreement.getId()));
            clearRequest.setOpUserId(request.getOpUserId());
            agreementRebateOrderService.clearDiscountAmountByOrderIdsAndAgreementIds(clearRequest);
        }
        if (StrUtil.isNotBlank(request.getName())) {
            updateAgreement.setName(request.getName());
        }
        if (StrUtil.isNotBlank(request.getContent())) {
            updateAgreement.setContent(request.getContent());
        }
        result = updateById(updateAgreement);
        return result;
    }

    @Override
    public Boolean syncAgreementRelation() {
        QueryWrapper<AgreementDO> tempQueryWrappers = new QueryWrapper();
        Date dateTime = new Date();
        //补充
        tempQueryWrappers.lambda()
                .eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
                .eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                .lt(AgreementDO::getEndTime, dateTime)
                .eq(AgreementDO::getAgreementStatus, 0);

        List<AgreementDO> tempList = this.list(tempQueryWrappers);
        //循环所有的协议
        for (AgreementDO agreementDO : tempList) {
            tempAgreementStop(agreementDO.getId(), agreementDO.getSecondEid(), agreementDO.getThirdEid(), dateTime);
        }

        //年度
        QueryWrapper<AgreementDO> yearQueryWrappers = new QueryWrapper();
        yearQueryWrappers.lambda().eq(AgreementDO::getCategory, AgreementCategoryEnum.YEAR_AGREEMENT.getCode())
                .eq(AgreementDO::getStatus, AgreementStatusEnum.OPEN.getCode())
                .lt(AgreementDO::getEndTime, dateTime)
                .eq(AgreementDO::getAgreementStatus, 0);
        List<AgreementDO> yearList = this.list(yearQueryWrappers);
        for (AgreementDO agreementDO : yearList) {
            //添加商品和渠道商的关系
            yearAgreementStop(agreementDO);
        }
        return true;
    }

	@Override
	public Page<EntPageListItemDTO> queryEntPageList(QueryEntPageListRequest request) {
		return this.baseMapper.queryEntPageList(request.getPage(),request);
	}

	public Boolean yearAgreementStop(AgreementDO agreementDO) {
        //移除采购商品的权限
        List<AgreementGoodsDTO> agreementGoodsList = agreementGoodsService.getAgreementGoodsAgreementIdByList(agreementDO.getId());
        List<Long> goodsIds = agreementGoodsList.stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());
        agreementGoodsPurchaseRelationService.deleteBuyerGatherByGid(goodsIds, agreementDO.getSecondEid(), 0L);
        AgreementDO agreement = new AgreementDO();
        agreement.setAgreementStatus(1);
        agreement.setId(agreementDO.getId());
        agreement.setOpUserId(agreementDO.getOpUserId());

        return this.updateById(agreement);
    }

    public Boolean tempAgreementStop(Long agreementId, Long secondEid, Long thirdEid, Date dateTime) {
        //解除配送商的关系
        QueryWrapper<AgreementDO> wrappers = new QueryWrapper();
        wrappers.lambda().eq(AgreementDO::getSecondEid, secondEid)
                .eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
                .eq(AgreementDO::getStatus,AgreementStatusEnum.OPEN.getCode())
                .le(AgreementDO::getStartTime, dateTime)
                .ge(AgreementDO::getEndTime, dateTime)
                .eq(AgreementDO::getThirdEid, thirdEid);
        if (CollUtil.isEmpty(this.list(wrappers))) {
            RemovePurchaseRelationFormRequest removePurchaseRelationFormRequest = new RemovePurchaseRelationFormRequest();
            removePurchaseRelationFormRequest.setBuyerId(thirdEid);
            removePurchaseRelationFormRequest.setSellerIds(Arrays.asList(secondEid));
            enterprisePurchaseRelationService.removePurchaseRelation(removePurchaseRelationFormRequest);
        }
        AgreementDO agreement = new AgreementDO();
        agreement.setAgreementStatus(1);
        agreement.setId(agreementId);
        return this.updateById(agreement);
    }

    @Override
    public List<AgreementDTO> queryAgreementListByThirdEid(Long thirdEid) {
        QueryWrapper<AgreementDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AgreementDO::getCategory, AgreementCategoryEnum.TEMP_AGREEMENT.getCode())
                .eq(AgreementDO::getThirdEid, thirdEid)
                .eq(AgreementDO::getStatus, 1)
                .eq(AgreementDO::getMigrateStatus,0)
                .le(AgreementDO::getEndTime, DateUtil.endOfYear(new Date()))
                .ge(AgreementDO::getEndTime, new Date())
                .ge(AgreementDO::getStartTime, DateUtil.beginOfYear(new Date()));

        List<AgreementDO> agreementList = list(queryWrapper);
        return PojoUtils.map(agreementList, AgreementDTO.class);
    }

    @Override
    public List<Long> queryEidByMigrate() {
        return baseMapper.queryEidByMigrate();
    }

    @Override
    public Boolean updateMigrateStatus(List<AgreementDO> agreementDOS) {
        if (CollUtil.isEmpty(agreementDOS)){
            return false;
        }
        return updateBatchById(agreementDOS);
    }

    /**
     * 根据主体协议id对协议表、协议条件、多选条件进行快照
     *
     * @param agreement
     * @param conditionList
     * @param multipleCondition
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean agreementSnapshot(AgreementDO agreement, List<AgreementConditionDO> conditionList, Map<String, List<Integer>> multipleCondition, List<AgreementGoodsDTO> goodsList, Long opUser) {
        Boolean result;
        //插入协议
        AgreementSnapshotDO agreementSnapshotDO = PojoUtils.map(agreement, AgreementSnapshotDO.class);
        agreementSnapshotDO.setAgreementId(agreement.getId());
        agreementSnapshotDO.setOpUserId(opUser);
        result = agreementSnapshotService.save(agreementSnapshotDO);
        //插入条件
        if (!result) {
            return result;
        }
        List<AgreementConditionSnapshotDO> conditions = PojoUtils.map(conditionList, AgreementConditionSnapshotDO.class);
        conditions.forEach(e -> {
            e.setVersion(agreement.getVersion());
            e.setAgreementId(agreement.getId());
            e.setOpUserId(opUser);
        });
        result = agreementConditionSnapshotService.saveBatch(conditions);
        //年度协议没有条件
        if (AgreementCategoryEnum.TEMP_AGREEMENT.getCode().equals(agreement.getCategory()) && !result) {
            log.error("协议更新时打条件快照失败");
            throw new BusinessException(AgreementErrorCode.AGREEMENT_UPDATE);
        }
        //插入多选条件
        List<AgreementMultipleConditionSnapshotDO> addMultipleCondition = ListUtil.toList();
        Set<String> keySet = multipleCondition.keySet();
        keySet.forEach(s -> {
            //多选条件
            List<Integer> options = multipleCondition.get(s);
            for (Integer item : options) {
                AgreementMultipleConditionSnapshotDO multipleConditionSnapshotDO = new AgreementMultipleConditionSnapshotDO();
                multipleConditionSnapshotDO.setAgreementId(agreement.getId());
                multipleConditionSnapshotDO.setVersion(agreement.getVersion());
                multipleConditionSnapshotDO.setConditionType(s);
                multipleConditionSnapshotDO.setConditionValue(item);
                multipleConditionSnapshotDO.setOpUserId(opUser);
                addMultipleCondition.add(multipleConditionSnapshotDO);
            }
        });
        result = agreementMultipleConditionSnapshotService.saveBatch(addMultipleCondition);
        //年度协议没有多选条件,或者协议没有多选条件
        if (CollUtil.isNotEmpty(addMultipleCondition) && !result) {
            log.error("协议更新时打多选项快照失败");
            throw new BusinessException(AgreementErrorCode.AGREEMENT_UPDATE);
        }
        List<GoodsSnapshotDO> goodsSnapshotDOList = PojoUtils.map(goodsList, GoodsSnapshotDO.class);
        goodsSnapshotDOList.forEach(e -> {
            e.setVersion(agreement.getVersion());
            e.setOpUserId(opUser);
        });
        result = goodsSnapshotService.saveBatch(goodsSnapshotDOList);
        //是否更新成功
        if (CollUtil.isNotEmpty(goodsSnapshotDOList) && !result) {
            log.error("协议更新时打多选项快照失败");
            throw new BusinessException(AgreementErrorCode.AGREEMENT_UPDATE);
        }
        return result;
    }

}
