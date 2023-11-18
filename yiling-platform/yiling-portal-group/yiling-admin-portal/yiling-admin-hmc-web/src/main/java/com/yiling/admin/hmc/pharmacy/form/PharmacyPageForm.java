package com.yiling.admin.hmc.pharmacy.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: 终端药店
 * @date: 2022/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PharmacyPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "终端商家id")
    private Long eid;

    @ApiModelProperty("状态：1-启用，2-停用")
    private Integer status;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
