package com.yiling.user.agreement.dto.request;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.user.agreement.dto.request
 * @date: 2021/9/13
 */
@Data
@Accessors(chain = true)
public class SalePurchaseGoodsRequest implements Serializable {

    /**
     *  查询
     */
    private String keyWord;

    /**
     * 配送eid
     */
    private Long distributionEid;

    /**
     * 返利周期
     */
    private Integer rebateCycle;

    /**
     * 返利类型
     */
    private Integer  rebateType;

    /**
     * 采购商eid
     */
    private Long purchaseEid;


}
