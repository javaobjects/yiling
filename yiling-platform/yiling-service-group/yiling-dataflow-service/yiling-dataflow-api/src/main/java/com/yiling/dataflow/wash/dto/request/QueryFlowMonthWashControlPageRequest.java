package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowMonthWashControlPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 2699609447886278314L;

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
