package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.wash.bo.ErpClientSimpleBO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveOrUpdateFlowMonthWashControlRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 数据开始时间
     */
    private Date dataStartTime;

    /**
     * 数据结束时间
     */
    private Date dataEndTime;

    /**
     * 流向上传、商品对照开始时间
     */
    private Date goodsMappingStartTime;

    /**
     * 流向上传、商品对照结束时间
     */
    private Date goodsMappingEndTime;

    /**
     * 客户对照、销量申诉开始时间
     */
    private Date customerMappingStartTime;

    /**
     * 客户对照、销量申诉结束时间
     */
    private Date customerMappingEndTime;

    /**
     * 在途库存、终端库存上报开始时间
     */
    private Date goodsBatchStartTime;

    /**
     * 在途库存、终端库存上报结束时间
     */
    private Date goodsBatchEndTime;

    /**
     * 窜货提报开始时间
     */
    private Date flowCrossStartTime;

    /**
     * 窜货提报结束时间
     */
    private Date flowCrossEndTime;

    /**
     * 团购开始时间
     */
    private Date flowGroupStartTime;

    /**
     * 团购结束时间
     */
    private Date flowGroupEndTime;


    private Integer employeeBackupStatus;
    private Date    employeeBackupTime;
    private Integer basisStatus;
    private Date    basisTime;
    private Integer basisBackupStatus;
    private Date    basisBackupTime;
    private Integer washStatus;
    private Date    washTime;
    private Integer gbLockStatus;
    private Date    gbLockTime;
    private Integer unlockStatus;
    private Date    unlockTime;
    private Integer gbUnlockStatus;
    private Date    gbUnlockTime;
    private Integer taskStatus;
    private Date    taskTime;

}
