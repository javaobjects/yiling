package com.yiling.admin.b2b.member.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-权益列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryMemberEquityForm extends QueryPageListForm {

    /**
     * 权益类型：1-系统生成 2-自定义
     */
    @Range(min = 0,max = 2)
    @ApiModelProperty("权益类型：1-系统生成 2-自定义")
    private Integer type;

    /**
     * 权益名称
     */
    @Length(max = 50)
    @ApiModelProperty("权益名称")
    private String name;

    /**
     * 权益状态：0-关闭 1-开启
     */
    @ApiModelProperty("权益状态：0-关闭 1-开启")
    private Integer status;

}
