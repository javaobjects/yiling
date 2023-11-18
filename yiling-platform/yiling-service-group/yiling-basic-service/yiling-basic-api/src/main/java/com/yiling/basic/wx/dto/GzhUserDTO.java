package com.yiling.basic.wx.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 公众号用户信息DTO
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GzhUserDTO extends BaseDTO {

    /**
     * 公众号appId
     */
    private String appId;

    /**
     * 公众号openId
     */
    private String gzhOpenId;

    /**
     * unionId
     */
    private String unionId;

    /**
     * 关注来源 1-自然流量，2-店员或者员工，3-药盒二维码
     */
    private Integer subscribeSource;

    /**
     * 订阅状态 1-订阅，2-取消订阅
     */
    private Integer subscribeStatus;

    private Date updateTime;


}
