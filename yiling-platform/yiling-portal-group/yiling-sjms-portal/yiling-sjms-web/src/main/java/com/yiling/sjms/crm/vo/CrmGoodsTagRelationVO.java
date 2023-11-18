package com.yiling.sjms.crm.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationVO
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsTagRelationVO extends BaseVO {
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Long tagId;

    /**
     * crm商品id
     */
    @ApiModelProperty(value = "crm商品id")
    private Long crmGoodsId;

    /**
     * crm商品名称
     */
    @ApiModelProperty(value = "crm商品名称")
    private String crmGoodsName;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private Long crmGoodsCode;

    /**
     * crm商品规格
     */
    @ApiModelProperty(value = "crm商品规格")
    private String crmGoodsSpec;

    /**
     * crm商品品类id
     */
    @ApiModelProperty(value = "crm商品品类id")
    private Long crmGoodsCategoryId;

    /**
     * crm商品品类id
     */
    @ApiModelProperty(value = "crm商品品类")
    private String crmGoodsCategory;
}
