package com.yiling.sjms.export.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/31
 */
@Data
public class ExportTaskRecordVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 所属组名
     */
    @ApiModelProperty(value = "所属组名")
    private String groupName;

    /**
     * 调用菜单路径
     */
    @ApiModelProperty(value = "调用菜单路径")
    private String menuName;

    /**
     * 导出状态状态：0-正在导出；1-导出成功;-1导出失败
     */
    @ApiModelProperty(value = "导出状态状态：0-正在导出；-1-导出失败；1-导出成功; ")
    private Integer status;

    /**
     * 下载文件的名称
     */
    @ApiModelProperty(value = "下载文件的名称")
    private String fileName;

    /**
     * 生成文件时间
     */
    @ApiModelProperty(value = "生成文件时间")
    private Date finishTime;

    /**
     * 任务创建时间
     */
    @ApiModelProperty(value = "任务创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 查询条件对象列表
     */
    @ApiModelProperty(value = "查询条件对象列表")
    private List<ExportSearchConditionVO> searchConditionList;
}
