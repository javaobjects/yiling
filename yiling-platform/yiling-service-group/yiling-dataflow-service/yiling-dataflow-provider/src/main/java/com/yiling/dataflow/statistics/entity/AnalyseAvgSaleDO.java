package com.yiling.dataflow.statistics.entity;

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
 * <p>
 * 商业公司每天平衡表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("analyse_avg_sale")
public class AnalyseAvgSaleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 时间
     */
    private String soTime;

    /**
     * 商业等级
     */
    private String businessLevel;

    /**
     * 商业组
     */
    private String businessGroup;

    /**
     * 商业名称
     */

    private String businessName;
    /**
     * 商品规格ID
     */
    private Long   specifications;

    /**
     * 近3天日平均销量
     */
    private BigDecimal avg3;
    /**
     * 近3天日平均销量
     */
    private BigDecimal avg7;
    /**
     * 商业组
     */
    private BigDecimal avg15;
    /**
     * 商业组
     */
    private BigDecimal avg30;
    /**
     * 商业组
     */
    private BigDecimal avg60;
    /**
     * 商业组
     */
    private BigDecimal avg90;
    /**
     * 商业组
     */
    private BigDecimal currentStock;
    /**
     * 商业组
     */
    private BigDecimal safetyStock;

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
