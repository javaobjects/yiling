package com.yiling.f2b.admin.procrelation.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveProcRelationForm extends BaseForm {

    /**
     * 工业主体eid
     */
    @NotNull
    @ApiModelProperty(value = "工业主体eid")
    private Long factoryEid;

    /**
     * 工业主体名称
     */
    @NotBlank
    @ApiModelProperty(value = "工业主体名称")
    private String factoryName;

    /**
     * 配送商eid
     */
    @ApiModelProperty(value = "配送商eid---配送类型为三方配送时必填")
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    @ApiModelProperty(value = "配送商名称---配送类型为三方配送时必填")
    private String deliveryName;

    /**
     * 渠道商eid
     */
    @NotNull
    @ApiModelProperty(value = "渠道商eid")
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    @NotBlank
    @ApiModelProperty(value = "渠道商名称")
    private String channelPartnerName;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    @NotNull
    @ApiModelProperty(value = "配送类型：1-工业直配 2-三方配送")
    private Integer deliveryType;

}
