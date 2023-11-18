package com.yiling.dataflow.flow.entity;

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
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingDO
 * @描述 配送商对照关系表
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_enterprise_supplier_mapping")
public class FlowEnterpriseSupplierMappingDO extends BaseDO {
    /**
     * 原始配送商名称
     */
    private String flowSupplierName;

    /**
     * 标准机构编码
     */
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    private String orgName;

    /**
     * 标准机构社会信用代码
     */
    private String orgLicenseNumber;
    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 最后上传时间
     */
    private Date lastUploadTime;

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
