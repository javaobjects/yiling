package com.yiling.admin.sales.assistant.task.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class TaskAccompanyingBillVO extends BaseVO {

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
    @ApiModelProperty(value = "随货同行单编号")
    private String docCode;



    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传资料时间")
    private Date uploadTime;


    /**
     * 收货单位eid
     */
    private Long recvEid;
    @ApiModelProperty(value = "收货单位")
    private String recvEname;
}
