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
public class StandardDisinfectionImportExcelRequest extends BaseRequest {

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
     * 剂型
     */
    private String gdfName;

    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 成分
     */
    private String drugDetails;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 使用方法
     */
    private String usageDosage;

    /**
     * 灭菌类别
     */
    private String sterilizationCategory;

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
