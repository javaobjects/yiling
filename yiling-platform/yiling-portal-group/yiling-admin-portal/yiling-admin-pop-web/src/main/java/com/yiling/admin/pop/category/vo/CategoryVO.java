package com.yiling.admin.pop.category.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类分页列表 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Long goodsNum;

    /**
     * 分类商品明细集合
     */
    @ApiModelProperty("分类商品明细集合")
    private List<CategoryGoodsDetailVO> categoryGoodsList;

}
