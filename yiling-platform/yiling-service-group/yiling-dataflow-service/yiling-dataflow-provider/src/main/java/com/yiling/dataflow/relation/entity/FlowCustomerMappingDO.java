package com.yiling.dataflow.relation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_customer_mapping")
public class FlowCustomerMappingDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 流向过来的客户名称
     */
    private String flowCustomerName;

    /**
     * CRM内码+CRM类型码
     */
    private String innerCode;

    /**
     * CRM 中标准名称
     */
    private String innerName;

    /**
     * 商业公司CRM内码
     */
    private String businessCode;

    /**
     * 商业公司名称（流向来源）
     */
    private String fromBusiness;

    private String businessTypeName;

    /**
     * 类型名称
     */
    private String customerTypeName;

    /**
     * 归属部门
     */
    private String belongDepartment;

    /**
     * 锁定类型：0 非锁 ， 1 锁定
     */
    private Integer lockType;

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


}
