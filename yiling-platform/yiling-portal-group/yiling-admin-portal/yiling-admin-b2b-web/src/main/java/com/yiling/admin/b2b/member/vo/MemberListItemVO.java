package com.yiling.admin.b2b.member.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("会员列表VO")
public class MemberListItemVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String name;

    /**
     * 会员描述
     */
    @ApiModelProperty("会员描述")
    private String description;

    /**
     * 开通终端数
     */
    @ApiModelProperty("开通终端数")
    private Integer openNum;

    /**
     * 是否停止获取：0-否 1-是
     */
    @ApiModelProperty("是否停止获取：0-否 1-是")
    private Integer stopGet;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人",hidden = true)
    private Long updateUser;

    /**
     * 更新人名称
     */
    @ApiModelProperty("更新人名称")
    private String updateName;

}
