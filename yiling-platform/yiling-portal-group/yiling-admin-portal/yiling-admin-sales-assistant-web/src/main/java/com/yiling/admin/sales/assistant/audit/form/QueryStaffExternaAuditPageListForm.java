package com.yiling.admin.sales.assistant.audit.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询个人完善信息审核分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2022/1/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStaffExternaAuditPageListForm extends QueryPageListForm {

    /**
     * 姓名（精确查询）
     */
    @ApiModelProperty("姓名（模糊查询）")
    private String name;

    /**
     * 身份证号（精确查询）
     */
    @ApiModelProperty("身份证号（模糊查询）")
    private String idNumber;

    /**
     * 审核时间-开始
     */
    @ApiModelProperty("审核时间-开始")
    private Date auditTimeBegin;

    /**
     * 审核时间-结束
     */
    @ApiModelProperty("审核时间-结束")
    private Date auditTimeEnd;

    /**
     * 审核状态：0-全部 1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty("审核状态：0-全部 1-待审核 2-审核通过 3-审核驳回")
    private Integer auditStatus;

    /**
     * 审核人姓名（精确查询）
     */
    @ApiModelProperty("审核人姓名（模糊查询）")
    private String auditUserName;
}
