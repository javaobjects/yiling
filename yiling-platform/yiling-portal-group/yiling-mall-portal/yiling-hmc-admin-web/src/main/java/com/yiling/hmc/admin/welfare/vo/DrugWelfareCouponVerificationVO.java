package com.yiling.hmc.admin.welfare.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/10/08
 */
@Data
public class DrugWelfareCouponVerificationVO extends BaseVO {


    @ApiModelProperty("状态：1券码输入错误 2已经核销 3活动不存在 4活动未开始 5活动已过期 6活动已结束 7核销成功")
    private Integer status;

    @ApiModelProperty("用药福利计划名称")
    private String drugWelfareName;

    @ApiModelProperty("入组姓名")
    private String medicineUserName;

    @ApiModelProperty("入组id")
    private Long joinGroupId;

}
