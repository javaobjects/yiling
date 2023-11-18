package com.yiling.sjms.esb.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ESB组织架构结构树 BO
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsbOrgInfoTreeVO {

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
     * 上级部门ID
     */
    @ApiModelProperty("上级部门ID")
    private Long orgPid;

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    @ApiModelProperty("打标类型：1-事业部打标 2-业务省区打标 3-区办打标")
    private Integer tagType;

    /**
     * 是否失效：0-正常，其他失效
     */
    @ApiModelProperty("是否失效：0-正常，其他失效（失效的当前节点以及下级节点都不可选择）")
    private Integer state;

    /**
     * 当前节点禁用标志
     */
    @ApiModelProperty("当前节点禁用标志：true禁用，false启用")
    private Boolean disabled;

    /**
     * 下级部门
     */
    @ApiModelProperty("下级部门")
    private List<EsbOrgInfoTreeVO> children;

}