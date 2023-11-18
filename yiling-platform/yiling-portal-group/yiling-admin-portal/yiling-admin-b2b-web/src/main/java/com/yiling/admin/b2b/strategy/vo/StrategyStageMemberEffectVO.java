package com.yiling.admin.b2b.strategy.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠购买会员方案表
 * </p>
 *
 * @author zhangy
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyStageMemberEffectVO extends BaseVO {

    /**
     * 营销活动id
     */
    @ApiModelProperty("营销活动id")
    private Long marketingStrategyId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    
    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 价格
     */
    @ApiModelProperty("价格")
    private BigDecimal price;

    /**
     * 有效时长
     */
    @ApiModelProperty("有效时长")
    private Integer validTime;

    /**
     * 名称（如：季卡VIP）
     */
    @ApiModelProperty("名称（如：季卡VIP）")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}
