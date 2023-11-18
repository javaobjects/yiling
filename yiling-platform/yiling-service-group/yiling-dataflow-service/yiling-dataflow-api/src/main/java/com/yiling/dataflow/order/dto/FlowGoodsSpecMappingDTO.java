package com.yiling.dataflow.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsSpecMappingDTO extends BaseDTO {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String spec;

    /**
     * 待匹配商品规格
     */
    private String manufacturer;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品名称+规格 id
     */
    private Long specificationId;

    private Long recommendSpecificationId;

    private String recommendGoods;

    private String recommendSpec;

    private Integer recommendScore;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
