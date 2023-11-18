package com.yiling.hmc.remind.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 用药提醒任务
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/6/9
 */
@Data
@ToString
@ApiModel(value = "MedsRemindTaskDetailVO", description = "提醒详情")
@Slf4j
public class MedsRemindTaskDetailVO extends BaseVO {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 用药提醒表主键
     */
    @ApiModelProperty(value = "用药提醒表主键")
    private Long medsRemindId;

    /**
     * 确认状态 1-已确认，2-未确认
     */
    @ApiModelProperty(value = "确认状态 1-已确认，2-未确认")
    private Integer confirmStatus;

    /**
     * 初始发送时间
     */
    @ApiModelProperty(value = "初始发送时间")
    private Date initSendTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 单次用量（例如：3、1）
     */
    @ApiModelProperty(value = "单次用量（例如：3、1）")
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    @ApiModelProperty(value = "用法用量单位（例如：粒、袋）")
    private String useAmountUnit;

    /**
     * 用药次数
     */
    @ApiModelProperty(value = "用药次数")
    private Integer useTimesType;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatarUrl;

}
