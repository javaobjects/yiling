package com.yiling.admin.data.center.report.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改订单标识form
 * @author: dexi.yao
 * @date: 2022-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateProporIdentificaForm extends BaseForm {

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    @NotNull
    @Range(min = 1,max = 2)
    @ApiModelProperty(value = "类型：1-B2B返利 2-流向返利")
    private Integer type;

    /**
     * idList
     */
    @NotNull
    @ApiModelProperty("idList")
    private List<Long> idList;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @NotNull
    @Range(min = 1,max = 3)
    @ApiModelProperty("标识状态：1-正常订单,2-无效订单,3-异常订单")
    private Integer updateIdenStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    @ApiModelProperty("异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他")
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    @ApiModelProperty("异常描述")
    private String abnormalDescribed;

}
