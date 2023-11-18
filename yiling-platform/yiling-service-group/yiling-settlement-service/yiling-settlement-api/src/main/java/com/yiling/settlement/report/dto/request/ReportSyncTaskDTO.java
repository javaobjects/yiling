package com.yiling.settlement.report.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利报表同步任务表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportSyncTaskDTO extends BaseDTO {


    /**
     * 同步类型：1-流向订单
     */
    private Integer type;

    /**
     * 同步状态：1-待同步 2-同步失败 3-同步成功
     */
    private Integer status;

    /**
     * 同步数据
     */
    private String syncData;

    /**
     * 失败原因
     */
    private String errMsg;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
