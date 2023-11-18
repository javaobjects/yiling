package com.yiling.sjms.gb.form;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存商品信息
 */
@Data
public class SaveGBGoodsInfoForm extends BaseForm {
    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private Long code;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String name;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格")
    private String specification;

    /**
     * 小包装
     */
    @ApiModelProperty(value = "小包装")
    private Integer smallPackage;

    /**
     * 团购数量（盒）
     */
    @ApiModelProperty(value = "团购数量（盒）")
    private Integer quantityBox;

    /**
     * 实际团购单价
     */
    @ApiModelProperty(value = "实际团购单价")
    private BigDecimal finalPrice;

    /**
     * 产品核算价
     */
    @ApiModelProperty(value = "产品核算价")
    private BigDecimal price;

    /**
     * 团购成功回款日期
     */
    @ApiModelProperty(value = "团购成功回款日期")
    private Date paymentTime;

    /**
     *流向月份
     */
    @ApiModelProperty(value = "流向月份")
    private String flowMonthDay;

}
