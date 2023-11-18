package com.yiling.b2b.app.member.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B移动端-会员详情VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
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
     * 背景图片
     */
    @ApiModelProperty("背景图片")
    private String bgPicture;

    /**
     * 当前用户是否为会员：0-否 1-是
     */
    @ApiModelProperty("当前用户是否为会员：0-否 1-是")
    private Integer currentMember;

    /**
     * 会员开通时间
     */
    @ApiModelProperty("会员开通时间")
    private Date startTime;

    /**
     * 会员到期时间
     */
    @ApiModelProperty("会员到期时间")
    private Date endTime;

    /**
     * 是否停止获取：0-否 1-是
     */
    @ApiModelProperty("是否停止获取：0-否 1-是")
    private Integer stopGet;

    /**
     * 节省金额
     */
    @ApiModelProperty("节省金额")
    private BigDecimal frugalAmount;

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
