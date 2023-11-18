package com.yiling.admin.data.center.standard.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
public class StandardGoodsSaveInfoForm {

    /**
     * 基础信息和其他信息
     */
    @ApiModelProperty(value = "基础信息和其他信息")
    private StandardGoodsBasicInfoForm baseInfo;

    /**
     * 中药饮片说明书信息
     */
    @ApiModelProperty(value = "中药饮片说明书信息")
    private StandardInstructionsDecoctionForm decoctionInstructionsInfo;

    /**
     * 消杀品说明书信息
     */
    @ApiModelProperty(value = "消杀品说明书信息")
    private StandardInstructionsDisinfectionForm disinfectionInstructionsInfo;

    /**
     * 食品说明书信息
     */
    @ApiModelProperty(value = "食品说明书信息")
    private StandardInstructionsFoodsForm foodsInstructionsInfo;

    /**
     * 保健食品说明书信息
     */
    @ApiModelProperty(value = "保健食品说明书信息")
    private StandardInstructionsHealthForm healthInstructionsInfo;

    /**
     * 中药材说明书信息
     */
    @ApiModelProperty(value = "中药材说明书信息")
    private StandardInstructionsMaterialsForm materialsInstructionsInfo;

    /**
     * 药品说明书信息
     */
    @ApiModelProperty(value = "药品说明书信息")
    private StandardInstructionsGoodsForm goodsInstructionsInfo;

    /**
     * 医疗器械说明书
     */
    @ApiModelProperty(value = "医疗器械说明书")
    private StandardInstructionsMedicalInstrumentForm medicalInstrumentInfo;

    /**
     * 配方颗粒说明书
     */
    @ApiModelProperty(value = "配方颗粒说明书")
    private StandardInstructionsDispensingGranuleForm dispensingGranuleInfo;

    /**
     * 规格图片信息
     */
    @ApiModelProperty(value = "规格图片信息")
    private List<StandardSpecificationInfoForm> specificationInfo;

    /**
     * 图片信息
     */
    @ApiModelProperty(value = "图片信息")
    private List<StandardGoodsPicForm> picBasicsInfoList;

}
