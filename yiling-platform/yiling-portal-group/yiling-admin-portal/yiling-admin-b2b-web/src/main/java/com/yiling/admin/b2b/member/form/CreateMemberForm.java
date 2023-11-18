package com.yiling.admin.b2b.member.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-创建会员 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CreateMemberForm extends BaseForm {

    /**
     * 会员名称
     */
    @NotNull
    @Length(max = 10)
    @ApiModelProperty(value = "会员名称", required = true)
    private String name;

    /**
     * 会员描述
     */
    @Length(max = 30)
    @ApiModelProperty("会员描述")
    private String description;

    /**
     * 背景图
     */
    @NotEmpty
    @ApiModelProperty(value = "背景图", required = true)
    private String bgPicture;

    /**
     * 会员点亮图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员点亮图", required = true)
    private String lightPicture;

    /**
     * 会员熄灭图
     */
    @NotEmpty
    @ApiModelProperty(value = "会员熄灭图", required = true)
    private String extinguishPicture;

    /**
     * 排序
     */
    @Range(min = 0, max = 200)
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否续卡提醒：0-否 1-是
     */
    @NotNull
    @Range(min = 0,max = 1)
    @ApiModelProperty(value = "是否续卡提醒：0-否 1-是", required = true)
    private Integer renewalWarn;

    /**
     * 到期前提醒天数
     */
    @Min(0)
    @ApiModelProperty("到期前提醒天数")
    private Integer warnDays;

    /**
     * 获得条件集合
     */
    @NotEmpty
    @ApiModelProperty(value = "获得条件集合", required = true)
    private List<@Valid CreateMemberBuyStageForm> memberBuyStageList;

    /**
     * 会员权益集合
     */
    @NotEmpty
    @ApiModelProperty(value = "会员权益集合", required = true)
    private List<@Valid SaveMemberEquityRelationForm> memberEquityList;


}
