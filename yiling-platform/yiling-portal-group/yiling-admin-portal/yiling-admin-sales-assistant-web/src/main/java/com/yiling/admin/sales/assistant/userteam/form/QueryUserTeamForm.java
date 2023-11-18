package com.yiling.admin.sales.assistant.userteam.form;


import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-查询团队 Form
 * 
 * @author lun.yu
 * @date 2021/9/29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserTeamForm extends QueryPageListForm {

    /**
     * 姓名
     */
    @Length(max = 30)
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 注册开始时间
     */
    @ApiModelProperty("注册开始时间")
    private Date registerStartTime;

    /**
     * 注册结束时间
     */
    @ApiModelProperty("注册结束时间")
    private Date registerEndTime;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String mobilePhone;

}
