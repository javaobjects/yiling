package com.yiling.sjms.flow.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectEnterprisePageDetailVO extends BaseVO {

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码", example = "1")
    private Long crmEnterpriseId;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码", example = "")
    private String licenseNumber;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "商业id", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "经销商名称", example = "")
    private String clientName;

    /**
     * 经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level
     */
    @ApiModelProperty(value = "经销商级别：1-一级经销商 2-二级经销商 3-连锁商业 4-未分级经销商 5-云仓商业 6-准一级经销商 0-全部，数据字典：flow_analyse_ename_level", example = "0")
    private Integer enameLevelValue;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "接口实施负责人", example = "")
    private String installEmployee;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    @ApiModelProperty(value = "流向收集方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口。字典：erp_client_flow_mode", example = "1")
    private Integer flowMode;


    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向。字典：erp_client_flow_level", example = "1")
    private Integer flowLevel;

    /**
     * 对接时间
     */
    @ApiModelProperty(value = "对接时间", example = "")
    private Date depthTime;

    /**
     * 上次收集流向时间
     */
    @ApiModelProperty(value = "上次收集流向时间", example = "")
    private Date lastestCollectDate;

    /**
     * 同步状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启", example = "1")
    private Integer syncStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    @ApiModelProperty(value = "终端激活状态：0-未激活 1-已激活。字典：erp_client_status", example = "1")
    private Integer clientStatus;

    /**
     * 运行状态：1-未运行 2-运行中。字典：erp_client_running_status
     */
    @ApiModelProperty(value = "运行状态：1-未运行 2-运行中。字典：erp_client_running_status", example = "2")
    private Integer runningStatus;

    /**
     * 说明
     */
    @ApiModelProperty(value = "说明", example = "")
    private String description;

}
