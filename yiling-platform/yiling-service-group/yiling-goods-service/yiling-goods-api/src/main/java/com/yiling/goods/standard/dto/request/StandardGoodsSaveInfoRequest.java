package com.yiling.goods.standard.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
public class StandardGoodsSaveInfoRequest extends BaseRequest {

    /**
     * 基础信息和其他信息
     */
    private StandardGoodsBasicInfoRequest baseInfo;

    /**
     * 中药饮片说明书信息
     */
    private StandardInstructionsDecoctionRequest decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    private StandardInstructionsDisinfectionRequest disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    private StandardInstructionsFoodsRequest foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    private StandardInstructionsHealthRequest healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    private StandardInstructionsMaterialsRequest materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    private StandardInstructionsGoodsBaseRequest goodsInstructionsInfo;

    /**
     * 医疗器械说明书
     */
    private StandardInstructionsMedicalInstrumentRequest medicalInstrumentInfo;

    /**
     * 配方颗粒说明书
     */
    private StandardInstructionsDispensingGranuleRequest dispensingGranuleInfo;

    /**
     * 规格图片信息
     */
    private List<StandardSpecificationInfoRequest> specificationInfo;

    /**
     * 图片信息
     */
    private List<StandardGoodsPicRequest> picBasicsInfoList;

}
