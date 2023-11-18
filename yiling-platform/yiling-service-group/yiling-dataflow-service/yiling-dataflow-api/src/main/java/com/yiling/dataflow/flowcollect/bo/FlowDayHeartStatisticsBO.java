package com.yiling.dataflow.flowcollect.bo;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowDayHeartStatisticsBO extends BaseDTO {
    /**
     * 商业公司编码
     */
    private Long crmEnterpriseId;


    /**
     * 企业名称
     */
    private String ename;

    //扩展信息
    private Integer supplierLevel;

    /**
     * 流向打取人工号
     */
    private String flowJobNumber;

    /**
     * 流向打取人姓名 :从esb_employee 中获取
     */
    private String flowLiablePerson;

    /**
     * 商务负责人工号
     */
    private String commerceJobNumber;

    /**
     * 商务负责人姓名从esb_employee 中获取
     */
    private String commerceLiablePerson;
    //---end

    /**
     * 流向类型：1-采购 2-销售 3库存
     */
    private Integer flowType;
    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode
     */
    private Integer flowMode;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 对接时间
     */
    private Date depthTime;

    private Map<String, FlowDayHeartStatisticsDetailBO> dateHeadersInfoMap;

}

