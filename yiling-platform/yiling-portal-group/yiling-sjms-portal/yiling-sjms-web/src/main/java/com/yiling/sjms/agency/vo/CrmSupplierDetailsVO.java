package com.yiling.sjms.agency.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmSupplierDetailsVO extends CrmAgencyDetailsVO{
    /**
     * 主表主键
     */
    @ApiModelProperty("基础信息主键")
    private Long crmEnterpriseId;

    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty("商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商")
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty("商业属性 1-城市商业、2-县级商业")
    private Integer supplierAttribute;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty("普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议")
    private Integer generalMedicineLevel;

    /**
     * 是否连锁总部 1是2否
     */
    @ApiModelProperty("是否连锁总部 1是2否")
    private Integer headChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty("连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁")
    private Integer chainAttribute;

    /**
     * 连锁KA 1是2否
     */
    @ApiModelProperty("连锁KA 1是2否")
    private Integer chainKaFlag;

    /**
     * 上级公司名称
     */
    @ApiModelProperty("上级公司名称")
    private String parentSupplierName;

    /**
     * 上级公司编码
     */
    @ApiModelProperty("上级公司编码")
    private String parentSupplierCode;

    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty("商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型")
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty("基药商信息 1-基药配送商、2-基药促销商")
    private Integer baseSupplierInfo;

    /**
     * 流向打取人业务部门
     */
    @ApiModelProperty("流向打取人业务部门")
    private String department;

    /**
     * 流向打取人业务省区
     */
    @ApiModelProperty("流向打取人业务省区")
    private String provincialArea;

    /**
     * 流向打取人工号
     */
    @ApiModelProperty("流向打取人工号")
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    @ApiModelProperty("流向打取人姓名")
    private String flowLiablePerson;

    /**
     * 商务负责人工号
     */
    @ApiModelProperty("商务负责人工号")
    private String commerceJobNumber;

    /**
     * 商务负责人姓名
     */
    @ApiModelProperty("商务负责人姓名")
    private String commerceLiablePerson;

    /**
     * 是否重点商业1是2否
     */
    @ApiModelProperty("是否重点商业1是2否")
    private Integer supplierImportFlag;

    /**
     * 专利药协议类型1-全产品一级(六大专利药产品)、2-专利一级(六大专利药产品之一)、OTC直供(连花36粒)、3-全产品二级(六大专利药产品)、4-专利二级(六大专利药产品之一)、5-不签协议
     */
    @ApiModelProperty("专利药协议类型1-全产品一级(六大专利药产品)、2-专利一级(六大专利药产品之一)、OTC直供(连花36粒)、3-全产品二级(六大专利药产品)、4-专利二级(六大专利药产品之一)、5-不签协议")
    private Integer patentAgreementType;

    /**
     * 是否捆绑普药 1是2否
     */
    @ApiModelProperty("是否捆绑普药 1是2否")
    private Integer bindGeneralMedicineFlag;

    /**
     * 普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、3-KA连锁模式
     */
    @ApiModelProperty("普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、3-KA连锁模式")
    private Integer generalMedicineSaleType;

    /**
     * 助记码
     */
    @ApiModelProperty("助记码")
    private String mnemonicCode;

    /**
     * 是否承包1是2否
     */
    @ApiModelProperty("是否承包1是2否")
    private Integer contractFlag;

    /**
     * 是否有自己的下属连锁1是2否
     */
    @ApiModelProperty("是否有自己的下属连锁1是2否")
    private Integer chainSubordinateFlag;

    /**
     * 托管类型: 1-行业托管2-终端托管3-区域托管
     */
    @ApiModelProperty("托管类型: 1-行业托管2-终端托管3-区域托管")
    private Integer trusteeshipType;

    /**
     * 商业体系分类: 1-全国体系2-省内体系3-其他体系
     */
    @ApiModelProperty("商业体系分类: 1-全国体系2-省内体系3-其他体系")
    private Integer businessSystemCategory;
    @ApiModelProperty("商业体系")
    private Integer businessSystem;
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
     * 收获地址
     */
    @ApiModelProperty("收获地址")
    private String supplierAddress;



    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("三者关系")
    private List<CrmRelationshiplDetailVO> relationshiplDetail;
    /**
     * 锁定类型: 1-打单2-销售
     */
    @ApiModelProperty("锁定类型")
    private Integer lockType;
}
