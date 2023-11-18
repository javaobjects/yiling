package com.yiling.b2b.admin.goods.vo;


import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 药品分类管理详情
 * </p>
 *
 * @author wei.wang
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsCategoryInfoAllVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 父Id
     */
    @ApiModelProperty(value = "父ID")
    private Long parentId;

    /**
     * 子类
     */
    @ApiModelProperty(value = "子分类")
    private List<StandardGoodsCategoryInfoAllVO> children;

}
