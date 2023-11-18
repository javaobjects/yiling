package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardMaterialsImportExcelRequest extends BaseRequest {

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer isCn;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    private Integer goodsType;

    /**
     * 商品名称
     */
    private String  name;

    /**
     * 生成地址
     */
    private String manufacturerAddress;

    /**
     * 一级分类
     */
    private String standardCategoryName1;

    /**
     * 二级分类
     */
    private String standardCategoryName2;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 来源
     */
    private String goodsSource;

    /**
     * 性状
     */
    private String drugProperties;

    /**
     * 性味
     */
    private String propertyFlavor;

    /**
     * 功效
     */
    private String effect;

    /**
     * 用法用量
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;

    /**
     * 规格
     */
    private String sellSpecifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * YPID编码
     */
    private String ypidCode;

}
