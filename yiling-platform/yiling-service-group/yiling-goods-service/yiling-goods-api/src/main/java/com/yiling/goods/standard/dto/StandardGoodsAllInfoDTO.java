package com.yiling.goods.standard.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsAllInfoDTO extends BaseDTO {

    /**
     * 基础信息和其他信息
     */
    private StandardGoodsBasicInfoDTO baseInfo;

    /**
     * 中药饮片说明书信息
     */
    private StandardInstructionsDecoctionDTO decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    private StandardInstructionsDisinfectionDTO disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    private StandardInstructionsFoodsDTO foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    private StandardInstructionsHealthDTO healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    private StandardInstructionsMaterialsDTO materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    private StandardInstructionsGoodsDTO goodsInstructionsInfo;

    /**
     * 医疗器械说明书
     */
    private StandardInstructionsMedicalInstrumentDTO medicalInstrumentInfo;

    /**
     * 配方颗粒说明书
     */
    private StandardInstructionsDispensingGranuleDTO dispensingGranuleInfo;

    /**
     * 规格图片
     */
    private List<StandardSpecificationInfoDTO> specificationInfo;

    /**
     * 图片信息
     */
    private List<StandardGoodsPicDTO> picBasicsInfoList;


}
