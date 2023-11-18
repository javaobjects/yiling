package com.yiling.hmc.goods.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author shichen
 * @类名 HmcGoodsBO
 * @描述
 * @创建时间 2022/3/31
 * @修改人 shichen
 * @修改时间 2022/3/31
 **/
@Data
public class HmcGoodsBO implements Serializable {
    private Long id;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 保险药品id
     */
    private Long goodsId;

    /**
     * 保险药品名称
     */
    private String goodsName;

    /**
     * 注册证号（批准文号）
     */
    private String licenseNo;

    /**
     * 标准库规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 标准库分类
     */
    private String standardCategoryName;

    /**
     * 标准库销售规格
     */
    private String sellSpecifications;

    /**
     * 销售规格
     */
    private String specifications;
    /**
     * 标准库销售单位
     */
    private String sellUnit;
    /**
     * 规格单位
     */
    private String unit;

    /**
     * 商家售卖金额/盒
     */
    private BigDecimal sellerPrice;

    /**
     * 给终端结算额/盒
     */
    private BigDecimal settlePrice;

    /**
     * 商品状态 1上架，2下架
     */
    private Integer goodsStatus;

    private Date createTime;

    private Date updateTime;

    /**
     * IH C端平台药房id
     */
    private Long ihCPlatformId;

    /**
     * IH 配送id
     */
    private Long ihEid;

    /**
     * IH 配送商商品ID
     */
    private Long ihPharmacyGoodsId;

}
