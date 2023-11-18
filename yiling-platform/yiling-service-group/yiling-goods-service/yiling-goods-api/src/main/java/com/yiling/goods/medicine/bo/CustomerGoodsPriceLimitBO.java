package com.yiling.goods.medicine.bo;

import lombok.Data;

/**
 * @author shichen
 * @类名 CustomerGoodsPriceLimitBO
 * @描述
 * @创建时间 2022/10/17
 * @修改人 shichen
 * @修改时间 2022/10/17
 **/
@Data
public class CustomerGoodsPriceLimitBO {
    private Long id;

    private Long goodsId;

    private Long cplId;

    private Long eid;

    private Long customerEid;
}
