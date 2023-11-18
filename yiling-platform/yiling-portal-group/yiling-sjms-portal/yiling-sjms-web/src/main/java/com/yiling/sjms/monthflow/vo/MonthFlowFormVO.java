package com.yiling.sjms.monthflow.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MonthFlowFormVO extends BaseVO {
    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String excelName;


    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 上传人工号
     */
    @ApiModelProperty("上传人工号")
    private String uploader;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploaderName;

    /**
     * 流向数据类型 1库存2采购3销售
     */
    @ApiModelProperty("流向数据类型 1采购2销售3库存")
    private Integer dataType;

    /**
     * 数据检查状态 1 通过 2未通过 3检查中
     */
    @ApiModelProperty("数据检查状态 1 通过 2未通过 3警告")
    private Integer checkStatus;

    /**
     * 数据检查未通过原因
     */
    @ApiModelProperty("数据检查未通过原因-弹窗显示")
    private String reason;

    /**
     * 导入状态 1 成功 2失败 3导入中
     */
    @ApiModelProperty("导入状态 1 成功 2失败 3导入中")
    private Integer importStatus;



    /**
     * 来源excel地址
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String sourceUrl;

    /**
     * 目标excel地址
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String targetUrl;


    /**
     * 下载文件url
     */
    @ApiModelProperty("下载文件url")
    private String fileUrl;

    /**
     * 下载文件名
     */
    @ApiModelProperty("下载文件名")
    private String downLoadName;



}
