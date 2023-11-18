package com.yiling.dataflow.flow.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryEnterpriseConnectMonitorPageRequest
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseConnectMonitorPageRequest extends QueryPageListRequest {

    /**
     * 经销商名称
     */
    private String crmEnterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商编码列表（权限筛选）
     */
    private List<Long> crmEnterpriseIds;

    /**
     * 对接级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private Integer flowMode;

    /**
     * 直连状态 0：失败 1：成功
     */
    private Integer connectStatus;

    /**
     * 对接时间-开始
     */
    private Date startDockingTime;

    /**
     * 对接时间-结束
     */
    private Date endDockingTime;

    /**
     * 收集时间-开始
     */
    private Date startCollectionTime;

    /**
     * 收集时间-结束
     */
    private Date endCollectionTime;

    /**
     * 回流流向天数计数-起
     */
    private Integer startFlowDayCount;

    /**
     * 回流流向天数计数-止
     */
    private Integer endFlowDayCount;

    /**
     * 权限对象
     */
    private SjmsUserDatascopeBO userDatascopeBO;
}
