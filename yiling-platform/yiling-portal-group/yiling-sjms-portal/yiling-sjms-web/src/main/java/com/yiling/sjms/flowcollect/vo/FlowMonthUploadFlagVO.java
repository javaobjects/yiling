package com.yiling.sjms.flowcollect.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向上传按钮状态控制 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-08
 */
@Data
@Accessors(chain = true)
public class FlowMonthUploadFlagVO {

    /**
     * 上传按钮是否可用
     */
    @ApiModelProperty("上传按钮是否可用")
    private Boolean flag;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private Integer month;

    /**
     * 流向上传、商品对照开始时间
     */
    @ApiModelProperty("流向上传、商品对照开始时间")
    private Date goodsMappingStartTime;

    /**
     * 流向上传、商品对照结束时间
     */
    @ApiModelProperty("流向上传、商品对照结束时间")
    private Date goodsMappingEndTime;

}
