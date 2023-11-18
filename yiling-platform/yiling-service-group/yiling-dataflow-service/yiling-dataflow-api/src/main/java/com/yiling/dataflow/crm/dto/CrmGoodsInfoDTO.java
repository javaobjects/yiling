package com.yiling.dataflow.crm.dto;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * crm商品编码
     */
    private Long goodsCode;

    /**
     * 产品分类
     */
    private String goodsGroup;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String goodsSpec;

    /**
     * 缩写
     */
    private String goodsNameAbbr;

    /**
     * 小包装
     */
    private String packing;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 是否处方药
     */
    private Integer isPrescription;

    /**
     * 换算因子
     */
    private String conversionFactor;

    /**
     * 是否指标产品
     */
    private String isTarget;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 出厂价
     */
    private BigDecimal exFactoryPrice;

    /**
     * 采购价
     */
    private BigDecimal purchasePrice;

    /**
     * 销售价
     */
    private BigDecimal salesPrice;

    /**
     * 考核价
     */
    private BigDecimal assessmentPrice;

    /**
     * 品种类型
     */
    private String varietyType;

    /**
     * 是否团购 0：否 1：是
     */
    private Integer isGroupPurchase;

    /**
     * 品类id
     */
    private Long categoryId;

    /**
     * 0:有效，1：失效
     */
    private Integer status;
}
