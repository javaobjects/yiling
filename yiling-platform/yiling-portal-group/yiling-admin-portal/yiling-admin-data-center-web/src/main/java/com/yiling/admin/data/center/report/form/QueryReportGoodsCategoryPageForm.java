package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@Data
@Accessors(chain = true)
public class QueryReportGoodsCategoryPageForm extends QueryPageListForm {

    /**
     * 参数id
     */
    @NotNull
    @ApiModelProperty(value = "参数id")
    private Long paramId;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利
     */
    @NotNull
    @ApiModelProperty(value = "参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利")
    private Integer parType;

    /**
     * 商业eid
     */
    @ApiModelProperty("商业eid")
    private Long eid;

    /**
     * 门槛金额
     */
    @ApiModelProperty("门槛金额")
    private BigDecimal thresholdAmount;

    /**
     * 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
     */
    @ApiModelProperty(value = "数据来源 0-全部 2-B2B-企业推广 3-销售助手")
    private Integer memberSource;

    /**
     * 会员参数的会员id
     */
    @ApiModelProperty(value = "会员参数的会员id")
    private Long memberId;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String userName;

    /**
     * 开始修改时间
     */
    @ApiModelProperty("参数id")
    private Date startUpdateTime;

    /**
     * 结束修改时间
     */
    @ApiModelProperty("参数id")
    private Date endUpdateTime;
}