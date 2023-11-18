package com.yiling.b2b.admin.shop.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.b2b.admin.goods.vo.GoodsSkuVO;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺楼层商品 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopFloorGoodsItemVO extends BaseVO {

    /**
     * 楼层ID
     */
    @ApiModelProperty("楼层ID")
    private Long floorId;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 厂家
     */
    @ApiModelProperty("厂家")
    private String manufacturer;

    /**
     * 批准文号
     */
    @ApiModelProperty("批准文号")
    private String licenseNo;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String sellSpecifications;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 图片商品值
     */
    @ApiModelProperty("图片商品值")
    private String pic;

    /**
     * 排序值
     */
    @ApiModelProperty("排序值")
    private Integer sort;

    /**
     * 销售包装
     */
    @ApiModelProperty(value = "销售包装")
    private List<GoodsSkuVO> goodsSkuList;

}
