package com.yiling.hmc.admin.welfare.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: benben.jia
 * @data: 2022/09/28
 */
@Data
public class DrugWelfareStatisticsPageVO extends BaseVO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("福利计划id")
    private Long drugWelfareId;

    @ApiModelProperty("福利计划名称")
    private String drugWelfareName;

    @ApiModelProperty("商家销售人员id")
    private Long sellerUserId;

    @ApiModelProperty("商家销售人员")
    private String sellerUserName;

    @ApiModelProperty("服药人姓名")
    private String medicineUserName;

    @ApiModelProperty("服药人手机号")
    private String medicineUserPhone;

    @ApiModelProperty("使用进度")
    private String useSchedule;

    @ApiModelProperty("用户入组id")
    private Long joinGroupId;

    @ApiModelProperty("入组时间")
    private Date createTime;

}
