package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveErpFlowGoodsConfigRequest extends BaseRequest {

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    private String goodsInSn;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 批准文号（注册证号）
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

}
