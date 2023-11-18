package com.yiling.b2b.admin.promotion.vo;

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

    @ApiModelProperty(value = "企业ID")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String ename;

    @ApiModelProperty(value = "类型（1-平台；2-商家）")
    private Integer sponsorType;

    @ApiModelProperty(value = "促销活动描述")
    private String description;

    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "费用承担方（1-平台；2-商家）")
    private Integer bear;

    @ApiModelProperty(value = "活动类型（1-满赠）")
    private Integer type;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "促销范围-选择平台（1-B2B；2-销售助手）")
    private List<Integer> platformSelected;

    @ApiModelProperty(value = "活动状态（1-启用；2-停用；）")
    private Integer status;

    @ApiModelProperty(value = "满赠金额")
    private BigDecimal promotionAmount;

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
}
