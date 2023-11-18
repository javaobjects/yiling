package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
public class ErpEnterpriseDetailVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * crm企业ID
     */
    @ApiModelProperty(value = "crm企业ID", example = "0")
    private Long crmEnterpriseId;

    /**
     * crm企业名称
     */
    @ApiModelProperty(value = "crm企业名称", example = "")
    private String crmEnterpriseName;

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
     * 父级企业id
     */
    @ApiModelProperty(value = "父级企业id", example = "1")
    private Long suId;

    /**
     * 父类企业名称
     */
    @ApiModelProperty(value = "父类企业名称", example = "1")
    private String clientNameParent;

    /**
     * 分公司编码
     */
    @ApiModelProperty(value = "分公司编码", example = "1")
    private String suDeptNo;

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
    @ApiModelProperty(value = "对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接", example = "2")
    private Integer depth;

    /**
     * 对接时间
     */
    @ApiModelProperty(value = "对接时间", example = "7")
    private Date    depthTime;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向", example = "3")
    private Integer flowLevel;

    /**
     * 对接负责人
     */
    @ApiModelProperty(value = "对接负责人", example = "4")
    private String installEmployee;

    /**
     * 远程执行命令：0-执行完成 1-远程更新版本 2-重启服务
     */
    @ApiModelProperty(value = "远程执行命令：0-执行完成 1-远程更新版本 2-重启服务", example = "5")
    private Integer command;

    /**
     * 同步状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启", example = "6")
    private Integer syncStatus;

    /**
     * 监控状态：0-未开启 1-开启
     */
    @ApiModelProperty(value = "监控状态：0-未开启 1-开启", example = "5")
    private Integer monitorStatus;

    /**
     * erp品牌
     */
    @ApiModelProperty(value = "erp品牌", example = "7")
    private String erpBrand;

    /**
     * 商务人员
     */
    @ApiModelProperty(value = "商务人员", example = "8")
    private String businessEmployee;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    @ApiModelProperty(value = "对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口", example = "9")
    private Integer flowMode;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "10")
    private String remark;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    @ApiModelProperty(value = "终端激活状态：0-未激活 1-已激活", example = "5")
    private Integer clientStatus;

}
