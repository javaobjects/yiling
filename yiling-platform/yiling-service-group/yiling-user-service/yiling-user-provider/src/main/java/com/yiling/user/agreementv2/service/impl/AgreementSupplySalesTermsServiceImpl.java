package com.yiling.user.agreementv2.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.bo.AgreementControlAreaBO;
import com.yiling.user.agreementv2.bo.AgreementSupplySalesEnterpriseBO;
import com.yiling.user.agreementv2.bo.AgreementSupplySalesGoodsGroupBO;
import com.yiling.user.agreementv2.bo.AgreementSupplySalesGoodsProBO;
import com.yiling.user.agreementv2.bo.AgreementSupplySalesTermsBO;
import com.yiling.user.agreementv2.dto.AgreementControlAreaDTO;
import com.yiling.user.agreementv2.dto.AgreementControlCustomerTypeDTO;
import com.yiling.user.agreementv2.dto.AgreementControlDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesEnterpriseDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesTermsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementControlAreaRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsGroupRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesGoodsRequest;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesTermsRequest;
import com.yiling.user.agreementv2.entity.AgreementControlCustomerTypeDO;
import com.yiling.user.agreementv2.entity.AgreementControlDO;
import com.yiling.user.agreementv2.entity.AgreementMainTermsDO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesEnterpriseDO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsDO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsGroupDO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesTermsDO;
import com.yiling.user.agreementv2.dao.AgreementSupplySalesTermsMapper;
import com.yiling.user.agreementv2.enums.AgreementBuyChannelEnum;
import com.yiling.user.agreementv2.enums.AgreementControlSaleConditionEnum;
import com.yiling.user.agreementv2.enums.AgreementControlSaleTypeEnum;
import com.yiling.user.agreementv2.enums.AgreementFirstTypeEnum;
import com.yiling.user.agreementv2.service.AgreementControlAreaService;
import com.yiling.user.agreementv2.service.AgreementControlCustomerTypeService;
import com.yiling.user.agreementv2.service.AgreementControlService;
import com.yiling.user.agreementv2.service.AgreementMainTermsService;
import com.yiling.user.agreementv2.service.AgreementManufacturerGoodsService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesEnterpriseService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsGroupService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesTermsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.common.UserErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议供销条款表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Slf4j
@Service
public class AgreementSupplySalesTermsServiceImpl extends BaseServiceImpl<AgreementSupplySalesTermsMapper, AgreementSupplySalesTermsDO> implements AgreementSupplySalesTermsService {

    @Autowired
    private AgreementSupplySalesEnterpriseService agreementSupplySalesEnterpriseService;
    @Autowired
    private AgreementSupplySalesGoodsService agreementSupplySalesGoodsService;
    @Autowired
    private AgreementControlService agreementControlService;
    @Autowired
    private AgreementControlCustomerTypeService agreementControlCustomerTypeService;
    @Autowired
    private AgreementControlAreaService agreementControlAreaService;
    @Autowired
    private AgreementSupplySalesGoodsGroupService agreementSupplySalesGoodsGroupService;
    @Autowired
    private AgreementMainTermsService agreementMainTermsService;
    @Autowired
    private AgreementManufacturerGoodsService manufacturerGoodsService;

    /**
     * 最大商品组数量
     */
    private static final int MAX_PRODUCT_GROUP_NUM = 6;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAgreementSupplySalesTerms(AddAgreementSupplySalesTermsRequest request) {
        if (request.getAllLevelKindsFlag() == 1) {
            request.setSupplySalesGoodsGroupList(null);
        }
        // 协议供销条款
        AgreementSupplySalesTermsDO agreementSupplySalesTermsDo = PojoUtils.map(request, AgreementSupplySalesTermsDO.class);
        this.save(agreementSupplySalesTermsDo);

        // 协议供销指定商业公司
        List<AgreementSupplySalesEnterpriseDO> salesEnterpriseDoList = PojoUtils.map(request.getSupplySalesEnterpriseList(), AgreementSupplySalesEnterpriseDO.class);
        salesEnterpriseDoList.forEach(agreementSupplySalesEnterpriseDO -> agreementSupplySalesEnterpriseDO.setAgreementId(request.getAgreementId()));
        agreementSupplySalesEnterpriseService.saveBatch(salesEnterpriseDoList);

        // 协议供销商品组
        List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList = request.getSupplySalesGoodsGroupList();
        setAgreementSupplySaleGroups(request, supplySalesGoodsGroupList);

        return true;
    }

