package com.yiling.admin.data.center.standard.vo;


import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标准库商品分类
 * </p>
 *
 * @author wei.wang
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsCategoryVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

}
