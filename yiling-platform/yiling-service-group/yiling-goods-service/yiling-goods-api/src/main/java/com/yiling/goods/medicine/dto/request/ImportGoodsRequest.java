package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ImportGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 批准文号/生产许可证号
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
     * 包装规格
     */
    private String sellSpecifications;

    /**
     * 库存
     */
    private Long qty;

    /**
     * 挂网价（售价）
     */
    private BigDecimal price;

    /**
     * 是否拆零销售
     */
    private Integer canSplit;

    /**
     * 大件装数量
     */
    private Integer bigPackage;
}
