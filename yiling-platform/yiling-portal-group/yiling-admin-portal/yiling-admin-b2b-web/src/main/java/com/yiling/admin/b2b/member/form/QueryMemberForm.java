package com.yiling.admin.b2b.member.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryMemberForm extends QueryPageListForm {

    /**
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 会员名称
     */
    @Length(max = 50)
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 创建开始时间
     */
    @ApiModelProperty("创建开始时间")
    private Date startCreateTime;

    /**
     * 创建结束时间
     */
    @ApiModelProperty("创建结束时间")
    private Date endCreateTime;



}
