package com.yiling.dataflow.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医疗进销存报表
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_wash_hospital_stock_report")
public class FlowWashHospitalStockReportDO extends BaseDO {

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
     * 产品价格
     */
    private BigDecimal price;

    /**
     * 上个月库存数量
     */
    private BigDecimal lastMonthStockQty;

    /**
     * 上月库存金额（万元)
     */
    private BigDecimal lastMonthStockMoney;

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
     * 商销量（购进）数量
     */
    private BigDecimal saleQty;

    /**
     * 商销量（购进）金额（万元）
     */
    private BigDecimal saleMoney;

    /**
     * 纯销量（销售）数量
     */
    private BigDecimal pureSaleQty;

    /**
     * 纯销量(销售)金额(万元)
     */
    private BigDecimal pureSaleMoney;

    /**
     * 本月上报库存数量
     */
    private BigDecimal monthStockQty;

    /**
     * 本月上报库存金额（万元）
     */
    private BigDecimal monthStockMoney;

    /**
     * 期末库存数量
     */
    private BigDecimal endQty;

    /**
     * 期末库存金额(万元)
     */
    private BigDecimal endMoney;

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
     * 医院性质 1公立 2私立 3厂办
     */
    private Integer medicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    private Integer medicalType;

    /**
     * 以岭级别
     */
    private Integer ylLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    private Integer nationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    private Integer baseMedicineFlag;

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
     * 页面是否隐藏
     */
    private Integer hideFlag;

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
     * 初始化防止为空
     *
     * @return
     */
    public void initModel() {

        // 初始化防止为空
        this.setEnterpriseCersId(0l);
        this.setCrmId(0l);
        this.setName("");
        this.setGoodsCode(0l);
        this.setGoodsName("");
        this.setSpecifications("");
        this.setPrice(BigDecimal.ZERO);
        this.setLastMonthStockQty(BigDecimal.ZERO);
        this.setLastMonthStockMoney(BigDecimal.ZERO);
        this.setInnerPurchaseQty(BigDecimal.ZERO);
        this.setFleeingPurchaseQty(BigDecimal.ZERO);
        this.setFleeingPurchaseReduceQty(BigDecimal.ZERO);
        this.setPurchaseReverse(BigDecimal.ZERO);
        this.setOutterPurchaseSum(BigDecimal.ZERO);
        this.setSaleQty(BigDecimal.ZERO);
        this.setSaleMoney(BigDecimal.ZERO);
        this.setPureSaleQty(BigDecimal.ZERO);
        this.setPureSaleMoney(BigDecimal.ZERO);
        this.setMonthStockQty(BigDecimal.ZERO);
        this.setMonthStockMoney(BigDecimal.ZERO);
        this.setEndQty(BigDecimal.ZERO);
        this.setEndMoney(BigDecimal.ZERO);
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
        this.setMedicalNature(0);
        this.setMedicalType(0);
        this.setYlLevel(0);
        this.setNationalGrade(0);
        this.setBaseMedicineFlag(0);
        this.setCreateUser(0l);
        this.setCreateTime(new Date());
        this.setUpdateUser(0l);
        this.setUpdateTime(new Date());
        this.setProvinceCode("");
        this.setCityCode("");
        this.setRegionCode("");
        this.setHideFlag(Constants.IS_NO);
        this.setProductGroupId(0l);
        this.setLockTime(DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
        this.setLastUnLockTime(DateUtil.parse("1970-01-01", "yyyy-MM-dd"));
        this.setProvinceManagerCode("");
        this.setProvinceManagerName("");
        this.setSubstituteRunning(1);
    }

}
