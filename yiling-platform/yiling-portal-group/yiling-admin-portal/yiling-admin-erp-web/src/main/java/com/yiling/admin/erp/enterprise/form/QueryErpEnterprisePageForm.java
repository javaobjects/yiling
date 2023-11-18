package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询ERP对接企业管理分页参数")
public class QueryErpEnterprisePageForm extends QueryPageListForm {

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
     * 对接负责人
     */
    @ApiModelProperty(value = "对接负责人", example = "2")
    private String installEmployee;

    /**
     * 对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接，-1 全部
     */
    @ApiModelProperty(value = "对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接，-1 全部", example = "3")
    private Integer depth;

    /**
     * 流向级别：0-未对接 1-以岭流向 2-全品流向，-1 全部
     */
    @ApiModelProperty(value = "流向级别：0-未对接 1-以岭流向 2-全品流向，-1 全部", example = "4")
    private Integer flowLevel;

    /**
     * 同步状态：0-未开启 1-开启，-1 全部
     */
    @ApiModelProperty(value = "同步状态：0-未开启 1-开启，-1 全部", example = "5")
    private Integer syncStatus;

    /**
     * 监控状态：0-未开启 1-开启，-1 全部
     */
    @ApiModelProperty(value = "监控状态：0-未开启 1-开启，-1 全部", example = "6")
    private Integer monitorStatus;

    /**
     * 对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    @ApiModelProperty(value = "对接方式：0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口", example = "9")
    private Integer flowMode;

    /**
     * 终端激活状态：0-未激活 1-已激活
     */
    @ApiModelProperty(value = "终端激活状态：-1 全部，0-未激活 1-已激活", example = "10")
    private Integer clientStatus;

}
