package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ERP对接企业管理查询列表分页
 *
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Data
public class ErpEnterprisePageVO extends BaseVO {

    /**
     * crm企业id
     */
    @ApiModelProperty(value = "crm企业id", example = "1")
    private Long crmEnterpriseId;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID", example = "1")
    private Long rkSuId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称", example = "1")
    private String clientName;

    /**
     * 客户端实例Id
     */
    @ApiModelProperty(value = "APPKEY", example = "2")
    private String clientKey;

    /**
     * 客户端实例密钥
     */
    @ApiModelProperty(value = "密钥", example = "3")
    private String clientSecret;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
     */
    @ApiModelProperty(value = "对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接", example = "4")
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向", example = "5")
    private Integer flowLevel;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "对接负责人", example = "6")
    private String installEmployee;

    /**
     * 对接时间
     */
    @ApiModelProperty(value = "对接时间", example = "7")
    private Date depthTime;

    /**
     * 同步状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启", example = "8")
    private Integer syncStatus;

    /**
     * 监控状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "监控状态：0-未开启 1-开启", example = "5")
    private Integer monitorStatus;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    @ApiModelProperty(value = "终端激活状态：0-未激活 1-已激活", example = "5")
    private Integer clientStatus;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    @ApiModelProperty(value = "对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口", example = "9")
    private Integer flowMode;

}
