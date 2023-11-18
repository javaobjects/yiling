package com.yiling.dataflow.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateFlowSaleRequest extends BaseRequest {

    /**
     * 主键
     */
    private Long id;
    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * 经销商级别
     */
    private Integer supplierLevel;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 供应商编码（供应商内码）
     */
    private String enterpriseInnerCode;

    /**
     * 供应商名称
     */
    private String enterpriseName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 购进时间
     */
    private Date soTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 销售月份
     */
    private String soMonth;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 商品+规格id
     */
    private Long specificationId;

    /**
     * 批次号
     */
    private String soBatchNo;

    /**
     * 生产时间
     */
    private Date soProductTime;

    /**
     * 有效期时间
     */
    private Date soEffectiveTime;

    /**
     * 采购数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 采购价格
     */
    private BigDecimal soPrice;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 商品批准文号
     */
    private String soLicense;

    /**
     * 订单来源，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    private String crmCode;

    private String enterpriseCrmCode;

    private String  customerTypeName;

    private String   belongDepartment;

    private Integer lockType;

    private Long crmGoodsCode;

    private Integer goodsPossibleError;

    private String remark;

    private Integer dataTag;
}
