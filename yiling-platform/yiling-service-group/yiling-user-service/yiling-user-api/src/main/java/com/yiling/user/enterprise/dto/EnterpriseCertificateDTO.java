package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业资质 DTO
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCertificateDTO extends BaseDTO {

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
