package com.yiling.dataflow.gb.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向团购申诉申请关联流向表
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_appeal_flow_related")
public class GbAppealFlowRelatedDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 申诉申请Id
     */
    private Long appealFormId;

    /**
     * 流向业务主键
     */
    private String flowKey;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 匹配数量
     */
    private BigDecimal matchQuantity;

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


}
