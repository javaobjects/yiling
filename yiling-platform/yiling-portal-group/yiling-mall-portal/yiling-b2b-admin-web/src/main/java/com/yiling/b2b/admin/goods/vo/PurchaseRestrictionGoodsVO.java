package com.yiling.b2b.admin.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 PurchaseRestrictionGoodsVO
 * @描述
 * @创建时间 2022/12/7
 * @修改人 shichen
 * @修改时间 2022/12/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PurchaseRestrictionGoodsVO extends BaseVO {

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号", example = "Z109090")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家", example = "以岭")
    private String manufacturer;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String name;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "销售规格", example = "1片")
    private String sellSpecifications;

    /**
     * 商品状态：1上架 2下架
     */
    @ApiModelProperty(value = "商品状态：1上架 2下架", example = "1")
    private Integer goodsStatus;

    /**
     * 商品图片值
     */
    @ApiModelProperty(value = "商品图片值", example = "https://xxxx.xxxx.xxxx/goods.jpg")
    private String pic;


    /**
     * 每单限购数量 0为无限制
     */
    @ApiModelProperty(value = "每单限购数量 0为无限制", example = "1")
    private Long orderRestrictionQuantity;

    /**
     * 时间内限购数量 0为无限制
     */
    @ApiModelProperty(value = "时间内限购数量 0为无限制", example = "1")
    private Long timeRestrictionQuantity;

    /**
     * 限购时间类型 1自定义 2 每天 3 每周 4每月
     */
    @ApiModelProperty(value = "限购时间类型 1自定义 2 每天 3 每周 4每月", example = "1")
    private Integer timeType;

    /**
     * 限购开始时间
     */
    @ApiModelProperty(value = "限购开始时间", example = "1")
    private Date startTime;

    /**
     * 限购结束时间
     */
    @ApiModelProperty(value = "限购结束时间", example = "1")
    private Date endTime;


    @ApiModelProperty(value = "限购信息", example = "1")
    private PurchaseRestrictionVO purchaseRestrictionVO;
}
