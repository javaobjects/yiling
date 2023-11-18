package com.yiling.b2b.app.member.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B移动端-会员购买条件 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@ApiModel("会员购买条件")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBuyStageVO extends BaseVO {

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
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 每天的价格
     */
    @ApiModelProperty("每天的价格")
    private BigDecimal perDayPrice;

}
