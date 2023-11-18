package com.yiling.sjms.wash.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import cn.hutool.core.date.DateUtil;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateFlowMonthWashControlForm extends BaseForm {

    private Long id;

    /**
     * 年份
     */
    @NotNull
    @ApiModelProperty(value = "所属年")
    private Integer year;

    /**
     * 月份
     */
    @NotNull
    @ApiModelProperty(value = "所属月")
    private Integer month;

    /**
     * 数据开始时间
     */
    @NotNull
    @ApiModelProperty(value = "数据开始时间")
    private Date dataStartTime;

    /**
     * 数据结束时间
     */
    @NotNull
    @ApiModelProperty(value = "数据结束时间")
    private Date dataEndTime;

    /**
     * 流向上传、商品对照开始时间
     */
    @ApiModelProperty(value = "流向上传、商品对照开始时间")
    private Date goodsMappingStartTime;

    /**
     * 流向上传、商品对照结束时间
     */
    @ApiModelProperty(value = "流向上传、商品对照结束时间")
    private Date goodsMappingEndTime;

    /**
     * 客户对照、销量申诉开始时间
     */
    @ApiModelProperty(value = "客户对照、销量申诉开始时间")
    private Date customerMappingStartTime;

    /**
     * 客户对照、销量申诉结束时间
     */
    @ApiModelProperty(value = "客户对照、销量申诉结束时间")
    private Date customerMappingEndTime;

    /**
     * 在途库存、终端库存上报开始时间
     */
    @ApiModelProperty(value = "在途库存、终端库存上报开始时间")
    private Date goodsBatchStartTime;

    /**
     * 在途库存、终端库存上报结束时间
     */
    @ApiModelProperty(value = "在途库存、终端库存上报结束时间")
    private Date goodsBatchEndTime;

    /**
     * 窜货提报开始时间
     */
    @ApiModelProperty(value = "窜货提报开始时间")
    private Date flowCrossStartTime;

    /**
     * 窜货提报结束时间
     */
    @ApiModelProperty(value = "窜货提报结束时间")
    private Date flowCrossEndTime;

    /**
     * 团购开始时间
     */
    @ApiModelProperty(value = "团购开始时间")
    private Date flowGroupStartTime;

    /**
     * 团购结束时间
     */
    @ApiModelProperty(value = "团购结束时间")
    private Date flowGroupEndTime;

}
