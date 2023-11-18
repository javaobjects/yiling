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
public class StandardGoodsSpecificationPicDTO extends BaseDTO {

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
     * 剂型
     */
    private String gdfName;

    /**
     * YPID编码
     */
    private String ypidCode;

    private String pic;

    private Long createUser;


    private Date createTime;

    /**
     * 备注
     */
    private String remark;

}
