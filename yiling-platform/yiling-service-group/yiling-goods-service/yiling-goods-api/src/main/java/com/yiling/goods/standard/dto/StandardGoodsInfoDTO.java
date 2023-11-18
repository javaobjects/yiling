package com.yiling.goods.standard.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class StandardGoodsInfoDTO extends BaseDTO {

    private static final long serialVersionUID = -33371030421231608L;


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
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 分类
     */
    private String standardCategoryName;

    /**
     * 分类名称2
     */
    private String standardCategoryName1;

    /**
     * 分类名称2
     */
    private String standardCategoryName2;

    /**
     * 分类id1
     */
    private Long standardCategoryId1;

    /**
     * 分类id2
     */
    private Long standardCategoryId2;

    /**
     * 规格数量
     */
    private Integer specificationNumber;

    /**
     * 以岭标识 0:非以岭  1：以岭
     */
    private Integer ylFlag;

    private Long updateUser;

    private Date updateTime;

    private String userName;

    /**
     * 默认图片信息
     */
    private String pic;

}
