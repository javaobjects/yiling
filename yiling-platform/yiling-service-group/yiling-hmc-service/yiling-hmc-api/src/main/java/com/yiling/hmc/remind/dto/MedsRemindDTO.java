package com.yiling.hmc.remind.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 用药提醒DTO
 * @author: fan.shen
 * @date: 2022/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MedsRemindDTO extends BaseDTO {

    private static final long serialVersionUID = 2280448824232686833L;


    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单次用量（例如：3、1）
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
     * 用药天数
     */
    private Integer useDaysType;

    /**
     * 提醒状态 1-有效，2-无效
     */
    private Integer remindStatus;
    
    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 提醒时间
     */
    private List<String> medsRemindTimeList;

    private Long createUser;

    /**
     * 创建人头像
     */
    private String creatorAvatarUrl;

    /**
     * 关注人头像列表
     */
    private List<String> subAvatarUrlList;

    /**
     * 显示编辑按钮
     */
    private Boolean showEditButton = false;

    /**
     * 是否订阅
     */
    private Boolean subFlag = false;

}