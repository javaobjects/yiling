package com.yiling.admin.b2b.member.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员详情VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("会员详情VO")
public class MemberDetailVO extends BaseVO {

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
     * 背景图
     */
    @NotEmpty
    @ApiModelProperty(value = "背景图key", required = true)
    private String bgPicture;

    /**
     * 会员点亮图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员点亮图key", required = true)
    private String lightPicture;

    /**
     * 会员熄灭图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员熄灭图key", required = true)
    private String extinguishPicture;

    /**
     * 背景图
     */
    @NotEmpty
    @ApiModelProperty(value = "背景图url", required = true)
    private String bgPictureUrl;

    /**
     * 会员点亮图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员点亮图url", required = true)
    private String lightPictureUrl;

    /**
     * 会员熄灭图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员熄灭图url", required = true)
    private String extinguishPictureUrl;

    /**
     * 获取条件：1-付费 2-活动赠送
     */
    @ApiModelProperty("获取条件：1-付费 2-活动赠送")
    private Integer getType;

    /**
     * 是否续卡提醒：0-否 1-是
     */
    @ApiModelProperty("是否续卡提醒：0-否 1-是")
    private Integer renewalWarn;

    /**
     * 到期前提醒天数
     */
    @ApiModelProperty("到期前提醒天数")
    private Integer warnDays;

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
     * 获得条件集合
     */
    @ApiModelProperty("获得条件集合")
    private List<MemberBuyStageVO> memberBuyStageList;

    /**
     * 会员权益集合
     */
    @ApiModelProperty("会员权益集合")
    private List<MemberEquityVO> memberEquityList;

}
