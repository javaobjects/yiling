package com.yiling.f2b.web.goods.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: shuang.zhang
 * @date: 2021/6/18
 */
@Getter
@Setter
@ToString
public class GoodsDetailVO extends GoodsItemVO {

    private static final long   serialVersionUID = -3819773967257974480L;

    /**
     * 配送商商品信息
     */
    @ApiModelProperty(value = "配送商商品信息列表")
    private List<DistributorGoodsVO> distributorGoodsList;

    /**
     * 协议列表
     */
    @ApiModelProperty(value = "协议列表")
    private List<SimpleAgreementVO> simpleAgreementList;

    /**
     * 商品图片列表
     */
    @ApiModelProperty(value = "商品图片列表")
    private List<StandardGoodsPicVO> picBasicsInfoList;

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
     * 药品说明书信息
     */
    @ApiModelProperty(value = "药品说明书信息")
    private StandardInstructionsGoodsVO goodsInstructionsInfo;

}
