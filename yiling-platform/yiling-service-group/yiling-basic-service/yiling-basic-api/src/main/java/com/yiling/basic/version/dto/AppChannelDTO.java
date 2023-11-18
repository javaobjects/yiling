package com.yiling.basic.version.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用渠道信息
 * </p>
 *
 * @author yong.zhang
 * @date 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppChannelDTO extends BaseDTO {

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 渠道号
     */
    private String code;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 渠道描述
     */
    private String description;

    /**
     * 是否默认渠道：0-否 1-是
     */
    private Integer defaultFlag;

    /**
     * 备注
     */
    private String remark;


}
