package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺企业资质 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopCertificateVO extends BaseVO {

    /**
     * 店铺企业ID
     */
    @ApiModelProperty("店铺企业ID")
    private Long shopEid;

    /**
     * 店铺名称
     */
    @ApiModelProperty("企业名称")
    private String shopName;

    /**
     * 企业logo
     */
    @ApiModelProperty("企业logo")
    private String shopLogo;

    /**
     * 企业简介
     */
    @ApiModelProperty("企业简介")
    private String shopDesc;

    /**
     * 起配金额
     */
    @ApiModelProperty("起配金额")
    private BigDecimal startAmount;

    /**
     * 商品种类
     */
    @ApiModelProperty("商品种类")
    private Integer goodsKind;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    /**
     * 企业地址
     */
    @ApiModelProperty("企业地址")
    private String address;

    /**
     * 相关证件
     */
    @ApiModelProperty("相关证件")
    private List<EnterpriseCertificateVO> certificateVoList;



}
