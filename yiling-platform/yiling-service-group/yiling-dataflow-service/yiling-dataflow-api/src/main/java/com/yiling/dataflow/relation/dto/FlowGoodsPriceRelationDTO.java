package com.yiling.dataflow.relation.dto;

import java.math.BigDecimal;

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
public class FlowGoodsPriceRelationDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long oldGoodsCode;

    private String oldGoodsName;

    private String spec;

    private Long goodsCode;

    private String goodsName;

    private String customerCode;

    private String customer;

    private BigDecimal price;

    private Long specificationId;


}
