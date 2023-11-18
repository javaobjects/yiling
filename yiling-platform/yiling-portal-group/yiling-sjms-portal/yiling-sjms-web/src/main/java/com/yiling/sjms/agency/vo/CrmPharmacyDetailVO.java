package com.yiling.sjms.agency.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 零售机构档案详细信息
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmPharmacyDetailVO extends CrmAgencyDetailsVO {

    /**
     * 机构基本信息id
     */
    @ApiModelProperty("机构基本信息id")
    private Long crmEnterpriseId;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty("药店属性 1-连锁分店；2-单体药店")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @ApiModelProperty("药店类型 1-直营；2-加盟")
    private Integer pharmacyType;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty("药店级别 1-A级；2-B级；3-C级")
    private Integer pharmacyLevel;

    /**
     * 是否协议 1-是；2-否
     */
    @ApiModelProperty("是否协议 1-是；2-否")
    private Integer agreement;

    /**
     * 上级公司名称
     */
    @ApiModelProperty("上级公司名称")
    private String parentCompanyName;

    /**
     * 上级公司编码
     */
    @ApiModelProperty("上级公司编码")
    private String parentCompanyCode;

    /**
     * 连锁属性
     */
    @ApiModelProperty("连锁属性")
    private Integer chainAttribute;

    /**
     * 是否医保 1-医保药店；2-慢保药店；3-否医保
     */
    @ApiModelProperty("是否医保 1-医保药店；2-慢保药店；3-否医保")
    private Integer medicalInsurance;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty("标签属性 1-社区店；2-商圈店；3-院边店；4-电商店")
    private Integer labelAttribute;

    /**
     * 是否目标 1-是；2-否
     */
    @ApiModelProperty("是否目标 1-是；2-否")
    private Integer targetFlag;

    /**
     * 店经理
     */
    @ApiModelProperty("店经理")
    private String storeManager;

    /**
     * 药店营业额
     */
    @ApiModelProperty("药店营业额")
    private BigDecimal turnover;

    /**
     * 以岭年销售额
     */
    @ApiModelProperty("以岭年销售额")
    private BigDecimal ylAnnualSales;

    /**
     * 营业面积
     */
    @ApiModelProperty("营业面积")
    private BigDecimal businessArea;

    /**
     * 县域客户代码
     */
    @ApiModelProperty("县域客户代码")
    private String countyCustomerCode;

    /**
     * 县域客户名称
     */
    @ApiModelProperty("县域客户名称")
    private String countyCustomerName;

    /**
     * 是否平价 1-是；2-否
     */
    @ApiModelProperty("是否平价 1-是；2-否")
    private Integer parity;

    /**
     * 是否基药终端 1是 2否
     */
    @ApiModelProperty("是否基药终端 1是 2否")
    private Integer baseMedicineFlag;

    /**
     * 助记码
     */
    @ApiModelProperty("助记码")
    private String mnemonicCode;

    /**
     * 是否承包 1是 2否
     */
    @ApiModelProperty("是否承包 1是 2否")
    private Integer contractFlag;

//    /**
//     * 零售机构档案扩展信息
//     */
//    private CrmPharmacyVO crmPharmacyVO;

    @ApiModelProperty("三者关系")
    private List<CrmRelationshiplDetailVO> relationshipDetail;
    /**
     * 首次锁定时间
     */
    private Date firstLockTime;
    /**
     * 最后一次解锁时间
     */
    private Date lastUnlockTime;
}
