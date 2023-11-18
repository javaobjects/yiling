package com.yiling.sjms.flowcollect.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向上传记录列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthUploadRecordListItemVO extends BaseVO {

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 流向数据类型：1-销售 2-库存 3-采购
     */
    @ApiModelProperty("流向数据类型：1-销售 2-库存 3-采购")
    private Integer dataType;

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
     * 检查状态：1-通过 2-未通过 3-警告
     */
    @ApiModelProperty("检查状态：1-通过 2-未通过 3-警告")
    private Integer checkStatus;

    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    private String fileUrl;

    /**
     * 上传人名称
     */
    @ApiModelProperty("上传人名称")
    private String createUserName;

    /**
     * 上传人工号
     */
    @ApiModelProperty("上传人工号")
    private String createUserNo;

    /**
     * 上传时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 下载文件名
     */
    @ApiModelProperty("下载文件名")
    private String downLoadName;

}