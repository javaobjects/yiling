package com.yiling.hmc.admin.employee.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 员工分页列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EmployeePageListItemVO extends BaseVO {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("工号")
    private String code;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("展示用药福利按钮flag 0-不展示，1-展示")
    private Integer showDrugWelfareButtonFlag = 0;


}
