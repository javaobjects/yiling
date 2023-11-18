package com.yiling.open.erp.entity;

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
 * 流向封存实例表
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_flow_sealed")
public class ErpFlowSealedDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部
     */
    private Integer type;

    /**
     * 封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部
     */
    private Integer status;

    /**
     * 封存月份
     */
    private String month;

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
