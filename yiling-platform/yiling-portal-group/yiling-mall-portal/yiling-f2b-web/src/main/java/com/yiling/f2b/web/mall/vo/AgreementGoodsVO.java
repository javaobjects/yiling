package com.yiling.f2b.web.mall.vo;

import java.util.List;

import com.yiling.common.web.goods.vo.GoodsItemVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author: shuang.zhang
 * @date: 2021/6/29
 */
@Data
public class AgreementGoodsVO {

    @ApiModelProperty(value = "补充协议名称列表")
    private List<String> agrementNameList;

    @ApiModelProperty(value = "商品信息")
    private GoodsItemVO goodsItem;

}
