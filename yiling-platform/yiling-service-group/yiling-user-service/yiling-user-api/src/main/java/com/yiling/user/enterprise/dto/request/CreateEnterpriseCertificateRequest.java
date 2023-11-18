package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 修改企业资质 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
public class CreateEnterpriseCertificateRequest extends BaseRequest implements java.io.Serializable {

    private static final long serialVersionUID = -5064679810671137972L;

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
     * 资质有效期-至
     */
    private Date periodEnd;

    /**
     * 是否长期有效：0-否 1-是
     */
    private Integer longEffective;

}
