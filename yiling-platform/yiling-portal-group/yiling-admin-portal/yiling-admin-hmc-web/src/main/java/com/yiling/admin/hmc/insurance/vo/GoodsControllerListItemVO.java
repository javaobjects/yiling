package com.yiling.admin.hmc.insurance.vo;

import com.yiling.admin.hmc.goods.vo.GoodsControlVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/1
 */
@Data
public class GoodsControllerListItemVO extends GoodsControlVO {

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;
}
