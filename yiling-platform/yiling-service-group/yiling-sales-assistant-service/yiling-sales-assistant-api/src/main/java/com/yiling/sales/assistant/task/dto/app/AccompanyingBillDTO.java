package com.yiling.sales.assistant.task.dto.app;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class AccompanyingBillDTO extends BaseDTO {


    /**
     * 单据编号
     */
    private String docCode;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 发货单位eid
     */
    private Long distributorEid;
    /**
     * 发货名称
     */
    private String distributorEname;

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
    private String accompanyingBillPic;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    private String rejectionReason;


    private Long createUser;


    private Date createTime;


    private Long updateUser;


    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
