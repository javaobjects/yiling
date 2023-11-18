package com.yiling.sjms.esb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ESB部门组织架构树节点VO
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsbOrgTreeNodeVO {

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型：1-部门 2-员工")
    private Integer type;
}
