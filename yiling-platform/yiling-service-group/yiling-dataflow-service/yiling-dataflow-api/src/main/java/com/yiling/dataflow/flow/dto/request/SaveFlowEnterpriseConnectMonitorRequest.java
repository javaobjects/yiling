package com.yiling.dataflow.flow.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowEnterpriseConnectMonitorRequest
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowEnterpriseConnectMonitorRequest  extends BaseRequest {

    private Long id;

    /**
     * erp客户端id
     */
    private Long erpClientId;

    /**
     * 经销商id
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String crmEnterpriseName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 对接级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 对接时间
     */
    private Date dockingTime;

    /**
     * 流向收集时间
     */
    private Date flowCollectionTime;

    /**
     * 回传流向天数计数
     */
    private Integer returnFlowDayCount;

    /**
     * 直连状态 0：失败 1：成功
     */
    private Integer connectStatus;

    /**
     * 说明
     */
    private String explanation;
}
