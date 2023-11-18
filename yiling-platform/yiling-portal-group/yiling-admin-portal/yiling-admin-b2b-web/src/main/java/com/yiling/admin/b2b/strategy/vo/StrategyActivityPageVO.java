package com.yiling.admin.b2b.strategy.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
public class StrategyActivityPageVO extends BaseVO {

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员")
    private Integer strategyType;

    @ApiModelProperty("生效开始时间")
    private Date beginTime;

    @ApiModelProperty("生效结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建人手机号")
    private String createUserTel;

    @ApiModelProperty("状态：1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("参与次数")
    private Integer times;

    @ApiModelProperty("查看标识（true-可；false-不可")
    private Boolean lookFlag;

    @ApiModelProperty("修改标识（true-可；false-不可")
    private Boolean updateFlag;

    @ApiModelProperty("复制标识（true-可；false-不可")
    private Boolean copyFlag;

    @ApiModelProperty("停用标识（true-可；false-不可")
    private Boolean stopFlag;
}
