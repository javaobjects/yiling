package com.yiling.admin.b2b.integralmessage.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换消息配置分页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralMessagePageForm extends QueryPageListForm {

    /**
     * 标题
     */
    @Length(max = 10)
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 状态：1-启用 2-禁用
     */
    @ApiModelProperty("状态：1-启用 2-禁用")
    private Integer status;

    /**
     * 开始投放开始时间
     */
    @ApiModelProperty(value = "投放开始时间开始")
    private Date startTimeStart;

    /**
     * 结束投放开始时间
     */
    @ApiModelProperty(value = "投放开始时间结束")
    private Date startTimeEnd;

    /**
     * 创建时间开始
     */
    @ApiModelProperty(value = "创建时间开始")
    private Date startCreateTime;

    /**
     * 创建时间结束
     */
    @ApiModelProperty(value = "创建时间结束")
    private Date EndCreateTime;


}
