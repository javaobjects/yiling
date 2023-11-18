package com.yiling.sjms.esb.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EsbOrgSimpleInfoVO extends BaseVO {

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long orgId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String orgName;

    /**
     * 组织类型：0-公司 1-部门
     */
    @ApiModelProperty("组织类型：0-公司 1-部门")
    private String orgType;

    /**
     * 全路径
     */
    @ApiModelProperty("全路径")
    private String fullpath;

}
