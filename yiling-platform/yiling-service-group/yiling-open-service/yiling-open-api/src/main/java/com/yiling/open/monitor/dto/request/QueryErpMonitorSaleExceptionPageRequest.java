package com.yiling.open.monitor.dto.request;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpMonitorSaleExceptionPageRequest extends QueryPageListRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 父类企业ID
     */
    private Long parentId;

    /**
     * 销售单据时间，开始
     */
    private Date flowTimeStart;

    /**
     * 销售单据时间，结束
     */
    private Date flowTimeEnd;

    /**
     * 销售单主键ID
     */
    private String soId;

    /**
     * 销售单号
     */
    private String soNo;

    /**
     * 任务上传ID
     */
    private Long controlId;

    /**
     * 查询类型：0-全部，1-关闭对接企业数量，2-24小时无心跳企业数量,3-当月未上传销售企业数量，4-销售异常数量，5-采购异常数量
     */
    private Integer  openType;

}
