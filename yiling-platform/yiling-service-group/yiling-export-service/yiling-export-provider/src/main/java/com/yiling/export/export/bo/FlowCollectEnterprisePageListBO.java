package com.yiling.export.export.bo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/2/22
 */
@Data
public class FlowCollectEnterprisePageListBO {

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码", example = "1")
    private Long crmEnterpriseId;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "经销商名称", example = "")
    private String clientName;

    /**
     * 经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level
     */
    private String enameLevel;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "接口实施负责人", example = "")
    private String installEmployee;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    private String flowModeStr;


    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    private String flowLevelStr;

    /**
     * 对接时间
     */
    private String depthTimeStr;

    /**
     * 上次收集流向时间
     */
    private String lastestCollectDateStr;

    /**
     * 运行状态：1-未运行 2-运行中。字典：erp_client_running_status
     */
    private String runningStatusStr;

    /**
     * 说明
     */
    private String description;
}
