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
 * 院外药店关系绑定表
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_hospital_drugstore_relation")
public class CrmHospitalDrugstoreRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 院外药店机构编码
     */
    private Long drugstoreOrgId;

    /**
     * 院外药店机构名称
     */
    private String drugstoreOrgName;

    /**
     * 医疗机构编码
     */
    private Long hospitalOrgId;

    /**
     * 医疗机构名称
     */
    private String hospitalOrgName;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 品种名称
     */
    private String categoryName;

    /**
     * 标准产品编码
     */
    private Long crmGoodsCode;

    /**
     * 标准产品名称
     */
    private String crmGoodsName;

    /**
     * 标准产品规格
     */
    private String crmGoodsSpec;

    /**
     * 开始生效时间
     */
    private Date effectStartTime;

    /**
     * 结束生效时间
     */
    private Date effectEndTime;

    /**
     * 是否停用 0-否 1-是
     */
    private Integer disableFlag;

    /**
     * 数据来源 1-导入数据 2-审批流数据
     */
    private Integer dataSource;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 操作人
     */
    private Long lastOpUser;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
