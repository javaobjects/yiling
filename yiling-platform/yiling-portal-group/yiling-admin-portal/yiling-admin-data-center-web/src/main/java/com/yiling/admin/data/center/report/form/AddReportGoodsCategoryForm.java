package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddReportGoodsCategoryForm extends BaseForm {

    /**
     * 子参数id
     */
    @NotNull
    @ApiModelProperty("子参数id")
    private Long paramSubId;

    /**
     * 对应以岭的商品id
     */
    @NotNull
    @ApiModelProperty("对应以岭的商品id")
    private Long ylGoodsId;

    /**
     * 商品名称
     */
    @NotBlank
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @NotBlank
    @ApiModelProperty("商品规格")
    private String goodsSpecification;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty("结束时间")
    private Date endTime;

}
