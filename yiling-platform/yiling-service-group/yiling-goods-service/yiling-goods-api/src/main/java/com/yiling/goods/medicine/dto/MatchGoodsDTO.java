package com.yiling.goods.medicine.dto;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 进行比较的商品信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MatchGoodsDTO extends BaseDTO {
    private static final long serialVersionUID = 2448172915830529703L;

    /**
     * 商品名称，化学名
     */
    private String            name;

    /**
     * 匹配的通用名
     */
    private String            commonName;

    /**
     * 生产厂家
     */
    private String            manufacturer;
    /**
     * 规格
     */
    private String            specification;
    /**
     * 批准文号
     */
    private String            licenseno;

}
