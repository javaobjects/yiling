package com.yiling.admin.system.excel.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@Data
public class ExcelTaskRecordVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 调用菜单路径
     */
    @ApiModelProperty(value = "调用菜单路径")
    private String menuName;

    /**
     * 导出状态状态：0-正在导出；1-导出成功;-1导出失败
     */
    @ApiModelProperty(value = "导出状态状态：1-正在导出；3-导出失败；2-导出成功; ")
    private Integer status;

    /**
     * 上传文件的名称
     */
    @ApiModelProperty(value = "上传文件的名称")
    private String fileName;

    /**
     * 下载文件的名称
     */
    @ApiModelProperty(value = "下载文件的名称")
    private String downloadName;

    /**
     * 解析文件时间
     */
    @ApiModelProperty(value = "解析文件完成时间")
    private Date finishTime;

    /**
     * 任务创建时间
     */
    @ApiModelProperty(value = "任务创建时间")
    private Date createTime;

    /**
     * 成功数量
     */
    @ApiModelProperty(value = "成功数量")
    private Integer successNumber;

    /**
     * 失败数量
     */
    @ApiModelProperty(value = "失败数量")
    private Integer failNumber;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createName;
}
