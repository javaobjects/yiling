package com.yiling.dataflow.report.entity;

import java.math.BigDecimal;
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
 * @date 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_flow_pop_purchase")
public class ReportFlowPopPurchaseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司id
     */
    private Long eid;

    /**
     * 商业公司名称
     */
    private String ename;

    /**
     * 销售时间
     */
    private Date poTime;

    /**
     * 销售数量
     */
    private Long erpQuantity;

    /**
     * 销售总额
     */
    private BigDecimal erpTotalAmount;

    /**
     * 销售数量
     */
    private Long poQuantity;

    /**
     * 销售总额
     */
    private BigDecimal poTotalAmount;

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
