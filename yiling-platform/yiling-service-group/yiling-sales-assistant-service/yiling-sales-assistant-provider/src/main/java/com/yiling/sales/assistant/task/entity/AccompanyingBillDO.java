package com.yiling.sales.assistant.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("sa_accompanying_bill")
public class AccompanyingBillDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
