package com.yiling.user.enterprise.bo;

import lombok.Data;

/**
 *  渠道商采购关系 BO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7
 */
@Data
public class PurchaseRelationBO {

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 采购渠道商ID
     */
    private Long buyerChannelId;

    /**
     * 销售渠道商ID
     */
    private Long sellerChannelId;

    /**
     * 是否已选择
     */
    private Boolean chooseFlag;
}
