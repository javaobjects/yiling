package com.yiling.admin.b2b.strategy.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityPageForm extends QueryPageListForm {

    @ApiModelProperty("活动名称")
    private String strategyActivityName;
    
    @ApiModelProperty("状态：0-全部 1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty("企业id")
    private Long eid;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建人手机号")
    private String createTel;

    @ApiModelProperty("创建开始时间")
    private Date startTime;

    @ApiModelProperty("创建截止时间")
    private Date stopTime;

    @ApiModelProperty("策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员")
    private Integer strategyType;

    @ApiModelProperty("运营备注")
    private String operatingRemark;
}
