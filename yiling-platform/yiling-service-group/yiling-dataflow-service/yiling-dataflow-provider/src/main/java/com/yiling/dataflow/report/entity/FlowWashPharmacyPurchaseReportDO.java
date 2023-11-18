package com.yiling.dataflow.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 零售购进报表
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_wash_pharmacy_purchase_report")
public class FlowWashPharmacyPurchaseReportDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 机构省份代码
     */
    private String provinceCode;

    /**
     * 机构城市代码
     */
    private String cityCode;

    /**
     * 机构区域编码
     */
    private String regionCode;

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
     * 产品组Id
     */
    @TableField(exist = false)
    private Long productGroupId;


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
     * 上级公司编码
     */
    @TableField(exist = false)
    private String parentCompanyCode;

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


    /**
     * 是否代跑
     */
    @TableField(exist = false)
    private Integer substituteRunning;

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
     * 初始化字段防止为空
     */
    public void initModel() {

        this.setCrmId(0l);
        this.setName("");
        this.setGoodsCode(0l);
        this.setGoodsName("");
        this.setSpecifications("");
        this.setInnerPurchaseQty(BigDecimal.ZERO);
        this.setFleeingPurchaseQty(BigDecimal.ZERO);
        this.setFleeingPurchaseReduceQty(BigDecimal.ZERO);
        this.setPurchaseReverse(BigDecimal.ZERO);
        this.setOutterPurchaseSum(BigDecimal.ZERO);
        this.setPurchaseSum(BigDecimal.ZERO);
        this.setPurchaseSumMoney(BigDecimal.ZERO);
        this.setPrice(BigDecimal.ZERO);
        this.setProvinceName("");
        this.setCityName("");
        this.setRegionName("");
        this.setProvincialArea("");
        this.setBusinessProvince("");
        this.setDepartment("");
        this.setBusinessDepartment("");
        this.setDistrictCounty("");
        this.setSuperiorSupervisorCode("");
        this.setSuperiorSupervisorName("");
        this.setRepresentativeCode("");
        this.setRepresentativeName("");
        this.setPostCode(0l);
        this.setPostName("");
        this.setProductGroup("");
        this.setPharmacyAttribute(0);
        this.setPharmacyType(0);
        this.setParentSupplierName("");
        this.setParentSupplierCode("");
        this.setPharmacyLevel(0);
        this.setPharmacyChainAttribute(0);
        this.setParentCompanyCode("");
        this.setLabelAttribute(0);
        this.setTargetFlag(0);
        this.setCreateUser(0l);
        this.setCreateTime(new Date());
        this.setUpdateUser(0l);
        this.setUpdateTime(new Date());
        this.setProvinceCode("");
        this.setCityCode("");
        this.setRegionCode("");
        this.setProductGroupId(0l);
        this.setLockTime(DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
        this.setLastUnLockTime(DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
        this.setProvinceManagerCode("");
        this.setProvinceManagerName("");
        this.setSubstituteRunning(1);
    }

}
