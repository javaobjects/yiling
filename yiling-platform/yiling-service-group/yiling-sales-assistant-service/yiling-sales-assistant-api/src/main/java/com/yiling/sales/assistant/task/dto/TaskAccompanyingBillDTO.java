package com.yiling.sales.assistant.task.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gxl
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskAccompanyingBillDTO extends BaseDTO {

    private static final long serialVersionUID = -4619294996731600428L;

    private Long accompanyingBillId;
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户任务id
     */
    private Long userTaskId;

    /**
     * 随货同行单编号
     */
    private String docCode;


    private Long createUser;


    private Date createTime;


    private Long updateUser;


    private Date updateTime;


    private String remark;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 收货单位eid
     */
    private Long recvEid;

    private String recvEname;
}
