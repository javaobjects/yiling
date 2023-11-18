package com.yiling.admin.hmc.welfare.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/09/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareUpdateForm extends BaseForm {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String name;

    /**
     * 药品规格id
     */
    @ApiModelProperty("药品规格id")
    @NotNull(message = "药品规格id不能为空")
    private Long sellSpecificationsId;


    /**
     * 福利券包
     */
    @ApiModelProperty("福利券包")
    @NotNull(message = "福利券包不能为空")
    private List<DrugWelfareCouponForm> drugWelfareCouponList;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 活动状态：1-启用 2-停用
     */
    @ApiModelProperty("活动状态：1-启用 2-停用")
    private Integer status;

    /**
     * 备注
     */
    @Length(max = 255)
    @ApiModelProperty("备注")
    private String remark;

}
