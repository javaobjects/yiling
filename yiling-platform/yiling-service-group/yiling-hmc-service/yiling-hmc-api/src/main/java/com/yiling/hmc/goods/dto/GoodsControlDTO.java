package com.yiling.hmc.goods.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsControlDTO
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsControlDTO extends BaseDTO {

    /**
     * 商品名称
     */
    private String name;
    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;
    /**
     * 标准库商品id
     */
    private Long standardId;
    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;
    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
