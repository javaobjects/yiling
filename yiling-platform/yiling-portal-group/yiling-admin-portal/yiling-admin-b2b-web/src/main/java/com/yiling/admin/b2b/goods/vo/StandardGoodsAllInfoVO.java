package com.yiling.admin.b2b.goods.vo;

import java.util.List;

import com.yiling.goods.standard.dto.StandardGoodsPicDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
public class StandardGoodsAllInfoVO {

    /**
     * 基础信息和其他信息
     */
    @ApiModelProperty(value = "基础信息和其他信息")
    private StandardGoodsBasicInfoVO baseInfo;

    /**
     * 中药饮片说明书信息
     */
    @ApiModelProperty(value = "中药饮片说明书信息")
    private StandardInstructionsDecoctionVO decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    @ApiModelProperty(value = "消杀品说明书信息")
    private StandardInstructionsDisinfectionVO disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    @ApiModelProperty(value = "食品说明书信息")
    private StandardInstructionsFoodsVO foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    @ApiModelProperty(value = "保健食品说明书信息")
    private StandardInstructionsHealthVO healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    @ApiModelProperty(value = "中药材说明书信息")
    private StandardInstructionsMaterialsVO materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    @ApiModelProperty(value = "药品说明书信息")
    private StandardInstructionsGoodsVO goodsInstructionsInfo;

    /**
     * 医疗器械说明书
     */
    @ApiModelProperty(value = "医疗器械说明书")
    private StandardInstructionsMedicalInstrumentVO medicalInstrumentInfo;

    /**
     * 配方颗粒说明书
     */
    @ApiModelProperty(value = "配方颗粒说明书")
    private StandardInstructionsDispensingGranuleVO dispensingGranuleInfo;


    /**
     * 规格图片
     */
    @ApiModelProperty(value = "规格图片")
    private List<StandardSpecificationInfoVO> specificationInfo;

    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private List<StandardGoodsPicDTO> picBasicsInfoList;

}
