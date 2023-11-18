package com.yiling.goods.medicine.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/26
 */
@Data
public class GoodsListItemBO implements java.io.Serializable {

    private static final long serialVersionUID = -7631547950698054406L;

    /**
     * ID
     */
    private Long id;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long  sellSpecificationsId;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产厂家地址
     */
    private String manufacturerAddress;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 供应商id
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    /**
     * 销售单位
     */
    private String sellUnit;

    /**
     * 自己销售规格
     */
    private String specifications;

    /**
     * 自己销售单位
     */
    private String unit;

    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    private Integer auditStatus;

    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    private Integer goodsStatus;

    /**
     * 下架原因：1平台下架 2质管下架 3供应商下架
     */
    private Integer outReason;

    /**
     * 挂网价
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Long qty;

    /**
     * 冻结数量
     */
    private Long frozenQty;

    /**
     * 商品图片值
     */
    private String pic;

    /**
     * 一级分类名称
     */
    private String standardCategoryName1;

    /**
     * 二级分类名称
     */
    private String standardCategoryName2;

    /**
     * 中包装
     */
    private Integer middlePackage;

    /**
     * 大包装
     */
    private Integer bigPackage;

    /**
     * 是否拆包销售：1可拆0不可拆
     */
    private Integer canSplit;

    /**
     * 专销
     */
    private Integer isPatent;

    /**
     * 产品线描述
     */
    private String goodsLineDesc;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 是否超卖商品 0-非超卖 1-超卖
     */
    private Integer overSoldType;

    /**
     * 剂型名称
     */
    private String gdfName;
}
