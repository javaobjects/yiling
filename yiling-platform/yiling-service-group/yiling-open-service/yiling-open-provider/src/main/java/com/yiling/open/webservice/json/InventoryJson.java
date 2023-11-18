package com.yiling.open.webservice.json;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@Data
public class InventoryJson {
    /**
     * 前端库存
     */
    private String curstoreQty;

    /**
     * 货位
     */
    private String locationName;

    /**
     * 货位
     */
    private String locationNumber;

    /**
     * 组织编码
     */
    private String storNumber;

    /**
     * 库房
     */
    private String wareHouseName;

    /**
     * 库房
     */
    private String wareHouseNumber;

    /**
     * 商品编码
     */
    private String matNumber;

    /**
     * 主键
     */
    private String id;

    /**
     * 批次
     */
    private String lot;

    /**
     * 锁定库存
     */
    private String lockqty;

    /**
     * 预留库存
     */
    private String reservationbaseQty;
}
