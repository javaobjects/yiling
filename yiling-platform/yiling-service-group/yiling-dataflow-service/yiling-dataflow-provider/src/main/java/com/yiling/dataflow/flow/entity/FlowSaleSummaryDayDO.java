package com.yiling.dataflow.flow.entity;

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
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_sale_summary_day")
public class FlowSaleSummaryDayDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long   crmEnterpriseId;
    private String businessDepartment;
    private String commercialBusinessDepartment;
    private String commercialProvinceArea;
    private String commercialBusinessProvince;
    private String commercialAreaCode;
    private String commercialArea;
    private String commercialManagerCode;
    private String commercialManagerName;
    private String commercialRepresentativeCode;
    private String commercialRepresentativeName;
    private String customerTypeName;


    private Long eid;

    private String soId;

    /**
     * 流向销售ID
     */
    private Long flowSaleId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 年份(实际)
     */
    private String year;

    /**
     * 月份(实际)
     */
    private String month;

    /**
     * 商业编码
     */
    private String businessCode;

    /**
     * 商业名称
     */
    private String businessName;


    /**
     * 协议级别
     */
    private String agreementLevel;

    /**
     * 商业属性
     */
    private String commercialAttributes;

    /**
     * 连锁协议类型
     */
    private String chainAgreementType;

    /**
     * 销售类型
     */
    private String salesType;

    /**
     * 商业省份
     */
    private String commercialProvince;

    /**
     * 商业地市
     */
    private String commercialCity;

    /**
     * 商业区县
     */
    private String commercialCounty;

    /**
     * 原始机构名称
     */
    private String originalOrganizationName;

    /**
     * 机构编码
     */
    private String organizationCode;

    /**
     * 机构名称
     */
    private String organizationName;

    /**
     * 机构是否锁定
     */
    private String isLocked;

    /**
     * 机构部门
     */
    private String organizationDepartment;

    /**
     * 机构业务部门
     */
    private String organizationBusinessDepartment;

    /**
     * 机构省区
     */
    private String organizationProvinceArea;

    /**
     * 机构业务省区
     */
    private String organizationBusinessProvince;

    /**
     * 机构区办代码
     */
    private String organizationAreaCode;

    /**
     * 机构区办
     */
    private String organizationArea;

    /**
     * 主管工号
     */
    private String managerCode;

    /**
     * 主管姓名
     */
    private String managerName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

//    /**
//     * 岗位代码
//     */
//    private String postCode;
//
//    /**
//     * 岗位名称
//     */
//    private String postName;

    /**
     * 机构省份
     */
    private String institutionalProvince;

    /**
     * 城市代码
     */
    private String institutionalCityCode;

    /**
     * 机构地市
     */
    private String institutionalCity;

    /**
     * 区县代码
     */
    private String institutionalCountyCode;

    /**
     * 机构区县
     */
    private String institutionalCounty;

    /**
     * 终端类型
     */
    private String terminalType;

    /**
     * 终端类型二
     */
    private String terminalTypeTwo;

    /**
     * 机构级别
     */
    private String organizationLevel;

    /**
     * 机构国家级别
     */
    private String institutionalCountryLevel;

    /**
     * 机构是否连锁
     */
    private String isChain;

    /**
     * 机构连锁属性
     */
    private String chainAttribute;

    /**
     * 原始产品品名
     */
    private String originalProductName;

    /**
     * 原始产品品规
     */
    private String originalProductSpecification;

    /**
     * 品种
     */
    private String varieties;

    /**
     * 产品(SKU)编码
     */
    private Long crmGoodsCode;

    /**
     * 产品品名
     */
    private String crmGoodsName;

    /**
     * 产品品规
     */
    private String crmGoodsSpec;

    /**
     * 产品单价
     */
    private BigDecimal price;

    /**
     * 原始数量
     */
    private BigDecimal originalQuantity;

    /**
     * 原始单位
     */
    private String originalUnit;

    /**
     * 金额/元
     */
    private BigDecimal amountPrice;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 原始单价
     */
    private BigDecimal originalPrice;

    /**
     * 标记信息
     */
    private String signMsg;

    /**
     * 可能错误信息
     */
    private Integer goodsPossibleError;

    /**
     * 终端客户类型
     */
    private Integer terminalCustomerType;

    private String  departmentLabel;
    private String  judgmentLabel;
    private String  remarkLabel;
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
