package com.yiling.b2b.app.shop.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺楼层基础信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopFloorBasicVO extends BaseVO {

    /**
     * 楼层名称
     */
    private String name;

}
