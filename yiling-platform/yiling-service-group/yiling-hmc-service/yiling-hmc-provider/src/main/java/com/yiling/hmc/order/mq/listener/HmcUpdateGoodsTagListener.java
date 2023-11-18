package com.yiling.hmc.order.mq.listener;

import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.beust.jcommander.internal.Lists;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDecoctionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDisinfectionDTO;
import com.yiling.goods.standard.dto.StandardInstructionsDispensingGranuleDTO;
import com.yiling.goods.standard.dto.StandardInstructionsFoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMaterialsDTO;
import com.yiling.goods.standard.dto.StandardInstructionsMedicalInstrumentDTO;
import com.yiling.goods.standard.enums.StandardGoodsTypeEnum;
import com.yiling.hmc.config.SyncGoodsConfig;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.request.HmcSyncGoodsRequest;

import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * HMC 修改商品标签消息监听器
 *
 * @author: fan.shen
 * @date: 2023/5/6
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_UPDATE_GOODS_TAG, consumerGroup = Constants.TOPIC_HMC_UPDATE_GOODS_TAG)
public class HmcUpdateGoodsTagListener extends AbstractMessageListener {

    @Autowired
    OrderService orderService;

    @Autowired
    SyncGoodsConfig syncGoodsConfig;

    @Autowired
    OrderDetailService orderDetailService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    StandardGoodsSpecificationApi specificationApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @MdcLog
    @Override
    @GlobalTransactional
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        log.info("[HmcUpdateGoodsTagListener]HMC监听器收到消息：{}", body);
        StandardGoodsAllInfoDTO standardGoodsAllInfoDTO = standardGoodsApi.getStandardGoodsById(Long.valueOf(body));
        List<StandardGoodsSpecificationDTO> specificationDTOS = specificationApi.getListStandardGoodsSpecification(Lists.newArrayList(Long.valueOf(body)));

        log.info("品：{}", JSONUtil.toJsonStr(standardGoodsAllInfoDTO));
        log.info("规：{}", JSONUtil.toJsonStr(specificationDTOS));

        // 处方类型：1处方药 2甲类非处方药 3乙类非处方药 4其他
        Integer otcType = standardGoodsAllInfoDTO.getBaseInfo().getOtcType();

        // 是否为处方药 0:不是 1:是
        String isPrescriptionDrug;
        if (otcType == 1) {
            isPrescriptionDrug = "1";
        } else {
            isPrescriptionDrug = "0";
        }
        // 药品类型(1,2为西药 3,4为中药) 1：西药 2：中成药 3：中药饮片 4：中草药颗粒
        Integer drugType = null;
        Long standardCategoryId1 = standardGoodsAllInfoDTO.getBaseInfo().getStandardCategoryId1();

        if (Objects.equals(syncGoodsConfig.getWesternMedicine(), standardCategoryId1)) {
            drugType = 1;
        }
        if (Objects.equals(syncGoodsConfig.getChinesePatentMedicine(), standardCategoryId1)) {
            drugType = 2;
        }
        if (Objects.equals(syncGoodsConfig.getChineseHerbalDecoctionPiece(), standardCategoryId1)) {
            drugType = 3;
            // 中药饮片统一处理为 -> 处方药
            isPrescriptionDrug = "1";
        }
        if (Objects.equals(syncGoodsConfig.getDispensingGranules(), standardCategoryId1)) {
            drugType = 4;
            isPrescriptionDrug = "1";
        }
        if (Objects.equals(syncGoodsConfig.getIntrahospitalPreparations(), standardCategoryId1)) {
            drugType = 2;
        }

        if (Objects.isNull(drugType)) {
            log.info("[HmcUpdateGoodsTagListener] 药品类型drugType is NULL");
            return MqAction.CommitMessage;
        }

