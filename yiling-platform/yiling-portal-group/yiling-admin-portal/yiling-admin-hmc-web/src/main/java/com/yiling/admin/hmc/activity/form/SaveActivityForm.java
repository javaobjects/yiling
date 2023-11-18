package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 保存患教活动 Form
 *
 * @author: fan.shen
 * @date: 2022/9/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveActivityForm extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 活动描述
     */
    @ApiModelProperty("活动描述")
    private String activityDesc;

}
