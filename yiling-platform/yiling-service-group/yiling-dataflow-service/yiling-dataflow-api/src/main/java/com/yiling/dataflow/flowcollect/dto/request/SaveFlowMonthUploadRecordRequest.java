package com.yiling.dataflow.flowcollect.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存月流向上传记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowMonthUploadRecordRequest extends BaseRequest {

    /**
     * 上传队列ID
     */
    private Long recordId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 流向数据类型：1-销售 2-库存 3-采购
     */
    private Integer dataType;
    /**
     * 上传类型：1-正常 2-补传
     */
    private Integer uploadType;
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
     * 备注
     */
    private String remark;


}
