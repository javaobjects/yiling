package com.yiling.dataflow.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashInventoryReportDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * ERP库存汇总同步数据主键Id
     */
    private Long flowGoodsBatchWashId;

    /**
     * 流向KeyId
     */
    private String flowKey;

    /**
     * 清洗任务Id
     */
    private Long fmwtId;

    /**
     * 经销商三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 年月
     */
    private String soMonth;

    /**
     * 机构编码
     */
    private Long crmId;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构省份
     */
    private String provinceName;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 机构城市
     */
    private String cityName;

    /**
     * 区县代码
     */
    private String regionCode;

    /**
     * 机构区县
     */
    private String regionName;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门
     */
    private String businessDepartment;

    /**
     * 机构省区
     */
    private String provincialArea;

    /**
     * 机构业务省区
     */
    private String businessProvince;

    /**
     * 机构区办代码
     */
    private String districtCountyCode;

    /**
     * 机构区办
     */
    private String districtCounty;

    /**
     * 主管工号
     */
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 标准产品规格
     */
    private String specifications;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 库存数量
     */
    private BigDecimal inventoryQuantity;

    /**
     * 金额
     */
    private BigDecimal totalAmount;

    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 客户普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    private Integer generalMedicineLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    private Integer supplierAttribute;

    /**
     * 是否连锁总部 1是 2否
     */
    private Integer headChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    private Integer chainAttribute;

    /**
     * (客户)连锁KA 1是 2否
     */
    private Integer chainKaFlag;

    /**
     * 客户上级公司名称
     */
    private String parentSupplierName;

    /**
     * 客户上级公司编码
     */
    private String parentSupplierCode;

    /**
     * 客户商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    private Integer baseSupplierInfo;


    /**
     * 首次锁定时间
     */
    private Date lockTime;


    /**
     * 最后解锁时间
     */
    private Date lastUnLockTime;


}
