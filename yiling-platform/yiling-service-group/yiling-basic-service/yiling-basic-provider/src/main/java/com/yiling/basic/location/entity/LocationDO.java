package com.yiling.basic.location.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行政区划字典表
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("location")
public class LocationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 上级区域编码
     */
    private String parentCode;

    /**
     * 排序优先级
     */
    private Integer priority;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;


}
