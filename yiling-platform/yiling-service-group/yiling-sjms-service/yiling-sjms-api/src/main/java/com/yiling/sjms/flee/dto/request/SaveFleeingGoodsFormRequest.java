package com.yiling.sjms.flee.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFleeingGoodsFormRequest extends BaseRequest {

    /**
     * 主流程表单id
     */
    private Long formId;

    private Long taskId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 上传人id
     */
    private String importUser;

    /**
     * 数据检查 1-通过 2-未通过 3-警告
     */
    private Integer checkStatus;

    /**
     * 导入状态：1-导入成功 2-导入失败
     */
    private Integer importStatus;

    /**
     * 导入失败原因
     */
    private String failReason;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 失败结果下载地址
     */
    private String resultUrl;

    /**
     * 上传时间
     */
    private Date importTime;

    /**
     * 文件类型 1-申报 2-申报确认
     */
    private Integer importFileType;

    /**
     * 调整流向的月份
     */
    private String toMonth;

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 发起人姓名
     */
    private String empName;
    
}
