package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * erp销售流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSaleDTO extends BaseDTO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 商品规格id
     */
    private Long specificationId;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 订单来源，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 销售时间
     */
    private String soMonth;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 有效期
     */
    private Date soEffectiveTime;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 价格
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

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

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 同步返利状态，0-未同步 1-已同步
     */
    private Integer reportSyncStatus;

    private String crmCode;

    private Long crmGoodsCode;

    private String enterpriseCrmCode;

    private String  customerTypeName;
    
    private String   belongDepartment;

    private Integer lockType;

    private String remark;

    /**
     * 以岭商品ID（冗余字段）
     */
    private Long ylGoodsId;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

}
