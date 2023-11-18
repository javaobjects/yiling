package com.yiling.sjms.flee.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shixing.sun
 * @date: 2023/3/13 0013
 */
@Data
public class SaveSalesAppealDetailForm extends BaseForm {

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    private String key;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名")
    private String name;

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
}
