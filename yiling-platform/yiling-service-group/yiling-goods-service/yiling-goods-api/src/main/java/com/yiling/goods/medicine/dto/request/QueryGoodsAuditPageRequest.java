package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsAuditPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 生产厂家
     */
    private String manufacturer;


    /**
     * 数据来源：1pop平台 2商城平台 3ERP对接
     */
    private Integer source;
}
