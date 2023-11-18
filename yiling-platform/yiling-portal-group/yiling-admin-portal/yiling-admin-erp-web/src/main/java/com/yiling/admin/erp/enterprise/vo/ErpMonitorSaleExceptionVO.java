package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("销售异常信息VO")
public class ErpMonitorSaleExceptionVO extends BaseVO {

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String ename;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 原始单据上传时间
     */
    @ApiModelProperty(value = "原始单据上传时间")
    private Date parentUploadTime;

    /**
     * 操作类型：1新增 2修改（字典：erp_oper_type）
     */
    @ApiModelProperty(value = "操作类型：1新增 2修改（字典：erp_oper_type）")
    private Integer operType;

    /**
     * 销售单据时间
     */
    @ApiModelProperty(value = "销售单据时间")
    private Date flowTime;

    /**
     * 销售单主键ID
     */
    @ApiModelProperty(value = "销售单主键ID")
    private String soId;

    /**
     * 销售单号
     */
    @ApiModelProperty(value = "销售单号")
    private String soNo;

    /**
     * 任务上传ID
     */
    @ApiModelProperty(value = "任务上传ID")
    private Long controlId;

    /**
     * 原始数据ID（新增的）
     */
    private Long parentId;


}
