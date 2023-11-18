package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long id;

    private Long sellSpecificationsId;

    private Long  standardId;

    private Long eid;

    private Integer goodsType;

    private Integer isCn;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产厂家地址
     */
    private String manufacturerAddress;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 规格单位
     */
    private String unit;

    /**
     * 中包装
     */
    private Long middlePackage;

    /**
     * 大包装
     */
    private Long bigPackage;

    /**
     * 是否拆包销售：1可拆0不可拆
     */
    private Integer canSplit;

    private String inSn;

    private String sn;

    private BigDecimal price;

    private Long qty;

    /**
     * 备注
     */
    private String remark;

    /**
     * 产品线
     */
    private SaveGoodsLineRequest goodsLineInfo;

}
