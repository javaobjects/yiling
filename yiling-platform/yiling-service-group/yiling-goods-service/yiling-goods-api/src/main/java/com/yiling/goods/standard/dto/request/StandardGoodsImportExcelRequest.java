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
public class StandardGoodsImportExcelRequest extends BaseRequest {

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
     * 一级分类名称
     */
    private String standardCategoryName1;

    /**
     * 二级分类名称
     */
    private String standardCategoryName2;

    /**
     * 剂型
     */
    private String gdfName;

    /**
     * 剂型规格
     */
    private String gdfSpecifications;

    /**
     * 处方类型
     */
    private Integer otcType;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 是否医保 是_1 非_2
     */
    private Integer isYb;

    /**
     * 管制类型
     */
    private Integer controlType;

    /**
     * 药品本位码
     */
    private String goodsCode;

    /**
     * 复方制剂具体成分
     */
    private String ingredient;

    /**
     * 特殊成分
     */
    private Integer specialComposition;

    /**
     * 质量标准类别
     */
    private Integer qualityType;

    /**
     * 成分
     */
    private String drugDetails;

    /**
     * 性状
     */
    private String drugProperties;

    /**
     * 适应症
     */
    private String indications;

    /**
     * 用法用量
     */
    private String usageDosage;

    /**
     * 不良反应
     */
    private String adverseEvents;

    /**
     * 禁忌
     */
    private String contraindication;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 药物相互作用
     */
    private String interreaction;

    /**
     * 储藏
     */
    private String storageConditions;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 保质期
     */
    private String shelfLife;

    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 规格"
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
