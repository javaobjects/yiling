package com.yiling.sjms.crm.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 DepartmentVO
 * @描述
 * @创建时间 2023/4/18
 * @修改人 shichen
 * @修改时间 2023/4/18
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DepartmentVO extends BaseVO {

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Long orgId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String orgName;

    /**
     * 组织类型：0-公司 1-部门
     */
    @ApiModelProperty(value = "组织类型：0-公司 1-部门")
    private String orgType;

    /**
     * 上级部门ID
     */
    @ApiModelProperty(value = "上级部门ID")
    private Long orgPid;

    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    private Long compId;
}
