package com.yiling.cms.document.dto;

import java.util.Date;

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
public class DocumentCategoryDTO extends BaseDTO {


    private static final long serialVersionUID = -6782488986026902233L;
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


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 修改时间
     */
    private Date updateTime;
}
