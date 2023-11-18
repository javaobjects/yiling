package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加报表参数-商品类型
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReportParamSubForm extends BaseForm {

    /**
     * 子参数id
     */
    @ApiModelProperty(value = "子参数id--更新时必穿")
    private Long id;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
     */
    @NotNull
    @ApiModelProperty(value = "参数类型：1-商品类型 4-会员返利")
    private Integer parType;

    /**
     * 名称
     */
    @ApiModelProperty(value = "分类名称--商品分类时必填")
    private String name;


    /**
     * 门槛金额
     */
    @ApiModelProperty(value = "会员售价--添加会员时必填")
    private BigDecimal thresholdAmount;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    @Range(min = 1,max = 2)
    @ApiModelProperty(value = "奖励类型：1-金额 2-百分比--会员时必填")
    private Integer rewardType;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    @ApiModelProperty(value = "返利金额--百分比时传小数如86.3%传86.3")
    private BigDecimal rewardValue;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @ApiModelProperty(value = "数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手")
    private Integer memberSource;

    /**
     * 会员参数的会员id
     */
    @ApiModelProperty(value = "会员参数的会员id--添加会员时必填")
    private Long memberId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;



}
