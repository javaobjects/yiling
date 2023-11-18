package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议主条款导入 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-18
 */
@Data
@ApiModel("协议主条款导入VO")
@Accessors(chain = true)
public class AgreementImportMainTermsVO extends BaseVO implements Serializable {

    /**
     * 协议编号
     */
    @ApiModelProperty("协议编号")
    private String agreementNo;

    /**
     * 甲方ID
     */
    @ApiModelProperty(value = "甲方ID")
    private Long eid;

    /**
     * 甲方名称
     */
    @ApiModelProperty(value = "甲方名称")
    private String ename;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商（枚举：AgreementFirstTypeEnum）
     */
    @ApiModelProperty(value = "甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商（见字典：agreement_first_type）")
    private Integer firstType;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（枚举：AgreementTypeEnum）
     */
    @ApiModelProperty(value = "协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（见字典：agreement_type）")
    private Integer agreementType;

    /**
     * 乙方ID
     */
    @ApiModelProperty(value = "乙方ID")
    private Long secondEid;

    /**
     * 乙方名称
     */
    @ApiModelProperty(value = "乙方名称")
    private String secondName;

    /**
     * 签订日期
     */
    @ApiModelProperty(value = "签订日期")
    private Date signTime;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private Date startTime;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private Date endTime;

    /**
     * 甲方协议签订人ID
     */
    @ApiModelProperty(value = "甲方协议签订人ID")
    private Long firstSignUserId;

    /**
     * 甲方协议签订人名称
     */
    @ApiModelProperty(value = "甲方协议签订人名称")
    private String firstSignUserName;


    /**
     * 甲方协议签订人手机号
     */
    @ApiModelProperty(value = "甲方协议签订人手机号")
    private String firstSignUserPhone;

    /**
     * 乙方协议签订人ID
     */
    @ApiModelProperty(value = "乙方协议签订人ID")
    private Long secondSignUserId;

    /**
     * 乙方协议签订人名称
     */
    @ApiModelProperty(value = "乙方协议签订人名称")
    private String secondSignUserName;

    /**
     * 乙方协议签订人手机号
     */
    @ApiModelProperty(value = "乙方协议签订人手机号")
    private String secondSignUserPhone;

    /**
     * 协议负责人：1-甲方联系人 2-乙方联系人
     */
    @ApiModelProperty(value = "协议负责人：1-甲方联系人 2-乙方联系人")
    private Integer mainUser;

    /**
     * 协议负责人名称
     */
    @ApiModelProperty(value = "协议负责人名称")
    private String mainUserName;

    /**
     * 是否提供流向：0-否 1-是，默认为是
     */
    @ApiModelProperty(value = "是否提供流向：0-否 1-是，默认为是")
    private Integer flowFlag;

    /**
     * 是否为草签协议：0-否 1-是
     */
    @ApiModelProperty(value = "是否为草签协议：0-否 1-是")
    private Integer draftFlag;

    /**
     * 是否交保证金：0-否 1-是
     */
    @ApiModelProperty(value = "是否交保证金：0-否 1-是")
    private Integer marginFlag;

    /**
     * 保证金金额
     */
    @ApiModelProperty(value = "保证金金额")
    private BigDecimal marginAmount;

    /**
     * 保证金支付方： 2-乙方 3-指定商业公司
     */
    @ApiModelProperty(value = "保证金支付方： 2-乙方 3-指定商业公司")
    private Integer marginPayer;

    /**
     * 指定商业公司ID
     */
    @ApiModelProperty(value = "指定商业公司ID")
    private Long businessEid;

    /**
     * 指定商业公司名称
     */
    @ApiModelProperty(value = "指定商业公司名称")
    private String businessName;

    /**
     * 保证金返还方式：1-协议结束后返还 2-指定日期返还
     */
    @ApiModelProperty(value = "保证金返还方式：1-协议结束后返还 2-指定日期返还")
    private Integer marginBackType;

    /**
     * 保证金返还日期
     */
    @ApiModelProperty(value = "保证金返还日期")
    private Date marginBackDate;

    /**
     * 是否活动协议：0-否 1-是
     */
    @ApiModelProperty(value = "是否活动协议：0-否 1-是")
    private Integer activeFlag;

    /**
     * 商业运营签订人ID
     */
    @ApiModelProperty(value = "商业运营签订人ID")
    private Long businessOperatorId;

    /**
     * 商业运营签订人名称
     */
    @ApiModelProperty(value = "商业运营签订人名称")
    private String businessOperatorName;

    /**
     * KA协议类型：1-统谈统签，统一支付 2-统谈统签，分开支付 3-统谈分签，分开支付 4-单独签订
     */
    @ApiModelProperty(value = "KA协议类型：1-统谈统签，统一支付 2-统谈统签，分开支付 3-统谈分签，分开支付 4-单独签订")
    private Integer kaAgreementType;

    /**
     * 协议附件集合
     */
    @ApiModelProperty(value = "协议附件集合")
    private List<AgreementAttachmentVO> agreementAttachmentList;

}
