package com.yiling.open.cms.goods.vo;

import com.yiling.framework.common.base.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 规格信息
 *
 * @author: fan.shen
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsSpecificationVO extends BaseVO {

    private static final long serialVersionUID = -333712190231608L;

    /**
     * 标准商品ID
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
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * YPID编码
     */
    private String ypidCode;


}
