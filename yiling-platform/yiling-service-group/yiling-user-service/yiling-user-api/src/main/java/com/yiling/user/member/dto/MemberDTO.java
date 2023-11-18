package com.yiling.user.member.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员描述
     */
    private String description;

    /**
     * 获取条件：1-付费 2-活动赠送
     */
    private Integer getType;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 有效时长
     */
    private Integer validTime;

    /**
     * 是否续卡提醒：0-否 1-是
     */
    private Integer renewalWarn;

    /**
     * 到期前提醒天数
     */
    private Integer warnDays;

    /**
     * 是否停止获取：0-否 1-是
     */
    private Integer stopGet;

    /**
     * 背景图
     */
    private String bgPicture;

    /**
     * 会员点亮图
     */
    private String lightPicture;

    /**
     * 会员熄灭图
     */
    private String extinguishPicture;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
