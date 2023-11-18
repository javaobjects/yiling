package com.yiling.dataflow.flowcollect.bo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 月流向上传记录 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowMonthUploadRecordBO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    private String fileName;

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人工号
     */
    private String createUserNo;

    /**
     * 创建时间
     */
    private Date createTime;

}
