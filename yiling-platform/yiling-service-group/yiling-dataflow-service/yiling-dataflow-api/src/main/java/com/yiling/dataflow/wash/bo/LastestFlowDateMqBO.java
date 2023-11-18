package com.yiling.dataflow.wash.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Data
public class LastestFlowDateMqBO implements Serializable {

    private static final long serialVersionUID = 6321850184353341077L;

    /**
     * 月流向清洗日程主键id
     */
    private Long washControlId;

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * 最新流向业务日期
     */
    private Date lastestFlowDate;

    /**
     * eid
     */
    private Long eid;

    /**
     * 对接方式
     */
    private Integer flowMode;



}
