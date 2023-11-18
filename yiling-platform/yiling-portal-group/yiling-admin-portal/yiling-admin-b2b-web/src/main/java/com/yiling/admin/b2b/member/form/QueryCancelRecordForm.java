package com.yiling.admin.b2b.member.form;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员取消记录查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCancelRecordForm extends QueryPageListForm {

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
     * 会员ID集合
     */
    @ApiModelProperty("会员ID集合")
    private List<Long> memberIdList;

    /**
     * 推广方ID
     */
    @ApiModelProperty("推广方ID")
    private String promoterId;

    /**
     * 推广人ID
     */
    @ApiModelProperty("推广人ID")
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    @ApiModelProperty("推广人名称")
    private String promoterUserName;

    /**
     * 取消开始时间
     */
    @ApiModelProperty("取消开始时间")
    private Date cancelStartTime;

    /**
     * 取消结束时间
     */
    @ApiModelProperty("取消结束时间")
    private Date cancelEndTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String updateUserName;

}
