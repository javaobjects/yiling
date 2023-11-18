package com.yiling.mall.navigation.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NavigationInfoDTO extends BaseDTO {

    /**
     * 导航名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 链接
     */
    private String link;

    /**
     * 状态1-启用 2-停用
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;
}
