package com.yiling.b2b.app.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFeaturedGoodsPageFrom
 * @描述
 * @创建时间 2022/4/14
 * @修改人 shichen
 * @修改时间 2022/4/14
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFeaturedGoodsPageFrom extends QueryPageListForm {

    /**
     * 商品售卖规格id
     */
    @ApiModelProperty(value = "商品售卖规格id")
    private Long sellSpecificationsId;

    /**
     * 价格排序
     */
    @ApiModelProperty(value = "价格降序")
    private Boolean sortPriceDesc;

    @ApiModelProperty(value = "价格升序")
    private Boolean sortPriceAsc;

    /**
     * 销量排序
     */
    @ApiModelProperty(value = "销量排序")
    private Boolean sortSaleVolume;

    /**
     * 是否筛选建采商家
     */
    @ApiModelProperty(value = "筛选建采商家")
    private Boolean filterPurchaseEnterprise = false;

    /**
     * 是否筛选有库存商品
     */
    @ApiModelProperty(value = "筛选有库存")
    private Boolean filterInStockGoods = false;

    /**
     * 是否筛选销售省份
     */
    @ApiModelProperty(value = "筛选省份内")
    private Boolean filterProvince = false;

    /**
     * 是否筛选在有效期内
     */
    @ApiModelProperty(value = "筛选有效期内")
    private Boolean filterExpired = false;
}
