package com.yiling.b2b.app.member.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B移动端-权益VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
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
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
