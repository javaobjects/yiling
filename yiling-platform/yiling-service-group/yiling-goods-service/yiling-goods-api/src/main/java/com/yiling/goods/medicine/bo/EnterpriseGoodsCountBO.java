package com.yiling.goods.medicine.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: chen.shi
 * @date: 2021/12/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsCountBO implements java.io.Serializable{
    private static final long serialVersionUID = 8060378743103319174L;

    private Long eid;

    /**
     * 品格数量
     */
    private Long sellSpecificationCount;

    /**
     * 种类数量
     */
    private Long standardCount;
}
