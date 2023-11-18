package com.yiling.f2b.web.goods.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 详情页vo
 * @author dexi.yao
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsDetailPageVO extends BaseVO {

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String name;

	/**
	 * 注册证号
	 */
	@ApiModelProperty(value = "注册证号")
	private String licenseNo;

	/**
	 * 生产厂家
	 */
	@ApiModelProperty(value = "生产厂家")
	private String manufacturer;

	/**
	 * 销售规格
	 */
	@ApiModelProperty(value = "销售规格")
	private String sellSpecifications;

	/**
	 * 大包装
	 */
	@ApiModelProperty(value = "大包装")
	private Integer bigPackage;

	/**
	 * 商品图片列表
	 */
	@ApiModelProperty(value = "商品图片列表")
	private List<StandardGoodsPicVO> standardGoodsPicVOS;

	/**
	 * 中药饮片说明书信息
	 */
	@ApiModelProperty(value = "中药饮片说明书信息")
	private StandardInstructionsDecoctionVO decoctionInfo;

	/**
	 * 消杀品说明书信息
	 */
	@ApiModelProperty(value = "消杀品说明书信息")
	private StandardInstructionsDisinfectionVO disinfectionInfo;

	/**
	 * 食品说明书信息
	 */
	@ApiModelProperty(value = "食品说明书信息")
	private StandardInstructionsFoodsVO foodsInfo;

	/**
	 * 保健食品说明书信息
	 */
	@ApiModelProperty(value = "保健食品说明书信息")
	private StandardInstructionsHealthVO healthInfo;

	/**
	 * 中药材说明书信息
	 */
	@ApiModelProperty(value = "中药材说明书信息")
	private StandardInstructionsMaterialsVO materialsInfo;

	/**
	 * 药品说明书信息
	 */
	@ApiModelProperty(value = "药品说明书信息")
	private StandardInstructionsGoodsVO goodsInfo;

    /**
     * 医疗器械说明书
     */
    @ApiModelProperty(value = "医疗器械说明书")
    private StandardInstructionsMedicalInstrumentVO medicalInstrumentInfo;


}
