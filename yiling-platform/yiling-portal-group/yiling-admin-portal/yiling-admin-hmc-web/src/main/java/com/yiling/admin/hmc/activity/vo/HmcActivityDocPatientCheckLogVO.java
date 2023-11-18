package com.yiling.admin.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 医带患活动患者审核日志
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocPatientCheckLogVO extends BaseVO {

    @ApiModelProperty("提审时间")
    private Date arraignmentTime;

    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ApiModelProperty("创建用户")
    private Integer createUser;

    @ApiModelProperty("创建用户名称")
    private String createUserName;

    @ApiModelProperty("审核结果 1-通过 2-驳回")
    private Integer checkResult;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("处方图片集合")
    private List<String> picture;


}