        for (StandardGoodsSpecificationDTO specification : specificationDTOS) {
            HmcSyncGoodsRequest request = new HmcSyncGoodsRequest();
            request.setHmcGoodsId(specification.getStandardId().intValue());
            request.setHmcSellSpecificationsId(specification.getId().intValue());
            request.setYpidCode(specification.getYpidCode());
            request.setIsPrescriptionDrug(isPrescriptionDrug);
            request.setGeneralName(standardGoodsAllInfoDTO.getBaseInfo().getName());
            request.setName(standardGoodsAllInfoDTO.getBaseInfo().getName());
            request.setDrugType(drugType);
            request.setSpec(specification.getSellSpecifications());
            request.setPreparationForm(standardGoodsAllInfoDTO.getBaseInfo().getGdfName());
            request.setProductCompany(standardGoodsAllInfoDTO.getBaseInfo().getManufacturer());
            request.setSpecificationsUnit(specification.getUnit());
            request.setApprovalNumber(standardGoodsAllInfoDTO.getBaseInfo().getLicenseNo());
            request.setPreparationFormCode("0");
            StandardGoodsTypeEnum goodsTypeEnum = StandardGoodsTypeEnum.find(standardGoodsAllInfoDTO.getBaseInfo().getGoodsType());
            StandardInstructionsGoodsDTO goodsInstructionsInfo = null;
            switch (goodsTypeEnum) {
                case GOODS_TYPE:
                    //1普通药品
                    goodsInstructionsInfo = standardGoodsAllInfoDTO.getGoodsInstructionsInfo();
                    break;
                case DECOCTION_TYPE:
                    //2中药饮片
                    StandardInstructionsDecoctionDTO decoctionInstructionsInfo = standardGoodsAllInfoDTO.getDecoctionInstructionsInfo();
                    goodsInstructionsInfo = PojoUtils.map(decoctionInstructionsInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setStorageConditions(Objects.nonNull(decoctionInstructionsInfo) ? decoctionInstructionsInfo.getStore() : "");
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(decoctionInstructionsInfo) ? decoctionInstructionsInfo.getExpirationDate() : "");
                    break;
                case MATERIAL_TYPE:
                    //3中药材
                    StandardInstructionsMaterialsDTO materialsInstructionsInfo = standardGoodsAllInfoDTO.getMaterialsInstructionsInfo();
                    goodsInstructionsInfo = PojoUtils.map(materialsInstructionsInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setStorageConditions(Objects.nonNull(materialsInstructionsInfo) ? materialsInstructionsInfo.getStore() : "");
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(materialsInstructionsInfo) ? materialsInstructionsInfo.getExpirationDate() : "");
                    goodsInstructionsInfo.setIndications(Objects.nonNull(materialsInstructionsInfo) ? materialsInstructionsInfo.getEffect() : "");
                    break;
                case DISINFECTION_TYPE:
                    //4消杀
                    StandardInstructionsDisinfectionDTO disinfectionInstructionsInfo = standardGoodsAllInfoDTO.getDisinfectionInstructionsInfo();
                    goodsInstructionsInfo = PojoUtils.map(disinfectionInstructionsInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(disinfectionInstructionsInfo) ? disinfectionInstructionsInfo.getExpirationDate() : "");
                    goodsInstructionsInfo.setIndications(Objects.nonNull(disinfectionInstructionsInfo) ? disinfectionInstructionsInfo.getSterilizationCategory() : "");
                    break;
                case HEALTH_TYPE:
                    //5保健食品
                    StandardInstructionsHealthDTO healthInstructionsInfo = standardGoodsAllInfoDTO.getHealthInstructionsInfo();
                    goodsInstructionsInfo = PojoUtils.map(healthInstructionsInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setStorageConditions(Objects.nonNull(healthInstructionsInfo) ? healthInstructionsInfo.getStore() : "");
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(healthInstructionsInfo) ? healthInstructionsInfo.getExpirationDate() : "");
                    break;
                case FOODS_TYPE:
                    //6食品
                    StandardInstructionsFoodsDTO foodsInstructionsInfo = standardGoodsAllInfoDTO.getFoodsInstructionsInfo();
                    goodsInstructionsInfo = PojoUtils.map(foodsInstructionsInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setStorageConditions(Objects.nonNull(foodsInstructionsInfo) ? foodsInstructionsInfo.getStore() : "");
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(foodsInstructionsInfo) ? foodsInstructionsInfo.getExpirationDate() : "");
                    break;
                case MEDICAL_INSTRUMENT_TYPE:
                    //7医疗器械
                    StandardInstructionsMedicalInstrumentDTO medicalInstrumentInfo = standardGoodsAllInfoDTO.getMedicalInstrumentInfo();
                    goodsInstructionsInfo = PojoUtils.map(medicalInstrumentInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(medicalInstrumentInfo) ? medicalInstrumentInfo.getExpirationDate() : "");
                    goodsInstructionsInfo.setIndications(Objects.nonNull(medicalInstrumentInfo) ? medicalInstrumentInfo.getUseScope() : "");
                    break;
                case DISPENSING_GRANULE_TYPE:
                    //8配方颗粒
                    StandardInstructionsDispensingGranuleDTO dispensingGranuleInfo = standardGoodsAllInfoDTO.getDispensingGranuleInfo();
                    goodsInstructionsInfo = PojoUtils.map(dispensingGranuleInfo, StandardInstructionsGoodsDTO.class);
                    goodsInstructionsInfo.setDrugDetails(Objects.nonNull(dispensingGranuleInfo) ? dispensingGranuleInfo.getNetContent() : "");
                    goodsInstructionsInfo.setStorageConditions(Objects.nonNull(dispensingGranuleInfo) ? dispensingGranuleInfo.getStore() : "");
                    goodsInstructionsInfo.setShelfLife(Objects.nonNull(dispensingGranuleInfo) ? dispensingGranuleInfo.getExpirationDate() : "");
                    break;
            }
            if (Objects.nonNull(goodsInstructionsInfo)) {
                request.setUsageAndDosage(goodsInstructionsInfo.getUsageDosage());
                request.setComposition(goodsInstructionsInfo.getDrugDetails());
                request.setProperty(goodsInstructionsInfo.getDrugProperties());
                request.setStorage(goodsInstructionsInfo.getStorageConditions());
                request.setValidity(goodsInstructionsInfo.getShelfLife());
                request.setEffect(goodsInstructionsInfo.getIndications());
                request.setAdverseReaction(goodsInstructionsInfo.getAdverseEvents());
                request.setAttention(goodsInstructionsInfo.getNoteEvents());
                request.setTaboo(goodsInstructionsInfo.getContraindication());
                request.setDrugInteraction(goodsInstructionsInfo.getInterreaction());
            }
            request.setLicenseNo(standardGoodsAllInfoDTO.getBaseInfo().getLicenseNo());

            log.info(JSONUtil.toJsonStr(request));
            diagnosisApi.syncHmcGoodsToIH(request);
        }
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
        };
    }
}
