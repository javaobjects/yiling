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
public class StandardFoodsImportExcelRequest extends BaseRequest {

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer isCn;

    /**
     * 是否国产：  1 国产 2进口 3 出口
     */
    private Integer goodsType;

    /**
     * 商品名称
     */
    private String  name;

    /**
     * 生产许可证号
     */
    private String  licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 一级分类名称
     */
    private String standardCategoryName1;

    /**
     * 二级分类名称
     */
    private String standardCategoryName2;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 产品类别
     */
    private String productClassification;

    /**
     * 产品标准号
     */
    private String productStandardCode;

    /**
     * 配料
     */
    private String ingredients;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 贮存条件
     */
    private String store;

    /**
     * 致敏源信息
     */
    private String allergens;

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
