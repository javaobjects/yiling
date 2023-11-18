package com.yiling.sjms.flow.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 FlowMonthWashControlVO
 * @描述
 * @创建时间 2023/3/6
 * @修改人 shichen
 * @修改时间 2023/3/6
 **/
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class FlowMonthWashControlVO extends BaseVO {

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
    @ApiModelProperty("商品对照开始时间")
    private Date goodsMappingStartTime;

    /**
     * 流向上传、商品对照结束时间
     */
    @ApiModelProperty("商品对照结束时间")
    private Date goodsMappingEndTime;

    /**
     * 流向上传、商品对照状态控制：1未开始2进行中3手动关闭4过期关闭
     */
    @ApiModelProperty("商品对照状状态控制 1未开始2进行中3手动关闭4过期关闭")
    private Integer goodsMappingStatus;

    /**
     * 客户对照、销量申诉开始时间
     */
    @ApiModelProperty("客户对照开始时间")
    private Date customerMappingStartTime;

    /**
     * 客户对照、销量申诉结束时间
     */
    @ApiModelProperty("客户对照结束时间")
    private Date customerMappingEndTime;

    /**
     * 客户对照、销量申诉状态控制：1未开始2进行中3手动关闭4过期关闭
     */
    @ApiModelProperty("客户对照状态控制 1未开始2进行中3手动关闭4过期关闭")
    private Integer customerMappingStatus;

}
