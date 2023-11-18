package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业资质副本信息 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCertificateAuthInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业副本ID
     */
    private Long enterpriseAuthId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 资质类型（参见EnterpriseCertificateTypeEnum）
     */
    private Integer type;

    /**
     * 资质文件KEY
     */
    private String fileKey;

    /**
     * 资质有效期-起
     */
    private Date periodBegin;

    /**
     * 资质有效期-止
     */
    private Date periodEnd;

    /**
     * 是否长期有效：0-否 1-是
     */
    private Integer longEffective;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
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
     * 审核驳回原因
     */
    private String authRejectReason;

}
