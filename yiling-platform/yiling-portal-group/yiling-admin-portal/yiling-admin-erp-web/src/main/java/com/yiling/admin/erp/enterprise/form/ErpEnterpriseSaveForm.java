package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpEnterpriseSaveForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", example = "1")
    private Long id;

    /**
     * crm企业ID
     */
    @ApiModelProperty(value = "crm企业ID", example = "1")
    private Long crmEnterpriseId;

    /**
     * 企业id
     */
    @NotNull
    @ApiModelProperty(value = "企业ID", example = "1")
    private Long rkSuId;

    /**
     * 父级企业id
     */
    @ApiModelProperty(value = "父级企业id", example = "1")
    private Long suId;

    /**
     * 分公司编码
     */
    @ApiModelProperty(value = "分公司编码", example = "1")
    private String suDeptNo;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
     */
    @NotNull
    @Min(0)
    @ApiModelProperty(value = "对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接", example = "2")
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向
     */
    @NotNull
    @Min(0)
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
    @NotNull
    @Min(0)
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启", example = "6")
    private Integer syncStatus;

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
    @NotNull
    @Min(0)
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
