package com.yiling.sjms.flee.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 窜货申报表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SalesAppealFormVO extends BaseVO {
    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 表单类型 1销量申诉表，2销量申诉确认表
     */
    private Integer type;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String excelName;

    /**
     * 补传流向的月份
     */
    @ApiModelProperty("补传流向的月份")
    private Date appealMonth;

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
    @ApiModelProperty("数据检查状态 1 通过 2未通过 3检查中")
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
     * 文件导入行数
     */
    private Integer lineNumber;

    /**
     * 来源excel地址
     */
    @ApiModelProperty("来源excel地址")
    private String sourceUrl;

    /**
     * 目标excel地址
     */
    @ApiModelProperty("目标excel地址-用来下载使用")
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

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
