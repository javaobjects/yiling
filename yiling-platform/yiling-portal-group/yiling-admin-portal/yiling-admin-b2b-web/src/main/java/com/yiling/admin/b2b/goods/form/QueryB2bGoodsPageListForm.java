package com.yiling.admin.b2b.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryB2bGoodsPageListForm extends QueryPageListForm {

    /**
     * 商业主体
     */
    @ApiModelProperty(value = "商业主体")
    private List<Long> eidList;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    /**
     * 商品状态：1上架 2下架 3待设置
     */
    @ApiModelProperty(value = "商品状态：0全部,1上架 2下架 3待设置")
    private Integer goodsStatus;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String goodsName;

    @ApiModelProperty(value = "营销活动id")
    private Long promotionActivityId;

    /**
     * 应用来源 1-营销活动
     */
    @ApiModelProperty(value = "应用来源 1-营销活动", example = "1")
    private Integer from;

    @ApiModelProperty(value = "活动类型 1-满赠，2-特价，3-秒杀, 4-组合包")
    private Integer type;

    @ApiModelProperty(value = "活动分类（1-平台；2-商家；）")
    private Integer sponsorType;

    /**
     * 以岭商品标记 0-全部 1-以岭品 2-非以岭品
     */
    @ApiModelProperty(value = "以岭商品标记 0-全部 1-以岭品 2-非以岭品")
    private Integer yilingGoodsFlag;

    /**
     * 可用库存是否大于零 0全部库存 1可用库存大于0
     */
    @ApiModelProperty(value = "0全部库存 1可用库存大于0")
    private Integer isAvailableQty;

    /**
     * 限价需要用的客户id
     */
    @ApiModelProperty(value = "客户ID")
    private Long customerEid;
}
