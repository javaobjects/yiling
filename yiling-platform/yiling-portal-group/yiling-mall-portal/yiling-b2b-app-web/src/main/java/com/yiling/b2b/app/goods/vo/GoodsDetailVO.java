package com.yiling.b2b.app.goods.vo;

import java.util.Date;
import java.util.List;

import com.yiling.b2b.app.shop.vo.ShopDetailVO;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.PresaleInfoVO;

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

    private static final long serialVersionUID = -3819773967257974480L;

    /**
     * 销售包装数量
     */
    @ApiModelProperty(value = "销售包装数量")
    private Integer countSku;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String batchNumber;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date manufacturingDate;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    /**
     * 销售包装商品信息
     */
    @ApiModelProperty(value = "销售包装商品信息")
    private List<GoodsSkuVO> goodsSkuList;

    /**
     * 商家信息
     */
    @ApiModelProperty(value = "商家信息")
    private ShopDetailVO shopDetailVO;

    /**
     * 标准库商品信息
     */
    @ApiModelProperty(value = "标准库商品信息")
    private AppStandardGoodsVO appStandardGoodsVO;

    /**
     * 满减活动对象
     */
    @ApiModelProperty(value = "满减活动对象")
    private List<SimpleActivityVO> simpleActivityList;

    /**
     * 满赠活动
     */
    @ApiModelProperty(value = "满赠活动")
    private List<SimpleActivityVO> giftActivity;

    /**
     * 策略满赠活动对象
     */
    private List<StrategyActivityVO> strategyActivityList;

    /**
     * 商品图片列表
     */
    @ApiModelProperty(value = "商品图片列表")
    private List<StandardGoodsPicVO> picBasicsInfoList;

    /**
     * 商业公司销售前4的商品列表
     */
    @ApiModelProperty(value = "商业公司销售前4的商品列表")
    private List<GoodsItemVO> top4GoodsList;

    /**
     * 商品预售信息
     */
    @ApiModelProperty("商品预售信息")
    private PresaleInfoVO presaleInfoVO;

    /**
     * 商品限购信息
     */
    @ApiModelProperty("商品限购信息")
    private PurchaseRestrictionVO purchaseRestrictionVO;

    /**
     * 特殊说明
     */
    @ApiModelProperty(value = "特殊说明")
    private  String specialExplain;

    /**
     * 售后流程
     */
    @ApiModelProperty(value = "售后流程")
    private  String postSaleFlow;
}
