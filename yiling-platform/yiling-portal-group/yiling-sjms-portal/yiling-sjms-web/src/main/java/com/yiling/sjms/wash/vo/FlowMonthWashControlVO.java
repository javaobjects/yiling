package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2023/3/7
 */
@Data
public class FlowMonthWashControlVO extends BaseVO {

    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private Integer year;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private Integer month;
    /**
     * 数据开始时间
     */
    @ApiModelProperty(value = "数据开始时间")
    private Date dataStartTime;
    /**
     * 数据结束时间
     */
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

    @ApiModelProperty(value = "员工备份状态：0未开始1已完成")
    private Integer employeeBackupStatus;
    @ApiModelProperty(value = "员工备份时间")
    private Date    employeeBackupTime;
    @ApiModelProperty(value = "基础状态：0未开始1进行中2已完成")
    private Integer basisStatus;
    @ApiModelProperty(value = "基础完成时间")
    private Date    basisTime;
    @ApiModelProperty(value = "备份状态：0未开始1已完成")
    private Integer basisBackupStatus;
    @ApiModelProperty(value = "备份完成时间")
    private Date    basisBackupTime;
    @ApiModelProperty(value = "第一阶段清洗状态：0未开始1进行中2已完成")
    private Integer washStatus;
    @ApiModelProperty(value = "第一阶段清洗完成时间")
    private Date    washTime;
    @ApiModelProperty(value = "团购锁任务状态：0未开始1进行中2已完成")
    private Integer gbLockStatus;
    @ApiModelProperty(value = "团购锁任务完成时间")
    private Date    gbLockTime;
    @ApiModelProperty(value = "非锁任务状态：00未开始1进行中2已完成")
    private Integer unlockStatus;
    @ApiModelProperty(value = "非锁任务完成时间")
    private Date    unlockTime;
    @ApiModelProperty(value = "团购非锁任务状态：0未开始1进行中2已完成")
    private Integer gbUnlockStatus;
    @ApiModelProperty(value = "团购非锁任务完成时间")
    private Date    gbUnlockTime;
    @ApiModelProperty(value = "任务状态：0未处理1已完成")
    private Integer taskStatus;
    @ApiModelProperty(value = "任务完成时间")
    private Date    taskTime;

}
