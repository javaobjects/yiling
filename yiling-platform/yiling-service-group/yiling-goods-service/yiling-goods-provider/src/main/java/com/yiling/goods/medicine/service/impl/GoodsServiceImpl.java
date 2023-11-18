package com.yiling.goods.medicine.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.inventory.dto.request.QueryInventorySubscriptionRequest;
import com.yiling.goods.inventory.entity.InventoryDO;
import com.yiling.goods.inventory.service.InventoryService;
import com.yiling.goods.inventory.service.InventorySubscriptionService;
import com.yiling.goods.medicine.bo.DistributorAgreementGoodsBO;
import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.MatchGoodsBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dao.GoodsMapper;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsLogPageListItemDTO;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.dto.MergeGoodsRequest;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsOverSoldRequest;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.InstructionsDecoctionRequest;
import com.yiling.goods.medicine.dto.request.InstructionsDisinfectionRequest;
import com.yiling.goods.medicine.dto.request.InstructionsDispensingGranuleRequest;
import com.yiling.goods.medicine.dto.request.InstructionsFoodsRequest;
import com.yiling.goods.medicine.dto.request.InstructionsGoodsRequest;
import com.yiling.goods.medicine.dto.request.InstructionsHealthRequest;
import com.yiling.goods.medicine.dto.request.InstructionsMaterialsRequest;
import com.yiling.goods.medicine.dto.request.InstructionsMedicalInstrumentRequest;
import com.yiling.goods.medicine.dto.request.MatchGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryDistributorGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLogRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsPicRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsSkuRequest;
import com.yiling.goods.medicine.dto.request.UpdateAuditStatusByGoodsIdRequest;
import com.yiling.goods.medicine.dto.request.UpdateB2bGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdatePopGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateShelfLifeRequest;
import com.yiling.goods.medicine.entity.B2bGoodsDO;
import com.yiling.goods.medicine.entity.GoodsDO;
import com.yiling.goods.medicine.entity.GoodsLogDO;
import com.yiling.goods.medicine.entity.GoodsPicDO;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.entity.InstructionsDecoctionDO;
import com.yiling.goods.medicine.entity.InstructionsDisinfectionDO;
import com.yiling.goods.medicine.entity.InstructionsDispensingGranuleDO;
import com.yiling.goods.medicine.entity.InstructionsFoodsDO;
import com.yiling.goods.medicine.entity.InstructionsGoodsDO;
import com.yiling.goods.medicine.entity.InstructionsHealthDO;
import com.yiling.goods.medicine.entity.InstructionsMaterialsDO;
import com.yiling.goods.medicine.entity.InstructionsMedicalInstrumentDO;
import com.yiling.goods.medicine.entity.PopGoodsDO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.B2bGoodsService;
import com.yiling.goods.medicine.service.GoodsAuditService;
import com.yiling.goods.medicine.service.GoodsLogService;
import com.yiling.goods.medicine.service.GoodsPicService;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;
import com.yiling.goods.medicine.service.InstructionsDecoctionService;
import com.yiling.goods.medicine.service.InstructionsDisinfectionService;
import com.yiling.goods.medicine.service.InstructionsDispensingGranuleService;
import com.yiling.goods.medicine.service.InstructionsFoodsService;
import com.yiling.goods.medicine.service.InstructionsGoodsService;
import com.yiling.goods.medicine.service.InstructionsHealthService;
import com.yiling.goods.medicine.service.InstructionsMaterialsService;
import com.yiling.goods.medicine.service.InstructionsMedicalInstrumentService;
import com.yiling.goods.medicine.service.PopGoodsService;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDecoctionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDisinfectionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDispensingGranuleDTO;
import com.yiling.goods.standard.dto.StandardInstructionsFoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMaterialsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMedicalInstrumentDTO;
import com.yiling.goods.standard.dto.StandardSpecificationInfoDTO;
import com.yiling.goods.standard.entity.StandardGoodsDO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.entity.StandardGoodsSpecificationDO;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.goods.standard.service.StandardGoodsCategoryService;
import com.yiling.goods.standard.service.StandardGoodsPicService;
import com.yiling.goods.standard.service.StandardGoodsService;
import com.yiling.goods.standard.service.StandardGoodsSpecificationService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
@Slf4j
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMapper, GoodsDO> implements GoodsService {

    @Autowired
    InventoryService inventoryService;
    @Autowired
    StandardGoodsService standardGoodsService;
    @Autowired
    StandardGoodsCategoryService standardGoodsCateService;
    @Autowired
    StandardGoodsPicService standardGoodsPicService;
    @Autowired
    StandardGoodsSpecificationService standardGoodsSpecificationService;
    @Autowired
    GoodsAuditService goodsAuditService;
    @Autowired
    GoodsLogService goodsLogService;
    @Autowired
    GoodsPicService goodsPicService;
    @Autowired
    InstructionsGoodsService instructionsGoodsService;
    @Autowired
    InstructionsDecoctionService instructionsDecoctionService;
    @Autowired
    InstructionsDisinfectionService instructionsDisinfectionService;
    @Autowired
    InstructionsFoodsService instructionsFoodsService;
    @Autowired
    InstructionsHealthService instructionsHealthService;
    @Autowired
    InstructionsMaterialsService instructionsMaterialsService;
    @Autowired
    InstructionsMedicalInstrumentService instructionsMedicalInstrumentService;
    @Autowired
    InstructionsDispensingGranuleService instructionsDispensingGranuleService;
    @Autowired
    GoodsSkuService goodsSkuService;
    @Autowired
    B2bGoodsService b2bGoodsService;
    @Autowired
    PopGoodsService popGoodsService;
    @Autowired
    InventorySubscriptionService inventorySubscriptionService;

    @Autowired
    @Lazy
    GoodsServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    private static final Integer YL_FLAG = 1;
    /**
     * 1、商家后台新增商品信息 --包含开通产品线，没有合并的逻辑存在
     * 2、运营后台商品信息导入 --包含开通产品线，有合并的逻辑存在(有内码的情况就不能判断销售包装去重)
     * 3、erp对接新增商品信息 --不包含开通产品需要上线引擎商业公司开通的产品线，有合并的逻辑存在(有内码的情况就不能判断销售包装去重)
     * 所有没有匹配上标准信息都需要人功能审核，审核的时候必须知道产品线，然后形成sku,做库存的初始化。有重复合并并且标记重复状态
     * 审核的时候没有产品线并且是重复的商品只标记重复状态，不形成sku和库存
     *
     * @param saveOrUpdateGoodsRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        GoodsDO goodsDO = saveGoodsBase(saveOrUpdateGoodsRequest);
        Long repGoodsId = saveOrUpdateGoodsRequest.getRepGoodsId();
        Long gid = repGoodsId != null && repGoodsId > 0 ? repGoodsId : goodsDO.getId();
        //插入带审核信息，不执行后续代码
        SaveOrUpdateGoodsAuditRequest goodsAuditRequest = PojoUtils.map(saveOrUpdateGoodsRequest, SaveOrUpdateGoodsAuditRequest.class);
        goodsAuditRequest.setGid(goodsDO.getId());
        if (GoodsStatusEnum.UNDER_REVIEW.getCode().equals(goodsDO.getAuditStatus())) {
            goodsAuditService.saveGoodsAudit(goodsAuditRequest);
            return gid;
        }else if(GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDO.getAuditStatus())
                ||GoodsStatusEnum.REPETITION.getCode().equals(goodsDO.getAuditStatus())){
            goodsAuditService.passGoodsAudit(goodsAuditRequest);
        }
        //保存产品线
        saveGoodsLine(saveOrUpdateGoodsRequest, gid);
        SaveOrUpdateGoodsSkuRequest request = new SaveOrUpdateGoodsSkuRequest();
        request.setGoodsId(gid);
        request.setEid(saveOrUpdateGoodsRequest.getEid());
        goodsSkuService.updateByGoodsId(request);
        return gid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long editGoods(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        Assert.notNull(saveOrUpdateGoodsRequest.getId(), "商品id为空");
        GoodsDO goodsDO = getById(saveOrUpdateGoodsRequest.getId());
        if(null==goodsDO){
            throw new BusinessException(GoodsErrorCode.NOT_EXIST);
        }
        GoodsDO goods = PojoUtils.map(saveOrUpdateGoodsRequest, GoodsDO.class);
        Long repGoodsId = saveOrUpdateGoodsRequest.getRepGoodsId();
        Long gid = repGoodsId != null && repGoodsId > 0 ? repGoodsId : goodsDO.getId();
        this.saveOrUpdate(goods);
        //以岭品标准库反写以岭标志
        if(GoodsStatusEnum.AUDIT_PASS.getCode().equals(goods.getAuditStatus())
                && CollectionUtils.isNotEmpty(saveOrUpdateGoodsRequest.getPopEidList())
                &&  saveOrUpdateGoodsRequest.getPopEidList().contains(saveOrUpdateGoodsRequest.getEid())){
            standardGoodsService.updateYlFlagById(saveOrUpdateGoodsRequest.getStandardId(),YL_FLAG);
        }
        //修改保存多条sku
        saveGoodsSkuList(saveOrUpdateGoodsRequest, gid);
        //保存产品线
        saveGoodsLine(saveOrUpdateGoodsRequest, gid);
        sendErpFlowMq(gid);
        return gid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveGoodsByErp(SaveOrUpdateGoodsRequest saveOrUpdateGoodsRequest) {
        GoodsDO goodsDO = saveGoodsBase(saveOrUpdateGoodsRequest);
        Long repGoodsId = saveOrUpdateGoodsRequest.getRepGoodsId();
        Long gid = repGoodsId != null && repGoodsId > 0 ? repGoodsId : goodsDO.getId();
        {
            //erp同步或者导入插入sku，通过中包装、大包装、是否拆包销售：1可拆0不可拆 生成包装数据
            SaveGoodsRequest request = PojoUtils.map(saveOrUpdateGoodsRequest, SaveGoodsRequest.class);
            request.setId(gid);
            saveGoodsSku(request);
        }
        //插入带审核信息，不执行后续代码
        SaveOrUpdateGoodsAuditRequest goodsAuditRequest = PojoUtils.map(saveOrUpdateGoodsRequest, SaveOrUpdateGoodsAuditRequest.class);
        goodsAuditRequest.setGid(goodsDO.getId());
        if (GoodsStatusEnum.UNDER_REVIEW.getCode().equals(goodsDO.getAuditStatus())) {
            goodsAuditService.saveGoodsAudit(goodsAuditRequest);
            return gid;
        }else if(GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDO.getAuditStatus())
                ||GoodsStatusEnum.REPETITION.getCode().equals(goodsDO.getAuditStatus())){
            goodsAuditService.passGoodsAudit(goodsAuditRequest);
        }
        //保存产品线
        saveGoodsLine(saveOrUpdateGoodsRequest, gid);
        sendErpFlowMq(gid);
        return gid;
    }

    private void sendErpFlowMq(Long gid){
        GoodsDO goods = getById(gid);
        //匹配成功通知erp流向
        if (GoodsStatusEnum.AUDIT_PASS.getCode().equals(goods.getAuditStatus())){
            JSONObject goodsJson = new JSONObject();
            goodsJson.put("id",gid);
            goodsJson.put("name",goods.getName());
            goodsJson.put("ename",goods.getEname());
            goodsJson.put("specifications",goods.getSellSpecifications());
            _this.sendMq(Constants.TOPIC_GOODS_AUDIT_ERP_FLOW_SEND, Constants.TAG_GOODS_AUDIT_FLOW_SALE_SEND,goodsJson.toJSONString(),null);
        }
    }

    /**
     * 保存商品基础信息
     *
     * @param request
     * @return
     */
    private GoodsDO saveGoodsBase(SaveOrUpdateGoodsRequest request) {
        //先判断商品id是否存在,不存在需要匹配。如果存在查询商品信息售卖规格ID是否为空，为空就匹配
        GoodsDO goodsDO = PojoUtils.map(request, GoodsDO.class);
        Long sellSpecificationsId = request.getSellSpecificationsId();
        if (goodsDO.getId() != null && goodsDO.getId() != 0) {
            //如果传进来的规格是空的，必须重新获取规格ID
            if (sellSpecificationsId == null || sellSpecificationsId == 0) {
                GoodsDO oldGoodsDO = getById(goodsDO.getId());
                sellSpecificationsId = oldGoodsDO.getSellSpecificationsId();
            }
        }
        //如果商品没有售卖规格ID需要重新匹配
        if (sellSpecificationsId == null || sellSpecificationsId == 0) {
            //商品匹配逻辑
            MatchGoodsRequest matchGoodsRequest = PojoUtils.map(request, MatchGoodsRequest.class);
            MatchGoodsBO matchGoodsBO = this.matchSellSpecificationsByGoods(matchGoodsRequest);
            //匹配成功，商品上架
            if (matchGoodsBO.getMatchStatus() == 2) {
                StandardGoodsSpecificationDO standardGoodsSpecificationDO = standardGoodsSpecificationService.getById(matchGoodsBO.getSellSpecificationsId());
                StandardGoodsDO standardGoodsDO = standardGoodsService.getById(matchGoodsBO.getStandardId());
                goodsDO.setStandardId(matchGoodsBO.getStandardId());
                goodsDO.setGoodsType(standardGoodsDO.getGoodsType());
                goodsDO.setSellSpecificationsId(matchGoodsBO.getSellSpecificationsId());
                goodsDO.setSellSpecifications(standardGoodsSpecificationDO.getSellSpecifications());
                goodsDO.setSellUnit(standardGoodsSpecificationDO.getUnit());
                Long repGoodsId = this.queryInfoBySpecIdAndEid(matchGoodsBO.getSellSpecificationsId(), request.getEid());
                //判断商品是否重复
                if (repGoodsId > 0) {
                    request.setRepGoodsId(repGoodsId);
                    //匹配有重复商品数据，需要生成销售包装数据
                    goodsDO.setAuditStatus(GoodsStatusEnum.REPETITION.getCode());
                } else {
                    goodsDO.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
                }
                //匹配成功通知erp流向
                if (goodsDO.getId() != null && goodsDO.getId() != 0){
                    JSONObject goodsJson = new JSONObject();
                    goodsJson.put("id",goodsDO.getId());
                    goodsJson.put("ename",goodsDO.getEname());
                    goodsJson.put("name",goodsDO.getName());
                    goodsJson.put("specifications",goodsDO.getSpecifications());
                    _this.sendMq(Constants.TOPIC_GOODS_AUDIT_ERP_FLOW_SEND, Constants.TAG_GOODS_AUDIT_FLOW_SALE_SEND,goodsJson.toJSONString(),null);
                }

            } else {
                goodsDO.setAuditStatus(GoodsStatusEnum.UNDER_REVIEW.getCode());
                request.setOutReason(GoodsOutReasonEnum.DEFAULT.getCode());
            }
        } else {
            StandardGoodsSpecificationDO standardGoodsSpecificationDO = standardGoodsSpecificationService.getById(sellSpecificationsId);
            goodsDO.setSellSpecifications(standardGoodsSpecificationDO.getSellSpecifications());
            goodsDO.setSellUnit(standardGoodsSpecificationDO.getUnit());
        }

        if(!GoodsStatusEnum.REPETITION.getCode().equals(goodsDO.getAuditStatus())){
            //商品信息插入或者修改
            this.saveOrUpdate(goodsDO);
            //以岭品标准库反写以岭标志
            if(GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDO.getAuditStatus())
                    && CollectionUtils.isNotEmpty(request.getPopEidList())
                    &&request.getPopEidList().contains(goodsDO.getEid())){
                standardGoodsService.updateYlFlagById(goodsDO.getStandardId(),YL_FLAG);
            }
            //保存图片信息
            List<SaveGoodsPicRequest> picList = request.getPicBasicsInfoList();
            if (CollUtil.isNotEmpty(picList)) {
                picList.forEach(e -> {
                    e.setOpUserId(request.getOpUserId());
                });
                goodsPicService.insertGoodsPic(picList, goodsDO.getId());
            }
            //保存说明书
            saveManual(request, goodsDO.getId());
        }
        return goodsDO;
    }

    /**
     * 保存说明书
     *
     * @param request
     * @param gid
     */
    private void saveManual(SaveOrUpdateGoodsRequest request, Long gid) {
        if (request.getGoodsType() != null && request.getGoodsType() != 0) {
            StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(request.getGoodsType());
            switch (goodsTypeEnum) {
                case GOODS_TYPE:
                    //药品说明书
                    InstructionsGoodsRequest instructionsGoodsRequest = request.getGoodsInstructionsInfo();
                    if (instructionsGoodsRequest != null) {
                        QueryWrapper<InstructionsGoodsDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsGoodsDO::getGoodsId, gid);
                        instructionsGoodsRequest.setGoodsId(gid);
                        instructionsGoodsService.saveOrUpdate(PojoUtils.map(instructionsGoodsRequest, InstructionsGoodsDO.class), queryWrapper);
                    }
                    break;
                case DECOCTION_TYPE:
                    //中药饮片
                    InstructionsDecoctionRequest instructionsDecoctionRequest = request.getDecoctionInstructionsInfo();
                    if (instructionsDecoctionRequest != null) {
                        QueryWrapper<InstructionsDecoctionDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsDecoctionDO::getGoodsId, gid);
                        instructionsDecoctionRequest.setGoodsId(gid);
                        instructionsDecoctionService.saveOrUpdate(PojoUtils.map(instructionsDecoctionRequest, InstructionsDecoctionDO.class), queryWrapper);
                    }
                    break;
                case MATERIAL_TYPE:
                    //中药材
                    InstructionsMaterialsRequest instructionsMaterialsRequest = request.getMaterialsInstructionsInfo();
                    if (instructionsMaterialsRequest != null) {
                        QueryWrapper<InstructionsMaterialsDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsMaterialsDO::getGoodsId, gid);
                        instructionsMaterialsRequest.setGoodsId(gid);
                        instructionsMaterialsService.saveOrUpdate(PojoUtils.map(instructionsMaterialsRequest, InstructionsMaterialsDO.class), queryWrapper);
                    }
                    break;
                case DISINFECTION_TYPE:
                    //消杀品
                    InstructionsDisinfectionRequest instructionsDisinfectionRequest = request.getDisinfectionInstructionsInfo();
                    if (instructionsDisinfectionRequest != null) {
                        QueryWrapper<InstructionsDisinfectionDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsDisinfectionDO::getGoodsId, gid);
                        instructionsDisinfectionRequest.setGoodsId(gid);
                        instructionsDisinfectionService.saveOrUpdate(PojoUtils.map(instructionsDisinfectionRequest, InstructionsDisinfectionDO.class), queryWrapper);
                    }
                    break;
                case HEALTH_TYPE:
                    //保健食品
                    InstructionsHealthRequest instructionsHealthRequest = request.getHealthInstructionsInfo();
                    if (instructionsHealthRequest != null) {
                        QueryWrapper<InstructionsHealthDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsHealthDO::getGoodsId, gid);
                        instructionsHealthRequest.setGoodsId(gid);
                        instructionsHealthService.saveOrUpdate(PojoUtils.map(instructionsHealthRequest, InstructionsHealthDO.class), queryWrapper);
                    }
                    break;
                case FOODS_TYPE:
                    //食品
                    InstructionsFoodsRequest instructionsFoodsRequest = request.getFoodsInstructionsInfo();
                    if (instructionsFoodsRequest != null) {
                        QueryWrapper<InstructionsFoodsDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsFoodsDO::getGoodsId, gid);
                        instructionsFoodsRequest.setGoodsId(gid);
                        instructionsFoodsService.saveOrUpdate(PojoUtils.map(instructionsFoodsRequest, InstructionsFoodsDO.class), queryWrapper);
                    }
                    break;
                case MEDICAL_INSTRUMENT_TYPE:
                    //食品
                    InstructionsMedicalInstrumentRequest medicalInstrumentRequest = request.getMedicalInstrumentInfo();
                    if (medicalInstrumentRequest != null) {
                        QueryWrapper<InstructionsMedicalInstrumentDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsMedicalInstrumentDO::getGoodsId, gid);
                        medicalInstrumentRequest.setGoodsId(gid);
                        instructionsMedicalInstrumentService.saveOrUpdate(PojoUtils.map(medicalInstrumentRequest, InstructionsMedicalInstrumentDO.class), queryWrapper);
                    }
                    break;
                case DISPENSING_GRANULE_TYPE:
                    //食品
                    InstructionsDispensingGranuleRequest dispensingGranuleRequest = request.getDispensingGranuleInfo();
                    if (dispensingGranuleRequest != null) {
                        QueryWrapper<InstructionsDispensingGranuleDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(InstructionsDispensingGranuleDO::getGoodsId, gid);
                        dispensingGranuleRequest.setGoodsId(gid);
                        instructionsDispensingGranuleService.saveOrUpdate(PojoUtils.map(dispensingGranuleRequest, InstructionsDispensingGranuleDO.class), queryWrapper);
                    }
                    break;
            }
        }
    }

    private void saveGoodsSkuList(SaveOrUpdateGoodsRequest request, Long gid) {
        //修改包裹信息和库存信息
        List<SaveOrUpdateGoodsSkuRequest> list = request.getGoodsSkuList();
        SaveGoodsLineRequest goodsLineRequest = request.getGoodsLineInfo();
        if (goodsLineRequest != null) {
            if (CollUtil.isNotEmpty(list)) {
                SaveOrUpdateGoodsSkuRequest zeroPackageSku = list.stream().filter(skuRequest -> skuRequest.getPackageNumber() <= 0).findFirst().orElse(null);
                if (null != zeroPackageSku) {
                    throw new BusinessException(GoodsErrorCode.GOODS_PACKAGE_ZERO);
                }
                Integer openFlag = 1;
                //查出原始sku
                for (SaveOrUpdateGoodsSkuRequest saveOrUpdateGoodsSkuRequest : list) {
                    saveOrUpdateGoodsSkuRequest.setOpUserId(request.getOpUserId());
                    saveOrUpdateGoodsSkuRequest.setGoodsId(gid);
                    saveOrUpdateGoodsSkuRequest.setEid(request.getEid());
                    if (null != saveOrUpdateGoodsSkuRequest.getId() && saveOrUpdateGoodsSkuRequest.getId() > 0) {
                        goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
                    } else {
                        if (openFlag.equals(goodsLineRequest.getMallFlag())) {
                            //添加sku和库存
                            saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());
                            goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
                        }
                        if (openFlag.equals(goodsLineRequest.getPopFlag())) {
                            saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.POP.getCode());
                            goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
                        }
                    }
                }
            }
        }
    }

    private void saveGoodsLine(SaveOrUpdateGoodsRequest request, Long gid) {
        //开通产品线，修改对应的商品库上下架信息
        SaveGoodsLineRequest goodsLineRequest = request.getGoodsLineInfo();
        if (goodsLineRequest != null) {
            //设置商品GID
            goodsLineRequest.setGoodsId(gid);
            //产品线集合
            List<SaveGoodsLineRequest> goodsLineList = new ArrayList<>();
            goodsLineList.add(goodsLineRequest);
            //产品信息信息
            UpdateGoodsLineRequest updateRequest = new UpdateGoodsLineRequest();
            updateRequest.setGoodsLineList(goodsLineList);
            updateRequest.setIsPatent(request.getIsPatent());
            updateRequest.setOutReason(request.getOutReason());
            updateRequest.setGoodsStatus(request.getGoodsStatus());
            updateRequest.setOpUserId(request.getOpUserId());
            updateRequest.setPopEidList(request.getPopEidList());
            updateGoodsLine(updateRequest);
        }
    }

    /**
     * 开通pop产品线需要判断是否是以岭的商品
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateGoodsLine(UpdateGoodsLineRequest request) {
        List<Long> goodsIds = request.getGoodsLineList().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<GoodsDTO> goodsDTOList = this.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> goodsMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        //商品规格Id
        Map<Long, GoodsDTO> goodsSpecificationsIdMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getSellSpecificationsId, Function.identity()));
        List<GoodsDTO> goodsSpecificationsList = this.findGoodsBySellSpecificationsIdAndEid(new ArrayList<>(goodsSpecificationsIdMap.keySet()), request.getPopEidList());
        //        goodsSpecificationsList = goodsSpecificationsList.stream().filter(e -> request.getPopEidList().contains(e.getEid())).collect(Collectors.toList());
        List<Long> specificationsIdList = goodsSpecificationsList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        //产品线开启标识
        Integer openFlag = 1;
        for (SaveGoodsLineRequest saveGoodsLineRequest : request.getGoodsLineList()) {
            //开通pop产品线需要判断是否是以岭的商品
            GoodsDTO goodsDTO = goodsMap.get(saveGoodsLineRequest.getGoodsId());
            if (saveGoodsLineRequest.getPopFlag() != null && saveGoodsLineRequest.getPopFlag() == 1) {
                if (!request.getPopEidList().contains(goodsDTO.getEid()) && !specificationsIdList.contains(goodsDTO.getSellSpecificationsId())) {
                    saveGoodsLineRequest.setPopFlag(0);
                }
            }
            UpdateGoodsRequest updateGoodsRequest = new UpdateGoodsRequest();
            updateGoodsRequest.setGoodsId(saveGoodsLineRequest.getGoodsId());
            updateGoodsRequest.setIsPatent(request.getIsPatent());
            updateGoodsRequest.setOutReason(request.getOutReason());
            updateGoodsRequest.setGoodsStatus(request.getGoodsStatus());

            updateGoodsRequest.setEid(goodsDTO.getEid());
            updateGoodsRequest.setSellSpecificationsId(goodsDTO.getSellSpecificationsId());
            updateGoodsRequest.setStandardId(goodsDTO.getStandardId());

            if(openFlag.equals(saveGoodsLineRequest.getMallFlag())){
                updateGoodsRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());
                this.updateGoods(updateGoodsRequest);
            }

            if(openFlag.equals(saveGoodsLineRequest.getPopFlag())){
                updateGoodsRequest.setGoodsLine(GoodsLineEnum.POP.getCode());
                this.updateGoods(updateGoodsRequest);
            }
        }
        return true;
    }

    @Override
    public Boolean updateGoods(UpdateGoodsRequest updateGoodsRequest) {
        //添加产品线里面商品信息
        if (updateGoodsRequest.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
            UpdateB2bGoodsRequest request = new UpdateB2bGoodsRequest();
            request.setGoodsId(updateGoodsRequest.getGoodsId());
            request.setEid(updateGoodsRequest.getEid());
            request.setStatus(GoodsLineStatusEnum.ENABLED.getCode());
            request.setSellSpecificationsId(updateGoodsRequest.getSellSpecificationsId());
            request.setStandardId(updateGoodsRequest.getStandardId());
            request.setOpUserId(updateGoodsRequest.getOpUserId());
            b2bGoodsService.updateB2bGoods(request);
        }
        if (updateGoodsRequest.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
            UpdatePopGoodsRequest request = new UpdatePopGoodsRequest();
            request.setGoodsId(updateGoodsRequest.getGoodsId());
            request.setEid(updateGoodsRequest.getEid());
            request.setStatus(GoodsLineStatusEnum.ENABLED.getCode());
            request.setSellSpecificationsId(updateGoodsRequest.getSellSpecificationsId());
            request.setStandardId(updateGoodsRequest.getStandardId());
            request.setOpUserId(updateGoodsRequest.getOpUserId());
            request.setIsPatent(updateGoodsRequest.getIsPatent());
            popGoodsService.updatePopGoods(request);
        }

        if (updateGoodsRequest.getGoodsStatus() != null && updateGoodsRequest.getGoodsStatus() != 0) {
            BatchUpdateGoodsStatusRequest batchUpdateGoodsStatusRequest = new BatchUpdateGoodsStatusRequest();
            batchUpdateGoodsStatusRequest.setGoodsIds(Arrays.asList(updateGoodsRequest.getGoodsId()));
            batchUpdateGoodsStatusRequest.setGoodsStatus(updateGoodsRequest.getGoodsStatus());
            batchUpdateGoodsStatusRequest.setOutReason(updateGoodsRequest.getOutReason());
            batchUpdateGoodsStatusRequest.setOpUserId(updateGoodsRequest.getOpUserId());
            if (updateGoodsRequest.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
                b2bGoodsService.updateGoodsStatus(batchUpdateGoodsStatusRequest);
            }
            if (updateGoodsRequest.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
                popGoodsService.updateGoodsStatus(batchUpdateGoodsStatusRequest);
            }
        }
        return true;
    }

    @Override
    public Boolean updateAuditStatusByGoodsId(UpdateAuditStatusByGoodsIdRequest request) {
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(request.getId());
        goodsDO.setAuditStatus(GoodsStatusEnum.REJECT.getCode());
        goodsDO.setOpUserId(request.getOpUserId());
        return this.updateById(goodsDO);
    }

    /**
     * 添加商品skuID和初始化库存信息
     *
     * @param saveGoodsRequest
     * @return
     */
    public Boolean saveGoodsSku(SaveGoodsRequest saveGoodsRequest) {
        SaveGoodsLineRequest goodsLine = saveGoodsRequest.getGoodsLineInfo();
        if (goodsLine != null) {
            Integer openFlag = 1;
            SaveOrUpdateGoodsSkuRequest saveOrUpdateGoodsSkuRequest = new SaveOrUpdateGoodsSkuRequest();
            saveOrUpdateGoodsSkuRequest.setGoodsId(saveGoodsRequest.getId());
            saveOrUpdateGoodsSkuRequest.setInSn(saveGoodsRequest.getInSn());
            saveOrUpdateGoodsSkuRequest.setSn(saveGoodsRequest.getSn());
            saveOrUpdateGoodsSkuRequest.setQty(saveGoodsRequest.getQty());
            saveOrUpdateGoodsSkuRequest.setEid(saveGoodsRequest.getEid());
            saveOrUpdateGoodsSkuRequest.setRemark(saveGoodsRequest.getRemark());
            saveOrUpdateGoodsSkuRequest.setOpUserId(saveGoodsRequest.getOpUserId());
            if (openFlag.equals(goodsLine.getPopFlag())) {
                saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.POP.getCode());
                saveSkuBySplitPackage(saveGoodsRequest, saveOrUpdateGoodsSkuRequest);
            }
            if (openFlag.equals(goodsLine.getMallFlag())) {
                saveOrUpdateGoodsSkuRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());
                saveSkuBySplitPackage(saveGoodsRequest, saveOrUpdateGoodsSkuRequest);
            }
        }
        return true;
    }

    private void saveSkuBySplitPackage(SaveGoodsRequest saveGoodsRequest, SaveOrUpdateGoodsSkuRequest saveOrUpdateGoodsSkuRequest) {
        if (saveGoodsRequest.getCanSplit() != null && saveGoodsRequest.getCanSplit() == 1) {
            saveOrUpdateGoodsSkuRequest.setPackageNumber(1L);
            goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
        } else if (saveGoodsRequest.getMiddlePackage() != null && saveGoodsRequest.getMiddlePackage() != 0) {
            saveOrUpdateGoodsSkuRequest.setPackageNumber(saveGoodsRequest.getMiddlePackage().longValue());
            goodsSkuService.saveOrUpdateByNumber(saveOrUpdateGoodsSkuRequest);
        }
    }

    /**
     * 匹配标准库id以及售卖规格id(0-未匹配任何信息 1-只匹配上standardId 2-匹配上specificationId
     *
     * @param request 通过对象取匹配标准库
     * @return
     */
    @Override
    public MatchGoodsBO matchSellSpecificationsByGoods(MatchGoodsRequest request) {
        MatchGoodsBO matchGoodsBO = new MatchGoodsBO();
        matchGoodsBO.setMatchStatus(0);
        //传进来的商品信息为空
        if (request == null) {
            return matchGoodsBO;
        }
        // 标准库id
        Long standardId = request.getStandardId();
        if (standardId == null) {
            standardId = 0L;
        }
        // 规格id
        Long specificationsId = request.getSellSpecificationsId();
        if (specificationsId == null) {
            specificationsId = 0L;
        }
        // 需要匹配标准库
        if (standardId == 0) {
            // 1.首先同步批准文号匹配
            String licenseNo = request.getLicenseNo();
            isStandardGoods(licenseNo);
            if (StringUtils.isNotEmpty(licenseNo)) {
                // 判断是否有进口的字段
                licenseNo = licenseNo.toUpperCase();
                //查询标准库信息
                standardId = standardGoodsService.getStandardGoodsByLicenseNoAndName(licenseNo, request.getName());
            }
            // 标准库匹配不上，直接返回
            if (standardId == null || standardId == 0) {
                return matchGoodsBO;
            }
        }
        // 需要匹配规格
        if (standardId > 0 && specificationsId == 0) {
            matchGoodsBO.setStandardId(standardId);
            matchGoodsBO.setMatchStatus(1);
            String sellSpecifications = request.getSpecifications();
            List<StandardGoodsSpecificationDO> specList = standardGoodsSpecificationService.getListStandardGoodsSpecification(Arrays.asList(standardId));
            if (!CollUtil.isEmpty(specList)) {
                for (StandardGoodsSpecificationDO standardGoodsSpecification : specList) {
                    String specifications = standardGoodsSpecification.getSellSpecifications();
                    if (specifications.trim().equals(sellSpecifications.trim())) {
                        specificationsId = standardGoodsSpecification.getId();
                    }
                }
            }
        }
        if (specificationsId > 0) {
            matchGoodsBO.setSellSpecificationsId(specificationsId);
            matchGoodsBO.setStandardId(standardId);
            matchGoodsBO.setMatchStatus(2);
        }
        return matchGoodsBO;
    }

    @Override
    public Page<GoodsListItemBO> queryPageListGoods(QueryGoodsPageListRequest request) {
        Page<GoodsListItemBO> page = this.baseMapper.queryPageListGoods(new Page<>(request.getCurrent(), request.getSize()), request);
        List<Long> standardIds = page.getRecords().stream().map(e -> e.getStandardId()).distinct().collect(Collectors.toList());

        //获取产品线信息
        List<Long> ids = page.getRecords().stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        List<GoodsLineBO> goodsLineBOList = this.getGoodsLineByGoodsIds(ids);
        Map<Long, GoodsLineBO> lineBOMap = goodsLineBOList.stream().collect(Collectors.toMap(GoodsLineBO::getGoodsId, Function.identity(), (e1, e2) -> e1));

        //获取分类信息
        Map<Long, StandardGoodsDO> standardGoodsDOMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(standardIds) && standardIds.size() > 0) {
            List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
            standardGoodsDOMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));
        }
        Map<Long, StandardGoodsDO> finalStandardGoodsDOMap = standardGoodsDOMap;

        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = MapUtil.newHashMap();
        if (request.getIsShowDefaultPic() == null || request.getIsShowDefaultPic() == 0) {
            standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        }
        Map<Long, List<StandardGoodsPicDO>> finalStandardGoodsPicMap = standardGoodsPicMap;

        page.getRecords().forEach(e -> {
            e.setPic(getDefaultUrlByStandardGoodsPicList(finalStandardGoodsPicMap.get(e.getStandardId()), e.getSellSpecificationsId()));
            if (finalStandardGoodsDOMap.get(e.getStandardId()) != null) {
                e.setStandardCategoryName1(finalStandardGoodsDOMap.get(e.getStandardId()).getStandardCategoryName1());
                e.setStandardCategoryName2(finalStandardGoodsDOMap.get(e.getStandardId()).getStandardCategoryName2());
            }
            GoodsLineBO goodsLineBO = lineBOMap.get(e.getId());
            if(null!=goodsLineBO){
                List<String> lineStr = new ArrayList<>();
                if(GoodsLineStatusEnum.ENABLED.getCode().equals(goodsLineBO.getPopStatus())){
                    lineStr.add(GoodsLineEnum.POP.getName());
                }
                if(GoodsLineStatusEnum.ENABLED.getCode().equals(goodsLineBO.getMallStatus())){
                    lineStr.add(GoodsLineEnum.B2B.getName());
                }
                e.setGoodsLineDesc(String.join(",", lineStr));
            }
        });
        return page;
    }

    @Override
    public String getDefaultUrl(Long standardId, Long sellSpecificationsId) {
        List<StandardGoodsPicDO> standardGoodsPicDOList = standardGoodsPicService.getStandardGoodsPicByStandardId(standardId);
        return getDefaultUrlByStandardGoodsPicList(standardGoodsPicDOList, sellSpecificationsId);
    }

    @Override
    public String getDefaultUrlByStandardGoodsPicList(List<StandardGoodsPicDO> standardGoodsPicDOList, Long sellSpecificationsId) {
        //先通过售卖ID获取图片信息
        if (CollUtil.isEmpty(standardGoodsPicDOList)) {
            return "";
        }
        String defaultPic = "";
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicDOMap = new HashMap<>();
        for (StandardGoodsPicDO standardGoodsPicDO : standardGoodsPicDOList) {
            if (standardGoodsPicDOMap.containsKey(standardGoodsPicDO.getSellSpecificationsId())) {
                standardGoodsPicDOMap.get(standardGoodsPicDO.getSellSpecificationsId()).add(standardGoodsPicDO);
            } else {
                List standardGoodsPicList = new ArrayList<StandardGoodsPicDO>();
                standardGoodsPicList.add(standardGoodsPicDO);
                standardGoodsPicDOMap.put(standardGoodsPicDO.getSellSpecificationsId(), standardGoodsPicList);
            }
        }
        //取规格图片信息
        for (Map.Entry<Long, List<StandardGoodsPicDO>> listMap : standardGoodsPicDOMap.entrySet()) {
            if (listMap.getKey().equals(sellSpecificationsId)) {
                List<StandardGoodsPicDO> standardGoodsPicList = listMap.getValue();
                for (StandardGoodsPicDO standardGoodsPicDO : standardGoodsPicList) {
                    if (standardGoodsPicDO.getPicDefault().equals(1)) {
                        defaultPic = standardGoodsPicDO.getPic();
                    }
                }
                if (StringUtils.isEmpty(defaultPic)) {
                    defaultPic = standardGoodsPicList.get(0).getPic();
                }
            }
        }

        //取标准库图片,规格为0的情况
        if (StringUtils.isEmpty(defaultPic)) {
            List<StandardGoodsPicDO> standardGoodsPicList = standardGoodsPicDOMap.get(0L);
            if (CollectionUtils.isNotEmpty(standardGoodsPicList)) {
                for (StandardGoodsPicDO standardGoodsPicDO : standardGoodsPicList) {
                    if (standardGoodsPicDO.getPicDefault().equals(1)) {
                        defaultPic = standardGoodsPicDO.getPic();
                    }
                }
                if (StringUtils.isEmpty(defaultPic)) {
                    defaultPic = standardGoodsPicList.get(0).getPic();
                }
            }

        }
        return defaultPic;
    }

    @Override
    public Map<Long, List<GoodsDTO>> batchQueryDistributorGoodsInfo(List<QueryDistributorGoodsRequest> requestList) {
        List<Long> goodsIds = requestList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<Long> distributorEids = requestList.stream().map(e -> e.getDistributorEid()).collect(Collectors.toList());
        List<GoodsDO> goodsList = this.listByIds(goodsIds);
        //售卖规格Id对应以岭商品Id
        Map<Long, Long> longLongMap = goodsList.stream().collect(Collectors.toMap(GoodsDO::getId, GoodsDO::getSellSpecificationsId));

        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsDO::getSellSpecificationsId, longLongMap.values());
        queryWrapper.lambda().in(GoodsDO::getEid, distributorEids);
        queryWrapper.lambda().in(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());

        List<GoodsDO> goodsDOList = this.list(queryWrapper);

        //key=销售规格ID，value=配送商品集合
        Map<Long, List<GoodsDO>> map = new HashMap<>();
        for (GoodsDO goodsDO : goodsDOList) {
            if (map.containsKey(goodsDO.getSellSpecificationsId())) {
                map.get(goodsDO.getSellSpecificationsId()).add(goodsDO);
            } else {
                List<GoodsDO> goodsDOlist = new ArrayList<>();
                goodsDOlist.add(goodsDO);
                map.put(goodsDO.getSellSpecificationsId(), goodsDOlist);
            }
        }

        Map<Long, List<GoodsDTO>> returnMap = new HashMap<>();
        for (QueryDistributorGoodsRequest queryDistributorGoodsRequest : requestList) {
            Long sellSpecificationsId = longLongMap.get(queryDistributorGoodsRequest.getGoodsId());
            List<GoodsDTO> goodsInfoDTOList = PojoUtils.map(map.get(sellSpecificationsId), GoodsDTO.class);
            returnMap.put(queryDistributorGoodsRequest.getGoodsId(), goodsInfoDTOList);
        }
        return returnMap;
    }

    @Override
    public Map<Long, String> getPictureUrlMapByGoodsIds(List<Long> goodsIds) {
        Map<Long, String> map = new HashMap<>(goodsIds.size());
        List<GoodsDO> list = this.listByIds(goodsIds);
        List<Long> standardIds = list.stream().map(e -> e.getStandardId()).collect(Collectors.toList());
        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        for (GoodsDO goodsDO : list) {
            map.put(goodsDO.getId(), getDefaultUrlByStandardGoodsPicList(standardGoodsPicMap.get(goodsDO.getStandardId()), goodsDO.getSellSpecificationsId()));
        }
        return map;
    }

    @Override
    public Long queryInfoBySpecIdAndEid(Long sellSpecificationsId, Long eid) {
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsDO::getSellSpecificationsId, sellSpecificationsId);
        queryWrapper.lambda().eq(GoodsDO::getEid, eid);
        queryWrapper.lambda().eq(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());
        GoodsDO goodsDO = this.getOne(queryWrapper);
        if (goodsDO == null) {
            return 0L;
        }
        return goodsDO.getId();
    }

    /**
     * 查询商业公司对应的售卖商品id
     *
     * @param sellSpecificationsId
     * @param eids
     * @return
     */
    @Override
    public Map<Long, Long> queryInfoBySpecIdAndEids(Long sellSpecificationsId, List<Long> eids) {
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsDO::getSellSpecificationsId, sellSpecificationsId);
        queryWrapper.lambda().eq(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());
        queryWrapper.lambda().in(GoodsDO::getEid, eids);
        List<GoodsDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, Long> mapId = new HashMap<>();
        Map<Long, List<GoodsDO>> map = new HashMap<>();
        for (GoodsDO goodsDO : list) {
            if (map.containsKey(goodsDO.getEid())) {
                map.get(goodsDO.getEid()).add(goodsDO);
            } else {
                List<GoodsDO> goodsDOlist = new ArrayList<>();
                goodsDOlist.add(goodsDO);
                map.put(goodsDO.getEid(), goodsDOlist);
            }
        }

        for (Map.Entry<Long, List<GoodsDO>> entry : map.entrySet()) {
            GoodsDO goodsDO = getDistributionGoodsId(entry.getValue());
            mapId.put(entry.getKey(), goodsDO.getId());
        }
        return mapId;
    }

    /**
     * 售卖规格id对应 该商业公司下面的商品信息Id
     *
     * @param sellSpecificationsIds
     * @param eid
     * @return
     */
    @Override
    public Map<Long, Long> queryInfoMapBySpecIdsAndEid(List<Long> sellSpecificationsIds, Long eid) {
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsDO::getSellSpecificationsId, sellSpecificationsIds);
        queryWrapper.lambda().eq(GoodsDO::getEid, eid);
        List<GoodsDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        Map<Long, Long> mapId = new HashMap<>();
        Map<Long, List<GoodsDO>> map = new HashMap<>();
        for (GoodsDO goodsDO : list) {
            if (map.containsKey(goodsDO.getSellSpecificationsId())) {
                map.get(goodsDO.getSellSpecificationsId()).add(goodsDO);
            } else {
                List<GoodsDO> goodsDOlist = new ArrayList<>();
                goodsDOlist.add(goodsDO);
                map.put(goodsDO.getSellSpecificationsId(), goodsDOlist);
            }
        }

        for (Map.Entry<Long, List<GoodsDO>> entry : map.entrySet()) {
            GoodsDO goodsDO = getDistributionGoodsId(entry.getValue());
            mapId.put(entry.getKey(), goodsDO.getId());
        }
        return mapId;
    }

    @Override
    public Map<Long, Long> getDistributorGoodsIdMapByYlGoodsId(List<Long> distributorEids, Long goodsId) {
        Map<Long, Long> distributorEidMap = new HashMap<>();
        GoodsDO goods = this.getById(goodsId);
        //配送商Eid对应配送商商品Id
        Map<Long, Long> longLongMap = this.queryInfoBySpecIdAndEids(goods.getSellSpecificationsId(), distributorEids);

        if (CollUtil.isEmpty(longLongMap)) {
            return MapUtil.empty();
        }

        for (Long distributorEid : distributorEids) {
            distributorEidMap.put(distributorEid, longLongMap.getOrDefault(distributorEid, 0L));
        }
        return distributorEidMap;
    }

    @Override
    public Map<Long, Long> getDistributorGoodsMapByDistributorEid(List<Long> goodsIds, Long distributorEid) {
        Map<Long, Long> ylGoodsMap = new HashMap<>();
        //以岭商品信息集合
        List<GoodsDO> goodsList = this.listByIds(goodsIds);
        //售卖规格Id和以岭商品Id映射Map对象
        Map<Long, Long> ylGoodsAndSpecificationMap = goodsList.stream().collect(Collectors.toMap(GoodsDO::getId, GoodsDO::getSellSpecificationsId));
        List<Long> specificationIds = goodsList.stream().map(GoodsDO::getSellSpecificationsId).collect(Collectors.toList());
        //售卖规格映射配送商品Id
        Map<Long, Long> specificationAndDistributorGoodsMap = this.queryInfoMapBySpecIdsAndEid(specificationIds, distributorEid);
        for (Long goodsId : goodsIds) {
            Long distributorGoodsId = specificationAndDistributorGoodsMap.get(ylGoodsAndSpecificationMap.get(goodsId));
            if (null != distributorGoodsId) {
                ylGoodsMap.put(goodsId, distributorGoodsId);
            }
        }
        return ylGoodsMap;
    }

    @Override
    public Page<DistributorAgreementGoodsBO> getDistributorGoodsByrAgreementGoodsPageList(Map<Long, String> goodsAgreementMap, Long distributorEid, int current, int size) {
        List<Long> ylGoodsIds = Lists.newArrayList(goodsAgreementMap.keySet());
        //以岭商品信息集合
        List<GoodsDO> goodsList = this.listByIds(ylGoodsIds);
        //获取以岭品规格id
        List<Long> specificationIds = goodsList.stream().map(GoodsDO::getSellSpecificationsId).collect(Collectors.toList());
        //创建以岭goodsId和规格map
        Map<Long, Long> specificationAndYlGoodsMap = goodsList.stream().collect(Collectors.toMap(GoodsDO::getSellSpecificationsId, GoodsDO::getId));
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsDO::getSellSpecificationsId, specificationIds);
        queryWrapper.lambda().eq(GoodsDO::getEid, distributorEid);
        queryWrapper.lambda().ne(GoodsDO::getAuditStatus, GoodsStatusEnum.REPETITION.getCode());
        Page<GoodsDO> page = new Page<>(current, size);
        //规格分页获取配送商商品
        Page<GoodsDO> distributorGoodsPage = this.page(page, queryWrapper);
        List<DistributorAgreementGoodsBO> resultList = distributorGoodsPage.getRecords().stream().map(distributorGoods -> {
            DistributorAgreementGoodsBO distributorAgreementGoodsBO = new DistributorAgreementGoodsBO();
            Long ylGoodsId = specificationAndYlGoodsMap.get(distributorGoods.getSellSpecificationsId());
            String agreementId = goodsAgreementMap.get(ylGoodsId);
            distributorAgreementGoodsBO.setYlGoodsId(ylGoodsId);
            distributorAgreementGoodsBO.setDistributorGoodsId(distributorGoods.getId());
            distributorAgreementGoodsBO.setAgreementIds(agreementId);
            return distributorAgreementGoodsBO;
        }).collect(Collectors.toList());
        Page<DistributorAgreementGoodsBO> resultPage = PojoUtils.map(distributorGoodsPage, DistributorAgreementGoodsBO.class);
        resultPage.setRecords(resultList);
        return resultPage;
    }

    @Override
    public Boolean batchUpdateGoodsStatus(BatchUpdateGoodsStatusRequest request) {
        if (request.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
            b2bGoodsService.updateGoodsStatus(request);
        }
        if (request.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
            popGoodsService.updateGoodsStatus(request);
        }
        return true;
    }

    @Override
    public Boolean batchUpdateGoodsOverSold(BatchUpdateGoodsOverSoldRequest request) {
        List<GoodsDTO> goodsDTOS = this.batchQueryInfo(request.getGoodsIds());
        if (CollectionUtils.isNotEmpty(goodsDTOS)) {
            List<Long> goodsIds = goodsDTOS.stream().map(GoodsDTO::getId).distinct().collect(Collectors.toList());
            //批量修改商品库存超卖type
            QueryWrapper<InventoryDO> inventoryWrapper = new QueryWrapper<>();
            inventoryWrapper.lambda().in(InventoryDO::getGid, goodsIds);
            InventoryDO inventoryDO = new InventoryDO();
            inventoryDO.setOverSoldType(request.getOverSoldType());
            inventoryService.update(inventoryDO, inventoryWrapper);
            //批量修改商品超卖type
            QueryWrapper<GoodsDO> goodsWrapper = new QueryWrapper<>();
            goodsWrapper.lambda().in(GoodsDO::getId, goodsIds);
            GoodsDO goodsDO = new GoodsDO();
            goodsDO.setOverSoldType(request.getOverSoldType());
            return this.update(goodsDO, goodsWrapper);
        } else {
            return false;
        }
    }

    //确定是同一个配送商品下面有多个商品信息取上架商品
    private GoodsDO getDistributionGoodsId(List<GoodsDO> list) {
        for (GoodsDO goodsDO : list) {
            PopGoodsDTO popGoodsDTO = this.popGoodsService.getPopGoodsByGoodsId(goodsDO.getId());
            if (popGoodsDTO == null) {
                continue;
            }
            if (popGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())) {
                return goodsDO;
            }
        }
        return list.get(0);
    }

    @Override
    public List<GoodsDTO> batchQueryInfo(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }
        List<GoodsDO> list = this.listByIds(goodsIds);
        List<Long> standardIds = list.stream().map(e -> e.getStandardId()).collect(Collectors.toList());
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        List<GoodsDTO> goodList = PojoUtils.map(list, GoodsDTO.class);
        goodList.forEach(e -> {
            //获取默认图片
            e.setPic(getDefaultUrlByStandardGoodsPicList(standardGoodsPicMap.get(e.getStandardId()), e.getSellSpecificationsId()));
        });
        return goodList;
    }

    @Override
    public List<GoodsSkuInfoDTO> batchQueryInfoBySkuIds(List<Long> skuIds) {
        List<GoodsSkuDO> goodsSkuDOList = goodsSkuService.listByIds(skuIds);
        if (CollUtil.isEmpty(goodsSkuDOList)) {
            return ListUtil.empty();
        }

        List<GoodsSkuDO> popList = goodsSkuDOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
        List<Long> popGoodsIds = popList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<PopGoodsDTO> popGoodsDTOList = popGoodsService.getPopGoodsListByGoodsIds(popGoodsIds);
        Map<Long, PopGoodsDTO> popMap = popGoodsDTOList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

        List<GoodsSkuDO> b2bList = goodsSkuDOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
        List<Long> b2bGoodsIds = b2bList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<B2bGoodsDTO> b2bGoodsDTOList = b2bGoodsService.getB2bGoodsListByGoodsIds(b2bGoodsIds);
        Map<Long, B2bGoodsDTO> b2bMap = b2bGoodsDTOList.stream().collect(Collectors.toMap(B2bGoodsDTO::getGoodsId, Function.identity()));

        List<Long> goodsIds = goodsSkuDOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<GoodsDTO> goodsInfoDTOList = batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> goodsMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));

        List<GoodsSkuInfoDTO> goodsSkuInfoDTOList = PojoUtils.map(goodsSkuDOList, GoodsSkuInfoDTO.class);
        goodsSkuInfoDTOList.forEach(e -> {
            GoodsInfoDTO goodsInfoDTO = PojoUtils.map(goodsMap.get(e.getGoodsId()), GoodsInfoDTO.class);
            if (e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
                B2bGoodsDTO b2bGoodsDTO = b2bMap.get(e.getGoodsId());
                if (b2bGoodsDTO != null) {
                    goodsInfoDTO.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
                    goodsInfoDTO.setOutReason(b2bGoodsDTO.getOutReason());
                }
            }
            if (e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
                PopGoodsDTO popGoodsDTO = popMap.get(e.getGoodsId());
                if (popGoodsDTO != null) {
                    goodsInfoDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
                    goodsInfoDTO.setOutReason(popGoodsDTO.getOutReason());
                    goodsInfoDTO.setIsPatent(popGoodsDTO.getIsPatent());
                }
            }
            e.setGoodsInfo(goodsInfoDTO);
        });
        return goodsSkuInfoDTOList;
    }

    @Override
    public List<Long> getGoodsIdsByEid(Long eid) {
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsDO::getEid, eid).in(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());

        List<GoodsDO> list = this.list(queryWrapper);
        return list.stream().map(e -> e.getId()).collect(Collectors.toList());
    }

    @Override
    public List<QueryStatusCountBO> getCountByEid(Long eid) {
        return this.getBaseMapper().getCountByEid(eid);
    }

    @Override
    public List<GoodsDO> getGoodsListByEid(Long eid) {
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GoodsDO::getEid, eid).eq(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());

        List<GoodsDO> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public GoodsDTO findGoodsAuditPassByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine) {
        List<GoodsSkuDTO> goodsSkuDTOList = goodsSkuService.getGoodsSkuListByEidAndInSnAndGoodsLine(eid, inSn, goodsLine);
        List<Long> goodsIds = goodsSkuDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(goodsIds)) {
            GoodsDTO goodsDTO = this.queryInfo(goodsIds.get(0));
            if (goodsDTO.getAuditStatus().equals(GoodsStatusEnum.AUDIT_PASS.getCode())) {
                return goodsDTO;
            }
        }
        return null;
    }

    @Override
    public GoodsDTO findGoodsByInSnAndEidAndGoodsLine(Long eid, String inSn, Integer goodsLine) {
        List<GoodsSkuDTO> goodsSkuDTOList = goodsSkuService.getGoodsSkuListByEidAndInSnAndGoodsLine(eid, inSn, goodsLine);
        List<Long> goodsIds = goodsSkuDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(goodsIds)) {
            List<GoodsDTO> goodsDTOList = this.batchQueryInfo(goodsIds);
            if (CollUtil.isNotEmpty(goodsDTOList)) {
                GoodsDTO goodsDTO = goodsDTOList.stream().filter(goods -> GoodsStatusEnum.AUDIT_PASS.getCode().equals(goods.getAuditStatus())).findFirst().orElse(null);
                //如果没有审核通过的商品，则取第一个
                if (ObjectUtil.isNull(goodsDTO)) {
                    goodsDTO = goodsDTOList.get(0);
                }
                return goodsDTO;
            }
        }
        return null;
    }

    @Override
    public GoodsDTO queryInfo(Long goodsId) {
        GoodsDO goodsDO = this.getById(goodsId);
        if (goodsDO == null) {
            return null;
        }
        GoodsDTO goodsInfoDTO = PojoUtils.map(goodsDO, GoodsDTO.class);
        //获取默认图片
        goodsInfoDTO.setPic(getDefaultUrl(goodsDO.getStandardId(), goodsDO.getSellSpecificationsId()));
        return goodsInfoDTO;
    }

    @Override
    public GoodsSkuInfoDTO getGoodsSkuInfoById(Long skuId) {
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(skuId);
        if (goodsSkuDO == null) {
            return null;
        }
        GoodsSkuInfoDTO goodsSkuInfoDTO = PojoUtils.map(goodsSkuDO, GoodsSkuInfoDTO.class);
        GoodsDTO goodsDTO = queryInfo(goodsSkuInfoDTO.getGoodsId());
        GoodsInfoDTO goodsInfoDTO = PojoUtils.map(goodsDTO, GoodsInfoDTO.class);

        if (goodsSkuDO.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsService.getB2bGoodsByGoodsId(goodsSkuDO.getGoodsId());
            goodsInfoDTO.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
            goodsInfoDTO.setOutReason(b2bGoodsDTO.getOutReason());
        }
        if (goodsSkuDO.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
            PopGoodsDTO popGoodsDTO = popGoodsService.getPopGoodsByGoodsId(goodsSkuDO.getGoodsId());
            goodsInfoDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            goodsInfoDTO.setOutReason(popGoodsDTO.getOutReason());
            goodsInfoDTO.setIsPatent(popGoodsDTO.getIsPatent());
        }

        goodsSkuInfoDTO.setGoodsInfo(goodsInfoDTO);
        return goodsSkuInfoDTO;
    }

    @Override
    public GoodsFullDTO queryFullInfo(Long goodsId) {
        GoodsDO goodsDO = this.getById(goodsId);
        if (goodsDO == null) {
            throw new BusinessException(GoodsErrorCode.GOODS_AUDIT_NOT_EXIST);
        }
        GoodsFullDTO goodsFullDTO = PojoUtils.map(goodsDO, GoodsFullDTO.class);
        if (goodsFullDTO.getStandardId() != null && goodsFullDTO.getStandardId() != 0) {
            //赋值全部的说明书
            StandardGoodsAllInfoDTO standardGoodsAllInfoDTO = standardGoodsService.getStandardGoodsById(goodsFullDTO.getStandardId());
            if (goodsFullDTO.getSellSpecificationsId() != null && goodsFullDTO.getSellSpecificationsId() != 0) {
                //标准库规格信息
                List<StandardSpecificationInfoDTO> specificationInfoList = standardGoodsAllInfoDTO.getSpecificationInfo();
                //标准库商品图片
                List<StandardGoodsPicDTO> picBasicsInfoList = standardGoodsAllInfoDTO.getPicBasicsInfoList();
                //获取到的图片信息
                List<StandardGoodsPicDTO> getPicBasicsInfoList = new ArrayList<>();
                if (CollUtil.isNotEmpty(specificationInfoList)) {
                    specificationInfoList.forEach(e -> {
                        if (e.getId().equals(goodsFullDTO.getSellSpecificationsId())) {
                            getPicBasicsInfoList.addAll(e.getPicInfoList());
                        }
                    });

                    if (CollUtil.isNotEmpty(getPicBasicsInfoList)) {
                        picBasicsInfoList = getPicBasicsInfoList;
                    }

                    if (CollUtil.isNotEmpty(picBasicsInfoList)) {
                        if (picBasicsInfoList.size() > 5) {
                            picBasicsInfoList = picBasicsInfoList.subList(0, 5);
                        }
                    }
                    standardGoodsAllInfoDTO.setPicBasicsInfoList(picBasicsInfoList);
                }
            }
            goodsFullDTO.setStandardGoodsAllInfo(standardGoodsAllInfoDTO);
            //获取默认图片
            goodsFullDTO.setPic(getDefaultUrl(goodsDO.getStandardId(), goodsDO.getSellSpecificationsId()));
        } else {
            //没有匹配上取自己的说明书和图片
            StandardGoodsAllInfoDTO standardGoodsAllInfoDTO = new StandardGoodsAllInfoDTO();
            if(null != goodsFullDTO.getGoodsType() && goodsFullDTO.getGoodsType() != 0){
                StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(goodsFullDTO.getGoodsType());
                switch (goodsTypeEnum){
                    case GOODS_TYPE:
                        //药品说明书
                        QueryWrapper<InstructionsGoodsDO> goodsWrapper = new QueryWrapper<>();
                        goodsWrapper.lambda().eq(InstructionsGoodsDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setGoodsInstructionsInfo(PojoUtils.map(instructionsGoodsService.getOne(goodsWrapper), StandardInstructionsGoodsDTO.class));
                        break;
                    case DECOCTION_TYPE:
                        //中药饮片
                        QueryWrapper<InstructionsDecoctionDO> decoctionWrapper = new QueryWrapper<>();
                        decoctionWrapper.lambda().eq(InstructionsDecoctionDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setDecoctionInstructionsInfo(PojoUtils.map(instructionsDecoctionService.getOne(decoctionWrapper), StandardInstructionsDecoctionDTO.class));
                        break;
                    case MATERIAL_TYPE:
                        //中药材
                        QueryWrapper<InstructionsMaterialsDO> materialsWrapper = new QueryWrapper<>();
                        materialsWrapper.lambda().eq(InstructionsMaterialsDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setMaterialsInstructionsInfo(PojoUtils.map(instructionsMaterialsService.getOne(materialsWrapper), StandardInstructionsMaterialsDTO.class));
                        break;
                    case DISINFECTION_TYPE:
                        //消杀品
                        QueryWrapper<InstructionsDisinfectionDO> disinfectionWrapper = new QueryWrapper<>();
                        disinfectionWrapper.lambda().eq(InstructionsDisinfectionDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setDisinfectionInstructionsInfo(PojoUtils.map(instructionsDisinfectionService.getOne(disinfectionWrapper), StandardInstructionsDisinfectionDTO.class));
                        break;
                    case HEALTH_TYPE:
                        //保健食品
                        QueryWrapper<InstructionsHealthDO> healthWrapper = new QueryWrapper<>();
                        healthWrapper.lambda().eq(InstructionsHealthDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setHealthInstructionsInfo(PojoUtils.map(instructionsHealthService.getOne(healthWrapper), StandardInstructionsHealthDTO.class));
                        break;
                    case FOODS_TYPE:
                        //食品
                        QueryWrapper<InstructionsFoodsDO> foodsWrapper = new QueryWrapper<>();
                        foodsWrapper.lambda().eq(InstructionsFoodsDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setFoodsInstructionsInfo(PojoUtils.map(instructionsFoodsService.getOne(foodsWrapper), StandardInstructionsFoodsDTO.class));
                        break;
                    case MEDICAL_INSTRUMENT_TYPE:
                        //医疗器械
                        QueryWrapper<InstructionsMedicalInstrumentDO> medicalInstrumentWrapper = new QueryWrapper<>();
                        medicalInstrumentWrapper.lambda().eq(InstructionsMedicalInstrumentDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setMedicalInstrumentInfo(PojoUtils.map(instructionsMedicalInstrumentService.getOne(medicalInstrumentWrapper), StandardInstructionsMedicalInstrumentDTO.class));
                        break;
                    case DISPENSING_GRANULE_TYPE:
                        //配方颗粒
                        QueryWrapper<InstructionsDispensingGranuleDO> dispensingGranuleWrapper = new QueryWrapper<>();
                        dispensingGranuleWrapper.lambda().eq(InstructionsDispensingGranuleDO::getGoodsId, goodsFullDTO.getId());
                        standardGoodsAllInfoDTO.setDispensingGranuleInfo(PojoUtils.map(instructionsDispensingGranuleService.getOne(dispensingGranuleWrapper), StandardInstructionsDispensingGranuleDTO.class));
                        break;
                }
            }
            //获取图片
            QueryWrapper<GoodsPicDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(GoodsPicDO::getGoodsId, goodsFullDTO.getId());
            List<GoodsPicDO> goodsPicDOList = goodsPicService.list(queryWrapper);

            //获取规格
            List<StandardSpecificationInfoDTO> specificationInfo = new ArrayList<>();
            StandardSpecificationInfoDTO standardSpecificationInfoDTO = new StandardSpecificationInfoDTO();
            standardSpecificationInfoDTO.setSellSpecifications(goodsFullDTO.getSpecifications());
            standardSpecificationInfoDTO.setUnit(goodsFullDTO.getUnit());
            standardSpecificationInfoDTO.setYpidCode("");
            standardSpecificationInfoDTO.setPicInfoList(PojoUtils.map(goodsPicDOList, StandardGoodsPicDTO.class));
            specificationInfo.add(standardSpecificationInfoDTO);

            //获取商品基本信息
            StandardGoodsBasicInfoDTO baseInfo = PojoUtils.map(goodsDO, StandardGoodsBasicInfoDTO.class);
            if (baseInfo.getGoodsType() == 0) {
                baseInfo.setGoodsType(1);
            }
            standardGoodsAllInfoDTO.setSpecificationInfo(specificationInfo);
            standardGoodsAllInfoDTO.setPicBasicsInfoList(PojoUtils.map(goodsPicDOList, StandardGoodsPicDTO.class));
            standardGoodsAllInfoDTO.setBaseInfo(baseInfo);
            goodsFullDTO.setStandardGoodsAllInfo(standardGoodsAllInfoDTO);

        }

        return goodsFullDTO;
    }

    @Override
    public GoodsSkuFullDTO queryFullInfoBySkuId(Long skuId) {
        GoodsSkuDO goodsSkuDO = goodsSkuService.getById(skuId);

        GoodsSkuFullDTO goodsSkuFullDTO = PojoUtils.map(goodsSkuDO, GoodsSkuFullDTO.class);
        GoodsFullDTO goodsFullDTO = queryFullInfo(goodsSkuFullDTO.getGoodsId());

        if (goodsSkuDO.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsService.getB2bGoodsByGoodsId(goodsSkuDO.getGoodsId());
            goodsFullDTO.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
            goodsFullDTO.setOutReason(b2bGoodsDTO.getOutReason());
        }
        if (goodsSkuDO.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
            PopGoodsDTO popGoodsDTO = popGoodsService.getPopGoodsByGoodsId(goodsSkuDO.getGoodsId());
            goodsFullDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            goodsFullDTO.setOutReason(popGoodsDTO.getOutReason());
            goodsFullDTO.setIsPatent(popGoodsDTO.getIsPatent());
        }

        goodsSkuFullDTO.setGoodsInfo(goodsFullDTO);
        return goodsSkuFullDTO;
    }

    @Override
    public List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return ListUtil.empty();
        }
        //批量获取商品基本信息
        List<GoodsDO> list = this.listByIds(goodsIds);
        List<Long> standardIds = list.stream().map(e -> e.getStandardId()).collect(Collectors.toList());
        //批量获取标准库信息
        List<StandardGoodsDO> standardGoodsDOList = standardGoodsService.listByIds(standardIds);
        Map<Long, StandardGoodsDO> standardGoodsDOMap = standardGoodsDOList.stream().collect(Collectors.toMap(StandardGoodsDO::getId, Function.identity()));
        //批量获取图片信息
        Map<Long, List<StandardGoodsPicDO>> standardGoodsPicMap = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);

        List<StandardGoodsBasicDTO> goodList = PojoUtils.map(list, StandardGoodsBasicDTO.class);
        goodList.forEach(e -> {
            if (e.getStandardId() != null && e.getStandardId() != 0) {
                if (standardGoodsDOMap != null && standardGoodsDOMap.get(e.getStandardId()) != null) {
                    StandardGoodsDO standardGoodsDO = standardGoodsDOMap.get(e.getStandardId());
                    StandardGoodsBasicInfoDTO infoDTO = PojoUtils.map(standardGoodsDO, StandardGoodsBasicInfoDTO.class);
                    e.setStandardGoods(infoDTO);
                }
                //获取默认图片
                e.setPic(getDefaultUrlByStandardGoodsPicList(standardGoodsPicMap.get(e.getStandardId()), e.getSellSpecificationsId()));
            }
        });
        return goodList;
    }

    @Override
    public List<GoodsSkuStandardBasicDTO> batchQueryStandardGoodsBasicBySkuIds(List<Long> skuIds) {
        List<GoodsSkuDO> goodsSkuDOList = goodsSkuService.listByIds(skuIds);

        List<GoodsSkuDO> popList = goodsSkuDOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
        List<Long> popGoodsIds = popList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<PopGoodsDTO> popGoodsDTOList = popGoodsService.getPopGoodsListByGoodsIds(popGoodsIds);
        Map<Long, PopGoodsDTO> popMap = popGoodsDTOList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

        List<GoodsSkuDO> b2bList = goodsSkuDOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
        List<Long> b2bGoodsIds = b2bList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<B2bGoodsDTO> b2bGoodsDTOList = b2bGoodsService.getB2bGoodsListByGoodsIds(b2bGoodsIds);
        Map<Long, B2bGoodsDTO> b2bMap = b2bGoodsDTOList.stream().collect(Collectors.toMap(B2bGoodsDTO::getGoodsId, Function.identity()));


        List<Long> goodsIds = goodsSkuDOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = batchQueryStandardGoodsBasic(goodsIds);
        Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDTOMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        List<GoodsSkuStandardBasicDTO> goodList = PojoUtils.map(goodsSkuDOList, GoodsSkuStandardBasicDTO.class);
        goodList.forEach(e -> {
            StandardGoodsBasicDTO goodsInfoDTO = standardGoodsBasicDTOMap.get(e.getGoodsId());
            if (e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())) {
                B2bGoodsDTO b2bGoodsDTO = b2bMap.get(e.getGoodsId());
                if(null!=b2bGoodsDTO){
                    goodsInfoDTO.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
                    goodsInfoDTO.setOutReason(b2bGoodsDTO.getOutReason());
                }
            }
            if (e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())) {
                PopGoodsDTO popGoodsDTO = popMap.get(e.getGoodsId());
                if(null!=popGoodsDTO){
                    goodsInfoDTO.setGoodsStatus(popGoodsDTO.getGoodsStatus());
                    goodsInfoDTO.setOutReason(popGoodsDTO.getOutReason());
                    goodsInfoDTO.setIsPatent(popGoodsDTO.getIsPatent());
                }
            }
            e.setStandardGoodsBasic(goodsInfoDTO);
        });
        return goodList;
    }


    @Override
    public Page<GoodsLogPageListItemDTO> queryGoodsLogPageList(QueryGoodsLogRequest request) {
        Page<GoodsLogPageListItemDTO> result = null;
        //操作人id列表
        Set<Long> userIds = new HashSet<>();

        QueryWrapper<GoodsLogDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StrUtil.isNotEmpty(request.getName()), GoodsLogDO::getName, request.getName()).eq(StrUtil.isNotEmpty(request.getLicenseNo()), GoodsLogDO::getLicenseNo, request.getLicenseNo()).eq(request.getGid() != null, GoodsLogDO::getGid, request.getGid()).eq(StrUtil.isNotEmpty(request.getModifyColumn()), GoodsLogDO::getModifyColumn, request.getModifyColumn()).ge(request.getStartTime() != null, GoodsLogDO::getCreateTime, request.getStartTime()).le(request.getEndTime() != null, GoodsLogDO::getCreateTime, request.getEndTime()).in(CollUtil.isNotEmpty(request.getOperUserList()), GoodsLogDO::getCreateUser, request.getOperUserList());
        Page<GoodsLogDO> page = new Page<>(request.getCurrent(), request.getSize());
        Page<GoodsLogDO> goodsLogDOPage = goodsLogService.page(page, queryWrapper);
        return PojoUtils.map(page, GoodsLogPageListItemDTO.class);
    }

    @Override
    public List<GoodsDTO> findGoodsBySellSpecificationsIdAndEid(List<Long> sellSpecificationsIdList, List<Long> eids) {
        if (CollUtil.isEmpty(sellSpecificationsIdList) || ObjectUtil.isNull(eids)) {
            return ListUtil.empty();
        }
        QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GoodsDO::getEid, eids);
        queryWrapper.lambda().in(GoodsDO::getSellSpecificationsId, sellSpecificationsIdList);
        queryWrapper.lambda().in(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());
        List<GoodsDO> goodsDOList = this.list(queryWrapper);
        if (CollUtil.isEmpty(goodsDOList)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(goodsDOList, GoodsDTO.class);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsId(Long goodsId) {
        return goodsSkuService.getGoodsSkuByGoodsId(goodsId);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdAndStatus(Long goodsId,List<Integer> statusList){
        return goodsSkuService.getGoodsSkuByGoodsIdAndStatus(goodsId,statusList);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIds(List<Long> goodsIds) {
        return goodsSkuService.getGoodsSkuByGoodsIds(goodsIds);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByGoodsIdsAndStatus(List<Long> goodsIds,List<Integer> statusList){
        return goodsSkuService.getGoodsSkuByGoodsIdsAndStatus(goodsIds,statusList);
    }

    @Override
    public List<GoodsSkuDTO> getGoodsSkuByIds(List<Long> skuIds) {
        return goodsSkuService.getGoodsSkuByIds(skuIds);
    }


    @Override
    public Boolean isWaitSetGoodsStatus(Integer goodsLine, Long goodsId) {
        List<GoodsSkuDTO> list = goodsSkuService.getGoodsSkuByGoodsIdAndStatus(goodsId,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
        GoodsDTO goodsDTO = this.queryInfo(goodsId);
        if (goodsLine.equals(GoodsLineEnum.B2B.getCode())) {
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsService.getB2bGoodsByGoodsId(goodsId);
            if (b2bGoodsDTO == null) {
                return false;
            }

            if (goodsDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            List<GoodsSkuDTO> b2bList = list.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
            if (CollUtil.isEmpty(b2bList)) {
                return false;
            }
        }
        if (goodsLine.equals(GoodsLineEnum.POP.getCode())) {
            PopGoodsDTO popGoodsDTO = popGoodsService.getPopGoodsByGoodsId(goodsId);
            if (popGoodsDTO == null) {
                return false;
            }

            if (goodsDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            List<GoodsSkuDTO> b2bList = list.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
            return !CollUtil.isEmpty(b2bList);
        }
        return true;
    }

    @Override
    public Map<Long, Long> getYilingGoodsIdByGoodsIdAndYilingEids(List<Long> goodsIds, List<Long> yilingEids) {
        if (CollUtil.isEmpty(goodsIds) || CollUtil.isEmpty(yilingEids)) {
            return MapUtil.empty();
        }

        List<GoodsDTO> goodsDTOList = this.batchQueryInfo(goodsIds);
        List<Long> sellSpecificationsIds = goodsDTOList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());

        List<GoodsDTO> goodsList = this.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIds, yilingEids);
        Map<Long, Long> yilingGoodsIdMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getSellSpecificationsId, GoodsDTO::getId,(v1, v2)-> v2));

        Map<Long, Long> returnMap = new HashMap<>();
        for (GoodsDTO goodsDTO : goodsDTOList) {
            returnMap.put(goodsDTO.getId(), yilingGoodsIdMap.getOrDefault(goodsDTO.getSellSpecificationsId(), 0L));
        }
        return returnMap;
    }

    @Override
    public Boolean updateSkuStatusByEidAndInSn(Long eid, String inSn, Integer status, Long updater) {
        return goodsSkuService.updateStatusByEidAndInSn(eid, inSn, status, updater);
    }

    @Override
    public String goodsMerge(MergeGoodsRequest mergeGoodsReques) {
        //判断类型为合并并且后面有值的情况
        if (mergeGoodsReques.getType().equals("合并")) {
            //在查询正确的标准库规格 id1
            StandardGoodsDO standardGoodsDO = standardGoodsService.getById(mergeGoodsReques.getStandardId());
            if (standardGoodsDO == null) {
                return "标准库商品信息为空";
            }
            StandardGoodsSpecificationDTO mergeStandardGoodsSpecification = standardGoodsSpecificationService.getStandardGoodsSpecification(mergeGoodsReques.getMergeSpecificationsId());
            if (null == mergeStandardGoodsSpecification) {
                return "合并规格ID未找到规格";
            }

            if (!mergeStandardGoodsSpecification.getStandardId().equals(standardGoodsDO.getId())) {
                return "合并规格ID不属于标准库ID："+standardGoodsDO.getId();
            }

            Long mergeSpecificationId = mergeStandardGoodsSpecification.getId();
            String mergeSellSpecifications = mergeStandardGoodsSpecification.getSellSpecifications();
            String unit = mergeStandardGoodsSpecification.getUnit();
            //先查询需要处理的标准库规格 id2
            StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationService.getStandardGoodsSpecification(mergeGoodsReques.getSpecificationsId());
            if (null ==standardGoodsSpecification) {
                return "包装规格ID未找到规格";
            }

            if (!standardGoodsSpecification.getStandardId().equals(standardGoodsDO.getId())) {
                return "包装规格ID不属于标准库ID："+standardGoodsDO.getId();
            }

            Long specificationId = standardGoodsSpecification.getId();
            //然后通过查询商品信息id2的商品信息 id2->id1
            QueryWrapper<GoodsDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(GoodsDO::getSellSpecificationsId, specificationId);
            queryWrapper.lambda().in(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());
            List<GoodsDO> goodsDOList = this.list(queryWrapper);
            if (CollUtil.isEmpty(goodsDOList)) {
                return "包装规格商品查询为空";
            }

            for (GoodsDO goodsDO : goodsDOList) {
                QueryWrapper<GoodsDO> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.lambda().eq(GoodsDO::getEid, goodsDO.getEid());
                queryWrapper1.lambda().eq(GoodsDO::getSellSpecificationsId, mergeSpecificationId);
                queryWrapper1.lambda().eq(GoodsDO::getAuditStatus, GoodsStatusEnum.AUDIT_PASS.getCode());
                List<GoodsDO> mergeGoodsDOList = this.list(queryWrapper1);
                if (mergeGoodsDOList.size() > 1) {
                    continue;
                }
                if (CollUtil.isEmpty(mergeGoodsDOList)) {
                    //没有就直接替换
                    goodsDO.setRemark("合并规格处理");
                    goodsDO.setOpUserId(mergeGoodsReques.getOpUserId());
                    goodsDO.setSellSpecificationsId(mergeSpecificationId);
                    goodsDO.setSellSpecifications(mergeSellSpecifications);
                    goodsDO.setSellUnit(unit);
                    this.updateById(goodsDO);
                    continue;
                }

                //如果有 就要把循环的商品Id替换成正常的商品Id
                GoodsDO goodsDOOld = mergeGoodsDOList.get(0);
                goodsDO.setRemark("合并规格处理");
                goodsDO.setOpUserId(mergeGoodsReques.getOpUserId());
                goodsDO.setAuditStatus(7);
                goodsDO.setSellSpecificationsId(mergeSpecificationId);
                goodsDO.setSellSpecifications(mergeSellSpecifications);
                goodsDO.setSellUnit(unit);
                this.updateById(goodsDO);

                BatchUpdateGoodsStatusRequest batchUpdateGoodsStatusRequest=new BatchUpdateGoodsStatusRequest();
                batchUpdateGoodsStatusRequest.setOutReason(2);
                batchUpdateGoodsStatusRequest.setGoodsStatus(2);
                batchUpdateGoodsStatusRequest.setGoodsIds(Arrays.asList(goodsDO.getId()));
                batchUpdateGoodsStatusRequest.setOpUserId(mergeGoodsReques.getOpUserId());
                {
                    batchUpdateGoodsStatusRequest.setGoodsLine(GoodsLineEnum.POP.getCode());
                    popGoodsService.updateGoodsStatus(batchUpdateGoodsStatusRequest);

                    batchUpdateGoodsStatusRequest.setGoodsLine(GoodsLineEnum.B2B.getCode());
                    b2bGoodsService.updateGoodsStatus(batchUpdateGoodsStatusRequest);

                }
                {
                    batchUpdateGoodsStatusRequest.setGoodsLineStatusEnum(GoodsLineStatusEnum.NOT_ENABLED);
                    popGoodsService.updatePopLineStatus(batchUpdateGoodsStatusRequest);
                    b2bGoodsService.updateB2bLineStatus(batchUpdateGoodsStatusRequest);
                }
                List<GoodsSkuDTO> goodsSkuDTOList = goodsSkuService.getGoodsSkuByGoodsIdAndStatus(goodsDO.getId(),null);
                for (GoodsSkuDTO goodsSkuDTO : goodsSkuDTOList) {
                    goodsSkuDTO.setGoodsId(goodsDOOld.getId());
                    GoodsSkuDO goodsSkuDO = PojoUtils.map(goodsSkuDTO, GoodsSkuDO.class);
                    goodsSkuDO.setOpUserId(mergeGoodsReques.getOpUserId());
                    goodsSkuService.updateById(goodsSkuDO);
                    Long inventoryId = goodsSkuDTO.getInventoryId();
                    InventoryDO inventoryDO = new InventoryDO();
                    inventoryDO.setId(inventoryId);
                    inventoryDO.setGid(goodsDOOld.getId());
                    inventoryDO.setOpUserId(mergeGoodsReques.getOpUserId());
                    inventoryDO.setRemark("合并规格处理");
                    inventoryService.updateById(inventoryDO);
                }
            }
            StandardGoodsSpecificationDO standardGoodsSpecificationDO = new StandardGoodsSpecificationDO();
            standardGoodsSpecificationDO.setId(specificationId);
            standardGoodsSpecificationDO.setOpUserId(mergeGoodsReques.getOpUserId());
            standardGoodsSpecificationService.deleteByIdWithFill(standardGoodsSpecificationDO);
        } else {
            return "数据格式不正确";
        }
        return null;
    }

    @Override
    public GoodsDTO getYlGoodsByEidAndInSn(Long eid, String inSn, List<Long> yilingEids) {
        GoodsDTO goods = this.findGoodsAuditPassByInSnAndEidAndGoodsLine(eid, inSn, null);
        if(null!=goods){
            List<GoodsDTO> ylGoodsList = this.findGoodsBySellSpecificationsIdAndEid(Lists.newArrayList(goods.getSellSpecificationsId()), yilingEids);
            if(CollectionUtils.isNotEmpty(ylGoodsList)){
                return ylGoodsList.get(0);
            }
        }
        return null;
    }

    @Override
    public GoodsSkuDTO getGoodsSkuById(Long skuId) {
        return goodsSkuService.getGoodsSkuById(skuId);
    }

    @Override
    public List<InventorySubscriptionDTO> getInventoryDetailByInventoryId(Long inventoryId) {
        QueryInventorySubscriptionRequest request = new QueryInventorySubscriptionRequest();
        request.setInventoryId(inventoryId);
        request.setIsIncludeSelf(false);
        return inventorySubscriptionService.getInventorySubscriptionList(request);
    }

    @Override
    public List<GoodsLineBO> getGoodsLineByGoodsIds(List<Long> goodsIdList) {
        List<GoodsLineBO> list = ListUtil.toList();
        if(CollectionUtil.isEmpty(goodsIdList)){
            return list;
        }
        List<B2bGoodsDTO> b2bGoodsList = b2bGoodsService.getB2bGoodsListByGoodsIds(goodsIdList);
        Map<Long, B2bGoodsDTO> b2bGoodsMap = b2bGoodsList.stream().collect(Collectors.toMap(B2bGoodsDTO::getGoodsId, Function.identity(), (g1, g2) -> g1));
        List<PopGoodsDTO> popGoodsList = popGoodsService.getPopGoodsListByGoodsIds(goodsIdList);
        Map<Long, PopGoodsDTO> popGoodsMap = popGoodsList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity(), (g1, g2) -> g1));

        goodsIdList.forEach(id->{
            GoodsLineBO bo = new GoodsLineBO();
            bo.setGoodsId(id);
            B2bGoodsDTO b2bGoods = b2bGoodsMap.get(id);
            if(null!=b2bGoods){
                bo.setMallStatus(b2bGoods.getStatus());
            }else {
                bo.setMallStatus(GoodsLineStatusEnum.NOT_ENABLED.getCode());
            }
            PopGoodsDTO popGoods = popGoodsMap.get(id);
            if(null!=popGoods){
                bo.setPopStatus(popGoods.getStatus());
            }else {
                bo.setPopStatus(GoodsLineStatusEnum.NOT_ENABLED.getCode());
            }
            list.add(bo);
        });
        return list;
    }

    @Override
    public Boolean updateShelfLife(UpdateShelfLifeRequest request) {
        log.info("更新商品保质期，request：{}", JSONUtil.toJsonStr(request));
        Assert.notNull(request.getGoodsId(),"更新保质期，商品id不能为空");
        this.baseMapper.updateShelfLife(request);
        return true;
    }

    /**
     * 判断批准文号是否为普通药品和进口药品
     */
    public Boolean isStandardGoods(String licenseNo) {
        ///^国药准字[HZSBTFJ][1-6]\d{7}$/ 正则表达式
        if (StringUtils.isEmpty(licenseNo)) {
            return false;
        }
        String regex = "^国药准字[HZSBTFJ][1-6]\\d{7}$";
        //判断功能
        boolean flag = licenseNo.matches(regex);
        if (flag) {
            return true;
        }

        //判断是否为进口药品 以注册证号开头
        String zc = "注册证号";
        return licenseNo.startsWith(zc);
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg,Long id) {
        Integer intId=null;
        if(null !=id && 0<id){
            intId=id.intValue();
        }
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg, intId);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg,Integer id) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg,id);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

}
