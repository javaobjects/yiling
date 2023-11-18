package com.yiling.cms.content.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryDTO extends BaseDTO {


    private static final long serialVersionUID = -7863296268309963238L;
    /**
     * 父id
     */
    private Long parentId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 0-禁用 1启用
     */
    private Integer status;

    // List<Long> displayLines;


    /**
     * 排序
     */
    private Integer categorySort;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

}
