package com.yiling.dataflow.flow.entity;

import java.math.BigDecimal;
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
 * @类名 FlowEnterpriseCustomerMappingDO
 * @描述 客户对照关系
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_enterprise_customer_mapping")
public class FlowEnterpriseCustomerMappingDO extends BaseDO {
    /**
     * 流向客户名称
     */
    private String flowCustomerName;

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
     * 推荐客户crmId
     */
    private Long recommendOrgCrmId;

    /**
     * 推荐客户名称
     */
    private String recommendOrgName;

    /**
     * 推荐分数
     */
    private BigDecimal recommendScore;

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
