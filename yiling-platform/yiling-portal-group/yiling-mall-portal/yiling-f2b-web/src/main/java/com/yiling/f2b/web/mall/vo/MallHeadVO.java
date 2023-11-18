package com.yiling.f2b.web.mall.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/16
 */
@Data
public class MallHeadVO {

    @ApiModelProperty(value = "购物车商品件数")
    private Integer cartGoodsNum;

    @ApiModelProperty(value = "以岭logo")
    private String yilingLogo;

    @ApiModelProperty(value = "搜索热词列表")
    private List<GoodsHotWordsVO> goodsHotWordsList;

    @ApiModelProperty(value = "自定义导航列表")
    private List<NavigationInfoVO> navigationInfoList;
}
