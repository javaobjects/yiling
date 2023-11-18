package com.yiling.sales.assistant.task.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员推广任务记录
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskMemberRecordDTO extends BaseDTO {


    private static final long serialVersionUID = -2802939956481578218L;
    /**
     * 终端名称
     */
    private String ename;

    /**
     * 联系人
     */
    private String contactorPhone;

    /**
     * 联系人id
     */
    private Long contactorUserId;

    /**
     * 用户承接任务id
     */
    private Long userTaskId;

    /**
     * 下单时间或者购买时间
     */
    private Date tradeTime;

    /**
     * 8-会员推广-购买 9-会员推广-满赠
     */
    private Integer finishType;

}
