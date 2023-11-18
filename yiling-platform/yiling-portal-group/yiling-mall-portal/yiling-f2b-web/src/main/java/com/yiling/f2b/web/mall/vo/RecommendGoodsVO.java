package com.yiling.f2b.web.mall.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@Data
public class RecommendGoodsVO {

    @ApiModelProperty(value = "智能推荐")
     private List<GoodsItemVO> goodsList;


}
