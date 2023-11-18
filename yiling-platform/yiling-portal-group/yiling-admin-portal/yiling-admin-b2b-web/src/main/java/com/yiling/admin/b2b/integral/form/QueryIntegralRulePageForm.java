package com.yiling.admin.b2b.integral.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分规则分页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralRulePageForm extends QueryPageListForm {

    /**
     * 规则名称
     */
    @Length(max = 100)
    @ApiModelProperty("规则名称")
    private String name;

    /**
     * 执行状态：1-启用 2-停用
     */
    @ApiModelProperty("执行状态：1-启用 2-停用")
    private Integer status;

    /**
     * 执行进度：1-未开始 2-进行中 3-已结束
     */
    @ApiModelProperty("执行进度：1-未开始 2-进行中 3-已结束")
    private Integer progress;

    /**
     * 规则生效时间
     */
    @ApiModelProperty("规则生效时间")
    private Date startTime;

    /**
     * 规则失效时间
     */
    @ApiModelProperty("规则失效时间")
    private Date endTime;

    /**
     * 创建人名称
     */
    @Length(max = 50)
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建人手机号
     */
    @Length(max = 20)
    @ApiModelProperty("创建人手机号")
    private String mobile;

}
