package com.yiling.common.web.goods.vo;

import com.yiling.common.web.cart.vo.AddToCartButtonVO;
import com.yiling.framework.common.pojo.vo.SimpleGoodsVO;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品基本项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/15
 */
@Data
public class GoodsItemVO {

    /**
     * 售卖数量
     */
    @ApiModelProperty("售卖数量")
    private Integer sellerCount;

    /**
     * 售卖规格ID
     */
    @ApiModelProperty("售卖规格ID")
    private Long sellSpecificationsId;

    /**
     * 以岭商品ID
     */
    @ApiModelProperty("以岭商品ID")
    private Long ylGoodsId;

    /**
     * 商业公司名称
     */
    @ApiModelProperty("商业公司名称")
    private String ename;

    @ApiModelProperty("商业公司id")
    private Long eid;

    /**
     * 商品信息
     */
    @ApiModelProperty("商品信息")
    private SimpleGoodsVO goodsInfo;

    /**
     * 商品价格信息
     */
    @ApiModelProperty("商品价格信息")
    private GoodsPriceVO priceInfo;

    /**
     * 商品预售信息
     */
    @ApiModelProperty("商品预售信息")
    private PresaleInfoVO presaleInfoVO;

    /**
     * 加车按钮信息
     */
    @ApiModelProperty("加车按钮信息")
    private AddToCartButtonVO addToCartButtonInfo;

    /**
     * 商品采购权限状态：1加入进货单 2无采购关系 3不在采购协议内 4未登录
     */
    @ApiModelProperty("商品采购权限状态：1加入进货单 2无采购关系 3不在采购协议内 4未登录 5控销品种 6控销区域 7非上架商品")
    private Integer goodsLimitStatus;

    public AddToCartButtonVO getAddToCartButtonInfo() {
        GoodsLimitStatusEnum limitStatusEnum = GoodsLimitStatusEnum.getByCode(this.goodsLimitStatus);
        if(null==limitStatusEnum){
            limitStatusEnum = GoodsLimitStatusEnum.INVALID_GOODS;
        }
        if(null!=this.addToCartButtonInfo && this.addToCartButtonInfo.getGoodsLimitStatus().equals(this.goodsLimitStatus)){
            return this.addToCartButtonInfo;
        }
        boolean enabled = true;
        switch (limitStatusEnum){
            case NOT_LOGIN:
            case NOT_BUY:
            case OUT_OF_RELATION:
            case CONTROL_GOODS:
            case SHOP_CONTROL:
            case UN_SHELF:
            case NOT_RELATION_SHIP:
            case AUDIT_RELATION_SHIP:
            case INVALID_GOODS:
                enabled = false;
                break;
            case NORMAL:
                enabled = true;
                break;
        }
        this.addToCartButtonInfo = new AddToCartButtonVO(limitStatusEnum,enabled);
        return this.addToCartButtonInfo;
    }


}
