package com.yiling.admin.b2b.integral.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分发放/扣减记录 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralRecordForm extends QueryPageListForm {

    /**
     * 类型：1-发放 2-扣减
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "类型：1-发放 2-扣减", required = true)
    private Integer type;

    /**
     * 发放/扣减开始时间
     */
    @ApiModelProperty("发放/扣减开始时间")
    private Date startOperTime;

    /**
     * 发放/扣减结束时间
     */
    @ApiModelProperty("发放/扣减结束时间")
    private Date endOperTime;

    /**
     * 规则名称
     */
    @Length(max = 100)
    @ApiModelProperty("规则名称")
    private String ruleName;

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String behaviorName;

    /**
     * 创建人手机号
     */
    @Length(max = 20)
    @ApiModelProperty("创建人手机号")
    private String mobile;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long uid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 操作备注
     */
    @ApiModelProperty("操作备注")
    private String opRemark;

}
