package com.yiling.sales.assistant.app.search.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.sales.assistant.app.order.vo.GoodsPriceVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  搜索商品信息
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.search.dto
 * @date: 2021/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleGoodsInfoVO extends BaseVO {

    /**
     * 商品优化折扣，单位为百分比
     */
    @ApiModelProperty("商品优化折扣")
    private BigDecimal rebate;
    /**
     * 协议ID
     */
    @ApiModelProperty("协议ID-已失效")
    @Deprecated
    private List<Long> agreementIds;

    /**
     * 是否为协议商品
     */
    @ApiModelProperty("是否为协议商品-已失效")
    @Deprecated
    private Boolean isAgreementGood;

    /**
     * 商品信息
     */
    @ApiModelProperty("商品信息")
    private SimpleGoodsVO goodsInfo;

    /**
     * 商品价格信息
     */
    @ApiModelProperty("商品信息")
    private GoodsPriceVO priceInfo;

    /**
     * 配送商，商品ID
     */
    @ApiModelProperty("配送商商品ID")
    private Long distributeGoodId;

    /**
     * 以岭商品ID
     */
    @ApiModelProperty("以岭商品ID")
    private Long goodsId;

    /**
     * 配送商ID
     */
    @ApiModelProperty("配送商ID")
    private Long distributionEid;


    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long purchaseEid;

    /**
     * 店铺起配金额
     */
    @ApiModelProperty("店铺起配金额:b2b以岭全品才有，pop无效")
    private BigDecimal startAmount;


    /**
     * 销售包装数量
     */
    @ApiModelProperty(value = "销售包装数量")
    private Integer countSku;

    /**
     * 售卖规格ID
     */
    @ApiModelProperty("售卖规格ID")
    private Long sellSpecificationsId;


    /**
     * 销售包装商品信息
     */
    @ApiModelProperty(value = "销售包装商品信息")
    private List<GoodsSkuVO> goodsSkuList;

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
     * 商品图片列表
     */
    @ApiModelProperty(value = "商品图片列表")
    private List<StandardGoodsPicVO> picBasicsInfoList;

    /**
     * 标准库商品信息
     */
    @ApiModelProperty(value = "标准库商品信息")
    private AppStandardGoodsVO appStandardGoodsVO;

    /**
     * 商品采购权限状态：1加入进货单 2无采购关系 3不在采购协议内 4未登录
     */
    @ApiModelProperty("商品采购权限状态：1加入进货单 2无采购关系 3不在采购协议内 4未登录 5控销品种 6控销区域 7非上架商品")
    private Integer goodsLimitStatus;




}
