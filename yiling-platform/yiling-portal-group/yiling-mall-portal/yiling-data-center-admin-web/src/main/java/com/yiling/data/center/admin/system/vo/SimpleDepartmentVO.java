package com.yiling.data.center.admin.system.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 简单部门信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SimpleDepartmentVO extends BaseVO {

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;
}
