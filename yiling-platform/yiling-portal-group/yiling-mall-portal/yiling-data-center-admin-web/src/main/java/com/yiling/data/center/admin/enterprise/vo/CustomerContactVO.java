package com.yiling.data.center.admin.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户商务联系人 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerContactVO extends BaseVO {

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String mobile;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;
}
