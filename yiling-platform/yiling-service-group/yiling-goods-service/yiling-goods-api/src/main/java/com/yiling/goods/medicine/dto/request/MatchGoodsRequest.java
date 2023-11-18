package com.yiling.goods.medicine.dto.request;


import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MatchGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;
    /**
     * 售卖规格ID
     */
    private              Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;


    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;
}
