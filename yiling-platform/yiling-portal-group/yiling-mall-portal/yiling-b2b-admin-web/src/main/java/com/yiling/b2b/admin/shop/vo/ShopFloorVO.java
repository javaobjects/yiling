package com.yiling.b2b.admin.shop.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺楼层 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopFloorVO extends BaseVO {

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 楼层名称
     */
    private String name;

    /**
     * 权重值
     */
    private Integer sort;

    /**
     * 执行状态：1-启用 2-停用
     */
    private Integer status;

}
