package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 搜索QA
 * @author: fan.shen
 * @date: 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryQAPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "问答id")
    private Long id;

    @ApiModelProperty(value = "上级问答id")
    private Long qaId;

    @ApiModelProperty(value = "创建人类型 1-患者，2-医生")
    private Long userType;

    @ApiModelProperty(value = "针对的内容")
    private String title;

    @ApiModelProperty("开始时间")
    private Date beginDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

    @ApiModelProperty("业务线id 1-HMC,2-IH-doc,3-IH-patient")
    private Integer lineId;
}