package com.yiling.goods.medicine.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceLimitDTO extends BaseDTO {

    private static final long serialVersionUID = -7631547950698054406L;

    private Integer customerType;

    /**
     * 省
     */
    private String provinceCode;

    /**
     * 市
     */
    private String cityCode;

    /**
     * 区
     */
    private String regionCode;


    private BigDecimal price;

    private Long goodsId;

    private Long updateUser;

    private Date updateTime;
}
