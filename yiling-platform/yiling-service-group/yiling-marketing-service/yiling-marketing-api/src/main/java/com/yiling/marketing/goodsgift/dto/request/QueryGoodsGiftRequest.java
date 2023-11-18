package com.yiling.marketing.goodsgift.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 赠品库查询条件
 * @author:wei.wang
 * @date:2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsGiftRequest extends BaseRequest {

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品编号
     */
    private Long id;

    /**
     * 类型（1-平台；2-商家）
     */
    private Integer sponsorType;

    /**
     * 所属业务（0-全部；1-2b；2-2c
     */
    private Integer businessType;

    /**
     * （1-真实物品；2-虚拟物品；3-优惠券；4-会员）
     */
    private List<Integer> goodsType;
}
