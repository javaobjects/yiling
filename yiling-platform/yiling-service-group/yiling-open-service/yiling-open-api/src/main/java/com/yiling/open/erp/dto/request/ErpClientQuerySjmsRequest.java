package com.yiling.open.erp.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientQuerySjmsRequest extends QueryPageListRequest{

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 企业id
     */
    private Long rkSuId;

    /**
     * 经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level
     */
    private Integer enameLevelValue;

    /**
     * 对接时间开始
     */
    private Date depthTimeStart;

    /**
     * 对接时间结束
     */
    private Date depthTimeEnd;

    /**
     * 上次收集时间开始
     */
    private Date lastestCollectDateStart;

    /**
     * 上次收集时间结束
     */
    private Date lastestCollectDateEnd;

    /**
     * 上次流向业务时间开始
     */
    private Date lastestFlowDateStart;

    /**
     * 上次流向业务时间结束
     */
    private Date lastestFlowDateEnd;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口， 0-全部
     */
    private Integer flowMode;

    /**
     * 同步状态：0-未开启 1-开启， -1 全部
     */
    private Integer syncStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活， -1 全部
     */
    private Integer clientStatus;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向， -1 全部
     */
    private Integer flowLevel;

    /**
     * 企业id列表
     */
    private List<Long> rkSuIdList;

    /**
     * 经销商编码
     */
    private List<Long> crmEnterpriseIdList;

    /**
     * 统一信用代码
     */
    private List<String> licenseNumberList;

    /**
     * 运行状态：1-未运行 2-运行中 0-全部。字典：erp_client_running_status
     */
    private Integer runningStatus;

    /**
     * 员工工号
     */
    private String currentUserCode;
}
