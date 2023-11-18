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
public class SaveActivityDocPatientForm extends BaseForm {

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
     * 运营备注
     */
    @ApiModelProperty("运营备注")
    private String activityRemark;

    /**
     * 活动头图
     */
    @ApiModelProperty("活动头图")
    private String activityHeadPic;

    /**
     * 活动描述
     */
    @ApiModelProperty("活动描述")
    private String activityDesc;

    /**
     * 限制医生类型：1-手动配置
     */
    @ApiModelProperty("限制医生类型：1-手动配置")
    private Integer restrictDocType;

    /**
     * 限制用户类型：1-平台新用户（没有注册过的），2-不限制
     */
    @ApiModelProperty("限制用户类型：1-平台新用户（没有注册过的），2-不限制")
    private Integer restrictUserType;

    /**
     * 审核用户类型：1-需要审核，2-无需审核
     */
    @ApiModelProperty("审核用户类型：1-需要审核，2-无需审核")
    private Integer auditUserType;
}
