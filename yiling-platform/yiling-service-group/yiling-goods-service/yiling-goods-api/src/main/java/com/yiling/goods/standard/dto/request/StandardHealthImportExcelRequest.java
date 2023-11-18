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
public class StandardHealthImportExcelRequest extends BaseRequest {

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
     * 批准文号
     */
    private String  licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 一级分类
     */
    private String standardCategoryName1;

    /**
     * 二级分类
     */
    private String standardCategoryName2;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 批准日期
     */
    private String approvalDate;

    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 原料
     */
    private String rawMaterial;

    /**
     * 辅料
     */
    private String ingredients;

    /**
     * 适宜人群
     */
    private String suitablePeople;

    /**
     * 不适宜人群
     */
    private String unsuitablePeople;

    /**
     * 保健功能
     */
    private String healthcareFunction;

    /**
     * 食用量及食用方法
     */
    private String usageDosage;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 贮藏方法
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
