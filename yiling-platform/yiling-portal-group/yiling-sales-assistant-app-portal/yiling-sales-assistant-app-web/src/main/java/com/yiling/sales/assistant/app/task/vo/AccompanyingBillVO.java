package com.yiling.sales.assistant.app.task.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 随货同行单上传
 * </p>
 *
 * @author gxl
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccompanyingBillVO extends BaseVO {


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String docCode;

    /**
     * 上传时间
     */
    private Date uploadTime;


    /**
     * 最新上传时间
     */
    private Date lastUploadTime;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核人id
     */
    private Long auditUserId;

    /**
     * 随货同行单
     */
    @ApiModelProperty(value = "随货同行单")
    private String accompanyingBillPic;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 6-待审核 1-待审核 2-审核通过 3-审核失败 4-审核失败 5-审核失败 ")
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因")
    private String rejectionReason;

    /**
     * 发货名称
     */
    @ApiModelProperty(value = "发货单位")
    private String distributorEname;

    private Long createUser;


    private Date createTime;


    private Long updateUser;


}
