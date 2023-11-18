package com.yiling.dataflow.statistics.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDTO;

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
public class FlowErpSyncDateDTO extends BaseDTO {

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
     * 创建时间
     */
    private Date createTime;

}
