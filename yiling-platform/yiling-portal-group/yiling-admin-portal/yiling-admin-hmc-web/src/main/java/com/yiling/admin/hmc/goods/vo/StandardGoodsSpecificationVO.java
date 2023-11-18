package com.yiling.admin.hmc.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsSpecificationVO extends BaseVO {


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
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;


}
