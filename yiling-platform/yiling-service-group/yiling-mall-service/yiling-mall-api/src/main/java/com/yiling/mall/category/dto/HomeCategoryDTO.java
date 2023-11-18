package com.yiling.mall.category.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * pop首页-商品分类栏 dto
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HomeCategoryDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
