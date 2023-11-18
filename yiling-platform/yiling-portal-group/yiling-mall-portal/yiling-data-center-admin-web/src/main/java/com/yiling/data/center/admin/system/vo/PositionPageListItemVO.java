package com.yiling.data.center.admin.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 职位分页列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PositionPageListItemVO extends BaseVO {

    @ApiModelProperty("职位名称")
    private String name;

    @ApiModelProperty("职位描述")
    private String description;

    @ApiModelProperty("关联人数")
    private Long peopleNumber;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("创建人ID")
    private Long createUser;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
