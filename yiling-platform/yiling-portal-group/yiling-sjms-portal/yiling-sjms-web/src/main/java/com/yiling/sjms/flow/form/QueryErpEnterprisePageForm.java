package com.yiling.sjms.flow.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpEnterprisePageForm extends QueryPageListForm {

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码", example = "1")
    private Long crmEnterpriseId;

    /**
     * 经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level
     */
    @ApiModelProperty(value = "经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level", example = "0")
    private Integer enameLevelValue;

    /**
     * 对接时间开始
     */
    @ApiModelProperty(value = "对接时间开始", example = "")
    private Date depthTimeStart;

    /**
     * 对接时间结束
     */
    @ApiModelProperty(value = "对接时间结束", example = "")
    private Date depthTimeEnd;

    /**
     * 上次收集时间开始
     */
    @ApiModelProperty(value = "上次收集时间开始", example = "")
    private Date lastestCollectDateStart;

    /**
     * 上次收集时间结束
     */
    @ApiModelProperty(value = "上次收集时间结束", example = "")
    private Date lastestCollectDateEnd;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口， 0 全部
     */
    @ApiModelProperty(value = "流向收集方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口， 0 全部。字典：erp_client_flow_mode", example = "0")
    private Integer flowMode;

    /**
     * 同步状态：0-未开启 1-开启， -1 全部
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启， -1 全部。字典：erp_client_sync_status", example = "-1")
    private Integer syncStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活， -1 全部
     */
    @ApiModelProperty(value = "终端激活状态：0-未激活 1-已激活， -1 全部。字典：erp_client_status", example = "-1")
    private Integer clientStatus;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向， -1 全部
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向， -1 全部。字典：erp_client_flow_level", example = "-1")
    private Integer flowLevel;

    /**
     * 运行状态：1-未运行 2-运行中。字典：erp_client_running_status
     */
    @ApiModelProperty(value = "运行状态：1-未运行 2-运行中 0-全部。字典：erp_client_running_status", example = "0")
    private Integer runningStatus;

}
