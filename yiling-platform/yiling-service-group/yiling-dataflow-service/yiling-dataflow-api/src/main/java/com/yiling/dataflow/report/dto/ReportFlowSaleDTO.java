package com.yiling.dataflow.report.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

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
public class ReportFlowSaleDTO extends BaseDTO {

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
     * 商品品类:0连花1非连花
     */
    private Integer goodsCategory;

    /**
     * 销售时间
     */
    private Date soTime;

    /**
     * 销售数量
     */
    private Long soQuantity;

    /**
     * 商销价
     */
    private BigDecimal ylSalePrice;

    /**
     * 销售总额
     */
    private BigDecimal totalAmount;
}