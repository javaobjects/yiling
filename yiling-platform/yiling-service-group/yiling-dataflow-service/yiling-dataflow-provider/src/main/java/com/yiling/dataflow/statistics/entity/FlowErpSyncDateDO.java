package com.yiling.dataflow.statistics.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/1/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_erp_sync_date")
public class FlowErpSyncDateDO extends BaseDO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 统计日期
     */
    private Date taskTime;

    /**
     * 是否已同步：0-否 1-是
     */
    private Integer syncFlag;

    /**
     * 任务数据包是否有同步：0-否 1-是
     */
    private Integer flowControlFlag;

    /**
     * 销售是否有同步：0-否 1-是
     */
    private Integer saleFlag;

    /**
     * 采购是否有同步：0-否 1-是
     */
    private Integer purchaseFlag;

    /**
     * 库存是否有同步：0-否 1-是
     */
    private Integer goodsBatchFlag;

    /**
     * 创建时间
     */
    private Date createTime;

}
