package com.yiling.dataflow.flowcollect.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向上传记录表
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_month_upload_record")
public class FlowMonthUploadRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 上传队列ID
     */
    private Long recordId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 上传类型：1-正常 2-补传
     */
    private Integer uploadType;

    /**
     * 流向数据类型：1-销售 2-库存 3-采购
     */
    private Integer dataType;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;

    /**
     * 导入失败原因
     */
    private String failReason;

    /**
     * 检查状态：1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 清洗任务id
     */
    private Long washTaskId;
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
