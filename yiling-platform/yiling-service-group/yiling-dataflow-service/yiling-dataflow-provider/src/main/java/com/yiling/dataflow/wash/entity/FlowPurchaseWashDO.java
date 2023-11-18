package com.yiling.dataflow.wash.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.dataflow.wash.bo.BaseFlowDataWashDO;
import com.yiling.dataflow.wash.bo.FlowDataWashEntity;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购流向清洗表
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("flow_purchase_wash")
public class FlowPurchaseWashDO extends BaseFlowDataWashDO {

    private static final long serialVersionUID = 1L;

    /**
     * 行业库经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 月流向清洗任务id
     */
    private Long fmwtId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 业务主键
     */
    private String flowKey;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String name;

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
     * 供应商行业库机构编码
     */
    private Long crmOrganizationId;

    /**
     * 供应商行业库机构名称
     */
    private String crmOrganizationName;

    /**
     * 购进时间
     */
    private Date poTime;

    /**
     * 购进月份
     */
    private String poMonth;

    /**
     * 下单日期
     */
    private Date orderTime;

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
     * 换算数量
     */
    private BigDecimal conversionQuantity;

    /**
     * 商品生产厂家
     */
    private String poManufacturer;

    /**
     * 商品批准文号
     */
    private String poLicense;

    /**
     * 订单来源，字典：erp_purchase_flow_source，1-大运河采购 2-非大运河采购
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

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    private String crmGoodsSpecifications;

    /**
     * 清洗结果：1-未清洗 2-正常 3-疑似重复 4-区间外删除
     */
    private Integer washStatus;

    /**
     * 产品是否对照 0-否 1-是
     */
    private Integer goodsMappingStatus;

    /**
     * 供应商是否对照 0-否 1-是
     */
    private Integer organizationMappingStatus;

    /**
     * 经销商三者关系id
     */
    private Long enterpriseCersId;

    /**
     * 是否异常 0-否 1-是
     */
    private Integer errorFlag;

    /**
     * 异常信息
     */
    private String errorMsg;

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

    @Override
    public Date getTime() {
        return this.poTime;
    }

    @Override
    public String getSpecifications() {
        return this.poSpecifications;
    }

    @Override
    public String getManufacturer() {
        return this.poManufacturer;
    }

    @Override
    public String getUnit() {
        return this.poUnit;
    }
}
