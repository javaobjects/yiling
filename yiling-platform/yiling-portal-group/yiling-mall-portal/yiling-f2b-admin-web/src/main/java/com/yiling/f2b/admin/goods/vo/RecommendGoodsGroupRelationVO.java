package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupRelationVO
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGoodsGroupRelationVO extends BaseVO {
    /**
     * 商品组id
     */
    @ApiModelProperty(value = "商品组id")
    private Long groupId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品规格")
    private String sellSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家", example = "以岭")
    private String manufacturer;
}
