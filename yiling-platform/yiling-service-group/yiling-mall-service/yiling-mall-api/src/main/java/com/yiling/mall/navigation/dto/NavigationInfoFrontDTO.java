package com.yiling.mall.navigation.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NavigationInfoFrontDTO extends BaseDTO {
    /**
     * 导航名称
     */
    private String name;

    /**
     * 链接
     */
    private String link;
}
