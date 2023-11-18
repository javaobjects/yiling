package com.yiling.admin.data.center.goods.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.standard.vo.StandardGoodsInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shuan
 */
@Data
public class GoodsAuditPageVO<T> extends Page {
    @ApiModelProperty(value = "标准库商品信息")
    private StandardGoodsInfoVO standardGoodsInfoVO;
    @ApiModelProperty(value = "待审核商品信息")
    private GoodsAuditListVO    goodsAuditInfoVO;

}
