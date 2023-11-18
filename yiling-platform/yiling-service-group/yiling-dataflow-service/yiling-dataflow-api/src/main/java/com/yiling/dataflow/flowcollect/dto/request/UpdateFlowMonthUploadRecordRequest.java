package com.yiling.dataflow.flowcollect.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新月流向上传记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowMonthUploadRecordRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;

    /**
     * 检查状态：1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 导入失败原因
     */
    private String failReason;

}