    @Override
    public AgreementSupplySalesTermsDTO getSalesTermsByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementSupplySalesTermsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesTermsDO::getAgreementId, agreementId);
        return PojoUtils.map(this.getOne(wrapper), AgreementSupplySalesTermsDTO.class);
    }

    @Override
    public AgreementSupplySalesTermsBO getSupplySalesTerms(Long agreementId) {
        long time = System.currentTimeMillis();
        // 协议供销条款设置
        AgreementSupplySalesTermsDTO supplySalesTermsDTO = this.getSalesTermsByAgreementId(agreementId);
        AgreementSupplySalesTermsBO supplySalesTermsBO = PojoUtils.map(supplySalesTermsDTO, AgreementSupplySalesTermsBO.class);
        // 指定商业公司
        List<AgreementSupplySalesEnterpriseDTO> supplySalesEnterpriseDTOList = agreementSupplySalesEnterpriseService.getByAgreementId(agreementId);
        List<AgreementSupplySalesEnterpriseBO> supplySalesEnterpriseBOList = PojoUtils.map(supplySalesEnterpriseDTOList, AgreementSupplySalesEnterpriseBO.class);
        supplySalesTermsBO.setSupplySalesEnterpriseList(supplySalesEnterpriseBOList);

        if (supplySalesTermsDTO.getAllLevelKindsFlag() == 0) {
            // 供销商品组
            List<AgreementSupplySalesGoodsGroupBO> supplySalesGoodsGroupList = ListUtil.toList();
            // 供销商品组
            List<AgreementSupplySalesGoodsGroupDO> supplySalesGoodsGroupDOList = agreementSupplySalesGoodsGroupService.getByAgreementId(agreementId);
            supplySalesGoodsGroupDOList.forEach(agreementSupplySalesGoodsGroupDO -> {

                AgreementSupplySalesGoodsGroupBO supplySalesGoodsGroupBO = PojoUtils.map(agreementSupplySalesGoodsGroupDO, AgreementSupplySalesGoodsGroupBO.class);
                supplySalesGoodsGroupBO.setExitWarehouseTaxPriceFlag(agreementSupplySalesGoodsGroupDO.getExitWarehouseTaxPriceFlag() == 1);
                supplySalesGoodsGroupBO.setRetailTaxPriceFlag(agreementSupplySalesGoodsGroupDO.getRetailTaxPriceFlag() == 1);

                Long goodsGroupId = agreementSupplySalesGoodsGroupDO.getId();
                // 供销商品
                List<AgreementSupplySalesGoodsDTO> supplyGoodsList = agreementSupplySalesGoodsService.getSupplyGoodsList(goodsGroupId);
                List<AgreementSupplySalesGoodsProBO> supplySalesGoodsProBOList = supplyGoodsList.stream().map(agreementSupplySalesGoodsDTO -> {
                    AgreementSupplySalesGoodsProBO supplySalesGoodsProBO = PojoUtils.map(agreementSupplySalesGoodsDTO, AgreementSupplySalesGoodsProBO.class);
                    supplySalesGoodsProBO.setExclusiveFlag(agreementSupplySalesGoodsDTO.getExclusiveFlag() == 1);
                    return supplySalesGoodsProBO;
                }).collect(Collectors.toList());
                supplySalesGoodsGroupBO.setSupplySalesGoodsList(supplySalesGoodsProBOList);

                if (AgreementControlSaleTypeEnum.NONE != AgreementControlSaleTypeEnum.getByCode(agreementSupplySalesGoodsGroupDO.getControlSaleType())) {
                    // 控销条件
                    List<AgreementControlDTO> controlList = agreementControlService.getControlList(goodsGroupId);
                    List<Integer> controlConditionList = controlList.stream().map(AgreementControlDTO::getControlSaleCondition).collect(Collectors.toList());
                    supplySalesGoodsGroupBO.setAgreementControlList(controlConditionList);

                    if (controlConditionList.contains(AgreementControlSaleConditionEnum.AREA.getCode())) {
                        // 控销区域
                        AgreementControlAreaDTO controlAreaDto = agreementControlAreaService.getControlArea(goodsGroupId);
                        supplySalesGoodsGroupBO.setControlArea(PojoUtils.map(controlAreaDto, AgreementControlAreaBO.class));
                    }
                    if (controlConditionList.contains(AgreementControlSaleConditionEnum.CUSTOMER_TYPE.getCode())) {
                        // 控销客户类型
                        List<AgreementControlCustomerTypeDTO> controlCustomerTypeList = agreementControlCustomerTypeService.getControlCustomerTypeList(goodsGroupId);
                        List<Integer> customerTypeList = controlCustomerTypeList.stream().map(AgreementControlCustomerTypeDTO::getCustomerType).collect(Collectors.toList());
                        supplySalesGoodsGroupBO.setControlCustomerTypeList(customerTypeList);
                    }

                }
                supplySalesGoodsGroupList.add(supplySalesGoodsGroupBO);

            });
            supplySalesTermsBO.setSupplySalesGoodsGroupList(supplySalesGoodsGroupList);
        }
        log.debug("获取协议供销条款共耗时：{}", System.currentTimeMillis() - time);

        return supplySalesTermsBO;
    }


    /**
     * 设置协议供销商品组
     *
     * @param request 请求参数
     * @param supplySalesGoodsGroupList 商品组集合
     */
    private void setAgreementSupplySaleGroups(AddAgreementSupplySalesTermsRequest request, List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList) {
        if (CollUtil.isEmpty(supplySalesGoodsGroupList)) {
            return;
        }
        // 初始化数据，方便批量操作
        List<AgreementSupplySalesGoodsDO> allSupplySalesGoodsDoList = ListUtil.toList();
        List<AgreementControlDO> allAgreementControlDoList = ListUtil.toList();
        List<AgreementControlCustomerTypeDO> allControlCustomerTypeDoList = ListUtil.toList();
        List<AddAgreementControlAreaRequest> allControlAreaList = ListUtil.toList();
        // 对每一个控销商品组进行处理
        for (AddAgreementSupplySalesGoodsGroupRequest supplySalesGoodsGroupRequest : supplySalesGoodsGroupList) {

            // 创建供销商品组对象
            AgreementSupplySalesGoodsGroupDO supplySalesGoodsGroupDO = new AgreementSupplySalesGoodsGroupDO();
            supplySalesGoodsGroupDO.setAgreementId(request.getAgreementId());
            supplySalesGoodsGroupDO.setControlSaleType(supplySalesGoodsGroupRequest.getControlSaleType());
            supplySalesGoodsGroupDO.setExitWarehouseTaxPriceFlag(supplySalesGoodsGroupRequest.getExitWarehouseTaxPriceFlag() ? 1 : 0);
            supplySalesGoodsGroupDO.setRetailTaxPriceFlag(supplySalesGoodsGroupRequest.getRetailTaxPriceFlag() ? 1 : 0);
            supplySalesGoodsGroupDO.setOpUserId(request.getOpUserId());
            agreementSupplySalesGoodsGroupService.save(supplySalesGoodsGroupDO);
            // 供销商品组ID
            Long groupId = supplySalesGoodsGroupDO.getId();

            // 控销商品
            List<AddAgreementSupplySalesGoodsRequest> supplySalesGoodsRequestList = supplySalesGoodsGroupRequest.getSupplySalesGoodsList();
            supplySalesGoodsRequestList.forEach(addAgreementSupplySalesGoodsRequest -> {
                AgreementSupplySalesGoodsDO agreementSupplySalesGoodsDO = PojoUtils.map(addAgreementSupplySalesGoodsRequest, AgreementSupplySalesGoodsDO.class);
                agreementSupplySalesGoodsDO.setAgreementId(request.getAgreementId());
                agreementSupplySalesGoodsDO.setControlGoodsGroupId(groupId);
                agreementSupplySalesGoodsDO.setExclusiveFlag(addAgreementSupplySalesGoodsRequest.getExclusiveFlag() ? 1 : 0);
                agreementSupplySalesGoodsDO.setOpUserId(request.getOpUserId());
                allSupplySalesGoodsDoList.add(agreementSupplySalesGoodsDO);
            });

            // 只有黑名单或白名单才需要处理 控销区域或控销客户类型
            if (AgreementControlSaleTypeEnum.BLACKLIST == AgreementControlSaleTypeEnum.getByCode(supplySalesGoodsGroupRequest.getControlSaleType())
                    || AgreementControlSaleTypeEnum.WHITELIST == AgreementControlSaleTypeEnum.getByCode(supplySalesGoodsGroupRequest.getControlSaleType())) {

                // 控销条件：有值才处理后续的区域和客户类型
                List<Integer> agreementControlList = supplySalesGoodsGroupRequest.getAgreementControlList();
                if (CollUtil.isNotEmpty(agreementControlList)) {
                    for (Integer controlSaleCondition : agreementControlList) {
                        AgreementControlDO agreementControlDO = new AgreementControlDO();
                        agreementControlDO.setControlSaleCondition(controlSaleCondition);
                        agreementControlDO.setAgreementId(request.getAgreementId());
                        agreementControlDO.setControlGoodsGroupId(groupId);
                        agreementControlDO.setOpUserId(request.getOpUserId());
                        allAgreementControlDoList.add(agreementControlDO);
                    }

                    // 控销区域
                    if (Objects.nonNull(supplySalesGoodsGroupRequest.getControlArea())) {
                        AddAgreementControlAreaRequest controlAreaRequest = PojoUtils.map(supplySalesGoodsGroupRequest.getControlArea(), AddAgreementControlAreaRequest.class);
                        controlAreaRequest.setAgreementId(request.getAgreementId());
                        controlAreaRequest.setControlGoodsGroupId(groupId);
                        controlAreaRequest.setOpUserId(request.getOpUserId());
                        allControlAreaList.add(controlAreaRequest);
                    }

                    // 控销客户类型
                    List<Integer> controlCustomerTypeList = supplySalesGoodsGroupRequest.getControlCustomerTypeList();
                    if (CollUtil.isNotEmpty(controlCustomerTypeList)) {
                        for (Integer customerType : controlCustomerTypeList) {
                            AgreementControlCustomerTypeDO customerTypeDO = new AgreementControlCustomerTypeDO();
                            customerTypeDO.setCustomerType(customerType);
                            customerTypeDO.setAgreementId(request.getAgreementId());
                            customerTypeDO.setControlGoodsGroupId(groupId);
                            customerTypeDO.setOpUserId(request.getOpUserId());
                            allControlCustomerTypeDoList.add(customerTypeDO);
                        }
                    }
                }
            }
        }

        // 批量数据入库
        if (CollUtil.isNotEmpty(allSupplySalesGoodsDoList)) {
            agreementSupplySalesGoodsService.saveBatch(allSupplySalesGoodsDoList);
        }
        if (CollUtil.isNotEmpty(allAgreementControlDoList)) {
            agreementControlService.saveBatch(allAgreementControlDoList);
        }
        if (CollUtil.isNotEmpty(allControlCustomerTypeDoList)) {
            agreementControlCustomerTypeService.saveBatch(allControlCustomerTypeDoList);
        }
        if (CollUtil.isNotEmpty(allControlAreaList)) {
            agreementControlAreaService.saveControlAreaList(allControlAreaList);
        }
    }

    /**
     * 基础校验
     *
     * @param request 请求参数
     */
    @Override
    public void validAgreementSupplySalesTerms(AddAgreementSupplySalesTermsRequest request) {
        // 购进渠道为指定商业公司购进时，必须设置商业公司
        if (AgreementBuyChannelEnum.SPECIFIED_BUSINESS_BUY == AgreementBuyChannelEnum.getByCode(request.getBuyChannel())) {
            if (CollUtil.isEmpty(request.getSupplySalesEnterpriseList())) {
                throw new BusinessException(UserErrorCode.SPECIAL_BUSINESS_MUST_SET);
            }
        }

        // 非全系列品种必须设置商品组，且不能大于6个
        if (request.getAllLevelKindsFlag() == 0) {
            if (CollUtil.isEmpty(request.getSupplySalesGoodsGroupList())) {
                throw new BusinessException(UserErrorCode.MUST_SET_PRODUCT_GROUP);
            }

            if (request.getSupplySalesGoodsGroupList().size() > MAX_PRODUCT_GROUP_NUM) {
                throw new BusinessException(UserErrorCode.MORE_THAN_SET_PRODUCT_GROUP);
            }

            List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList = request.getSupplySalesGoodsGroupList();
            for (AddAgreementSupplySalesGoodsGroupRequest salesGoodsGroupRequest : supplySalesGoodsGroupList) {
                // 控销商品不能为空
                if (CollUtil.isEmpty(salesGoodsGroupRequest.getSupplySalesGoodsList())) {
                    throw new BusinessException(UserErrorCode.SUPPLY_SALES_PRODUCT_NOT_NULL);
                }

                List<Integer> controlSaleConditionList = salesGoodsGroupRequest.getAgreementControlList();
                if (AgreementControlSaleTypeEnum.NONE != AgreementControlSaleTypeEnum.getByCode(salesGoodsGroupRequest.getControlSaleType())) {
                    if (CollUtil.isEmpty(controlSaleConditionList)) {
                        throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                    }

                    // 选择了区域或者客户类型，对应的值不能为空
                    if (controlSaleConditionList.contains(AgreementControlSaleConditionEnum.AREA.getCode())) {
                        if (Objects.isNull(salesGoodsGroupRequest.getControlArea()) || StrUtil.isEmpty(salesGoodsGroupRequest.getControlArea().getJsonContent())) {
                            throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                        }
                    }
                    if (controlSaleConditionList.contains(AgreementControlSaleConditionEnum.CUSTOMER_TYPE.getCode())) {
                        if (CollUtil.isEmpty(salesGoodsGroupRequest.getControlCustomerTypeList())) {
                            throw new BusinessException(UserErrorCode.CONTROL_SALES_AREA_TYPE_NOT_NULL);
                        }
                    }
                }

            }

            // 如果甲方类型为生产厂家或品牌厂家，控销商品必须存在于该厂家内
            List<AddAgreementSupplySalesGoodsRequest> allSupplySalesGoodsList = ListUtil.toList();
            supplySalesGoodsGroupList.forEach(agreementSupplySalesGoodsGroupRequest -> allSupplySalesGoodsList.addAll(agreementSupplySalesGoodsGroupRequest.getSupplySalesGoodsList()));
            AgreementMainTermsDO mainTermsDO = this.agreementMainTermsService.getById(request.getAgreementId());

            if (AgreementFirstTypeEnum.getByCode(mainTermsDO.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER
                    || AgreementFirstTypeEnum.getByCode(mainTermsDO.getFirstType()) == AgreementFirstTypeEnum.INDUSTRIAL_BRAND) {

                List<AgreementManufacturerGoodsDTO> manufacturerGoodsDTOS = manufacturerGoodsService.getManufactureGoodsListByEid(mainTermsDO.getEid());
                List<Long> manufacturerGoodsIdList = manufacturerGoodsDTOS.stream().map(AgreementManufacturerGoodsDTO::getSpecificationGoodsId).distinct().collect(Collectors.toList());
                allSupplySalesGoodsList.forEach(agreementSupplySalesGoodsRequest -> {
                    if (!manufacturerGoodsIdList.contains(agreementSupplySalesGoodsRequest.getSpecificationGoodsId())) {
                        String message = StrFormatter.format(UserErrorCode.AGREEMENT_SUPPLY_GOODS_NOT_IN_MANUFACTURER.getMessage(), agreementSupplySalesGoodsRequest.getGoodsName());
                        throw new BusinessException(UserErrorCode.AGREEMENT_SUPPLY_GOODS_NOT_IN_MANUFACTURER, message);
                    }
                });

            }

        } else {
            request.setSupplySalesGoodsGroupList(null);
        }

    }
}
