package com.yiling.goods.restriction.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionDTO
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPurchaseRestrictionDTO extends BaseDTO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 每单限购数量 0为无限制
     */
    private Long orderRestrictionQuantity;

    /**
     * 时间内限购数量 0为无限制
     */
    private Long timeRestrictionQuantity;

    /**
     * 限购时间类型 1自定义 2 每天 3 每周 4每月
     */
    private Integer timeType;

    /**
     * 限购开始时间
     */
    private Date startTime;

    /**
     * 限购结束时间
     */
    private Date endTime;

    /**
     * 客户设置类型 0：全部客户  1:部分客户
     */
    private Integer customerSettingType;

    /**
     * 0 正常状态 1 关闭状态
     */
    private Integer status;
}
