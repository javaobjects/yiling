package com.yiling.sjms.flow.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补传月流向
 * @author: GXL
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSubFormRequest extends BaseRequest {

    private Long id;
    private static final long serialVersionUID = -6280803173243158414L;
    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件地址
     */
    private String key;


    /**
     * 导入任务id
     */
    private Long taskId;



    /**
     * 补传流向的月份
     */
    private Date appealMonth;


    /**
     * 流向数据类型 1库存2采购3销售
     */
    private Integer dataType;

    /**
     * 数据检查状态 1 通过 2未通过 3检查中
     */
    private Integer checkStatus;

    /**
     * 数据检查未通过原因
     */
    private String reason;

    /**
     * 导入状态 1 成功 2失败 3导入中
     */
    private Integer importStatus;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人工号
     */
    private String uploader;

    /**
     * 上传人姓名
     */
    private String uploaderName;

    /**
     * 目标excel地址
     */
    private String targetUrl;

    /**
     * flow_month_upload_record表主键
     */
    private Long flowMonthUploadId;
}
