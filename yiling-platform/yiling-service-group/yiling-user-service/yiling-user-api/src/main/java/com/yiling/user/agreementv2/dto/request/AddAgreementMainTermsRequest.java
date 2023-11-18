package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议主条款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementMainTermsRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 协议编号
     */
    private String agreementNo;

    /**
     * 甲方ID
     */
    private Long eid;

    /**
     * 甲方名称
     */
    private String ename;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    private Integer firstType;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     */
    private Integer agreementType;

    /**
     * 协议流水号
     */
    private Integer agreementSerialNo;

    /**
     * 乙方ID
     */
    private Long secondEid;

    /**
     * 乙方名称
     */
    private String secondName;

    /**
     * 签订日期
     */
    private Date signTime;

    /**
     * 生效时间
     */
    private Date startTime;

    /**
     * 失效时间
     */
    private Date endTime;

    /**
     * 甲方协议签订人ID
     */
    private Long firstSignUserId;

    /**
     * 甲方协议签订人名称
     */
    private String firstSignUserName;

    /**
     * 甲方协议签订人手机号
     */
    private String firstSignUserPhone;

    /**
     * 甲方协议签订人所属部门ID
     */
    private Long firstSignDepartmentId;

    /**
     * 甲方协议签订人所属部门名称
     */
    private String firstSignDepartmentName;

    /**
     * 乙方协议签订人ID
     */
    private Long secondSignUserId;

    /**
     * 乙方协议签订人名称
     */
    private String secondSignUserName;

    /**
     * 乙方协议签订人手机号
     */
    private String secondSignUserPhone;

    /**
     * 乙方协议签订人所属部门ID
     */
    private Long secondSignDepartmentId;

    /**
     * 乙方协议签订人所属部门名称
     */
    private String secondSignDepartmentName;

    /**
     * 协议负责人：1-甲方联系人 2-乙方联系人
     */
    private Integer mainUser;

    /**
     * 是否提供流向：0-否 1-是，默认为是
     */
    private Integer flowFlag;

    /**
     * 是否为草签协议：0-否 1-是
     */
    private Integer draftFlag;

    /**
     * 是否交保证金：0-否 1-是
     */
    private Integer marginFlag;

    /**
     * 保证金金额
     */
    private BigDecimal marginAmount;

    /**
     * 保证金支付方： 2-乙方 3-指定商业公司
     */
    private Integer marginPayer;

    /**
     * 指定商业公司ID
     */
    private Long businessEid;

    /**
     * 保证金返还方式：1-协议结束后返还 2-指定日期返还
     */
    private Integer marginBackType;

    /**
     * 保证金返还日期
     */
    private Date marginBackDate;

    /**
     * 是否活动协议：0-否 1-是
     */
    private Integer activeFlag;

    /**
     * 商业运营签订人ID
     */
    private Long businessOperatorId;

    /**
     * 商业运营签订人名称
     */
    private String businessOperatorName;

    /**
     * KA协议类型：1-统谈统签，统一支付 2-统谈统签，分开支付 3-统谈分签，分开支付 4-单独签订
     */
    private Integer kaAgreementType;

    /**
     * 协议附件类型：1-协议原件 2-协议复印件
     */
    private Integer attachmentType;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回 4-已归档
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 审核拒绝原因
     */
    private String authRejectReason;

    /**
     * 归档编号
     */
    private String archiveNo;

    /**
     * 归档备注
     */
    private String archiveRemark;

    /**
     * 纸质件编号
     */
    private String paperNo;

    /**
     * 协议生效状态：1-正常 2-中止 3-作废
     */
    private Integer effectStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 协议附件集合
     */
    private List<AddAgreementAttachmentRequest> agreementAttachmentList;

    /**
     * 乙方关联子公司集合
     */
    private List<Long> secondSubEidList;

}
