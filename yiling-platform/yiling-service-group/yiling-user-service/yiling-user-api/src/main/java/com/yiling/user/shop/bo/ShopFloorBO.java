package com.yiling.user.shop.bo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺楼层列表项 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ShopFloorBO extends BaseDTO {

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 包含商品数
     */
    private Integer goodsNum;

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

    /**
     * 创建时间
     */
    private Date createTime;




}
