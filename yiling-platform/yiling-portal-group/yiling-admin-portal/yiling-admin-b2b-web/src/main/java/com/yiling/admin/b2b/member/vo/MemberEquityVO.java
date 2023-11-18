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
 * B2B-权益VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberEquityVO extends BaseVO {

    /**
     * 权益名称
     */
    @ApiModelProperty("权益名称")
    private String name;

    /**
     * 权益类型
     */
    @ApiModelProperty("权益类型")
    private Integer type;

    /**
     * 权益图标
     */
    @ApiModelProperty("权益图标")
    private String icon;

    /**
     * 权益说明
     */
    @ApiModelProperty("权益说明")
    private String description;

    /**
     * 权益状态：0-关闭 1-开启
     */
    @ApiModelProperty("权益状态：0-关闭 1-开启")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
