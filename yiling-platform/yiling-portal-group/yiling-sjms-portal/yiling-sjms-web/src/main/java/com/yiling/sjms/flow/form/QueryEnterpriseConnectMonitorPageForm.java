package com.yiling.sjms.flow.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryEnterpriseConnectMonitorPageForm
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseConnectMonitorPageForm extends QueryPageListForm {
    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String crmEnterpriseName;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 对接级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "对接级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商")
    private Integer supplierLevel;

    /**
     * 对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口
     */
    @ApiModelProperty(value = "对接方式：1-工具 2-ftp 3-第三方接口 4-以岭平台接口")
    private Integer flowMode;


    /**
     * 直连状态 0：失败 1：成功
     */
    @ApiModelProperty(value = "直连状态 0：失败 1：成功")
    private Integer connectStatus;

    /**
     * 对接时间-开始
     */
    @ApiModelProperty(value = "对接时间-开始")
    private Date startDockingTime;

    /**
     * 对接时间-结束
     */
    @ApiModelProperty(value = "对接时间-结束")
    private Date endDockingTime;

    /**
     * 收集时间-开始
     */
    @ApiModelProperty(value = "收集时间-开始")
    private Date startCollectionTime;

    /**
     * 收集时间-结束
     */
    @ApiModelProperty(value = "收集时间-结束")
    private Date endCollectionTime;

    /**
     * 回流流向天数计数-起
     */
    @ApiModelProperty(value = "回流流向天数计数-起")
    private Integer startFlowDayCount;

    /**
     * 回流流向天数计数-止
     */
    @ApiModelProperty(value = "回流流向天数计数-止")
    private Integer endFlowDayCount;
}
