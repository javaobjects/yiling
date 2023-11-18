package com.yiling.user.enterprise.entity;

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
 * 企业资质
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_certificate")
public class EnterpriseCertificateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
