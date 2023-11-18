package com.yiling.admin.data.center.report.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityGoodsListItemVO extends BaseVO {

    /**
     * 商业商品名称
     */
    @ApiModelProperty("商业商品名称")
    private String goodsName;

    /**
     * 商业商品规格
     */
    @ApiModelProperty("商业商品规格")
    private String goodsSpecifications;

    /**
     * 以岭商品ID
     */
    @ApiModelProperty("以岭商品ID")
    private Long ylGoodsId;

    /**
     * 商业商品内码
     */
    @ApiModelProperty("商业商品内码")
    private String goodsInSn;

}
