package com.yiling.goods.standard.dto;


import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标准库商品分类表
 * </p>
 *
 * @author wei.wang
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsCategoryInfoAllDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父Id
     */
    private Long parentId;

    /**
     * 子类
     */
    private List<StandardGoodsCategoryInfoAllDTO> children;

}
