package com.yiling.admin.b2b.promotion.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2021/05/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialActivityAppointmentPageVO extends BaseVO {


    @ApiModelProperty(value = "专场活动表id")
    private Long specialActivityEnterpriseId;

    @ApiModelProperty(value = "专场活动企业名称")
    private String specialActivityEnterpriseName;

    @ApiModelProperty(value = "预约时间")
    private Date appointmentTime;

    @ApiModelProperty(value = "预约人id")
    private Long appointmentUserId;

    @ApiModelProperty(value = "预约人姓名")
    private String appointmentUserName;

    @ApiModelProperty(value = "预约人联系方式")
    private String mobile;

    @ApiModelProperty(value = "预约人所在企业")
    private String appointmentUserEnterpriseName;

    @ApiModelProperty(value = "预约人所属企业id")
    private Long appointmentUserEid;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "专场活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "专场活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "活动类型（1-满赠,2-特价,3-秒杀,4-组合包）")
    private Integer type;

    @ApiModelProperty(value = "活动状态（1-启用；2-停用；3，未开始，4进行中，5已结束 根据时间判断）")
    private Integer status;

    @ApiModelProperty("活动进程（1未开始，2进行中，3已结束）")
    private Integer process;
}
