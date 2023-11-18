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
public class SaveSalesAppeaDraftlForm extends BaseForm {


    /**
     * 附件
     */
    @ApiModelProperty("附件 文件url集合，用英文逗号分割")
    private String appendix;

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;

    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    private String sourceUrl;

    /**
     * 表单类型 1销量申诉表，2销量申诉确认表
     */
    private Integer type;

    /**
     * 文件名称
     */
    private String excelName;

    /**
     * 补传流向的月份
     */
    @ApiModelProperty("补传流向的月份")
    private Date appealMonth;

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
     * 流向数据类型 1库存2采购3销售
     */
    @ApiModelProperty("流向数据类型 1库存2采购3销售")
    private Integer dataType;


    /**
     * 目标excel地址
     */
    private String targetUrl;
}
