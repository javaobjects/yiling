package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业标签 VO
 *
 * @author: lun.yu
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseTagVO extends BaseVO {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 类型：1-手动标签 2-自动标签
     */
    @ApiModelProperty("类型：1-手动标签 2-自动标签")
    private Integer type;

    /**
     * 企业数量
     */
    @ApiModelProperty("企业数量")
    private Integer enterpriseNum;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
}
