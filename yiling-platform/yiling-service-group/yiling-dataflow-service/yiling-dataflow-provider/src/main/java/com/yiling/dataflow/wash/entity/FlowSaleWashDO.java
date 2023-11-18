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
 * 流向销售清洗表
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("flow_sale_wash")
public class FlowSaleWashDO extends BaseFlowDataWashDO {

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
     * 商业名称（商家名称）
     */
    private String name;

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
     * 行业库机构编码
     */
    private Long crmOrganizationId;

    /**
     * 行业库机构名称
     */
    private String crmOrganizationName;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 月份
     */
    private String soMonth;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

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
     * 换算数量
     */
    private BigDecimal conversionQuantity;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
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
     * 经销商三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 机构三者关系ID
     */
    private Long organizationCersId;

    /**
     * 清洗结果：1-未清洗 2-正常 3-疑似重复 4-区间外删除
     */
    private Integer washStatus;

    /**
     * 产品是否对照 0-否 1-是
     */
    private Integer goodsMappingStatus;

    /**
     * 客户是否对照 0-否 1-是
     */
    private Integer organizationMappingStatus;

    /**
     * 是否异常 0-否 1-是
     */
    private Integer errorFlag;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 流向分类 1-正常 2-销量申诉 3-窜货申报 5-月流向上传 6-院外药店生成
     */
    private Integer flowClassify;

    /**
     * 业务代表工号
     */
    private String representativeCode;

    /**
     * 是否非锁流向 0-锁定流向 1-非锁流向
     */
    private Integer unlockFlag;

    /**
     * 数据生成方式 0-正常 1-院外药店生成
     */
    private Integer createType;

    /**
     * 自动生成数据的源流向id
     */
    private Long sourceId;

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
        return this.soTime;
    }

    @Override
    public String getSpecifications() {
        return this.soSpecifications;
    }

    @Override
    public String getManufacturer() {
        return this.soManufacturer;
    }

    @Override
    public String getUnit() {
        return this.soUnit;
    }
}
