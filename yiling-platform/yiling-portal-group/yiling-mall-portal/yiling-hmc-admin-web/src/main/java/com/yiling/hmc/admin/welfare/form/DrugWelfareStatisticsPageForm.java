package com.yiling.hmc.admin.welfare.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/09/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareStatisticsPageForm extends QueryPageListForm {

//    @ApiModelProperty("用户id")
//    private Long userId;

    @ApiModelProperty("福利计划id")
    private Long drugWelfareId;

//    @ApiModelProperty("商家id")
//    private Long eid;

    @ApiModelProperty("销售人员id")
    private Long sellerUserId;

    @ApiModelProperty("服药人姓名")
    private String medicineUserName;

    @ApiModelProperty("服药人手机号")
    private String medicineUserPhone;

    @ApiModelProperty("用户入组id")
    private Long groupId;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

}
