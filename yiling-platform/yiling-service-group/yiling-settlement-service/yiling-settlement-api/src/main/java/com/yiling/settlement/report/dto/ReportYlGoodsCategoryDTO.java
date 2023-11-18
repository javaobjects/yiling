package com.yiling.settlement.report.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 以岭商品分类dto
 * @author: dexi.yao
 * @date: 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportYlGoodsCategoryDTO extends BaseDTO {

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 子参数id
     */
    private Long paramSubId;

    /**
     * 对应以岭的商品id
     */
    private Long ylGoodsId;

    /**
     * 名称
     */
    private String name;
}
