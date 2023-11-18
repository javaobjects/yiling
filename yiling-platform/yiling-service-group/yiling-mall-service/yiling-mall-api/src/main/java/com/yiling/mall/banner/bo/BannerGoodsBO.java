package com.yiling.mall.banner.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * banner商品表
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
public class BannerGoodsBO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * bannerID
     */
    private Long bannerId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 排序
     */
    private Integer sort;

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

    /**
     * 商品图片值
     */
    private String pic;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 注册证号（批准文号）
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 挂网价
     */
    private BigDecimal price;

}
