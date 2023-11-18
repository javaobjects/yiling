package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportB2bDetailPageForm extends QueryPageListForm {

    /**
     * 商业eid
     */
    @ApiModelProperty(value = "商业eid")
    private Long eid;

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty(value = "报表id")
    private  Long reportId;

    /**
     * 开始下单时间
     */
    @ApiModelProperty(value = "开始下单时间")
    private Date startCreateOrderTime;

    /**
     * 结束下单时间
     */
    @ApiModelProperty(value = "结束下单时间")
    private Date endCreateOrderTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @ApiModelProperty(value = "标识状态：0-全部 1-正常订单,2-无效订单,3-异常订单")
    private Integer identificationStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    @ApiModelProperty(value = "返利状态：0-全部 1-待返利 2-已返利 3-部分返利")
    private Integer rebateStatus;

    /**
     * 省code
     */
    @ApiModelProperty(value = "省code")
    private String provinceCode;

    /**
     * 市code
     */
    @ApiModelProperty(value = "市code")
    private String cityCode;

    /**
     * 区code
     */
    @ApiModelProperty(value = "区code")
    private String regionCode;


}
