package com.yiling.goods.ylprice.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/8/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsYilingPriceLogRequest extends BaseDTO {


    private static final long serialVersionUID = -1418207736106613621L;

    /**
     * 以岭品ID
     */
    private Long goodsId;

    /**
     * 标准库规格ID
     */
    private Long specificationId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * report_param表id
     */
    private Long paramId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecification;


    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
