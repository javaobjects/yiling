package com.yiling.f2b.admin.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsDetailsVO extends BaseVO {

    /**
     * 所属企业
     */
    @ApiModelProperty(value = "所属企业")
    private Long eid;

    /**
     * 所属企业名称
     */
    @ApiModelProperty(value = "所属企业名称")
    private String ename;

	/**
	 * 注册证号
	 */
	@ApiModelProperty(value = "注册证号", example = "Z109090")
	private String licenseNo;

	/**
	 * 生产厂家
	 */
	@ApiModelProperty(value = "生产厂家", example = "以岭")
	private String manufacturer;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产地址", example = "石家庄")
    private String manufacturerAddress;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称", example = "莲花")
	private String name;

	/**
	 * 销售规格
	 */
	@ApiModelProperty(value = "销售规格", example = "1片")
	private String sellSpecifications;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "自己公司销售规格", example = "1片")
    private String specifications;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回（只有1是上架状态，其余全为下架）", example = "1")
	private Integer goodsStatus;

	/**
	 * 挂网价
	 */
	@ApiModelProperty(value = "挂网价", example = "1.11")
	private BigDecimal price;

	/**
	 * 规格单位
	 */
	@ApiModelProperty(value = "规格单位", example = "盒")
	private String unit;



    @ApiModelProperty(value = "专利标识: 1-非专利 2-专利", example = "1")
    private Integer isPatent;

    /**
     * 超卖标识：0-非超卖  1-超卖
     */
    @ApiModelProperty(value = "超卖标识：0-非超卖  1-超卖", example = "1")
    private Integer overSoldType;

    /**
     * 标准库信息
     */
    @ApiModelProperty(value = "标准库信息")
	private StandardGoodsAllInfoVO standardGoodsAllInfo;

    /**
     * 销售包装
     */
    @ApiModelProperty(value = "销售包装")
    private List<GoodsSkuVO> goodsSkuList;

    @ApiModelProperty(value = "商品限价信息")
    private GoodsLimitPriceVO goodsLimitPrice;
}
