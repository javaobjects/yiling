package com.yiling.goods.medicine.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChoicenessGoodsPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 销售规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 客户eid
     */
    private Long customerEid;

    /**
     * 销售商家集合
     */
    private List<Long> eids;

    /**
     * 建采商家集合
     */
    private List<Long> purchaseEids;

    /**
     * 优质商家集合
     */
    private List<Long> highQualityEids;

    /**
     * 售卖时间开始
     */
    private Date saleTimStart;

    /**
     * 售卖时间结束
     */
    private Date saleTimEnd;

    /**
     * 排序-有库存商品id集合
     */
    private List<Long> inStockGoodsIds;

    /**
     * 排序-销售省份
     */
    private String provinceCode;

    /**
     * 价格排序 1 降序 desc ，2 升序 asc
     */
    private Integer sortPrice;

    /**
     * 销量排序
     */
    private Boolean sortSaleVolume;

    /**
     * 是否筛选建采商家
     */
    private Boolean filterPurchaseEnterprise;

    /**
     * 是否筛选有库存商品
     */
    private Boolean filterInStockGoods;

    /**
     * 是否筛选销售省份
     */
    private Boolean filterProvince;

    /**
     * 是否筛选在有效期内
     */
    private Boolean filterExpired;
}
