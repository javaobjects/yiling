package com.yiling.mall.category.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分类 dto
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
