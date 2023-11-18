package com.yiling.dataflow.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 辖区机构品类关系
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_enterprise_relation_manor")
public class CrmEnterpriseRelationManorDO extends BaseDO {

    private static final long serialVersionUID = 1L;
    /**
     * 辖区表Id
     */
    private Long crmManorId;

    /**
     * 机构编码
     */
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    private String crmEnterpriseName;
    /**、
     * 机构Crm编码
     */
    private String crmEnterpriseCode;


    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String categoryName;

    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String remark;


}
