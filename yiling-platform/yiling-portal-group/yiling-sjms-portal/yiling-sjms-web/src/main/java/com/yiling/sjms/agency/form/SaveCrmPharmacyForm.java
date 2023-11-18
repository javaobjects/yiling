package com.yiling.sjms.agency.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.yiling.framework.common.base.form.BaseForm;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmPharmacyForm extends BaseForm {
    @ApiModelProperty("ID")
    private Long id ;
    @ApiModelProperty("基础档案名称")
    private String crmEnterpriseName;
    /**
     * 主表主键
     */
    @ApiModelProperty("基础信息主键")
    private Long crmEnterpriseId;
    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty("药店属性 1-连锁分店；2-单体药店")
    @NotNull(message = "药店属性不能为空")
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
    @NotNull(message = "药店级别不能为空")
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
    @Length(max = 200, message = "上级公司名称限制200个字符")
    private String parentCompanyName;

    /**
     * 上级公司编码
     */
    @ApiModelProperty("上级公司编码")
    private String parentCompanyCode;

    /**
     * 是否医保 1-医保药店；2-慢保药店；3-否医保
     */
    @ApiModelProperty("是否医保 1-医保药店；2-慢保药店；3-否医保")
    @NotNull(message = "是否医保不能为空")
    private Integer medicalInsurance;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty("标签属性 1-社区店；2-商圈店；3-院边店；4-电商店")
    @NotNull(message = "标签属性不能为空")
    private Integer labelAttribute;

    /**
     * 是否目标 1-是；2-否
     */
    @ApiModelProperty("是否目标 1-是；2-否")
    @NotNull(message = "是否目标不能为空")
    private Integer targetFlag;

    /**
     * 店经理
     */
    @ApiModelProperty("店经理")
    @NotBlank(message = "店经理不能为空")
    @Length(max = 10, message = "店经理限制10个字符")
    private String storeManager;

    /**
     * 药店营业额
     */
    @ApiModelProperty("药店营业额")
    @Digits(integer = 10, fraction = 0, message = "药店营业额限制10位数字")
    private BigDecimal turnover;

    /**
     * 以岭年销售额
     */
    @ApiModelProperty("以岭年销售额")
    @Digits(integer = 10, fraction = 0, message = "以岭年销售额限制10位数字")
    private BigDecimal ylAnnualSales;

    /**
     * 营业面积
     */
    @ApiModelProperty("营业面积")
    @Digits(integer = 10, fraction = 0, message = "营业面积限制10位数字")
    private BigDecimal businessArea;

    /**
     * 县域客户代码
     */
    @ApiModelProperty("县域客户代码")
    @Length(max = 200, message = "县域客户代码限制200个字符")
    private String countyCustomerCode;

    /**
     * 县域客户名称
     */
    @ApiModelProperty("县域客户名称")
    @Length(max = 200, message = "县域客户名称限制200个字符")
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
    @Length(max = 50, message = "助记码限制50个字符")
    private String mnemonicCode;

    /**
     * 是否承包 1是 2否
     */
    @ApiModelProperty("是否承包 1是 2否")
    private Integer contractFlag;

    /**
     * 三者关系表单
     */
    @ApiModelProperty("三者关系表单")
    private List<SaveCrmRelationShipForm> crmRelationShip;
}
