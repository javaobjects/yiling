package com.yiling.admin.data.center.report.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportPageListForm extends QueryPageListForm {

    /**
     * 商业eid
     */
    @ApiModelProperty(value = "商业eid")
    private Long eid;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    @ApiModelProperty(value = "报表类型：1-B2B返利 2-流向返利")
    private Integer type;

    /**
     * 报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    @ApiModelProperty(value = "报表状态：1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回")
    private List<Integer> status;

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

    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    /**
     * 订单返利状态：1-待返利 2-已返利 3-部分返利
     */
    @ApiModelProperty(value = "订单返利状态：1-待返利 2-已返利 3-部分返利")
    private Integer rebateStatus;
}
