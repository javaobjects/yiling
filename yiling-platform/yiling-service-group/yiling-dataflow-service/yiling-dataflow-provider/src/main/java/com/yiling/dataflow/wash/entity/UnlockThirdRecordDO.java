package com.yiling.dataflow.wash.entity;

import java.math.BigDecimal;
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
 * 小三批备案表
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("unlock_third_record")
public class UnlockThirdRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 客户机构编码
     */
    private Long orgCrmId;

    /**
     * 客户机构名称
     */
    private String customerName;

    /**
     * 月购进额度（万元）
     */
    private BigDecimal purchaseQuota;

    /**
     * 生效业务部门
     */
    private String effectiveDepartment;

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
