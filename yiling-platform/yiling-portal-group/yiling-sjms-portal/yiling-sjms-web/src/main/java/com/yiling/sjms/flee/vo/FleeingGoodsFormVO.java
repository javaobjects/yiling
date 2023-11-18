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
public class FleeingGoodsFormVO extends BaseVO {

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 上传人id
     */
    @ApiModelProperty("上传人id")
    private String importUser;

    /**
     * 数据检查 1-通过 2-未通过 3-警告
     */
    @ApiModelProperty("数据检查 1-通过 2-未通过 3-警告")
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    @ApiModelProperty("导入状态：1-导入成功 2-导入失败")
    private Integer importStatus;

    /**
     * 导入失败原因
     */
    @ApiModelProperty("导入失败原因")
    private String failReason;

    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    private String fileUrl;

    /**
     * 下载文件名称
     */
    @ApiModelProperty("下载文件名称")
    private String downLoadName;

    /**
     * 失败结果下载地址
     */
    @ApiModelProperty("失败结果下载地址")
    private String resultUrl;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date importTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    // ========================================

    /**
     * 申报类型 1-电商 2-非电商
     */
    @ApiModelProperty("申报类型 1-电商 2-非电商")
    private Integer reportType;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String importUserName;

    /**
     * 流向数据类型 1库存2采购3销售
     */
    @ApiModelProperty("流向数据类型 1库存2采购3销售")
    private Integer dataType = 2;
}
