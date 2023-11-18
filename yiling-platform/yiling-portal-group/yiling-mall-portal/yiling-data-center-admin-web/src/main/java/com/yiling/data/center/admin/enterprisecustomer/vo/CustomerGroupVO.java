package com.yiling.data.center.admin.enterprisecustomer.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业客户分组 VO
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerGroupVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 分组名称
     */
    @ApiModelProperty("分组名称")
    private String name;

    /**
     * 分组描述
     */
    @ApiModelProperty("分组描述")
    private String description;

    /**
     * 分组类型：1-平台创建 2-ERP同步
     */
    @ApiModelProperty("分组类型：1-平台创建 2-ERP同步")
    private Integer type;

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

}
