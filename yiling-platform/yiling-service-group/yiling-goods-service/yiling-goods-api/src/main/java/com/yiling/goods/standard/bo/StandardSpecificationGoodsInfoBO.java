package com.yiling.goods.standard.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 StandardSpecificationGoodsInfoBO
 * @描述
 * @创建时间 2022/8/26
 * @修改人 shichen
 * @修改时间 2022/8/26
 **/
@Data
public class StandardSpecificationGoodsInfoBO implements Serializable {
    /**
     * 标准库规格id
     */
    private Long id;

    /**
     * 标准库id
     */
    private Long standardId;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7 医疗器械
     */
    private Integer goodsType;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;


    /**
     * 是否以岭品标识 以岭标识 0:非以岭  1：以岭
     */
    private Integer ylFlag;
}
