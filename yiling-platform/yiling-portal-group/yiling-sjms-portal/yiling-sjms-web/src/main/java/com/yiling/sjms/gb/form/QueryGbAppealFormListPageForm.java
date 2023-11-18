package com.yiling.sjms.gb.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/15
 */
@Data
public class QueryGbAppealFormListPageForm extends QueryPageListForm {

    /**
     * 所属年月
     */
    @ApiModelProperty(value = "所属年月")
    private String matchMonth;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String gbMonth;

    /**
     * 商业公司编码
     */
    @ApiModelProperty(value = "出库商业编码")
    private Long crmId;

    /**
     * 终端名称
     */
    @ApiModelProperty(value = "出库终端名称")
    private String enterpriseName;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long goodsCode;

    /**
     * 处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
     */
    @ApiModelProperty(value = "处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status")
    private Integer dataExecStatus;

    /**
     * 最后操作人
     */
    @ApiModelProperty(value = "最后操作人")
    private Long lastUpdateUser;

    /**
     * 最后操作时间-开始
     */
    @ApiModelProperty(value = "最后操作时间-开始")
    private Date lastUpdateTimeStart;

    /**
     * 最后操作时间-结束
     */
    @ApiModelProperty(value = "最后操作时间-结束")
    private Date lastUpdateTimeEnd;

}
