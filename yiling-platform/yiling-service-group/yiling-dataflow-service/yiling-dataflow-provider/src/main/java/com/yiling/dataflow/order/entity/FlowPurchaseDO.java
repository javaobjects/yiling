package com.yiling.dataflow.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购流向表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_purchase")
public class FlowPurchaseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * crm企业ID
     */
    private Long crmEnterpriseId;

    /**
     * 经销商级别
     */
    private Integer supplierLevel;

    private String crmCode;

    /**
     * op库主键
     */
    private String poId;

    /**
     * Erp采购订单号
     */
    private String poNo;

    /**
     * 供应商编码（供应商内码）
     */
    private String enterpriseInnerCode;

    /**
     * 供应商名称
     */
    private String enterpriseName;

    /**
     * 供应商Id
     */
    private Long supplierId;

    /**
     * 购进时间
     */
    private Date poTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 购进月份
     */
    private String poMonth;


    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String poSpecifications;

    /**
     * 商品规格id
     */
    private Long specificationId;

    private Long crmGoodsCode;

    /**
     * 批次号
     */
    private String poBatchNo;

    /**
     * 生产时间
     */
    private Date poProductTime;

    /**
     * 有效期时间
     */
    private Date poEffectiveTime;

    /**
     * 采购数量
     */
    private BigDecimal poQuantity;

    /**
     * 商品单位
     */
    private String poUnit;

    /**
     * 采购价格
     */
    private BigDecimal poPrice;

    /**
     * 金额
     */
    private BigDecimal poTotalAmount;

    /**
     * 商品生产厂家
     */
    private String poManufacturer;

    /**
     * 商品批准文号
     */
    private String poLicense;

    /**
     * 订单来源，1-大运河采购 2-非大运河采购
     */
    private String poSource;

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

    /**shi d
     * 所属区域名称
     */
    private String regionName;

    /**
     * 可能错误信息
     */
    private Integer goodsPossibleError;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;


    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
