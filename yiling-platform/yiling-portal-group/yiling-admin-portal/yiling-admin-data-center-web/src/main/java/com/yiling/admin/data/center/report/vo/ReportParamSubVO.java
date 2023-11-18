package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表子参数
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportParamSubVO extends BaseDTO {


    private static final long serialVersionUID = -8448117014374188936L;

    /**
     * 参数id
     */
    @ApiModelProperty(value = "主参数id")
    private Long paramId;

    /**
     * 商家eid
     */
    @ApiModelProperty(value = "商家eid")
    private Long eid;

    /**
     * 阶梯序号（参数类型仅阶梯时意义）
     */
    @ApiModelProperty(value = "阶梯序号（参数类型仅阶梯时意义）用于排序")
    private Integer ladderRange;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称--用于商品类型、活动设置、阶梯规则的名称展示")
    private String name;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "仅会员类型有意义--商业名称")
    private String eName;

    /**
     * 门槛金额
     */
    @ApiModelProperty(value = "仅会员类型有意义--会员售价")
    private BigDecimal thresholdAmount;

    /**
     * 奖励金额
     */
    @ApiModelProperty(value = "仅会员类型有意义--奖励金额")
    private BigDecimal rewardAmount;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    @ApiModelProperty(value = "奖励类型：1-金额 2-百分比")
    private Integer rewardType;

    /**
     * 奖励百分比
     */
    @ApiModelProperty(value = "奖励百分比")
    private BigDecimal rewardPercentage;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @ApiModelProperty(value = "数据来源 2-B2B-企业推广 3-销售助手")
    private Integer memberSource;

    /**
     * 会员参数的会员id
     */
    @ApiModelProperty(value = "会员参数的会员id")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String memberName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "仅会员类型有意义--开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "仅会员类型有意义--结束时间")
    private Date endTime;

    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人",hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建时间",hidden = true)
    private Date createTime;

    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(value = "修改人",hidden = true)
    private Long updateUser;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "操作人")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date updateTime;


}
