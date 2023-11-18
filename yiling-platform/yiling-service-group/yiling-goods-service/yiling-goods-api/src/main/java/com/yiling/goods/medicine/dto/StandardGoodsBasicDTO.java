package com.yiling.goods.medicine.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsBasicDTO extends BaseDTO {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7医疗器械
     */
    private Integer goodsType;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 注册证号（批准文号）
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAdress;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 销售规格
     */
    private String sellSpecifications;

    /**
     * 销售单位
     */
    private String sellUnit;

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
     * 规格单位
     */
    private String unit;

    /**
     * 是否拆包销售：1可拆0不可拆
     */
    private Integer canSplit;

    /**
     * 中包装
     */
    private Integer middlePackage;

    /**
     * 大包装
     */
    private Integer bigPackage;

    /**
     * 是否医保：1是 2非 3未采集到相关信息
     */
    private Integer isYb;

    /**
     * 管制类型：0非管制1管制
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
     * 特殊成分： 0不含麻黄碱 1含麻黄碱
     */
    private Integer specialComposition;

    /**
     * 质量标准类别：1中国药典2地方标准3其他
     */
    private Integer qualityType;

    /**
     * 来源
     */
    private String goodsSource;

    /**
     * 毛重
     */
    private String roughWeight;

    /**
     * 等级
     */
    private String goodsGrade;

    /**
     * 执行标准
     */
    private String executiveStandard;

    /**
     * 产品类型
     */
    private String productClassification;

    /**
     * 产品标准号
     */
    private String productStandardCode;

    /**
     * 数据来源：1-平台录入2-平台导入3-erp对接
     */
    private Integer source;

    /**
     * 商品封图片
     */
    private String pic;

    private Integer IsPatent;

    /**
     * 超卖类型 0-正常商品 1-超卖商品
     */
    private Integer overSoldType;

    /**
     * 生产日期
     */
    private Date manufacturingDate;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 标准库商品信息
     */
    private StandardGoodsBasicInfoDTO standardGoods;
}
