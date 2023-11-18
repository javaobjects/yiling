package com.yiling.f2b.web.mall.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@Data
public class MallBannerVO {
    /**
     * Banner列表
     */
    @ApiModelProperty(value = "Banner列表")
    private List<BannerVO> bannerList;

    /**
     * 公告列表
     */
    @ApiModelProperty(value = "公告列表")
    private List<NoticeInfoVO> noticeInfoList;

    /**
     * 智能推荐
     */
    @ApiModelProperty(value = "智能推荐")
    private RecommendGoodsVO recommendGoods;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private List<CategoryVO> categoryList;
}
