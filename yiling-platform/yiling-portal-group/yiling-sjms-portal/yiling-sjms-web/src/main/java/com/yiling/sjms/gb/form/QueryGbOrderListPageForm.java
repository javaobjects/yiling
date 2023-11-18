package com.yiling.sjms.gb.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/23
 */
@Data
public class QueryGbOrderListPageForm extends QueryPageListForm {

    /**
     * 团购表单ID
     */
    @ApiModelProperty(value = "团购表单ID")
    private Long formId;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 出库商业ID
     */
    @ApiModelProperty(value = "出库商业ID")
    private Long crmId;

    /**
     * 出库终端名称
     */
    @ApiModelProperty(value = "出库终端名称")
    private String enterpriseName;

    /**
     * 复核状态：1-未复核 2-已复核，字典：gb_form_review_status
     */
    @ApiModelProperty(value = "复核状态：1-未复核 2-已复核，字典：gb_form_review_status")
    private Integer checkStatus;
    
    /**
     * 提交时间-开始
     */
    @ApiModelProperty(value = "提交时间-开始")
    private Date createTimeStart;

    /**
     * 提交时间-结束
     */
    @ApiModelProperty(value = "提交时间-结束")
    private Date createTimeEnd;


}
