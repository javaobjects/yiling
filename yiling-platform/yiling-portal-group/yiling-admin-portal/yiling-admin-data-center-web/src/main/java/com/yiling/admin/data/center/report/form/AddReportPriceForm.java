package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数价格
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddReportPriceForm extends BaseForm {

    @NotNull
    private Long goodsId;

    /**
     * 价格
     */
    @NotNull
    private BigDecimal price;

    /**
     * report_param表id
     */
    @NotNull
    private Long paramId;

    /**
     * 开始时间
     */
    @NotNull
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    private Date endTime;

    /**
     * 商品名称
     */
    @NotNull
    private String goodsName;

    /**
     * 商品规格
     */
    @NotNull
    private String goodsSpecification;

    /**
     * 标准库规格ID
     */
    @NotNull
    private Long specificationId;

}
