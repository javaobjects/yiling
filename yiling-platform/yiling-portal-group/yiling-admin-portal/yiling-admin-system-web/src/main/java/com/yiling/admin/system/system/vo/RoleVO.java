package com.yiling.admin.system.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色 VO
 *
 * @author: lun.yu
 * @date: 2021/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleVO extends BaseVO {

    /**
     * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
     */
    @ApiModelProperty(value = "应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手")
    private String appId;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Integer eid;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String code;

    /**
     * 角色类型：1-系统角色 2-自定义角色
     */
    @ApiModelProperty(value = "角色类型：1-系统角色 2-自定义角色")
    private String type;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
