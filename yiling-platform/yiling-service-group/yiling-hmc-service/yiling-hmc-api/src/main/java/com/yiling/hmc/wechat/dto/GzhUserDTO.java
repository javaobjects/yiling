package com.yiling.hmc.wechat.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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


}
