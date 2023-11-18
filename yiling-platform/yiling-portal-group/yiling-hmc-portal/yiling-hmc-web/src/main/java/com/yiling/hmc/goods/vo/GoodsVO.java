package com.yiling.hmc.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsVO extends BaseVO {

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    /**
     * 挂网价
     */
    private BigDecimal price;

    /**
     * 适用症
     */
    private String indications;

    /**
     * 轮播图
     */
    private List<String> picBasicsInfoList;
    /**
     * 商品详情图
     */
    private String pic;


}
