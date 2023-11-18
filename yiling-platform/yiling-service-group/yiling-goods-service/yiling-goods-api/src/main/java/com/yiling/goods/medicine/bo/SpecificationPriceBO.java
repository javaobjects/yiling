package com.yiling.goods.medicine.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author shichen
 * @类名 SpecificationPriceBO
 * @描述
 * @创建时间 2023/5/5
 * @修改人 shichen
 * @修改时间 2023/5/5
 **/
@Data
public class SpecificationPriceBO implements Serializable {

    private Long sellSpecificationId;

    private BigDecimal price;
}
