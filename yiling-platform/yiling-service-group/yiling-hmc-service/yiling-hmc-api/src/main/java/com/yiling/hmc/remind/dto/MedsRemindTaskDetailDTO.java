package com.yiling.hmc.remind.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 用药提醒任务详情DTO
 * @author: fan.shen
 * @date: 2022/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MedsRemindTaskDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * 用药提醒表主键
     */
    private Long medsRemindId;

    /**
     * 确认状态 1-已确认，2-未确认
     */
    private Integer confirmStatus;

    /**
     * 初始发送时间
     */
    private Date initSendTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单次用量（例如：3粒、1袋）
     */
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    private String useAmountUnit;

    /**
     * 用药次数
     */
    private Integer useTimesType;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 时间图标标签
     */
    private Integer timeIconFlag;

}