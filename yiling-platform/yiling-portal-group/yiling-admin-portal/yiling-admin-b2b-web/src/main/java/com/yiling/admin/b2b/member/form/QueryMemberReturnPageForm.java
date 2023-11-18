package com.yiling.admin.b2b.member.form;

import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员退款审核列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryMemberReturnPageForm extends QueryPageListForm {

    /**
     * 终端ID
     */
    @ApiModelProperty("终端ID")
    private Long eid;

    /**
     * 终端名称
     */
    @Length(max = 50)
    @ApiModelProperty("终端名称")
    private String ename;

    /**
     * 订单编号
     */
    @Length(max = 50)
    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 申请开始时间
     */
    @ApiModelProperty("申请开始时间")
    private Date applyStartTime;

    /**
     * 申请结束时间
     */
    @ApiModelProperty("申请结束时间")
    private Date applyEndTime;

    /**
     * 审核状态：1-待审核 2-已审核 3-已驳回
     */
    @ApiModelProperty("审核状态：1-待审核 2-已审核 3-已驳回")
    private Integer authStatus;

}
