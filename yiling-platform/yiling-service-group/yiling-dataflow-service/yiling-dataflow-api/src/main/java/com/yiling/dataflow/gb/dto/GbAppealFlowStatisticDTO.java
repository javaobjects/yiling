package com.yiling.dataflow.gb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFlowStatisticDTO extends BaseDTO {

    /**
     * 流向业务主键
     */
    private String flowKey;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 数量
     */
    private BigDecimal soQuantity;

    /**
     * 匹配数量
     */
    private BigDecimal matchQuantity;

    /**
     * 未匹配数量
     */
    private BigDecimal unMatchQuantity;

    /**
     * 流向版本控制并发使用
     */
    private Long flowVersion;

}
