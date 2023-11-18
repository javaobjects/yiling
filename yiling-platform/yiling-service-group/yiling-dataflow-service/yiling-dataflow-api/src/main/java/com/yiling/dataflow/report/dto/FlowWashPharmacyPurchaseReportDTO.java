package com.yiling.dataflow.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashPharmacyPurchaseReportDTO extends BaseDTO {

    /**
     * 机构三者关系Id
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
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 流向内购进数量
     */
    private BigDecimal innerPurchaseQty;

    /**
     * 流向外购进窜货批复
     */
    private BigDecimal fleeingPurchaseQty;

    /**
     * 流向外购进窜货扣减
     */
    private BigDecimal fleeingPurchaseReduceQty;

    /**
     * 流向外购进反流向
     */
    private BigDecimal purchaseReverse;

    /**
     * 流向外购进合计
     */
    private BigDecimal outterPurchaseSum;

    /**
     * 购进数量合计
     */
    private BigDecimal purchaseSum;

    /**
     * 购进总金额
     */
    private BigDecimal purchaseSumMoney;

    /**
     * 产品价格
     */
    private BigDecimal price;

    /**
     * 机构省份
     */
    private String provinceName;

    /**
     * 机构城市
     */
    private String cityName;

    /**
     * 机构区县
     */
    private String regionName;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 机构业务省区
     */
    private String businessProvince;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门
     */
    private String businessDepartment;

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
     * 药店属性 1-连锁分店；2-单体药店
     */
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    private Integer pharmacyType;

    /**
     * 客户上级公司名称
     */
    private String parentSupplierName;

    /**
     * 客户上级公司编码
     */
    private String parentSupplierCode;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    private Integer pharmacyLevel;

    /**
     * 连锁属性
     */
    private Integer pharmacyChainAttribute;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    private Integer labelAttribute;

    /**
     * 是否目标 1-是；2-否
     */
    private Integer targetFlag;

    /**
     * 首次锁定时间
     */
    private Date lockTime;

    /**
     * 最后解锁时间
     */
    private Date lastUnLockTime;

    /**
     * 省区经理工号
     */
    private String provinceManagerCode;

    /**
     * 省区经理编号
     */
    private String provinceManagerName;


}
