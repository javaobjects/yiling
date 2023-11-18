package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchAddCustomerGoodsLimitRequest extends BaseRequest {

    /**
     * 商业主体
     */
    private List<Long> eidList;

    private Long eid;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 生产厂家(全模糊搜索）
     */
    private String manufacturer;


    private Long customerEid;

}
