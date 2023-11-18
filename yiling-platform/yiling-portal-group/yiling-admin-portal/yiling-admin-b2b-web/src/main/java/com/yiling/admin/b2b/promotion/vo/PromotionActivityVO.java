package com.yiling.admin.b2b.promotion.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动主表
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionActivityVO extends BaseVO {

    @ApiModelProperty(value = "促销活动名称")
    private String name;

    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "费用承担方（1-平台；2-商家）")
    private Integer bear;

    @ApiModelProperty(value = "分摊-平台百分比")
    private BigDecimal platformPercent;

    @ApiModelProperty(value = "分摊-商户百分比")
    private BigDecimal merchantPercent;

    @ApiModelProperty(value = "活动类型（1-满赠,2-秒杀，3-特价）")
    private Integer type;

    @ApiModelProperty(value = "生效类型 1-立即生效，2-固定生效时间")
    private Integer effectType;

    @ApiModelProperty(value = "立即生效-持续时间（h）")
    private Integer lastTime;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "促销范围-选择平台（1-B2B；2-销售助手）")
    private List<Integer> platformSelected;

    @ApiModelProperty(value = "活动状态（1-启用；2-停用；）")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "促销编码")
    private String promotionCode;

    @ApiModelProperty(value = "活动使用数量")
    private Integer activityUsedCount;

    @ApiModelProperty(value = "商家类型 1-以岭，2-非以岭")
    private Integer merchantType;

}
