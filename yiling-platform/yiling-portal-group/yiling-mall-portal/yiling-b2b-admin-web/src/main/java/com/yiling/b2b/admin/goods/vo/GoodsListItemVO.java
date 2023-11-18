package com.yiling.b2b.admin.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsListItemVO extends BaseVO {


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
     * 销售规格
     */
    @ApiModelProperty(value = "自己公司销售规格", example = "1片")
    private String specifications;

    /**
     * 标准库Id
     */
    @ApiModelProperty(value = "标准库Id")
    private Long standardId;

    /**
     * 销售规格Id
     */
    @ApiModelProperty(value = "销售规格Id")
    private Long sellSpecificationsId;

    /**
     * 专利类型 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 1-非专利 2-专利")
    private Integer isPatent;
    /**
     * 商品状态：1上架 2下架 5待审核 6驳回
     */
    @ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回", example = "1")
    private Integer goodsStatus;

    /**
     * 挂网价
     */
    @ApiModelProperty(value = "挂网价", example = "1.11")
    private BigDecimal price;

    /**
     * 商品图片值
     */
    @ApiModelProperty(value = "商品图片值", example = "https://xxxx.xxxx.xxxx/goods.jpg")
    private String picUrl;

    /**
     * 商品图片值
     */
    @ApiModelProperty(value = "商品图片值", example = "https://xxxx.xxxx.xxxx/goods.jpg")
    private String pic;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    @ApiModelProperty("商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品")
    private Integer goodsType;

    /**
     * 供应商id
     */
    @ApiModelProperty("供应商id")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String ename;

    /**
     * 销售包装
     */
    @ApiModelProperty(value = "销售包装")
    private List<GoodsSkuVO> goodsSkuList;

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;

    @ApiModelProperty("总库存数量（单位）")
    private Long count;

    @ApiModelProperty("商品id--营销活动使用")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String goodsName;

    @ApiModelProperty(value = "是否以岭品", example = "true")
    private Boolean yilingGoodsFlag;
}
