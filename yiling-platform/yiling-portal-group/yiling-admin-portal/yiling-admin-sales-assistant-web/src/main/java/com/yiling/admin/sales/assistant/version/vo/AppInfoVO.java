package com.yiling.admin.sales.assistant.version.vo;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用信息
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppInfoVO extends BaseDTO {

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称(暂时只有销售助手和B2B移动端)
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * App类型：1-android 2-ios
     */
    private Integer appType;

    /**
     * 应用状态：1-上架中 2-已下架
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    //=========================================

    /**
     * 渠道号
     */
    private String channelCode;
}
